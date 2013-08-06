package com.alexecollins.openbookmaker.sports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class BetPlacementService {

	private final JmsTemplate jmsTemplate;

	@Autowired
	public BetPlacementService(@Qualifier("betJmsTemplate") JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void place(final BetPlacement placement) throws BetPlacementFailedException {

		jmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				final ObjectMessage objectMessage = session.createObjectMessage();
				objectMessage.setObject(placement);
				return objectMessage;
			}
		});

		placement.setStatus(BetPlacement.Status.PENDING);

	}
}
