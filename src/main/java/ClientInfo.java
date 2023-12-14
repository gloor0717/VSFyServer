import java.net.InetAddress;
import java.util.List;

/**
 * The ClientInfo class holds details about a client.
 */
public class ClientInfo {
    private String clientId;
    private InetAddress ipAddress;
    private List<String> audioFiles;

    public ClientInfo(String clientId, InetAddress ipAddress, List<String> audioFiles) {
        this.clientId = clientId;
        this.ipAddress = ipAddress;
        this.audioFiles = audioFiles;
    }

    // Getters and setters
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List<String> getAudioFiles() {
        return audioFiles;
    }

    public void setAudioFiles(List<String> audioFiles) {
        this.audioFiles = audioFiles;
    }
}