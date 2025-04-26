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
 * @author Kris | 20/08/2019 23:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Basilisk extends NPC implements Spawnable, CombatScript {
    public Basilisk(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("basilisk");
    }

    private static final Projectile PROJECTILE = new Projectile(75, 18, 25, 115, 0);

    @Override
    public int attack(final Entity target) {
        final Basilisk npc = this;
        if (!(target instanceof Player)) {
            return 0;
        }
        final Player player = (Player) target;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        attackSound();
        if (!SlayerEquipment.MIRROR_SHIELD.isWielding(player)) {
            delayHit(npc, 0, player, new Hit(npc, 11, HitType.REGULAR));
            WorldTasksManager.schedule(() -> {
                if (!player.getLocation().withinDistance(target.getLocation(), 15)) {
                    return;
                }
                for (int i = 0; i <= 6; i++) {
                    if (i == 3) {
                        continue;
                    }
                    if (i == 5) {
                        player.getPrayerManager().setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.4078947368421053));
                    } else {
                        player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.4078947368421053));
                    }
                }
            }, World.sendProjectile(npc, target, PROJECTILE));
        } else {
            delayHit(npc, 0, player, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
