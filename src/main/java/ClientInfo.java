import java.net.InetAddress;
import java.util.List;

public class ClientInfo {
    private String clientId;
    private InetAddress ipAddress;
    private List<String> audioFiles;
    private int p2pPort; // Port used for P2P communication

    // Constructor
    public ClientInfo(String clientId, InetAddress ipAddress, List<String> audioFiles, int p2pPort) {
        this.clientId = clientId;
        this.ipAddress = ipAddress;
        this.audioFiles = audioFiles;
        this.p2pPort = p2pPort;
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

    public int getP2PPort() {
        return p2pPort;
    }

    public void setP2PPort(int p2pPort) {
        this.p2pPort = p2pPort;
    }
}
