package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.sports.model.Event;
import com.alexecollins.openbookmaker.sports.model.Market;
import com.alexecollins.openbookmaker.sports.model.Outcome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class PropositionService {

	private AtomicLong id = new AtomicLong();
	private final Map<Long,List<Market>> markets = new HashMap<>();
	private final Map<Long,List<Outcome>> outcomes = new HashMap<>();

	public Event newEvent() {
		return Event.of(id.getAndIncrement());
	}

	public Market newMarket(Event event) {
		final Market market = Market.of(id.getAndIncrement(), event);
		if (!markets.containsKey(event.getId())) {markets.put(event.getId(), new ArrayList<Market>());}
		markets.get(event.getId()).add(market);
		return market;
	}

	public Outcome newOutcome(Market market) {
		final Outcome outcome = Outcome.of(id.getAndIncrement(), market);
		if (!outcomes.containsKey(market.getId())) {outcomes.put(market.getId(), new ArrayList<Outcome>());}
		outcomes.get(market.getId()).add(outcome);
		return outcome;
	}

	public Event eventById(long id) {
		return Event.of(id);
	}

	public List<Market> marketsByEvent(long eventId) {
		return markets.get(eventId);
	}

	public List<Outcome> outcomesByMarket(long marketId) {
		return outcomes.get(marketId);
	}

	public List<Market> marketsByEvent(Event event) {
		return marketsByEvent(event.getId());
	}

	public List<Outcome> outcomesByMarket(Market market) {
		return outcomesByMarket(market.getId());
	}
}
