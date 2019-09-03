package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * Measure entity representation.
 */
@Entity
public class Measure extends NamedModel {
    @ManyToOne
    @NotNull(message = "measure.sensor")
    public Sensor sensor;

    @NotNull(message = "measure.temperatura")
    public Float temperatura;

    @NotNull(message = "measure.humedad")
    public Float humedad;

    @NotNull(message = "measure.fechat")
    public BigInteger fechat;

    public Measure(Sensor sensor, Float temperatura, Float humedad, BigInteger fechat, long version) {
        this.sensor = sensor;
        this.temperatura = temperatura;
        this.humedad = humedad;
        this.fechat = fechat;
        this.version = version;
    }
    public Measure() {
    }
}
