import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private static ConcurrentHashMap<String, ClientInfo> clientList = new ConcurrentHashMap<>();

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public static ConcurrentHashMap<String, ClientInfo> getClientList() {
        return clientList;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String input;
            while ((input = reader.readLine()) != null) {

                if (input.equals("LIST_MUSIC")) {
                    // Aggregate and send the list of all available music files
                    String musicList = getAllMusicFiles();
                    writer.println(musicList);
                    Logger.log("INFO", "Sent music list to " + clientSocket.getInetAddress().getHostAddress());
                    System.out.println("Sent music list to " + clientSocket.getInetAddress().getHostAddress());
                }

                if (input.startsWith("REGISTER")) {
                    // Registration logic
                    String[] parts = input.split(" ");
                    String clientId = parts[1];
                    List<String> fileList = Arrays.asList(parts).subList(2, parts.length);

                    ClientInfo clientInfo = new ClientInfo(clientId, clientSocket.getInetAddress(), fileList);
                    clientList.put(clientId, clientInfo);
                    Logger.log("INFO", "Registered client: " + clientId + " with files: " + fileList);
                    System.out.println("Registered client: " + clientId + " with files: " + fileList);
                } else if (input.equals("REQUEST_CLIENT_LIST")) {
                    // Send back the list of clients
                    String clientListString = clientList.entrySet().stream()
                            .map(entry -> entry.getKey() + " - " + entry.getValue().getAudioFiles())
                            .collect(Collectors.joining(", "));
                    writer.println(clientListString);
                    Logger.log("INFO", "Sent client list to " + clientSocket.getInetAddress().getHostAddress());
                    System.out.println("Sent client list to " + clientSocket.getInetAddress().getHostAddress());
                } else if (input.startsWith("REQUEST_FILE")) {
                    // Process file request
                    String[] parts = input.split(" ");
                    String targetClientId = parts[1];
                    String requestedFile = parts[2];

                    ClientInfo targetClient = clientList.get(targetClientId);
                    if (targetClient != null && targetClient.getAudioFiles().contains(requestedFile)) {
                        // Send target client's IP address and file info back to the requester
                        writer.println(targetClient.getIpAddress().getHostAddress() + " " + requestedFile);
                    } else {
                        writer.println("ERROR: Client not found or file not available");
                    }
                    Logger.log("INFO", "Processed file request for " + requestedFile + " from " + targetClientId);
                    System.out.println("Processed file request for " + requestedFile + " from " + targetClientId);
                }
            }
        } catch (IOException e) {
            Logger.log("ERROR", "Error in ClientHandler: " + e.getMessage());
            System.out.println("Error in ClientHandler: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getAllMusicFiles() {
        return clientList.values().stream()
                .flatMap(clientInfo -> clientInfo.getAudioFiles().stream())
                .distinct()
                .collect(Collectors.joining(", "));
    }
}
