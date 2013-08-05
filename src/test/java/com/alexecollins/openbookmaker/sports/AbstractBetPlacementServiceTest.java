package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.repo.Propositions;
import com.alexecollins.openbookmaker.sports.model.Event;
import org.junit.After;
import org.junit.Before;

import java.io.File;
import java.util.List;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractBetPlacementServiceTest {
	protected final List<Event> events;
	protected final Propositions propositions = new Propositions();
	protected final TestEventGenerator testEventGenerator = new TestEventGenerator(propositions);
	protected BetPlacementService sut;
	protected BetAcceptorService acceptor;
	protected File repoDir = new File(System.getProperty("java.io.tmpdir"));

	public AbstractBetPlacementServiceTest() {
		events = testEventGenerator.generate(100);
	}

	@Before
	public void setUp() throws Exception {
		sut = new BetPlacementService();
		acceptor = new BetAcceptorService(repoDir);
		sut.start();
		acceptor.start();
	}

	@After
	public void tearDown() throws Exception {
		acceptor.stop();
		sut.stop();
	}
}
