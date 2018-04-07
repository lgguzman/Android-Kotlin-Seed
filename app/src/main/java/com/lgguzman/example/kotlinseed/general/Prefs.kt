package com.lgguzman.example.kotlinseed.general
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import org.json.JSONArray

/**
 * Created by lgguzman on 27/03/18.
 */
class Prefs (context: Context) {
    private val PREFS_FILENAME = context.packageName
    private val TOKEN = "token"
    private val LOADING = "loading"
    val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var token: String
        get() = prefs.getString(TOKEN, "")
        set(value)   {
            prefs.edit().putString(TOKEN, value).commit()
        }

    var isLoading: Boolean
        get() = prefs.getBoolean(LOADING, false)
        set(value) {prefs.edit().putBoolean(LOADING, value).commit()}


}