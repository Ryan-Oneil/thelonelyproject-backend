package org.lonelyproject.backend.dto;

import java.io.Serializable;
import org.lonelyproject.backend.enums.ConnectionStatus;

public class ProfileConnectionDto implements Serializable {

    private ConnectionStatus status;
    private UserProfileDto connector;
    private UserProfileDto target;

    public ProfileConnectionDto() {
    }

    public ProfileConnectionDto(ConnectionStatus status, UserProfileDto connector, UserProfileDto target) {
        this.status = status;
        this.connector = connector;
        this.target = target;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public UserProfileDto getConnector() {
        return connector;
    }

    public void setConnector(UserProfileDto connector) {
        this.connector = connector;
    }

    public UserProfileDto getTarget() {
        return target;
    }

    public void setTarget(UserProfileDto target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "status = " + status + ", " +
            "connector = " + connector + ", " +
            "target = " + target + ")";
    }
}
