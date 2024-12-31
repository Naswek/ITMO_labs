package creature;

public interface Kindness {

    default void takeDeceasedWorriers() {
        System.out.println("Покойтесь с миром, мои воины...");
    }
}
