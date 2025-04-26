package com.zenyte.game.world.entity.npc.impl.slayer.superior.impl;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RepugnantSpectre extends SuperiorNPC implements CombatScript {
    public RepugnantSpectre(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7403, tile);
    }
    private static final Projectile PROJECTILE = new Projectile(640, 85, 38, 45, 0, 95, 64, 5);
    private static final Graphics GRAPHICS = new Graphics(641, 0, 320);

    @Override
    public int attack(final Entity target) {
        if (!(target instanceof Player)) {
            return 0;
        }
        final Player player = (Player) target;
        this.setAnimation(this.getCombatDefinitions().getAttackAnim());
        if (!SlayerEquipment.NOSE_PEG.isWielding(player)) {
            delayHit(this, World.sendProjectile(this, target, PROJECTILE), player, new Hit(this, 39, HitType.REGULAR).onLand(hit -> {
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
                player.setGraphics(GRAPHICS);
            }));
        } else {
            delayHit(this, World.sendProjectile(this, target, PROJECTILE), player,
                    new Hit(this, getRandomMaxHit(this, this.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MAGIC).onLand(hit -> player.setGraphics(GRAPHICS)));
        }
        return this.getCombatDefinitions().getAttackSpeed();
    }
}
