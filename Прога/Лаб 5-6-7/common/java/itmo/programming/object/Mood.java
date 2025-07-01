package itmo.programming.object;

import java.io.Serializable;

/**
 * Содержит возможные варианты настроения, такие как грусть, тоска, спокойствие, уныние и безумие,
 * которые могут быть ассоциированы с человеком в коллекции.
 */
public enum Mood  implements Serializable {

    /**
     * Тип настроения: грусть.
     */
    SORROW,
    /**
     * Тип настроения: тоска.
     */
    LONGING,
    /**
     * Тип настроения: уныние.
     */
    GLOOM,
    /**
     * Тип настроения: спокойствие.
     */
    CALM,
    /**
     * Тип настроения: безумие.
     */
    FRENZY;

    /**
     * Contains boolean.
     *
     * @param mood the mood
     * @return the boolean
     */
    public static boolean contains(String mood) {
        for (Mood m : Mood.values()) {
            if (m.toString().equalsIgnoreCase(mood)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets mood.
     *
     * @param mood the mood
     * @return the mood
     */
    public static Mood getMood(String mood) {
        for (Mood m : Mood.values()) {
            if (m.toString().equalsIgnoreCase(mood)) {
                return m;
            }
        }
        return null;
    }
}
