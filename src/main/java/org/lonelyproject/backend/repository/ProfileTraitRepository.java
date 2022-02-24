package org.lonelyproject.backend.repository;

import java.util.List;
import org.lonelyproject.backend.entities.InterestCategory;
import org.lonelyproject.backend.entities.supers.ProfileTrait;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProfileTraitRepository<T extends ProfileTrait> extends CrudRepository<T, Integer> {

    @Query("select c from InterestCategory  c")
    List<InterestCategory> findAllInterestCategories();
}