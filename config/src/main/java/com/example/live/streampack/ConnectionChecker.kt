package com.example.live.streampack

interface ConnectionChecker {

    fun onConnectionSuccess()

    fun onConnectionFailed(reason: String)

    fun onNewBitrate(bitrate: Long)

    fun onDisconnect()

    fun onAuthError()

    fun onAuthSuccess()
}