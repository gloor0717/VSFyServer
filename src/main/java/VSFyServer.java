import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class VSFyServer {
    private static final int PORT = 45000;
    private ServerSocket serverSocket;

    public void start() {
        System.out.println("VSFy Server is starting...");
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port: " + PORT);

            while (!serverSocket.isClosed()) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    new ClientHandler(clientSocket).start();
                } catch (IOException e) {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            System.out.println("VSFy Server start method completed.");
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        VSFyServer server = new VSFyServer();
        server.start();
    }
}
