package com.alexecollins.openbookmaker.sports.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public interface PriceStrategy extends Serializable {
	Price getPrice(final Date time);
}
