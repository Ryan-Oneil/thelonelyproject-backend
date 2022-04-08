package org.lonelyproject.userprofileservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "backblaze.buckets")
public record BucketInfo(BaseBucket avatars) {

}
