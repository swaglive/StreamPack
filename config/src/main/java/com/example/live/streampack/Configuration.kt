/*
 * Copyright (C) 2021 Thibault B.
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
package com.example.live.streampack

import android.media.AudioFormat
import android.media.MediaFormat
import android.util.Range
import android.util.Size

data class Configuration(
    val protocol: LivestreamProtocolType,
    val audio: AudioConfig = AudioConfig(),
    val video: VideoConfig = VideoConfig(),
    val muxer: MuxerConfig = MuxerConfig(),
    val endpoint: EndPointConfig = EndPointConfig(),
) {
    fun setSrtConnection(
        ip: String = "",
        passPhrase: String = "",
        port: Int = 10080,
        streamID: String = "#!::r=live/livestream,m=publish"
    ) {
        endpoint.setSrtConnection(ip, passPhrase, port, streamID)
    }

    fun setRtmpConnection(url: String = "") {
        endpoint.setRtmpConnection(url)
    }

    data class VideoConfig(
        val enable: Boolean = true,
        val encoder: String = MediaFormat.MIMETYPE_VIDEO_AVC,
        val fps: Int = 30,
        val resolution: Size = Size(1280, 720),
        val bitrate: Int = 2000 * 1000,
    )

    data class AudioConfig(
        val enable: Boolean = true,
        val encoder: String = MediaFormat.MIMETYPE_AUDIO_AAC,
        val numberOfChannels: Int = 2, // l 1 for mono, 2 for stereo
        val bitrate: Int = 12800,
        val sampleRate: Int = 48000,
        val byteFormat: Int = AudioFormat.ENCODING_PCM_16BIT,
        val enableEchoCanceler: Boolean = false,
        val enableNoiseSuppressor: Boolean = false,
    )

    data class MuxerConfig(
        val service: String = "StreamPackService",
        val provider: String = "StreamPack Inc",
    )

    class EndPointConfig {
        var srt: SrtConnection = SrtConnection()
            private set
        var rtmp: RtmpConnection = RtmpConnection()
            private set

        fun setSrtConnection(
            ip: String = "",
            passPhrase: String = "",
            port: Int = 10080,
            streamID: String = "#!::r=live/livestream,m=publish"
        ) {
            val new = SrtConnection(ip, port, streamID, passPhrase)
            if (srt != new) {
                srt = new
            }
        }

        fun setRtmpConnection(url: String = "") {
            val new = RtmpConnection(url)
            if (rtmp != new) {
                rtmp = new
            }
        }

        data class SrtConnection(
            val ip: String = "",
            val port: Int = 9998,
            val streamID: String = "",
            val passPhrase: String = "",
            val enableBitrateRegulation: Boolean = false,
            val videoBitrateRange: Range<Int> = Range(300, 5000000)
        )

        data class RtmpConnection(
            val url: String = ""
        )

    }

}