package org.lonelyproject.backend.repository;

import java.util.List;
import org.lonelyproject.backend.entities.ProfileMedia;
import org.springframework.data.repository.CrudRepository;

public interface ProfileMediaRepository extends CrudRepository<ProfileMedia, Integer> {

    List<ProfileMedia> getAllByUserProfile_IdOrderByIdDesc(String userId);
}