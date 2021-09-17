package mods.feldim2425.computes

import mods.feldim2425.computes.client.gui.ScreenScreen
import mods.feldim2425.computes.common.Register
import mods.feldim2425.computes.font.UniFontUtil
import mods.feldim2425.computes.network.NetworkRegister
import net.minecraft.client.gui.ScreenManager
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(Computes.ID)
object Computes {
    const val ID: String = "computes"

    // the logger for our mod
    val LOGGER: Logger = LogManager.getLogger()

    init {
        // Register the KDeferredRegister to the mod-specific event bus

        Register.init()
        NetworkRegister.init()

        UniFontUtil.initialize()

        // usage of the KotlinEventBus
        MOD_BUS.addListener(::onClientSetup)
        FORGE_BUS.addListener(::onServerAboutToStart)

        MOD_BUS.addListener(::clientInit)
    }

    /**
     * This is used for initializing client specific
     * things such as renderers and keymaps
     * Fired on the mod specific event bus.
     */
    private fun onClientSetup(event: FMLClientSetupEvent) {
        LOGGER.log(Level.INFO, "Initializing client...")
    }

    /**
     * Fired on the global Forge bus.
     */
    private fun onServerAboutToStart(event: FMLServerAboutToStartEvent) {
        LOGGER.log(Level.INFO, "Server starting...")
    }

    private fun clientInit(event: FMLClientSetupEvent) {
        ScreenManager.register(Register.SCREEN_CONTAINER.get(), ::ScreenScreen)
    }
}