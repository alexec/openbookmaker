package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.cust.Acct;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class Bet {
	@NonNull private final UUID uuid = UUID.randomUUID();
	@NonNull private final Acct acct;
	@NonNull private final List<Leg> legs;
}
