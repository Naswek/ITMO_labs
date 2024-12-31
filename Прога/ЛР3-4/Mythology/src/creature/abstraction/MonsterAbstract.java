package creature.abstraction;

import creature.enumeration.LifeStatus;
import creature.enumeration.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public abstract class MonsterAbstract extends CreatureAbstract {

    protected Integer killsCount;
    protected World world;
    protected ArrayList<String> Weapon  = new ArrayList<>(Arrays.asList("Stick",
                                                                        "Sword",
                                                                        "Bow",
                                                                        "Claws",
                                                                        "Fangs"
    ));

    public MonsterAbstract(
            String creatureName,
            Integer age,
            World world,
            Integer killsCount,
            LifeStatus lifeStatus
    ) {
        super(
            creatureName,
            age,
            lifeStatus
        );
        this.killsCount = killsCount;
        this.world = world;
    }

    public void destroyEverything(){
        System.out.println("КРУШИТЬ ЛОМАТЬ УНИЧТОЖАТЬ");
    }

    public void killEveryone(){
        System.out.println("*геноцид*");
    }

    protected int getRandomWeapon(){
        Random random = new Random();
        int index = random.nextInt(6);
        return index-1;
    }

    public String getWeapon(){
        return Weapon.get(getRandomWeapon());
    }
}
