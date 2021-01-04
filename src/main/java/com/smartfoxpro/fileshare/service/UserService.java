package com.smartfoxpro.fileshare.service;

import com.smartfoxpro.fileshare.dto.UserDto;
import com.smartfoxpro.fileshare.entity.User;

public interface UserService {
    User registerUser(UserDto userDto);
}
