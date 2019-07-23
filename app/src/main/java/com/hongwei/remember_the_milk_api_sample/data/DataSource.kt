package com.hongwei.remember_the_milk_api_sample.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.hongwei.remember_the_milk_api_sample.injection.annotations.AppContext
import it.bova.rtmapi.RtmApiAuthenticator
import it.bova.rtmapi.Token
import javax.inject.Inject

class DataSource @Inject constructor(@AppContext context: Context) {
    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences("remember-the-milk-sample", Context.MODE_PRIVATE)
    }

    var authenticator: RtmApiAuthenticator? = null

    var frob: String? = null

    fun saveToken(token: Token) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(token)
        editor.putString("token", json)
        editor.apply()
    }

    fun retriveToken(): Token? {
        val gson = Gson()
        val json = sharedPreferences.getString("token", null)
        val token = gson.fromJson<Any>(json, Token::class.java)
        if (token != null) {
            return token as Token
        }
        return null
    }

    fun retriveTokenLocal(): Token? {
        val gson = Gson()
        val json = com.hongwei.remember_the_milk_api_sample.data.model.Token.json
        val token = gson.fromJson<Any>(json, Token::class.java)
        if (token != null) {
            return token as Token
        }
        return null
    }
}