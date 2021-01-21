package mods.feldim2425.computes.network

import net.minecraft.network.PacketBuffer
import net.minecraftforge.fml.network.NetworkEvent
import java.util.function.Supplier

interface IPacket {

    fun write(buffer: PacketBuffer)
    fun handle(ctx: Supplier<NetworkEvent.Context>): Boolean

}