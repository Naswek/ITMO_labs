package attacks.physical;

import ru.ifmo.se.pokemon.*;

import java.util.Random;

public class thunderfang extends PhysicalMove {

    public thunderfang(double power, double accuracy, Type type){
    }

    protected String describe() {
        return "uses Thunder Fang!";
    }
    protected void applyOppEffects(Pokemon p){
        Effect thunderFangEffect = new Effect().chance(0.1).condition(Status.PARALYZE);
        p.addEffect(thunderFangEffect);
        Random random = new Random();
        int chance = random.nextInt(11);
        System.out.println(chance);
        if (chance == 1) {
            Effect.flinch(p);
        }


    }


}

