package com.zenyte.game.content.chambersofxeric.npc;

import com.zenyte.game.content.chambersofxeric.ScalingMechanics;
import com.zenyte.game.content.chambersofxeric.room.IceDemonRoom;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.skills.prayer.PrayerManager;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.calog.CAType;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.ArrayList;

/**
 * @author Kris | 17. nov 2017 : 19:41.40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class IceDemon extends RaidNPC<IceDemonRoom> implements CombatScript {

    private static final Animation death = new Animation(67);

    private final IceDemonRoom room;

    private final int maxLifepoints;

    private double lifepoints;

    private int percentage;

    private final BlueProgressiveHitBar hitbar = new BlueProgressiveHitBar(percentage);

    private int stage;
    private String lastHitFireSpellUsername = null;

    private ArrayList<String> prayedRange = new ArrayList<String>();
    private ArrayList<String> takenDamage = new ArrayList<String>();

    @Override protected void onDeath(Entity source) {
        super.onDeath(source);
        getPlayersWithKillCredit().stream().filter(p -> !prayedRange.contains(p.getUsername())).forEach(p -> p.getCombatAchievements().complete(CAType.BLIZZARD_DODGER));
        getPlayersWithKillCredit().stream().filter(p -> !takenDamage.contains(p.getUsername())).forEach(p -> p.getCombatAchievements().complete(CAType.CRYO_NO_MORE));
        if (source instanceof final Player player && player.getUsername().equals(lastHitFireSpellUsername)) {
            player.getCombatAchievements().complete(CAType.KILL_IT_WITH_FIRE);
        }
    }

    public IceDemon(final IceDemonRoom room, final int id, final Location tile) {
        super(room.getRaid(), room, id, tile);
        this.room = room;
        lifepoints = maxLifepoints = ScalingMechanics.getIceDemonMaxHP(raid);
        this.radius = 0;
        super.hitBar = new EntityHitBar(this) {

            @Override
            protected int getSize() {
                return 6;
            }
        };
    }

    public void removeLifepoints(final double amount) {
        lifepoints -= amount;
    }

    @Override
    protected void drop(final Location tile) {
        final Player killer = getMostDamagePlayerCheckIronman();
        if (killer == null)
            return;
        onDrop(killer);
        dropItem(killer, new Item(592), tile, true);
    }

    @Override
    public float getXpModifier(final Hit hit) {
        if (hit.containsAttribute("reduced damage")) {
            return 1;
        }
        final Object weapon = hit.getWeapon();
        if (weapon == CombatSpell.FLAMES_OF_ZAMORAK || weapon == CombatSpell.FIRE_WAVE || weapon == CombatSpell.FIRE_BOLT || weapon == CombatSpell.FIRE_BLAST || weapon == CombatSpell.FIRE_STRIKE || weapon == CombatSpell.FIRE_SURGE) {
            hit.setDamage((int) (hit.getDamage() * 1.5F));
        } else {
            hit.setDamage((int) (hit.getDamage() / 3.0F));
        }
        hit.putAttribute("reduced damage", true);
        return 1.0F;
    }

    @Override
    public boolean canAttack(final Player source) {
        if (!definitions.containsOptionCaseSensitive("Attack")) {
            source.sendMessage("You can't attack this npc.");
            return false;
        }
        if (getId() == NpcId.ICE_DEMON) {
            source.sendMessage("You need to unfreeze the demon before you can attack it.");
            return false;
        }
        return true;
    }

    @Override
    public boolean isAttackable() {
        if (getId() == NpcId.ICE_DEMON) {
            return false;
        }
        return super.isAttackable();
    }

    @Override
    public void processNPC() {
        if (stage == 0) {
            if (!room.getBraziers().isEmpty()) {
                room.processBraziers();
            } else {
                if (lifepoints < maxLifepoints) {
                    lifepoints = Math.min(maxLifepoints, lifepoints + Utils.random(2, 5));
                }
            }
            percentage = (int) (((float) lifepoints / maxLifepoints) * 100.0F);
            hitbar.setPercentage(percentage);
            getUpdateFlags().flag(UpdateFlag.HIT);
            getHitBars().add(hitbar);
            if (lifepoints <= 0) {
                room.defreeze();
            }
        } else if (stage == 2) {
            super.processNPC();
        }
    }

    @Override
    public boolean isAttackable(final Entity e) {
        if (getId() == NpcId.ICE_DEMON) {
            if (e instanceof Player) {
                ((Player) e).sendMessage("It's protected by the ice storm.");
            }
            return false;
        }
        return true;
    }

    @Override
    public void sendDeath() {
        resetWalkSteps();
        getCombat().removeTarget();
        setAnimation(null);
        room.removeStorm();
        WorldTasksManager.schedule(new WorldTask() {

            private int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    setAnimation(death);
                } else if (loop == 2) {
                    drop(getMiddleLocation());
                    finish();
                    stop();
                }
                loop++;
            }
        }, 0, 1);
    }

    private static final Animation attack = new Animation(69);

    private static final Graphics impactGfx = new Graphics(1325);

    private static final Graphics burstGfx = new Graphics(363);

    private static final Projectile proj = new Projectile(1324, 60, 15, 40, 15, 10, 64, 5);

    private static final SoundEffect rangedAttackStartSound = new SoundEffect(1440, 10, 0);

    private static final SoundEffect rangedAttackEndSound = new SoundEffect(1021, 10, -1);

    private static final SoundEffect magicAttackStartSound = new SoundEffect(171, 10, 0);

    private static final SoundEffect magicAttackEndSound = new SoundEffect(170, 10, -1);

    @Override
    public double getMagicPrayerMultiplier() {
        return 0.33;
    }

    @Override
    public double getRangedPrayerMultiplier() {
        return 0.5;
    }

    @Override
    public boolean isFreezeable() {
        return false;
    }

    @Override public void handleIngoingHit(Hit hit) {
        super.handleIngoingHit(hit);
        final Object weapon = hit.getWeapon();
        if ((weapon == CombatSpell.FLAMES_OF_ZAMORAK || weapon == CombatSpell.FIRE_WAVE || weapon == CombatSpell.FIRE_BOLT ||
                weapon == CombatSpell.FIRE_BLAST || weapon == CombatSpell.FIRE_STRIKE || weapon == CombatSpell.FIRE_SURGE) && hit.getSource() instanceof final Player player) {
            lastHitFireSpellUsername = player.getUsername();
        } else {
            lastHitFireSpellUsername = null;
        }
    }

    @Override
    public int attack(final Entity target) {
        final MutableBoolean magicPrayer = new MutableBoolean();
        final MutableBoolean rangedPrayer = new MutableBoolean();
        getPossibleTargets(EntityType.PLAYER).forEach(t -> {
            final Player player = (Player) t;
            final PrayerManager prayers = player.getPrayerManager();
            if (prayers.isActive(Prayer.PROTECT_FROM_MISSILES)) {
                if (!prayedRange.contains(player.getUsername())) {
                    prayedRange.add(player.getUsername());
                }
                rangedPrayer.setTrue();
            } else if (prayers.isActive(Prayer.PROTECT_FROM_MAGIC)) {
                magicPrayer.setTrue();
            }
        });
        final int style = (magicPrayer.isTrue() && rangedPrayer.isTrue()) || (magicPrayer.isFalse() && rangedPrayer.isFalse()) ? Utils.random(1) : magicPrayer.isTrue() ? 1 : 0;
        setAnimation(attack);
        final Location targetTile = new Location(target.getLocation());
        if (style == 0) {
            World.sendSoundEffect(getMiddleLocation(), rangedAttackStartSound);
            final int delay = World.sendProjectile(this, targetTile, proj);
            World.sendSoundEffect(targetTile, new SoundEffect(rangedAttackEndSound.getId(), rangedAttackEndSound.getRadius(), proj.getProjectileDuration(this.getLocation(), targetTile) - 15));
            WorldTasksManager.schedule(() -> {
                if (!target.getLocation().withinDistance(getLocation(), 25)) {
                    return;
                }
                World.sendGraphics(impactGfx, targetTile);
                for (final Player p : getRoom().getRaid().getPlayers()) {
                    if (p.getLocation().withinDistance(targetTile, 1)) {
                        p.applyHit(new Hit(IceDemon.this, getRandomMaxHit(IceDemon.this, (int) (30 * (room.getRaid().isChallengeMode() ? 1.5F : 1.0F)), RANGED, p), HitType.REGULAR));
                        if (!takenDamage.contains(p.getUsername())) {
                            takenDamage.add(p.getUsername());
                        }
                    }
                }
            }, delay);
        } else if (style == 1) {
            World.sendSoundEffect(getMiddleLocation(), magicAttackStartSound);
            World.sendSoundEffect(targetTile, new SoundEffect(magicAttackEndSound.getId(), magicAttackEndSound.getRadius(), 60));
            WorldTasksManager.schedule(() -> {
                if (!target.getLocation().withinDistance(getLocation(), 25)) {
                    return;
                }
                for (int x = -1; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        if (!World.isFloorFree(getPlane(), targetTile.getX() + x, targetTile.getY() + y, 1)) {
                            continue;
                        }
                        World.sendGraphics(burstGfx, new Location(targetTile.getX() + x, targetTile.getY() + y, targetTile.getPlane()));
                    }
                }
                for (final Player p : getRoom().getRaid().getPlayers()) {
                    if (p.getLocation().withinDistance(targetTile, 1)) {
                        p.applyHit(new Hit(IceDemon.this, getRandomMaxHit(IceDemon.this, (int) (25 * (room.getRaid().isChallengeMode() ? 1.5F : 1.0F)), MAGIC, p), HitType.REGULAR));
                        p.freezeWithNotification(8);
                        if (!takenDamage.contains(p.getUsername())) {
                            takenDamage.add(p.getUsername());
                        }
                    }
                }
            }, 1);
        }
        return combatDefinitions.getAttackSpeed() + 1;
    }

    public IceDemonRoom getRoom() {
        return room;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }
}
