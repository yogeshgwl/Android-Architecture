package com.task.utils.analytics

interface AppAnalytics {
    fun logEvents(logEventName: String, vararg eventActionPairs: Pair<String, Any>)
}