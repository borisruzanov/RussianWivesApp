package com.borisruzanov.russianwives.utils;

import com.borisruzanov.russianwives.models.FsUser;

import java.util.List;

public interface UsersListCallback {

    void setUsers(List<FsUser> fsUserList);

}
