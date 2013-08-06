package com.alexecollins.openbookmaker.sports;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/alexecollins/openbookmaker/sports/applicationContext.xml","classpath:/com/alexecollins/openbookmaker/sports/applicationContext-test.xml"})
public abstract class AbstractTest {
	@Autowired
	protected PropositionService propositionService;
	@Autowired
	protected TestEventGenerator testEventGenerator;
	@Autowired
	protected BetPlacementService betPlacementService;
	@Autowired
	protected BetAcceptorService betAcceptorService;
}
