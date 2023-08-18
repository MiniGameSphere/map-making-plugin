package com.mgs.map_making.api

data class GameBlock<T : Enum<T>>(
    val blockType: Enum<T>,
    val pos: BlockPos
)
