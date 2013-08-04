package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.cust.Account;
import com.alexecollins.openbookmaker.cust.Customer;
import com.alexecollins.openbookmaker.repo.Repo;
import com.alexecollins.openbookmaker.sports.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Currency;

import static org.junit.Assert.assertEquals;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class BetPlacementServiceTest {

	private BetPlacementService sut;
	private BetAcceptorService acceptor;

	@Before
	public void setUp() throws Exception {
		sut = new BetPlacementService();
		acceptor = new BetAcceptorService();
		sut.start();
		acceptor.start();
	}

	@After
	public void tearDown() throws Exception {
		acceptor.stop();
		sut.stop();
	}

	@Test
	public void testPlace() throws Exception {
		final Outcome outcome = Outcome.of(Market.of(Event.of()));
		final Account acct = Account.of(Customer.of("alex"), Currency.getInstance("GBP"));
		final Bet bet = Bet.of(Arrays.asList(Leg.of(Arrays.asList(Part.of(outcome, StrikePriceStrategy.of(outcome.getPrices().get(Price.Type.LIVE)))))));
		final BetPlacement placement = BetPlacement.of(acct, bet);
		sut.place(placement);

		Thread.sleep(100);

		assertEquals(BetPlacement.Status.COMMITTED,  new Repo<BetPlacement>().find(placement.getUuid()).getStatus());
	}
}
