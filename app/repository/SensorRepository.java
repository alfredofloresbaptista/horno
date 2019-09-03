package repository;

import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.FetchConfig;
import io.ebean.Transaction;
import models.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.db.ebean.EbeanConfig;
import play.libs.Json;
import play.mvc.Result;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.mvc.Results.ok;

/**
 * Author repository as service implementation.
 */
public class SensorRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorRepository.class);

    @Inject
    public SensorRepository(final EbeanConfig ebeanConfig,
                            final DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    public CompletionStage<Result> save(Sensor sensor) {
        JsonNode jsonNode = Json.toJson(sensor);
        CompletionStage<Result> savedSensor = supplyAsync(() -> ok(jsonNode));
        return savedSensor;
    }

    public Sensor saveModel(final Sensor sensor) {
        final Transaction txn = ebeanServer.beginTransaction();
        try {
            ebeanServer.save(sensor);
            txn.commit();
            return sensor;
        } catch (OptimisticLockException e) {
            LOGGER.warn("Old version saving");
            txn.rollback();
            return sensor;
        } finally {
            txn.end();
        }
    }

    public CompletionStage<Sensor> getById(final String id) {
        new FetchConfig().query();
        return supplyAsync(
                () -> ebeanServer.find(Sensor.class, id));
    }
}
