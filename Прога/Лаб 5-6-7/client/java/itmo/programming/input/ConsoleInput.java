package itmo.programming.input;

import itmo.programming.exceptions.RequestCreationException;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Stack;

/**
 * The type Console input.
 */
public class ConsoleInput implements Closeable {

    private Scanner scanner;
    private InputStream currentInputStream;
    private final Stack<InputStream> inputStreamStack;


    /**
     * Instantiates a new Console input.
     *
     * @param inputStream the input stream
     */
    public ConsoleInput(InputStream inputStream) {
        this.scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
        this.inputStreamStack = new Stack<>();
        inputStreamStack.push(System.in);
    }

    /**
     * Read line string.
     *
     * @return the string
     */
    public String readLine() {
        return scanner.nextLine().trim();
    }

    /**
     * Has next line boolean.
     *
     * @return the boolean
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Build line string builder.
     *
     * @param fileName the file name
     * @return the string builder
     * @throws IOException the io exception
     */
    public StringBuilder buildLine(String fileName) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(fileName);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {
            int i;
            final StringBuilder word = new StringBuilder();

            while ((i = bufferedInputStream.read()) != -1) {
                word.append((char) i);
            }
            return word;
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден");
        }
        return null;
    }

    /**
     * Sets simulate input stream.
     *
     * @param fileName the file name
     * @throws FileNotFoundException the file not found exception
     */
    public void setSimulateInputStream(String fileName) throws FileNotFoundException {
        final File file = new File(fileName);
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            throw new RequestCreationException("Невозможно прочитать файл: " + fileName);
        }
        this.currentInputStream = new FileInputStream(file);
        setInputStream(currentInputStream);
        inputStreamStack.push(currentInputStream);
    }

    /**
     * Sets default stream.
     */
    public void setDefaultStream() {
        inputStreamStack.pop();
        setInputStream(inputStreamStack.firstElement());
    }

    /**
     * Sets input stream.
     *
     * @param inputStream the input stream
     */
    public void setInputStream(InputStream inputStream) {
        this.currentInputStream = inputStream;
        this.scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * Is simulated stream boolean.
     *
     * @return the boolean
     */
    public boolean isSimulatedStream() {
        return this.currentInputStream instanceof FileInputStream;
    }

    @Override
    public void close() throws IOException {
        if (this.currentInputStream != null && this.currentInputStream != System.in) {
            this.currentInputStream.close();
        }
        scanner.close();
    }
}
