package com.zenyte.net

import com.zenyte.net.io.ByteBufUtil.readJAGString
import com.zenyte.net.io.ByteBufUtil.readMedium
import io.netty.buffer.ByteBuf

class HardwareInfoReader(buffer: ByteBuf) {

    val cpuFeatures = IntArray(3)
    var osId = 0
        private set
    var osVersion = 0
        private set
    var javaVendorId = 0
        private set
    var javaVersionMajor = 0
        private set
    var javaVersionMinor = 0
        private set
    var javaVersionUpdate = 0
        private set
    var heap = 0
        private set
    var logicalProcessors = 0
        private set
    var physicalMemory = 0
        private set
    var clockSpeed = 0
        private set
    var graphicCardReleaseMonth = 0
        private set
    var graphicCardReleaseYear = 0
        private set
    var cpuCount = 0
        private set
    var cpuBrandType = 0
        private set
    var cpuModel = 0
        private set
    var graphicCardManufacture: String? = null
        private set
    var graphicCardName: String? = null
        private set
    var dxVersion: String? = null
        private set
    var cpuManufacture: String? = null
        private set
    var cpuName: String? = null
        private set
    var isArch64Bit = false
        private set
    var isApplet = false
        private set

    init {
        decode(buffer)
    }

    override fun hashCode(): Int {
        return clockSpeed + (osId shl 12) + (osVersion shl 16) + (logicalProcessors shl 24)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is HardwareInfoReader) return false
        return osId == other.osId
                && isArch64Bit == other.isArch64Bit
                && osVersion == other.osVersion
                && javaVendorId == other.javaVendorId
                && javaVersionMajor == other.javaVersionMajor
                && javaVersionMinor == other.javaVersionMinor
                && javaVersionUpdate == other.javaVersionUpdate
                && isApplet == other.isApplet
                && heap == other.heap
                && logicalProcessors == other.logicalProcessors
                && physicalMemory == other.physicalMemory
                && clockSpeed == other.clockSpeed
    }

    /**
     * Decode's hardware information.
     */
    private fun decode(buffer: ByteBuf) {
        val version = buffer.readUnsignedByte()
        if (version.toInt() != 9) throw RuntimeException("Unsupported version: $version")
        osId = buffer.readUnsignedByte().toInt()
        isArch64Bit = buffer.readUnsignedByte().toInt() == 1
        osVersion = buffer.readUnsignedShort()
        javaVendorId = buffer.readUnsignedByte().toInt()
        javaVersionMajor = buffer.readUnsignedByte().toInt()
        javaVersionMinor = buffer.readUnsignedByte().toInt()
        javaVersionUpdate = buffer.readUnsignedByte().toInt()
        isApplet = buffer.readUnsignedByte().toInt() == 0
        heap = buffer.readUnsignedShort()
        logicalProcessors = buffer.readUnsignedByte().toInt() // only if > java1.3
        physicalMemory = readMedium(buffer)
        clockSpeed = buffer.readUnsignedShort()
        graphicCardManufacture = readJAGString(buffer)
        graphicCardName = readJAGString(buffer)
        readJAGString(buffer) // empty3
        dxVersion = readJAGString(buffer)
        graphicCardReleaseMonth = buffer.readUnsignedByte().toInt()
        graphicCardReleaseYear = buffer.readUnsignedShort()
        cpuManufacture = readJAGString(buffer)
        cpuName = readJAGString(buffer)
        cpuCount = buffer.readUnsignedByte().toInt()
        cpuBrandType = buffer.readUnsignedByte().toInt()
        for (index in cpuFeatures.indices) {
            cpuFeatures[index] = buffer.readInt()
        }
        cpuModel = buffer.readInt() // cpuModel
        readJAGString(buffer) //unknown
        readJAGString(buffer) //unknown
    }

}
