package place;

import creature.enumeration.World;

public abstract class PlaceAbstract implements Place {
    String namePlace;
    World world;
    int dangerous;

    protected PlaceAbstract(String namePlace, World world, int dangerous) {
        this.namePlace = namePlace;
        this.world = world;
        this.dangerous = dangerous;
    }

    public String getNamePlace(){
        return this.namePlace;
    }

    public int getDangerous(){
        return this.dangerous;
    }
}
