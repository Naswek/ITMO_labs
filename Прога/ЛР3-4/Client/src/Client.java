import java.io.*;
import java.net.Socket;


public class Client {
    public static void main(String[] args) throws IOException {

        String host = "localhost";
        int port = 2525;
        String userInput;

        try(
                Socket socket = new Socket(host, port);
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true
                );
                BufferedReader inConsole = new BufferedReader(
                        new InputStreamReader(System.in)
                );
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
        ){
             System.out.println("Подключён к серверу: "
                     + host
                     + ":"
                     + port
             );
             outer: while (true) {
                System.out.println("Введите 1, если хотите написать "
                        + "номера предложений ручками и 2, "
                        + "если хотите закинуть файлик");

                 userInput = inConsole.readLine();

                 if (userInput.equals("exit")) {
                     System.out.println("Бай-бай!");
                     break;
                 }

                    //input
                if (userInput.equals("1")) {
                    System.out.println("Введите цифры предложений");
                    while (true) {
                        userInput = inConsole.readLine();

                        if (userInput.equals("exit")) {
                            System.out.println("Бай-бай!");
                            break outer;

                        } else {
                            out.println(userInput);
                            System.out.println(in.readLine());
                        }
                    }

                    //script
                } else if (userInput.equals("2")) {
                    System.out.println("Одну секунду...");

                        try (BufferedReader fileReader = new BufferedReader(
                                new FileReader("text.txt"));
                                ) {
                            String sentenceNumber;

                            while ((sentenceNumber = fileReader.readLine()) != null) {
                                out.println(sentenceNumber);
                                System.out.println(in.readLine());
                        }
                    } catch (FileNotFoundException e) {
                            System.out.println("Файла нет :/");
                    }
                }
            }
        }
    }
}


//streamAPI