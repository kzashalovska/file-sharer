package com.smartfoxpro.fileshare.service;

import com.smartfoxpro.fileshare.entity.FileEntity;
import com.smartfoxpro.fileshare.exeptions.UserNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    String storeFile(MultipartFile fileDto) throws UserNotFoundException;

    String getFile(long id) throws UserNotFoundException;

    List<FileEntity> getFiles() throws UserNotFoundException;

    String shareFile(String email, long fileId) throws UserNotFoundException;
}
