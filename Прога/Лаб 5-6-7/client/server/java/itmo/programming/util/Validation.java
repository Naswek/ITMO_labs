package itmo.programming.util;

import itmo.programming.object.HumanBeing;

/**
 * The type Validation.
 */
public class Validation {

    /**
     * Validate human boolean.
     *
     * @param humanBeing the human being
     * @return the boolean
     */
    public boolean validateHuman(HumanBeing humanBeing) {
        return humanBeing != null;
    }

    /**
     * Проверяет, является ли строка числовым значением.
     *
     * @param str строка для проверки
     * @return true если строка представляет собой число, false в противном случае
     */
    public boolean inNumeric(String str) {
        return str != null && str.matches("-?\\d+(\\.\\d+)?");
    }
}
