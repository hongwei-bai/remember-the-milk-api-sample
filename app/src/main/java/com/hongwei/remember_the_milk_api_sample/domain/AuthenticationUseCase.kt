package com.hongwei.remember_the_milk_api_sample.domain

import android.util.Log
import com.google.gson.Gson
import com.hongwei.remember_the_milk_api_sample.data.DataSource
import it.bova.rtmapi.Permission
import it.bova.rtmapi.RtmApiAuthenticator
import javax.inject.Inject

class AuthenticationUseCase @Inject constructor(val dataSource: DataSource) {
    companion object {
        const val TAG = "rtm.auth.usecase"
    }

    fun prepare(apiKey: String, sharedSecret: String): String? {
        dataSource.authenticator = RtmApiAuthenticator(apiKey, sharedSecret)

        dataSource.authenticator?.apply {
            dataSource.authToken = dataSource.retriveToken()
            Log.i(TAG, "retrive token: ${dataSource.authToken}")
            if (dataSource.authToken == null) {
                dataSource.frob = authGetFrob()
                Log.i(TAG, "frob: ${dataSource.frob}")
                val validationUrl = authGetDesktopUrl(Permission.READ, dataSource.frob)
                Log.i(TAG, "validationUrl: $validationUrl")
                return validationUrl
            } else {
                val gson = Gson()
                val json = gson.toJson(dataSource.authToken)
                Log.i(TAG, "token.display: $json")
                return null
            }
        }
        return null
    }

    fun execute(): Boolean {
        dataSource.authenticator?.apply {
            try {
                Log.i(TAG, "frob: ${dataSource.frob}")

                dataSource.authToken = authGetToken(dataSource.frob)
                Log.i(TAG, "authToken: ${dataSource.authToken}")

                dataSource.saveToken(dataSource.authToken!!)
                Log.i(TAG, "authToken: ${dataSource.authToken}")
            } catch (e: Exception) {
                Log.i(TAG, "catch exception: $e")
                return false
            }
            return true
        }
        return false
    }
}