package org.lonelyproject.userprofileservice.entities.compositeids;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ConnectionId implements Serializable {

    @Column(name = "connector_id", nullable = false, updatable = false)
    private String connectorId;

    @Column(name = "target_id", nullable = false, updatable = false)
    private String targetId;

    public ConnectionId() {
    }

    public ConnectionId(String connectorId, String targetId) {
        this.connectorId = connectorId;
        this.targetId = targetId;
    }

    public String getConnectorId() {
        return connectorId;
    }

    public String getTargetId() {
        return targetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConnectionId that)) {
            return false;
        }
        return getConnectorId().equals(that.getConnectorId()) && getTargetId().equals(that.getTargetId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getConnectorId(), getTargetId());
    }
}
