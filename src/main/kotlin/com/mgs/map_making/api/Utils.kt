package com.mgs.map_making.api

import com.sk89q.worldedit.IncompleteRegionException
import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.regions.Region
import com.sk89q.worldedit.util.formatting.text.TextComponent
import com.sk89q.worldedit.world.World
import org.bukkit.Material
import org.bukkit.entity.Player


fun <T: Enum<T>> scanRegion(player: Player, map: Map<Material, Enum<T>>) : MutableList<GameBlock<T>> {
    val wePlayer = BukkitAdapter.adapt(player)
    val sessionManager = WorldEdit.getInstance().sessionManager
    val session = sessionManager.get(wePlayer)
    val selectionWorld = session.selectionWorld
    val region : Region
    try {
        if (selectionWorld == null) throw IncompleteRegionException()
        region = session.getSelection(selectionWorld)
    } catch (ex: IncompleteRegionException) {
        wePlayer.printError(TextComponent.of("Please make a region selection first."))
        return arrayListOf()
    }
    val outArray = arrayListOf<GameBlock<T>>()
    region.forEach {
        val blockType = scanBlockFromVector(it, selectionWorld, map)
        if (blockType != null) {
            outArray.add(GameBlock(blockType, BlockPos(it.x, it.y, it.z)))
        }
    }
    return outArray
}

private fun <T: Enum<T>> scanBlockFromVector(vector: BlockVector3, world: World, map: Map<Material, Enum<T>>) : Enum<T>? {
    val bukkitWorld = BukkitAdapter.adapt(world)
    val pos = BlockPos(vector.x, vector.y, vector.z, bukkitWorld)
    val block = pos.getBlock().type
    return map[block]
}

