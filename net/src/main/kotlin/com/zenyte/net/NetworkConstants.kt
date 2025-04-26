package com.zenyte.net

/**
 * @author Tommeh | 28 jul. 2018 | 20:22:18
 * @see [Rune-Server profile](https://www.rune-server.ee/members/tommeh/)}
 *
 * @author Jire
 */
object NetworkConstants {

    const val PLAYER_CAP = 2_000

    const val REVISION = 211
    const val SUB_VERSION_DESKTOP = 1
    const val SESSION_TOKEN = "ElZAIrq5NpKN6D3mDdihco3oPeYN2KFy2DCquj7JMmECPmLrDP3Bnw"

    const val MAX_CLIENT_BUFFER_SIZE = 5_000
    const val MAX_SERVER_BUFFER_SIZE = 40_000
    const val INITIAL_SERVER_BUFFER_SIZE = 16

    const val MAX_INBOUND_PACKETS_PER_TICK = 32 // must be multiple of 4
    const val MAX_OUTBOUND_PACKETS_PER_TICK = 512
    const val MAX_PENDING_OUTBOUND_PACKETS = MAX_OUTBOUND_PACKETS_PER_TICK * 3

    const val NETWORKING_DEBUG = false
    const val DEBUG_CLIENT_PACKETS = true
    const val DEBUG_JS5_PROTOCOL = false

    @JvmField
    @Volatile
    var proofOfWorkDifficulty = 20

}
