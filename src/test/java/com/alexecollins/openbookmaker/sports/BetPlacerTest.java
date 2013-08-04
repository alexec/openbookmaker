package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.cust.Acct;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.*;
import java.util.Queue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class BetPlacerTest {

	private BetPlacer sut;
	private Queue<Message> messages;

	@Before
	public void setUp() throws Exception {
	sut = new BetPlacer();
		messages = new LinkedList<>();

		InitialContext ctx = new InitialContext();
		final TopicConnection connectionFactory = ((TopicConnectionFactory) ctx.lookup("ConnectionFactory")).createTopicConnection();
		connectionFactory.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE).createSubscriber((Topic) ctx.lookup("Bets")).setMessageListener(new MessageListener() {
			 @Override
			 public void onMessage(Message message) {
				 System.out.println(message);
				 messages.add(message);
			 }
		 });

		connectionFactory.start();
		sut.start();
	}


	@After
	public void tearDown() throws Exception {
		sut.stop();

	}

	@Test
	public void testPlace() throws Exception {
		final Outcome outcome = Outcome.of(Market.of(Event.of()));
		final Bet bet = Bet.of(Acct.of(Currency.getInstance("GBP")), Arrays.asList(Leg.of(Arrays.asList(Part.of(outcome, StrikePriceStrategy.of(outcome.getPrices().get(Price.Type.LIVE)))))));
		sut.place(bet);

		Thread.sleep(100);

		final MapMessage peek = (MapMessage)messages.peek();
		assertNotNull(peek);
		assertEquals(bet.getUuid(), UUID.fromString(peek.getString("uuid")));
	}
}
