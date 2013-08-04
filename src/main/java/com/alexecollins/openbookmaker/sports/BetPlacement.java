package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.cust.Account;
import com.alexecollins.openbookmaker.sports.model.Bet;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Data(staticConstructor = "of")
public class BetPlacement implements Serializable {
	public enum Status {
		PENDING, COMMITTED, REJECTED
	}

	@NonNull
	private final UUID uuid = UUID.randomUUID();
	@NonNull
	private final Account account;
	@NonNull
	private final Bet bet;
	@NonNull
	private Status status = Status.PENDING;
}
