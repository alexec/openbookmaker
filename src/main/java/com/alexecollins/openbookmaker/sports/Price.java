package com.alexecollins.openbookmaker.sports;

import lombok.Data;
import lombok.NonNull;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Price {
	public static enum Type {
		LIVE
	}

	@NonNull
	private short num, den;
}
