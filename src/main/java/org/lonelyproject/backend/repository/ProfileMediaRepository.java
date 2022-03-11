package org.lonelyproject.backend.repository;

import java.util.Optional;
import org.lonelyproject.backend.entities.ProfileMedia;
import org.springframework.data.repository.CrudRepository;

public interface ProfileMediaRepository extends CrudRepository<ProfileMedia, Integer> {
    
    Optional<ProfileMedia> findByIdAndUserProfile_Id(int id, String profileId);
}