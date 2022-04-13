/*
 * Copyright (C) 2022 Thibault B.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.thibaultbee.streampack.internal.muxers.mediamuxer

import android.media.MediaCodec
import android.media.MediaMuxer
import io.github.thibaultbee.streampack.data.Config
import io.github.thibaultbee.streampack.internal.data.Frame
import io.github.thibaultbee.streampack.internal.muxers.IMuxer
import io.github.thibaultbee.streampack.internal.muxers.IMuxerHelper
import io.github.thibaultbee.streampack.internal.muxers.IMuxerListener
import java.io.File

class MediaMuxerEndpoint(private val format: Int) : IMuxer {
    override val helper: IMuxerHelper = MediaMuxerHelper()
    override var manageVideoOrientation: Boolean = false // Useless here
    override var listener: IMuxerListener? = null
        set(_) = throw UnsupportedOperationException("No callback possible")

    var file: File? = null
        set(value) {
            value?.let {
                mediaMuxer = MediaMuxer(it.path, format)
            }
            field = value
        }
    private var mediaMuxer: MediaMuxer? = null

    override fun configure(config: Unit) {
        // Nothing to configure
    }

    override fun addStreams(streamsConfig: List<Config>): Map<Config, Int> {
        val streamMap = mutableMapOf<Config, Int>()
        streamsConfig.forEach {
            val index = mediaMuxer?.addTrack(it.toMediaFormat())!!
            streamMap[it] = index
        }
        return streamMap
    }

    override fun encode(frame: Frame, streamPid: Int) {
        require(mediaMuxer != null) { "MediaMuxer must be initialized" }
        mediaMuxer!!.writeSampleData(streamPid, frame.buffer, MediaCodec.BufferInfo().apply {
            presentationTimeUs = frame.pts
            flags = if (frame.isKeyFrame) {
                MediaCodec.BUFFER_FLAG_KEY_FRAME
            } else {
                0
            }
        })
    }

    override fun startStream() {
        require(mediaMuxer != null) { "MediaMuxer must be initialized" }
        mediaMuxer!!.start()
    }

    override fun stopStream() {
        mediaMuxer?.stop()
    }

    override fun release() {
        mediaMuxer?.release()
    }
}