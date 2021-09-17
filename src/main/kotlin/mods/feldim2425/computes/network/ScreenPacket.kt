package mods.feldim2425.computes.network

import mods.feldim2425.computes.client.gui.ScreenScreen
import mods.feldim2425.computes.common.tile.TileComputer
import net.minecraft.client.Minecraft
import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

class ScreenPacket private constructor() : IPacket {

    private lateinit var screenPos: BlockPos
    private lateinit var size: Pair<Int, Int>
    private lateinit var chars: MutableList<Char>

    constructor(screenPos: BlockPos, size: Pair<Int, Int>, chars: Collection<Char>) : this() {
        this.screenPos = screenPos
        this.size = size
        this.chars = ArrayList(chars)
    }

    constructor(buffer: PacketBuffer) : this() {
        screenPos = buffer.readBlockPos()
        val width = buffer.readInt()
        val height = buffer.readInt()
        size = Pair(width, height)
        chars = ArrayList(width * height)
        for (i in 0 until width * height) {
            chars.add(buffer.readChar())
        }
    }

    override fun write(buffer: PacketBuffer) {
        buffer.writeBlockPos(this.screenPos)
        buffer.writeInt(size.first)
        buffer.writeInt(size.second)
        for (ch in chars) {
            buffer.writeChar(ch.toInt())
        }
    }

    override fun handle(ctx: Supplier<NetworkEvent.Context>): Boolean {
        if (ctx.get().direction != NetworkDirection.PLAY_TO_CLIENT) {
            return false
        }

        ctx.get().enqueueWork {
            val tile = Minecraft.getInstance().player?.clientLevel?.getBlockEntity(this.screenPos)
            if (tile !is TileComputer) {
                return@enqueueWork
            }
            tile.getBuffer().update(size, chars)

            val screen = Minecraft.getInstance().screen
            if (screen is ScreenScreen && screen.menu.tileentity == tile) {
                screen.updateScreen()
            }
        }

        return true
    }
}