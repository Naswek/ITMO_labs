package creature;

public interface Hammer {
    default void hitWithHammer(){
        System.out.println("Тор наносит ударом своим Молотом!");
    }
}
