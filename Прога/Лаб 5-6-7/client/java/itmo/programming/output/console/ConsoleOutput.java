package itmo.programming.output.console;

import itmo.programming.connection.Console;

/**
 * The type Console output.
 */
public class ConsoleOutput implements Console {

    @Override
    public void printErr(Object object) {
        System.err.println(object);
    }

    @Override
    public void print(Object object) {
        System.out.println(object);
    }
}
