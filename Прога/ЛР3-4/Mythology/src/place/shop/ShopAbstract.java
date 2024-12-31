package place.shop;

import place.PlaceAbstract;
import creature.enumeration.World;

public abstract class ShopAbstract extends PlaceAbstract {

    protected int timeOpen;
    protected int timeClose;
    protected String shopSpeciality;

    protected ShopAbstract(String namePlace, World world, int dangerous, int timeOpen, int timeClose, String shopSpeciality) {
        super(namePlace, world, dangerous);
        this.timeOpen = timeOpen;
        this.timeClose = timeClose;
        this.shopSpeciality = shopSpeciality;
    }

    protected void opened() {
        System.out.println("Магазин открыт");
    }
    protected void closed() {
        System.out.println("Магазин закрыт");
    }
}
