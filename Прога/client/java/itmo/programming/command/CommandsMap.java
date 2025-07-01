package itmo.programming.command;

import itmo.programming.connection.authentication.GettingUserData;
import itmo.programming.creator.PromptObjects;
import itmo.programming.input.ConsoleInput;
import itmo.programming.output.console.ConsoleControl;
import itmo.programming.output.console.ConsoleOutput;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Commands map.
 */
public class CommandsMap {

    private final PromptObjects humanCreator;
    private Map<String, Promptable> commands;
    private final ConsoleInput readHuman;
    private final ConsoleControl consolePrinter;
    private final ConsoleOutput consoleOutput;

    /**
     * Instantiates a new Commands map.
     *
     * @param humanCreator   the human creator
     * @param readHuman      the read human
     * @param consolePrinter the console printer
     * @param consoleOutput  the console output
     */
    public CommandsMap(PromptObjects humanCreator,
                       ConsoleInput readHuman,
                       ConsoleControl consolePrinter,
                       ConsoleOutput consoleOutput) {
        this.humanCreator = humanCreator;
        this.readHuman = readHuman;
        this.consolePrinter = consolePrinter;
        this.consoleOutput = consoleOutput;
        initializeCommand();
    }

    private void initializeCommand() {
        commands = new HashMap<>() {{
                put("add", new AddPrompt(humanCreator));
                put("help", new HelpPrompt());
                put("info", new InfoPrompt());
                put("show", new ShowPrompt());
                put("sort", new SortPrompt());
                put("clear", new ClearPrompt());
                put("updateById", new UpdatePrompt(humanCreator));
                put("remove_at", new RemoveByIndexPrompt());
                put("remove_by_id", new RemoveByIdPrompt());
                put("remove_first", new RemoveFirstPrompt());
                put("filter_contains_name", new FilterContainsNamePrompt());
                put("count_greater_than_car", new CountGreaterThanCarPrompt());
                put("count_less_than_soundtrack_name",
                        new CountLessThanSoundtrackNamePrompt());
                put("execute_script", new ReadScriptPrompt(readHuman,
                        consolePrinter, consoleOutput));
                put("exit", new ExitPrompt());
                put("logout", new LogoutPrompt());
                put("user_objects", new UserObjectsPrompt());
                put("login", new LoginPrompt(new GettingUserData(readHuman, consoleOutput)));
            }};
    }

    /**
     * Gets commands.
     *
     * @return the commands
     */
    public Map<String, Promptable> getCommands() {
        return Collections.unmodifiableMap(commands);
    }
}
