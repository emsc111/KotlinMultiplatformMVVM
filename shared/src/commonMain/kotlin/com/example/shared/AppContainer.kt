package com.example.shared

import com.example.shared.db.DriverFactory
import com.example.shared.db.PlatformContext
import com.example.shared.db.createDatabase

class AppContainer(ctx: PlatformContext) {
    private val factory = DriverFactory(ctx)
    val database = createDatabase(factory)
}
