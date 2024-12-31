package pokemons;


import attacks.physical.bulldoze;
import attacks.physical.thunderfang;
import attacks.special.mudbomb;
import attacks.status.leer;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Arbok extends Pokemon{

    public Arbok(String name, int level){

        super(name, level);
        super.setType(Type.POISON);
        super.setStats(60, 95, 69, 65, 79, 80);

        bulldoze bulldoze = new bulldoze(60, 100, Type.GROUND);
        leer leer = new leer(0, 100, Type.NORMAL);
        mudbomb mud_bomb = new mudbomb(65, 85, Type.GROUND);
        thunderfang thunder_fang = new thunderfang(65, 95, Type.ELECTRIC);

        super.setMove(bulldoze, leer, mud_bomb, thunder_fang);
    }

}
