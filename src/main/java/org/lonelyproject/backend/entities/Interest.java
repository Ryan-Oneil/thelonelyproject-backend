package org.lonelyproject.backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(unique = true, nullable = false)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private InterestCategory category;

    public Interest() {
    }

    public Interest(String description, InterestCategory category) {
        this.description = description;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public InterestCategory getCategory() {
        return category;
    }
}
