package com.mgs.map_making.api

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.world.block.BaseBlock
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Block

import com.sk89q.worldedit.world.World as WeWorld

data class BlockPos(
    var x: Int,
    var y: Int,
    var z: Int,
    var world: World? = null
) {
    var location: Location
        get() {
            return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
        }
        set(location) {
            x = location.blockX
            y = location.blockY
            z = location.blockZ
        }

    var weBlockVector: BlockVector3
        get() {
            return BukkitAdapter.adapt(location).toVector().toBlockPoint()
        }
        set(blockVec3) {
            x = blockVec3.blockX
            y = blockVec3.blockY
            z = blockVec3.blockZ
        }

    fun getBlock(world: World? = this.world): Block {
        world ?: throw Error("World not defined")
        return world.getBlockAt(x, y, z)
    }

    fun getWeBlock(world: World? = this.world): BaseBlock {
        world ?: throw Error("World not defined")
        return BukkitAdapter.adapt(world).getFullBlock(weBlockVector)
    }
    fun getWeBlock(world: WeWorld): BaseBlock {
        return world.getFullBlock(weBlockVector)
    }
}
