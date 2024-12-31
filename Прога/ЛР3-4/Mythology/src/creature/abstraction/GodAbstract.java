package creature.abstraction;

import creature.enumeration.LifeStatus;
import creature.enumeration.GodType;

public abstract class GodAbstract extends CreatureAbstract {

    protected Integer childsCount;
    protected String marriage;
    protected String specialization;
    protected GodType godType;

    public GodAbstract(
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
                lifeStatus
        );
        this.childsCount = childsCount;
        this.marriage = marriage;
        this.specialization = specialization;
        this.godType = godType;
    }

    protected void getMarriage() {
        if ((this.marriage != null)
                && (this.age > 18)){

            System.out.println(this.getCreatureName()
                    + "теперь состоит в браке!");
        }
    }

    protected void makeMoreChildren() {
        if (this.marriage != null) {
            System.out.println("*дети делаются...*");

        } else {
            System.out.println("а с кем детей делать??");
        }
    }

    protected void helpPeople() {
        System.out.println("Готовьте подношения, я явился!");
    }

    protected void killMonster() {
        System.out.println("Ваш Бог явился!");
    }
}
