package com.zenyte.game.content.rots;

import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.content.minigame.barrows.BarrowsWight;
import com.zenyte.game.content.rots.npc.*;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.RunePouch;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.TeleportPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.plugins.item.mysteryboxes.MysteryItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zenyte.game.world.entity.player.Player.DEATH_ANIMATION;

public class RotsInstance extends DynamicArea implements TeleportPlugin, DeathPlugin {

	private final Player player;
	private final List<RotsBrother> brothers = new ArrayList<>();
	private boolean entered;
	private boolean completed;
	private boolean looted;
	private Container container;

	protected RotsInstance(final AllocatedArea allocatedArea, final Player player) {
		super(allocatedArea, 432, 680);
		this.container = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.BARROWS_CHEST, Optional.empty());
		this.player = player;
	}

	public void spawnBrothers() {
		entered = true;
		WorldTasksManager.schedule(() -> {
			RotsBrother dharok = (RotsBrother) World.spawnNPC(new DharokTheWretchedRots(getBaseLocation(43, 22), this));
			dharok.getCombat().setCombatDelay(1);
			RotsBrother verac = (RotsBrother) World.spawnNPC(new VeracTheDefiledRots(getBaseLocation(20, 22), this));
			verac.getCombat().setCombatDelay(2);
			RotsBrother ahrim = (RotsBrother) World.spawnNPC(new AhrimTheBlightedRots(getBaseLocation(31, 22), this));
			ahrim.getCombat().setCombatDelay(3);
			RotsBrother torag = (RotsBrother) World.spawnNPC(new ToragTheCorruptedRots(getBaseLocation(23, 17), this));
			torag.getCombat().setCombatDelay(4);
			RotsBrother karil = (RotsBrother) World.spawnNPC(new KarilTheTaintedRots(getBaseLocation(31, 17), this));
			karil.getCombat().setCombatDelay(5);
			RotsBrother guthan = (RotsBrother) World.spawnNPC(new GuthanTheInfestedRots(getBaseLocation(40, 17), this));
			guthan.getCombat().setCombatDelay(6);
			brothers.add(dharok);
			brothers.add(verac);
			brothers.add(ahrim);
			brothers.add(torag);
			brothers.add(karil);
			brothers.add(guthan);
			NPC randomBrother = Utils.random(brothers);
			randomBrother.setForceTalk("You dare disturb the darkness!");
			player.getBossTimer().startTracking("rise of the six");
		}, 10);
	}

	public void generateRewards() {
		container.add(new Item(32185, Utils.random(1, 3)));
		if (Utils.randomBoolean(400)) {
			Item item;
			switch (Utils.random(2)) {
				case 0:
					item = new Item(32192);
					break;
				case 1:
					item = new Item(32195);
					break;
				default:
					item = new Item(32198);
					break;
			}

			container.add(item);
			WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, item, "Rise of the Six");
		} else if (Utils.randomBoolean()) {
			container.add(Utils.random(BarrowsWight.ALL_WIGHT_EQUIPMENT));
		} else {
			for (int i = 0; i < 6; i++) {
				MysteryItem mysteryItem = Utils.random(RotsRewards.supplies);
				container.add(new Item(mysteryItem.getId(), Utils.random(mysteryItem.getMinAmount(), mysteryItem.getMaxAmount())));
			}
		}
	}

	public void addLoot() {
		if (container.isEmpty()) return;
		final Container inventory = player.getInventory().getContainer();
		container.getItems().int2ObjectEntrySet().fastForEach(entry -> {
			player.getCollectionLog().add(entry.getValue());
			final RunePouch runePouch = player.getRunePouch();
			final int amountInRunePouch = runePouch.getAmountOf(entry.getValue().getId());
			final boolean addToRunePouch = player.getInventory().containsItem(12791, 1) && amountInRunePouch > 0 && (amountInRunePouch + entry.getValue().getAmount()) < 16000;
			final boolean addToQuiver = (player.getEquipment().getId(EquipmentSlot.AMMUNITION) == entry.getValue().getId() || (entry.getValue().isStackable() && player.getEquipment().getId(EquipmentSlot.WEAPON) == entry.getValue().getId()));
			final Container container = addToQuiver ? player.getEquipment().getContainer() : addToRunePouch ? runePouch.getContainer() : inventory;
			container.add(entry.getValue()).onFailure(remainder -> World.spawnFloorItem(remainder, player));
		});
		player.getRunePouch().getContainer().refresh(player);
		player.getEquipment().getContainer().refresh(player);
		inventory.refresh(player);
		container.refresh(player);
		container.clear();
	}

	public Container getContainer() {
		return container;
	}

	public boolean isEntered() {
		return entered;
	}

	public void setCompleted() {
		if (completed) {
			return;
		}

		brothers.clear();
		generateRewards();
		completed = true;
		player.getBossTimer().inform("rise of the six", System.currentTimeMillis() - player.getBossTimer().getCurrentTracker(), "Encounter");
		player.getNotificationSettings().increaseKill("rise of the six");
		player.getNotificationSettings().sendBossKillCountNotification("rise of the six");
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setLooted(boolean looted) {
		this.looted = looted;
	}

	public boolean isLooted() {
		return looted;
	}

	@Override
	public void constructed() {
	}

	@Override
	public void enter(Player player) {

	}

	@Override
	public void leave(Player player, boolean logout) {

	}

	@Override
	public String name() {
		return "Rise of the Six";
	}

	@Override
	public boolean isMultiwayArea(Position position) {
		return true;
	}

	public List<RotsBrother> getBrothers() {
		return brothers;
	}

	@Override
	public boolean canTeleport(Player player, Teleport teleport) {
		if (entered && !completed) {
			player.sendMessage("You can't teleport out when you've entered the encounter.");
			return false;
		}

		return true;
	}

	@Override
	public boolean isSafe() {
		return true;
	}

	@Override
	public String getDeathInformation() {
		return null;
	}

	@Override
	public Location getRespawnLocation() {
		return null;
	}

	@Override
	public boolean sendDeath(Player player, Entity source) {
		player.setAnimation(Animation.STOP);
		player.lock();
		player.stopAll();
		if (player.getPrayerManager().isActive(Prayer.RETRIBUTION)) {
			player.getPrayerManager().applyRetributionEffect(source);
		}
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;
			@Override
			public void run() {
				if (player.isFinished() || player.isNulled()) {
					stop();
					return;
				}
				if (ticks == 0) {
					player.setAnimation(DEATH_ANIMATION);
				} else if (ticks == 2) {
					player.getDeathMechanics().service(ItemRetrievalService.RetrievalServiceType.ROTS, source, true);
					player.sendMessage("Oh dear, you have died.");
					player.reset();
					player.setAnimation(Animation.STOP);
					//player.sendMessage("Strange Old Man has retrieved some of your items. You can collect them from him on the Barrows hills.");
					//ItemRetrievalService.updateVarps(player);
					if (player.getVariables().isSkulled()) {
						player.getVariables().setSkull(false);
					}
					player.blockIncomingHits();
					player.setLocation(player.getRespawnPoint().getLocation());
				} else if (ticks == 3) {
					player.unlock();
					player.getAppearance().resetRenderAnimation();
					player.setAnimation(Animation.STOP);
					stop();
				}
				ticks++;
			}
		}, 0, 1);
		return true;
	}

}
