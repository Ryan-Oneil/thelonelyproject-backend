package org.lonelyproject.backend.repository;

import java.util.List;
import java.util.Optional;
import org.lonelyproject.backend.entities.UserProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserProfileRepository extends CrudRepository<UserProfile, String> {

    Optional<UserProfile> getUserProfileByUserId(String userId);

    List<UserProfile> findAll();

    @Query("select case when count(pc)> 0 then true else false end from ProfileConnection pc where (pc.id.connectorId = ?1 and pc.id.targetId = ?2) "
        + "OR (pc.id.connectorId = ?2 and pc.id.targetId = ?1)")
    boolean hasConnection(String connectorId, String userId);

    @Query("select new UserProfile(pc) from ProfileConnection pc where (pc.id.connectorId = ?1 and pc.id.targetId = ?2) OR (pc.id.connectorId = ?2 and pc.id.targetId = ?1)")
    Optional<UserProfile> getProfileConnection(String profileTargetId, String connectorId);

    @Query(
        "select case when count(pc)> 0 then true else false end from ProfileConnection pc where pc.status = 'CONNECTED' and ((pc.id.connectorId = ?1 and pc.id.targetId = ?2) OR (pc.id.connectorId = ?2 and pc.id.targetId = ?1))")
    boolean isConnected(String connectorId, String userId);

    @Query("select p from UserProfile p where p.id not in (?1)")
    List<UserProfile> findAllNotInList(List<String> userIds);

    @Query("select p.target from ProfileConnection p where p.id.connectorId = ?1")
    List<UserProfile> getAllMatchesByConnector(String connectorId);
}