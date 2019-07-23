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

    object Constants {
        const val REQUEST_CODE_AUTH = 442

        const val RESULT_CODE_FAILURE = 0
        const val RESULT_CODE_SUCCESS = 944
    }

    object Cridentials {
        const val API_KEY = "28c5974a50066a0b200bec226e91179f"
        const val SHARED_SECRET = "fdde1ce02f803273"
    }
}