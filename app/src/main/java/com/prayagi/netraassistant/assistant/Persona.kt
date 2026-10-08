package com.prayagi.netraassistant.assistant

/** Two independent personalities: style only, same capabilities. */
enum class Persona(val displayName: String, val tagline: String) {
    NETRA("NETRA", "Warm and gentle"),
    TRIKAL("TRIKAL", "Brief and direct");

    fun greeting(): String = when (this) {
        NETRA -> "Namaste, main Netra hoon. Battery, volume ya brightness ke baare mein poochhiye."
        TRIKAL -> "Trikal here. Ask: battery, volume, brightness."
    }
}
