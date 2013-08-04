package com.alexecollins.openbookmaker.sports.model;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Part implements Serializable {
	@NonNull
	private final Outcome outcome;
	@NonNull
	private final PriceStrategy priceStrategy;
}
