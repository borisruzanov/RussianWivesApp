package com.borisruzanov.russianwives.mvp.model.repository;

import com.google.firebase.auth.FirebaseAuth;

public class CheckRepository {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    public boolean checkUserRegistered(){
        return auth.getCurrentUser() != null;
    }

}
