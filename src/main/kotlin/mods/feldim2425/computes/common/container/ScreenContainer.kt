package mods.feldim2425.computes.common.container

import mods.feldim2425.computes.common.Register
import mods.feldim2425.computes.common.tile.TileComputer
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.container.Container
import net.minecraft.network.PacketBuffer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.IWorldPosCallable

class ScreenContainer(windowId: Int, entity: TileEntity?, inv: PlayerInventory) :
    Container(Register.SCREEN_CONTAINER.get(), windowId) {

    val inventory = inv
    val tileentity: TileComputer = entity as TileComputer

    constructor(windowId: Int, inv: PlayerInventory, data: PacketBuffer) : this(
        windowId,
        inv.player.level.getBlockEntity(data.readBlockPos()),
        inv
    )

    override fun stillValid(playerIn: PlayerEntity): Boolean {
        return Container.stillValid(
            IWorldPosCallable.create(tileentity.level!!, tileentity.blockPos),
            this.inventory.player,
            Register.COMPUTER_BLOCK.get()
        )
    }
}