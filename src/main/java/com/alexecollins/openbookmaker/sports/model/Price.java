package com.alexecollins.openbookmaker.sports.model;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Price implements Serializable {

	private static final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);

	public static enum Type {
		LIVE
	}

	@NonNull
	private int num, den;

	public BigDecimal getValue() {
		return BigDecimal.valueOf(num).divide(BigDecimal.valueOf(den), MATH_CONTEXT);
	}
}
