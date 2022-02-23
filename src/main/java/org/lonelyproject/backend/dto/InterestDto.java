package org.lonelyproject.backend.dto;

import java.io.Serializable;

public class InterestDto implements Serializable {

    private int id;
    private String description;

    public InterestDto() {
    }

    public InterestDto(int id, String name) {
        this.id = id;
        this.description = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "id = " + id + ", " +
            "name = " + description + ")";
    }
}
