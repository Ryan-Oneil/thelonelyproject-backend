package org.lonelyproject.backend.config;

import com.backblaze.b2.client.B2StorageClient;
import com.backblaze.b2.client.B2StorageClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackblazeConfig {

    @Value("${backblaze.appId}")
    private String appId;

    @Value("${backblaze.appKey}")
    private String appKey;

    @Bean
    public B2StorageClient b2StorageClient() {
        return B2StorageClientFactory
            .createDefaultFactory()
            .create(appId, appKey, "LonelyProject");
    }
}
