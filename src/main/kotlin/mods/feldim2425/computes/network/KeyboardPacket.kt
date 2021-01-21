package mods.feldim2425.computes.network

import net.minecraft.network.PacketBuffer
import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.network.NetworkDirection
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

class KeyboardPacket private constructor() : IPacket {

    private lateinit var screenPos: BlockPos
    private var modifier: Int = 0
    private var keyCode: Int = 0
    private var character: Char = ' '
    private var pressed: Boolean = false
    private var rawCode: Boolean = false

    constructor(screenPos: BlockPos, modifier: Int, keyCode: Int, pressed: Boolean) : this() {
        this.rawCode = true
        this.screenPos = screenPos
        this.keyCode = keyCode
        this.modifier = modifier
        this.pressed = pressed
    }

    constructor(screenPos: BlockPos, modifier: Int, character: Char, pressed: Boolean) : this() {
        this.rawCode = false
        this.screenPos = screenPos
        this.character = character
        this.modifier = modifier
        this.pressed = pressed
    }

    constructor(buffer: PacketBuffer) : this() {
        screenPos = buffer.readBlockPos()
        modifier = buffer.readInt()
        pressed = buffer.readBoolean()
        rawCode = buffer.readBoolean()
        if (rawCode) {
            keyCode = buffer.readInt()
        } else {
            character = buffer.readChar()
        }
    }

    override fun write(buffer: PacketBuffer) {
        buffer.writeBlockPos(screenPos)
        buffer.writeInt(modifier)
        buffer.writeBoolean(pressed)
        buffer.writeBoolean(rawCode)
        if (rawCode) {
            buffer.writeInt(keyCode)
        } else {
            buffer.writeChar(character.toInt())
        }
    }

    override fun handle(ctx: Supplier<NetworkEvent.Context>): Boolean {
        if (ctx.get().direction != NetworkDirection.PLAY_TO_SERVER) {
            return false
        }

        ctx.get().enqueueWork {
            System.out.println(this.character + ":" + this.keyCode + ":" + this.rawCode + ":" + this.pressed + ":" + this.modifier)
        }

        return true
    }


}