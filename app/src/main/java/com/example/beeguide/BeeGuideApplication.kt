package com.example.beeguide

import android.app.Application
import com.example.beeguide.data.AppContainer
import com.example.beeguide.data.DefaultAppContainer

class BeeGuideApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}