package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.sports.model.Price;
import com.alexecollins.openbookmaker.sports.model.PriceStrategy;
import lombok.NonNull;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class StrikePriceStrategy implements PriceStrategy {

	@NonNull private final Price price;

	private StrikePriceStrategy(Price price) {
		if (price == null) {throw new IllegalArgumentException();}
		this.price = price;
	}

	public static StrikePriceStrategy of(final Price price) {
		return new StrikePriceStrategy(price);
	}

	@Override
	public Price getPrice(long time) {
		return price;
	}
}
