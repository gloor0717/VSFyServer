import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private static final ConcurrentHashMap<String, ClientInfo> clientList = new ConcurrentHashMap<>();

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

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
            e.printStackTrace();
        } finally {
            cleanupClientResources();
        }
    }

    private void processClientCommand(String input, PrintWriter writer) {
        if (input.startsWith("REGISTER")) {
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
        String clientId = parts[1];
        int clientP2PPort = Integer.parseInt(parts[2]); // Get the client's P2P port

        if (!clientList.containsKey(clientId)) {
            List<String> fileList = Arrays.asList(parts).subList(3, parts.length);
            ClientInfo clientInfo = new ClientInfo(clientId, clientSocket.getInetAddress(), fileList, clientP2PPort);
            clientList.put(clientId, clientInfo);

            writer.println("REGISTERED");
        } else {
            writer.println("ERROR: Client already registered");
        }
    }

    private void handleSongRequestCommand(String input, PrintWriter writer) {
        String[] parts = input.split(" ");
        String songName = parts[1];

        // Logic to find a client with the requested song and send back its address
        clientList.values().stream()
                .filter(info -> info.getAudioFiles().contains(songName))
                .findFirst()
                .ifPresent(clientInfo -> writer.println(
                        "PEER_ADDRESS " + clientInfo.getIpAddress().getHostAddress() + " " + clientInfo.getP2PPort()));
    }

    private void cleanupClientResources() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
