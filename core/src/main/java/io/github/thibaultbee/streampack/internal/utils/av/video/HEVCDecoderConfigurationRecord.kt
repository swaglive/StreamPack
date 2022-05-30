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
package io.github.thibaultbee.streampack.internal.utils.av.video

import java.nio.ByteBuffer

class HEVCDecoderConfigurationRecord(
    sps: List<ByteBuffer>,
    pps: List<ByteBuffer>,
    vps: List<ByteBuffer>
) {
    private val spsNoStartCode = sps.map { it.removeStartCode() }
    private val ppsNoStartCode = pps.map { it.removeStartCode() }
    private val vpsNoStartCode = vps.map { it.removeStartCode() }

    fun write(buffer: ByteBuffer) {
        buffer.put(0x01) // configurationVersion
        throw NotImplementedError("HEVC is not supported yet")
    }

    companion object {
        private const val HEVC_DECODER_CONFIGURATION_RECORD_SIZE = 11

        fun fromSPSandPPSandVPS(
            sps: ByteBuffer,
            pps: ByteBuffer,
            vps: ByteBuffer
        ) = fromSPSandPPSandVPS(
            listOf(sps),
            listOf(pps),
            listOf(vps)
        )

        fun fromSPSandPPSandVPS(
            sps: List<ByteBuffer>,
            pps: List<ByteBuffer>,
            vps: List<ByteBuffer>
        ): HEVCDecoderConfigurationRecord {
            return HEVCDecoderConfigurationRecord(sps, pps, vps)
        }

        fun getSize(
            sps: ByteBuffer,
            pps: ByteBuffer,
            vps: ByteBuffer
        ) = getSize(
            listOf(sps),
            listOf(pps),
            listOf(vps)
        )

        fun getSize(
            sps: List<ByteBuffer>,
            pps: List<ByteBuffer>,
            vps: List<ByteBuffer>
        ): Int {
            var size =
                HEVC_DECODER_CONFIGURATION_RECORD_SIZE
            sps.forEach {
                size += it.remaining() - it.getStartCodeSize()
            }
            pps.forEach {
                size += it.remaining() - it.getStartCodeSize()
            }
            vps.forEach {
                size += it.remaining() - it.getStartCodeSize()
            }

            val profileIdc = sps[0].get(1).toInt()
            if ((profileIdc == 100) || (profileIdc == 110) || (profileIdc == 122) || (profileIdc == 144)) {
                size += 4
            }
            return size
        }
    }

    enum class ColorFormat(val value: Int) {
        YUV400(0),
        YUV420(1),
        YUV422(2),
        YUV444(3)
    }
}