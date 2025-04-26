package com.near_reality.network.proofofwork

import com.zenyte.net.PacketIn

/**
 * @author Jire
 */
data class ProofOfWorkResponse(
    val nonce: Long
) : PacketIn