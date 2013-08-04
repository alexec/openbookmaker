package com.alexecollins.openbookmaker.sports;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class BetPlacementFailedException extends Exception {

	public BetPlacementFailedException(String message) {
		super(message);
	}

	public BetPlacementFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public BetPlacementFailedException(Throwable cause) {
		super(cause);
	}
}
