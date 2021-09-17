package mods.feldim2425.computes.common

import mods.feldim2425.computes.Computes
import mods.feldim2425.computes.common.blocks.ComputerBlock
import mods.feldim2425.computes.common.container.ScreenContainer
import mods.feldim2425.computes.common.tile.TileComputer
import net.minecraft.inventory.container.ContainerType
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.tileentity.TileEntityType
import net.minecraftforge.common.extensions.IForgeContainerType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.MOD_BUS

object Register {

    private val BLOCKS_REG = DeferredRegister.create(ForgeRegistries.BLOCKS, Computes.ID)
    private val ITEM_REG = DeferredRegister.create(ForgeRegistries.ITEMS, Computes.ID)
    private val CONTAINERS_REG: DeferredRegister<ContainerType<*>> =
        DeferredRegister.create(ForgeRegistries.CONTAINERS, Computes.ID)
    private val TILEENTITIY_REG = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Computes.ID)

    val COMPUTER_BLOCK = BLOCKS_REG.register("computer", ::ComputerBlock)

    val COMPUTER_BLOCKITEM = ITEM_REG.register("computer") {
        BlockItem(
            COMPUTER_BLOCK.get(), Item.Properties().tab(
                ItemGroup.TAB_DECORATIONS
            )
        )
    }

    val SCREEN_CONTAINER = CONTAINERS_REG.register("screen_container") { IForgeContainerType.create(::ScreenContainer) }

    val COMPUTER_TILE = TILEENTITIY_REG.register("computer") {
        TileEntityType.Builder.of(::TileComputer, COMPUTER_BLOCK.get()).build(null)
    }

    fun init() {
        BLOCKS_REG.register(MOD_BUS)
        ITEM_REG.register(MOD_BUS)
        CONTAINERS_REG.register(MOD_BUS)
        TILEENTITIY_REG.register(MOD_BUS)

        /* if (DIST.isClient)
             MOD_BUS.addListener(::clientInit)*/
    }

    //@OnlyIn(Dist.CLIENT)
    
}