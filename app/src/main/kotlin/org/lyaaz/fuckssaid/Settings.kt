package org.lyaaz.fuckssaid

import android.content.SharedPreferences
import kotlin.concurrent.Volatile

class Settings private constructor(private val prefs: SharedPreferences) {
    var idForPackage: String?
        get() = prefs.getString(PREF_SSAID, null)
        set(id) {
            val editor = prefs.edit()
            editor.putString(PREF_SSAID, id)
            editor.apply()
        }

    @Synchronized
    fun getOrCreateId(): String {
        return idForPackage ?: java.util.UUID.randomUUID().toString().also { idForPackage = it }
    }

    companion object {
        const val PREF_SSAID = "ssaid"

        @Volatile
        private var INSTANCE: Settings? = null
        fun getInstance(prefs: SharedPreferences): Settings {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Settings(prefs).also { INSTANCE = it }
            }
        }
    }
}
