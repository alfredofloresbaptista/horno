package modules;

import akka.DataCollectorActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension;
import play.Logger;
import play.api.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;

@Singleton
public class QuartzSchedulerHelper {

	private static final Logger.ALogger LOG = Logger.of(QuartzSchedulerHelper.class);

	@Inject
	public QuartzSchedulerHelper(ActorSystem registry, QuartzSchedulerExtension quartzSchedulerExtension, Configuration configuration) {

		ActorRef helloActor = registry.actorOf(Props.create(DataCollectorActor.class), "data-collector-actor");
//		schedule(quartzSchedulerExtension, helloActor, DataCollectorActor.NAME, RandomStringUtils.randomAlphabetic(10));
		schedule(quartzSchedulerExtension, helloActor, DataCollectorActor.NAME, "Mensaje enviado");

	}

	private void schedule(QuartzSchedulerExtension quartzExtension, ActorRef actor, String actorName, String message) {
		Date schedule = quartzExtension.schedule(actorName, actor,  message);
		LOG.info("Job {} scheduled at {}.", actorName, schedule);
	}
}
