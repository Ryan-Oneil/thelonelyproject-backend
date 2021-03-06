package org.lonelyproject.userprofileservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.lonelyproject.userprofileservice.entities.Interest;
import org.lonelyproject.userprofileservice.entities.InterestCategory;
import org.lonelyproject.userprofileservice.entities.supers.ProfileTraitRelation;
import org.springframework.beans.factory.annotation.Autowired;

class ProfileTraitRepositoryTest extends BaseRepository {

    @Autowired
    private ProfileTraitRepository<ProfileTraitRelation> profileTraitRepository;

    @Test
    void findAllInterestCategoriesTest() {
        List<InterestCategory> categoryList = profileTraitRepository.findAllInterestCategories();

        assertThat(categoryList).isNotEmpty();
        assertThat(categoryList.size()).isEqualTo(2);

        categoryList.forEach(interestCategory -> {
            assertThat(interestCategory.getInterests()).isNotEmpty();
        });
    }

    @Test
    void getInterestByIdValidTest() {
        Optional<Interest> interest = profileTraitRepository.getInterestById(1);

        assertThat(interest).isPresent();
    }

    @Test
    void getInterestByIdInValidTest() {
        Optional<Interest> interest = profileTraitRepository.getInterestById(100);

        assertThat(interest).isNotPresent();
    }

    @Test
    void getTotalProfileInterestsTest() {
        int count = profileTraitRepository.getTotalProfileInterests("2");

        assertThat(count).isEqualTo(1);
    }
}
