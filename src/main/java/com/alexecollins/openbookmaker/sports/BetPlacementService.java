package com.alexecollins.openbookmaker.sports;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.*;
import javax.naming.NamingException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class BetPlacementService {

	private final QueueConnection queueConnection;
	private final Queue betQueue;
	private final ThreadLocal<QueueSession> session = new ThreadLocal<QueueSession>() {
		@Override
		protected QueueSession initialValue() {
			try {
				return queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		}
	};

	@Autowired
	public BetPlacementService(QueueConnectionFactory queueConnection, Queue betQueue) throws NamingException, JMSException {
		this.queueConnection = queueConnection.createQueueConnection();
		this.betQueue = betQueue;
	}

	public void place(final BetPlacement placement) throws BetPlacementFailedException {
		final ObjectMessage message;
		try {
			message = session.get().createObjectMessage();
			message.setObject(placement);
		} catch (JMSException e) {
			throw new BetPlacementFailedException(e);
		}

		final QueueSender sender;
		try {
			sender = session.get().createSender(betQueue);
		} catch (JMSException e) {
			throw new BetPlacementFailedException(e);
		}
		try {
			sender.send(message);
		} catch (JMSException e) {
			throw new BetPlacementFailedException(e);
		} finally {
			try {
				sender.close();
			} catch (JMSException ignored) {
			}
		}

		placement.setStatus(BetPlacement.Status.PENDING);

	}

	@PostConstruct
	public void start() throws JMSException {
		queueConnection.start();

	}

	@PreDestroy
	public void stop() throws JMSException {
		queueConnection.stop();
		queueConnection.close();
	}

}
