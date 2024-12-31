package pokemons;

import attacks.physical.bulldoze;
import attacks.special.mudbomb;
import attacks.status.leer;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Ekans extends Pokemon {

    public Ekans(String name, int level) {

        super(name, level);
        super.setType(Type.POISON);
        super.setStats(35, 60, 44, 40, 54, 55);

        bulldoze bulldoze = new bulldoze(60, 100, Type.GROUND);
        leer leer = new leer(0, 100, Type.NORMAL);
        mudbomb mud_bomb = new mudbomb(65, 85, Type.GROUND);

        super.setMove(bulldoze, leer, mud_bomb);
    }
}
