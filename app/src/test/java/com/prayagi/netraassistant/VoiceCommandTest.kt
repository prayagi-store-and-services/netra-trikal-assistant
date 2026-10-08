package com.prayagi.netraassistant

import com.prayagi.netraassistant.assistant.Persona
import com.prayagi.netraassistant.assistant.VoiceCommand
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class VoiceCommandTest {
    @Test fun netraPrefix() {
        val p = VoiceCommand.parse("Netra, battery kitni hai")
        assertEquals(Persona.NETRA, p.persona); assertEquals("battery kitni hai", p.text)
    }
    @Test fun heyTrikal() {
        val p = VoiceCommand.parse("hey Trikal volume up")
        assertEquals(Persona.TRIKAL, p.persona); assertEquals("volume up", p.text)
    }
    @Test fun devanagariName() {
        val p = VoiceCommand.parse("नेत्रा बैटरी")
        assertEquals(Persona.NETRA, p.persona); assertEquals("बैटरी", p.text)
    }
    @Test fun noName() {
        val p = VoiceCommand.parse("hello")
        assertNull(p.persona); assertEquals("hello", p.text)
    }
    @Test fun nameOnly() {
        val p = VoiceCommand.parse("Trikal")
        assertEquals(Persona.TRIKAL, p.persona); assertEquals("", p.text)
    }
}
