package attacks.special;

import ru.ifmo.se.pokemon.*;

public class thunderbolt extends SpecialMove {

    public thunderbolt(double power, double accuracy, Type type){
    }

    protected String describe() {
        return "uses Thunderbolt!";
    }

    protected void applyOppEffects(Pokemon p){
        Effect ThunderoboltEffect = new Effect().chance(0.1).condition(Status.PARALYZE);
        p.setCondition(ThunderoboltEffect);
    }
}

