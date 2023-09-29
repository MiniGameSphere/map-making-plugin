package com.mgs.map_making.api

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block

data class BlockPos(
    var x : Int,
    var y : Int,
    var z : Int,
    var world: World? = null
) {
    fun getBlock(world: World) : Block {
        return world.getBlockAt(x, y, z)
    }

    fun getBlock() : Block {
        val world = world
        world ?: throw Error("World not defined")
        return world.getBlockAt(x, y, z)
    }

}
