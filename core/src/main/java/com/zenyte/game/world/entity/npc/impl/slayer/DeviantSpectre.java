package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 20/08/2019 23:20
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DeviantSpectre extends NPC implements Spawnable, CombatScript {
    public DeviantSpectre(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 7279;
    }

    private static final Projectile PROJECTILE = new Projectile(640, 45, 38, 110, 30);
    private static final Graphics GRAPHICS = new Graphics(641, 0, 320);

    @Override
    public int attack(final Entity target) {
        if (!(target instanceof Player)) {
            return 0;
        }
        final DeviantSpectre npc = this;
        final Player player = (Player) target;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        if (!SlayerEquipment.NOSE_PEG.isWielding(player)) {
            delayHit(npc, World.sendProjectile(npc, target, PROJECTILE), player, new Hit(npc, 13, HitType.REGULAR).onLand(hit -> player.setGraphics(GRAPHICS)));
            for (int i = 0; i <= 6; i++) {
                if (i == 3) {
                    continue;
                }
                if (i == 5) {
                    player.getPrayerManager().setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.2105263157894737));
                } else {
                    player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.2105263157894737));
                }
            }
        } else {
            delayHit(npc, World.sendProjectile(npc, target, PROJECTILE), player, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC).onLand(hit -> player.setGraphics(GRAPHICS)));
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
