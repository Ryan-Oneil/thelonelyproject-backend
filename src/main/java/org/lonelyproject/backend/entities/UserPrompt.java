package org.lonelyproject.backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.lonelyproject.backend.entities.supers.ProfileTraitId;
import org.lonelyproject.backend.entities.supers.ProfileTraitRelation;

@Entity
public class UserPrompt extends ProfileTraitRelation {

    @Column(nullable = false)
    private String text;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trait_id", insertable = false, updatable = false)
    private Prompt prompt;

    public UserPrompt() {
    }

    public UserPrompt(UserProfile userProfile, String text, Prompt prompt) {
        super(new ProfileTraitId(userProfile.getId(), prompt.getId()), userProfile);

        this.text = text;
        this.prompt = prompt;
    }

    public Prompt getPrompt() {
        return prompt;
    }

    public String getText() {
        return text;
    }

}
