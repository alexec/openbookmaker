package com.alexecollins.openbookmaker.cust;

import lombok.Data;
import lombok.NonNull;

import java.util.Currency;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Acct {
	@NonNull private final Currency currency;
}
