package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.repo.Repo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Slf4j
public class BetAcceptorService implements MessageListener {

	private final AtomicLong processed = new AtomicLong();
	private final Repo<BetPlacement> repo;

	@Autowired
	public BetAcceptorService(Repo<BetPlacement> betPlacementRepo) {
		this.repo = betPlacementRepo;
	}

	@Override
	public void onMessage(Message message) {
		log.info("processing  " + message);

		final ObjectMessage objectMessage = (ObjectMessage) message;

		try {
			final BetPlacement placement = (BetPlacement) objectMessage.getObject();

			placement.setStatus(BetPlacement.Status.COMMITTED);

			repo.add(placement.getUuid(), placement);

			log.info("committed " + placement.getUuid());

		} catch (JMSException | IOException e) {
			log.warn("failed to process " + message);
		} finally {
			processed.incrementAndGet();
		}
	}

	public long getProcessed() {
		return processed.get();
	}
}
