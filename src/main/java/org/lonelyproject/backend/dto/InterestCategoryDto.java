package org.lonelyproject.backend.dto;

import java.io.Serializable;
import java.util.List;

public class InterestCategoryDto implements Serializable {

    private int id;
    private String name;
    private List<InterestDto> interests;

    public InterestCategoryDto() {
    }

    public InterestCategoryDto(int id, String name, List<InterestDto> interests) {
        this.id = id;
        this.name = name;
        this.interests = interests;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InterestDto> getInterests() {
        return interests;
    }

    public void setInterests(List<InterestDto> interests) {
        this.interests = interests;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
            "id = " + id + ", " +
            "name = " + name + ", " +
            "interests = " + interests + ")";
    }
}
