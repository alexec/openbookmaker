package com.alexecollins.openbookmaker.sports.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Event implements Serializable {
	private final long id;
}
