package com.alexecollins.openbookmaker.sports;

import com.alexecollins.openbookmaker.LifeCycleManager;
import com.alexecollins.openbookmaker.jms.JmsQueueConsumer;
import com.alexecollins.openbookmaker.jms.JmsQueueTemplate;
import com.alexecollins.openbookmaker.jms.JmsTopicConsumer;
import com.alexecollins.openbookmaker.jms.JmsTopicTemplate;
import com.alexecollins.openbookmaker.repo.Repo;
import dagger.Module;
import dagger.Provides;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
@Module(
		injects = {
				OpenSportsBookApp.class

		}
)
public class OpenbookmakerModule {
	@Provides
	@Named("betPlacementRepo")
	@Singleton
	public Repo<BetPlacement> betPlacementRepo() {
		return new Repo<>(new File(System.getProperty("java.io.tmpdir")));
	}

	@Provides
	@Named("acceptedBets")
	@Singleton
	public Topic acceptedBets() {
		return jndiLookup("AcceptedBets");
	}

	@Provides
	@Singleton
	public TopicConnectionFactory topicConnectionFactory() {
		return jndiLookup("topicConnectionFactory");
	}

	@Provides
	@Singleton
	public QueueConnectionFactory queueConnectionFactory() {
		return jndiLookup("queueConnectionFactory");
	}

	@Provides
	@Named("bets")
	@Singleton
	public Queue bets() {
		return jndiLookup("Bets");
	}

	@Provides @Singleton
	public LifeCycleManager lifeCycleManager() {
		return new LifeCycleManager();
	}

	@Provides
	@Named("betsJmsTemplate")
	@Singleton
	public JmsQueueTemplate betsJmsTemplate(LifeCycleManager lifeCycleManager, QueueConnectionFactory queueConnectionFactory, @Named("bets") Queue queue) {
		return lifeCycleManager.register(new JmsQueueTemplate(queueConnectionFactory, queue));
	}

	@Provides
	@Named("acceptedBetsJmsTemplate")
	@Singleton
	public JmsTopicTemplate acceptedBetsJmsTemplate(LifeCycleManager lifeCycleManager, TopicConnectionFactory factory, @Named("acceptedBets") Topic topic) {
		return lifeCycleManager.register(new JmsTopicTemplate(factory, topic));
	}

	@Provides @Singleton
	public PropositionService propositionService()  {
		return new PropositionService();
	}

	@Provides @Singleton
	public BetPlacementService betPlacementService(@Named("betsJmsTemplate") JmsQueueTemplate betsJmsTemplate) {
		return new BetPlacementService(betsJmsTemplate);
	}

	@Provides @Singleton
	public BetAcceptorService betAcceptorService(@Named("betPlacementRepo") Repo<BetPlacement> betPlacementRepo, @Named("acceptedBetsJmsTemplate") JmsTopicTemplate acceptedBetsJmsTemplate) {
		return new BetAcceptorService(betPlacementRepo, acceptedBetsJmsTemplate);
	}

	@Provides
	@Named("betsConsumer")
	@Singleton
	public JmsQueueConsumer<BetAcceptorService> betsConsumer(LifeCycleManager lifeCycleManager, QueueConnectionFactory factory, @Named("bets") Queue queue, BetAcceptorService listener) {
		return lifeCycleManager.register(new JmsQueueConsumer<>(factory, queue, listener));
	}

	@Provides @Singleton
	public LiabilityService liabilityService() {
		return new LiabilityService();
	}

	@Provides
	@Named("acceptedBetsConsumer")
	@Singleton
	public JmsTopicConsumer<LiabilityService> acceptedBetsConsumer(LifeCycleManager lifeCycleManager, TopicConnectionFactory factory, @Named("acceptedBets") Topic topic, LiabilityService listener) {
		return lifeCycleManager.register(new JmsTopicConsumer<>(factory, topic, listener));
	}

	@SuppressWarnings("unchecked")
	private static <T> T jndiLookup(String name) {
		try {
			final InitialContext initialContext = new InitialContext();
			try {
				return (T) initialContext.lookup(name);
			} finally {
				initialContext.close();
			}
		} catch (NamingException e) {
			throw new IllegalStateException(e);
		}
	}
}
