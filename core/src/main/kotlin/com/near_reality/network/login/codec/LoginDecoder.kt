package com.near_reality.network.login.codec

import com.near_reality.buffer.readString
import com.near_reality.crypto.IsaacRandom
import com.near_reality.crypto.Rsa.rsa
import com.near_reality.crypto.StreamCipher
import com.near_reality.crypto.xteaDecrypt
import com.near_reality.net.HardwareInfo
import com.near_reality.network.login.AuthenticatorInfo
import com.near_reality.network.login.ClientSettings
import com.near_reality.network.login.LoginCiphers
import com.near_reality.network.login.packet.LoginPacketIn
import com.zenyte.net.ClientResponse
import com.zenyte.net.HardwareInfoReader
import com.zenyte.net.NetworkConstants
import com.zenyte.net.io.ByteBufUtil.readIntIME
import com.zenyte.net.io.ByteBufUtil.readIntME
import com.zenyte.net.login.AuthType
import com.zenyte.net.login.LoginType
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import kotlin.math.min

/**
 * @author Jire
 */
class LoginDecoder : ByteToMessageDecoder() {

    private enum class State {
        OPCODE,
        LENGTH,
        PAYLOAD
    }

    private var state = State.OPCODE

    private var loginType: LoginType? = null
    private var length = -1

    override fun decode(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        if (state == State.OPCODE) {
            loginType = when (val loginTypeID = buf.readUnsignedByte().toInt()) {
                LoginType.NEW_LOGIN_CONNECTION.id -> LoginType.NEW_LOGIN_CONNECTION
                LoginType.RECONNECT_LOGIN_CONNECTION.id -> LoginType.RECONNECT_LOGIN_CONNECTION
                else -> {
                    ctx.failureResponse(ClientResponse.SERVICE_UNAVAILABLE)
                    logger.warn("Invalid login type ID {}", loginTypeID)
                    return
                }
            }

            state = State.LENGTH
        }

        if (state == State.LENGTH) {
            if (!buf.isReadable(2)) return

            length = buf.readUnsignedShort()

            state = State.PAYLOAD
        }

        if (state == State.PAYLOAD) {
            if (!buf.isReadable(length)) return

            handlePayload(ctx, buf, out)
        }
    }

    private fun handlePayload(ctx: ChannelHandlerContext, buf: ByteBuf, out: MutableList<Any>) {
        val version = buf.readInt()
        if (version != NetworkConstants.REVISION) {
            ctx.failureResponse(ClientResponse.CLIENT_UPDATED)
            logger.warn(
                "Version didn't match, expected {} but received {}",
                NetworkConstants.REVISION,
                version
            )
            return
        }
        val subVersion = buf.readInt()
        if (subVersion != NetworkConstants.SUB_VERSION_DESKTOP) {
            ctx.failureResponse(ClientResponse.CLIENT_UPDATED)
            logger.warn(
                "Subversion didn't match, expected {} but received {}",
                NetworkConstants.SUB_VERSION_DESKTOP,
                subVersion
            )
            return
        }

        val expectedClientType = 1
        val clientType = buf.readUnsignedByte().toInt()
        if (clientType != expectedClientType) {
            ctx.failureResponse(ClientResponse.CLIENT_UPDATED)
            logger.warn("Client type didn't match, expected {} but received {}", expectedClientType, clientType)
            return
        }

        buf.skipBytes(1) // field486
        buf.skipBytes(1) // always 0

        val rsaBufSize = buf.readUnsignedShort()
        if (rsaBufSize > MAX_RSA_BUFFER_SIZE) {
            logger.warn("Maximum RSA buffer size ({}) exceeded: {}", MAX_RSA_BUFFER_SIZE, rsaBufSize)
            return
        }
        val rsaBuf = buf.readSlice(rsaBufSize).rsa()
        try {
            val encCheck = rsaBuf.readUnsignedByte().toInt()
            if (encCheck != 1) {
                ctx.failureResponse(ClientResponse.BAD_SESSION_ID)
                logger.warn("RSA keys didn't match, first byte was {}", encCheck)
                return
            }
            val xteaKeys = IntArray(4) { rsaBuf.readInt() }
            @Suppress("UNUSED_VARIABLE") val sessionID = rsaBuf.readLong()

            var authInfo: AuthenticatorInfo? = null
            var password = ""
            val previousSeed = IntArray(4)
            when (loginType) {
                LoginType.RECONNECT_LOGIN_CONNECTION -> {
                    for (i in 0..previousSeed.lastIndex) {
                        previousSeed[i] = rsaBuf.readInt()
                    }
                }

                else -> {
                    authInfo = rsaBuf.decodeAuthInfo()

                    rsaBuf.skipBytes(1)
                    password = rsaBuf.readString()
                }
            }

            val xteaBufLength = buf.readableBytes()
            val xteaBuf = buf.readBytes(xteaBufLength)
            try {
                xteaBuf.xteaDecrypt(length = xteaBufLength, key = xteaKeys)
                val username = xteaBuf.readString()
                val clientSettings = xteaBuf.decodeClientSettings()
                val uniqueID = ByteArray(24) { xteaBuf.readByte() } // unique id stored in random.dat
                val sessionToken = xteaBuf.readString()
                @Suppress("UNUSED_VARIABLE") val affiliateID = xteaBuf.readInt()
                val hardwareInfoReader = xteaBuf.decodeHardwareInfo()
                val hardwareInfo = HardwareInfo(
                    hardwareInfoReader.cpuFeatures,
                    hardwareInfoReader.osId,
                    hardwareInfoReader.osVersion,
                    hardwareInfoReader.javaVendorId,
                    hardwareInfoReader.javaVersionMajor,
                    hardwareInfoReader.javaVersionMinor,
                    hardwareInfoReader.javaVersionUpdate,
                    hardwareInfoReader.heap,
                    hardwareInfoReader.logicalProcessors,
                    hardwareInfoReader.physicalMemory,
                    hardwareInfoReader.clockSpeed,
                    hardwareInfoReader.graphicCardReleaseMonth,
                    hardwareInfoReader.graphicCardReleaseYear,
                    hardwareInfoReader.cpuCount,
                    hardwareInfoReader.cpuBrandType,
                    hardwareInfoReader.cpuModel,
                    hardwareInfoReader.graphicCardManufacture,
                    hardwareInfoReader.graphicCardName,
                    hardwareInfoReader.dxVersion,
                    hardwareInfoReader.cpuManufacture,
                    hardwareInfoReader.cpuName,
                    hardwareInfoReader.isArch64Bit,
                    hardwareInfoReader.isApplet
                )
                @Suppress("UNUSED_VARIABLE") val supportsJS = xteaBuf.readUnsignedByte()
                xteaBuf.readInt()

                val crcs = IntArray(min(0xFF, xteaBuf.readableBytes() / 4))
                readCRCs(crcs, xteaBuf)

                val serverKeys = IntArray(xteaKeys.size) {
                    xteaKeys[it] + 50
                }
                val encodeCipher: StreamCipher = IsaacRandom(serverKeys)
                val decodeCipher: StreamCipher = IsaacRandom(xteaKeys)

                val ciphers = LoginCiphers(encodeCipher, decodeCipher)

                val request = LoginPacketIn(
                    loginType!!,
                    version,
                    /* we replace with something modifiable in the client through easy mixin */
                    //subVersion,
                    hardwareInfo.clockSpeed,
                    username,
                    password,
                    clientSettings,
                    uniqueID,
                    hardwareInfo,
                    authInfo,
                    crcs,
                    sessionToken,
                    previousSeed,
                    ciphers
                )
                out += request

                state = State.OPCODE
            } finally {
                xteaBuf.release()
            }
        } finally {
            rsaBuf.release()
        }
    }

