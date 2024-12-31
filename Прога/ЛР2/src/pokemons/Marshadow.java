package pokemons;

import attacks.physical.ironhead;
import attacks.special.icebeam;
import attacks.special.thunderbolt;
import attacks.status.thunderwave;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Marshadow extends Pokemon {

    public Marshadow(String name, int level){

        super(name, level);
        super.setType(Type.FIGHTING, Type.GHOST);
        super.setStats(90, 125, 80, 90, 90, 125);

        thunderbolt thunderbolt = new thunderbolt(90, 100, Type.ELECTRIC);
        ironhead iron_head = new ironhead(80, 100, Type.STEEL);
        icebeam ice_beam = new icebeam(90, 100, Type.ICE);
        thunderwave thunder_wave = new thunderwave(0, 90, Type.ELECTRIC);

        super.setMove(iron_head, thunder_wave);
    }
}
