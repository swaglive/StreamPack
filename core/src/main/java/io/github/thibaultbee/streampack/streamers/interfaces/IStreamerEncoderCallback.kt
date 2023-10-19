package io.github.thibaultbee.streampack.streamers.interfaces

import android.content.Context
import io.github.thibaultbee.streampack.internal.encoders.AudioMediaCodecEncoder
import io.github.thibaultbee.streampack.internal.encoders.IEncoderListener
import io.github.thibaultbee.streampack.internal.encoders.VideoMediaCodecEncoder
import io.github.thibaultbee.streampack.listeners.OnErrorListener

interface IStreamerEncoderCallback {
    fun onVideoEncoderCreate(
        encoderListener: IEncoderListener,
        onInternalErrorListener: OnErrorListener,
        context: Context,
        useSurfaceMode: Boolean,
        manageVideoOrientation: Boolean
    ): VideoMediaCodecEncoder?

    fun onAudioEncoderCreate(
        encoderListener: IEncoderListener,
        onInternalErrorListener: OnErrorListener
    ): AudioMediaCodecEncoder?
}