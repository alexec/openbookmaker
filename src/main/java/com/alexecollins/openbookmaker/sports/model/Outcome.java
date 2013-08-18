package com.alexecollins.openbookmaker.sports.model;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Outcome implements Serializable {
	private final long id;
	@NonNull
	private final Market market;
	@NonNull
	private final Map<Price.Type, Price> prices = new EnumMap<>(Price.Type.class);

}
