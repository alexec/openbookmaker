package com.alexecollins.openbookmaker.cust;

import lombok.Data;
import lombok.NonNull;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Cust {
	@NonNull private final Map<Currency,Acct> accts = new HashMap<>();
	@NonNull private final String uname;
	private String passwd = null;
}
