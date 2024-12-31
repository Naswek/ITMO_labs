package creature.monster;

import creature.abstraction.MonsterAbstract;
import creature.enumeration.LifeStatus;
import creature.enumeration.World;

public class Pudge extends MonsterAbstract {

    protected double weight;
    protected int hungerLevel;

    public Pudge(
            String creatureName,
            Integer age,
            LifeStatus lifeStatus,
            World world,
            Integer killsCount,
            Double weight,
            Integer hungerLevel
    ) {
        super(
                creatureName,
                age,
                world,
                killsCount,
                lifeStatus
        );
        this.weight = weight;
        this.hungerLevel = hungerLevel;
    }

    public String getPudgeName() {
        return this.getCreatureName();
    }

    public void PudgeIsEating() {
        System.out.println("Мммм.... свежее мясо");
        weight = weight + 0.5;
        hungerLevel = hungerLevel - 1;
    }

    public void PudgeIsHungry() {
        System.out.println("Тушку мне! - говорит " + getPudgeName());
        hungerLevel = hungerLevel + 1;
    }

    public void hungerLevelTooHigh(){
        if (hungerLevel > 10){
            destroyEverything();
            killEveryone();
            PudgeIsEating();
        }
    }

    public String getWeapon(){
        return this.creatureName + " вынимает хук!";
    }

    public void weightIsTooHigh(){
        System.out.println("Пуговичка оторвалась :(");
    }
}
