package com.task.utils.analytics

interface AppAnalytics {
    fun logEvents(logEventName: String, eventActionPairs: HashMap<String, Any>)
}