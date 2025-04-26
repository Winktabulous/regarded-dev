package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.content.boss.BossRespawnTimer;
import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.content.godwars.instance.GodwarsInstance;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.MovementLock;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.utils.TimeUnit;
import org.jetbrains.annotations.NotNull;

/**
 * @author Tommeh | 26 mrt. 2018 : 16:55:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KreeArra extends GodwarsBossNPC implements Spawnable, CombatScript {

    private boolean usedSalamanderOnly = true;
    private boolean targetedByPlayer;

    private boolean doneMeleeDamage;

    public KreeArra(final int id, final Location tile, final Direction direction, final int radius) {
        super(id, tile, direction, radius);
        if (isAbstractNPC() || tile.getX() >= 6400) return;
        setMinions(new GodwarsBossMinion[] {new WingmanSkree(NpcId.WINGMAN_SKREE, new Location(2834, 5297, 2), Direction.SOUTH, 5), new FlockleaderGeerin(NpcId.FLOCKLEADER_GEERIN, new Location(2827, 5299, 2), Direction.SOUTH, 5), new GodwarsBossMinion(NpcId.FLIGHT_KILISA, new Location(2829, 5300, 2), Direction.SOUTH, 5)});
    }

    @Override
    protected BossRespawnTimer timer() {
        return BossRespawnTimer.KREE_ARRA;
    }

    public KreeArra(final GodwarsBossMinion[] minions, final int id, final Location tile, final Direction direction, final int radius) {
        this(id, tile, direction, radius);
        setMinions(minions);
    }

    long clickDelay;

    @Override
    public void processNPC() {
        super.processNPC();
        if (isForceFollowClose()) {
            if (clickDelay > Utils.currentTimeMillis()) {
                setForceFollowClose(false);
            }
        } else {
            if (clickDelay < Utils.currentTimeMillis()) {
                setForceFollowClose(true);
            }
        }
    }

    @Override
    protected ForceTalk[] getQuotes() {
        return null;
    }

    @Override
    protected int diaryFlag() {
        return 4;
    }

    @Override
    public GodType type() {
        return GodType.ARMADYL;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == NpcId.KREEARRA;
    }

    private static final Animation meleeAnimation = new Animation(6981);
    private static final Animation distancedAnimation = new Animation(6980);
    private static final Projectile magicProjectile = new Projectile(1200, 41, 16, 40, 5, 10, 0, 5);
    private static final Projectile rangedProjectile = new Projectile(1199, 41, 16, 40, 5, 10, 0, 5);
    private static final SoundEffect meleeSound = new SoundEffect(3892, 10, 0);
    public static final SoundEffect TORNADO_SOUND = new SoundEffect(3870, 10, 0);
    public static final SoundEffect TORNADO_HIT_SOUND = new SoundEffect(2727, 10, -1);
    public static final SoundEffect TORNADO_SPLASH_SOUND = new SoundEffect(227, 10, -1);

    @Override
    public int attack(final Entity target) {
        final KreeArra npc = this;
        if (npc.isForceFollowClose() && Utils.random(1) == 0) {
            final int distanceX = target.getX() - npc.getX();
            final int distanceY = target.getY() - npc.getY();
            final int size = npc.getSize();
            if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
                return 0;
            }
            npc.setAnimation(meleeAnimation);
            World.sendSoundEffect(getMiddleLocation(), meleeSound);
            delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, 26, MELEE, MAGIC, target), HitType.MELEE));
            return npc.getCombatDefinitions().getAttackSpeed();
        }
        npc.setAnimation(distancedAnimation);
        World.sendSoundEffect(getMiddleLocation(), TORNADO_SOUND);
        for (final Entity t : npc.getPossibleTargets(EntityType.PLAYER)) {
            if (t instanceof Player targetPlayer) {
                final int style = Utils.random(1);

                if (style == 0) {
                    int damage = getRandomMaxHit(npc, 21, MAGIC, RANGED, t);
                    //kree'arra deals a minimum of 10 damage upon successful hit; for even distribution, we re-calc it.
                    if (damage > 0)
                        damage = Utils.random(10, 21);
                    final Hit hit = new Hit(npc, damage, HitType.MAGIC);
                    fireTornadoPush(targetPlayer, hit, magicProjectile);
                }
                else {
                    final Hit hit = new Hit(npc, getRandomMaxHit(npc, 71, RANGED, t), HitType.RANGED);
                    fireTornadoPush(targetPlayer, hit, rangedProjectile);
                }

            }
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }

    private void fireTornadoPush(Player target, Hit hit, Projectile projectile) {
        if (target.isDead() || target.isFinished()) return;
        var delay = World.sendProjectile(this, target, projectile);
        delayHit(this, delay, target, hit);
        var sound = (hit.getDamage() == 0 ? TORNADO_SPLASH_SOUND : TORNADO_HIT_SOUND);
        World.sendSoundEffect(target.getLocation(), sound.withDelay(projectile.getProjectileDuration(getMiddleLocation(), target)));
        if (Utils.random(2) == 0)
            push(target);
    }

    @Override public void handleIngoingHit(Hit hit) {
        super.handleIngoingHit(hit);
        if (!hit.containsAttribute("salamander_weapon")) {
            usedSalamanderOnly = false;
        }
        if (hit.getSource() != null && hit.getSource() instanceof final Player player && player.getLastTarget().equals(this)) {
            targetedByPlayer = true;
        }
        if (hit.getDamage() > 0 && HitType.MELEE.equals(hit.getHitType())) {
            doneMeleeDamage = true;
        }
    }

    @Override
    protected void onFinish(Entity source) {
        super.onFinish(source);

        if (source instanceof final Player player) {
            player.getCombatAchievements().checkKcTask("kree'arra", 50, CAType.KREEARRA_ADEPT);
            player.getCombatAchievements().checkKcTask("kree'arra", 100, CAType.KREEARRA_VETERAN);
            if (allMinionsDead()) {
                player.getCombatAchievements().complete(CAType.AIRBORNE_SHOWDOWN);
            }
            if (usedSalamanderOnly) {
                player.getCombatAchievements().complete(CAType.THE_WORST_RANGED_WEAPON);
            }
            if ((int) player.getAttributes().getOrDefault(GodwarsInstance.CA_TASK_INSTANCE_KC_ATT, 0) >= 30) {
                player.getCombatAchievements().complete(CAType.FEATHER_HUNTER);
            }
            if (player.getAttributes().containsKey(GodwarsInstance.CA_TASK_INSTANCE_ENTERED_ATT)) {
                if (!targetedByPlayer) {
                    player.getCombatAchievements().complete(CAType.COLLATERAL_DAMAGE);
                }
                if (!doneMeleeDamage) {
                    player.getCombatAchievements().complete(CAType.SWOOP_NO_MORE);
                }
            }
        }
    }

    private static final Animation knockbackAnimation = new Animation(848);
    private static final Graphics stunGraphics = new Graphics(348, 0, 92);

    private void push(@NotNull final Player player) {
        if (player.isDead() || player.isFinished()) return;
        final Location tile = player.getFaceLocation(this, 2, 1024);
        final Location destination = new Location(player.getLocation());
        final int dir = DirectionUtil.getMoveDirection(tile.getX() - destination.getX(), tile.getY() - destination.getY());
        if (dir != -1) {
            if (World.checkWalkStep(destination, dir, player.getSize(), false, false))
                destination.setLocation(tile);
        }
        player.faceEntity(this);
        if (!destination.matches(player))
            player.setLocation(destination);

        //50% chance to stun regardless if teleported or not.
        if (Utils.random(1) == 0) {
            if (player.getActionManager().getAction() instanceof PlayerCombat && player.getActionManager().getActionDelay() == 0)
                player.getActionManager().addActionDelay(1);

            var lockTime = System.currentTimeMillis() + TimeUnit.TICKS.toMillis(1);
            var movementLock = new MovementLock(lockTime, "You're stunned.");
            player.addMovementLock(movementLock);
            player.setAnimation(knockbackAnimation);
            player.setGraphics(stunGraphics);
        }
    }

    public void setDoneMeleeDamage() {
        doneMeleeDamage = true;
    }
}
