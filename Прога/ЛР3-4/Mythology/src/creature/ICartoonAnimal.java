package creature;

public interface ICartoonAnimal {
    default void talkLikePeople() {
        System.out.println("Это животное может говорить как человек");
    }
}
