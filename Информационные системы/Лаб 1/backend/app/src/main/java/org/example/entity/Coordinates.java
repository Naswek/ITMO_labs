package org.example.entity;

public class Coordinates {
    private float x; //Значение поля должно быть больше -523
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
