package modules;

import akka.DataCollectorActor;
import akka.actor.ActorSystem;
import akka.actor.ExtendedActorSystem;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension;
import play.libs.akka.AkkaGuiceSupport;

import javax.inject.Inject;

public class ScheduleJobsModule extends AbstractModule implements AkkaGuiceSupport {

	@Override
	protected void configure() {
		bindActor(DataCollectorActor.class, DataCollectorActor.NAME);
		bind(QuartzSchedulerHelper.class).asEagerSingleton();
		bind(QuartzSchedulerExtension.class).toProvider(SchedulerJobInitializer.class);
	}


	private static class SchedulerJobInitializer implements Provider<QuartzSchedulerExtension> {
		private final QuartzSchedulerExtension quartzSchedulerExtension;

		@Inject
		public SchedulerJobInitializer(ActorSystem actorSystem) {
			this.quartzSchedulerExtension = new QuartzSchedulerExtension((ExtendedActorSystem) actorSystem);
		}

		@Override
		public QuartzSchedulerExtension get() {
			return quartzSchedulerExtension;
		}
	}
}
