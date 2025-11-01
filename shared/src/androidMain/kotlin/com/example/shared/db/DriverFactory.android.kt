package com.example.shared.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual typealias PlatformContext = Context

actual class DriverFactory actual constructor(
    actual val context: PlatformContext
)

internal actual fun createDriver(factory: DriverFactory): SqlDriver =
    AndroidSqliteDriver(DeweyDatabase.Schema, factory.context, "dewey.db")
