package com.movtrans.movtrackinglite.utils

import android.content.Context

object PreferencesHelper {
    private const val PREFS_NAME = "movtracking_prefs"
    private const val KEY_REGISTERED = "is_registered"

    fun saveRegistration(context: Context, empresa: String, deviceId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString("empresa", empresa)
            .putString("device_id", deviceId)
            .putBoolean(KEY_REGISTERED, true)
            .apply()
    }

    fun isRegistered(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_REGISTERED, false)
    }
}