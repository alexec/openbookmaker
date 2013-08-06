package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.sports.model.Event;
import com.alexecollins.openbookmaker.sports.model.Market;
import com.alexecollins.openbookmaker.sports.model.Outcome;
import com.alexecollins.openbookmaker.sports.model.Price;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class TestEventGenerator {
	@NonNull
	private final PropositionService propositionService;

	@Autowired
	public TestEventGenerator(PropositionService propositionService) {
		this.propositionService = propositionService;
	}

	public List<Event> generate(final long n) {
		final List<Event> list = new ArrayList<>();
		for (int i = 0;i<n;i++) {
			final Event event = propositionService.newEvent();
			final Market market = propositionService.newMarket(event);
			for (int j=0;j<20;j++) {
				final Outcome outcome = propositionService.newOutcome(market);
				outcome.getPrices().put(Price.Type.LIVE, Price.of(1,1));
			}
			list.add(event);
		}
		return list;
	}
}
