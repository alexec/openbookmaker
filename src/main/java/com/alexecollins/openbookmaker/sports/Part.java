package com.alexecollins.openbookmaker.sports;

import lombok.Data;
import lombok.NonNull;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Part {
	@NonNull
	private final Outcome outcome;
	@NonNull
	private final PriceStrategy priceStrategy;
}
