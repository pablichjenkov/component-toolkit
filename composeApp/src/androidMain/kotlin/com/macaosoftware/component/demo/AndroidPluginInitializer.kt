package com.macaosoftware.component.demo

import android.content.Context
import com.macaosoftware.plugin.app.PluginInitializer
import com.macaosoftware.plugin.app.PluginManager

class AndroidPluginInitializer(context: Context) : PluginInitializer {

    override fun initialize(pluginManager: PluginManager) {


        // Should initialize the minimum necessary dependencies to run the App
        // 1- Koin-Inject or Koin or PluginManager(manual DI)
        // 2- Database Migration
    }
}
