package com.prayagi.netraassistant.assistant

import com.prayagi.netraassistant.actions.BatteryInfo
import com.prayagi.netraassistant.actions.BatteryTemp
import com.prayagi.netraassistant.actions.BatteryTime
import com.prayagi.netraassistant.actions.DeviceActions
import com.prayagi.netraassistant.actions.Result
import com.prayagi.netraassistant.actions.VolumeInfo

object Responder {
    fun respondAll(persona: Persona, intents: List<Intent>, device: DeviceActions): String =
        intents.joinToString("\n") { respond(persona, it, device) }

    fun respond(persona: Persona, intent: Intent, device: DeviceActions): String = when (intent) {
        Intent.BatteryLevel -> batteryText(persona, device.battery())
        Intent.BatteryTemp -> tempText(persona, device.batteryTemp())
        Intent.BatteryTime -> timeText(persona, device.batteryTime())
        Intent.Greeting -> greeting(persona)
        Intent.Thanks -> if (persona == Persona.NETRA) "Aapka swagat hai." else "Welcome."
        Intent.Help -> help(persona)
        Intent.VolumeGet -> volumeText(persona, device.volume())
        Intent.VolumeUp -> volumeText(persona, device.volumeStep(true))
        Intent.VolumeDown -> volumeText(persona, device.volumeStep(false))
        Intent.BrightnessGet -> brightnessText(persona, device.brightnessPct())
        Intent.Unknown -> unknown(persona)
    }

    fun batteryText(p: Persona, r: Result<BatteryInfo>): String = when (r) {
        is Result.Value -> {
            val c = if (r.v.charging) "charging" else "not charging"
            if (p == Persona.NETRA) "Battery ${r.v.levelPct}% hai, ${if (r.v.charging) "charge ho rahi hai" else "charge nahi ho rahi"}." else "Battery ${r.v.levelPct}%, $c."
        }
        is Result.Unavailable -> "Battery: Unavailable (${r.reason})."
    }

    fun volumeText(p: Persona, r: Result<VolumeInfo>): String = when (r) {
        is Result.Value -> if (p == Persona.NETRA) "Media volume ${r.v.current} / ${r.v.max} hai." else "Volume ${r.v.current}/${r.v.max}."
        is Result.Unavailable -> "Volume: Unavailable (${r.reason})."
    }

    fun brightnessText(p: Persona, r: Result<Int>): String = when (r) {
        is Result.Value -> if (p == Persona.NETRA) "Brightness lagbhag ${r.v}% hai. Badalna abhi is beta mein Unavailable hai." else "Brightness ${r.v}%. Changing it: Unavailable in this beta."
        is Result.Unavailable -> "Brightness: Unavailable (${r.reason})."
    }

    fun unknown(p: Persona): String =
        if (p == Persona.NETRA) "Maaf kijiye, abhi main sirf battery, volume aur brightness samajhti hoon."
        else "Not supported yet. Try: battery, volume, brightness."

    fun tempText(p: Persona, r: Result<BatteryTemp>): String = when (r) {
        is Result.Value -> if (p == Persona.NETRA) "Battery ka temperature ${"%.1f".format(r.v.celsius)} C hai." else "Battery temp ${"%.1f".format(r.v.celsius)} C."
        is Result.Unavailable -> "Temperature: Unavailable (${r.reason})."
    }

    fun timeText(p: Persona, r: Result<BatteryTime>): String = when (r) {
        is Result.Value -> {
            val h = "%.1f".format(r.v.hours)
            if (p == Persona.NETRA) "Abhi ke upyog (lagbhag ${r.v.basedOnMa} mA) ke hisab se lagbhag $h ghante chalega. Yeh sirf andaza hai."
            else "About $h h at the current draw (~${r.v.basedOnMa} mA). Rough estimate only."
        }
        is Result.Unavailable -> "Battery time: Unavailable (${r.reason})."
    }

    fun greeting(p: Persona): String =
        if (p == Persona.NETRA) "Namaste! Main aapki kya madad kar sakti hoon?" else "Hello. What do you need?"

    fun help(p: Persona): String =
        if (p == Persona.NETRA) "Main batayi sakti hoon: battery level, temperature, battery kitne ghante chalegi, volume (badhana/ghatana), aur brightness."
        else "Battery level, temperature, time left, volume up/down, brightness."
}
