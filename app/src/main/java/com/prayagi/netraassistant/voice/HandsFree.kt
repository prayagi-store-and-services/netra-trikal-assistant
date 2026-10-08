package com.prayagi.netraassistant.voice

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/** Shared on/off state of the hands-free service, read by the UI. */
object HandsFree {
    var running by mutableStateOf(false)
    var lastStatus by mutableStateOf("Off")
}
