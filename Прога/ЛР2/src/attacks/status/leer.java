package attacks.status;

import ru.ifmo.se.pokemon.*;

public class leer extends StatusMove {

    public leer(double power, double accuracy, Type type){
    }

    protected String describe() {
        return "uses Leer!";
    }

    protected void applyOppEffects(Pokemon p){
        Effect LeerEffect = new Effect().stat(Stat.DEFENSE, -1);
        p.addEffect(LeerEffect);
    }
}
