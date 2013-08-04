package com.alexecollins.openbookmaker.sports;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class BetPlacer {

	private final Topic topic;
	private final TopicSession session;
	private final TopicConnection topicConnection;

	public BetPlacer() throws NamingException, JMSException {
		InitialContext ctx = new InitialContext();

		TopicConnectionFactory factory = (TopicConnectionFactory) ctx.lookup("ConnectionFactory");
		topicConnection = factory.createTopicConnection();
		topic = (Topic) ctx.lookup("Bets");
		session = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);

	}

	public void place(final Bet bet) throws BetPlacementFailedException {
		final MapMessage message;
		try {
			message = session.createMapMessage();
			message.setString("uuid", bet.getUuid().toString());
		} catch (JMSException e) {
			throw new BetPlacementFailedException(e);
		}

		final TopicPublisher publisher;
		try {
			publisher = session.createPublisher(topic);
		} catch (JMSException e) {
			throw new BetPlacementFailedException(e);
		}
		try {
			publisher.send(message);
		} catch (JMSException e) {
			throw new BetPlacementFailedException(e);
		} finally {
			try {
				publisher.close();
			} catch (JMSException ignored) {
			}
		}

	}

	public void stop() {
		try {
			topicConnection.start();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public void start() {
		try {
			topicConnection.stop();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
}
