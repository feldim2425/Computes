package mods.feldim2425.computes.common.tile

import mods.feldim2425.computes.common.Register
import mods.feldim2425.computes.common.container.ScreenContainer
import mods.feldim2425.computes.common.peripherals.TextBuffer
import mods.feldim2425.computes.network.NetworkRegister
import mods.feldim2425.computes.network.ScreenPacket
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.inventory.container.INamedContainerProvider
import net.minecraft.tileentity.ITickableTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.StringTextComponent
import net.minecraft.world.chunk.Chunk
import net.minecraftforge.fml.network.PacketDistributor

class TileComputer : TileEntity(Register.COMPUTER_TILE.get()), ITickableTileEntity, INamedContainerProvider {

    private var buffer = TextBuffer()
    var down = 0
    var tick = 0

    override fun tick() {
        if (this.world == null || this.world!!.isRemote) {
            return
        }

        if (buffer.isDirty()) {
            buffer.setClear()
            NetworkRegister.getChannel()
                .send(
                    PacketDistributor.TRACKING_CHUNK.with { -> this.world?.getChunk(this.pos) as Chunk? },
                    ScreenPacket(pos, buffer.size, buffer.textBuffer)
                )
        }
        down++
        if (down >= 20) {
            down = 0
            buffer.writeString(String.format("%10d", tick), 0, 0)
            tick++
        }
    }

    override fun createMenu(
        windowId: Int,
        playerInv: PlayerInventory,
        player: PlayerEntity
    ): Container? {
        return ScreenContainer(windowId, this, playerInv)
    }

    override fun getDisplayName(): ITextComponent {
        return StringTextComponent("Computer")
    }

    fun getBuffer(): TextBuffer {
        return buffer
    }
}