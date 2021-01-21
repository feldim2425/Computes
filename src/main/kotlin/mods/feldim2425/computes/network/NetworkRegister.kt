package mods.feldim2425.computes.network

import mods.feldim2425.computes.Computes
import net.minecraft.network.PacketBuffer
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.network.NetworkRegistry
import net.minecraftforge.fml.network.simple.SimpleChannel

object NetworkRegister {

    private val CHANNEL_NAME = ResourceLocation(Computes.ID, "network")
    private const val CHANNEL_VERSION = Computes.ID + ":1"

    private lateinit var channel: SimpleChannel

    fun init() {
        channel = NetworkRegistry.ChannelBuilder.named(CHANNEL_NAME)
            .serverAcceptedVersions { _ -> true }
            .clientAcceptedVersions { _ -> true }
            .networkProtocolVersion { -> CHANNEL_VERSION }
            .simpleChannel()

        var index = 0

        registerPacket(index++, ScreenPacket::class.java, ::ScreenPacket)
        registerPacket(index++, KeyboardPacket::class.java, ::KeyboardPacket)
    }

    fun getChannel(): SimpleChannel {
        return channel
    }


    private fun <T : IPacket> registerPacket(index: Int, clazz: Class<T>, factory: (PacketBuffer) -> T) {
        channel.messageBuilder(clazz, index)
            .encoder { msg, buffer -> msg.write(buffer) }
            .decoder(factory)
            .consumer(fun(msg, supplier): Boolean { return msg.handle(supplier) })
            .add()
    }

}