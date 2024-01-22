package com.my.comparustesttask.controller;

import com.my.comparustesttask.dto.UserDTO;
import com.my.comparustesttask.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController implements UsersApi {

    private UserService userService;

    @Override
    public ResponseEntity<List<UserDTO>> findUsers(String name) {
        List<UserDTO> allUsers = userService.getUsers(name);
        return ResponseEntity.ok().body(allUsers);
    }
}
