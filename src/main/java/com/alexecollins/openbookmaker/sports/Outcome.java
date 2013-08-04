package com.alexecollins.openbookmaker.sports;

import lombok.Data;
import lombok.NonNull;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Outcome {
	@NonNull
	private final Market outcome;
	@NonNull
	private final Map<Price.Type,Price> prices = new EnumMap<Price.Type, Price>(Price.Type.class);

}
