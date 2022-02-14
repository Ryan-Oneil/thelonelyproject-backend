package org.lonelyproject.backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CloudItemDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "external_id", nullable = false, unique = true)
    private String externalId;

    @Column(nullable = false)
    private String name;

    @Column(name = "container_id", nullable = false)
    private String containerId;

    @Column(nullable = false)
    private Long size;

    public CloudItemDetails(String externalId, String name, String containerId, Long size) {
        this.externalId = externalId;
        this.name = name;
        this.containerId = containerId;
        this.size = size;
    }

    public CloudItemDetails() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
