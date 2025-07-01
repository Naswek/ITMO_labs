package itmo.programming.command;

import itmo.programming.PrettyColor;
import itmo.programming.exceptions.FailedExecution;
import itmo.programming.exceptions.NullRequestException;
import itmo.programming.input.ConsoleInput;
import itmo.programming.output.console.ConsoleControl;
import itmo.programming.output.console.ConsoleOutput;
import itmo.programming.requests.Request;
import java.io.IOException;
import java.util.Stack;

/**
 * The type Read script prompt.
 */
public class ReadScriptPrompt implements Promptable {

    private final Stack<String> scriptsNames = new Stack<>();

    private final ConsoleInput humanReader;
    private final ConsoleControl printer;
    private final ConsoleOutput consoleOutput;

    /**
     * Instantiates a new Read script prompt.
     *
     * @param humanReader   the human reader
     * @param printer       the printer
     * @param consoleOutput the console output
     */
    public ReadScriptPrompt(
            ConsoleInput humanReader,
            ConsoleControl printer,
            ConsoleOutput consoleOutput) {
        this.humanReader = humanReader;
        this.printer = printer;
        this.consoleOutput = consoleOutput;
    }

    /**
     * Is script running boolean.
     *
     * @param fileName the file name
     * @return the boolean
     * @throws IOException the io exception
     */
    public boolean isScriptRunning(String fileName) throws IOException {
        if (scriptsNames.contains(fileName)) {
            consoleOutput.print(PrettyColor.red("Скрипт уже выполняется"));
            return true;
        }
        return false;
    }

    private void setScriptStream(String fileName) throws IOException {
        humanReader.setSimulateInputStream(fileName);
        scriptsNames.add(fileName);
    }

    private void setDefaultInput() throws IOException {
        scriptsNames.pop();
        humanReader.setDefaultStream();
        consoleOutput.print(PrettyColor.green("Скрипт выполнен"));
    }

    @Override
    public Request createRequest(String filename, String login) throws
            FailedExecution, IOException {
        if (login == null) {
            return null;
        }
        if (!isScriptRunning(filename) && filename != null) {
            setScriptStream(filename);
        } else {
            throw new NullRequestException();
        }

        while (!scriptsNames.empty()) {
            readScript();
        }

        setDefaultInput();
        throw new NullRequestException();
    }

    private void readScript() throws IOException {
        while (humanReader.hasNextLine()) {
            printer.processNewCommand();
        }
        setDefaultInput();
    }
}
