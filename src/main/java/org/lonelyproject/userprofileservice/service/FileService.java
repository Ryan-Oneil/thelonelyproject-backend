package org.lonelyproject.userprofileservice.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.lonelyproject.userprofileservice.dto.UploadedFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private static final Logger logger = LogManager.getLogger(FileService.class);

    private final String tempDirectory;

    public FileService(@Value("${server.tempDirectory}") String tempDirectory) {
        this.tempDirectory = tempDirectory;
    }

    public List<UploadedFile> handleFileUpload(HttpServletRequest request, long uploadLimit) throws IOException {
        ArrayList<UploadedFile> uploadedFiles = new ArrayList<>();
        ServletFileUpload upload = new ServletFileUpload();
        upload.setSizeMax(uploadLimit);

        FileItemIterator iterator;
        //Gets the uploaded file from request
        try {
            iterator = upload.getItemIterator(request);
        } catch (IOException e) {
            throw new FileNotFoundException("No form data");
        }

        while (iterator.hasNext()) {
            FileItemStream item = iterator.next();

            if (item.isFormField()) {
                continue;
            }
            String fileName = UUID.randomUUID() + "." + getExtensionType(item.getName());
            UploadedFile uploadedFile = writeFile(item.openStream(), fileName, tempDirectory);

            uploadedFiles.add(uploadedFile);
        }
        if (uploadedFiles.isEmpty()) {
            throw new FileNotFoundException("No files uploaded");
        }
        return uploadedFiles;
    }

    public String getExtensionType(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf('.') + 1).toLowerCase();
    }

    public UploadedFile writeFile(InputStream inputStream, String originalFileName, String dest) throws IOException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        }
        File destFolder = new File(dest);

        if (!destFolder.exists()) {
            destFolder.mkdir();
        }
        String fileName = UUID.randomUUID() + "." + getExtensionType(originalFileName);
        File newFile = new File("%s/%s".formatted(dest, fileName));

        DigestInputStream dis = new DigestInputStream(inputStream, md);
        //Copy file to new file
        FileUtils.copyInputStreamToFile(dis, newFile);

        String sha1 = null;
        if (md != null) {
            sha1 = DatatypeConverter.printHexBinary(md.digest());
        }
        return new UploadedFile(originalFileName, sha1, newFile);
    }
}
