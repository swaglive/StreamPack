package com.example.live.streampack

enum class LivestreamProtocolType {
    RTMP,
    SRT;

    companion object {
        fun fromString(type: String) = when (type) {
            "srt" -> SRT
            else -> RTMP
        }
    }
}