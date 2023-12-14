import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The VSFyServer class implements a server for the VSFy audio streaming service.
 * It listens for client connections on a specified port and creates a new thread
 * for each client to handle their requests.
 */
public class VSFyServer {
    private static final int PORT = 45000;
    private ServerSocket serverSocket;

    /**
     * Starts the server to listen on the specified port and handles client connections.
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            Logger.log("INFO", "Server started on port: " + PORT);

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printRegisteredClients() {
        ClientHandler.getClientList().forEach((clientId, clientInfo) -> {
            Logger.log("INFO", "Client " + clientId + " is registered with IP " +
                    clientInfo.getIpAddress().getHostAddress() + " and files " + clientInfo.getAudioFiles());
            System.out.println("Client " + clientId + " is registered with IP " +
                    clientInfo.getIpAddress().getHostAddress() + " and files " + clientInfo.getAudioFiles());
        });
    }

    public Set<String> getAllMusicFiles() {
        return ClientHandler.getClientList().values().stream()
                .flatMap(clientInfo -> clientInfo.getAudioFiles().stream())
                .collect(Collectors.toSet());
    }

    public void printAllMusicFiles() {
        Set<String> allMusicFiles = getAllMusicFiles();
        Logger.log("INFO", "All available music files: " + allMusicFiles);
        System.out.println("All available music files: " + allMusicFiles);
    }

    /**
     * The application's entry point.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        VSFyServer server = new VSFyServer();
        server.start();
    }
}
