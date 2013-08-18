package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.jms.JmsTopicTemplate;
import com.alexecollins.openbookmaker.repo.Repo;
import lombok.extern.slf4j.Slf4j;

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
	private final JmsTopicTemplate jmsTemplate;

	public BetAcceptorService(Repo<BetPlacement> betPlacementRepo, JmsTopicTemplate jmsTemplate) {
		this.repo = betPlacementRepo;
		this.jmsTemplate = jmsTemplate;
	}

	@Override
	public void onMessage(Message message) {
		log.info("accepting message");

		final ObjectMessage placedMessage = (ObjectMessage) message;

		try {
			final BetPlacement placement = (BetPlacement) placedMessage.getObject();
			log.info("processing {}", placement.getUuid());

			placement.setStatus(BetPlacement.Status.COMMITTED);

			repo.add(placement.getUuid(), placement);

			log.info("committed {}", placement.getUuid());

			jmsTemplate.send(placement);

		} catch (JMSException | IOException e) {
			log.warn("failed to process {}: {}", message, e);
		} finally {
			processed.incrementAndGet();
		}
	}

	public long getProcessed() {
		return processed.get();
	}
}
