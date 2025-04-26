package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 20/08/2019 23:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Jelly extends NPC implements Spawnable, CombatScript {
    public Jelly(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("jelly");
    }

    @Override
    public int attack(Entity target) {
        final Jelly npc = this;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        attackSound();
        delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, MAGIC, target), HitType.MELEE));
        return npc.getCombatDefinitions().getAttackSpeed();
    }

    @Override
    protected String notificationName(@NotNull final Player player) {
        return "jellie";
    }
}
