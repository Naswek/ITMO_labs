package place;

public interface Place {
    default void exist() {
        System.out.println("Место существует");
    }
    default void visit() {
        System.out.println("Место можно посетить :)");
    }
    default void takePhoto() {
        System.out.println("Место можно сфоткать");
    }
}
