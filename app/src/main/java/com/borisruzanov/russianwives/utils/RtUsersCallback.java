package com.borisruzanov.russianwives.utils;

import com.borisruzanov.russianwives.models.RtUser;

import java.util.List;

public interface RtUsersCallback {

    void getUsers(List<RtUser> rtUserList);

}
