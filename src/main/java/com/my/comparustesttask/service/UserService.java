package com.my.comparustesttask.service;

import com.my.comparustesttask.dto.UserDTO;
import org.springframework.lang.Nullable;

import java.util.List;

public interface UserService {
    List<UserDTO> getUsers(@Nullable String name);
}
