package com.smartfoxpro.fileshare.service;

import com.smartfoxpro.fileshare.FileSharerRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSystemStorageService implements StorageService {

    @Autowired
    private FileSharerRunner runner;
    @Value("${app.file-directory}")
    private String rootPath;

    @Override
    public void store(MultipartFile file) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Path root = Paths.get(rootPath + authentication.getName());

        if (!Files.exists(root)) {
            Files.createDirectories(root);
        }

        Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));
    }

    @Override
    public Path load(String filename) {
        return Paths.get(runner.getPath()).resolve(filename);
    }
}
