package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.Date;
import org.example.enums.Difficulty;

@Entity
@Table(name = "lab_work")
public class LabWork {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Version
    @Column(nullable = false)
    private Long version;
   
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой
   
    @NotNull
    @Valid
    @Embedded
    private Coordinates coordinates; //Поле не может быть null
   
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
   
    @NotNull
    @Size(max = 3429)
    @Column(nullable = false, length = 3429)
    private String description; //Длина строки не должна быть больше 3429, Поле не может быть null
   
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty; //Поле не может быть null
   
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discipline_id")
    private Discipline discipline; //Поле может быть null
   
    @Positive
    @Column(name = "minimal_point", nullable = false)
    private double minimalPoint; //Значение поля должно быть больше 0
   
    @NotNull
    @Positive
    @Column(name = "average_point", nullable = false)
    private Integer averagePoint; //Поле не может быть null, Значение поля должно быть больше 0
   
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Person author; //Поле может быть null

    public LabWork() {
    }

    @PrePersist
    private void assignCreationDate() {
        creationDate = new Date();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public double getMinimalPoint() {
        return minimalPoint;
    }

    public void setMinimalPoint(double minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public Integer getAveragePoint() {
        return averagePoint;
    }

    public void setAveragePoint(Integer averagePoint) {
        this.averagePoint = averagePoint;
    }

    public Person getAuthor() {
        return author;
    }

    public void setAuthor(Person author) {
        this.author = author;
    }
}
