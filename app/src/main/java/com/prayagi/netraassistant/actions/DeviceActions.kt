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
data class BatteryTemp(val celsius: Double)
data class BatteryTime(val hours: Double, val basedOnMa: Int)
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


    fun batteryTemp(): Result<BatteryTemp> = try {
        val i = context.registerReceiver(null, IntentFilter(AndroidIntent.ACTION_BATTERY_CHANGED))
        val t = i?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, Int.MIN_VALUE) ?: Int.MIN_VALUE
        if (t == Int.MIN_VALUE || t <= 0) Result.Unavailable("temperature not reported by this phone")
        else Result.Value(BatteryTemp(t / 10.0))
    } catch (e: Exception) {
        Result.Unavailable("temperature read failed")
    }

    /** Rough estimate from remaining charge and the current draw right now. Not a prediction of future use. */
    fun batteryTime(): Result<BatteryTime> = try {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
        val counterUah = bm?.getLongProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER) ?: 0L
        val nowUa = bm?.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW) ?: 0L
        val info = battery()
        if (info is Result.Value && info.v.charging) {
            Result.Unavailable("phone is charging, discharge time cannot be estimated")
        } else if (counterUah <= 0L || nowUa == 0L || nowUa == Long.MIN_VALUE) {
            Result.Unavailable("this phone does not report remaining charge or current draw")
        } else {
            // Units differ by device (uA or mA); values above 20000 are treated as uA.
            val drawMa = Math.abs(nowUa).let { if (it > 20000L) it / 1000.0 else it.toDouble() }
            val capMah = counterUah / 1000.0
            if (drawMa < 1.0 || capMah < 1.0) Result.Unavailable("draw or charge reading too small to trust")
            else Result.Value(BatteryTime(capMah / drawMa, drawMa.toInt()))
        }
    } catch (e: Exception) {
        Result.Unavailable("battery time read failed")
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
