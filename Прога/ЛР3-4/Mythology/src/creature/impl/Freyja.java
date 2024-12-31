package creature.impl;

import creature.enumeration.LifeStatus;
import creature.enumeration.GodType;
import creature.Kindness;
import creature.abstraction.GodAbstract;

public class Freyja extends GodAbstract implements Kindness {

    public Freyja(
            String creatureName,
            Integer age,
            LifeStatus lifeStatus,
            Integer childsCount,
            String marriage,
            String specialization,
            GodType godType
    ) {
        super(
                creatureName,
                age,
                lifeStatus,
                childsCount,
                marriage,
                specialization,
                GodType.VANS
        );
    }

    public void feelKind() {
        System.out.println(this.getCreatureName()
                + ", бог "
                + this.specialization
                + "проявила сочувствие к людям");
    }

    public void givePeopleLove() {
        System.out.println("Богиня"
                + this.creatureName
                + "раздала людям свою любовь");
    }

    public void givePeopleHarvest() {
        System.out.println("Сегодня богиня"
                + this.getCreatureName()
                + "проявила к нам благосклонность");
    }
}
