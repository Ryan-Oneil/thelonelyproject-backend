package org.lonelyproject.backend.dto;

public record UploadURLResponse(String bucketId, String uploadUrl, String authorizationToken) {

}
