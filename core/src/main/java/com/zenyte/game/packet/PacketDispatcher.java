package com.zenyte.game.packet;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.grandexchange.ExchangeOffer;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.model.LineSpacingType;
import com.zenyte.game.model.MinimapState;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.packet.out.*;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.MaskBuilder;
import com.zenyte.game.util.RSColour;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.MessageType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ItemContainer;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.game.world.GraphicsObjectRS;
import com.zenyte.net.Session;
import mgi.types.component.ComponentDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for sending packets.
 *
 * @author Graham Edgecombe
 * @author Tom - modifications and additions.
 */
public class PacketDispatcher {
	/**
	 * The private player object used for creating the constructor.
	 */
	private final Player player;

	private final RebuildNormal rebuildNormal;
	private final RebuildRegion rebuildRegion;

	/**
	 * Creates an action sender for the specified player.
	 *
	 * @param player
	 *            The player to create the action sender for.
	 */
	public PacketDispatcher(final Player player) {
		this.player = player;
		rebuildNormal = new RebuildNormal(player, false);
		rebuildRegion = new RebuildRegion(player);
	}

	public void sendLogout() {
		final Session session = player.getSession();
		final Logout packet = new Logout();
		session.send(packet);
		packet.log(player);
	}

	public void sendURL(final String link) {
		player.send(new OpenUrl(link));
	}

	public void sendPane(final PaneType pane) {
		player.send(new IfOpenTop(pane));
		player.getInterfaceHandler().setPane(pane);
	}

	public void sendProjectile(final Position sender, final Position receiver, final Projectile projectile, final int speed, final int offset) {
		player.addProj(sender.getPosition(), new MapProjAnim(player, sender, receiver, projectile, speed, offset));
		//player.sendZoneUpdate(sender.getX(), sender.getY(), new MapProjAnim(player, sender, receiver, projectile,
		//        speed, offset));
	}

	public void sendInterface(final int interfaceId, final int targetChild, final PaneType pane, final boolean walkable) {
		player.send(new IfOpenSub(interfaceId, targetChild, pane, walkable));
	}

	public void sendMoveInterface(final int fromPane, final int fromChild, final int toPane, final int toChild) {
		player.send(new IfMoveSub(fromPane, fromChild, toPane, toChild));
	}

	public void closeInterface(final int hash) {
		player.send(new IfCloseSub(hash));
	}

	public void sendLineSpacing(@NotNull final LineSpacingType horizontalType, @NotNull final LineSpacingType verticalType, final int interfaceId, final int componentId) {
		sendLineSpacing(horizontalType, verticalType, 0, interfaceId, componentId);
	}

	public void sendLineSpacing(@NotNull final LineSpacingType horizontalType, @NotNull final LineSpacingType verticalType, final int lineSpacing, final int interfaceId, final int componentId) {
		player.getPacketDispatcher().sendClientScript(600, horizontalType.ordinal(), verticalType.ordinal(), lineSpacing, (interfaceId << 16) | componentId);
	}

	public void sendMessage(final String message, final MessageType type, final String extension) {
		if (message == null || message.isEmpty()) {
			return;
		}
		player.send(new MessageGame(message, type, extension));
	}

	public void sendGameMessage(final String message, final MessageType type) {
		sendMessage(message, type, null);
	}

	public void sendGameMessage(final String message, final boolean filterable) {
		sendMessage(message, filterable ? MessageType.FILTERABLE : MessageType.UNFILTERABLE, null);
	}

	public void sendGameMessage(final String message, final boolean filterable, final Object... params) {
		sendMessage(params.length > 0 ? String.format(message, params) : message, filterable ? MessageType.FILTERABLE : MessageType.UNFILTERABLE, null);
	}

	public void sendTradeRequest(final String message, final String user) {
		sendMessage(message, MessageType.TRADE_REQUEST, user);
	}

	public void sendChallengeRequest(final String message, final String user) {
		sendMessage(message, MessageType.CHALLENGE_REQUEST, user);
	}

	public void sendGlobalBroadcast(final String message) {
		sendMessage(message, MessageType.GLOBAL_BROADCAST, null);
	}

	public void sendRunEnergy() {
		player.send(new UpdateRunEnergy((int) player.getVariables().getRunEnergy()));
	}

	public void sendWeight() {
		player.send(new UpdateRunWeight(player));
	}

	public void sendSkillUpdate(final int skill) {
		player.send(new UpdateStat(skill, player.getSkills().getExperience(skill), player.getSkills().getLevel(skill)));
	}

	public void sendSkillUpdateForce(final int skill, int level) {
		player.send(new UpdateStat(skill, player.getSkills().getExperience(skill), level));
	}

