package com.borisruzanov.russianwives.utils;

import com.borisruzanov.russianwives.models.User;

import java.util.List;

public interface UsersListCallback {

    void getUsers(List<User> userList);

}
