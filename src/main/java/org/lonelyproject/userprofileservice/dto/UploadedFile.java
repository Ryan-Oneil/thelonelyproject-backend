package org.lonelyproject.userprofileservice.dto;

import java.io.File;

public record UploadedFile(String name, String sha1, File file) {

}
