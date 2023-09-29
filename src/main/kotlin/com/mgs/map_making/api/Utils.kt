package com.mgs.map_making.api

import org.bukkit.Location

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