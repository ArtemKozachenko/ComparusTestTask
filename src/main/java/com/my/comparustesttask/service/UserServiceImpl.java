package com.my.comparustesttask.service;

import com.my.comparustesttask.dto.UserDTO;
import com.my.comparustesttask.dto.UserDTOConvertor;
import com.my.comparustesttask.entity.User;
import com.my.comparustesttask.exception.UserNotFoundException;
import com.my.comparustesttask.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final List<UserRepository> userRepositories;

    @Autowired
    public UserServiceImpl(List<UserRepository> userRepositories) {
        this.userRepositories = userRepositories;
    }

    @Override
    public List<UserDTO> getUsers(String name) {
        if (StringUtils.isBlank(name)) {
            return userRepositories.stream()
                    .map(UserRepository::findAll)
                    .flatMap(Collection::stream)
                    .map(UserDTOConvertor::convertTo).toList();
        }

        List<User> users = userRepositories.stream()
                .map(userRepository -> userRepository.findByName(name))
                .flatMap(Collection::stream).toList();
        if (users.isEmpty()) {
            throw new UserNotFoundException("User with name '" + name + "' not found.");
        }
        return UserDTOConvertor.convert(users);
    }

}
