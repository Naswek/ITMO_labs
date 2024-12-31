package creature.human;

import creature.abstraction.HumanAbstract;
import creature.enumeration.LifeStatus;
import exception.PlaceIsToHighExeption;

public class ChristopherRobin extends HumanAbstract {

    public ChristopherRobin(
            String creatureName,
            Integer age,
            String gender,
            Double height,
            Double weight,
            Double strength,
            LifeStatus lifeStatus
    ) {
        super(
                creatureName,
                age,
                gender,
                height,
                weight,
                strength,
                lifeStatus
        );
    }

    public void helpFriends() {}

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

    public String getChristopherRobinName() {
        return this.getCreatureName();
    }

    public Double getChristopherWeight() {
        return this.weight;
    }

    public double getChristopherStrength() {
        return this.strength;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ChristopherRobin) {
            ChristopherRobin c = (ChristopherRobin) object;
            return (creatureName == c.creatureName && age == c.age);
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
        return ("This ChristopherRobin: "
                + "Name: " + this.creatureName
                + "Age: " + this.age
                + "LifeStatus: " + this.lifeStatus);
    }
}
