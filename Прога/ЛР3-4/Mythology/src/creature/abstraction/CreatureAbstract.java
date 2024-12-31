package creature.abstraction;

import creature.Creature;
import creature.enumeration.LifeStatus;

public class CreatureAbstract implements Creature {
    protected String creatureName;
    protected Integer age;
    protected LifeStatus lifeStatus;

    public CreatureAbstract(
            String creatureName,
            Integer age,
            LifeStatus lifeStatus
    ) {
        this.creatureName = creatureName;
        this.age = age;
        this.lifeStatus = lifeStatus;
    }

    protected String getCreatureName() {
        return creatureName;
    }

    protected Integer getAge() {
        return age;
    }
}
