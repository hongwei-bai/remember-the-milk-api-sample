package com.hongwei.remember_the_milk_api_sample.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.hongwei.remember_the_milk_api_sample.ApiConfig.DataStoreKey.KEY_API_KEY
import com.hongwei.remember_the_milk_api_sample.ApiConfig.DataStoreKey.KEY_SHARED_SECRET
import com.hongwei.remember_the_milk_api_sample.ApiConfig.DataStoreKey.KEY_TOKEN
import com.hongwei.remember_the_milk_api_sample.injection.annotations.AppContext
import it.bova.rtmapi.RtmApiAuthenticator
import it.bova.rtmapi.Token
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSource @Inject constructor(@AppContext context: Context) {
    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("remember-the-milk-sample", Context.MODE_PRIVATE)

    var authenticator: RtmApiAuthenticator? = null

    var frob: String? = null

    var apiKey: String? = null

    var sharedSecret: String? = null

    fun retriveApiKey(): String? {
        if (null == apiKey) {
            apiKey = retriveString(KEY_API_KEY)
        }

        return apiKey
    }

    fun saveApiKey(apikey: String) {
        apiKey = apikey
        saveString(KEY_API_KEY, apikey)
    }

    fun retriveSharedSecret(): String? {
        if (null == sharedSecret) {
            sharedSecret = retriveString(KEY_SHARED_SECRET)
        }

        return sharedSecret
    }

    fun saveSharedSecret(secret: String) {
        sharedSecret = secret
        saveString(KEY_SHARED_SECRET, secret)
    }

    fun saveToken(token: Token) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(token)
        editor.putString(KEY_TOKEN, json)
        editor.apply()
    }

    fun retriveToken(): Token? {
        val gson = Gson()
        val json = sharedPreferences.getString(KEY_TOKEN, null)
        val token = gson.fromJson<Any>(json, Token::class.java)
        if (token != null) {
            return token as Token
        }
        return null
    }

    private fun saveString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(value)
        editor.putString(key, json)
        editor.apply()
    }

    private fun retriveString(key: String): String? {
        val gson = Gson()
        val json = sharedPreferences.getString(key, null)
        return gson.fromJson(json, String::class.java)
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