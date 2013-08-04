package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.repo.Repo;
import lombok.extern.slf4j.Slf4j;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Slf4j
public class BetAcceptorService implements MessageListener {

	private final Repo<BetPlacement> repo = new Repo<>();
	private final QueueConnection topicConnection;
	private final Queue topic;
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

	public BetAcceptorService() throws NamingException, JMSException {
		final InitialContext ctx = new InitialContext();

		topicConnection = ((QueueConnectionFactory) ctx.lookup("ConnectionFactory")).createQueueConnection();
		topic = (Queue) ctx.lookup("Bets");
	}

	public static void main(String[] args) throws Exception {
		final BetAcceptorService acceptor = new BetAcceptorService();
		acceptor.start();
		acceptor.await();
	}

	public void await() throws InterruptedException {
		Thread.sleep(Long.MAX_VALUE);
	}

	public void start() throws JMSException {
		session.get().createReceiver(topic).setMessageListener(this);
		topicConnection.start();
	}

	public void stop() throws JMSException {
		topicConnection.stop();
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
		}
	}
}
