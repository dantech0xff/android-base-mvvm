package com.creative.mvvm.data.app

import org.json.JSONObject

data class AppData(val version: String) {
    companion object {
        fun fromJson(json: JSONObject?): AppData {
            return AppData(json?.optString("version", "v0.0.1") ?: "v0.0.1")
        }
    }

    fun toJson(): String {
        return JSONObject().accumulate("version", version).toString()
    }
}