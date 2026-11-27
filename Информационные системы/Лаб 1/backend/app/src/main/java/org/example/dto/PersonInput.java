package org.example.dto;

import java.time.format.DateTimeParseException;
import org.example.entity.Location;
import org.example.entity.Person;
import org.example.enums.Color;
import org.example.enums.Country;
import org.example.utils.json.IsoDateAdapter;

/** JSON request for creating or updating a person. */
public class PersonInput {

    private Long id;
    private String name;
    private Color eyeColor;
    private Color hairColor;
    private Location location;
    private String birthday;
    private Long weight;
    private Country nationality;

    public Person toEntity() {
        Person person = new Person();
        person.setId(id);
        person.setName(name);
        person.setEyeColor(eyeColor);
        person.setHairColor(hairColor);
        person.setLocation(location);
        if (birthday != null) {
            try {
                person.setBirthday(new IsoDateAdapter().adaptFromJson(birthday));
            } catch (DateTimeParseException error) {
                throw new IllegalArgumentException("Дата рождения должна быть в формате ISO 8601", error);
            }
        }
        person.setWeight(weight);
        person.setNationality(nationality);
        return person;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Color getEyeColor() { return eyeColor; }
    public void setEyeColor(Color eyeColor) { this.eyeColor = eyeColor; }

    public Color getHairColor() { return hairColor; }
    public void setHairColor(Color hairColor) { this.hairColor = hairColor; }

    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public Long getWeight() { return weight; }
    public void setWeight(Long weight) { this.weight = weight; }

    public Country getNationality() { return nationality; }
    public void setNationality(Country nationality) { this.nationality = nationality; }
}
