package com.borisruzanov.russianwives.utils;

import com.borisruzanov.russianwives.models.Users;

import java.util.List;

/**
 * Created by Михаил on 01.05.2018.
 */

public interface UsersListCallback {

    void getUsers(List<Users> usersList);

}
