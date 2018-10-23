package com.borisruzanov.russianwives.di

import android.support.v4.app.FragmentActivity
import android.support.v7.app.AppCompatActivity
import com.borisruzanov.russianwives.App
import com.borisruzanov.russianwives.di.component.AppComponent


// extention func for syntactic sugar
val AppCompatActivity.component: AppComponent
    get() = (application as App).component

val FragmentActivity.component: AppComponent
    get() = (application as App).component