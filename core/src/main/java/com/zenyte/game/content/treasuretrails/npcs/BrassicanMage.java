package com.zenyte.game.content.treasuretrails.npcs;

import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 12/04/2019 19:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BrassicanMage extends TreasureGuardian implements CombatScript {

    private static final ForceTalk chat = new ForceTalk("Grow up or leave.");
    private static final Projectile projectile = new Projectile(772, 40, 36, 21, 21, 11, 11, 5);

    public BrassicanMage(@NotNull final Player owner, @NotNull final Location tile) {
        super(owner, tile, 7310);
        setForceTalk(chat);
    }

    @Override
    public int attack(final Entity target) {
        setAnimation(combatDefinitions.getAttackAnim());
        delayHit(this, World.sendProjectile(this, target, projectile), target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, target), HitType.REGULAR));
        return combatDefinitions.getAttackSpeed();
    }
}
