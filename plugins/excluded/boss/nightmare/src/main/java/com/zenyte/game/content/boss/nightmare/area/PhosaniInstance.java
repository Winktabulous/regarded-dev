package com.zenyte.game.content.boss.nightmare.area;

import com.near_reality.game.world.entity.player.PlayerDeathHandlerKt;
import com.zenyte.game.GameConstants;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.content.boss.nightmare.*;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.EdgevilleArea;
import com.zenyte.game.world.region.area.plugins.*;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.utils.MapLocations;

public class PhosaniInstance extends DynamicArea implements DeathPlugin, CannonRestrictionPlugin, LootBroadcastPlugin {

	private final Player player;
	private WorldTask wakeUpPulse;
	private BaseNightmareNPC boss;
	private int instanceKc;

	public PhosaniInstance(final AllocatedArea allocatedArea, final Player player) {
		super(allocatedArea, 480, 1240);
		this.player = player;
	}

	@Override
	public void constructed() {
		checkWakePulse(20);

		boss = new PhosanisNightmareNPC(player, this, getLocation(BaseNightmareNPC.CENTER));
		boss.spawn();

		NightmareTotemNE totemNE = new NightmareTotemNE(boss, getLocation(NightmareTotemNE.SPAWN_LOCATION));
		totemNE.spawn();

		NightmareTotemNW totemNW = new NightmareTotemNW(boss, getLocation(NightmareTotemNW.SPAWN_LOCATION));
		totemNW.spawn();

		NightmareTotemSE totemSE = new NightmareTotemSE(boss, getLocation(NightmareTotemSE.SPAWN_LOCATION));
		totemSE.spawn();

		NightmareTotemSW totemSW = new NightmareTotemSW(boss, getLocation(NightmareTotemSW.SPAWN_LOCATION));
		totemSW.spawn();

		boss.initTotems(totemNE, totemNW, totemSE, totemSW);
	}

	@Override
	public void enter(Player player) {
		NightmareBossArea.enterStatic(player);
	}

	@Override
	public void leave(Player player, boolean logout) {
		if (!logout) {
			NightmareBossArea.leaveStatic(player);
		} else {
			player.setLocation(new Location(3090, 3492));
		}
	}

	@Override
	public void destroyRegion() {
		super.destroyRegion();

		if (wakeUpPulse != null) {
			wakeUpPulse.stop();
			wakeUpPulse = null;
		}
	}

	@Override
	public String name() {
		return "Phosanis Nightmare";
	}

	@Override
	public boolean isSafe() {
		return false;
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
		PlayerDeathHandlerKt.sendDeath(player, source, () -> {
			player.getDeathMechanics().service(ItemRetrievalService.RetrievalServiceType.PHOSANI_NIGHTMARE, source, true);
			player.sendMessage("Sister Senga has retrieved some of your items. You can collect them from her in the Sisterhood Sanctuary.");
			ItemRetrievalService.updateVarps(player);
			NightmareGlobal.updatePhosanisDeathStatistics();
			player.incrementNumericAttribute("phosanis_nightmare_death", 1);
			return null;
		});
		return true;
	}

	public void checkWakePulse(int initialTicks) {
		if (wakeUpPulse != null) {
			return;
		}

		wakeUpPulse = new WorldTask() {
			int ticks = initialTicks;
			@Override
			public void run() {
				switch (ticks) {
					case 17 -> player.sendMessage("The Nightmare will awaken in 10 seconds!");
					case 9 -> player.sendMessage("The Nightmare will awaken in 5 seconds!");
					case 0 -> {
						player.sendMessage("<col=E00A19>The Nightmare has awakened!</col>");
						boss.awaken();
						stop();
						wakeUpPulse = null;
						return;
					}
				}

				ticks--;
			}
		};

		WorldTasksManager.schedule(wakeUpPulse, 0, 0);
	}

	@Override
	public boolean isMultiwayArea(Position position) {
		return true;
	}

	public int getInstanceKc() {
		return instanceKc;
	}

	public void addInstanceKc() {
		this.instanceKc++;
	}

}
