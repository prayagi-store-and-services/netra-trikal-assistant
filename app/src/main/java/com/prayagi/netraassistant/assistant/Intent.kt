package com.prayagi.netraassistant.assistant

sealed class Intent {
    object BatteryLevel : Intent()
    object BatteryTemp : Intent()
    object BatteryTime : Intent()
    object VolumeGet : Intent()
    object VolumeUp : Intent()
    object VolumeDown : Intent()
    object BrightnessGet : Intent()
    object Greeting : Intent()
    object Thanks : Intent()
    object Help : Intent()
    object Unknown : Intent()
}

/** Rule-based, on-device intent parser (English + Hindi in Latin script and Devanagari). */
object IntentParser {
    private val battery = listOf("battery", "charge", "charging", "बैटरी", "चार्ज")
    private val report = listOf("report", "status", "statics", "stats", "statistics", "details", "full", "sab", "सब")
    private val temp = listOf("temperature", "temp", "garm", "garmi", "tapman", "heat", "hot", "तापमान", "गर्म", "टेम्परेचर")
    private val time = listOf("ghante", "ghanta", "hours", "hour", "backup", "chalega", "sakta", "bachi", "remaining", "घंटे", "घंटा", "बैकअप")
    private val volume = listOf("volume", "sound", "awaz", "awaaz", "आवाज", "वॉल्यूम")
    private val brightness = listOf("brightness", "bright", "roshni", "ब्राइटनेस")
    private val up = listOf("up", "increase", "raise", "louder", "badha", "badhao", "बढ़ा", "बढ़ाओ")
    private val down = listOf("down", "decrease", "lower", "reduce", "quieter", "kam", "ghata", "घटा", "कम")
    private val greet = listOf("hi", "hello", "hey", "namaste", "namaskar", "hii", "नमस्ते", "हेलो", "हाय")
    private val thanks = listOf("thanks", "thank", "thankyou", "shukriya", "dhanyavad", "धन्यवाद", "शुक्रिया")
    private val help = listOf("help", "madad", "options", "मदद")

    private fun tokens(t: String): List<String> =
        t.split(Regex("[^\\p{L}\\p{N}\\p{M}]+")).filter { it.isNotEmpty() }

    private fun has(tokens: List<String>, words: List<String>): Boolean =
        tokens.any { tok -> words.any { w -> if (w.length <= 3) tok == w else tok.startsWith(w) } }

    /** All intents found in the message, in answer order. Unknown only when nothing matched. */
    fun parseAll(input: String): List<Intent> {
        val tk = tokens(input.trim().lowercase())
        if (tk.isEmpty()) return listOf(Intent.Unknown)
        val out = mutableListOf<Intent>()
        val isBattery = has(tk, battery)
        val fullReport = isBattery && has(tk, report)
        val wantsTemp = has(tk, temp)
        val wantsTime = has(tk, time) && (isBattery || has(tk, listOf("phone", "chal", "fone")) )
        if (isBattery && (fullReport || (!wantsTemp && !wantsTime) || has(tk, listOf("level", "percent", "kitni", "kitna")) && !wantsTemp && !wantsTime)) {
            out.add(Intent.BatteryLevel)
        }
        if (wantsTemp) out.add(Intent.BatteryTemp) else if (fullReport) out.add(Intent.BatteryTemp)
        if (wantsTime) out.add(Intent.BatteryTime) else if (fullReport) out.add(Intent.BatteryTime)
        if (has(tk, volume)) {
            out.add(when {
                has(tk, up) -> Intent.VolumeUp
                has(tk, down) -> Intent.VolumeDown
                else -> Intent.VolumeGet
            })
        }
        if (has(tk, brightness)) out.add(Intent.BrightnessGet)
        if (out.isEmpty()) {
            when {
                has(tk, thanks) -> out.add(Intent.Thanks)
                has(tk, help) -> out.add(Intent.Help)
                has(tk, greet) -> out.add(Intent.Greeting)
                else -> out.add(Intent.Unknown)
            }
        }
        return out.distinct()
    }

    fun parse(input: String): Intent = parseAll(input).first()
}
