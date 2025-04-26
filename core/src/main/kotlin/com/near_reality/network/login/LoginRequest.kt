package com.near_reality.network.login

import com.near_reality.api.GameDatabase
import com.near_reality.api.responses.UserLoginResponse
import com.near_reality.api.service.user.UserPlayerHandler
import com.near_reality.api.service.user.twoFactorEnabled
import com.near_reality.game.world.entity.player.onLogin
import com.near_reality.network.MasterHandler
import com.near_reality.network.NetworkPipelineConstants.DECODER
import com.near_reality.network.NetworkPipelineConstants.ENCODER
import com.near_reality.network.NetworkPipelineConstants.HANDLER
import com.near_reality.network.ipv4Address
import com.near_reality.network.login.packet.LoginPacketIn
import com.near_reality.network.world.WorldDecoder
import com.near_reality.network.world.WorldEncoder
import com.zenyte.game.GameConstants
import com.zenyte.game.packet.out.RebuildNormal
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.PlayerInformation
import com.zenyte.net.ClientResponse
import com.zenyte.net.NetworkConstants.NETWORKING_DEBUG
import com.zenyte.net.game.ServerProt
import com.zenyte.net.login.AuthType
import com.zenyte.utils.TextUtils
import io.netty.channel.ChannelHandlerContext
import kotlinx.coroutines.runBlocking

/**
 * @author Jire
 */
