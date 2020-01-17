package com.borisruzanov.russianwives.mvp.ui.actions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.borisruzanov.russianwives.R;
import com.borisruzanov.russianwives.mvp.ui.chats.ChatsActivity;
import com.borisruzanov.russianwives.mvp.ui.main.MainScreenActivity;

public class ActionsActivity extends AppCompatActivity {
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ActionsFragment newFragment = new ActionsFragment();
        transaction.replace(R.id.contentContainerActionsActivity, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();


        mToolbar = (Toolbar) findViewById(R.id.toolbar_actions_activity);
        mToolbar.setTitle(R.string.actions_title);
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
        Intent mainActivityIntent = new Intent(ActionsActivity.this, MainScreenActivity.class);
        startActivity(mainActivityIntent);
    }
}