	public void initFriendsList() {
		final List<String> friends = player.getSocialManager().getFriends();
		final ArrayList<UpdateFriendList.FriendEntry> list = new ArrayList<UpdateFriendList.FriendEntry>(friends.size());
		for (final String friend : friends) {
			list.add(new UpdateFriendList.FriendEntry(friend, false));
		}
		player.send(new UpdateFriendList(player, list));
	}

	public void initIgnoreList() {
		final List<String> ignores = player.getSocialManager().getIgnores();
		final ArrayList<UpdateIgnoreList.IgnoreEntry> list = new ArrayList<UpdateIgnoreList.IgnoreEntry>(ignores.size());
		for (final String ignore : ignores) {
			list.add(new UpdateIgnoreList.IgnoreEntry(ignore, false));
		}
		player.send(new UpdateIgnoreList(list));
	}

	public void sendStaticMapRegion() {
		player.send(rebuildNormal);
	}

	public void sendDynamicMapRegion() {
		player.send(rebuildRegion);
	}

	public void sendComponentVisibility(final int interfaceId, final int componentId, final boolean hidden) {
		player.send(new IfSetHide(interfaceId, componentId, hidden));
	}

	public void sendComponentVisibility(final GameInterface inter, final int componentId, final boolean hidden) {
		player.send(new IfSetHide(inter.getId(), componentId, hidden));
	}

	public void sendComponentSettings(final int interfaceId, final int componentId, final int start, final int end, final int set) {
		player.send(new IfSetEvents(interfaceId, componentId, start, end, set));
	}

	public void sendComponentSettings(final int interfaceId, final int componentId, final int start, final int end, final AccessMask... masks) {
		player.send(new IfSetEvents(interfaceId, componentId, start, end, MaskBuilder.getValue(masks)));
	}

	public void sendComponentSettings(GameInterface gameInterface, final int componentId, final int start, final int end, final AccessMask... masks) {
		player.send(new IfSetEvents(gameInterface.getId(), componentId, start, end, MaskBuilder.getValue(masks)));
	}

	public void sendComponentSettings(final int interfaceId, final int componentId, final int start, final int end, final MaskBuilder builder) {
		player.send(new IfSetEvents(interfaceId, componentId, start, end, builder.getValue()));
	}

	public void sendComponentText(final int interfaceId, final int componentId, final Object text) {
		if (!ComponentDefinitions.containsInterface(interfaceId)) {
			return;
		}
		player.send(new IfSetText(interfaceId, componentId, text == null ? "null" : text.toString()));
	}

	public void sendComponentText(final GameInterface gameInterface, final int componentId, final Object text) {
		sendComponentText(gameInterface.getId(), componentId, text);
	}

	public void sendComponentItem(GameInterface gameInterface, final int componentId, final int itemId, final int zoom) {
		sendComponentItem(gameInterface.getId(), componentId, itemId, zoom);
	}

	public void sendComponentItem(final int interfaceId, final int componentId, final int itemId, final int zoom) {
		player.send(new IfSetObject(interfaceId, componentId, itemId, zoom));
	}

	public void sendComponentPlayerHead(final int interfaceId, final int componentId) {
		player.send(new IfSetPlayerHead(interfaceId, componentId));
	}

	public void sendComponentNPCHead(final int interfaceId, final int componentId, final int npcId) {
		player.send(new IfSetNpcHead(interfaceId, componentId, npcId));
	}

	public void sendComponentModel(final GameInterface inter, final int componentId, final int modelId) {
		player.send(new IfSetModel(inter.getId(), componentId, modelId));
	}

	public void sendComponentModel(final int interfaceId, final int componentId, final int modelId) {
		player.send(new IfSetModel(interfaceId, componentId, modelId));
	}

	public void sendComponentAnimation(final int interfaceId, final int componentId, final int animationId) {
		player.send(new IfSetAnim(interfaceId, componentId, animationId));
	}

	public void sendComponentAnimation(final GameInterface inter, final int componentId, final int animationId) {
		player.send(new IfSetAnim(inter.getId(), componentId, animationId));
	}

	public void sendComponentSpriteColour(final int interfaceId, final int componentId, final RSColour colour) {
		if (!ComponentDefinitions.containsInterface(interfaceId)) {
			return;
		}
		player.send(new IfSetColour(interfaceId, componentId, colour));
	}

	public void sendClientScript(final int scriptId, final Object... arguments) {
		player.send(new RunClientScript(scriptId, false, arguments));
	}

