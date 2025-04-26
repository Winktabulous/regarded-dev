package com.zenyte.game.content.minigame.barrows;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.Morytania;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;

/**
 * @author Kris | 31. mai 2018 : 02:14:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class BarrowsArea extends Morytania implements CycleProcessPlugin, CannonRestrictionPlugin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3564, 3312 }, { 3564, 3309 }, { 3563, 3308 }, { 3558, 3308 }, { 3557, 3307 },
				{ 3555, 3307 }, { 3554, 3306 }, { 3554, 3305 }, { 3553, 3305 }, { 3552, 3304 }, { 3551, 3304 }, { 3550, 3303 },
				{ 3550, 3301 }, { 3548, 3299 }, { 3548, 3298 }, { 3547, 3297 }, { 3547, 3293 }, { 3548, 3292 }, { 3548, 3290 },
				{ 3547, 3289 }, { 3547, 3288 }, { 3546, 3287 }, { 3546, 3279 }, { 3547, 3278 }, { 3547, 3277 }, { 3548, 3276 },
				{ 3549, 3276 }, { 3550, 3275 }, { 3556, 3275 }, { 3557, 3274 }, { 3558, 3274 }, { 3559, 3273 }, { 3559, 3272 },
				{ 3560, 3271 }, { 3560, 3270 }, { 3561, 3269 }, { 3563, 3269 }, { 3564, 3268 }, { 3567, 3268 }, { 3568, 3269 },
				{ 3570, 3269 }, { 3571, 3270 }, { 3571, 3271 }, { 3572, 3272 }, { 3573, 3272 }, { 3574, 3273 }, { 3575, 3273 },
				{ 3576, 3274 }, { 3582, 3274 }, { 3583, 3275 }, { 3583, 3276 }, { 3584, 3277 }, { 3584, 3288 }, { 3581, 3291 },
				{ 3582, 3292 }, { 3582, 3293 }, { 3583, 3294 }, { 3583, 3295 }, { 3584, 3296 }, { 3584, 3302 }, { 3583, 3303 },
				{ 3582, 3303 }, { 3579, 3306 }, { 3579, 3307 }, { 3578, 3308 }, { 3576, 3308 }, { 3575, 3309 }, { 3571, 3309 },
				{ 3570, 3308 }, { 3568, 3308 }, { 3567, 3309 }, { 3567, 3312 } }, 0) };
	}

	@Override
	public void enter(final Player player) {
	    super.enter(player);
        if (player.getBarrows().isLooted()) {
            player.getBarrows().reset();
            player.getBarrows().shiftDoorways();
            player.getVarManager().sendBit(Barrows.CHEST_VARBIT, 0);
        }
        GameInterface.BARROWS_OVERLAY.open(player);
		player.getCombatAchievements().removeCurrentTaskFlag(CAType.DEFENCE_WHAT_DEFENCE, Barrows.CA_TASK_MAGIC_DAMAGE_ONLY);
	}

	@Override
	public void leave(final Player player, boolean logout) {
	    super.leave(player, logout);
		player.getInterfaceHandler().closeInterface(GameInterface.BARROWS_OVERLAY);
		player.getCombatAchievements().removeCurrentTaskFlag(CAType.DEFENCE_WHAT_DEFENCE, Barrows.CA_TASK_MAGIC_DAMAGE_ONLY);
	}

	@Override
	public String name() {
		return "Barrows";
	}

	@Override public void process() {
		for (final Player player : players) {
			if (player.getPrayerManager().getPrayerPoints() > 0
					&& player.getCombatAchievements().hasCurrentTaskFlags(CAType.FAITHLESS_CRYPT_RUN, Barrows.CA_TASK_FAITHLESS_RUN)) {
				player.getCombatAchievements().removeCurrentTaskFlag(CAType.FAITHLESS_CRYPT_RUN, Barrows.CA_TASK_FAITHLESS_RUN);
			}
		}
	}
}
