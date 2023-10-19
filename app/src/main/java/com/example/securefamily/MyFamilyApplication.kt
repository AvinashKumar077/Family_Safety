package com.example.securefamily

import android.app.Application

class MyFamilyApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPref.init(this)
    }
}