	public void sendObjAdd(final FloorItem item, final Location tile) {
		player.sendZoneUpdate(tile.getX(), tile.getY(), new ObjAdd(item));
	}

	public void sendObjDel(final FloorItem item, final Location tile) {
		player.sendZoneUpdate(tile.getX(), tile.getY(), new ObjDel(item));
	}

	public void sendObjUpdate(final FloorItem item, final int oldQuantity, final Location tile) {
		player.sendZoneUpdate(tile.getX(), tile.getY(), new ObjUpdate(item, oldQuantity));
	}

	public void sendGraphicsObject(final GraphicsObjectRS object) {
		player.send(new GraphicsObjectSend(player, object));
	}

	public void sendClientScript(final int scriptId) {
		player.send(new RunClientScript(scriptId, true));
	}

	public void sendConfig(final int config, final int value) {
		player.send(value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE ? new VarpSmall(config, value) : new VarpLarge(config, value));
	}

	public void sendPlayerOption(final int index, final String option, final boolean top) {
		player.send(new SetPlayerOp(player, index, option, top));
	}

	public void sendHintArrow(final HintArrow arrow) {
		player.send(new SetHintArrow(arrow));
		player.getTemporaryAttributes().put("last hint arrow", arrow);
	}

	public void resetHintArrow() {
		player.send(new SetHintArrow(null));
		player.getTemporaryAttributes().remove("last hint arrow");
	}

	public void sendFriendServer() {
		player.send(new FriendListLoaded());
	}

	public void sendGrandExchangeOffer(final ExchangeOffer offer) {
		player.send(new GrandExchangeOffer(offer));
	}

	public void sendUpdateItemContainer(final Container container) {
		player.send(new UpdateInvFull(container));
	}

	public void sendUpdateItemContainer(final Container container, final ContainerType type) {
		player.send(new UpdateInvFull(container, type));
	}

	public void sendUpdateItemsPartial(final Container items) {
		player.send(new UpdateInvPartial(items));
	}

	public void sendUpdateItemContainer(final int key, final ItemContainer items) {
		sendUpdateItemContainer(key, -1, 0, items);
	}

	public void sendClearItemContainer(final int interfaceId, final int componentId) {
		player.send(new IfClearItems(interfaceId, componentId));
	}

	public void sendUpdateItemContainer(final int key, final int interfaceId, final int componentId, final ItemContainer items) {
		player.send(new UpdateInvFull(key, interfaceId, componentId, items));
	}

	public void sendUpdateItemContainer(final int key, final int interfaceId, final int componentId, final Container items) {
		player.send(new UpdateInvFull(key, interfaceId, componentId, items));
	}

	public void sendUpdateItemsInSlot(final int key, final int interfaceId, final int componentId, final ItemContainer items, final int... slots) {
		player.send(new UpdateInvPartial(key, interfaceId, componentId, items, slots));
	}

	public void sendGraphics(final Graphics graphics, final Location location) {
		player.sendZoneUpdate(location.getX(), location.getY(), new MapAnim(location, graphics));
	}

	public void sendSoundEffect(final SoundEffect sound) {
		player.send(new SynthSound(sound));
	}

	public void sendAreaSoundEffect(final Location tile, final SoundEffect sound) {
		player.sendZoneUpdate(tile.getX(), tile.getY(), new AreaSound(player, tile, sound));
	}

	public void sendMusic(final int song) {
		player.send(new MidiSong(song));
	}

	public void playJingle(final int song) {
		player.send(new MidiJingle(song));
	}

	public void sendMapFlag(final int x, final int y) {
		player.send(new SetMapFlag(x, y));
	}

	public void resetMapFlag() {
		sendMapFlag(255, 255);
	}

	public void sendCameraLook(final int viewLocalX, final int viewLocalY, final int cameraHeight, final int speed, final int acceleration) {
		player.send(new CamLookAt(viewLocalX, viewLocalY, cameraHeight, speed, acceleration));
	}

	public void sendCameraPosition(final int viewLocalX, final int viewLocalY, final int cameraHeight, final int speed, final int acceleration) {
		player.send(new CamMoveTo(viewLocalX, viewLocalY, cameraHeight, speed, acceleration));
	}

	public void sendCameraShake(final CameraShakeType type, final int shakeIntensity, final int movementIntensity, final int speed) {
		player.getTemporaryAttributes().put("cameraShake", true);
		player.send(new CamShake(type, shakeIntensity, movementIntensity, speed));
	}

	public void resetCamera() {
		player.getTemporaryAttributes().remove("cameraShake");
		player.send(new CamReset());
	}

	public void sendMinimapState(final MinimapState state) {
		player.send(new MinimapToggle(state));
	}
}
