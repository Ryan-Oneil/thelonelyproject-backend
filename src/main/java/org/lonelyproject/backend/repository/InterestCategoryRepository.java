package org.lonelyproject.backend.repository;

import java.util.List;
import org.lonelyproject.backend.entities.InterestCategory;
import org.springframework.data.repository.CrudRepository;

public interface InterestCategoryRepository extends CrudRepository<InterestCategory, Integer> {

    List<InterestCategory> findAll();
}