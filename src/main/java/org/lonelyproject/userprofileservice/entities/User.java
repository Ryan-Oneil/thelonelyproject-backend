package org.lonelyproject.userprofileservice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import org.lonelyproject.userprofileservice.enums.UserRole;

@Entity(name = "users")
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'ROLE_USER'")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User(String id) {
        this.id = id;
    }

    public User(String id, String email, UserRole role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public User() {

    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
