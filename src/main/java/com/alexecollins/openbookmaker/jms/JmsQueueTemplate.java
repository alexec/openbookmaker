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
public class JmsQueueTemplate {
	private final QueueConnection connection;
	private final QueueSession session;
	private final MessageProducer producer;
	private final Queue queue;

	public JmsQueueTemplate(QueueConnectionFactory factory, Queue queue) {
		this.queue = queue;
		try {
			connection = factory.createQueueConnection();
			session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			producer = session.createProducer(queue);
		} catch (JMSException e) {
			throw new IllegalStateException(e);
		}
	}

	@PostConstruct
	public void start() {
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
			connection.close();
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
