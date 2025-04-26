package com.zenyte.game.model.ui;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.Expose;
import com.near_reality.api.resources.Vote;
import com.near_reality.game.model.ui.chat_channel.ChatChannelPlayerExtKt;
import com.zenyte.game.GameInterface;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.plugins.PluginManager;
import com.zenyte.plugins.events.PostWindowStatusEvent;
import com.zenyte.utils.Ordinal;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mgi.types.component.ComponentDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import static com.zenyte.game.GameInterface.EMOTE_TAB;

/**
 * @author Tommeh | 28 jan. 2018 : 21:24:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class InterfaceHandler {
	private static final int[] EXPANDED_INTERFACES = new int[] {12, 139, 400, 345, 310, 700, 1704, 1709, 675, 724, 1614, 772, 774};
	private static final Object[] EXPANDED_ARGS = new Object[] {-1, -2};
	private static final Object[] DEFAULT_ARGS = new Object[] {-1, -1};
	private static final Map<Integer, Integer[]> BACKGROUND_SCRIPT_ARGS = ImmutableMap.<Integer, Integer[]>builder()
			.put(25, new Integer[] {5066031, 125}).put(52, new Integer[] {3612928, 0}).put(57, new Integer[] {4535323, 0})
			.put(416, new Integer[] {0x372100, 0})
			.put(398, new Integer[] {4212288, 50}).put(224, new Integer[] {4404769, 0}).put(116, new Integer[] {4404769, 0})
			.put(267, new Integer[] {65792, 0}).put(299, new Integer[] {2760198, 0}).build();
	private static final GameInterface[] WALKABLE_INTERFACES = new GameInterface[] {
			GameInterface.TELEPORTS,
			GameInterface.ADVANCED_SETTINGS,
			GameInterface.CREDIT_STORE,
			GameInterface.VOTE,
			GameInterface.TELEPORT_MENU,
			GameInterface.DROP_VIEWER
	};
	private static final Logger log = LoggerFactory.getLogger(InterfaceHandler.class);


	@Ordinal
	public enum Journal {
		CHARACTER_SUMMARY(0),
		QUEST_TAB(-1),
		ACHIEVEMENT_DIARIES(2),
		KOUREND(-1),
		ADVENTURE_PATHS(-1),
		GAME_NOTICEBOARD(3),
		SERVER_EVENTS(5),
		;
		public int id;
		Journal(int id){
			this.id = id;
		}
	}

	private final transient BiMap<Integer, Integer> visible;
	@Expose
	private PaneType pane;
	private Journal journal;
	@Expose
	private boolean resizable;
	private final transient Player player;

	public InterfaceHandler(final Player player) {
		this.player = player;
		//journal = Journal.QUEST_TAB;
		//TODO:
		journal = Journal.ACHIEVEMENT_DIARIES;
		visible = HashBiMap.create();
	}

	public final void initialize(final InterfaceHandler handler) {
		pane = handler.pane == null ? PaneType.RESIZABLE : handler.pane;
		resizable = handler.resizable;
		journal = handler.journal == null ? Journal.CHARACTER_SUMMARY : handler.journal;
		visible.put(pane.getId() << 16, pane.getId());
	}

	public void sendPane(final PaneType fromPane, final PaneType toPane) {
		player.getPacketDispatcher().sendPane(toPane);
		visible.remove(fromPane.getId() << 16);
		visible.forcePut(pane.getId() << 16, toPane.getId());
		int clientMode = -1;
		if (toPane == PaneType.FIXED) {
			clientMode = 0;
		} else if (toPane == PaneType.RESIZABLE) {
			clientMode = 1;
		} else if (toPane == PaneType.SIDE_PANELS) {
			clientMode = 2;
		}
		if (clientMode != -1) player.getPacketDispatcher().sendClientScript(3998, clientMode);
		final Int2IntOpenHashMap pairs = InterfacePosition.getPairs(fromPane, toPane);
		pairs.forEach((k, v) -> moveInterface(fromPane, k, toPane, v));
	}

	public void sendGameFrame() {
		final PaneType pane = resizable ? player.getSettings().isSidePanels()
				? PaneType.SIDE_PANELS : PaneType.RESIZABLE : PaneType.FIXED;
		final Map<Object, Object> map = player.getTemporaryAttributes();
		final PacketDispatcher dispatcher = player.getPacketDispatcher();
		final Object gameframe = map.get("gameframe_sent");
		if (gameframe != null) return;
		map.put("gameframe_sent", true);
		dispatcher.sendPane(pane);
		for (final InterfacePosition position : InterfacePosition.VALUES) {
			if (position.getGameframeInterfaceId() == -1 || position.equals(InterfacePosition.FRIENDS_TAB) || position.equals(InterfacePosition.JOURNAL_TAB_HEADER)) {
				continue;
			}
			final Optional<GameInterface> gameInter = GameInterface.get(position.getGameframeInterfaceId());
			if (gameInter.isPresent()) {
				gameInter.get().open(player);
			} else {
				sendInterface(position, position.getGameframeInterfaceId());
			}
		}
		player.getSettings().refreshSetting(Setting.FRIEND_LIST_TOGGLE);
		player.onLobbyClose();
		sendMisc();
		openJournal();
		ChatChannelPlayerExtKt.sendSocialTabs(player);
		GameInterface.GAME_NOTICEBOARD.open(player);
		GameInterface.GAME_SETTINGS.open(player);
		if (player.isOnMobile()) {
			player.getSettings().refreshSetting(Setting.MINIMIZE_MINIMAP);
		}
		PluginManager.post(new PostWindowStatusEvent(player));
	}

	public void openJournal() {
		GameInterface.JOURNAL_HEADER_TAB.open(player);
		switch (journal) {
			case QUEST_TAB:
				GameInterface.QUEST_TAB.open(player);
				break;
			case ACHIEVEMENT_DIARIES:
				GameInterface.ACHIEVEMENT_DIARY_TAB.open(player);
				break;
			case CHARACTER_SUMMARY:
				GameInterface.CHARACTER_SUMMARY.open(player);
				break;
			case KOUREND:
				GameInterface.KOUREND_FAVOUR_TAB.open(player);
				break;
			case SERVER_EVENTS:
				GameInterface.SERVER_EVENTS.open(player);
				break;
		}
	}

	public void sendWelcomeScreen() {
		final PacketDispatcher dispatcher = player.getPacketDispatcher();
		dispatcher.sendComponentText(378, 70, "You do not have a Bank PIN.<br>Please visit a bank if you would like one.");
		dispatcher.sendComponentText(378, 4, "<col=1f1fff>Cromperty</col> got a <col=003600>right-click option</col> to claim <col=ffffff>essence</col>,<br>and if you <col=ffff00>enable the ESC key</col> for closing menus,<br>it will work on the <col=3f007f>World Map</col> and <col=6f0000>this screen</col>.");
		dispatcher.sendClientScript(233, 24772660, 34024, 0, 0, 225, 1296, 0, 348, -1);
		dispatcher.sendClientScript(233, 24772661, 23446, 0, 50, 165, 1717, 0, 1116, -1);
		dispatcher.sendPane(PaneType.FULL_SCREEN);
		dispatcher.sendClientScript(2494, 1);
		sendInterface(InterfacePosition.CHATBOX, 162);
		sendInterface(InterfacePosition.PRIVATE_CHAT, 163);
		sendInterface(378, 27, PaneType.FULL_SCREEN, false);
		dispatcher.sendClientScript(1080, "");
		sendInterface(InterfacePosition.SKILLS_TAB, 320);
		dispatcher.sendComponentSettings(399, 7, 0, EnumDefinitions.get(1374).getSize(), AccessMask.CLICK_OP1);
		dispatcher.sendComponentSettings(399, 8, 0, EnumDefinitions.get(1375).getSize(), AccessMask.CLICK_OP1);
		dispatcher.sendComponentSettings(399, 9, 0, EnumDefinitions.get(1376).getSize(), AccessMask.CLICK_OP1);
		sendInterface(InterfacePosition.INVENTORY_TAB, 149);
		sendInterface(InterfacePosition.EQUIPMENT_TAB, 387);
		GameInterface.GAME_SETTINGS.open(player); //advanced settings tab
		sendInterface(InterfacePosition.PRAYER_TAB, 541);
		sendInterface(InterfacePosition.FRIENDS_TAB, 432);
		sendInterface(InterfacePosition.LOGOUT_TAB, 182);
		GameInterface.SPELLBOOK.open(player);
		GameInterface.SETTINGS.open(player);
		EMOTE_TAB.open(player);
		dispatcher.sendComponentSettings(216, 1, 0, 47, AccessMask.CLICK_OP1);
		//sendInterface(InterfacePosition.MUSIC_TAB, 239);
		//dispatcher.sendComponentSettings(239, 2, 0, 581, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2);
		GameInterface.MUSIC_TAB.open(player);
		//sendInterface(InterfacePosition.CLAN_TAB, 7);
		GameInterface.COMBAT_TAB.open(player);
		dispatcher.sendClientScript(2014, 0, 0, 0, 0, 0, 0);
		dispatcher.sendClientScript(2015, 0);
	}

	/**
	 * Second arg is the model id
	 */
	private void sendMisc() {
		if (player.getVarManager().getBitValue(SettingVariables.SHOW_DATA_ORBS_VARBIT_ID) == 0) {
			GameInterface.ORBS.open(player);
		}
		final PacketDispatcher dispatcher = player.getPacketDispatcher();
		dispatcher.sendComponentSettings(216, 1, 0, 47, AccessMask.CLICK_OP1);
		dispatcher.sendComponentSettings(261, 89, 1, 4, AccessMask.CLICK_OP1);
		dispatcher.sendComponentSettings(261, 90, 1, 4, AccessMask.CLICK_OP1);
		player.getVarManager().sendBit(4070, player.getCombatDefinitions().getSpellbook().ordinal());
	}

	public int getInterfaceComponent(final int interfaceId) {
		return visible.inverse().getOrDefault(interfaceId, -1) & 65535;
		// return visible.entrySet().stream().filter(e -> e.getValue() ==
		// interfaceId).map(Map.Entry::getKey).findAny().orElse(-1) & 0xFFFF;
	}

	public int getInterfacePane(final int interfaceId) {
		return visible.inverse().getOrDefault(interfaceId, -1) >> 16;
	}

	public int getInterface(final PaneType pane, final int paneComponent) {
		return visible.getOrDefault(pane.getId() << 16 | paneComponent, -1);
	}

	public boolean containsInterface(final InterfacePosition position) {
		if (position == InterfacePosition.CHATBOX || position == InterfacePosition.DIALOGUE) {
			return visible.containsKey(PaneType.CHATBOX.getId() << 16 | InterfacePosition.DIALOGUE.getComponent(PaneType.RESIZABLE));
		}
		return visible.containsKey(PaneType.FIXED.getId() << 16 | position.getFixedComponent()) || visible.containsKey(PaneType.SIDE_PANELS.getId() << 16 | position.getSidepanelsComponent()) || visible.containsKey(PaneType.RESIZABLE.getId() << 16 | position.getResizableComponent()) || visible.containsKey(PaneType.MOBILE.getId() << 16 | position.getMobileComponent());
	}

	public void sendInterface(final int interfaceId, final int paneComponent, final PaneType pane, final boolean walkable) {
		player.getPacketDispatcher().sendInterface(interfaceId, paneComponent, pane, walkable);
		visible.forcePut(pane.getId() << 16 | paneComponent, interfaceId);
	}

	public void closeInterfaceSpecific(final int paneComponent, final PaneType pane) {
		final boolean contains = visible.containsKey(pane.getId() << 16 | paneComponent);
		final boolean dialogue = pane == PaneType.CHATBOX;
		final int hash = (pane.getId() << 16) | paneComponent;
		closeInterface(hash, true, true, contains, dialogue, null);
	}

	public void sendInterface(final GameInterface gameInterface) {
		sendInterface(gameInterface.getPosition(), gameInterface.getId());
	}

	public void sendInterface(final Interface inter) {
		final GameInterface gameInterface = inter.getInterface();
		sendInterface(gameInterface.getPosition(), gameInterface.getId());
	}

	public void sendInterface(final InterfacePosition position, final int id) {
		sendInterface(position, id, position.isWalkable());
	}

	public void sendInterface(final InterfacePosition position, final int id, final boolean walkable) {
		if (!ComponentDefinitions.containsInterface(id)) {
			return;
		}
		if (position.equals(InterfacePosition.DIALOGUE)) {
			closeInterface(InterfacePosition.CENTRAL);
		} else if (position.equals(InterfacePosition.CENTRAL)) {
			closeInterface(InterfacePosition.DIALOGUE);
		}
		PaneType pane = position.equals(InterfacePosition.DIALOGUE) ? PaneType.CHATBOX : this.pane;
		if(pane == null)
			pane = PaneType.FIXED;//todo cheaphax fix find the root of this problem after launch - new players cannot open charachter design interface
		final PaneType paneToObtainComponentFrom = pane == PaneType.CHATBOX ? PaneType.RESIZABLE : pane;
		if (position.equals(InterfacePosition.CENTRAL)) {
			player.getPacketDispatcher().sendClientScript(2524, ArrayUtils.contains(EXPANDED_INTERFACES, id) ? EXPANDED_ARGS : DEFAULT_ARGS);
			if (BACKGROUND_SCRIPT_ARGS.containsKey(id)) {
				Object[] ints = BACKGROUND_SCRIPT_ARGS.get(id);
				player.getPacketDispatcher().sendClientScript(917, ints);
			}
		}
		final Optional<GameInterface> current = GameInterface.get(id);
		if (current.isPresent() && GameInterface.TOA_REWARD_POTENTIAL.equals(current.get())) {
			pane = PaneType.TOA_MANAGEMENT;
		}
		final Integer previous = visible.forcePut(pane.getId() << 16 | position.getComponent(paneToObtainComponentFrom), id);
		if (previous != null) {
			GameInterface.get(previous).ifPresent(gameInterface -> closePlugin(player, gameInterface, current));
		}
		player.getPacketDispatcher().sendInterface(id, position.getComponent(paneToObtainComponentFrom), pane, walkable);
	}

	public void closeInterface(final InterfacePosition position, final boolean removeFromMap, final boolean closeEvent, final boolean combat) {
		final boolean contains = containsInterface(position);
		final boolean dialogue = position.equals(InterfacePosition.DIALOGUE);
		final PaneType pane = dialogue ? PaneType.RESIZABLE
				: (position.equals(InterfacePosition.TOA_MANAGEMENT)
				? PaneType.TOA_MANAGEMENT
				: this.pane);
		final int hash = (dialogue
				? PaneType.CHATBOX.getId()
				: pane == null
				? 0
				: pane.getId()) << 16 | position.getComponent(pane);
		if (combat && contains) {
			try {
				int id = visible.get(hash);
				if (id == 0) return;
				final Interface plugin = NewInterfaceHandler.getInterface(id);
				if (plugin != null && !plugin.closeInCombat()) {
					return;
				}
			} catch (Throwable e) {
				log.error("Error closing interface in combat {} {} {} {} for {} ({})", dialogue, position, pane,  hash, player, e.getLocalizedMessage());
			}
		}

		closeInterface(hash, removeFromMap, closeEvent, contains, dialogue, position);
	}

	private void closeInterface(int hash,
								final boolean removeFromMap,
								final boolean closeEvent,
								boolean contains,
								boolean dialogue,
								InterfacePosition position) {
		closeInput();
		Integer previous = null;
		if (removeFromMap) {
			previous = visible.remove(hash);
		}
		if (!contains) {
			return;
		}

		player.getPacketDispatcher().closeInterface(hash);
		if (dialogue) {
			final Dialogue dial = player.getDialogueManager().getLastDialogue();
			if (dial != null) {
				if (dial.getNpc() != null) {
					dial.getNpc().finishInteractingWith(player);
				}
			}
		}
		if (previous != null) {
			final Optional<GameInterface> prevGameInterface = GameInterface.get(previous);
			prevGameInterface.ifPresent(gameInterface -> closePlugin(player, gameInterface, Optional.empty()));
		}
		if (closeEvent && (position == null || position.equals(InterfacePosition.CENTRAL))) {
			final Runnable runnable = player.getCloseInterfacesEvent();
			if (runnable == null) {
				return;
			}
			runnable.run();
			player.setCloseInterfacesEvent(null);
		}
	}

	private void closePlugin(@NotNull final Player player, @NotNull final GameInterface inter, @NotNull final Optional<GameInterface> replacement) {
		player.log(LogLevel.INFO, "Closing interface: " + inter + ", replacement: " + replacement);
		final Interface plugin = NewInterfaceHandler.getInterface(inter.getId());
		if (plugin != null) {
			plugin.close(player, replacement);
		}
	}

	public void closeInterface(final InterfacePosition position) {
		closeInterface(position, false);
	}

	public void closeInterface(final InterfacePosition position, final boolean combat) {
		closeInterface(position, true, true, combat);
	}

	public void closeInterface(final Interface inter) {
		final GameInterface gameInterface = inter.getInterface();
		closeInterface(gameInterface.getPosition());
	}

	public void closeInterface(final GameInterface gameInterface) {
		this.closeInterface(gameInterface.getPosition());
	}

	public void closeInterface(final int interfaceId) {
		final int componentId = getInterfaceComponent(interfaceId);
		final InterfacePosition position = InterfacePosition.getPosition(componentId, pane);
		if (position == null) {
			return;
		}
		closeInterface(position, true, true, false);
	}

	public void closeInterfaces() {
		closeInterfaces(false);
	}

	public void closeInterfaces(boolean combat) {
		closeInterface(InterfacePosition.CENTRAL, combat);
		closeInterface(InterfacePosition.SINGLE_TAB, combat);
		closeInterface(InterfacePosition.DIALOGUE, combat);
		if (this.isPresent(GameInterface.ADVANCED_SETTINGS)) closeInterface(InterfacePosition.WORLD_MAP, combat);
	}

	public boolean isPresent(final GameInterface inter) {
		return this.isVisible(inter.getId());
	}

	public boolean isWalkablePresent() {
		boolean hasWalkable = false;
		for(GameInterface inter: WALKABLE_INTERFACES) {
			if(this.isVisible(inter.getId()))
				hasWalkable = true;
		}
		if(this.isVisible(119))
			hasWalkable = true;
		return hasWalkable;
	}

	public void closeInput() {
		closeInput(false);
	}

	public void closeInput(boolean buttonClick) {
		final Map<Object, Object> attributes = player.getTemporaryAttributes();
		if (buttonClick && attributes.containsKey("interfaceInputNoCloseOnButton")) {
			return;
		}

		final Object input = attributes.get("interfaceInput");
		if (input != null) {
			forceCloseInput();
			attributes.remove("interfaceInput");
			attributes.remove("interfaceInputNoCloseOnButton");
		}
	}

	public void forceCloseInput() {
		player.getPacketDispatcher().sendClientScript(299, 1, 1);
	}

	private void moveInterface(final PaneType fromPane, final int fromComponent, final PaneType toPane, final int toComponent) {
		if (isVisible(214)) {
			closeInterface(InterfacePosition.CENTRAL);
		}
		final int interfaceId = getInterface(fromPane, fromComponent);
		player.getPacketDispatcher().sendMoveInterface(fromPane.getId(), fromComponent, toPane.getId(), toComponent);
		visible.remove(fromPane.getId() << 16 | fromComponent);
		visible.forcePut(toPane.getId() << 16 | toComponent, interfaceId);
	}

	public boolean isVisible(final int id) {
		if (pane != null && pane.getId() == id) {
			return true;
		}
		return visible.inverse().containsKey(id);
	}

	public boolean isVisible(final int pane, final int paneComponent) {
		return visible.containsKey(pane << 16 | paneComponent);
	}

	public void openGameTab(final GameTab tab) {
		player.getPacketDispatcher().sendClientScript(915, tab.getId());
	}

	public void setPane(final PaneType pane) {
		this.pane = pane;
		visible.forcePut(-1, pane.getId());
	}

	public BiMap<Integer, Integer> getVisible() {
		return visible;
	}

	public PaneType getPane() {
		return pane;
	}

	public Journal getJournal() {
		return journal;
	}

	public void setJournal(final Journal journal) {
		this.journal = journal;
		VarCollection.ACTIVE_JOURNAL.updateSingle(player);
		switch (journal) {
			case CHARACTER_SUMMARY -> GameInterface.CHARACTER_SUMMARY.open(player);
			case QUEST_TAB -> GameInterface.QUEST_TAB.open(player);
			case ACHIEVEMENT_DIARIES -> GameInterface.ACHIEVEMENT_DIARY_TAB.open(player);
			case GAME_NOTICEBOARD -> GameInterface.GAME_NOTICEBOARD.open(player);
			case SERVER_EVENTS -> GameInterface.SERVER_EVENTS.open(player);
		}
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}
}
