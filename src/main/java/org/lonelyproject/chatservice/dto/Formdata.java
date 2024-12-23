package org.lonelyproject.chatservice.dto;

import org.lonelyproject.userprofileservice.dto.UploadedFile;

import java.util.List;

public class Formdata {

    private String message;

    private List<UploadedFile> files;

    public Formdata(String message, List<UploadedFile> files) {
        this.message = message;
        this.files = files;
    }

    public String getMessage() {
        return message;
    }

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }
}
