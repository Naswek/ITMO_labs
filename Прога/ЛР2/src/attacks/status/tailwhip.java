package attacks.status;

import ru.ifmo.se.pokemon.*;

public class tailwhip extends StatusMove {

    public tailwhip(double power, double accuracy, Type type){
    }

    protected String describe() {
        return "uses Tail Whip!";
    }

    protected void applyOppEffects(Pokemon p){
        Effect TailWhipEffect = new Effect().stat(Stat.DEFENSE, -1);
        p.addEffect(TailWhipEffect);
    }
}
