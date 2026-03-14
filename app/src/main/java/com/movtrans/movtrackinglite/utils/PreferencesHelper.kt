package com.movtrans.movtrackinglite.utils

import android.content.Context

object PreferencesHelper {
    private const val PREFS_NAME = "movtracking_prefs"
    private const val KEY_REGISTERED = "is_registered"
    private const val KEY_EMPRESA = "empresa"
    private const val KEY_DEVICE_ID = "device_id"
    private const val KEY_TOKEN = "token"

    fun saveRegistration(context: Context, empresa: String, deviceId: String, token: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .putString(KEY_EMPRESA, empresa)
            .putString(KEY_DEVICE_ID, deviceId)
            .putString(KEY_TOKEN, token)
            .putBoolean(KEY_REGISTERED, true)
            .apply()
    }

    fun isRegistered(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_REGISTERED, false)
    }

    fun getToken(context: Context): String? {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_TOKEN, null)
    }
}