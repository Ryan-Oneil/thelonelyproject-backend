package org.lonelyproject.userprofileservice.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.lonelyproject.userprofileservice.entities.ProfileMatch;
import org.lonelyproject.userprofileservice.entities.compositeids.MatchId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProfileMatchRepository extends CrudRepository<ProfileMatch, MatchId> {

    @Query("select m from ProfileMatch m where m.matchId.profileId = ?1")
    List<ProfileMatch> getALlMatchesByUser(String userProfileId);

    @Transactional
    @Modifying
    @Query("delete from ProfileMatch m where m.matchId.profileId = ?1 and m.matchId.matchProfileId = ?2")
    void deleteByMatchAndTarget(String matcherId, String targetId);
}