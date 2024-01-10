import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents information about a client.
 */
public class ClientInfo {
    private String clientId; // Unique identifier for the client
    private InetAddress ipAddress; // IP address of the client
    private LinkedList<String> audioFiles; // List of audio files associated with the client
    private int p2pPort; // Port used for P2P communication

    /**
     * Constructs a ClientInfo object.
     *
     * @param clientId    the client's unique identifier
     * @param ipAddress   the client's IP address
     * @param audioFiles  the list of audio files associated with the client
     * @param p2pPort     the port used for P2P communication
     */
    public ClientInfo(String clientId, InetAddress ipAddress, List<String> audioFiles, int p2pPort) {
        this.clientId = clientId;
        this.ipAddress = ipAddress;
        this.audioFiles = new LinkedList<>(audioFiles);
        this.p2pPort = p2pPort;
    }

    /**
     * Returns the client's unique identifier.
     *
     * @return the client's unique identifier
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Sets the client's unique identifier.
     *
     * @param clientId the client's unique identifier
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Returns the client's IP address.
     *
     * @return the client's IP address
     */
    public InetAddress getIpAddress() {
        return ipAddress;
    }

    /**
     * Sets the client's IP address.
     *
     * @param ipAddress the client's IP address
     */
    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Returns the list of audio files associated with the client.
     *
     * @return the list of audio files associated with the client
     */
    public List<String> getAudioFiles() {
        return audioFiles;
    }

    /**
     * Sets the list of audio files associated with the client.
     *
     * @param audioFiles the list of audio files associated with the client
     */
    public void setAudioFiles(List<String> audioFiles) {
        this.audioFiles = new LinkedList<>(audioFiles);
    }

    /**
     * Returns the port used for P2P communication.
     *
     * @return the port used for P2P communication
     */
    public int getP2PPort() {
        return p2pPort;
    }

    /**
     * Sets the port used for P2P communication.
     *
     * @param p2pPort the port used for P2P communication
     */
    public void setP2PPort(int p2pPort) {
        this.p2pPort = p2pPort;
    }
}
