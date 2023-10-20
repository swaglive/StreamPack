package io.github.thibaultbee.streampack.streamers.fps

import io.github.thibaultbee.streampack.internal.utils.Scheduler
import io.github.thibaultbee.streampack.listeners.OnFpsListener

class FpsCalculator {
    private var fpsCount: Int = 0
    var listener: OnFpsListener? = null

    private val scheduler = Scheduler(1000) {
        listener?.onFps(fpsCount)
        fpsCount = 0
    }

    fun start() {
        scheduler.start()
    }

    fun stop() {
        scheduler.cancel()
    }

    fun increment() {
        fpsCount++
    }
}