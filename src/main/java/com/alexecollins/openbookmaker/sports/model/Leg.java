package com.alexecollins.openbookmaker.sports.model;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Leg implements Serializable {
	@NonNull
	private final List<Part> parts;
}
