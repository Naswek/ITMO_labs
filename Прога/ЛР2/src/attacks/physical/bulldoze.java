package attacks.physical;

import ru.ifmo.se.pokemon.*;

public class bulldoze extends PhysicalMove {

    public bulldoze(double power, double accuracy, Type type){
    }

    protected String describe() {
        return "uses Bulldoze!";
    }

    protected void applyOppEffects(Pokemon p){
        Effect BulldozeEffect = new Effect().stat(Stat.SPEED, -1);
        p.addEffect(BulldozeEffect);
    }
}
