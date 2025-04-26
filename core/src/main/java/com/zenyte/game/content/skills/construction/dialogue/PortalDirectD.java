package com.zenyte.game.content.skills.construction.dialogue;

import com.zenyte.game.content.skills.construction.FurnitureData;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.TeleportPortal;
import com.zenyte.game.content.skills.construction.constants.Furniture;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionsMenuD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Kris | 29. nov 2017 : 23:04.53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class PortalDirectD extends OptionsMenuD {
	private static final Logger log = LoggerFactory.getLogger(PortalDirectD.class);
	private static final Animation ANIM = new Animation(718);

	public PortalDirectD(Player player, String title, String[] options) {
		super(player, title, options);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleClick(int slotId) {
		player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
		RoomReference room = null;
		int index = 0;
		List<FurnitureData> furniture = null;
		try {
			room = (RoomReference) player.getTemporaryAttributes().remove("portalroom");
			index = (int) player.getTemporaryAttributes().remove("portaldirect");
			furniture = (List<FurnitureData>) player.getTemporaryAttributes().remove("portalfurn");
			final FurnitureData data = furniture.get(index);
			Furniture furn = null;
			if (data.getFurniture().getObjectId() == 13636 || data.getFurniture().ordinal() >= Furniture.TEAK_VARROCK_PORTAL.ordinal() && data.getFurniture().ordinal() <= Furniture.TEAK_KOUREND_PORTAL.ordinal()) furn = Furniture.VALUES[Furniture.TEAK_VARROCK_PORTAL.ordinal() + slotId];
			 else if (data.getFurniture().getObjectId() == 13637 || data.getFurniture().ordinal() >= Furniture.MAHOGANY_VARROCK_PORTAL.ordinal() && data.getFurniture().ordinal() <= Furniture.MAHOGANY_KOUREND_PORTAL.ordinal()) furn = Furniture.VALUES[Furniture.MAHOGANY_VARROCK_PORTAL.ordinal() + slotId];
			 else if (data.getFurniture().getObjectId() == 13638 || data.getFurniture().ordinal() >= Furniture.MARBLE_VARROCK_PORTAL.ordinal() && data.getFurniture().ordinal() <= Furniture.MARBLE_KOUREND_PORTAL.ordinal()) furn = Furniture.VALUES[Furniture.MARBLE_VARROCK_PORTAL.ordinal() + slotId];
			if (furn == null) return;
			final TeleportPortal portal = TeleportPortal.MAP.get(furn.getObjectId());
			if (portal == null) return;
			final Item[] teleportRunes = portal.getTeleport().getRunes();
			final Item[] runes = new Item[teleportRunes.length];
			int idx = 0;
			for (Item teleportRune : teleportRunes) {
				if (teleportRune.getId() == 1963) runes[idx++] = new Item(1964, teleportRune.getAmount() * 100);
				 else runes[idx++] = new Item(teleportRune.getId(), teleportRune.getAmount() * 100);
			}
			if (player.getSkills().getLevel(SkillConstants.MAGIC) < portal.getTeleport().getLevel() || !player.getInventory().containsItems(runes)) {
				final StringBuilder builder = new StringBuilder();
				for (Item i : runes) builder.append(i.getAmount() + " " + i.getName().toLowerCase() + "s, ");
				builder.delete(builder.length() - 2, builder.length());
				player.sendMessage("You need " + builder + " and " + portal.getTeleport().getLevel() + " Magic to direct the portal there.");
				return;
			}
			data.setFurniture(furn);
			final int x = (room.getX() * 8 - (player.getConstruction().getYardOffset() * 8)) % 64;
			final int y = (room.getY() * 8 - (player.getConstruction().getYardOffset() * 8)) % 64;
			final Location tile = new Location(player.getConstruction().getChunkX() * 8 + x + data.getLocation().getX(), player.getConstruction().getChunkY() * 8 + y + data.getLocation().getY(), player.getPlane());
			player.setAnimation(ANIM);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					World.spawnObject(new WorldObject(data.getFurniture().getObjectId(), 10, data.getRotation(), tile));
				}
			});
		} catch (Exception e) {
			log.error("", e);
		}
	}

	@Override
	public boolean cancelOption() {
		return false;
	}
}
