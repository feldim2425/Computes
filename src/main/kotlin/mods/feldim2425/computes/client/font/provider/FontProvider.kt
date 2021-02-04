package mods.feldim2425.computes.client.font.provider

import mods.feldim2425.computes.api.font.ITermGlyphProvider
import mods.feldim2425.computes.font.IFontSizeProvider
import mods.feldim2425.computes.font.UniFontUtil
import net.minecraft.util.Tuple
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.experimental.and

class FontProvider(fontstream: InputStream) : IFontSizeProvider, ITermGlyphProvider {

    private val CHAR_SIZE = Tuple(8, 16)

    private var charlen: Array<Array<Byte>?> = Array(UniFontUtil.UNIFONT_CAP) { null }

    init {
        InputStreamReader(fontstream).useLines {
            for (line in it) {
                val parts = line.split(':')
                if (parts.size != 2) {
                    throw Exception("Unifontutil failed to initialize! Error parsing file ${UniFontUtil.UNIFONT_HEX}")
                }

                try {
                    val addr = Integer.parseInt(parts[0], 16)
                    var intlen = 0
                    val lineval = parts[1].trim()
                    when (lineval.length) {
                        32 -> intlen = 1
                        64 -> intlen = 2
                        else -> {
                            throw Exception("Unifontutil failed to initialize! Invalid lenght in file ${UniFontUtil.UNIFONT_HEX}! Requires length 32 or 64!")
                        }
                    }

                    charlen[addr] = Array(intlen * 16) { 0 }
                    for (i in 0 until 16) {
                        for (j in 0 until intlen) {
                            val seq = (i * intlen) + j
                            val idx = (i * intlen) + (intlen - j - 1)
                            charlen[addr]!![idx] = Integer.parseInt(
                                lineval.subSequence(
                                    seq * 2,
                                    (seq + 1) * 2
                                ) as String?, 16
                            ).toByte()
                        }
                    }
                } catch (e: NumberFormatException) {
                    throw Exception("Unifontutil failed to initialize! Error parsing file ${UniFontUtil.UNIFONT_HEX}")
                }
            }
        }
    }

    override fun getCharacterWidth(ch: Int): Int {
        if (ch >= charlen.size) {
            return 0
        }

        val cha = charlen[ch]
        if (cha != null) {
            return cha.size / 16
        }
        return 0
    }

    override fun getImage(ch: Int): ByteBuffer {
        val cha = charlen[ch] ?: return ByteBuffer.allocate(16 * 4)

        //Use allocateDirect here: https://stackoverflow.com/questions/55937282/lwjgl3-exception-access-violation-while-calling-glteximage2d
        val buffer = ByteBuffer.allocateDirect(cha.size * 8 * 4)
        buffer.order(ByteOrder.nativeOrder())
        for (i in 0 until cha.size) {
            for (j in 0 until 8) {
                if ((cha[i] and ((1 shl j).toByte())) != 0.toByte()) {
                    buffer.put(0xFF.toByte())
                    buffer.put(0xFF.toByte())
                    buffer.put(0xFF.toByte())
                    buffer.put(0xFF.toByte())
                } else {
                    buffer.put(0.toByte())
                    buffer.put(0.toByte())
                    buffer.put(0.toByte())
                    buffer.put(0.toByte())
                }
            }
        }
        return buffer.flip()
    }

    override fun getSingleChSize(): Tuple<Int, Int> {
        return CHAR_SIZE
    }

}

