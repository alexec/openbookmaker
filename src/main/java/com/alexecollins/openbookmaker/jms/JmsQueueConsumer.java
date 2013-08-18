package com.alexecollins.openbookmaker.jms;

import lombok.ToString;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.jms.*;
import java.lang.IllegalStateException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@ToString(exclude = {"connection", "session", "receiver"})
public class JmsQueueConsumer<T extends MessageListener> {
	private enum State {READY,STARTING, RUNNING,STOPPING}
	private final QueueConnection connection;
	private final QueueSession session;
	private final QueueReceiver receiver;
	private final Queue queue;
	private final MessageListener listener;
	private State state = State.READY;

	public JmsQueueConsumer(QueueConnectionFactory factory, Queue queue, T listener) {
		this.queue = queue;
		this.listener = listener;
		try {
			connection = factory.createQueueConnection();
			session = connection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			receiver = session.createReceiver(queue);
		} catch (JMSException e) {
			throw new IllegalStateException(e);
		}
	}

	@PostConstruct
	public void start() {
		state = State.STARTING;
		try {
			receiver.setMessageListener(listener);
			connection.start();
		} catch (JMSException e) {
			throw new IllegalStateException(e);
		}
		state = State.RUNNING;
	}

	public State getState() {
		return state;
	}

	@PreDestroy
	public void stop() {
		state = State.STOPPING;
		try {
			session.close();
			connection.close();
		} catch (JMSException e) {
			throw new IllegalStateException(e);
		}
		state = State.READY;
	}

}
