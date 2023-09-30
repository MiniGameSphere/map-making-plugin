package com.mgs.map_making.api

import org.bukkit.Location
import com.sk89q.worldedit.IncompleteRegionException
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.NullRegion
import com.sk89q.worldedit.regions.Region
import com.sk89q.worldedit.util.formatting.text.TextComponent
import com.sk89q.worldedit.world.World
import com.sk89q.worldedit.world.block.BaseBlock
import com.sk89q.worldedit.world.block.BlockType
import org.bukkit.entity.Player

import com.sk89q.worldedit.entity.Player as WePlayer

var Location.blockPos : BlockPos
    get() {
        return BlockPos(this.blockX, this.blockY, this.blockZ, world)
    }
    set(pos) {
        this.x = pos.x.toDouble()
        this.y = pos.y.toDouble()
        this.z = pos.z.toDouble()
        world ?: { this.world = world }
    }

private fun getPlayerSelectionRegion(player: WePlayer): Region? {
    val sessionManager = WorldEdit.getInstance().sessionManager
    val session = sessionManager.get(player)
    val selectionWorld = session.selectionWorld
    val region : Region
    session.clipboard
    try {
        if (selectionWorld == null) throw IncompleteRegionException()
        region = session.getSelection(selectionWorld)
    } catch (ex: IncompleteRegionException) {
        player.printError(TextComponent.of("Please make a selection first."))
        return null
    }
    return region
}


fun <T: Enum<T>> scanGameBlockRegion(player: Player, map: (BaseBlock) -> T?): MutableList<GameBlock<T>> {
    val wePlayer = BukkitAdapter.adapt(player)
    val region = getPlayerSelectionRegion(wePlayer) ?: return arrayListOf()
    val world = region.world ?: return arrayListOf()

    val offset = wePlayer.blockLocation

    val outArray = arrayListOf<GameBlock<T>>()
    region.forEach {
        val blockType = scanGameBlockFromVector(it, world, map)
        if (blockType != null) {
            outArray.add(GameBlock(blockType, BlockPos(it.x - offset.blockX, it.y - offset.blockY, it.z - offset.blockZ)))
        }
    }
    return outArray
}

private fun <T: Enum<T>> scanGameBlockFromVector(vector: BlockVector3, world: World, map: (BaseBlock) -> T?) : T? {
    val pos = BlockPos(vector.x, vector.y, vector.z)
    val block = pos.getWeBlock(world)
    return map(block)
}

fun <T: Enum<T>> scanGameBlockRegion(player: Player, map: Map<BlockType, T>): MutableList<GameBlock<T>> {
    return scanGameBlockRegion(player){baseBlock -> map[baseBlock.blockType]}
}

fun scanBlockRegion(player: Player): Region {
    val wePlayer = BukkitAdapter.adapt(player)
    return getPlayerSelectionRegion(wePlayer) ?: NullRegion()
}

