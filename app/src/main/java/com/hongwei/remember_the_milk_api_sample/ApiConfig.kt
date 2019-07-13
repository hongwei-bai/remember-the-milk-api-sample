package com.hongwei.remember_the_milk_api_sample

object ApiConfig {
    const val IS_LOGGING_ENABLED = true

    object Urls {
        const val DOMAIN = "www.rememberthemilk.com"
        const val BASE = "https://" + DOMAIN

        const val USER_HOME = BASE + "/app"
        const val API_HOME = BASE + "/services/api/"
        const val NON_COMMERCIAL_KEY_APPLY = BASE + "/services/api/requestkey.rtm"
    }
}