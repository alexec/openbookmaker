package com.alexecollins.openbookmaker.jms;

import lombok.ToString;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.*;
import java.io.Serializable;
import java.lang.IllegalStateException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@ToString(exclude = {"connection", "session", "producer"})
public class JmsTopicTemplate {
	private final TopicConnection connection;
	private final TopicSession session;
	private final MessageProducer producer;
	private final Topic topic;

	public JmsTopicTemplate(TopicConnectionFactory factory, Topic topic) {
		this.topic = topic;

		try {
			connection = factory.createTopicConnection();
			session = connection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
			producer = session.createProducer(topic);
		} catch (JMSException e) {
			throw new IllegalStateException(e);
		}
	}

	@PostConstruct
	public void start()  {
		try {
			connection.start();
		} catch (JMSException e) {
			throw new IllegalStateException(e);
		}
	}

	@PreDestroy
	public void stop() {
		try {
			session.close();
		} catch (JMSException e) {
			throw new IllegalStateException(e);
		}
	}

	public void send(Serializable object) throws JMSException {
		final ObjectMessage message = session.createObjectMessage();
		message.setObject(object);
		producer.send(message);
	}
}
