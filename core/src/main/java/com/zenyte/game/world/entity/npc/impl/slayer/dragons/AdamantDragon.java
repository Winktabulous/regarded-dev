package com.zenyte.game.world.entity.npc.impl.slayer.dragons;

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
import com.zenyte.game.world.entity.player.variables.TickVariable;

import java.util.ArrayList;

/**
 * @author Tommeh | 28-4-2019 | 19:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AdamantDragon extends NPC implements CombatScript, Spawnable {
    private static final Projectile DRAGONFIRE_PROJ = new Projectile(54, 30, 30, 38, 10, 28, 0, 5);
    private static final Projectile MAGIC_ATTACK_PROJ = new Projectile(165, 35, 30, 41, 5, 28, 0, 5);
    private static final Projectile POISON_ATTACK_PROJ = new Projectile(1486, 35, 0, 41, 10, 101, 0, 5);
    private static final Projectile POISON_ATTACK_EFFECT_PROJ = new Projectile(1486, 0, 0, 41, 20, 41, 0, 5);
    private static final Graphics MAGIC_ATTACK_ONHIT = new Graphics(166, 0, 92);
    private static final Graphics SPLASH = new Graphics(85, 0, 92);
    private static final Graphics POISON_ATTACK_ONHIT = new Graphics(1487, 0, 92);
    private static final Graphics BLOOD_FORFEIT_GFX = new Graphics(754);
    private static final Animation ATTACK_ANIM = new Animation(80);
    private static final Animation SECONDARY_ATTACK_ANIM = new Animation(91);
    private static final Animation DISTANCE_ATTACK_ANIM = new Animation(81);
    private static final byte[][][] OFFSETS = new byte[][][] {new byte[][] {new byte[] {2, 0}, new byte[] {0, 2}}, new byte[][] {new byte[] {0, -2}, new byte[] {2, 0}}, new byte[][] {new byte[] {-2, 0}, new byte[] {0, -2}}, new byte[][] {new byte[] {-2, 0}, new byte[] {0, 2}}};

    public AdamantDragon(int id, Location tile, Direction facing, int radius) {
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

    private int attackFromDistance(final Entity target) {
        final int style = Utils.random(5);
        if (style <= 2) {
            //dragonfire
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
        } else if (style == 3) {
            //magic attack
            //TODO: Revisit this script: Is the damage calculation below actually correct? Makes no real sense.
            setAnimation(DISTANCE_ATTACK_ANIM);
            delayHit(World.sendProjectile(this, target, MAGIC_ATTACK_PROJ), target, new Hit(this, Utils.random(CombatUtilities.getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MAGIC, target)), HitType.MAGIC).onLand(hit -> target.setGraphics(hit.getDamage() == 0 ? SPLASH : MAGIC_ATTACK_ONHIT)));
        } else if (style == 4) {
            //poison attack
            final Location location = new Location(target.getLocation());
            setAnimation(DISTANCE_ATTACK_ANIM);
            WorldTasksManager.schedule(() -> {
                int damage = Utils.random(14, 25);
                if (target instanceof Player) {
                    final Player player = (Player) target;
                    if (player.getVariables().getTime(TickVariable.POISON_IMMUNITY) > 0) {
                        damage = Utils.random(4, 8);
                    }
                }
                World.sendGraphics(POISON_ATTACK_ONHIT, location);
                if (target.getLocation().withinDistance(location, 2)) {
                    delayHit(-1, target, new Hit(this, damage, HitType.POISON));
                }
                final ArrayList<Location> locations = new ArrayList<Location>(2);
                final int amount = Utils.random(1, 2);
                final int index = Utils.random(OFFSETS.length - 1);
                for (int i = 0; i < amount; i++) {
                    locations.add(location.transform(OFFSETS[index][0][i], OFFSETS[index][1][i], 0));
                }
                for (final Location l : locations) {
                    WorldTasksManager.schedule(() -> {
                        int dmg = Utils.random(14, 25);
                        if (target instanceof Player) {
                            final Player player = (Player) target;
                            if (player.getVariables().getTime(TickVariable.POISON_IMMUNITY) > 0) {
                                dmg = Utils.random(4, 8);
                            }
                        }
                        World.sendGraphics(POISON_ATTACK_ONHIT, l);
                        if (target.getLocation().withinDistance(l, 2)) {
                            delayHit(-1, target, new Hit(this, dmg, HitType.POISON));
                        }
                    }, World.sendProjectile(location, l, POISON_ATTACK_EFFECT_PROJ));
                }
            }, World.sendProjectile(this, location, POISON_ATTACK_PROJ));
        } else if (style == 5) {
            //blood forfeit
            final Projectile projectile = AmmunitionDefinitions.ENCHANTED_RUBY_BOLT.getProjectile();
            final int damage = (int) (Math.min(99, target.getHitpoints() * 0.2));//Do not allow the hit to deal more than 99 damage.
            setAnimation(DISTANCE_ATTACK_ANIM);
            World.sendProjectile(this, target, projectile);
            WorldTasksManager.schedule(() -> {
                if (damage > 0) {
                    target.setGraphics(BLOOD_FORFEIT_GFX);
                }
                removeHitpoints(new Hit(target, (int) (getHitpoints() * 0.1), HitType.REGULAR));
                delayHit(-1, target, new Hit(this, damage, HitType.REGULAR));
            }, projectile.getTime(this, target));
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 8030 || id == 8090;
    }
}
