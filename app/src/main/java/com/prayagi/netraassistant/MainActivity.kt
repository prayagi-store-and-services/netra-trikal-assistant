package com.prayagi.netraassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.prayagi.netraassistant.ui.AppShell
import com.prayagi.netraassistant.ui.NetraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NetraTheme { AppShell() }
        }
    }
}
