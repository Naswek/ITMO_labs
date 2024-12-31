package creature.abstraction;

import creature.enumeration.LifeStatus;

public abstract class HumanAbstract extends CreatureAbstract {

    protected String gender;
    protected Double height;
    protected Double weight;
    protected Double strength;

    public HumanAbstract(
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
                lifeStatus
        );
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.strength = strength;
    }

    public void prayToGods() {
        System.out.println(this.getCreatureName()
                + " помолился и отчистил свои грехи");
    }

    public void play(){
        System.out.println(this.getCreatureName() + " играет в доту :)");
    }

    public void hardWork() {
        System.out.println(this.getCreatureName() + " тяжело работает... ");
    }
}
