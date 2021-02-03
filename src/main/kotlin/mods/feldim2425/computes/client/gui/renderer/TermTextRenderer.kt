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
    var dirty = true;
    var tex = UniFontUtil.getSizeProvider() as ITermGlyphProvider

    fun setDirty() {
        dirty = true;
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

        val relsize = UniFontUtil.getCharacterWidth(0x2661)
        val img = tex.getImage(0x2661)
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
        RenderSystem.enableTexture()
        if (dirty || texId == null) {
            updateTexture()
        }

        if (texId != null) {


            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            RenderSystem.disableCull()
            val matrix = state.last.matrix
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId!!);

            val bufferbuilder = Tessellator.getInstance().buffer
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
            bufferbuilder.pos(matrix, x.toFloat(), y.toFloat(), 0.0f).tex(0.0f, 0.0f).endVertex()
            bufferbuilder.pos(matrix, (x + w).toFloat(), y.toFloat(), 0.0f).tex(1.0f, 0.0f).endVertex()
            bufferbuilder.pos(matrix, (x + w).toFloat(), (y + h).toFloat(), 0.0f).tex(1.0f, 1.0f).endVertex()
            bufferbuilder.pos(matrix, x.toFloat(), (y + h).toFloat(), 0.0f).tex(0.0f, 1.0f).endVertex()
            bufferbuilder.finishDrawing()
            WorldVertexBufferUploader.draw(bufferbuilder)
        }
    }

}