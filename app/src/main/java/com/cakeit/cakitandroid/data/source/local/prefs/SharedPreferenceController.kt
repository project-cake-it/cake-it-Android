package com.cakeit.cakitandroid.data.source.local.prefs

import android.content.Context

object SharedPreferenceController {

    fun setAccessToken(context: Context, accessToken : String) {
        val pref = context.getSharedPreferences("accessToken", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("accessToken", accessToken)
        editor.commit()
    }

    fun getAccessToken(context: Context) : String {
        val pref = context.getSharedPreferences("accessToken", Context.MODE_PRIVATE)
        return pref.getString("accessToken", "")!!
    }

    fun setSocialType(context: Context, socialType : String) {
        val pref = context.getSharedPreferences("socialType", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("socialType", socialType)
        editor.commit()
    }

    fun getSocialType(context: Context) : String {
        val pref = context.getSharedPreferences("socialType", Context.MODE_PRIVATE)
        return pref.getString("socialType", "")!!
    }
}