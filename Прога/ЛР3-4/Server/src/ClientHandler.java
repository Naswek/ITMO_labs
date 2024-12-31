import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ClientHandler implements Runnable {

    private final PrintWriter out;
    private final BufferedReader in;

    public ClientHandler(Socket socket) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.equals("exit")) {
                    break;
                }
                String specificLine = Files.readAllLines(Paths.get("my_text.txt")).get(Integer.parseInt(line));
                out.println(specificLine);
                System.out.println(specificLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
