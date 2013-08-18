package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.sports.model.Leg;
import com.alexecollins.openbookmaker.sports.model.Outcome;
import com.alexecollins.openbookmaker.sports.model.Part;
import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Slf4j
public class LiabilityService implements MessageListener {
	private final Map<Outcome, AtomicReference<BigDecimal>> liabilities = new ConcurrentHashMap<>();
	private final MathContext mathContext = new MathContext(2, RoundingMode.HALF_UP);

	@Override
	public void onMessage(Message message) {
		log.info("accepting message");
		BetPlacement placement;
		try {
			placement = (BetPlacement) ((ObjectMessage) message).getObject();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}

		log.info("recomputing liabilities for bet {}", placement.getUuid());

		BigDecimal liability = placement.getBet().getStake();
		int n = 0;
		for (Leg leg : placement.getBet().getLegs()) {
			for (Part part : leg.getParts()) {
				liability = liability.multiply(
						part.getPriceStrategy().getPrice(System.currentTimeMillis()).getValue());
				n++;
			}
		}

		liability = liability.divide(BigDecimal.valueOf(n), mathContext);

		for (Leg leg : placement.getBet().getLegs()) {
			for (Part part : leg.getParts()) {
				final Outcome outcome = part.getOutcome();
				if (!liabilities.containsKey(outcome)) {
					liabilities.put(outcome, new AtomicReference<>(BigDecimal.ZERO));
				}
				final AtomicReference<BigDecimal> newSum = liabilities.get(outcome);

				BigDecimal oldVal;
				do {
					oldVal = newSum.get();
				} while (!newSum.compareAndSet(oldVal, oldVal.add(liability)));

				log.info("liability on {} is {}", outcome, liabilities.get(outcome));
			}
		}
	}

	public BigDecimal getLiability(Outcome outcome) {
		return liabilities.containsKey(outcome) ? liabilities.get(outcome).get() : BigDecimal.ZERO;
	}
}
