package attacks.physical;

import ru.ifmo.se.pokemon.*;

import java.util.Random;

public class ironhead extends PhysicalMove {

    public ironhead(double power, double accuracy, Type type){
    }

    protected String describe() {
        return "uses Iron Head!";
    }

    protected void applyOppEffects(Pokemon p) {
        Random random = new Random();
        int chance = random.nextInt(1,10);
        if (chance == 1) {
            Effect.flinch(p);
        }
    }
}
