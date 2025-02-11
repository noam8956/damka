import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{
    private Socket socket;
    //private ChessGame game;

    public Client(/*, ChessGame game*/) {
        //this.game = game;
        try {
            this.socket = new Socket("localhost", 5000);
            System.out.println("Connected to Chess Server");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            while(true){
                StringBuilder board = new StringBuilder();
                String res;
                while((res = in.readLine()) != null && !res.isEmpty()) { // Read until an empty line
                    board.append(res).append("\n");
                }
                System.out.println(board.toString());


                System.out.println("Enter a move:");
                String command = scanner.nextLine();
                out.println(command);

                // Read the board (multi-line response)

                while((res = in.readLine()) != null && !res.isEmpty()) { // Read until an empty line
                    board.append(res).append("\n");
                }
                System.out.println(board.toString());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Client client = new Client();
    }
}
