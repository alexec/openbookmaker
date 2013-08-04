package com.alexecollins.openbookmaker.cust;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Customer implements Serializable {
	@NonNull private final String uname;
	@NonNull private String passwd = null;
}
