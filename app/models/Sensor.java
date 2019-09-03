package models;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Sensor entity representation.
 */
@Entity
public class Sensor extends NamedModel {
//    @JsonView({BookViews.Public.class, AuthorViews.Public.class, GenreViews.Public.class})
    @NotNull(message = "sensor.orden")
    public Integer orden;

//    @JsonView({BookViews.Public.class, AuthorViews.Public.class, GenreViews.Public.class})
    @NotNull(message = "sensor.nombre")
    public String nombre;

    public Sensor(Integer orden, String nombre) {
        this.orden = orden;
        this.nombre = nombre;
    }

    public Sensor() {
    }
}
