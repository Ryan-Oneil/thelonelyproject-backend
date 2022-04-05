package org.lonelyproject.backend.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.lonelyproject.backend.entities.supers.ProfileTrait;

@Entity
public class InterestCategory extends ProfileTrait {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "category", fetch = FetchType.EAGER)
    private List<Interest> interests;

    public InterestCategory() {
    }

    public List<Interest> getInterests() {
        return interests;
    }
}
