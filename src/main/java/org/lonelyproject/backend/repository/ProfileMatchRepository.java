package org.lonelyproject.backend.repository;

import java.util.List;
import org.lonelyproject.backend.entities.ProfileMatch;
import org.lonelyproject.backend.entities.compositeids.MatchId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProfileMatchRepository extends CrudRepository<ProfileMatch, MatchId> {

    @Query("select m from ProfileMatch m where m.matchId.profileId = ?1 or m.matchId.matchProfileId = ?1")
    List<ProfileMatch> getALlMatchesByUser(String userProfileId);
}