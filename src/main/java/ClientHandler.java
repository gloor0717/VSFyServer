import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The ClientHandler class handles the communication with a client connected to the server.
 */
public class ClientHandler extends Thread {
    private Socket clientSocket;
    private static final ConcurrentHashMap<String, ClientInfo> clientList = new ConcurrentHashMap<>();
    private String clientId;

    /**
     * Constructs a new ClientHandler object.
     *
     * @param socket the client socket
     */
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    /**
     * Runs the client handler thread.
     */
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String input;
            while ((input = reader.readLine()) != null) {
                processClientCommand(input, writer);
            }
        } catch (IOException e) {
            System.err.println("Error handling client connection: " + e.getMessage());
            if (clientId != null) {
                clientList.remove(clientId);
                System.out.println("Client " + clientId + " disconnected and removed from list.");
            }
            cleanupClientResources();
        }
    }

    private void processClientCommand(String input, PrintWriter writer) {
        if (input.startsWith("UPDATE_PORT")) {
            handleUpdatePortCommand(input, writer);
        } else if (input.startsWith("REGISTER")) {
            handleRegisterCommand(input, writer);
        } else if (input.startsWith("REQUEST_SONG")) {
            handleSongRequestCommand(input, writer);
        } else if (input.startsWith("INFO")) {
            handleInfoCommand(input, writer);
        } else if (input.equals("LIST_MUSIC")) {
            handleListMusicCommand(writer);
        } else {
            writer.println("ERROR: Unknown command");
        }
    }

    private void handleUpdatePortCommand(String input, PrintWriter writer) {
        String[] parts = input.split(" ");
        String clientId = parts[1];
        int newP2PPort = Integer.parseInt(parts[2]);

        if (clientList.containsKey(clientId)) {
            ClientInfo clientInfo = clientList.get(clientId);
            clientInfo.setP2PPort(newP2PPort);
        } else {
            writer.println("ERROR: Client not found for port update");
        }
    }

    private void handleListMusicCommand(PrintWriter writer) {
        StringBuilder musicList = new StringBuilder();
        clientList.forEach((clientId, clientInfo) -> {
            clientInfo.getAudioFiles()
                    .forEach(song -> musicList.append(clientId).append(" - ").append(song).append("\n"));
        });
        writer.println(musicList.toString());
        writer.println("END_OF_LIST");
    }

    private void handleInfoCommand(String input, PrintWriter writer) {
        String[] parts = input.split(" ", 2); // Split only into two parts
        if (parts.length < 2) {
            writer.println("ERROR: Invalid command format. Usage: INFO <client_id>");
            return;
        }

        String clientId = parts[1].trim(); // Trim the client ID
        ClientInfo clientInfo = clientList.get(clientId);
        if (clientInfo != null) {
            writer.println("Client: " + clientId + ", IP: " + clientInfo.getIpAddress().getHostAddress()
                    + ", P2P Port: " + clientInfo.getP2PPort());
        } else {
            writer.println("ERROR: Client not found.");
        }
    }

    private void handleRegisterCommand(String input, PrintWriter writer) {
        String[] parts = input.split(" ");
        clientId = parts[1];
        int clientP2PPort = Integer.parseInt(parts[2]);

        if (!clientList.containsKey(clientId)) {
            List<String> fileList = Arrays.asList(parts).subList(3, parts.length);
            ClientInfo clientInfo = new ClientInfo(clientId, clientSocket.getInetAddress(), fileList, clientP2PPort);
            clientList.put(clientId, clientInfo);
        } else {
            writer.println("ERROR: Client already registered");
        }
    }

    private void handleSongRequestCommand(String input, PrintWriter writer) {
        String songName = extractSongName(input);
        ClientInfo clientInfo = findClientWithSong(songName);

        if (clientInfo != null) {
            sendPeerAddressResponse(clientInfo, writer);
        } else {
            writer.println("ERROR: Song not found");
        }
    }

    private String extractSongName(String input) {
        String[] parts = input.split(" ");
        return parts.length > 1 ? parts[1] : "";
    }

    private ClientInfo findClientWithSong(String songName) {
        return clientList.values().stream()
                .filter(info -> info.getAudioFiles().contains(songName))
                .findFirst()
                .orElse(null);
    }

    private void sendPeerAddressResponse(ClientInfo clientInfo, PrintWriter writer) {
        String response = "PEER_ADDRESS " + clientInfo.getIpAddress().getHostAddress() + " " + clientInfo.getP2PPort();
        writer.println(response);
        writer.flush();
        System.out.println("ClientHandler: Sent response - " + response);
    }

    private void cleanupClientResources() {
        // Close resources if needed
        try {
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
        }
    }
}
