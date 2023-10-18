package com.example.myfamily

import android.app.Application
import com.example.myfamily.SharedPref

class MyFamilyApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        SharedPref.init(this)
    }
}