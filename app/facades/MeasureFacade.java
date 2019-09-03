package facades;

import models.Measure;
import models.Sensor;
import play.libs.Json;
import play.mvc.Result;
import repository.MeasureRepository;

import javax.inject.Inject;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.ok;

public class MeasureFacade {
    private MeasureRepository measureRepository;

    @Inject
    public MeasureFacade(final MeasureRepository measureRepository) {
        this.measureRepository = measureRepository;
    }

    public CompletionStage<Result> getById(final String id) {
        return measureRepository.byId(id)
                .thenApplyAsync(measure -> ok(Json.toJson(measure)));
    }

    public CompletionStage<Result> getBySensorId(final String id) {
        final Integer offset = 1;
        final Integer max = 5;

        return measureRepository.bySensorId(id, offset, max)
                .thenApplyAsync(measures -> ok(Json.toJson(measures)));
    }

    public CompletionStage<Result> getByDate(final String date) { //}, final Integer offset, final Integer max) {
        final Integer offset = 0;
        final Integer max = 500;
        //CompletionStage<List<Measure>> lista = measureRepository.byDate(date, offset, max);
        List<Measure> measures = measureRepository.byDateFlat(date, offset, max);
//        this.processMeasures(measures);
        List<Measure> measuresn = new ArrayList<>();
        this.createMeasures(measuresn);
        this.processMeasures(measuresn);


        return measureRepository.byDate(date, offset, max)
                .thenApplyAsync(meas -> ok(Json.toJson(meas)));
    }

    private void processMeasures(List<Measure> measures) {
        int index = 0;
        int lastEmpty = -1;
        int firstEmpty = -1;
        Long previousVersion = 0L;
        boolean emptyFound = false;
        for (Measure measure : measures) {
            if (measure.version < 0 && firstEmpty < 0) {
                firstEmpty = index;
            }
            if (measure.version < 0 && firstEmpty >= 0) {
                lastEmpty = index;
            }

            if (measure.version < 0 && index + 1 >= measures.size()) {
                this.calculateAverage(measures, firstEmpty, lastEmpty);
            }

            if (measure.version >= 0 && previousVersion <= 0) {
                this.calculateAverage(measures, firstEmpty, lastEmpty);
                firstEmpty = -1;
            }
            previousVersion = measure.version;
            index++;
        }
    }

    private void calculateAverage(List<Measure> measures, int firstEmpty, int lastEmpty) {
        int firstPosition = firstEmpty;
        int lastPosition = lastEmpty;
        if (firstEmpty > 0) {
            firstPosition = firstEmpty - 1;
        }
        if (measures.size() > lastEmpty + 1) {
            lastPosition = lastEmpty + 1;
        }
        Float firstTemperature = measures.get(firstPosition).temperatura;
        Float lastTemperature = measures.get(lastPosition).temperatura;
        Float firstHumidity = measures.get(firstPosition).humedad;
        Float lastHumidity = measures.get(lastPosition).humedad;
        Float varTemperature = (lastTemperature - firstTemperature) / (lastEmpty + 1 - firstEmpty);
        Float varHumidity = (lastHumidity - firstHumidity) / (lastEmpty + 1 - firstEmpty);
        Float acumTemperature = Float.valueOf(0);
        Float acumHumidity = Float.valueOf(0);

        for (int i = firstEmpty; i <= lastEmpty; i++) {
            measures.get(i).temperatura = measures.get(firstPosition).temperatura + varTemperature + acumTemperature;
            measures.get(i).humedad = measures.get(firstPosition).humedad + varHumidity + acumHumidity;
            acumTemperature += varTemperature;
            acumHumidity += varHumidity;
        }
    }

    private void createMeasures(List<Measure> measures) {
        Sensor sensor0 = new Sensor(0, "0");
        Measure measure = new Measure(sensor0, Float.valueOf(0), Float.valueOf(0), BigInteger.valueOf(100), -1L);
        measures.add(measure);

        Sensor sensor1 = new Sensor(1, "1");
        measure = new Measure(sensor1, Float.valueOf(0), Float.valueOf(0), BigInteger.valueOf(100), -1L);
        measures.add(measure);

        Sensor sensor2 = new Sensor(2, "2");
        measure = new Measure(sensor2, Float.valueOf(0), Float.valueOf(0), BigInteger.valueOf(100), -1L);
        measures.add(measure);

        Sensor sensor3 = new Sensor(3, "3");
        measure = new Measure(sensor3, Float.valueOf(0), Float.valueOf(0), BigInteger.valueOf(100), -1L);
        measures.add(measure);

        Sensor sensor4 = new Sensor(4, "4");
        measure = new Measure(sensor4, Float.valueOf(0), Float.valueOf(0), BigInteger.valueOf(100), -1L);
        measures.add(measure);

        Sensor sensor5 = new Sensor(5, "5");
        measure = new Measure(sensor5, Float.valueOf(10), Float.valueOf(100), BigInteger.valueOf(100), 1L);
        measures.add(measure);

        Sensor sensor6 = new Sensor(6, "6");
        measure = new Measure(sensor6, Float.valueOf(20), Float.valueOf(200), BigInteger.valueOf(100), 1L);
        measures.add(measure);

        Sensor sensor7 = new Sensor(7, "7");
        measure = new Measure(sensor7, Float.valueOf(30), Float.valueOf(300), BigInteger.valueOf(100), 1L);
        measures.add(measure);

        Sensor sensor8 = new Sensor(8, "8");
        measure = new Measure(sensor8, Float.valueOf(0), Float.valueOf(0), BigInteger.valueOf(100), -1L);
        measures.add(measure);

        Sensor sensor9 = new Sensor(9, "9");
        measure = new Measure(sensor9, Float.valueOf(0), Float.valueOf(0), BigInteger.valueOf(100), -1L);
        measures.add(measure);

        Sensor sensor10 = new Sensor(10, "10");
        measure = new Measure(sensor10, Float.valueOf(0), Float.valueOf(0), BigInteger.valueOf(100), -1L);
        measures.add(measure);
    }
}
