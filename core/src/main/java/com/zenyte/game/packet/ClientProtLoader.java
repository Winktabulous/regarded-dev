package com.zenyte.game.packet;

import com.zenyte.game.GameLoader;
import com.zenyte.game.packet.in.decoder.*;
import com.zenyte.net.game.ClientProt;

import java.util.Arrays;

/**
 * @author Tommeh | 28 jul. 2018 | 13:21:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ClientProtLoader {

    public static void load() {
        register(ClientProt.MAP_BUILD_COMPLETE.getOpcode(), new MapBuildComplete());
        register(ClientProt.WINDOW_STATUS.getOpcode(), new WindowStatusDecoder());
        register(ClientProt.NO_TIMEOUT.getOpcode(), new NoTimeOutDecoder());
        register(ClientProt.LOGIN_STATISTICS.getOpcode(), new LoginStatisticsDecoder());
        register(ClientProt.EVENT_APPLET_FOCUS.getOpcode(), new EventAppletFocusDecoder());
        register(ClientProt.IDLE_LOGOUT.getOpcode(), new EventMouseIdleDecoder());
        register(ClientProt.EVENT_MOUSE_MOVE.getOpcode(), new EventMouseMoveDecoder());
        register(ClientProt.EVENT_MOUSE_CLICK.getOpcode(), new EventMouseClickDecoder());
        Arrays.stream(If3ButtonActionDecoder.OPCODES).forEach(opcode -> register(opcode, new If3ButtonActionDecoder()));
        register(ClientProt.MOVE_MINIMAPCLICK.getOpcode(), new MoveMinimapClickDecoder());
        register(ClientProt.MOVE_GAMECLICK.getOpcode(), new MoveGameClickDecoder());
        register(ClientProt.EVENT_CAMERA_POSITION.getOpcode(), new EventCameraPosDecoder());
        register(ClientProt.EVENT_KEYBOARD.getOpcode(), new EventKeyboardDecoder());
        Arrays.stream(If1ButtonActionDecoder.OPCODES).forEach(opcode -> register(opcode, new If1ButtonActionDecoder()));
        register(ClientProt.BUTTON_CLICK.getOpcode(), new OpModelDecoder());
        register(ClientProt.CLOSE_MODAL.getOpcode(), new CloseModalDecoder());
        register(ClientProt.CLIENT_CHEAT.getOpcode(), new CommandDecoder());
        register(ClientProt.MESSAGE_PUBLIC.getOpcode(), new MessagePublicDecoder());
        register(ClientProt.TELEPORT.getOpcode(), new ClickWorldMapDecoder());
        register(ClientProt.CLICKWORLDMAP.getOpcode(), new DoubleClickWorldMapDecoder());
        register(ClientProt.RESUME_PAUSEBUTTON.getOpcode(), new ResumePauseButtonDecoder());
        register(ClientProt.SEND_PING_REPLY.getOpcode(), new PingStatisticsDecoder());
        register(ClientProt.RESUME_P_STRINGDIALOG.getOpcode(), new ResumePStringDialogDecoder());
        register(ClientProt.RESUME_P_NAMEDIALOG.getOpcode(), new ResumePNameDialogDecoder());
        register(ClientProt.RESUME_P_COUNTDIALOG.getOpcode(), new ResumePCountDialogDecoder());
        register(ClientProt.RESUME_P_OBJDIALOG.getOpcode(), new ResumePObjDialogDecoder());
        Arrays.stream(OpLocDecoder.OPCODES).forEach(opcode -> register(opcode, new OpLocDecoder()));
        register(ClientProt.OPLOC6.getOpcode(), new OpLocExamineDecoder());
        Arrays.stream(OpNpcDecoder.OPCODES).forEach(opcode -> register(opcode, new OpNpcDecoder()));
        register(ClientProt.OPNPC6.getOpcode(), new OpNpcExamineDecoder());
        Arrays.stream(OpObjDecoder.OPCODES).forEach(opcode -> register(opcode, new OpObjDecoder()));
        Arrays.stream(OpPlayerDecoder.OPCODES).forEach(opcode -> register(opcode, new OpPlayerDecoder()));
        register(ClientProt.OPNPCU.getOpcode(), new OpNpcUDecoder());
        register(ClientProt.OPLOCU.getOpcode(), new OpLocUDecoder());
        register(ClientProt.OPPLAYERU.getOpcode(), new OpPlayerUDecoder());
        register(ClientProt.OPOBJU.getOpcode(), new OpObjUDecoder());
        register(ClientProt.IF_BUTTOND.getOpcode(), new IfButtonDDecoder());
        register(ClientProt.OPLOCT.getOpcode(), new OpLocTDecoder());
        register(ClientProt.OPNPCT.getOpcode(), new OpNpcTDecoder());
        register(ClientProt.OPPLAYERT.getOpcode(), new OpPlayerTDecoder());
        register(ClientProt.OPOBJT.getOpcode(), new OpObjTDecoder());
        register(ClientProt.FRIENDLIST_ADD.getOpcode(), new FriendListAddDecoder());
        register(ClientProt.IGNORELIST_ADD.getOpcode(), new IgnoreListAddDecoder());
        register(ClientProt.FRIENDLIST_DEL.getOpcode(), new FriendListDelDecoder());
        register(ClientProt.IGNORELIST_DEL.getOpcode(), new IgnoreListDelDecoder());
        register(ClientProt.MESSAGE_PRIVATE.getOpcode(), new MessagePrivateDecoder());
        register(ClientProt.CLAN_JOINCHAT_LEAVECHAT.getOpcode(), new ClanJoinChatLeaveChatDecoder());
        register(ClientProt.EXIT_FREECAM.getOpcode(), new FreeCamResetDecoder());
        register(ClientProt.SET_CHATFILTERSETTINGS.getOpcode(), new ChatSetModeDecoder());
        register(ClientProt.FRIEND_SETRANK.getOpcode(), new FriendSetRankDecoder());
        register(ClientProt.CLAN_KICKUSER.getOpcode(), new ClanKickUserDecoder());
        register(ClientProt.BUG_REPORT.getOpcode(), new BugReportDecoder());
        register(ClientProt.SEND_SNAPSHOT.getOpcode(), new PlayerReportDecoder());
        register(ClientProt.IF_BUTTONT.getOpcode(), new IfButtonTDecoder());

        //register(ClientProt.LOGIN_INFO.getOpcode(), new LoginInfoDecoder());
        //register(ClientProt.PACKET_82.getOpcode(), new ComponentOnComponentDecoder());
        //register(ClientProt.REFLECTION_CHECK.getOpcode(), new ReflectionCheckDecoder());
        //Arrays.stream(If1ButtonActionDecoder.OPCODES).forEach(i -> Game.getDecoders()[i, new If1ButtonActionDecoder
		// ());
        //register(ClientProt.SPELL_ON_COMPONENT.getOpcode(), new SpellOnComponentDecoder());
        // Opheld no longer needed since inv turned to if3.
        // Arrays.stream(OpHeldDecoder.OPCODES).forEach(opcode -> register(opcode, new OpHeldDecoder()));
        // register(ClientProt.OPHELDU.getOpcode(), new OpHeldUDecoder());
        // register(ClientProt.OPHELDD.getOpcode(), new OpHeldDDecoder());
        // register(ClientProt.OPHELDT.getOpcode(), new OpHeldTDecoder());
        // Appearance unused now, was replaced with a new interface.
        // register(ClientProt.SET_APPEARANCE.getOpcode(), new SetAppearanceDecoder());
    }

    private static void register(final int opcode, final ClientProtDecoder decoder) {
        GameLoader.getDecoders()[opcode] = decoder;
    }

}
