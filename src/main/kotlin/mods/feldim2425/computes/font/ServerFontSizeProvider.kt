package mods.feldim2425.computes.font

import java.io.InputStream
import java.io.InputStreamReader

class ServerFontSizeProvider(fontstream: InputStream) : IFontSizeProvider {

    private var charlen: Array<Byte> = Array(UniFontUtil.UNIFONT_CAP) { 0 }

    init {
        InputStreamReader(fontstream).useLines {
            for (line in it) {
                val parts = line.split(':')
                if (parts.size != 2) {
                    throw Exception("Unifontutil failed to initialize! Error parsing file ${UniFontUtil.UNIFONT_HEX}")
                }

                try {
                    val addr = Integer.parseInt(parts[0], 16)
                    val linevalues = parts[1].trim()
                    when (linevalues.length) {
                        32 -> charlen[addr] = 1
                        64 -> charlen[addr] = 2
                        else -> {
                            val len = linevalues.length
                            throw Exception("Unifontutil failed to initialize! Invalid lenght in file ${UniFontUtil.UNIFONT_HEX}! Requires length 32 or 64! ($addr: $len)")
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
        return charlen[ch].toInt()
    }

}