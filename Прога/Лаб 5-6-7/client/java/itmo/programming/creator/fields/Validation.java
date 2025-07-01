package itmo.programming.creator.fields;

import itmo.programming.object.Mood;
import itmo.programming.object.WeaponType;

/**
 * The type Validation.
 */
public class Validation {

    private final int y1Limit = 673;
    private final int x1Limit = -449;
    private final int impactSpeedLimit = -442;

    /**
     * Is valid x boolean.
     *
     * @param x1Value the x 1 value
     * @return the boolean
     */
    public boolean isValidX(long x1Value) {
        return x1Value > x1Limit;
    }

    /**
     * Is valid y boolean.
     *
     * @param y1Value the y 1 value
     * @return the boolean
     */
    public boolean isValidY(float y1Value) {
        return y1Value < y1Limit && y1Value != 0;
    }

    /**
     * Is valid impact speed boolean.
     *
     * @param impactSpeed the impact speed
     * @return the boolean
     */
    public boolean isValidImpactSpeed(float impactSpeed) {
        return impactSpeed > impactSpeedLimit;
    }

    /**
     * Is valid input for boolean boolean.
     *
     * @param input the input
     * @return the boolean
     */
    public boolean isValidInputForBoolean(int input) {
        return (input == 1) || (input == 2);
    }

    /**
     * Is valid input for mood boolean.
     *
     * @param input the input
     * @return the boolean
     */
    public boolean isValidInputForMood(int input) {
        return (input <= Mood.values().length) && (input > 0);
    }

    /**
     * Is valid input for weapon boolean.
     *
     * @param input the input
     * @return the boolean
     */
    public boolean isValidInputForWeapon(int input) {
        return (input <= WeaponType.values().length) && (input > 0);
    }

    /**
     * Is input for enum is int boolean.
     *
     * @param input the input
     * @return the boolean
     */
    public boolean isInputForEnumIsInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
