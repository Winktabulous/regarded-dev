package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.NPCSpell;
import com.zenyte.game.content.skills.magic.spells.PlayerSpell;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 16. veebr 2018 : 16:38.06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CureOther implements PlayerSpell, NPCSpell {

	private static final Animation ANIM = new Animation(4411);
	private static final Graphics GFX = new Graphics(736, 0, 92);
	private static final SoundEffect castSound = new SoundEffect(2886, 5, 67);
	private static final SoundEffect areaSound = new SoundEffect(2889, 5, 51);

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
		if (!target.getToxins().isPoisoned() && !target.getToxins().isVenomed()) {
			player.sendMessage(target.getPlayerInformation().getDisplayname() + " is not poisoned.");
			return false;
		}
		this.addXp(player, 65);
		player.setAnimation(ANIM);
        World.sendSoundEffect(player, castSound);
		WorldTasksManager.schedule(() -> {
			target.setGraphics(GFX);
            World.sendSoundEffect(target, areaSound);
			target.getToxins().cureToxin(ToxinType.POISON);
			target.sendMessage("Your afflictions have been cured by " + player.getPlayerInformation().getDisplayname() + ".");
			player.sendMessage("You have cured the afflictions of " + target.getPlayerInformation().getDisplayname() + ".");
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
            player.sendMessage("You can only use this spell on players.");
        }, false));
        return false;
    }
}
