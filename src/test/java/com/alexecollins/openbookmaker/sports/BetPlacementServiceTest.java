package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.cust.Account;
import com.alexecollins.openbookmaker.cust.Customer;
import com.alexecollins.openbookmaker.repo.Repo;
import com.alexecollins.openbookmaker.sports.model.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;

import static org.junit.Assert.assertEquals;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class BetPlacementServiceTest extends AbstractBetPlacementServiceTest {

	@Autowired
	Repo<BetPlacement> betPlacementRepo;


	@Test
	public void testPlace() throws Exception {
		final Outcome outcome = Outcome.of(5l, Market.of(3l, Event.of(2l)));
		outcome.getPrices().put(Price.Type.LIVE, Price.of(1,1));
		final Account acct = Account.of(Customer.of("alex"), Currency.getInstance("GBP"));
		final Bet bet = Bet.of(Arrays.asList(Leg.of(Arrays.asList(Part.of(outcome, StrikePriceStrategy.of(outcome.getPrices().get(Price.Type.LIVE)))))), BigDecimal.ONE);
		final BetPlacement placement = BetPlacement.of(acct, bet);
		betPlacementService.place(placement);

		Thread.sleep(50);

		assertEquals(BetPlacement.Status.COMMITTED, betPlacementRepo.find(placement.getUuid()).getStatus());

		assertEquals(BigDecimal.ONE, liabilityService.getLiability(outcome));
	}
}
