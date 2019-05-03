package com.borisruzanov.russianwives.mvp.ui.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.borisruzanov.russianwives.R;

import java.util.ArrayList;

public class ServicesActivity extends AppCompatActivity implements ServicesAdapter.ItemClickListener{
    Toolbar toolbar;
    ServicesAdapter mServiceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.services);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_backspace_black_24dp);

        ArrayList<ServiceItem> services = new ArrayList<>();
        services.add(new ServiceItem("Hot Chart", "http://i64.tinypic.com/xqgfuq.jpg", "100"));
//        services.add(new ServiceItem("Hot Chart", "http://pixelartmaker.com/art/ef761b5d6b5bd99.png", "100"));
//        services.add(new ServiceItem("Hot Chart", "http://pixelartmaker.com/art/ef761b5d6b5bd99.png", "100"));
//        services.add(new ServiceItem("Hot Chart", "http://pixelartmaker.com/art/ef761b5d6b5bd99.png", "100"));
//        services.add(new ServiceItem("Hot Chart", "http://pixelartmaker.com/art/ef761b5d6b5bd99.png", "100"));

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.services_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mServiceAdapter = new ServicesAdapter(this, services);
        mServiceAdapter.setClickListener(this);
        recyclerView.setAdapter(mServiceAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d("xxx", "CLICK CLICK");
    }
}
