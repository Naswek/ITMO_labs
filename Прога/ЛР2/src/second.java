import pokemons.*;
import ru.ifmo.se.pokemon.Battle;

public class second {

    public static void main(String[] args) {

        Battle b = new Battle();

        Pichu pichu = new Pichu("Сёко", 5);
        b.addFoe(pichu);

        Marshadow marshadow = new Marshadow("Сугуру", 15);
        b.addAlly(marshadow);

        Raichu raichu = new Raichu("Сатору", 5);
        b.addFoe(raichu);

        Ekans ekans = new Ekans("Нобара", 5);
        b.addFoe(ekans);

        Pikachu pikachu = new Pikachu("Мегуми", 5);
        b.addFoe(pikachu);

        Arbok arbok = new Arbok("Итадори", 5);
        b.addFoe(arbok);

        b.go();
    }
}