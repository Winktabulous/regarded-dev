package com.near_reality.game.world.entity.player

import com.near_reality.net.HardwareInfo
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.PlayerInformation
import com.zenyte.net.Session
import com.zenyte.net.game.ServerEvent

class FakePlayer(username: String) : Player(
    fakePlayerInformation(username),
    null
) {
    init {
        session = FakeSession()
    }
}

private class FakeSession : Session {
    override fun getHostAddress(): String = "0.0.0.0"
    override fun process(): Boolean = true
    override fun send(event: ServerEvent): Boolean = true
    override fun flush() = Unit
    override fun close() = Unit
    override fun isActive(): Boolean = true
    override fun isExpired(): Boolean = false
}

private fun fakePlayerInformation(username: String) =
    PlayerInformation(username, username, -1, ByteArray(25), fakeHardwareInfo())

private fun fakeHardwareInfo() = HardwareInfo(
    cpuFeatures = intArrayOf(0, 0, 0),
    osId = 1,
    osVersion = 11,
    javaVendorId = 5,
    javaVersionMajor = 18,
    javaVersionMinor = 0,
    javaVersionUpdate = 2,
    heap = 4079,
    logicalProcessors = 12,
    physicalMemory = 0,
    clockSpeed = 0,
    graphicCardReleaseMonth = 0,
    graphicCardReleaseYear = 0,
    cpuCount = 0,
    cpuBrandType = 0,
    cpuModel = 0,
    graphicCardManufacture = "",
    graphicCardName = "",
    dxVersion = "",
    cpuManufacture = "",
    cpuName = "",
    isArch64Bit = true,
    isApplet = true
)
