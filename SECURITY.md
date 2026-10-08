# Security policy

## Supported versions
Only the latest pre-release (currently 0.3.1-beta.1) is supported. Older betas get no fixes.

## Reporting a vulnerability
Use the Security tab of this repository (private vulnerability reporting) if it is shown. If it is not, open an issue that says only that you have a security report and no details, and the maintainer will arrange a private channel. Please do not post exploit details publicly.

## Privacy and data (0.3.1-beta.1)
- Everything runs on the phone. The app declares no INTERNET permission and sends nothing.
- Voice is turned into text by the phone's on-device recognizer when the phone supports it. If not, voice shows "Unavailable". Online voice is OFF unless the user allows it in the app; if allowed, the phone's speech service sends the audio to Google's servers (the app itself still has no internet access). The choice lasts until the app is closed.
- The app does not record or save audio. Messages are not saved after the app closes in this beta.
- No analytics, no accounts, no API keys, no billing.

## Permissions
- RECORD_AUDIO: asked on the first mic tap, to hear voice commands.
- FOREGROUND_SERVICE and FOREGROUND_SERVICE_MICROPHONE: used only while the user turns Hands-free ON. A persistent notification with a Stop button is shown while the microphone is on. Android requires the service to be started from the visible app; it does not restart by itself after a phone restart.
- POST_NOTIFICATIONS: lets the Hands-free notification show on Android 13+.
- Not requested: contacts, location, storage, accessibility, internet.

## Maintenance
A security check runs daily (5 PM IST). Patches ship only for real findings, in the 6-9 PM IST window. This file is updated with every release.
