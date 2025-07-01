package itmo.programming.util;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Scanner;

/**
 * Класс для чтения ввода от пользователя и данных из файлов.
 * Предоставляет методы для работы со стандартным вводом (через консоль) и чтения данных
 * из файлов, включая JSON-файлы и текстовые файлы, с использованием потоков ввода.
 */
public class ReadHuman implements Closeable {

    /**
     * Статический объект Scanner для чтения данных из System.in.
     */
    private final Scanner scanner;

    /**
     * Instantiates a new Read human.
     *
     * @param inputStream the input stream
     */
    public ReadHuman(InputStream inputStream) {
        this.scanner = new Scanner(inputStream, StandardCharsets.UTF_8);
    }

    /**
     * Считывает строку из стандартного ввода (консоли).
     * Использует объект {@link Scanner} для получения следующей строки, введенной пользователем.
     *
     * @return строка, введенная пользователем
     */
    public String readLine() {
        return scanner.nextLine().trim();
    }

    /**
     * Проверяет, есть ли следующая строка во входном потоке.
     * Использует метод {@link Scanner#hasNextLine()} для определения наличия данных.
     *
     * @return true, если есть следующая строка, false в противном случае
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Читает содержимое файла и возвращает его как StringBuilder.
     * Открывает файл с указанным именем, использует BufferedInputStream для эффективного
     * чтения данных и собирает содержимое в StringBuilder построчно.
     *
     * @param fileName имя файла для чтения
     * @return объект StringBuilder с содержимым файла или null, если файл не найден
     * @throws IOException если возникает ошибка при работе с файлом
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
     * Читает JSON-данные из файла и возвращает их в виде строки без переносов строк.
     * Использует метод {@link #buildLine(String)} для чтения файла и заменяет все
     * символы перевода строки на пустую строку.
     *
     * @param fileName имя JSON-файла для чтения
     * @return строка с содержимым JSON-файла без переносов строк или пустая строка при ошибке
     */
    public String jsonRead(String fileName) {
        try {
            return Objects.requireNonNull(buildLine(fileName))
                    .toString()
                    .replaceAll("\\n", "");
        } catch (IOException e) {
            System.err.println("Произошла ошибка чтения json-файла");
        }
        return "";
    }

    /**
     * Close.
     *
     * @throws IOException the io exception
     */
    @Override
    public void close() throws IOException {
        scanner.close();
    }
}
