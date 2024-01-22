package com.my.comparustesttask.dto;

import com.my.comparustesttask.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTOConvertor {

    public static List<UserDTO> convert(List<User> users) {
        return users.stream().map(UserDTOConvertor::convertTo).collect(Collectors.toList());
    }

    public static UserDTO convertTo(User user) {
        UserDTO userDTO = new UserDTO();
        return userDTO.id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .surname(user.getSurname());
    }
}
