package com.alexecollins.openbookmaker.sports;


import com.alexecollins.openbookmaker.jms.JmsQueueTemplate;
import lombok.extern.slf4j.Slf4j;

import javax.jms.JMSException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Slf4j
public class BetPlacementService {

	private final JmsQueueTemplate jmsTemplate;

	public BetPlacementService(JmsQueueTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void place(final BetPlacement placement) throws BetPlacementFailedException {
		log.info("placing {}", placement);

		try {
			jmsTemplate.send(placement);
		} catch (JMSException e) {
			throw new BetPlacementFailedException(e);
		}

		placement.setStatus(BetPlacement.Status.PENDING);

	}
}
