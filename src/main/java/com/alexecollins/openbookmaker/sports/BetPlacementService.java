package com.alexecollins.openbookmaker.sports;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class BetPlacementService {

	private final Queue queue;
	private final QueueConnection queueConnection;
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

	public BetPlacementService() throws NamingException, JMSException {
		InitialContext ctx = new InitialContext();

		queueConnection = ((QueueConnectionFactory) ctx.lookup("ConnectionFactory")).createQueueConnection();
		queue = (Queue) ctx.lookup("Bets");
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
			sender = session.get().createSender(queue);
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

	public void stop() {
		try {
			queueConnection.start();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public void start() {
		try {
			queueConnection.stop();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
}
