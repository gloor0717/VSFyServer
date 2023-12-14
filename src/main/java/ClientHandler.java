import java.io.*;
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
                processClientCommand(input, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cleanupClientResources();
        }
    }

    private void processClientCommand(String input, PrintWriter writer) {
        if (input.startsWith("REGISTER")) {
            handleRegisterCommand(input, writer);
        } else if (input.equals("LIST_MUSIC")) {
            handleListMusicCommand(writer);
        } else {
            writer.println("ERROR: Unknown command");
        }
    }

    private void handleRegisterCommand(String input, PrintWriter writer) {
        String[] parts = input.split(" ");
        String clientId = parts[1];
    
        if (!clientList.containsKey(clientId)) {
            List<String> fileList = Arrays.asList(parts).subList(2, parts.length);
            ClientInfo clientInfo = new ClientInfo(clientId, clientSocket.getInetAddress(), fileList);
            clientList.put(clientId, clientInfo);
    
            // Log client login to the console
            System.out.println("INFO: Client " + clientId + " logged in from " + clientSocket.getInetAddress());
        } else {
            writer.println("ERROR: Client already registered");
        }
    }
    

    private void handleListMusicCommand(PrintWriter writer) {
        StringBuilder musicList = new StringBuilder();
        clientList.forEach((clientId, clientInfo) -> {
            clientInfo.getAudioFiles().forEach(song -> musicList.append(clientId).append(" - ").append(song).append("\n"));
        });
        writer.print(musicList.toString());
        writer.println("END_OF_LIST"); // Add a newline character after the list
    }     

    private void cleanupClientResources() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
