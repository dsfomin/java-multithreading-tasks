package Module2.Part2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSocketTask4 {
    private static final ArrayList<ClientHandlerTask4> clients = new ArrayList<>();
    private static final ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(9090);

        while (true) {
            System.out.println("Server waits for client connection...");
            Socket client = listener.accept();
            System.out.println("Client connected");
            ClientHandlerTask4 clientThread = new ClientHandlerTask4(client);
            clients.add(clientThread);
            pool.execute(clientThread);
        }
    }
}
