package creature.impl;

import creature.ICartoonAnimal;
import creature.abstraction.AnimalAbstract;
import creature.enumeration.FriendlinessStatus;
import creature.enumeration.LifeStatus;
import exception.PlaceIsToHighExeption;

public class Piglet extends AnimalAbstract implements ICartoonAnimal {

    public Piglet(
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

    public void makePiggySounds(){
        System.out.println("*звуки поросенка*");
    }

    public void riseSomeoneUp(String name, Double distance) throws PlaceIsToHighExeption {

        if (distance < 100) {
            System.out.println(this.getCreatureName() + " поднял " + name);
        } else {
            throw new PlaceIsToHighExeption("Здесь слишком высоко, " + this.getCreatureName() + " не сможет забраться наверх!");
        }
    }

    public Double getPigletWeight() {
        return this.weight;
    }

    public Double getPigletStrength() {
        return this.strength;
    }

    public String getPigletName() {
        return this.getCreatureName();
    }

    @Override
    public boolean equals(Object object) {

        if (object instanceof Piglet p) {
            return (creatureName == p.creatureName && age == p.age);
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
        return ("This Piglet: "
                + "Name: " + this.creatureName
                + "Age: " + this.age
                + "LifeStatus: " + this.lifeStatus);
    }
}
