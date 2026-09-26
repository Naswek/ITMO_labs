package org.example.entity;

public class Location {
    private Long x; //Поле не может быть null
    private Double y; //Поле не может быть null
    private long z;
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
