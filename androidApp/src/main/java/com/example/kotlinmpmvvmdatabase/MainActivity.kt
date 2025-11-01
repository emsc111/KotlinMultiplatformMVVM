package com.example.androidApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.shared.AppContainer // if you made one
import com.example.kotlinmpmvvmdatabase.ui.theme.DeweyScreen // <- match the package above

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val container = com.example.shared.AppContainer(applicationContext)
        val db = container.database

        setContent {
            DeweyScreen(database = db)
        }
    }
}
