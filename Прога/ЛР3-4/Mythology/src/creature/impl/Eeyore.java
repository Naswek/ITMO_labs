package creature.impl;

import creature.ICartoonAnimal;
import creature.abstraction.AnimalAbstract;
import creature.enumeration.FriendlinessStatus;
import creature.enumeration.LifeStatus;
import exception.PlaceIsToHighExeption;

public class Eeyore extends AnimalAbstract implements ICartoonAnimal {

    public Eeyore(
                  String creatureName,
                  Integer age,
                  LifeStatus lifeStatus,
                  Double strength,
                  String species,
                  String color,
                  Double weight,
                  FriendlinessStatus friendliness
    ) {
        super(
                creatureName,
                age,
                lifeStatus,
                strength,
                species,
                color,
                weight,
                friendliness
        );
    }

    public void makeDonkeySounds() {
        System.out.println("*звуки осла*");
    }

    public void riseSomeoneUp(double distance) throws PlaceIsToHighExeption {

        if (distance < 100) {
            System.out.println(this.getCreatureName() + " никого не поднимает :(");
        } else {
            throw new PlaceIsToHighExeption("Здесь слишком высоко, " + this.getCreatureName() + " не сможет забраться наверх!");
        }
    }

    public Double getEeyoreStrength() {
        return this.strength;
    }

    public Double getEeyoreWeight() {
        return this.weight;
    }

    public String getEeyoreName() {
        return this.getCreatureName();
    }

    @Override
    public boolean equals(Object object) {

        if (object instanceof Eeyore e) {
            return (creatureName == e.creatureName && age == e.age);
        }
        if (object == this) {
            return true;
        }
        if (object == null) {
            return false;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.creatureName.hashCode() + this.age + 2024;
    }

    @Override
    public String toString() {
        return ("This Eeyore: "
                + "Name: " + this.creatureName
                + "Age: " + this.age
                + "LifeStatus: " + this.lifeStatus);
    }
}
