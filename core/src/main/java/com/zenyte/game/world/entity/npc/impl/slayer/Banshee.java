package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 29 nov. 2017 : 22:02:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Banshee extends NPC implements Spawnable, CombatScript {
    private static final Animation ATTACK_ANIMATION = new Animation(1523);
    private static final Animation PLAYER_ANIMATION = new Animation(1572);
    private static final Projectile PROJECTILE = new Projectile(337, 20, 20);

    public Banshee(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(Entity target) {
        if (!(target instanceof Player)) {
            return 0;
        }
        final Player player = (Player) target;
        setAnimation(ATTACK_ANIMATION);
        attackSound();
        if (!SlayerEquipment.EARMUFFS.isWielding(player)) {
            int max = getId() == 7272 ? 7 : 6;
            delayHit(this, World.sendProjectile(this, target, PROJECTILE), player, new Hit(this, max, HitType.REGULAR).onLand(hit -> {
                player.stop(Player.StopType.WALK);
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

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getAchievementDiaries().update(MorytaniaDiary.KILL_A_BANSHEE);
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 414 || id == 7272;
    }
}
