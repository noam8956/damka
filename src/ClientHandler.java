import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            //wait for 2 clients to connect before starting the game
            System.out.println("closing connection");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
