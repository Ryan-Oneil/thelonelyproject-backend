package org.lonelyproject.backend.repository;

import java.util.Optional;
import org.lonelyproject.backend.entities.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, String> {

    Optional<UserProfile> getUserProfileByUserId(String userId);
}