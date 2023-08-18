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

    fun getBlock(world: World) : Block {
        return world.getBlockAt(x, y, z)
    }

    fun getBlock() : Block {
        val world = world
        world ?: throw Error("World not defined")
        return world.getBlockAt(x, y, z)
    }

}
