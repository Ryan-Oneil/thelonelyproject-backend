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
import org.lonelyproject.backend.entities.ProfilePicture;
import org.lonelyproject.backend.exception.UploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BackBlazeAPI {

    private static final Logger logger = LogManager.getLogger(BackBlazeAPI.class);

    private final B2StorageClient client;
    private final BucketInfo bucketInfo;
    private final String cdnUrl;

    public BackBlazeAPI(B2StorageClient client, BucketInfo bucketInfo, @Value("${cdn.url}") String cdnUrl) {
        this.client = client;
        this.bucketInfo = bucketInfo;
        this.cdnUrl = cdnUrl;
    }

    public B2FileVersion uploadToBucket(String bucketId, UploadedFile uploadedFile) {
        final B2ContentSource source = B2FileContentSource.builder(uploadedFile.file()).build();

        B2UploadFileRequest request = B2UploadFileRequest
            .builder(bucketId, uploadedFile.name(), B2ContentTypes.APPLICATION_OCTET, source)
            .build();
        try {
            final B2FileVersion cloudUpload = client.uploadSmallFile(request);

            return cloudUpload;
        } catch (B2Exception e) {
            logger.error(e);
        } finally {
            uploadedFile.file().delete();
        }
        throw new UploadException("Upload failed");
    }

    public ProfilePicture uploadToProfileBucket(UploadedFile uploadedFile) {
        B2FileVersion file = uploadToBucket(bucketInfo.avatars().id(), uploadedFile);
        CloudItemDetails cloudItemDetails = new CloudItemDetails(file.getFileId(), file.getFileName(), bucketInfo.avatars().id(),
            file.getContentLength());

        return new ProfilePicture(cloudItemDetails, "%s/%s/%s".formatted(cdnUrl, bucketInfo.avatars().name(), cloudItemDetails.getName()));
    }

    public void deleteFromBucket(String fileName, String fileId) {
        try {
            client.deleteFileVersion(fileName, fileId);
        } catch (B2Exception e) {
            logger.error("Error deleting item from Backblaze bucket", e);
        }
    }
}
