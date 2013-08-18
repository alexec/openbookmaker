package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.LifeCycleManager;
import dagger.ObjectGraph;
import org.junit.After;
import org.junit.Before;

import javax.inject.Inject;


/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractTest {
	@Inject
	LifeCycleManager lifeCycleManager;
	@Inject
	TestEventGenerator testEventGenerator;
	@Inject
	OpenSportsBookApp app;
	@Inject
	BetPlacementService betPlacementService;

	@Before
	public void setUp() throws Exception {
		ObjectGraph graph;
		graph = ObjectGraph.create(new TestModule());
		graph.inject(this);
		lifeCycleManager.start();
	}

	@After
	public void tearDown() throws Exception {
		lifeCycleManager.stop();
	}
}
