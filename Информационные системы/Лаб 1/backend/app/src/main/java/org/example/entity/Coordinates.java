package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Embeddable
public class Coordinates {
   
    @DecimalMin(value = "-523", inclusive = false)
    @Column(name = "coordinate_x", nullable = false)
    private float x; //Значение поля должно быть больше -523
   
    @NotNull
    @DecimalMin(value = "-643", inclusive = false)
    @Column(name = "coordinate_y", nullable = false)
    private Integer y; //Значение поля должно быть больше -643, Поле не может быть null

    public Coordinates() {
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
