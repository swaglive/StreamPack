package io.github.thibaultbee.streampack.internal.muxers.mediamuxer

import android.media.MediaFormat
import android.media.MediaMuxer
import io.github.thibaultbee.streampack.data.AudioConfig
import io.github.thibaultbee.streampack.data.VideoConfig
import io.github.thibaultbee.streampack.utils.FakeFrames
import org.junit.Test
import java.io.File

class MediaMuxerEndpointTest {
    private val mediaMuxerEndpoint =
        MediaMuxerEndpoint(MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4).apply {
            file = File.createTempFile("test", "mp4")
        }

    @Test
    fun writeAACAudioTrackTest() {
        val config = AudioConfig(mimeType = MediaFormat.MIMETYPE_AUDIO_AAC)
        val indexMap = mediaMuxerEndpoint.addStreams(listOf(config))
        mediaMuxerEndpoint.startStream()
        mediaMuxerEndpoint.encode(
            FakeFrames.createKeyFrame(MediaFormat.MIMETYPE_AUDIO_AAC),
            indexMap[config]!!
        )
    }

    @Test
    fun writeAVCVideoTrackTest() {
        val config = VideoConfig(mimeType = MediaFormat.MIMETYPE_VIDEO_AVC)
        val indexMap = mediaMuxerEndpoint.addStreams(listOf(config))
        mediaMuxerEndpoint.startStream()
        mediaMuxerEndpoint.encode(
            FakeFrames.createKeyFrame(MediaFormat.MIMETYPE_VIDEO_AVC),
            indexMap[config]!!
        )
    }

    @Test
    fun writeHEVCVideoTrackTest() {
        val config = VideoConfig(mimeType = MediaFormat.MIMETYPE_VIDEO_HEVC)
        val indexMap = mediaMuxerEndpoint.addStreams(listOf(config))
        mediaMuxerEndpoint.startStream()
        mediaMuxerEndpoint.encode(
            FakeFrames.createKeyFrame(MediaFormat.MIMETYPE_VIDEO_HEVC),
            indexMap[config]!!
        )
    }
}