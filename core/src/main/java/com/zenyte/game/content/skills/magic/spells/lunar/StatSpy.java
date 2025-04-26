package com.zenyte.game.content.skills.magic.spells.lunar;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.NPCSpell;
import com.zenyte.game.content.skills.magic.spells.PlayerSpell;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;

/**
 * @author Kris | 17. veebr 2018 : 1:57.46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class StatSpy implements PlayerSpell, NPCSpell {

    private static final int[] COMPONENTS = {
            1, 25, 13, 5, 37, 49, 61, 45, 69, 65, 33, 57, 53, 21, 9, 29, 17, 41, 77, 81, 73, 89, 85
    };
	private static final Animation ANIM = new Animation(6293);
	private static final Graphics GFX = new Graphics(1060, 0, 32);
	private static final Graphics TGFX = new Graphics(736, 56, 40);
	private static final SoundEffect targetSound = new SoundEffect(3620, 10, 65);
	private static final SoundEffect playerSound = new SoundEffect(3621, 10, 64);

	@Override
	public int getDelay() {
		return 3000;
	}

	@Override
	public boolean spellEffect(final Player player, final Player target) {
		if (target == null || !target.isInitialized() || target.isFinished()) {
            return false;
        }
        player.faceEntity((target));
        this.addXp(player, 76);
        player.setGraphics(GFX);
        player.setAnimation(ANIM);
        World.sendSoundEffect(player, playerSound);
        World.sendSoundEffect(target, targetSound);
        target.setGraphics(TGFX);
        target.sendMessage(player.getPlayerInformation().getDisplayname() + " is spying upon your stats...");
        player.getPacketDispatcher().sendComponentText(523, 1, target.getSkills().getLevel(0));
        final Skills skills = target.getSkills();
        for (int i = COMPONENTS.length - 1; i >= 0; i--) {
            final int index = COMPONENTS[i];
            player.getPacketDispatcher().sendComponentText(523, index, skills.getLevel(i));
            player.getPacketDispatcher().sendComponentText(523, index + 1, 99);
        }
        player.getPacketDispatcher().sendComponentText(523, 94, target.getPlayerInformation().getDisplayname());
        player.getInterfaceHandler().sendInterface(InterfacePosition.SPELLBOOK_TAB, 523);
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
