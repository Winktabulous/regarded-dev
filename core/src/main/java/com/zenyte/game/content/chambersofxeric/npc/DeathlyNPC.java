package com.zenyte.game.content.chambersofxeric.npc;

import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.ScalingMechanics;
import com.zenyte.game.content.chambersofxeric.room.DeathlyRoom;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Kris | 17. nov 2017 : 1:00.59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class DeathlyNPC extends RaidNPC<DeathlyRoom> implements CombatScript {

    public DeathlyNPC(final Raid raid, final DeathlyRoom room, final int id, final Location tile) {
        super(raid, room, id, tile);
        this.attackDistance = 10;
        this.aggressionDistance = 10;
    }

    private static final Graphics cast = new Graphics(129, 0, 90);

    private static final Graphics impact = new Graphics(131, 0, 90);

    private static final Graphics splash = new Graphics(339, 0, 60);

    private static final Animation magicAnim = new Animation(711);

    private static final Projectile magicProj = new Projectile(130, 43, 25, 57, 15, 18, 64, 5);

    private static final SoundEffect magicCastSound = new SoundEffect(155, 10, 0);

    private static final SoundEffect magicLandSound = new SoundEffect(156, 10, -1);

    private static final Animation rangedAnim = new Animation(426);

    private static final Projectile rangedProj = new Projectile(15, 42, 30, 40, 15, 10, 64, 5);

    private static final SoundEffect rangedAttackSound = new SoundEffect(2693, 10, 0);

    @Override
    public int attack(final Entity target) {
        if (getId() == NpcId.DEATHLY_MAGE) {
            setAnimation(magicAnim);
            World.sendSoundEffect(getLocation(), magicCastSound);
            setGraphics(cast);
            final int preciseDelay = magicProj.getProjectileDuration(getLocation(), target.getLocation());
            World.sendSoundEffect(target.getLocation(), new SoundEffect(magicLandSound.getId(), magicLandSound.getRadius(), preciseDelay));
            delayHit(this, World.sendProjectile(this, target, magicProj), target, magic(target, ScalingMechanics.getDeathlyMageMaxHit(raid)).onLand(hit -> target.setGraphics(hit.getDamage() > 0 ? impact : splash)));
        } else {
            World.sendSoundEffect(getLocation(), rangedAttackSound);
            setAnimation(rangedAnim);
            delayHit(World.sendProjectile(this, target, rangedProj), target, ranged(target, ScalingMechanics.getDeathlyRangeMaxHit(raid)));
        }
        return combatDefinitions.getAttackSpeed();
    }

    @Override
    protected void drop(final Location tile) {
        final Player killer = getMostDamagePlayerCheckIronman();
        if (killer == null)
            return;
        onDrop(killer);
        dropItem(killer, new Item(526), tile, true);
    }

    @Override
    public double getMagicPrayerMultiplier() {
        return 0.33;
    }

    @Override
    public double getRangedPrayerMultiplier() {
        return 0.33;
    }
}
