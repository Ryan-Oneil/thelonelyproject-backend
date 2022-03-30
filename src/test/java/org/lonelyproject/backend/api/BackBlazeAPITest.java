package org.lonelyproject.backend.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.exceptions.B2Exception;
import com.backblaze.b2.client.structures.B2FileVersion;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.lonelyproject.backend.dto.UploadedFile;
import org.lonelyproject.backend.entities.CloudItemDetails;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BackBlazeAPITest {

    @MockBean
    private B2StorageClient b2StorageClient;

    @Autowired
    private BackBlazeAPI backBlazeAPI;

    @Test
    void uploadToProfileBucketTest() throws IOException, B2Exception {
        Mockito.when(b2StorageClient.uploadSmallFile(ArgumentMatchers.any()))
            .thenReturn(new B2FileVersion("", "", 0, "", null, null, null, null, 0, null, null, null));
        File file = new File("test.txt");
        file.createNewFile();

        CloudItemDetails cloudItemDetails = backBlazeAPI.uploadToProfileBucket(new UploadedFile(file.getName(), "", file));

        assertThat(cloudItemDetails).isNotNull();
        assertThat(file).doesNotExist();
    }
}
