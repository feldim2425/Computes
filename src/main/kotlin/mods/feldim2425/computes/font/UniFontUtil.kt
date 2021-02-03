package mods.feldim2425.computes.font

import mods.feldim2425.computes.Computes
import mods.feldim2425.computes.client.font.provider.FontProvider
import net.minecraftforge.api.distmarker.Dist
import thedarkcolour.kotlinforforge.forge.DIST

object UniFontUtil {

    val UNIFONT_HEX = Computes.ID + "/unifont.hex"

    /**
     * Width in pixels of a single width unifont character
     */
    val UNIFONT_WIDTH = 8

    val UNIFONT_CAP = 0x10FFFF

    private var sizeProvider: IFontSizeProvider? = null

    fun initialize() {
        val fontstream = UniFontUtil::class.java.classLoader.getResourceAsStream(UNIFONT_HEX)

        if (fontstream == null) {
            Computes.LOGGER.warn("Unifontutil failed to initialize! Could not find $UNIFONT_HEX")
            return
        }

        var success = true

        fontstream.use {
            try {
                sizeProvider = when (DIST) {
                    Dist.CLIENT -> FontProvider(it)
                    Dist.DEDICATED_SERVER -> ServerFontSizeProvider(it)
                }
            } catch (e: Exception) {
                Computes.LOGGER.warn(e)
                success = false
            }
        }

        if (success) {
            Computes.LOGGER.warn("Unifont initialized")
        } else {
            Computes.LOGGER.warn("Unifont NOT initialized")
        }
    }

    fun getSizeProvider(): IFontSizeProvider? {
        return sizeProvider
    }

    fun getCharacterWidth(ch: Int): Int {
        if (sizeProvider != null) {
            return sizeProvider!!.getCharacterWidth(ch)
        }
        throw IllegalStateException("UniFontUtil not initialized")
    }

    fun getTotalCharWidth(ch: Int): Int {
        return getCharacterWidth(ch) * UNIFONT_WIDTH
    }

    fun getTextWidth(string: String): Int {
        var len = 0
        for (ch in string.codePoints()) {
            len += getCharacterWidth(ch)
        }
        return len
    }

}