    override fun isSingleDecode() = true

    private companion object {

        private const val MAX_RSA_BUFFER_SIZE = 500

        private val logger: Logger = LoggerFactory.getLogger(LoginDecoder::class.java)

        private fun ChannelHandlerContext.failureResponse(response: ClientResponse) {
            writeAndFlush(
                alloc()
                    .buffer(1, 1)
                    .writeByte(response.id)
            ).addListener(ChannelFutureListener.CLOSE)
        }

        private fun ByteBuf.decodeAuthInfo(): AuthenticatorInfo {
            var code = -1
            var identifier = -1
            val authTypeID = readUnsignedByte().toInt()
            val type = AuthType[authTypeID] ?: throw IOException("Invalid auth type for ID $authTypeID")
            when (type) {
                AuthType.NORMAL -> skipBytes(4)
                AuthType.TRUSTED_AUTHENTICATION, AuthType.UNTRUSTED_AUTHENTICATION -> {
                    code = readUnsignedMedium()
                    skipBytes(1)
                }

                AuthType.TRUSTED_COMPUTER -> identifier = readInt()
            }
            return AuthenticatorInfo(type, code, identifier)
        }

        private fun ByteBuf.decodeClientSettings(): ClientSettings {
            val bitPack = readUnsignedByte().toInt()
            val lowMemory = (bitPack and 1) == 1
            val isResizable = ((bitPack ushr 1) and 1) == 1

            val width = readUnsignedShort()
            val height = readUnsignedShort()

            return ClientSettings(lowMemory, isResizable, width, height)
        }

        private fun ByteBuf.decodeHardwareInfo() = HardwareInfoReader(this)

        private fun readCRCs(crc: IntArray, buf: ByteBuf) {
            crc[12] = readIntIME(buf)
            crc[2] = readIntIME(buf)
            crc[10] = readIntIME(buf)
            crc[11] = buf.readIntLE()
            crc[5] = readIntME(buf)
            crc[16] = buf.readIntLE()
            crc[14] = buf.readIntLE()
            crc[19] = buf.readInt()
            crc[18] = readIntME(buf)
            crc[20] = buf.readInt()
            crc[0] = readIntIME(buf)
            crc[3] = buf.readIntLE()
            crc[8] = readIntME(buf)
            crc[13] = buf.readInt()
            crc[6] = readIntME(buf)
            crc[15] = readIntME(buf)
            crc[9] = readIntIME(buf)
            crc[7] = readIntIME(buf)
            crc[17] = buf.readIntLE()
            crc[4] = buf.readInt()
            crc[1] = buf.readIntLE()
        }
    }

}
