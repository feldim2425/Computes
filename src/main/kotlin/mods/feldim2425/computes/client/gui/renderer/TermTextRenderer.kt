package mods.feldim2425.computes.client.gui.renderer

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import mods.feldim2425.computes.api.font.ITermGlyphProvider
import mods.feldim2425.computes.common.peripherals.TextBuffer
import mods.feldim2425.computes.font.UniFontUtil
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldVertexBufferUploader
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11

class TermTextRenderer(var buffer: TextBuffer) {

    var texId: Int? = null
    var dirty = true
    var destroyed = false
    var imageSize = Pair(0, 0)
    var imageBuffer: Array<Array<Byte>>? = null
    var tex = UniFontUtil.getSizeProvider() as ITermGlyphProvider

    fun setDirty() {
        dirty = true;
    }

    fun destroy() {
        destroyed = true
        if (texId != null) {
            GL11.glDeleteTextures(texId!!)
        }
        imageBuffer = null
    }

    private fun makeImage() {
        /*var ibuf = imageBuffer
        var update = false
        if (ibuf == null || imageSize != buffer.size) {
            ibuf = Array(buffer.size.first) { Array(buffer.size.second * 16) { 0 } }
            update = true
        }

        for (line in 0 until buffer.size.second) {
            val cbuffer = CharArray(buffer.size.first)
            for (ch in 0 until buffer.size.first) {
                cbuffer[ch] = buffer.textBuffer[ch]
            }
            val str = String(cbuffer, "utf-16")
        }


        if (update) {
            imageSize = buffer.size
            imageBuffer = ibuf
        }*/
    }

    private fun updateTexture() {
        val texIdl = texId;
        texId = null;
        if (texIdl != null) {
            GL11.glDeleteTextures(texIdl)
        }
        val size = tex.singleChSize
        val texIdN = GL11.glGenTextures()
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texIdN)

        val relsize = UniFontUtil.getCharacterWidth(0x1f43c)
        val img = tex.getImage(0x1f43c)
        GL11.glTexImage2D(
            GL11.GL_TEXTURE_2D,
            0,
            GL11.GL_RGBA8,
            relsize * size.a,
            size.b,
            0,
            GL11.GL_RGBA,
            GL11.GL_UNSIGNED_BYTE,
            img
        )

        texId = texIdN
        dirty = false
    }


    /*protected fun finalize() {
        if (texId != null) {
            GL11.glDeleteTextures(texId!!)
        }
    }*/

    fun render(state: MatrixStack, x: Int, y: Int, w: Int, h: Int) {
        if (destroyed) {
            return
        }

        RenderSystem.enableTexture()
        if (dirty || texId == null) {
            updateTexture()
        }

        if (texId != null) {
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            RenderSystem.disableCull()
            val matrix = state.last().pose()
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId!!);

            val bufferbuilder = Tessellator.getInstance().builder
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            bufferbuilder.vertex(matrix, x.toFloat(), y.toFloat(), 0.0f).uv(0.0f, 0.0f).endVertex()
            bufferbuilder.vertex(matrix, (x + w).toFloat(), y.toFloat(), 0.0f).uv(1.0f, 0.0f).endVertex()
            bufferbuilder.vertex(matrix, (x + w).toFloat(), (y + h).toFloat(), 0.0f).uv(1.0f, 1.0f).endVertex()
            bufferbuilder.vertex(matrix, x.toFloat(), (y + h).toFloat(), 0.0f).uv(0.0f, 1.0f).endVertex()
            bufferbuilder.end()
            WorldVertexBufferUploader.end(bufferbuilder)
        }
    }

}