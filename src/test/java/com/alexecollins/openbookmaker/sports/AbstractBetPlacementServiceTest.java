package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.sports.model.Event;
import org.junit.After;
import org.junit.Before;

import java.util.List;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractBetPlacementServiceTest extends AbstractTest {
	protected List<Event> events;

	@Before
	public void setUp() throws Exception {
		events = testEventGenerator.generate(100);
	}

	@After
	public void tearDown() throws Exception {
	}
}
