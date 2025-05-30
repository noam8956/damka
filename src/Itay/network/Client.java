package Itay.network;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Thread listenerThread;
    private Consumer<Message> onMessageReceived;

    public Client(String host, int port, Consumer<Message> onMessageReceived) throws IOException {
        this.onMessageReceived = onMessageReceived;
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        listenerThread = new Thread(this::listen);
        listenerThread.start();
    }

    private void listen() {
        try {
            while (!socket.isClosed()) {
                Message message = (Message) in.readObject();
                onMessageReceived.accept(message);
            }
        } catch (Exception e) {
            System.err.println("Client listener error: " + e.getMessage());
        }
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            System.err.println("Client failed to send: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (listenerThread != null) listenerThread.interrupt();
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
