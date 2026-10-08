package com.prayagi.netraassistant.assistant

import com.prayagi.netraassistant.actions.BatteryInfo
import com.prayagi.netraassistant.actions.DeviceActions
import com.prayagi.netraassistant.actions.Result
import com.prayagi.netraassistant.actions.VolumeInfo

object Responder {
    fun respond(persona: Persona, intent: Intent, device: DeviceActions): String = when (intent) {
        Intent.BatteryLevel -> batteryText(persona, device.battery())
        Intent.VolumeGet -> volumeText(persona, device.volume())
        Intent.VolumeUp -> volumeText(persona, device.volumeStep(true))
        Intent.VolumeDown -> volumeText(persona, device.volumeStep(false))
        Intent.BrightnessGet -> brightnessText(persona, device.brightnessPct())
        Intent.Unknown -> unknown(persona)
    }

    fun batteryText(p: Persona, r: Result<BatteryInfo>): String = when (r) {
        is Result.Value -> {
            val c = if (r.v.charging) "charging" else "not charging"
            if (p == Persona.NETRA) "Battery ${r.v.levelPct}% hai, $c." else "Battery ${r.v.levelPct}%, $c."
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
}
