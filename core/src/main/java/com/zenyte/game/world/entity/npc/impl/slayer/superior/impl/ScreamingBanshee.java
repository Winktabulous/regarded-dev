package com.zenyte.game.world.entity.npc.impl.slayer.superior.impl;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 01:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ScreamingBanshee extends SuperiorNPC implements CombatScript {
    public ScreamingBanshee(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7390, tile);
    }

    private static final Animation PLAYER_ANIMATION = new Animation(1572);
    private static final Projectile PROJECTILE = new Projectile(337, 20, 20);

    @Override
    public int attack(Entity target) {
        if (!(target instanceof Player)) {
            return 0;
        }
        final Player player = (Player) target;
        setAnimation(getCombatDefinitions().getAttackAnim());
        if (!SlayerEquipment.EARMUFFS.isWielding(player)) {
            delayHit(this, World.sendProjectile(this, target, PROJECTILE), player, new Hit(this, 9, HitType.REGULAR).onLand(hit -> {
                player.lock(3);
                player.setAnimation(PLAYER_ANIMATION);
                for (int i = 0; i <= 6; i++) {
                    if (i == 3) {
                        continue;
                    }
                    if (i == 5) {
                        player.getPrayerManager().setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.8020834));
                    } else {
                        player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.8020834));
                    }
                }
            }));
        } else {
            delayHit(this, 0, player, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        }
        return getCombatDefinitions().getAttackSpeed();
    }
}
