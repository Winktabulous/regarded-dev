package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.NPCSpell;
import com.zenyte.game.content.skills.magic.spells.PlayerSpell;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;

/**
 * @author Kris | 17. veebr 2018 : 19:43.24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class EnergyTransfer implements PlayerSpell, NPCSpell {
	private static final Animation ANIM = new Animation(4411);
	private static final Graphics GFX = new Graphics(734, 0, 100);

	@Override
	public int getDelay() {
		return 3000;
	}

	@Override
	public boolean spellEffect(final Player player, final Player target) {
		if (!hasDefenceRequirement(player)) {
			return false;
		}
		player.faceEntity(target);
		if (target.isDead() || !target.isInitialized() || target.isFinished() || target.isLocked()) {
			player.sendMessage("The other player is busy.");
			return false;
		}
		if (target.getVarManager().getBitValue(SettingVariables.ACCEPT_AID_VARBIT_ID) != 1) {
			player.sendMessage("The other player is not accepting aid.");
			return false;
		}
		if (target.getDuel() != null) {
			player.sendMessage("You cannot cast lunar spells on players within duels.");
			return false;
		}
		final boolean inWilderness = WildernessArea.isWithinWilderness(target.getX(), target.getY());
		if (inWilderness) {
			if (!player.isMultiArea()) {
				player.sendMessage("You need to be in a multi area to cast this spell.");
				return false;
			} else if (!target.isMultiArea()) {
				player.sendMessage("The target needs to be in a multi area to cast this spell.");
				return false;
			}
		}
		if (player.getHitpoints() <= 10) {
			player.sendMessage("You need more hitpoints to cast this spell.");
			return false;
		}
		if (player.getCombatDefinitions().getSpecialEnergy() < 100) {
			player.sendMessage("You need 100% special energy to cast this spell.");
			return false;
		}
		player.faceEntity(target);
		this.addXp(player, 100);
		player.setAnimation(ANIM);
		player.applyHit(new Hit(10, HitType.REGULAR));
		player.getCombatDefinitions().setSpecialEnergy(0);
		player.sendMessage("You transfer your energy to " + target.getPlayerInformation().getDisplayname() + ".");
		WorldTasksManager.schedule(() -> {
			if (target.isDead() || !target.isInitialized() || target.isFinished() || target.isLocked()) {
				return;
			}
			target.setGraphics(GFX);
			target.getCombatDefinitions().setSpecialEnergy(100);
			target.getVariables().setRunEnergy(100);
			target.sendMessage(player.getPlayerInformation().getDisplayname() + " has transferred their energy to you.");
		}, 1);
		return true;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

	@Override
	public boolean spellEffect(final Player player, final NPC npc) {
		player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc), () -> {
			player.sendMessage("Nothing interesting happens.");
		}, false));
		return false;
	}
}
