package org.lonelyproject.backend.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class InterestCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "category", fetch = FetchType.EAGER)
    private List<Interest> interests;

    public InterestCategory() {
    }

    public InterestCategory(int id, String name, List<Interest> interests) {
        this.id = id;
        this.name = name;
        this.interests = interests;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Interest> getInterests() {
        return interests;
    }
}
