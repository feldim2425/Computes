package mods.feldim2425.computes.common.blocks

import mods.feldim2425.computes.common.tile.TileComputer
import net.minecraft.block.AbstractBlock
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.ContainerBlock
import net.minecraft.block.material.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.ServerPlayerEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ActionResultType
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.BlockRayTraceResult
import net.minecraft.world.IBlockReader
import net.minecraft.world.World
import net.minecraftforge.fml.network.NetworkHooks

class ComputerBlock : ContainerBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL)) {

    override fun getRenderShape(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun use(
        state: BlockState,
        worldIn: World,
        pos: BlockPos,
        player: PlayerEntity,
        handIn: Hand,
        hit: BlockRayTraceResult
    ): ActionResultType {
        if (worldIn.isClientSide) {
            return ActionResultType.SUCCESS
        }

        val tile = worldIn.getBlockEntity(pos)
        if (tile !is TileComputer || player !is ServerPlayerEntity) {
            return ActionResultType.FAIL
        }

        NetworkHooks.openGui(player, tile, pos)
        return ActionResultType.SUCCESS
    }

    override fun newBlockEntity(worldIn: IBlockReader): TileEntity? {
        return TileComputer()
    }
}