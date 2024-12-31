package pokemons;

import attacks.physical.slam;
import attacks.status.rest;
import attacks.status.tailwhip;
import attacks.special.thunderbolt;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Raichu extends Pokemon {

    public Raichu(String name, int level){

        super(name, level);
        super.setType(Type.ELECTRIC);
        super.setStats(60, 90, 55, 90, 80, 110);

        thunderbolt thunderbolt = new thunderbolt(90, 100, Type.ELECTRIC);
        tailwhip tail_whip = new tailwhip(0, 100, Type.NORMAL);
        slam slam = new slam(80, 75, Type.NORMAL);
        rest rest = new rest(0, 0, Type.PSYCHIC);

        super.setMove(thunderbolt, tail_whip, slam, rest);
    }
}
