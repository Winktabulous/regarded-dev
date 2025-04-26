package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;

/**
 * @author Kris | 20/08/2019 23:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AberrantSpectre extends NPC implements Spawnable, CombatScript {

    public AberrantSpectre(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("aberrant spectre");
    }

    private static final Projectile PROJECTILE = new Projectile(335, 45, 38, 95, 30);
    private static final Graphics GRAPHICS = new Graphics(336, 0, 320);
    private static final SoundEffect impactSound = new SoundEffect(273, 10, 0);

    @Override protected void onFinish(Entity source) {
        super.onFinish(source);
        if (source instanceof Player) {
            ((Player) source).getCombatAchievements().complete(CAType.NOXIOUS_FOE);
        }
    }

    @Override
    public int attack(final Entity target) {
        final AberrantSpectre npc = this;
        if (!(target instanceof Player)) {
            return 0;
        }
        final Player player = (Player) target;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        attackSound();
        if (!SlayerEquipment.NOSE_PEG.isWielding(player)) {
            delayHit(npc, World.sendProjectile(npc, target, PROJECTILE), player, new Hit(npc, 14, HitType.REGULAR).onLand(hit -> {
                World.sendSoundEffect(new Location(player.getLocation()), impactSound);
                for (int i = 0; i <= 6; i++) {
                    if (i == 3) {
                        continue;
                    }
                    if (i == 5) {
                        player.getPrayerManager().setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.4105263157894737));
                    } else {
                        player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.2105263157894737));
                    }
                }
                player.setGraphics(GRAPHICS);
            }));
        } else {
            delayHit(npc, World.sendProjectile(npc, target, PROJECTILE), player, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC).onLand(hit -> {
                player.setGraphics(GRAPHICS);
                World.sendSoundEffect(new Location(player.getLocation()), impactSound);
            }));
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
