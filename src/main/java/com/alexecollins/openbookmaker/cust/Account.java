package com.alexecollins.openbookmaker.cust;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.Currency;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Account implements Serializable {
	@NonNull private final Customer customer;
	@NonNull private final Currency currency;
}
