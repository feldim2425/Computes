package mods.feldim2425.computes.client.gui

import com.mojang.blaze3d.matrix.MatrixStack
import com.mojang.blaze3d.systems.RenderSystem
import mods.feldim2425.computes.client.gui.renderer.TermTextRenderer
import mods.feldim2425.computes.common.container.ScreenContainer
import mods.feldim2425.computes.network.KeyboardPacket
import mods.feldim2425.computes.network.NetworkRegister
import mods.feldim2425.computes.util.ClientKeyModifier
import net.minecraft.client.gui.screen.inventory.ContainerScreen
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldVertexBufferUploader
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.util.text.ITextComponent
import org.lwjgl.glfw.GLFW
import org.lwjgl.opengl.GL11


class ScreenScreen(screenContainer: ScreenContainer, inv: PlayerInventory, titleIn: ITextComponent) :
    ContainerScreen<ScreenContainer>(
        screenContainer, inv,
        titleIn
    ) {

    var renderer: TermTextRenderer? = null


    init {
        this.width = this.menu.tileentity.getBuffer().size.first * 6 + 4
        this.height = this.menu.tileentity.getBuffer().size.second * 10 + 4

        renderer = TermTextRenderer(this.menu.tileentity.getBuffer())
    }

    fun updateScreen() {


    }

    override fun renderBg(stack: MatrixStack, partialTicks: Float, x: Int, y: Int) {
        val i = (width - xSize) / 2
        val j = (height - ySize) / 2

        val matrix = stack.last().pose()
        val minX = i
        val minY = j
        val maxX = i + xSize
        val maxY = j + ySize
        GL11.glColor4f(0F, 0F, 0F, 1F)


        val bufferbuilder = Tessellator.getInstance().builder
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        bufferbuilder.vertex(matrix, minX.toFloat(), maxY.toFloat(), 0.0f).endVertex()
        bufferbuilder.vertex(matrix, maxX.toFloat(), maxY.toFloat(), 0.0f).endVertex()
        bufferbuilder.vertex(matrix, maxX.toFloat(), minY.toFloat(), 0.0f).endVertex()
        bufferbuilder.vertex(matrix, minX.toFloat(), minY.toFloat(), 0.0f).endVertex()
        bufferbuilder.end()
        WorldVertexBufferUploader.end(bufferbuilder)
        RenderSystem.enableTexture()
        GL11.glColor4f(1F, 1F, 1F, 1F)
    }

    override fun renderLabels(matrixStack: MatrixStack, x: Int, y: Int) {
        super.renderLabels(matrixStack, x, y)
        var xT = 0
        var yT = 0
        /*for (ch in this.container.tileentity.getBuffer().textBuffer) {
            this.font.drawString(
                matrixStack,
                ch.toString(),
                (xT.toFloat() * 6) + 3,
                (yT.toFloat() * 9) + 2,
                0xffffff
            )
            xT++
            if (xT >= this.container.tileentity.getBuffer().size.first) {
                xT = 0
                yT++
            }
        }*/

        renderer!!.render(matrixStack, 0, 0, xSize, ySize)

    }


    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == 256 && shouldCloseOnEsc()) {
            onClose()
            return true
        }
        var pkgMod = 0
        if (modifiers and GLFW.GLFW_MOD_CONTROL != 0) {
            pkgMod = pkgMod or ClientKeyModifier.CTRL.bitmask
        }
        if (modifiers and GLFW.GLFW_MOD_ALT != 0) {
            pkgMod = pkgMod or ClientKeyModifier.ALT.bitmask
        }
        if (modifiers and GLFW.GLFW_MOD_SHIFT != 0) {
            pkgMod = pkgMod or ClientKeyModifier.SHIFT.bitmask
        }

        NetworkRegister.getChannel()
            .sendToServer(KeyboardPacket(this.menu.tileentity.blockPos, pkgMod, keyCode, true))
        return true
    }

    override fun charTyped(codePoint: Char, modifiers: Int): Boolean {
        var pkgMod = 0
        if (modifiers and GLFW.GLFW_MOD_CONTROL != 0) {
            pkgMod = pkgMod or ClientKeyModifier.CTRL.bitmask
        }
        if (modifiers and GLFW.GLFW_MOD_ALT != 0) {
            pkgMod = pkgMod or ClientKeyModifier.ALT.bitmask
        }
        if (modifiers and GLFW.GLFW_MOD_SHIFT != 0) {
            pkgMod = pkgMod or ClientKeyModifier.SHIFT.bitmask
        }

        NetworkRegister.getChannel()
            .sendToServer(KeyboardPacket(this.menu.tileentity.blockPos, pkgMod, codePoint, true))
        return super.charTyped(codePoint, modifiers)
    }

    override fun keyReleased(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        var pkgMod = 0
        if (modifiers and GLFW.GLFW_MOD_CONTROL != 0) {
            pkgMod = pkgMod or ClientKeyModifier.CTRL.bitmask
        }
        if (modifiers and GLFW.GLFW_MOD_ALT != 0) {
            pkgMod = pkgMod or ClientKeyModifier.ALT.bitmask
        }
        if (modifiers and GLFW.GLFW_MOD_SHIFT != 0) {
            pkgMod = pkgMod or ClientKeyModifier.SHIFT.bitmask
        }
        NetworkRegister.getChannel()
            .sendToServer(KeyboardPacket(this.menu.tileentity.blockPos, pkgMod, keyCode, false))
        return super.keyReleased(keyCode, scanCode, modifiers)
    }

}