package org.lonelyproject.backend.dto;

import java.io.File;

public record UploadedFile(String name, String sha1, File file) {

}
