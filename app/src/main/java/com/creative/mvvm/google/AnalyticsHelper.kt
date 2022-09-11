package com.creative.mvvm.google

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

object AnalyticsHelper {
    fun logEventSizeNote(size: Long) {
        Firebase.analytics.logEvent("save_note") {
            param(FirebaseAnalytics.Param.VALUE, size)
        }
    }
}