package com.zenyte.game.content.kebos.alchemicalhydra.npc.combat;

import com.zenyte.game.content.kebos.alchemicalhydra.npc.AlchemicalHydra;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;

/**
 * @author Tommeh | 02/11/2019 | 19:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public interface HydraPhaseSequence {
    Projectile magicAttackProj = new Projectile(1662, 22, 12, 30, 10, 30, 0, 5);
    Projectile rangedAttackProj = new Projectile(1663, 42, 32, 30, 2, 15, 0, 5);

    int autoAttack(final AlchemicalHydra hydra, final Player player);

    boolean attack(final AlchemicalHydra hydra, final Player player);

    void process(final AlchemicalHydra hydra, final Player player);

    default void applyPoison(final AlchemicalHydra hydra, final Player player) {
        //Prevent the attack from going through if the player has teleported off.
        final Location nextLocation = player.getNextLocation();
        if (nextLocation != null && !hydra.getInstance().inside(nextLocation)) {
            return;
        }
        CombatUtilities.delayHit(hydra, -1, player, new Hit(hydra, Utils.random(5, 12), HitType.POISON).onLand(hit -> player.getToxins().applyToxin(Toxins.ToxinType.POISON, 6, hydra)));
    }
}
