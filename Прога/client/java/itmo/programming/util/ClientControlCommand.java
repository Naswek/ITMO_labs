package itmo.programming.util;

import itmo.programming.command.CommandsMap;
import itmo.programming.command.ParamCheckable;
import itmo.programming.command.Promptable;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.exceptions.NullRequestException;
import itmo.programming.exceptions.RequestCreationException;
import itmo.programming.exepctions.LogoutException;
import itmo.programming.requests.Request;
import itmo.programming.responses.ValidationResponse;
import java.io.IOException;

/**
 * The type Client control command.
 */
public class ClientControlCommand {

    private final CommandsMap commandsMap;
    private final String login;

    /**
     * Instantiates a new Client control command.
     *
     * @param commandsMap the commands map
     * @param login       the login
     */
    public ClientControlCommand(CommandsMap commandsMap, String login) {
        this.commandsMap = commandsMap;
        this.login = login;
    }

    /**
     * Convert input request.
     *
     * @param input the input
     * @return the request
     * @throws FailedExecution the failed execution
     * @throws IOException     the io exception
     */
    public Request convertInput(String input) throws FailedExecution, IOException {
        final String[] tokens = getTokens(input);
        if (isCommand(tokens[0])) {
            return createRequest(tokens);
        }
        throw new RequestCreationException("Такой команды не существует");
    }

    /**
     * Is command boolean.
     *
     * @param command the command
     * @return the boolean
     */
    public boolean isCommand(String command) {
        return commandsMap.getCommands().containsKey(command);
    }

    /**
     * Create request request.
     *
     * @param tokens the tokens
     * @return the request
     * @throws IOException          the io exception
     * @throws FailedExecution      the failed execution
     * @throws NullRequestException the null request exception
     */
    public Request createRequest(String[] tokens) throws
            IOException,
            FailedExecution,
            NullRequestException {

        final String command = tokens[0];

        if (commandsMap.getCommands().containsKey(command)) {
            return parseRequest(tokens);
        }
        throw new RequestCreationException("такой команды не существует");
    }

    /**
     * Create request request.
     *
     * @param response the response
     * @return the request
     * @throws FailedExecution the failed execution
     */
    public Request createRequest(ValidationResponse response) throws FailedExecution {
        final Promptable prompt = getPrompt(response.command());
        if (prompt instanceof ParamCheckable check) {
            return check.checkParameter(response);
        } else {
            throw new RequestCreationException("класс не поддерживает проверку параметра");
        }
    }

    /**
     * Parse request request.
     *
     * @param tokens the tokens
     * @return the request
     * @throws IOException     the io exception
     * @throws FailedExecution the failed execution
     */
    public Request parseRequest(String[] tokens) throws IOException, FailedExecution {
        final Promptable prompt = getPrompt(tokens[0]);
        final String parameter = tokens.length > 1 ? tokens[1] : null;
        Request request = prompt.createRequest(parameter, login);
        if (request != null) {
            return request;
        }
        throw new LogoutException("вы должны залогиниться. напишите команду 'login'");
    }

    /**
     * Get tokens string [ ].
     *
     * @param text the text
     * @return the string [ ]
     */
    public String[] getTokens(String text) {
        return text.trim().split("\\s+");
    }

    /**
     * Gets prompt.
     *
     * @param command the command
     * @return the prompt
     */
    public Promptable getPrompt(String command) {
        return commandsMap.getCommands().get(command);
    }
}
