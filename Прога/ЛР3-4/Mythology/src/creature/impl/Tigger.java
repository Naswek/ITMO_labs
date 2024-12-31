package creature.impl;

import creature.ICartoonAnimal;
import creature.abstraction.AnimalAbstract;
import creature.enumeration.FriendlinessStatus;
import creature.enumeration.LifeStatus;
import exception.PlaceIsToHighExeption;

public class Tigger extends AnimalAbstract implements ICartoonAnimal {

    public Tigger(
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

    public void makeTigerSounds(){
        System.out.println("*звуки тигра*");
    }

    public void riseSomeoneUp(String name, double distance) throws PlaceIsToHighExeption {

        if (distance > 100) {
            System.out.println(this.getCreatureName()
                    + " поднял "
                    + name);
        } else {
            throw new PlaceIsToHighExeption("Здесь слишком высоко, "
                    + this.getCreatureName()
                    + " не сможет забраться наверх!");
        }
    }

    public Double getTiggerWeight() {
        return this.weight;
    }

    public Double getTiggerStrength() {
        return this.strength;
    }

    public String getTiggerName() {
        return this.getCreatureName();
    }

    @Override
    public boolean equals(Object object) {

        if (object instanceof Tigger) {
            Tigger t = (Tigger) object;
            return (creatureName == t.creatureName && age == t.age);
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
        return ("This Tiger: "
                + "Name: " + this.creatureName
                + "Age: " + this.age
                + "LifeStatus: " + this.lifeStatus);
    }
}
