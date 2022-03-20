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
package io.github.thibaultbee.streampack.internal.muxers.flv.packet

import io.github.thibaultbee.streampack.internal.utils.av.video.ChromaFormat
import io.github.thibaultbee.streampack.internal.utils.put
import io.github.thibaultbee.streampack.internal.utils.putShort
import io.github.thibaultbee.streampack.internal.utils.av.video.getStartCodeSize
import io.github.thibaultbee.streampack.internal.utils.av.video.removeStartCode
import java.nio.ByteBuffer

class HEVCDecoderConfigurationRecord(sps: ByteBuffer, pps: ByteBuffer, vps: ByteBuffer) {
    private val spsNoStartCode = sps.removeStartCode()
    private val ppsNoStartCode = pps.removeStartCode()
    private val vpsNoStartCode = vps.removeStartCode()

    fun write(buffer: ByteBuffer) {
        buffer.put(0x01) // configurationVersion

        // Get profile_tier_level from sps or vps
        buffer.putInt(spsNoStartCode.getInt(1))
        buffer.putInt(spsNoStartCode.getInt(5))
        buffer.putInt(spsNoStartCode.getInt(9))

        val minSpatialSegmentationIdc = 0 // TODO parse VUI
        buffer.putShort((0b1111 shl 12) or (minSpatialSegmentationIdc)) // min_spatial_segmentation_idc 12 bits
        val parallelismType = 0 // 0 = Unknown
        buffer.put((0b111111 shl 2) or (parallelismType)) // parallelismType 2 bits
        val chromaFormat = ChromaFormat.YUV420.value
        buffer.put((0b111111 shl 2) or (chromaFormat)) // chromaFormat 2 bits
        val bitDepthLumaMinus8 = 0 // TODO
        buffer.put((0b11111 shl 3) or (bitDepthLumaMinus8)) // bitDepthLumaMinus8 3 bits
        val bitDepthChromaMinus8 = 0 // TODO
        buffer.put((0b11111 shl 3) or (bitDepthChromaMinus8)) // bitDepthChromaMinus8 3 bits

        buffer.putShort(30) // avgFrameRate
        val numTemporalLayers = 0 // 0 = Unknown
        val temporalIdNested = 0 // TODO
        buffer.put((1 shl 6) or (numTemporalLayers shl 3) or (temporalIdNested shl 2) or 0b11) // constantFrameRate 2 bits = 1 for stable / numTemporalLayers 3 bits /  temporalIdNested 1 bit / lengthSizeMinusOne 2 bits

        buffer.put(3) // numOfArrays
        writeArray(buffer, spsNoStartCode, NalUnitType.SPS)
        writeArray(buffer, ppsNoStartCode, NalUnitType.PPS)
        writeArray(buffer, vpsNoStartCode, NalUnitType.VPS)
    }

    private fun writeArray(buffer: ByteBuffer, nalUnit: ByteBuffer, nalUnitType: NalUnitType) {
        buffer.put(nalUnitType.value) // array_completeness + reserved 1bit + naluType 6 bytes
        buffer.putShort(1) // numNalus
        buffer.putShort(nalUnit.remaining().toShort()) // nalUnitLength
        buffer.put(nalUnit)
        nalUnit.rewind()
    }

    companion object {
        private const val HEVC_DECODER_CONFIGURATION_RECORD_SIZE = 23

        fun getSize(sps: ByteBuffer, pps: ByteBuffer, vps: ByteBuffer): Int {
            return HEVC_DECODER_CONFIGURATION_RECORD_SIZE + 15 + sps.remaining() - sps.getStartCodeSize() + pps.remaining() - pps.getStartCodeSize() + vps.remaining() - vps.getStartCodeSize()
        }
    }
}

enum class NalUnitType(val value: Byte) {
    VPS(32),
    SPS(33),
    PPS(34)
}