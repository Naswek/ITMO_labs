package creature.impl;

import creature.ICartoonAnimal;
import creature.abstraction.AnimalAbstract;
import creature.enumeration.FriendlinessStatus;
import creature.enumeration.LifeStatus;
import exception.PlaceIsToHighExeption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class WinnieThePooh extends AnimalAbstract implements ICartoonAnimal {

    protected ArrayList<String> WinniesFridge  = new ArrayList<>(Arrays.asList( "Pie",
                                                                                "Cake",
                                                                                "Sweets",
                                                                                "Apple",
                                                                                "Honey")
    );

    public WinnieThePooh(
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
                friendliness);
    }

    public void makeBearSounds() {
        System.out.println("*звуки медведя*");
    }

    public void riseSomeoneUp(String name, double distance) throws PlaceIsToHighExeption {
        if (distance < 100) {
            System.out.println(this.getCreatureName()
                    + " поднял "
                    + name);
        } else {
            throw new PlaceIsToHighExeption("Здесь слишком высоко, "
                    + this.getCreatureName()
                    + " не сможет забраться наверх!");
        }
    }

    public Double getWinnieWeight() {
        return this.weight;
    }

    public Double getWinnieStrength() {
        return this.strength;
    }

    public String getWinniesName() {
        return this.getCreatureName();
    }

    public void eatHoney() {

        System.out.println(this.getCreatureName()
                + "съел весь мёд :(");
        WinniesFridge.remove("Honey");
    }

    protected int getRandomFood() {

        Random random = new Random();
        int index = random.nextInt(6);
        return index - 1;
    }

    public void getSomethingFromFridge() {
        System.out.println("Винни взял" + WinniesFridge.get(getRandomFood()));
    }

    @Override
    public boolean equals(Object object) {

        if (object instanceof WinnieThePooh) {
            WinnieThePooh w = (WinnieThePooh) object;
            return (creatureName == w.creatureName && age == w.age);
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

        return ("This Winnie The Pooh: "
                + "Name: " + this.creatureName
                + "Age: " + this.age
                + "LifeStatus: " + this.lifeStatus);
    }
}
