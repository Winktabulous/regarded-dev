package com.near_reality.net

import kotlinx.serialization.Serializable

@Serializable
data class HardwareInfo(
    val cpuFeatures: IntArray,
    val osId: Int,
    val osVersion: Int,
    val javaVendorId: Int,
    val javaVersionMajor: Int,
    val javaVersionMinor: Int,
    val javaVersionUpdate: Int,
    val heap: Int,
    val logicalProcessors: Int,
    val physicalMemory: Int,
    val clockSpeed: Int,
    val graphicCardReleaseMonth: Int,
    val graphicCardReleaseYear: Int,
    val cpuCount: Int,
    val cpuBrandType: Int,
    val cpuModel: Int,
    val graphicCardManufacture: String?,
    val graphicCardName: String?,
    val dxVersion: String?,
    val cpuManufacture: String?,
    val cpuName: String?,
    val isArch64Bit: Boolean,
    val isApplet: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HardwareInfo

        if (!cpuFeatures.contentEquals(other.cpuFeatures)) return false
        if (osId != other.osId) return false
        if (osVersion != other.osVersion) return false
        if (javaVendorId != other.javaVendorId) return false
        if (javaVersionMajor != other.javaVersionMajor) return false
        if (javaVersionMinor != other.javaVersionMinor) return false
        if (javaVersionUpdate != other.javaVersionUpdate) return false
        if (heap != other.heap) return false
        if (logicalProcessors != other.logicalProcessors) return false
        if (physicalMemory != other.physicalMemory) return false
        if (clockSpeed != other.clockSpeed) return false
        if (graphicCardReleaseMonth != other.graphicCardReleaseMonth) return false
        if (graphicCardReleaseYear != other.graphicCardReleaseYear) return false
        if (cpuCount != other.cpuCount) return false
        if (cpuBrandType != other.cpuBrandType) return false
        if (cpuModel != other.cpuModel) return false
        if (graphicCardManufacture != other.graphicCardManufacture) return false
        if (graphicCardName != other.graphicCardName) return false
        if (dxVersion != other.dxVersion) return false
        if (cpuManufacture != other.cpuManufacture) return false
        if (cpuName != other.cpuName) return false
        if (isArch64Bit != other.isArch64Bit) return false
        if (isApplet != other.isApplet) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cpuFeatures.contentHashCode()
        result = 31 * result + osId
        result = 31 * result + osVersion
        result = 31 * result + javaVendorId
        result = 31 * result + javaVersionMajor
        result = 31 * result + javaVersionMinor
        result = 31 * result + javaVersionUpdate
        result = 31 * result + heap
        result = 31 * result + logicalProcessors
        result = 31 * result + physicalMemory
        result = 31 * result + clockSpeed
        result = 31 * result + graphicCardReleaseMonth
        result = 31 * result + graphicCardReleaseYear
        result = 31 * result + cpuCount
        result = 31 * result + cpuBrandType
        result = 31 * result + cpuModel
        result = 31 * result + (graphicCardManufacture?.hashCode() ?: 0)
        result = 31 * result + (graphicCardName?.hashCode() ?: 0)
        result = 31 * result + (dxVersion?.hashCode() ?: 0)
        result = 31 * result + (cpuManufacture?.hashCode() ?: 0)
        result = 31 * result + (cpuName?.hashCode() ?: 0)
        result = 31 * result + isArch64Bit.hashCode()
        result = 31 * result + isApplet.hashCode()
        return result
    }
}
