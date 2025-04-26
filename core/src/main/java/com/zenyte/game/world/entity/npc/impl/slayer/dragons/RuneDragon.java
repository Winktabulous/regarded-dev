package com.zenyte.game.world.entity.npc.impl.slayer.dragons;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.AmmunitionDefinitions;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;

/**
 * @author Tommeh | 29-4-2019 | 18:15
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class RuneDragon extends NPC implements CombatScript, Spawnable {
    private static final Projectile DRAGONFIRE_PROJ = new Projectile(54, 30, 30, 38, 10, 28, 0, 5);
    private static final Projectile MAGIC_ATTACK_PROJ = new Projectile(162, 35, 30, 41, 5, 28, 0, 5);
    private static final Projectile LIGHTNING_ATTACK_PROJ = new Projectile(1488, 35, 0, 41, 10, 41, 0, 5);
    private static final Graphics MAGIC_ATTACK_ONHIT = new Graphics(163, 0, 92);
    private static final Graphics SPLASH = new Graphics(85, 0, 92);
    private static final Graphics LIFE_LEECH_GFX = new Graphics(753);
    private static final Animation ATTACK_ANIM = new Animation(80);
    private static final Animation SECONDARY_ATTACK_ANIM = new Animation(91);
    private static final Animation DISTANCE_ATTACK_ANIM = new Animation(81);
    private static final byte[][][] OFFSETS = new byte[][][] {new byte[][] {new byte[] {1, 0}, new byte[] {0, 1}}, new byte[][] {new byte[] {0, -1}, new byte[] {1, 0}}, new byte[][] {new byte[] {-1, 0}, new byte[] {0, -1}}, new byte[][] {new byte[] {-1, 0}, new byte[] {0, 1}}};
    private NPC[] lightnings;
    private int ticks;

    public RuneDragon(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(Entity target) {
        if (!(target instanceof Player)) {
            return 0;
        }
        if (isWithinMeleeDistance(this, target)) {
            if (Utils.random(2) == 0) {
                return attackFromDistance(target);
            }
            setAnimation(Utils.random(1) == 0 ? ATTACK_ANIM : SECONDARY_ATTACK_ANIM); //melee attack
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        } else {
            return attackFromDistance(target);
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public void processNPC() {
        super.processNPC();
        final Entity target = getCombat().getTarget();
        if (lightnings == null || lightnings[0] == null || lightnings[1] == null || target == null) {
            return;
        }
        if (ticks >= 7) {
            for (final NPC lightning : lightnings) {
                if (lightning == null) {
                    continue;
                }
                lightning.finish();
            }
            lightnings = null;
            ticks = 0;
            return;
        }
        for (final NPC lightning : lightnings) {
            if (lightning == null) {
                continue;
            }
            if (lightning.getLocation().withinDistance(target, 1)) {
                int damage = Utils.random(4, 7);
                if (target instanceof Player) {
                    final Player player = (Player) target;
                    if (SlayerEquipment.INSULATED_BOOTS.isWielding(player)) {
                        damage = Utils.random(1, 2);
                    }
                }
                target.applyHit(new Hit(this, damage, HitType.REGULAR));
            }
        }
        ticks++;
    }

    private int attackFromDistance(final Entity target) {
        final int style = Utils.random(7);
        if (style <= 3) {
            //dragonfire
            return dragonfire(target);
        } else if (style <= 5) {
            //magic attack
            setAnimation(DISTANCE_ATTACK_ANIM);
            CombatUtilities.delayHit(this, World.sendProjectile(this, target, MAGIC_ATTACK_PROJ), target, new Hit(this, CombatUtilities.getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC).onLand(hit -> target.setGraphics(hit.getDamage() == 0 ? SPLASH : MAGIC_ATTACK_ONHIT)));
        } else if (style == 6) {
            //lightning attack
            if (lightnings != null) {
                return dragonfire(target);
            }
            final Location base = new Location(target.getLocation());
            final int offset = Utils.random(OFFSETS.length - 1);
            final Location[] locations = new Location[2];
            lightnings = new NPC[2];
            setAnimation(DISTANCE_ATTACK_ANIM);
            for (int i = 0; i < lightnings.length; i++) {
                final int index = i;
                locations[index] = base.transform(OFFSETS[offset][0][index], OFFSETS[offset][1][index], 0);
                WorldTasksManager.schedule(() -> {
                    lightnings[index] = new NPC(8032, locations[index], false);
                    lightnings[index].spawn();
                }, World.sendProjectile(this, locations[index], LIGHTNING_ATTACK_PROJ));
            }
        } else if (style == 7) {
            //life leech
            if (getHitpoints() == getMaxHitpoints()) {
                return dragonfire(target);
            }
            final Projectile projectile = AmmunitionDefinitions.ENCHANTED_ONYX_BOLT.getProjectile();
            final int max = CombatUtilities.getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, target);
            final int damage = Utils.random(max);
            setAnimation(DISTANCE_ATTACK_ANIM);
            delayHit(World.sendProjectile(this, target, projectile), target, new Hit(this, damage, HitType.REGULAR).onLand(hit -> {
                if (hit.getDamage() > 0) {
                    target.setGraphics(LIFE_LEECH_GFX);
                }
                applyHit(new Hit(target, hit.getDamage(), HitType.HEALED));
            }));
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    private int dragonfire(final Entity target) {
        setAnimation(DISTANCE_ATTACK_ANIM);
        final Player player = (Player) target;
        final boolean perk = player.getPerkManager().isValid(PerkWrapper.BACKFIRE);
        final double modifier = !perk ? 1 : Math.max(0, Utils.randomDouble() - 0.25F);
        final Dragonfire dragonfire = new Dragonfire(DragonfireType.CHROMATIC_DRAGONFIRE, 50, DragonfireProtection.getProtection(this, player));
        final int deflected = !perk ? 0 : ((int) Math.floor(dragonfire.getMaximumDamage() * modifier));
        delayHit(World.sendProjectile(this, target, DRAGONFIRE_PROJ), target, new Hit(this, Utils.random(Math.max(0, dragonfire.getDamage() - deflected)), HitType.REGULAR).onLand(hit -> {
            PlayerCombat.appendDragonfireShieldCharges(player);
            player.sendFilteredMessage(String.format(dragonfire.getMessage(), "dragon's fiery breath"));
            if (perk) {
                dragonfire.backfire(this, player, 0, deflected);
            }
        }));
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 8031 || id == 8091;
    }
}
