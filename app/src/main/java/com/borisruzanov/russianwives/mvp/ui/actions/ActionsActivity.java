package com.borisruzanov.russianwives.mvp.ui.actions;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.borisruzanov.russianwives.R;

public class ActionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ActionsFragment newFragment = new ActionsFragment();
        transaction.replace(R.id.contentContainerActionsActivity, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
