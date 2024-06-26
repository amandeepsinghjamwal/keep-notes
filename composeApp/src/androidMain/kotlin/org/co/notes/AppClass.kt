package org.co.notes

import android.app.Application
import di.KoinInitializer

class AppClass:Application() {
    override fun onCreate() {
        super.onCreate()
        KoinInitializer(applicationContext).init()
    }
}