class LoginRequest(
    private val request: LoginPacketIn,
    private val info: PlayerInformation,
    private val ctx: ChannelHandlerContext
) {

    private fun getResponseCode(player: Player?): ClientResponse {
        val response = request.getResponse()
        if (response != null)
            return response

        val username = info.username
        if (username.isEmpty()
            || username.first().equals('_', true)
            || username.last().equals('_', true)
            || username.first().equals(' ', true)
            || username.last().equals(' ', true)
            || username.length > 12
            || !TextUtils.isValidName(username)
            )
            return ClientResponse.INVALID_USERNAME_OR_PASSWORD
//
//        if(request.subVersion != GameConstants.CLIENT_VERSION)
//            return ClientResponse.BAD_SESSION_ID
        val world = GameConstants.WORLD_PROFILE

        if (world.useWhitelist && !world.whitelistedUsernames.contains(username))
            return ClientResponse.CLOSED_BETA

        if (!world.verifyPasswords)
            return ClientResponse.LOGIN_OK

        if (player != null) {
            if (!world.isApiEnabled()) {
                if (world.isMainDatabaseEnabled()) {
                    val loginResponse = runBlocking {
                        GameDatabase.validateUserLogin(info.displayname, info.plainPassword)
                    }
                    return  when (loginResponse) {
                        UserLoginResponse.InvalidPassword,
                        UserLoginResponse.UserNotExist -> ClientResponse.INVALID_USERNAME_OR_PASSWORD
                        is UserLoginResponse.Success -> {
                            player.user = loginResponse.user
                            ClientResponse.LOGIN_OK
                        }
                    }
                } else {
                    if (request.password != player.playerInformation.plainPassword) {
                        return ClientResponse.INVALID_USERNAME_OR_PASSWORD
                    }
                    //return ClientResponse.LOGIN_SERVER_OFFLINE
                }
            } else {
                val loginResult = UserPlayerHandler.validateLogin(player, ctx.ipv4Address().hostAddress)
                if (loginResult != ClientResponse.LOGIN_OK)
                    return loginResult
                if (world.verify2FA()) {
                    if (player.twoFactorEnabled) {
                        player.authenticator.isEnabled = true
                        val authInfo = request.authInfo!!
                        when (val type = authInfo.type) {
                            AuthType.NORMAL -> {
                                return ClientResponse.PROMPT_AUTHENTICATOR
                            }

                            AuthType.TRUSTED_AUTHENTICATION,
                            AuthType.UNTRUSTED_AUTHENTICATION -> {
                                if (authInfo.code > 0) {
                                    if (!UserPlayerHandler.validate2FA(player, authInfo.code))
                                        return ClientResponse.WRONG_AUTHENICATOR_CODE
                                    if (type == AuthType.TRUSTED_AUTHENTICATION)
                                        player.authenticator.trust()
                                } else
                                    return ClientResponse.WRONG_AUTHENICATOR_CODE
                            }

                            AuthType.TRUSTED_COMPUTER -> {
                                val result = player.authenticator.validate(authInfo.identifier)
                                if (result != ClientResponse.LOGIN_OK) return ClientResponse.PROMPT_AUTHENTICATOR
                            }
                        }
                    } else
                        player.authenticator.isEnabled = true
                }
            }
        }
        if (World.containsPlayer(username)) return ClientResponse.ALREADY_ONLINE
        return ClientResponse.LOGIN_OK
    }

    fun loaded(player: Player?, timeout: Boolean) {
        if (timeout) {
            sendFailureResponse(ClientResponse.LOGIN_EXCEEDED)
            return
        }
        if (player == null) {
            sendFailureResponse(ClientResponse.ERROR_LOADING_PROFILE)
            return
        }

        val response = getResponseCode(player)
        if (response != ClientResponse.LOGIN_OK) {
            sendFailureResponse(response)
            return
        }

        postLogin(player)
    }

    private fun postLogin(player: Player) {
        player.createLogger()
        WorldTasksManager.schedule {
            ctx.channel().config().isAutoRead = false
            val alreadyOnline = World.containsPlayer(info.username)
            if (alreadyOnline) {
                sendFailureResponse(ClientResponse.ALREADY_ONLINE)
                return@schedule
            }

            val encodeCipher = request.ciphers.encode

            val session = ctx.pipeline().get(HANDLER)
            if(session !is MasterHandler)
                return@schedule
            else {
                session.player = player
                player.session = session
            }

            World.addPlayer(player)

            player.areaManager.onLogin(player)

            player.loadMapRegions(true)

            val rebuildNormal = RebuildNormal(player, true).encode()
            try {
                val rebuildNormalContent = rebuildNormal.content()
                val rebuildNormalSize = rebuildNormalContent.readableBytes()

                val initBufSize = 1 + 1 + 21 + rebuildNormalSize
                val initBuf = ctx.alloc().buffer(initBufSize, initBufSize).apply {
                    // login response header
                    writeByte(ClientResponse.LOGIN_OK.id)

                    val lengthWriterIndex = writerIndex()
                    writeZero(1)

                    val payloadWriterIndex = writerIndex()

                    // begin payload...

                    val authenticator = player.authenticator

                    val authenticated = authenticator.isAuthenticated
                    writeByte(if (authenticated) 1 else 0)
                    if (authenticated) {
                        val randomUID = authenticator.randomUID
                        writeByte(((randomUID ushr 24) and 0xFF) + encodeCipher.nextInt())
                        writeByte(((randomUID ushr 16) and 0xFF) + encodeCipher.nextInt())
                        writeByte(((randomUID ushr 8) and 0xFF) + encodeCipher.nextInt())
                        writeByte((randomUID and 0xFF) + encodeCipher.nextInt())
                    } else {
                        writeZero(4)
                    }

                    val privilege = player.privilege
                    writeByte(privilege.loginCode)
                    writeByte(if (privilege.isPMod) 1 else 0)
                    writeShort(player.index)
                    writeByte(1) // Friends/ignores container size. 0 = 200, 1 = 400. OSRS allows 400 for members, 200 for F2P.
                    writeLong(0) // account hash, used for new character picker in osrs, yet to be implemented by them.

                    // "actual world server" encoding starts here.

                    //writeLong(0) // account registration id, incrementing. first acc = 0, etc.

                    writeByte(ServerProt.REBUILD_NORMAL.opcode + encodeCipher.nextInt())
                    writeShort(rebuildNormalSize)

                    val written = writerIndex() - payloadWriterIndex
                    setByte(lengthWriterIndex, written)

                    writeBytes(rebuildNormalContent, 0, rebuildNormalSize)
                }

                player.afterLoadMapRegions()

                ctx.writeAndFlush(initBuf).addListener { future ->
                    if (future.isSuccess) {
                        val decoder = ctx.pipeline().get(DECODER)
                        if(NETWORKING_DEBUG)
                            println("Replaced POW decoder with World decoder")
                        ctx.pipeline().replace(decoder, DECODER, WorldDecoder(request.ciphers.decode))
                        ctx.pipeline().addBefore(HANDLER, ENCODER, WorldEncoder(encodeCipher))

                        WorldTasksManager.schedule {
                            player.isInitialized = true
                            player.onLogin()



                            ctx.channel().config().isAutoRead = true
                            //ctx.channel().read()
                        }
                    }
                }
            } finally {
                rebuildNormal.release()
            }
        }
    }

    private fun sendFailureResponse(response: ClientResponse) = response.writeAndClose(ctx)

}
