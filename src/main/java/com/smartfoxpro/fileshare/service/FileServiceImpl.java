package com.smartfoxpro.fileshare.service;

import com.smartfoxpro.fileshare.entity.FileEntity;
import com.smartfoxpro.fileshare.entity.State;
import com.smartfoxpro.fileshare.entity.User;
import com.smartfoxpro.fileshare.exeptions.UserNotFoundException;
import com.smartfoxpro.fileshare.repository.FileRepository;
import com.smartfoxpro.fileshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@Service
public class FileServiceImpl implements FileService {
    private static final String SUCCESSFUL = "Successful";
    private static final String FAILED = "Failed. You have no access to this file. Try enter another id";
    private static final String ALREADY_EXIST_MESSAGE = "This file already exists! Please, try upload another one";
    private static final String FAILED_TO_UPLOAD_MESSAGE = "Failed to upload File:";
    private static final String PATH_TO_LOAD = "Path to load: ";
    private static final String USER_IS_NOT_FOUND = "User is not found. Try to enter another email";

    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileSystemStorageService systemStorageService;

    public String storeFile(MultipartFile fileDto) throws UserNotFoundException {

        try {
            systemStorageService.store(fileDto);
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
            return ALREADY_EXIST_MESSAGE;
        } catch (IOException e) {
            e.printStackTrace();
            return FAILED_TO_UPLOAD_MESSAGE + e.getMessage() + e.getCause();
        }

        FileEntity file = setUpFile(fileDto);
        long id = fileRepository.save(file).getId();
        return String.valueOf(id);
    }

    public String getFile(long id) throws UserNotFoundException {

        FileEntity file = fileRepository.findFileEntityById(id);

        if (isOwnedByUser(file) || isSharedWithUser(file)) {
            Path path = systemStorageService.load(file.getName());
            return PATH_TO_LOAD + path.toUri().toString();
        } else return FAILED;
    }

    public List<FileEntity> getFiles() throws UserNotFoundException {
        return fileRepository.findFileEntitiesByUser(getAuthenticatedUser());
    }

    public String shareFile(String email, long fileId) throws UserNotFoundException {
        FileEntity file = fileRepository.getOne(fileId);
        if (isOwnedByUser(file)) {
            file.setState(State.SHARED.toString());
            User user;

            try {
                user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException(USER_IS_NOT_FOUND));
            } catch (UserNotFoundException userNotFoundExeption) {
                userNotFoundExeption.printStackTrace();
                return USER_IS_NOT_FOUND;
            }

            file.getUser().add(user);
            fileRepository.save(file);
            return SUCCESSFUL;
        } else return FAILED;
    }

    private FileEntity setUpFile(MultipartFile fileDto) throws UserNotFoundException {
        FileEntity file = new FileEntity();
        file.setName(fileDto.getOriginalFilename());
        User user = getAuthenticatedUser();
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        file.setUser(users);
        return file;
    }

    private User getAuthenticatedUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return userRepository.findByEmail(name).orElseThrow(() -> new UserNotFoundException(USER_IS_NOT_FOUND));
    }

    private boolean isOwnedByUser(FileEntity file) throws UserNotFoundException {
        return file.getState().equals(State.OWNED.name())
                && file.getUser().get(0).getId() == getAuthenticatedUser().getId();
    }

    private boolean isSharedWithUser(FileEntity file) throws UserNotFoundException {
        return file.getState().equals(State.SHARED.name())
                && file.getUser().contains(getAuthenticatedUser());
    }
}
