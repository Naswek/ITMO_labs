package creature.impl;

import creature.enumeration.LifeStatus;
import creature.enumeration.GodType;
import creature.Hammer;
import creature.abstraction.GodAbstract;

public class Thor extends GodAbstract implements Hammer {

    public Thor(
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
                LifeStatus.ALIVE,
                childsCount,
                marriage,
                specialization,
                GodType.ASGARDS
        );
    }

    public void protectAsgard() {
        System.out.println(this.getCreatureName()
                + ", бог "
                + this.specialization
                + "готов защитить свой дом");
    }

    public void drinkBeer() {
        sleep();
    }
}
