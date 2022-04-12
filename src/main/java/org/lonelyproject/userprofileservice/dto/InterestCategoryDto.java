package org.lonelyproject.userprofileservice.dto;

import java.io.Serializable;
import java.util.List;

public class InterestCategoryDto implements Serializable {

    private int id;
    private String name;
    private List<ProfileTraitDto> interests;

    public InterestCategoryDto() {
    }

    public InterestCategoryDto(int id, String name, List<ProfileTraitDto> interests) {
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

    public List<ProfileTraitDto> getInterests() {
        return interests;
    }

    public void setInterests(List<ProfileTraitDto> interests) {
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
