package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.repo.Repo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.*;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Slf4j
public class BetAcceptorService implements MessageListener {

	private final AtomicLong processed = new AtomicLong();
	private final Repo<BetPlacement> repo;
	private final QueueConnection topicConnection;
	private final Queue betQueue;
	private final ThreadLocal<QueueSession> session = new ThreadLocal<QueueSession>() {
		@Override
		protected QueueSession initialValue() {
			try {
				return topicConnection.createQueueSession(false, TopicSession.AUTO_ACKNOWLEDGE);
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		}
	};

	@Autowired
	public BetAcceptorService(Repo<BetPlacement> betPlacementRepo, QueueConnectionFactory topicConnection, Queue betQueue) throws NamingException, JMSException {
		this.repo = betPlacementRepo;
		this.topicConnection = topicConnection.createQueueConnection();
		this.betQueue = betQueue;
	}

	@PostConstruct
	public void start() throws JMSException {
		session.get().createReceiver(betQueue).setMessageListener(this);
		topicConnection.start();
	}

	@PreDestroy
	public void stop() throws JMSException {
		topicConnection.stop();
		topicConnection.close();
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
