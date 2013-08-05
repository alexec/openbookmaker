package com.alexecollins.openbookmaker.sports.model;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Price implements Serializable {
	public static enum Type {
		LIVE
	}

	@NonNull
	private int num, den;
}
