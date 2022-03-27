package org.lonelyproject.backend.api;

import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.contentSources.B2ContentSource;
import com.backblaze.b2.client.contentSources.B2ContentTypes;
import com.backblaze.b2.client.contentSources.B2FileContentSource;
import com.backblaze.b2.client.exceptions.B2Exception;
import com.backblaze.b2.client.structures.B2FileVersion;
import com.backblaze.b2.client.structures.B2UploadFileRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lonelyproject.backend.config.properties.BucketInfo;
import org.lonelyproject.backend.dto.UploadedFile;
import org.lonelyproject.backend.entities.CloudItemDetails;
import org.lonelyproject.backend.exception.UploadException;
import org.springframework.stereotype.Component;

@Component
public class BackBlazeAPI {

    private static final Logger logger = LogManager.getLogger(BackBlazeAPI.class);

    private final B2StorageClient client;
    private final BucketInfo bucketInfo;

    public BackBlazeAPI(B2StorageClient client, BucketInfo bucketInfo) {
        this.client = client;
        this.bucketInfo = bucketInfo;
    }

    public B2FileVersion uploadToBucket(String bucketId, UploadedFile uploadedFile) {
        final B2ContentSource source = B2FileContentSource.builder(uploadedFile.file()).build();

        B2UploadFileRequest request = B2UploadFileRequest
            .builder(bucketId, uploadedFile.name(), B2ContentTypes.B2_AUTO, source)
            .build();
        try {
            return client.uploadSmallFile(request);
        } catch (B2Exception e) {
            logger.error(e);
        } finally {
            uploadedFile.file().delete();
        }
        throw new UploadException("Upload failed");
    }

    public CloudItemDetails uploadToProfileBucket(UploadedFile uploadedFile) {
        B2FileVersion file = uploadToBucket(bucketInfo.avatars().id(), uploadedFile);

        return new CloudItemDetails(file.getFileId(), file.getFileName(),
            bucketInfo.avatars().name(), file.getContentLength());
    }

    public void deleteFromBucket(String fileName, String fileId) {
        try {
            client.deleteFileVersion(fileName, fileId);
        } catch (B2Exception e) {
            logger.error("Error deleting item from Backblaze bucket", e);
        }
    }
}
