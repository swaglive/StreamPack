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

import android.media.MediaFormat
import android.os.Build
import io.github.thibaultbee.streampack.internal.muxers.IAudioMuxerHelper
import io.github.thibaultbee.streampack.internal.muxers.IMuxerHelper
import io.github.thibaultbee.streampack.internal.muxers.IVideoMuxerHelper

class MediaMuxerHelper : IMuxerHelper {
    override val audio = AudioMediaMuxerHelper()
    override val video = VideoMediaMuxerHelper()
}

class AudioMediaMuxerHelper : IAudioMuxerHelper {
    /**
     * Get MediaMuxer supported audio encoders list
     */
    override val supportedEncoders = listOf(MediaFormat.MIMETYPE_AUDIO_AAC)

    override fun getSupportedSampleRates(): List<Int>? = null

    override fun getSupportedByteFormats(): List<Int>? = null
}

class VideoMediaMuxerHelper : IVideoMuxerHelper {
    /**
     * Get MediaMuxer supported video encoders list
     */
    override val supportedEncoders: List<String>
        get() {
            val list = mutableListOf(MediaFormat.MIMETYPE_VIDEO_AVC)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                list.add(MediaFormat.MIMETYPE_VIDEO_HEVC)
            }
            return list
        }
}
