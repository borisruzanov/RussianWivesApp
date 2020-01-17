package com.borisruzanov.russianwives.mvp.ui.chats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.ui.main.MainScreenActivity;

public class ChatsActivity extends AppCompatActivity {
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ChatsFragment newFragment = new ChatsFragment();
        transaction.replace(R.id.contentContainerChatsActivity, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_chats_activity);
        mToolbar.setTitle(R.string.chats_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainActivityIntent = new Intent(ChatsActivity.this, MainScreenActivity.class);
        startActivity(mainActivityIntent);
    }
}
