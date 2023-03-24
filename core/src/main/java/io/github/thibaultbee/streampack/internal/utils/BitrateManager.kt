package io.github.thibaultbee.streampack.internal.utils

import io.github.thibaultbee.streampack.listeners.OnConnectionListener

/**
 * Created by pedro on 10/07/19.
 *
 * Calculate video and audio bitrate per second
 */
class BitrateManager {
    var connectionListener: OnConnectionListener? = null
    private var bitrate: Long = 0
    private var timeStamp = System.currentTimeMillis()

    @Synchronized
    fun calculateBitrate(size: Long) {
        bitrate += size
        val timeDiff = System.currentTimeMillis() - timeStamp
        if (timeDiff >= 1000) {
            connectionListener?.onNewBitrate((bitrate / (timeDiff / 1000f)).toLong())
            timeStamp = System.currentTimeMillis()
            bitrate = 0
        }
    }
}