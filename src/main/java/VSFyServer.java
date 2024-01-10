import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The VSFyServer class represents a server that listens for client connections and handles them using the ClientHandler class.
 */
public class VSFyServer {
    private static final int PORT = 45000;
    private ServerSocket serverSocket;

    /**
     * Starts the VSFyServer by creating a ServerSocket and accepting client connections.
     */
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

    /**
     * The main method creates an instance of VSFyServer and starts it.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VSFyServer server = new VSFyServer();
        server.start();
    }
}
