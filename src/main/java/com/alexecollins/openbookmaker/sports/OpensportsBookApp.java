package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.LifeCycleManager;
import com.alexecollins.openbookmaker.jms.JmsQueueConsumer;
import com.alexecollins.openbookmaker.jms.JmsTopicConsumer;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class OpenSportsBookApp {
	@Inject
	@Named("acceptedBetsConsumer")
	JmsTopicConsumer<LiabilityService> acceptedBetsConsumer;
	@Inject
	@Named("betsConsumer")
	JmsQueueConsumer<BetAcceptorService> betsConsumer;
	@Inject
	BetPlacementService betPlacementService;
	@Inject
	LifeCycleManager lifeCycleManager;
	@Inject
	PropositionService propositionService;

}
