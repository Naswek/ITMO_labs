package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.requests.Request;
import itmo.programming.responses.MessageResponse;
import java.io.IOException;

/**
 * The type Read script command.
 */
public class ReadScriptCommand implements Command {

    @Override
    public MessageResponse execute(Request request) throws FailedExecution, IOException {
        return null;
    }

    @Override
    public String getDescription() {
        return PrettyColor.yellow(getCommandName()) + " - выполняет команды из скрипта";
    }

    @Override
    public String getCommandName() {
        return "execute_script script.txt";
    }
}
