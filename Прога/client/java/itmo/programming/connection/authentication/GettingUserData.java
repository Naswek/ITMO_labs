package itmo.programming.connection.authentication;

import itmo.programming.PrettyColor;
import itmo.programming.exepctions.InvalidPasswordException;
import itmo.programming.input.ConsoleInput;
import itmo.programming.output.console.ConsoleOutput;
import itmo.programming.requests.AuthenticationRequest;

/**
 * The type Getting user data.
 */
public class GettingUserData {

    private final ConsoleInput consoleInput;
    private final ConsoleOutput consoleOutput;
    private final PasswordValidation passwordValidation;


    /**
     * Instantiates a new Getting user data.
     *
     * @param consoleInput  the console input
     * @param consoleOutput the console output
     */
    public GettingUserData(ConsoleInput consoleInput, ConsoleOutput consoleOutput) {
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
        this.passwordValidation = new PasswordValidation();
    }

    /**
     * Start authentication request.
     *
     * @return the authentication request
     */
    public AuthenticationRequest startAuthentication() {
        while (true) {
            consoleOutput.print("вы зарегистрированный пользователь? Формат ввода: [да/нет]");
            final String input = consoleInput.readLine();
            if (checkInput(input)) {
                if (checkRegistration(input)) {
                    return logging();
                } else {
                    return startRegistration();
                }
            } else {
                consoleOutput.print("Введите [да/нет] для ответа на вопрос");
            }
        }
    }

    public boolean checkInput(String input) {
        return input.equalsIgnoreCase("да") || input.equalsIgnoreCase("нет");
    }

    /**
     * Check registration boolean.
     *
     * @param input the input
     * @return the boolean
     */
    public boolean checkRegistration(String input) {
        return input.equalsIgnoreCase("да");
    }

    /**
     * Logging authentication request.
     *
     * @return the authentication request
     */
    public AuthenticationRequest logging() {
        return new AuthenticationRequest(
                askLogin(), passwordValidation.getHash(askPassword()), true);
    }

    /**
     * Start registration authentication request.
     *
     * @return the authentication request
     */
    public AuthenticationRequest startRegistration() {
        consoleOutput.print("давайте регистрироваться");
        return new AuthenticationRequest(
                askLogin(), passwordValidation.getHash(askPassword()), false);
    }

    /**
     * Ask login string.
     *
     * @return the string
     */
    public String askLogin() {
        while (true) {
            consoleOutput.print(PrettyColor.yellow("Введите логин: "));
            final String login = consoleInput.readLine();
            if (!login.trim().isEmpty()) {
                return login;
            }
        }
    }

    /**
     * Ask password string.
     *
     * @return the string
     */
    public String askPassword() {
        while (true) {
            try {
                consoleOutput.print(PrettyColor.yellow("Введите пароль. Пароль должен "
                        + " содержать символы нижнего, верхнего регистра и спец символы: "));
                final String password = consoleInput.readLine();
                if (!password.equals(" ") && passwordValidation.checkPassword(password)) {
                    return password;
                }
            } catch (InvalidPasswordException e) {
                consoleOutput.printErr(e.getMessage());
            }
        }
    }
}
