package attacks.status;

import ru.ifmo.se.pokemon.*;

public class rest extends StatusMove {

    public rest(double power, double accuracy, Type type){
    }

    protected String describe() {
        return "uses Rest!";
    }

    protected void applySelfEffects(Pokemon p){
        int HP_before = (int) p.getStat(Stat.HP);
        Effect RestEffect = new Effect().condition(Status.SLEEP).turns(2).stat(Stat.HP, HP_before);
        p.setCondition(RestEffect);
    }
}
