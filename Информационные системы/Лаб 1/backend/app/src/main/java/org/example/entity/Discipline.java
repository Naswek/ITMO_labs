package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "discipline")
public class Discipline {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой
  
    @NotNull
    @Column(name = "practice_hours", nullable = false)
    private Long practiceHours; //Поле не может быть null
  
    @Column(name = "self_study_hours", nullable = false)
    private long selfStudyHours;
  
    @Column(name = "labs_count")
    private Integer labsCount; //Поле может быть null

    public Discipline() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPracticeHours() {
        return practiceHours;
    }

    public void setPracticeHours(Long practiceHours) {
        this.practiceHours = practiceHours;
    }

    public long getSelfStudyHours() {
        return selfStudyHours;
    }

    public void setSelfStudyHours(long selfStudyHours) {
        this.selfStudyHours = selfStudyHours;
    }

    public Integer getLabsCount() {
        return labsCount;
    }

    public void setLabsCount(Integer labsCount) {
        this.labsCount = labsCount;
    }
}
