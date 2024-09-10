package com.example.android_dynamic_changes_app_icon.ui.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder

class IconSwitchService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null // Not using binding in this example
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val enable = it.getStringExtra("enable") ?: return super.onStartCommand(intent, flags, startId)
            val disable = it.getStringExtra("disable") ?: return super.onStartCommand(intent, flags, startId)

            setIcon(enable, disable)
        }
        return START_NOT_STICKY
    }

    private fun setIcon(enable: String, disable: String) {
        packageManager.setComponentEnabledSetting(
            ComponentName(this, "$packageName$enable"),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        packageManager.setComponentEnabledSetting(
            ComponentName(this, "$packageName$disable"),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}