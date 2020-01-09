package com.borisruzanov.russianwives.mvp.ui.chats;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.borisruzanov.russianwives.R;

public class ChatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ChatsFragment newFragment = new ChatsFragment();
        transaction.replace(R.id.contentContainerChatsActivity, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
