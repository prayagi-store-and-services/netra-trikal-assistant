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

class IntentParserOwnerTest {
    @Test fun hiIsGreeting() = assertEquals(listOf<Intent>(Intent.Greeting), IntentParser.parseAll("hi"))
    @Test fun hiNotInsideWords() = assertEquals(listOf<Intent>(Intent.Unknown), IntentParser.parseAll("this is something"))
    @Test fun thanks() = assertEquals(listOf<Intent>(Intent.Thanks), IntentParser.parseAll("thanks a lot"))
    @Test fun help() = assertEquals(listOf<Intent>(Intent.Help), IntentParser.parseAll("help"))
    @Test fun fullReport() = assertEquals(
        listOf<Intent>(Intent.BatteryLevel, Intent.BatteryTemp, Intent.BatteryTime),
        IntentParser.parseAll("what is my battery statics full report")
    )
    @Test fun tempAndTimeSentence() = assertEquals(
        listOf<Intent>(Intent.BatteryTemp, Intent.BatteryTime),
        IntentParser.parseAll("agar battery ke bare mein bata rahi ho to yah batao mere phone ka temperature kitna hai mere phone ke uses ke hisab se mera phone kitne ghante chal sakta hai")
    )
    @Test fun tempOnly() = assertEquals(listOf<Intent>(Intent.BatteryTemp), IntentParser.parseAll("phone ka temperature kitna hai"))
    @Test fun batteryStillLevel() = assertEquals(listOf<Intent>(Intent.BatteryLevel), IntentParser.parseAll("battery kitni hai"))
}
