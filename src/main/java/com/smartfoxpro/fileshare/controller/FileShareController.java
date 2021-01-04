package com.smartfoxpro.fileshare.controller;

import com.smartfoxpro.fileshare.dto.UserDto;
import com.smartfoxpro.fileshare.entity.FileEntity;
import com.smartfoxpro.fileshare.exeptions.UserNotFoundException;
import com.smartfoxpro.fileshare.service.FileServiceImpl;
import com.smartfoxpro.fileshare.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
public class FileShareController {

    @Autowired
    private UserServiceImpl service;
    @Autowired
    private FileServiceImpl fileService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserDto userDto) {
        long id = service.registerUser(userDto).getId();
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/api/file")
    public List<FileEntity> getFiles() throws UserNotFoundException {
        return fileService.getFiles();
    }

    @GetMapping(path = "/api/file/{id}", produces = "application/json")
    public String getFileById(@PathVariable int id) throws UserNotFoundException {
        return fileService.getFile(id);
    }

    @PostMapping("/api/file")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws UserNotFoundException {
        return fileService.storeFile(file);

    }

    @PostMapping("/api/share")
    public String shareFile(@RequestParam("email") String email,
                            @RequestParam("fileId") long fileId) throws UserNotFoundException {

        return fileService.shareFile(email, fileId);
    }

}
