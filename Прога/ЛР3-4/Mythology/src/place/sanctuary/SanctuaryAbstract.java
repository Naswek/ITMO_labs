package place.sanctuary;

import place.PlaceAbstract;
import creature.enumeration.World;

public abstract class SanctuaryAbstract extends PlaceAbstract {

    protected String prayer;
    protected String rules;

    protected SanctuaryAbstract(String namePlace, World world, int dangerous, String prayer, String rules) {
        super(namePlace, world, dangerous);
        this.prayer = prayer;
        this.rules = rules;
    }

    public String getSanctuaryName() {
        return this.getNamePlace();
    }

    protected void readPrayer(String prayer) {
        System.out.println(prayer);
    }

    protected void cleanseSins() {
        System.out.println("Ваши грехи, так уж и быть, прощены");
    }
}
