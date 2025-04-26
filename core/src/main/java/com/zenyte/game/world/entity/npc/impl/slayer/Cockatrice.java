package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 20/08/2019 23:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Cockatrice extends NPC implements Spawnable, CombatScript {
    public Cockatrice(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("cockatrice");
    }

    private static final Projectile PROJECTILE = new Projectile(324, 16, 31, 0, 0, 16, 0, 2);
    @Override
    public int attack(final Entity target) {
        if (!(target instanceof Player)) {
            return 0;
        }
        final Cockatrice npc = this;
        final Player player = (Player) target;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        attackSound();
        if (!SlayerEquipment.MIRROR_SHIELD.isWielding(player)) {
            delayHit(npc, 0, player, new Hit(npc, 10, HitType.REGULAR));
            WorldTasksManager.schedule(() -> {
                if (!player.getLocation().withinDistance(target, 15)) {
                    return;
                }
                for (int i = 0; i <= 6; i++) {
                    if (i == 3) {
                        continue;
                    }
                    if (i == 5) {
                        player.getPrayerManager().setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.6052631578947368));
                    } else {
                        player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.6052631578947368));
                    }
                }
            }, World.sendProjectile(npc, target, PROJECTILE));
        } else {
            delayHit(npc, 0, player, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
