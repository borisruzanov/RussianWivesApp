package com.borisruzanov.russianwives.zz;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseQueryManager  {

    public static final DatabaseReference usersCattegory = FirebaseDatabase.getInstance().getReference().child("Users");
}
