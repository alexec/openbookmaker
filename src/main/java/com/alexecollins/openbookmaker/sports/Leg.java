package com.alexecollins.openbookmaker.sports;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Leg {
	@NonNull
	private final List<Part> parts;
}
