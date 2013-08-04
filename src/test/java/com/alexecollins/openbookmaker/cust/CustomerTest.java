package com.alexecollins.openbookmaker.cust;

import org.junit.Before;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class CustomerTest {

	@Before
	public void setUp() throws Exception {
		Customer.of("alex");
	}
}
