package org.example.entity;

public class Discipline {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Long practiceHours; //Поле не может быть null
    private long selfStudyHours;
    private Integer labsCount; //Поле может быть null

    public Discipline() {
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
