package com.zenyte.game.content.skills.magic.spells.teleports.structures;

import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 17. juuli 2018 : 02:58:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class RoyalSeedStructure implements TeleportStructure {

	private static final Animation START_ANIM = new Animation(4544);
	private static final Animation END_ANIM = new Animation(4546);
	private static final Graphics START_GFX = new Graphics(767);
	private static final Graphics END_GFX = new Graphics(769);
	
	@Override
	public Animation getStartAnimation() {
		return START_ANIM;
	}

	@Override
	public Graphics getStartGraphics() {
		return START_GFX;
	}
	
	@Override
	public Animation getEndAnimation() {
		return END_ANIM;
	}

    @Override
	public Graphics getEndGraphics() {
		return END_GFX;
	}

    @Override
	public void start(final Player player, final Teleport teleport) {
		TeleportStructure.super.start(player, teleport);
		WorldTasksManager.schedule(() -> player.getAppearance().setHideEquipment(true), 2);
	}
	
	@Override
	public void end(final Player player, final Teleport teleport) {
		TeleportStructure.super.end(player, teleport);
		WorldTasksManager.schedule(() -> player.getAppearance().setHideEquipment(false), 1);
	}
	
}
