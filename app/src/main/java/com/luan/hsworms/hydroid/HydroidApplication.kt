package com.luan.hsworms.hydroid

import android.app.Application

class HydroidApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        HydroidRepository.initialize(this)
    }
}