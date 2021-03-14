package com.cakeit.cakitandroid.data.source.local.prefs

import android.content.Context

object SharedPreferenceController {
    private val token= "token"

    fun setToken(context: Context, token : String) {
        val pref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", token)
        editor.commit()
    }

    fun getToken(context: Context) : String {
        val pref = context.getSharedPreferences("token", Context.MODE_PRIVATE)
        return pref.getString("token", "")!!
    }
}