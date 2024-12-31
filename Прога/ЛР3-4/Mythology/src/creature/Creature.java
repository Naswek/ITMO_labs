package creature;

public interface Creature {

    public default void fight(){
        System.out.println("Началась бойня!");
    }

    public default void eat(){
        System.out.println("надо покушац...");
    }

    public default void sleep(){
        System.out.println("Bzzz-");
    }

    public default void kill(){
        System.out.println("*кто-то испустил дух*");
    }
}
