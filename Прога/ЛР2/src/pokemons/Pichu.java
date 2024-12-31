package pokemons;

import attacks.status.tailwhip;
import attacks.special.thunderbolt;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Pichu extends Pokemon {

    public Pichu(String name, int level){

        super(name, level);
        super.setType(Type.ELECTRIC);
        super.setStats(20, 40, 15, 35, 35, 60);

        thunderbolt thunderbolt = new thunderbolt(90, 100, Type.ELECTRIC);
        tailwhip tail_whip = new tailwhip(0, 100, Type.NORMAL);

        super.setMove(thunderbolt, tail_whip);
    }
}
