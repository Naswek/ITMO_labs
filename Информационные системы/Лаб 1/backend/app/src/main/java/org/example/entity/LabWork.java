package org.example.dto

public class LabWork {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private String description; //Длина строки не должна быть больше 3429, Поле не может быть null
    private Difficulty difficulty; //Поле не может быть null
    private Discipline discipline; //Поле может быть null
    private double minimalPoint; //Значение поля должно быть больше 0
    private Integer averagePoint; //Поле не может быть null, Значение поля должно быть больше 0
    private Person author; //Поле может быть null
}