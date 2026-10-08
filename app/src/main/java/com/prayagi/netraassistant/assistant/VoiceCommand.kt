package com.prayagi.netraassistant.assistant

/** Picks the persona from a spoken name prefix ("Netra, battery", "hey Trikal volume up"). */
object VoiceCommand {
    data class Parsed(val persona: Persona?, val text: String)

    private val lead = setOf("hey", "hi", "ok", "okay", "he", "हे")
    private val netra = setOf("netra", "netraa", "नेत्रा", "नेत्र")
    private val trikal = setOf("trikal", "trikaal", "trical", "त्रिकाल")

    private fun tokens(t: String): List<String> =
        t.split(Regex("[^\\p{L}\\p{N}\\p{M}]+")).filter { it.isNotEmpty() }

    fun parse(raw: String): Parsed {
        val tk = tokens(raw.trim().lowercase())
        if (tk.isEmpty()) return Parsed(null, "")
        var i = 0
        if (tk[0] in lead && tk.size > 1 && (tk[1] in netra || tk[1] in trikal)) i = 1
        val persona = when (tk[i]) {
            in netra -> Persona.NETRA
            in trikal -> Persona.TRIKAL
            else -> return Parsed(null, raw.trim())
        }
        return Parsed(persona, tk.drop(i + 1).joinToString(" "))
    }
}
