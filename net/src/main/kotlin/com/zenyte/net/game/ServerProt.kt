package com.zenyte.net.game

import com.near_reality.crypto.StreamCipher
import com.zenyte.net.NetworkConstants
import com.zenyte.net.game.packet.GamePacketOut
import com.zenyte.net.io.RSBuffer
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.util.ByteProcessor

/**
 * @author Tommeh | 28 jul. 2018 | 14:10:53
 * @author Jire
 */
enum class ServerProt(
    override val opcode: Int,
    override val size: Int
) : Prot {
    REBUILD_NORMAL(0, -2),
    LOC_DEL(1, 2),
    PLAYER_INFO(2, -2),
    field3195(3, -2),
    CAM_MOVETO(4, 6),
    UPDATE_RUNWEIGHT(5, 2),
    CAM_LOOKAT(6, 6),
    NPC_INFO_SMALL_VIEWPORT_2(7, -2),
    RESET_CLIENT_VARCACHE(8, 0),
    NPC_INFO_SMALL(9, -2),
    GROUND_OBJECT_OPTION_FLAGS(10, 4),
    CAM_RESET(11, 0),
    UPDATE_ZONE_FULL_FOLLOWS(12, 2),
    UPDATE_ZONE_PARTIAL_FOLLOWS(13, 2),
    field3273(14, 0),
    REFLECTION_CHECKER(15, -2),
    CAM_SHAKE(16, 4),
    TRADING_POST_UPDATE(17, 20),
    RUNCLIENTSCRIPT(18, -2),
    UPDATE_INV_STOP_TRANSMIT(19, 2),
    TRADING_POST_RESULTS(20, -2),
    UPDATE_FRIENDCHAT_CHANNEL_FULL_NEW(21, -2),
    CHAT_FILTER_SETTINGS_PRIVATECHAT(22, 1),
    LOC_ADD_CHANGE(23, 5),
    OBJ_DEL(24, 7),
    MESSAGE_PRIVATE(25, -2),
    VARP_LARGE(26, 6),
    UPDATE_FRIENDCHAT_CHANNEL_SINGLEUSER(27, -1),
    field3220(28, 9),
    CLANSETTINGS_DELTA(29, -2),
    REBUILD_REGION(30, -2),
    OBJ_ADD(31, 14),
    MESSAGE_GAME(32, -1),
    OPEN_URL(33, -2),
    LOGOUT(34, 1),
    MAP_ANIM(35, 6),
    field3200(36, 0),
    UPDATE_STAT(37, 6),
    OBJ_COUNT(38, 7),
    MIDI_SONG(39, 2),
    IF_SETPOSITION(40, 8),
    MAPPROJ_ANIM(41, 15),
    PROJANIM_SPECIFIC(42, 17),
    TOGGLE_OCULUS_ORB(43, 4),
    UPDATE_INV_FULL(44, -2),
    SYNTH_SOUND(45, 5),
    IF_SETEVENTS(46, 12),
    IF_SETOBJECT(47, 10),
    IF_OPENTOP(48, 2),
    field3248(49, 0),
    MIDI_JINGLE(50, 5),
    UPDATE_REBOOT_TIMER(51, 2),
    CLANSETTINGS_FULL(52, -2),
    NPC_INFO_LARGE_VIEWPORT_2(53, -2),
    UPDATE_INV_CLEAR(54, 4),
    AREA_SOUND(55, 5),
    SET_PLAYER_OP(56, -1),
    LOGOUT_FULL(57, 0),
    IF_SETCOLOR(58, 6),
    GRAPHICSOBJECT_SPAWN(59, 8),
    IF_SETPLAYERHEAD(60, 4),
    UPDATE_SITESETTINGS(61, -1),
    NPC_SPOTANIM(62, 8),
    NPC_INFO_LARGE(63, -2),
    IF_CLOSESUB(64, 4),
    MINIMAP_TOGGLE(65, 1),
    IF_SETANGLE(66, 10),
    IF_SETMODEL(67, 6),
    LOGOUT_TRANSFER(68, -1),
    SEND_PING(69, 8),
    UPDATE_INV_PARTIAL(70, -2),
    TRIGGER_ONDIALOGABORT(71, 0),
    CLANCHANNEL_FULL(72, -2),
    UPDATE_FRIENDLIST(73, -2),
    UPDATE_UID192(74, 28),
    UPDATE_RUNENERGY(75, 2),
    CLANCHANNEL_DELTA(76, -2),
    field3269(77, 6),
    HINT_ARROW(78, 6),
    SYNC_CLIENT_VARCACHE(79, 0),
    IF1_MODELROTATE(80, 8),
    MESSAGE_CLANCHANNEL_SYSTEM(81, -1),
    UPDATE_FRIENDCHAT_CHANNEL_FULL(82, -2),
    IF_SETTEXT(83, -2),
    MESSAGE_CLANCHANNEL(84, -1),
    HEAT_MAP(85, 1),
    MESSAGE_PRIVATE_ECHO(86, -2),
    field3225(87, -1),
    IF_SETANIM(88, 6),
    IF_OPENSUB(89, 7),
    SET_MAP_FLAG(90, 2),
    NPC_SET_SEQUENCE(91, 5),
    CHAT_FILTER_SETTINGS(92, 2),
    field3285(93, -2),
    PLAYER_SPOTANIM(94, 8),
    ENTER_FREECAM(95, 1),
    UPDATE_IGNORELIST(96, -2),
    VARP_SMALL(97, 3),
    DYNAMICOBJECT_SPAWN(98, 6),
    RESET_ANIMS(99, 0),
    IF_SETSCROLLPOS(100, 6),
    LOC_COMBINE(101, 14),
    IF_SETHIDE(102, 5),
    field3295(103, 4),
    IF_SETNPCHEAD(104, 6),
    UPDATE_ZONE_PARTIAL_ENCLOSED(105, -2),
    field3298(106, 17),
    field3299(107, 16),
    FRIENDLIST_LOADED(108, 0),
    LOC_ANIM(109, 4),
    IF_MOVESUB(110, 8),
    MESSAGE_FRIENDCHANNEL(111, -1),
    field3304(112, 11),
    /*REBUILD_NORMAL(0, -2),
    PROJANIM_SPECIFIC(1, 17),
    RESET_ANIMS(2, 0),
    IF_SETANIM(3, 6),
    TRADING_POST_UPDATE(4, 20),
    IF_MOVESUB(5, 8),
    UPDATE_RUNWEIGHT(6, 2),
    TRADING_POST_RESULTS(7, -2),
    UPDATE_IGNORELIST(8, -2),
    HEAT_MAP(9, 1),
    UPDATE_SITESETTINGS(10, -1),
    UPDATE_STAT(11, 6),
    MIDI_JINGLE(12, 5),
    FRIENDLIST_LOADED(13, 0),
    RUNCLIENTSCRIPT(14, -2),
    UPDATE_RUNENERGY(15, 1),
    NPC_INFO_LARGE(16, -2),
    UPDATE_FRIENDCHAT_CHANNEL_FULL(17, -2),
    TRIGGER_ONDIALOGABORT(18, 0),
    MESSAGE_CLANCHANNEL(19, -1),
    NPC_INFO_SMALL(20, -2),
    UPDATE_ZONE_PARTIAL_FOLLOWS(21, 2),
    UPDATE_FRIENDLIST(22, -2),
    IF_SETTEXT(23, -2),
    MESSAGE_FRIENDCHANNEL(24, -1),
    SET_MAP_FLAG(25, 2),
    LOGOUT_TRANSFER(26, -1),
    LOGOUT(27, 1),
    IF_CLOSESUB(28, 4),
    UPDATE_FRIENDCHAT_CHANNEL_SINGLEUSER(29, -1),
    LEGACY_NPC_INFO_SMALL(30, -2),
    CLANCHANNEL_FULL(31, -2),
    UPDATE_ZONE_PARTIAL_ENCLOSED(32, -2),
    SYNTH_SOUND(33, 5),
    VARP_LARGE(34, 6),
    CAM_LOOKAT(35, 6),
    MESSAGE_GAME(36, -1),
    IF_SETPOSITION(37, 8),
    VARCLAN_DISABLE(38, 0),
    MAP_SPOTANIM_SPECIFIC(39, 8),
    UPDATE_INV_STOP_TRANSMIT(40, 2),
    GAMEFRAME_FULL(41, -2),
    LOC_ANIM_SPECIFIC(42, 6),
    UPDATE_REBOOT_TIMER(43, 2),
    LOC_COMBINE(44, 14),
    CAM_MOVETO(45, 6),
    NPC_ANIM_SPECIFIC(46, 5),
    TOGGLE_OCULUS_ORB(47, 4),
    IF_OPENSUB(48, 7),
    IF_OPENTOP(49, 2),
    IF_SETNPCHEAD(50, 6),
    SYNC_CLIENT_VARCACHE(51, 0),
    UPDATE_INV_FULL(52, -2),
    IF_SETCOLOR(53, 6),
    CHAT_FILTER_SETTINGS_PRIVATECHAT(54, 1),
    UPDATE_INV_CLEAR(55, 4),
    UPDATE_UID192(56, 28),
    VARCLAN_ENABLE(57, 0),
    LEGACY_NPC_INFO_LARGE(58, -2),
    OBJ_DEL(59, 3),
    UPDATE_ZONE_FULL_FOLLOWS(60, 2),
    IF_SETANGLE(61, 10),
    MINIMAP_TOGGLE(62, 1),
    VARCLAN(63, -1),
    IF_SETMODEL(64, 6),
    IF_SETSCROLLPOS(65, 6),
    MAP_ANIM(66, 6),
    CLANSETTINGS_FULL(67, -2),
    IF_SETHIDE(68, 5),
    CAM_RESET(69, 0),
    OBJ_COUNT(70, 7),
    ENTER_FREECAM(71, 1),
    IF_SETPLAYERHEAD(72, 4),
    IF1_MODELROTATE(73, 8),
    SPOTANIM_SPECIFIC(74, 8),
    RESET_CLIENT_VARCACHE(75, 0),
    HINT_ARROW(76, 6),
    VARP_SMALL(77, 3),
    SET_PLAYER_OP(78, -1),
    MESSAGE_PRIVATE_ECHO(79, -2),
    PLAYER_INFO(80, -2),
    MIDI_SONG(81, 2),
    CHAT_FILTER_SETTINGS(82, 2),
    LOC_ADD_CHANGE(83, 4),
    CAM_SHAKE(84, 4),
    LOC_DEL(85, 2),
    AREA_SOUND(86, 5),
    IF_SETEVENTS(87, 12),
    PLAYER_SPOTANIM_SPECIFIC(88, 8),
    CLANSETTINGS_DELTA(89, -2),
    IF_SETOBJECT(90, 10),
    OBJ_ADD(91, 5),
    LOGOUT_FULL(92, 0),
    LOC_ANIM(93, 4),
    CAM_SMOOTHRESET(94, 4),
    MESSAGE_PRIVATE(95, -2),
    UPDATE_INV_PARTIAL(96, -2),
    CLANCHANNEL_DELTA(97, -2),
    REFLECTION_CHECK(98, -2),
    SEND_PING(99, 8),
    OPEN_URL(100, -2),
    MESSAGE_CLANCHANNEL_SYSTEM(101, -1),
    MAPPROJ_ANIM(102, 15),
    REBUILD_REGION(103, -2),*/
    ;

    val initialSize: Int
        get() = if (size >= 0) size else 16
    val capacity: Int
        get() = if (size >= 0) size else if (size == -1) 255 else NetworkConstants.MAX_SERVER_BUFFER_SIZE

    override val headerSize = when (size) {
        -1 -> 1
        -2 -> 2
        in 0..NetworkConstants.MAX_SERVER_BUFFER_SIZE -> 0
        else -> throw IllegalArgumentException("($name, opcode $opcode) invalid size $size")
    }

    fun byteBuf(): ByteBuf = ByteBufAllocator.DEFAULT.buffer(initialSize, capacity)

    fun rsBuffer(): RSBuffer = RSBuffer(byteBuf())

    @JvmOverloads
    fun gamePacketOut(byteBuf: ByteBuf = byteBuf(), encryptBuffer: Boolean = false) =
        GamePacketOut(this, byteBuf, encryptBuffer)

    fun gamePacketOut(encryptBuffer: Boolean) = gamePacketOut(byteBuf(), encryptBuffer)

    @JvmOverloads
    fun gamePacketOut(rsBuffer: RSBuffer, encryptBuffer: Boolean = false) =
        gamePacketOut(rsBuffer.content(), encryptBuffer)

    fun encodePacket(cipher: StreamCipher, content: ByteBuf, encryptPacket: Boolean, out: ByteBuf) {
        val headerSize = headerSize
        val contentSize = content.readableBytes()

        out.writeByte(opcode + cipher.nextInt())
        when (headerSize) {
            1 -> out.writeByte(contentSize)
            2 -> out.writeShort(contentSize)
        }

        if (encryptPacket) {
            content.forEachByte { value ->
                out.writeByte((value + cipher.nextInt()) and 0xFF)
                true
            }
        } else {
            out.writeBytes(content, content.readerIndex(), contentSize)
        }
    }

    private companion object {

        private val encryptByteProcessor: ThreadLocal<EncryptByteProcessor> = ThreadLocal.withInitial {
            EncryptByteProcessor()
        }

        private class EncryptByteProcessor : ByteProcessor {
            var count = 0
            lateinit var cipher: StreamCipher
            lateinit var out: ByteBuf
            var size = 0

            override fun process(value: Byte): Boolean {
                if (++count >= size) return false
                out.writeByte((value + cipher.nextInt()) and 0xFF)
                return true
            }

            fun reset(cipher: StreamCipher, out: ByteBuf, size: Int) = apply {
                this.count = 0
                this.cipher = cipher
                this.out = out
                this.size = size
            }
        }

    }

}