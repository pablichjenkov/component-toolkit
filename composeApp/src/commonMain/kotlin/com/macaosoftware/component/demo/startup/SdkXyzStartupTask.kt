package com.macaosoftware.component.demo.startup

import com.macaosoftware.app.PluginManager
import com.macaosoftware.app.StartupTask
import com.macaosoftware.util.MacaoResult
import kotlinx.coroutines.delay

class SdkXyzStartupTask : StartupTask {
    override fun name(): String {
        return "Sdk XYZ setup"
    }

    override fun shouldShowLoader(): Boolean {
        return true
    }

    override suspend fun initialize(pluginManager: PluginManager): MacaoResult<Unit> {
        // todo: Remove thios delay
        delay(1000)
        return MacaoResult.Success(Unit)
    }
}