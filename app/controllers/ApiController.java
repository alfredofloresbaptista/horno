package controllers;

import akka.actor.ActorSystem;
import controllers.json.parser.SensorBodyParser;
import facades.MeasureFacade;
import facades.SensorFacade;
import models.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import repository.AuthorRepository;
import repository.BookRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static play.mvc.Controller.request;
import static play.mvc.Results.ok;

/**
 * API implementation.
 */
public class ApiController {
    private final MeasureFacade measureFacade;
    private final SensorFacade sensorFacade;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ActorSystem actorSystem;
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiController.class);

    @Inject
    public ApiController(final MeasureFacade measureFacade,
                         final SensorFacade sensorFacade,
                         final BookRepository bookRepository,
                         final AuthorRepository authorRepository,
                         final ActorSystem actorSystem) {
        this.measureFacade = measureFacade;
        this.sensorFacade = sensorFacade;
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.actorSystem = actorSystem;
    }

    public CompletionStage<Result> sensorById(final String id) {
        //Scheduler scheduler = new Scheduler();
//        TasksCustomExecutionContext executor = new TasksCustomExecutionContext(actorSystem);
//        SomeTask someTask = new SomeTask(actorSystem, executor);

//        MyActorTask myActorTask = new MyActorTask()
        System.out.println("HOLLLLA");
        return sensorFacade.getById(id)
                .thenApplyAsync(sensor -> ok(Json.toJson(sensor)));
    }

    @BodyParser.Of(SensorBodyParser.class)
    public CompletionStage<Result> createSensor() {

        final Sensor sensor = request().body().as(Sensor.class);
        return sensorFacade.save(sensor);
    }

    public CompletionStage<Result> measureById(final String id) {
        return measureFacade.getById(id)
                .thenApplyAsync(measure -> ok(Json.toJson(measure)));
    }

    public CompletionStage<Result> measuresBySensorId(final String id) {
        return measureFacade.getBySensorId(id)
                .thenApplyAsync(measures -> ok(Json.toJson(measures)));
    }

    public CompletionStage<Result> measuresByDate(final String date) {
        return measureFacade.getByDate(date)
                .thenApplyAsync(measures -> ok(Json.toJson(measures)));
    }
}
