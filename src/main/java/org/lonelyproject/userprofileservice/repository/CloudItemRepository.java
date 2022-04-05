package org.lonelyproject.userprofileservice.repository;

import java.util.Optional;
import org.lonelyproject.userprofileservice.entities.ProfileMedia;
import org.lonelyproject.userprofileservice.entities.supers.CloudItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CloudItemRepository<T extends CloudItem> extends CrudRepository<T, Integer> {

    @Query("select pm from ProfileMedia pm where pm.id = ?1 and pm.userProfile.id = ?2")
    Optional<ProfileMedia> findProfileMediaByIdAndUserProfile_Id(int id, String profileId);
}