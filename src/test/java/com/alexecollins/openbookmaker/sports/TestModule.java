package com.alexecollins.openbookmaker.sports;

import dagger.Module;
import dagger.Provides;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Module(
		injects = {AbstractTest.class, BetPlacementServiceTest.class, BetPlacementServiceIT.class},
		includes = {OpenbookmakerModule.class}
)
public class TestModule {

	@Provides
	TestEventGenerator testEventGenerator(PropositionService propositionService) {
		return new TestEventGenerator(propositionService);
	}
}
