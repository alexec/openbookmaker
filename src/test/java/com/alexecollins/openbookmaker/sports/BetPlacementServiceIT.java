package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.cust.Account;
import com.alexecollins.openbookmaker.cust.Customer;
import com.alexecollins.openbookmaker.sports.model.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.Currency;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class BetPlacementServiceIT extends AbstractBetPlacementServiceTest {
	@Test
	public void load() throws Exception {
		final Account acct = Account.of(Customer.of("alex"), Currency.getInstance("GBP"));

		int i = 100;
		int n = 0;
		final long start = System.currentTimeMillis();
		while (--i > 0) {
			for (Event event : events) {
				final Outcome outcome = propositions.outcomesByMarket(propositions.marketsByEvent(event).get(0)).get(0);

				sut.place(BetPlacement.of(acct, Bet.of(Arrays.asList(Leg.of(Arrays.asList(Part.of(outcome, StrikePriceStrategy.of(outcome.getPrices().get(Price.Type.LIVE)))))))));
				n++;
			}
			System.out.println("placed " + n + " bets, " + acceptor.getProcessed() + " accepted");

		}
		final long t = System.currentTimeMillis() - start;
		System.out.println("placed " + n + " in " + t +"ms, " + (n*1000/t) +" bets/sec");

		while (acceptor.getProcessed() < n) {
			System.out.println("waiting for acceptance to complete...");
			Thread.sleep(500);
		}

		final long t2 = System.currentTimeMillis() - start;
		System.out.println("acceptance in " + t2 +"ms, " + (n*1000/t2) +" bets/sec");

	}
}
