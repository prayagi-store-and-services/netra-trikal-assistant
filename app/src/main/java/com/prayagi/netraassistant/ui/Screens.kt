package com.prayagi.netraassistant.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayagi.netraassistant.actions.DeviceActions
import com.prayagi.netraassistant.assistant.IntentParser
import com.prayagi.netraassistant.assistant.Persona
import com.prayagi.netraassistant.assistant.Responder

private data class Msg(val fromUser: Boolean, val text: String)

@Composable
fun ChatScreen() {
    val ctx = LocalContext.current
    val device = remember { DeviceActions(ctx.applicationContext) }
    var persona by remember { mutableStateOf(Persona.NETRA) }
    var input by remember { mutableStateOf("") }
    // Separate conversation per persona (separate memories; encrypted storage comes in beta 2).
    val logs = remember {
        mapOf(
            Persona.NETRA to mutableStateListOf(Msg(false, Persona.NETRA.greeting())),
            Persona.TRIKAL to mutableStateListOf(Msg(false, Persona.TRIKAL.greeting()))
        )
    }
    val log = logs.getValue(persona)

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Persona.values().forEach { p ->
            if (p == persona) {
                Button(onClick = { persona = p }) { Text(p.displayName) }
            } else {
                OutlinedButton(onClick = { persona = p }) { Text(p.displayName) }
            }
        }
    }
    Text(persona.tagline, fontSize = 12.sp)
    log.forEach { m ->
        Text((if (m.fromUser) "You: " else persona.displayName + ": ") + m.text)
    }
    OutlinedTextField(
        value = input,
        onValueChange = { input = it },
        label = { Text("Type a message") },
        modifier = Modifier.fillMaxWidth()
    )
    Button(onClick = {
        val q = input.trim()
        if (q.isNotEmpty()) {
            log.add(Msg(true, q))
            log.add(Msg(false, Responder.respondAll(persona, IntentParser.parseAll(q), device)))
            input = ""
        }
    }) { Text("Send") }
}

@Composable
fun PermissionsScreen() {
    Text("Permissions", fontSize = 18.sp)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("This beta requests no Android permissions.")
        Text("Battery: read through the standard battery broadcast. Why: to answer battery questions.")
        Text("Volume: uses the media volume controls. Why: to read or step volume.")
        Text("Brightness: read-only. Why: to report current brightness. Changing it needs a Settings permission that is not requested yet (Unavailable).")
    }
}

@Composable
fun PrivacyScreen() {
    Text("Privacy", fontSize = 18.sp)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Everything runs on this phone. This app has no internet permission and sends nothing anywhere.")
        Text("Messages you type are not saved after you close the app in this beta.")
        Text("Any future cloud or AI step that sends data will show exactly what is sent and where, and will need your explicit opt-in.")
    }
}
