package com.zenyte.game

import com.zenyte.game.world.WorldThread
import java.text.NumberFormat

/**
 * @author Kris | 27/03/2022
 */
@Suppress("NOTHING_TO_INLINE")
inline fun gameClock(): Int = WorldThread.getCurrentCycle().toInt()
fun Int.format(): String = NumberFormat.getIntegerInstance().format(this)
fun Long.format(): String = NumberFormat.getIntegerInstance().format(this)