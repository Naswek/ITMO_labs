package place.sanctuary;

import creature.enumeration.World;

public class ElvenSanctuary extends SanctuaryAbstract {

    public ElvenSanctuary(String namePlace, World world, int dangerous, String prayer, String rules){
        super(namePlace, World.ALFHEIM, dangerous, prayer, rules);
    }

    protected void prayToLight(){
        System.out.println(this.prayer + "Аминь!");
    }

    protected void bePunished(){
        System.out.println("Ты наказан и выгнан с " + this.getNamePlace() + ". Читай правила: " + this.rules);
    }

    protected void prayToDark(){
        bePunished();
    }
}
