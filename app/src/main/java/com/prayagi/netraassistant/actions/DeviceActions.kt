package com.prayagi.netraassistant.actions

import android.content.Context
import android.content.Intent as AndroidIntent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.BatteryManager
import android.provider.Settings

/** Result is either a real value or Unavailable. Never a guess. */
sealed class Result<out T> {
    data class Value<T>(val v: T) : Result<T>()
    data class Unavailable(val reason: String) : Result<Nothing>()
}

data class BatteryInfo(val levelPct: Int, val charging: Boolean)
data class VolumeInfo(val current: Int, val max: Int)

class DeviceActions(private val context: Context) {

    fun battery(): Result<BatteryInfo> = try {
        val i = context.registerReceiver(null, IntentFilter(AndroidIntent.ACTION_BATTERY_CHANGED))
        val level = i?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = i?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val status = i?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        if (level < 0 || scale <= 0) {
            Result.Unavailable("battery data not reported")
        } else {
            val charging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL
            Result.Value(BatteryInfo(level * 100 / scale, charging))
        }
    } catch (e: Exception) {
        Result.Unavailable("battery read failed")
    }

    private fun audio(): AudioManager? =
        context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager

    fun volume(): Result<VolumeInfo> = try {
        val a = audio()
        if (a == null) {
            Result.Unavailable("audio service missing")
        } else {
            Result.Value(VolumeInfo(a.getStreamVolume(AudioManager.STREAM_MUSIC), a.getStreamMaxVolume(AudioManager.STREAM_MUSIC)))
        }
    } catch (e: Exception) {
        Result.Unavailable("volume read failed")
    }

    fun volumeStep(up: Boolean): Result<VolumeInfo> = try {
        val a = audio()
        if (a == null) {
            Result.Unavailable("audio service missing")
        } else {
            a.adjustStreamVolume(
                AudioManager.STREAM_MUSIC,
                if (up) AudioManager.ADJUST_RAISE else AudioManager.ADJUST_LOWER,
                0
            )
            volume()
        }
    } catch (e: Exception) {
        Result.Unavailable("volume change not allowed (for example Do Not Disturb)")
    }

    /** Reading brightness needs no permission. Changing it needs WRITE_SETTINGS (not in beta 1). */
    fun brightnessPct(): Result<Int> = try {
        val v = Settings.System.getInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        Result.Value(v * 100 / 255)
    } catch (e: Exception) {
        Result.Unavailable("brightness not readable")
    }
}
