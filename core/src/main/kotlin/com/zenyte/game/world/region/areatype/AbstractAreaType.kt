package com.zenyte.game.world.region.areatype

import it.unimi.dsi.fastutil.ints.IntList

/**
 * @author Jire
 */
abstract class AbstractAreaType : AreaType {

    override val regionIDs: IntList = IntList.of()

    override fun matches(x: Int, y: Int, z: Int) = false

    override fun load() {}

    override fun map() {}

}