package itmo.programming.connection.authentication;

import itmo.programming.exepctions.InvalidPasswordException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.IntStream;

/**
 * The type Password validation.
 */
public class PasswordValidation {

    public final int forHash = 0xff;
    /**
     * The Uppercase letters.
     */
    private final char[] uppercaseLetters = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    /**
     * The Lowercase letters.
     */
    private final char[] lowercaseLetters = {
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
        'u', 'v', 'w', 'x', 'y', 'z'
    };
    /**
     * The Special symbols.
     */
    private final char[] specialSymbols = {
        '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
        '-', '_', '+', '=', '{', '}', '[', ']', '|', '\\',
        ':', ';', '"', '\'', '<', '>', ',', '.', '?', '/'
    };


    /**
     * Check password boolean.
     *
     * @param password the password
     * @return the boolean
     */
    public boolean checkPassword(String password) {
        return checkLowercase(password) && checkUppercase(password) && checkSpecialSymb(password);
    }

    /**
     * Check lowercase boolean.
     *
     * @param password the password
     * @return the boolean
     */
    public boolean checkLowercase(String password) {
        final boolean isValid = password.chars()
                .anyMatch(c -> IntStream.range(0, lowercaseLetters.length)
                        .anyMatch(i -> lowercaseLetters[i] == c));

        if (isValid) {
            return true;
        }

        throw new InvalidPasswordException("Ваш пароль не содержит символы низкого регистра");
    }

    /**
     * Check uppercase boolean.
     *
     * @param password the password
     * @return the boolean
     */
    public boolean checkUppercase(String password) {

        final boolean isValid = password.chars()
                .anyMatch(c -> IntStream.range(0, uppercaseLetters.length)
                        .anyMatch(i -> uppercaseLetters[i] == c));

        if (isValid) {
            return true;
        }

        throw new InvalidPasswordException("Ваш пароль не содержит символы высокого регистра");
    }

    /**
     * Check special symb boolean.
     *
     * @param password the password
     * @return the boolean
     */
    public boolean checkSpecialSymb(String password) {

        final boolean isValid = password.chars()
                .anyMatch(c -> IntStream.range(0, specialSymbols.length)
                        .anyMatch(i -> specialSymbols[i] == c));

        if (isValid) {
            return true;
        }
        throw new InvalidPasswordException("Ваш пароль не содержит специальные символы");
    }

    /**
     * Gets hash sha 512.
     *
     * @param password the password
     * @return the hash sha 512
     */
    public String getHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidPasswordException("Ошибка при получении хеша пароля");
        }
    }
}
