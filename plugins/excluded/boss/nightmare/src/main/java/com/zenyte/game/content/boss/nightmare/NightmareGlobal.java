package com.zenyte.game.content.boss.nightmare;

import com.google.common.eventbus.Subscribe;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.content.boss.nightmare.area.NightmareBossArea;
import com.zenyte.game.content.boss.nightmare.area.NightmareLobbyArea;
import com.zenyte.game.content.boss.nightmare.object.PoolOfNightmares;
import com.zenyte.game.world.entity.player.GameCommands;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.plugins.events.ServerLaunchEvent;

public class NightmareGlobal {

	public static NightmareStatistics statistics;
	public static NightmareStatistics phosanisStatistics;

	@Subscribe
	public static final void onLogin(final LoginEvent event) {
		final Player player = event.getPlayer();
		player.getVarManager().sendBit(10237, player.getNotificationSettings().getKillcount("The Nightmare") > 0);
	}

	@Subscribe
	public static void onServerLaunch(final ServerLaunchEvent event) {
		NightmareStatisticsSerializer.INSTANCE.read();

		VarManager.appendPersistentVarp(2647);

		NightmareLobbyArea.lobbyNPC = new NightmareLobbyNPC();
		NightmareLobbyArea.lobbyNPC.spawn();

		NightmareBossArea.boss = new NightmareNPC();
		NightmareBossArea.boss.spawn();

		NightmareBossArea.totemNE = new NightmareTotemNE(NightmareBossArea.boss, NightmareTotemNE.SPAWN_LOCATION);
		NightmareBossArea.totemNE.spawn();

		NightmareBossArea.totemNW = new NightmareTotemNW(NightmareBossArea.boss, NightmareTotemNW.SPAWN_LOCATION);
		NightmareBossArea.totemNW.spawn();

		NightmareBossArea.totemSE = new NightmareTotemSE(NightmareBossArea.boss, NightmareTotemSE.SPAWN_LOCATION);
		NightmareBossArea.totemSE.spawn();

		NightmareBossArea.totemSW = new NightmareTotemSW(NightmareBossArea.boss, NightmareTotemSW.SPAWN_LOCATION);
		NightmareBossArea.totemSW.spawn();

		NightmareBossArea.boss.initTotems(NightmareBossArea.totemNE, NightmareBossArea.totemNW, NightmareBossArea.totemSE, NightmareBossArea.totemSW);

		new GameCommands.Command(PlayerPrivilege.DEVELOPER, "noa", (p, args) -> PoolOfNightmares.enterFight(p));
	}

	public static void updateKillStatistics(long duration) {
		statistics.increaseGlobalKill(duration);
		CoresManager.slowExecutor.execute(NightmareStatisticsSerializer.INSTANCE::write);
	}

	public static void updateDeathStatistics() {
		statistics.increaseGlobalDeath();
		CoresManager.slowExecutor.execute(NightmareStatisticsSerializer.INSTANCE::write);
	}

	public static void updatePhosanisKillStatistics(long duration) {
		phosanisStatistics.increaseGlobalKill(duration);
		CoresManager.slowExecutor.execute(PhosanisNightmareStatisticsSerializer.INSTANCE::write);
	}

	public static void updatePhosanisDeathStatistics() {
		phosanisStatistics.increaseGlobalDeath();
		CoresManager.slowExecutor.execute(PhosanisNightmareStatisticsSerializer.INSTANCE::write);
	}

}
