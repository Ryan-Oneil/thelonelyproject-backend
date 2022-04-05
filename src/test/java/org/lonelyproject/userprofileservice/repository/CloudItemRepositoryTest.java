package org.lonelyproject.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.lonelyproject.backend.entities.ProfileMedia;
import org.springframework.beans.factory.annotation.Autowired;

class CloudItemRepositoryTest extends BaseRepository {

    @Autowired
    private CloudItemRepository cloudItemRepository;

    @Test
    void findProfileMediaByIdAndUserProfile_IdTest() {
        Optional<ProfileMedia> media = cloudItemRepository.findProfileMediaByIdAndUserProfile_Id(1, "1");

        assertThat(media).isPresent();
    }

    @Test
    void findProfileMediaByIdAndUserProfile_IdInvalidTest() {
        Optional<ProfileMedia> media = cloudItemRepository.findProfileMediaByIdAndUserProfile_Id(1, "2");

        assertThat(media).isNotPresent();
    }
}
