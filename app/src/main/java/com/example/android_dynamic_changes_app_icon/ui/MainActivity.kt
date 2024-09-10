package com.example.android_dynamic_changes_app_icon.ui

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.android_dynamic_changes_app_icon.R
import com.example.android_dynamic_changes_app_icon.ui.service.IconSwitchService

@RequiresApi(Build.VERSION_CODES.N_MR1)
class MainActivity : AppCompatActivity() {

    private val shortcutId = "my_app_shortcut"
    private var isDefaultIcon = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_layout)

        val button = findViewById<Button>(R.id.switch_icon_button)
        button.setOnClickListener {
            switchIcon()
        }

     //   createShortcut()
    }

    private fun createShortcut() {
        val shortcutManager = getSystemService(ShortcutManager::class.java)
        val iconResId = if (isDefaultIcon) R.drawable.ic_launcher_background else R.drawable.ic_shortcut

        val shortcut = ShortcutInfo.Builder(this, shortcutId)
            .setShortLabel("My App")
            .setLongLabel("Open My App")
            .setIcon(Icon.createWithResource(this, iconResId))
            .setIntent(Intent(this, MainActivity::class.java).setAction(Intent.ACTION_VIEW))
            .build()

        shortcutManager?.setDynamicShortcuts(listOf(shortcut))
    }

    private fun removeShortcut() {
        val shortcutManager = getSystemService(ShortcutManager::class.java)
        shortcutManager?.removeDynamicShortcuts(listOf(shortcutId))
    }

  /*  private fun switchIcon() {
        isDefaultIcon = !isDefaultIcon // Toggle the icon state
        if (isDefaultIcon){
            setIcon(this,".MainActivityAlias",".ui.MainActivity")
        }else {
            setIcon(this,".ui.MainActivity",".MainActivityAlias")
        }
    }*/


    private fun switchIcon() {
        val iconToEnable = if (isDefaultIcon) ".MainActivityAlias" else ".ui.MainActivity"
        val iconToDisable = if (isDefaultIcon) ".ui.MainActivity" else ".MainActivityAlias"

        // Start the service
        val serviceIntent = Intent(this, IconSwitchService::class.java)
        startService(serviceIntent)

        // Use the service to set the icon
        val iconSwitchService = Intent(this, IconSwitchService::class.java)
        iconSwitchService.putExtra("enable", iconToEnable)
        iconSwitchService.putExtra("disable", iconToDisable)
        startService(iconSwitchService)

        isDefaultIcon = !isDefaultIcon // Toggle the icon state
    }
}

fun Activity.setIcon(context: Context,enable:String,disable:String){
    packageManager.setComponentEnabledSetting(
        ComponentName(context,"$packageName$enable"),
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )

    packageManager.setComponentEnabledSetting(
        ComponentName(context,"$packageName$disable"),
        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
        PackageManager.DONT_KILL_APP
    )

}