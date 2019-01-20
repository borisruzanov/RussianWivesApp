package com.borisruzanov.russianwives.utils;

import com.borisruzanov.russianwives.models.UserRt;

import java.util.List;

public interface RealtimeUsersCallback {

    void setUsers(List<UserRt> users);

}
