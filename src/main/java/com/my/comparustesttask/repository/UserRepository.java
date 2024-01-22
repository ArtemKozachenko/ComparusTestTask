package com.my.comparustesttask.repository;

import com.my.comparustesttask.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();
    List<User> findByName(String name);
}
