package com.alexecollins.openbookmaker.sports.model;

import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Market implements Serializable{
	@NonNull private final Event event;
}
