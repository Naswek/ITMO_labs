package org.example.dto

public class Person {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Color eyeColor; //Поле не может быть null
    private Color hairColor; //Поле может быть null
    private Location location; //Поле не может быть null
    private java.util.Date birthday; //Поле не может быть null
    private Long weight; //Поле может быть null, Значение поля должно быть больше 0
    private Country nationality; //Поле не может быть null
}