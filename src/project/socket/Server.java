package project.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    public static ExecutorService threadPool = Executors.newFixedThreadPool(1000);

    public static void main(String[] args) {
        Socket socket;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(5000);
            while (true) {
                System.out.println("Waiting for clients");
                socket = serverSocket.accept();
                System.out.println("Connected");
                ClientHandler clientHandler = new ClientHandler(clients, socket);
                clients.add(clientHandler);
                threadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            clients.clear();
        }
        threadPool.shutdownNow();
    }
}
