package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.FetchConfig;
import io.ebean.Transaction;
import models.Measure;
import models.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.db.ebean.EbeanConfig;
import play.libs.F;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Measure repository as service implementation.
 */
public class MeasureRepository implements SaveRepository<Measure> {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    private static final Logger LOGGER = LoggerFactory.getLogger(MeasureRepository.class);

    @Inject
    public MeasureRepository(final EbeanConfig ebeanConfig,
                            final DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<F.Either<String, Measure>> save(final Measure measure) {
        return supplyAsync(
                () -> {
                    final Transaction txn = ebeanServer.beginTransaction();
                    try {
                        if (Objects.nonNull(measure.id)) {
                            if (ebeanServer.find(Measure.class).where()
                                    .eq("id", measure.id)
                                    .findCount() != 1) {
                                LOGGER.warn("Measure with id = {} not found", measure.id);
                                return F.Either.Left("measure.id");
                            }
                            ebeanServer.update(measure);
                        } else {
                            ebeanServer.save(measure);
                        }
                        txn.commit();
                        return F.Either.Right(measure);
                    } catch (OptimisticLockException e) {
                        LOGGER.warn("Old version saving");
                        txn.rollback();
                        return F.Either.Left("measure.version");
                    } finally {
                        txn.end();
                    }
                },
                executionContext);
    }

    public CompletionStage<Measure> byId(final String id) {
        new FetchConfig().query();
        Measure measure = ebeanServer.find(Measure.class, id);
        return supplyAsync(
                () -> ebeanServer.find(Measure.class, id));
    }

    public CompletionStage<List<Measure>> bySensorId(final String sensorId, final int offset, final int max) {
        new FetchConfig().query();
        return supplyAsync(
                () -> ebeanServer.find(Measure.class)
                        .where()
                        .eq("sensor.id", sensorId)
                        .setFirstRow(offset)
                        .setMaxRows(max)
                        .findList(),
                executionContext);
    }

    public CompletionStage<List<Measure>> byDate(final String date, final int offset, final int max) {
        new FetchConfig().query();
        List<Measure> measures = new ArrayList<>();
        List<Sensor> listaSensores = ebeanServer.find(Sensor.class)
                .where()
                .isNotNull("id")
                .orderBy("id")
                .setFirstRow(0)
                .setMaxRows(1000)
                .findList();
        measures.addAll(listaSensores.stream().map(s -> getMeasure(s, date)).collect(Collectors.toList()));
        CompletionStage<List<Measure>> sensores = supplyAsync(() -> measures);

        return sensores;
    }

    public List<Measure> byDateFlat(final String date, final int offset, final int max) {
        new FetchConfig().query();
        List<Measure> measures = new ArrayList<>();
        List<Sensor> listaSensores = ebeanServer.find(Sensor.class)
                .where()
                .isNotNull("id")
                .orderBy("id")
                .setFirstRow(0)
                .setMaxRows(1000)
                .findList();
        measures.addAll(listaSensores.stream().map(s -> getMeasure(s, date)).collect(Collectors.toList()));
        //CompletionStage<List<Measure>> sensores = supplyAsync(() -> measures);

        return measures;
    }

    private Measure getMeasure(Sensor sensor, String date) {
        new FetchConfig().query();
        List<Measure> measures = ebeanServer.find(Measure.class)
                .where()
                .and() // nested and
                    .ge("fechat", date)
                    .eq("sensor.id", sensor.id)
                    .endAnd()
                .orderBy("fechat")
                .setFirstRow(0)
                .setMaxRows(1)
                .findList();
        if (measures.isEmpty()) {
            measures.add(this.createEmptyMeasure(sensor, date));
        }

        return measures.get(0);
    }

    private Measure createEmptyMeasure(Sensor sensor, String date) {
        Measure measure = new Measure();
        measure.id = 0;
        measure.version = -1;
        measure.fechat = BigInteger.valueOf(Long.parseLong(date));
        measure.temperatura = Float.valueOf(0);
        measure.humedad = Float.valueOf(0);
        measure.sensor = sensor;

        return measure;
    }
}
