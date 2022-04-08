package org.lonelyproject.userprofileservice.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.lonelyproject.userprofileservice.entities.Interest;
import org.lonelyproject.userprofileservice.entities.InterestCategory;
import org.lonelyproject.userprofileservice.entities.Prompt;
import org.lonelyproject.userprofileservice.entities.supers.ProfileTrait;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProfileTraitRepository<T extends ProfileTrait> extends CrudRepository<T, Integer> {

    @Query("select c from InterestCategory  c")
    List<InterestCategory> findAllInterestCategories();

    @Query("select i from Interest i where i.id = ?1")
    Optional<Interest> getInterestById(int id);

    @Transactional
    @Modifying
    @Query("delete from UserInterest ui where ui.profileTraitId.traitId = ?1 and ui.profileTraitId.userProfileId = ?2")
    void deleteUserInterestById(int interestId, String userId);

    @Query("select p from Prompt p")
    List<Prompt> findAllPrompts();

    @Query("select p from Prompt p where p.id = ?1")
    Optional<Prompt> getPromptById(int id);

    @Transactional
    @Modifying
    @Query("delete from UserPrompt up where up.profileTraitId.traitId = ?1 and up.profileTraitId.userProfileId = ?2")
    void deleteUserPromptById(int promptId, String userId);


    @Query("select count(ui) from UserInterest ui where ui.profileTraitId.userProfileId = ?1")
    Integer getTotalProfileInterests(String profileId);
}