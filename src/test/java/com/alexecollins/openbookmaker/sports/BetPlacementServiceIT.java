package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.cust.Account;
import com.alexecollins.openbookmaker.cust.Customer;
import com.alexecollins.openbookmaker.sports.model.Event;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Currency;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class BetPlacementServiceIT extends AbstractBetPlacementServiceTest {

	List<Event> events;
	private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<>(executorService);

	@Before
	public void setUp() throws Exception {
		events = testEventGenerator.generate(100);
	}

	@After
	public void tearDown() throws Exception {
		executorService.shutdown();

	}

	@Test
	public void load() throws Exception {
		final Account acct = Account.of(Customer.of("alex"), Currency.getInstance("GBP"));
		int i = 100;
		int n = 0;
		final long start = System.currentTimeMillis();
		while (--i > 0) {
			for (final Event event : events) {
				executorCompletionService.submit(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						//final Outcome outcome = propositionService.outcomesByMarket(propositionService.marketsByEvent(event).get(0)).get(0);

						//app.place(BetPlacement.of(acct, Bet.of(Arrays.asList(Leg.of(Arrays.asList(Part.of(outcome, StrikePriceStrategy.of(outcome.getPrices().get(Price.Type.LIVE)))))), BigDecimal.ONE)));
						return null;
					}
				});
				n++;
			}
			//System.out.println("placed " + n + " bets, " + betAcceptorService.getProcessed() + " accepted");

		}
		final long t = System.currentTimeMillis() - start;
		System.out.println("placed " + n + " in " + t +"ms, " + (n*1000/t) +" bets/sec");
		while (n-- > 0) {
			executorCompletionService.take();
		}

		//while (betAcceptorService.getProcessed() < n) {
		//	System.out.println("waiting for acceptance to complete...");
		//	Thread.sleep(500);
		//}

		final long t2 = System.currentTimeMillis() - start;
		System.out.println("acceptance in " + t2 +"ms, " + (n*1000/t2) +" bets/sec");

	}
}
