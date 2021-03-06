package com.smartfoxpro.fileshare.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

public interface StorageService {


    void store(MultipartFile file) throws IOException;

    Path load(String filename);

}