package com.prayagi.netraassistant.assistant

sealed class Intent {
    object BatteryLevel : Intent()
    object VolumeGet : Intent()
    object VolumeUp : Intent()
    object VolumeDown : Intent()
    object BrightnessGet : Intent()
    object Unknown : Intent()
}

/** Rule-based, on-device intent parser (English + Hindi in Latin script and Devanagari). */
object IntentParser {
    private val battery = listOf("battery", "charge", "charging", "बैटरी", "चार्ज")
    private val volume = listOf("volume", "sound", "awaz", "awaaz", "आवाज", "वॉल्यूम")
    private val brightness = listOf("brightness", "bright", "roshni", "उजाला", "ब्राइटनेस")
    private val up = listOf("up", "increase", "raise", "louder", "badha", "badhao", "बढ़ा", "बढ़ाओ")
    private val down = listOf("down", "decrease", "lower", "reduce", "quieter", "kam", "ghata", "घटा", "कम")

    fun parse(input: String): Intent {
        val t = input.trim().lowercase()
        if (t.isEmpty()) return Intent.Unknown
        fun has(words: List<String>) = words.any { t.contains(it) }
        return when {
            has(battery) -> Intent.BatteryLevel
            has(volume) -> when {
                has(up) -> Intent.VolumeUp
                has(down) -> Intent.VolumeDown
                else -> Intent.VolumeGet
            }
            has(brightness) -> Intent.BrightnessGet
            else -> Intent.Unknown
        }
    }
}
