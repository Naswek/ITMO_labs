package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Embeddable
public class Location {
    
    @NotNull
    @Column(name = "location_x", nullable = false)
    private Long x; //Поле не может быть null

    @NotNull
    @Column(name = "location_y", nullable = false)
    private Double y; //Поле не может быть null

    @Column(name = "location_z", nullable = false)
    private long z;

    @NotNull
    @Size(max = 500)
    @Column(name = "location_name", nullable = false, length = 500)
    private String name; //Длина строки не должна быть больше 500, Поле не может быть null

    public Location() {
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
