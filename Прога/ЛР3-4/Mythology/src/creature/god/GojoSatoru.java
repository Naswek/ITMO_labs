package creature.god;

import creature.abstraction.GodAbstract;
import creature.enumeration.LifeStatus;
import creature.enumeration.GodType;

public class GojoSatoru extends GodAbstract {

    protected String technique;
    protected Integer cursedEnergy;
    protected String Grade;
    protected Integer charm;

    public GojoSatoru(
            String creatureName,
            Integer age,
            LifeStatus lifeStatus,
            Integer childsCount,
            String marriage,
            String specialization,
            GodType godType,
            String technique,
            Integer cursedEnergy,
            String Grade,
            Integer charm
    ) {
        super(
                creatureName,
                age,
                lifeStatus,
                childsCount,
                marriage,
                specialization,
                godType
        );
        this.technique = technique;
        this.cursedEnergy = cursedEnergy;
        this.Grade = Grade;
        this.charm = charm;
    }

    public String getGojoSatoruName() {
        return this.getCreatureName();
    }

    public void usePurple() {
        this.technique = "Purple";
        kill();
        System.out.println("*враг унижен* Между небом и землей лишь я достоин");
    }

    public void useBlue() {
        this.technique = "Blue";
        kill();
        System.out.println("*враг унижен* Как же скучно быть лучшим во всем, что я делаю.");
    }

    public void useRed() {
        this.technique = "Red";
        kill();
        System.out.println( "*враг унижен* Не бойся быть слабым. Просто запомни, что ты никогда не будешь сильнее меня");
    }

    public void eatSweets() {
        System.out.println("+ 10 баллов к счастью");
         this.cursedEnergy += 10;
    }

    public Integer useCharm(){
        return (this.charm*10);
    }

    public void temptGirlsAndMans() {
        useCharm();
        System.out.println("Не хочу хвастаться, но я чертовски красивый.");
    }
}
