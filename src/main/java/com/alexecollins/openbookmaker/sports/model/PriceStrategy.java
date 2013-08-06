package com.alexecollins.openbookmaker.sports.model;

import java.io.Serializable;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public interface PriceStrategy extends Serializable {
	Price getPrice(final long time);
}
