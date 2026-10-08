package com.prayagi.netraassistant

import com.prayagi.netraassistant.assistant.Intent
import com.prayagi.netraassistant.assistant.IntentParser
import org.junit.Assert.assertEquals
import org.junit.Test

class IntentParserTest {
    @Test fun battery() = assertEquals(Intent.BatteryLevel, IntentParser.parse("What is my battery level?"))
    @Test fun batteryHindi() = assertEquals(Intent.BatteryLevel, IntentParser.parse("बैटरी कितनी है"))
    @Test fun volumeUp() = assertEquals(Intent.VolumeUp, IntentParser.parse("volume up please"))
    @Test fun volumeDown() = assertEquals(Intent.VolumeDown, IntentParser.parse("awaz kam karo"))
    @Test fun volumeGet() = assertEquals(Intent.VolumeGet, IntentParser.parse("volume?"))
    @Test fun brightness() = assertEquals(Intent.BrightnessGet, IntentParser.parse("brightness kitni hai"))
    @Test fun unknown() = assertEquals(Intent.Unknown, IntentParser.parse("tell me a joke"))
    @Test fun empty() = assertEquals(Intent.Unknown, IntentParser.parse("   "))
}
