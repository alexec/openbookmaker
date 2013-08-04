package com.alexecollins.openbookmaker.sports;

import java.util.Date;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public interface PriceStrategy {
	Price getPrice(final Date time);
}
