package com.alexecollins.openbookmaker.jms;

import lombok.ToString;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.*;
import java.lang.IllegalStateException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@ToString(exclude = {"connection", "session", "subscriber"})
public class JmsTopicConsumer<T extends MessageListener> {
	private final TopicConnection connection;
	private final TopicSession session;
	private final TopicSubscriber subscriber;
	private final MessageListener listener;
	private final Topic topic;

	public JmsTopicConsumer(TopicConnectionFactory factory, Topic topic, T listener) {
		this.topic = topic;
		this.listener = listener;

		try {
			connection = factory.createTopicConnection();
			session = connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
			subscriber = session.createSubscriber(topic);
		} catch (JMSException e) {
			throw new IllegalStateException(e);
		}
	}

	@PostConstruct
	public void start()  {
		try {
			subscriber.setMessageListener(listener);
			connection.start();
		} catch (JMSException e) {
			throw new IllegalStateException(e);
		}
	}

	@PreDestroy
	public void stop() {
		try {
			session.close();
			connection.close();
		} catch (JMSException e) {
			throw new IllegalStateException(e);
		}
	}
}
