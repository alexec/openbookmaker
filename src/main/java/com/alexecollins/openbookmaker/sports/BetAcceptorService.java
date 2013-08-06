package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.repo.Repo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Slf4j
public class BetAcceptorService implements MessageListener {

	private final AtomicLong processed = new AtomicLong();
	private final Repo<BetPlacement> repo;
	private final JmsTemplate jmsTemplate;

	@Autowired
	public BetAcceptorService(Repo<BetPlacement> betPlacementRepo, @Qualifier("acceptedBetsJmsTemplate") JmsTemplate jmsTemplate) {
		this.repo = betPlacementRepo;
		this.jmsTemplate = jmsTemplate;
	}

	@Override
	public void onMessage(Message message) {

		final ObjectMessage placedMessage = (ObjectMessage) message;

		try {
			final BetPlacement placement = (BetPlacement) placedMessage.getObject();
			log.info("processing  " + placement.getUuid());

			placement.setStatus(BetPlacement.Status.COMMITTED);

			repo.add(placement.getUuid(), placement);

			log.info("committed " + placement.getUuid());

			jmsTemplate.send(new MessageCreator() {
				@Override
				public Message createMessage(Session session) throws JMSException {
					final ObjectMessage accepted = session.createObjectMessage();
					accepted.setObject(placement);
					return accepted;
				}
			});

		} catch (JMSException | IOException e) {
			log.warn("failed to process " + message, e);
		} finally {
			processed.incrementAndGet();
		}
	}

	public long getProcessed() {
		return processed.get();
	}
}
