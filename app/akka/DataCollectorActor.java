package akka;

import akka.actor.AbstractLoggingActor;
import models.Measure;
import repository.MeasureRepository;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DataCollectorActor extends AbstractLoggingActor {

	public final static String NAME = "DATA_COLLECTOR_ACTOR";
	public static MeasureRepository measureRepository;

//	@Inject
	public DataCollectorActor() {
	}

	@Inject
	public DataCollectorActor(MeasureRepository measureRepository) {
		this.measureRepository = measureRepository;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
//				.match(String.class, s -> log().info(getMessage(s)))
				.match(String.class, s -> System.out.println(getMessage(s)))
				.matchAny(o -> log().info("received unknown message"))
				.build();
	}

	private String getMessage(String s) {
		//List<Measure> measures = measureRepository.byDateFlat("1565932060", 0, 1000);

		List<Measure> measureList = measureRepository.byDateFlat("1565932038", 0, 1000);

		//CompletionStage<Result> getByDate = measureFacade.getByDate("1565932060");
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return "Received String message " + dtf.format(now) + " records " + measureList.size();
//		return "Received String message " + dtf.format(now) + " records " ;
	}
}
