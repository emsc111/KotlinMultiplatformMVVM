package com.example.shared.db

import app.cash.sqldelight.db.SqlDriver

expect abstract class PlatformContext

expect class DriverFactory constructor(context: PlatformContext) {
    val context: PlatformContext
}

internal expect fun createDriver(factory: DriverFactory): SqlDriver

fun createDatabase(factory: DriverFactory): DeweyDatabase =
    DeweyDatabase(createDriver(factory))
