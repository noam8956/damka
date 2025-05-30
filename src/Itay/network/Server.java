package Itay.network;

import Itay.model.Move;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Server {
    private ServerSocket serverSocket;
    private final List<ClientHandler> clients = new ArrayList<>();
    private Consumer<Message> onMessageReceived;

    public Server(int port, Consumer<Message> onMessageReceived) throws IOException {
        this.onMessageReceived = onMessageReceived;
        serverSocket = new ServerSocket(port);
    }

    public void start() {
        Thread acceptThread = new Thread(() -> {
            try {
                while (clients.size() < 2) {
                    Socket socket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(socket);
                    clients.add(handler);
                    handler.start();
                    System.out.println("Player " + clients.size() + " connected.");
                }

                // Notify both players that the game is starting
                broadcast(Message.system(Message.Type.START, "Game Started"));

            } catch (IOException e) {
                System.err.println("Server error: " + e.getMessage());
            }
        });

        acceptThread.start();
    }

    public void broadcast(Message message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    private class ClientHandler {
        private final Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private Thread listenerThread;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }

        public void start() {
            listenerThread = new Thread(() -> {
                try {
                    while (!socket.isClosed()) {
                        Message message = (Message) in.readObject();
                        onMessageReceived.accept(message); // Send to controller
                    }
                } catch (Exception e) {
                    System.err.println("ClientHandler error: " + e.getMessage());
                }
            });
            listenerThread.start();
        }

        public void sendMessage(Message message) {
            try {
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                System.err.println("Failed to send to client: " + e.getMessage());
            }
        }
    }

    public void close() {
        try {
            for (ClientHandler client : clients) {
                client.socket.close();
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
