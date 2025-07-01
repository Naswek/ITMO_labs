package itmo.programming;

/**
 * The type Pretty color.
 */
public class PrettyColor {

    /**
     * Red string.
     *
     * @param text the text
     * @return the string
     */
    public static String red(String text) {
        return "\u001B[31m" + text + "\u001B[0m";
    }

    /**
     * Green string.
     *
     * @param text the text
     * @return the string
     */
    public static String green(String text) {
        return "\u001B[32m" + text + "\033[0m";
    }

    /**
     * Yellow string.
     *
     * @param text the text
     * @return the string
     */
    public static String yellow(String text) {
        return "\u001B[33m" + text + "\033[0m";
    }
}
