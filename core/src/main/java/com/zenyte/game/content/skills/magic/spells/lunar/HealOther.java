package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.minigame.castlewars.CastleWarsArea;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.NPCSpell;
import com.zenyte.game.content.skills.magic.spells.PlayerSpell;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 17. veebr 2018 : 20:52.48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class HealOther implements PlayerSpell, NPCSpell {
	private static final Animation ANIM = new Animation(4411);
	private static final Graphics GFX = new Graphics(736, 0, 92);
	private static final SoundEffect playerSound = new SoundEffect(2895, 10, 51);
	private static final SoundEffect otherSound = new SoundEffect(2892, 10, 67);

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
		if (player.inArea(CastleWarsArea.class)) {
			player.sendMessage("You cannot cast heal other in Castle-Wars.");
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
		final int minimumRequired = (int) Math.ceil(player.getMaxHitpoints() * 0.11F);
		if (player.getHitpoints() <= minimumRequired) {
			player.sendMessage("You need more hitpoints to cast this spell.");
			return false;
		}
		if (target.getHitpoints() >= target.getMaxHitpoints()) {
			player.sendMessage("The other player doesn't need healing.");
			return false;
		}
		final int missingHealth = target.getMaxHitpoints() - target.getHitpoints();
		final int amount = (int) (player.getHitpoints() * 0.75F);
		final int actualAmount = missingHealth > amount ? amount : missingHealth;
		this.addXp(player, 101);
		player.setAnimation(ANIM);
		player.applyHit(new Hit(actualAmount, HitType.REGULAR));
		World.sendSoundEffect(player, playerSound);
		player.sendMessage("You transfer some of your health to " + target.getName() + ".");
		WorldTasksManager.schedule(() -> {
			if (target.isDead() || !target.isInitialized() || target.isFinished() || target.isLocked()) {
				return;
			}
			target.setGraphics(GFX);
			target.heal(actualAmount);
			World.sendSoundEffect(target, otherSound);
			target.sendMessage(player.getPlayerInformation().getDisplayname() + " has transferred some of their health to you.");
		}, 3);
		return true;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}

	@Override
	public boolean spellEffect(final Player player, final NPC npc) {
		player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc), () -> {
			player.sendMessage("You can only use this spell on players.");
		}, false));
		return false;
	}
}
