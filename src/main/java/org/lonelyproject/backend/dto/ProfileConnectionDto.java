package org.lonelyproject.backend.dto;

import java.io.Serializable;
import org.lonelyproject.backend.enums.ConnectionStatus;

public class ProfileConnectionDto implements Serializable {

    private ConnectionStatus connectionStatus;
    private boolean isAttemptingToConnect;
    private boolean isConnector;

    public ProfileConnectionDto(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public ProfileConnectionDto() {
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(ConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public boolean isAttemptingToConnect() {
        return isAttemptingToConnect;
    }

    public void setAttemptingToConnect(boolean attemptingToConnect) {
        isAttemptingToConnect = attemptingToConnect;
    }

    public boolean isConnector() {
        return isConnector;
    }

    public void setConnector(boolean connector) {
        isConnector = connector;
    }

    @Override
    public String toString() {
        return "ProfileConnectionDto{" +
            "connectionStatus=" + connectionStatus +
            ", isAttemptingToConnect=" + isAttemptingToConnect +
            ", isConnector=" + isConnector +
            '}';
    }
}
