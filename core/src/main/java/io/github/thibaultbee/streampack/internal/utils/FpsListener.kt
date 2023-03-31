package io.github.thibaultbee.streampack.internal.utils

/**
 * Created by pedro on 09/07/19.
 */
class FpsListener {
  private var fpsCont = 0
  private var ts = System.currentTimeMillis()
  private var callback: Callback? = null

  interface Callback {
    fun onFps(fps: Int)
  }

  fun setCallback(callback: Callback?) {
    this.callback = callback
  }

  fun calculateFps() {
    fpsCont++
    if (System.currentTimeMillis() - ts >= 1000) {
      if (callback != null) callback!!.onFps(fpsCont)
      fpsCont = 0
      ts = System.currentTimeMillis()
    }
  }
}