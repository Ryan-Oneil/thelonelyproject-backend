package org.lonelyproject.backend.entities;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.lonelyproject.backend.entities.compositeids.ConnectionId;
import org.lonelyproject.backend.enums.ConnectionStatus;

@Entity
public class ProfileConnection {

    @EmbeddedId
    private ConnectionId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConnectionStatus status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "connector_id", insertable = false, updatable = false)
    private UserProfile connector;

    @ManyToOne(optional = false)
    @JoinColumn(name = "target_id", insertable = false, updatable = false)
    private UserProfile target;

    public ProfileConnection() {
    }

    public ProfileConnection(ConnectionStatus status, UserProfile connector, UserProfile target) {
        this.id = new ConnectionId(connector.getId(), target.getId());
        this.status = status;
        this.connector = connector;
        this.target = target;
    }

    public ConnectionId getId() {
        return id;
    }

    public ConnectionStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public UserProfile getConnector() {
        return connector;
    }

    public void setConnector(UserProfile requester) {
        this.connector = requester;
    }

    public UserProfile getTarget() {
        return target;
    }

    public void setTarget(UserProfile target) {
        this.target = target;
    }
}
