package org.lonelyproject.backend.repository;

import org.lonelyproject.backend.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

}