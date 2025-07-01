package itmo.programming.object;

import java.io.Serializable;

/**
 * Содержит возможные варианты оружия, такие как молоток, топор, винтовка и бита,
 * которые могут быть ассоциированы с человеком в коллекции.
 */
public enum WeaponType  implements Serializable {

    /**
     * Тип оружия: молоток.
     */
    HAMMER,

    /**
     * Тип оружия: топор.
     */
    AXE,

    /**
     * Тип оружия: винтовка.
     */
    RIFLE,

    /**
     * Тип оружия: бита.
     */
    BAT;

    /**
     * Contains boolean.
     *
     * @param weapon the weapon
     * @return the boolean
     */
    public static boolean contains(String weapon) {
        for (WeaponType w : WeaponType.values()) {
            if (w.toString().equalsIgnoreCase(weapon)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets weapon type.
     *
     * @param weapon the weapon
     * @return the weapon type
     */
    public static WeaponType getWeaponType(String weapon) {
        for (WeaponType w : WeaponType.values()) {
            if (w.toString().equalsIgnoreCase(weapon)) {
                return w;
            }
        }
        return null;
    }
}
