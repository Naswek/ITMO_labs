package pokemons;

import attacks.physical.slam;
import attacks.status.tailwhip;
import attacks.special.thunderbolt;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Pikachu extends Pokemon {

    public Pikachu(String name, int level){

        super(name, level);
        super.setType(Type.ELECTRIC);
        super.setStats(35, 55, 40, 50, 50, 90);

        thunderbolt thunderbolt = new thunderbolt(90, 100, Type.ELECTRIC);
        tailwhip tail_whip = new tailwhip(0, 100, Type.NORMAL);
        slam slam = new slam(80, 75, Type.NORMAL);

        super.setMove(thunderbolt, tail_whip, slam);
    }
}
