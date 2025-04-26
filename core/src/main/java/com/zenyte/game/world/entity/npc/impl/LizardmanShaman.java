package com.zenyte.game.world.entity.npc.impl;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.achievementdiary.diaries.KourendDiary;
import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.area.kourend.MolchAndLizardmanTemple;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Kris | 21/04/2019 18:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LizardmanShaman extends NPC implements Spawnable, CombatScript {
    private static final Animation attack = new Animation(7193);
    private static final Animation jump = new Animation(7152);
    private static final Animation land = new Animation(6946);
    private static final Animation meleeAnim = new Animation(7158);
    private static final Projectile rangedProjectile = new Projectile(1291, 70, 15, 60, 15, 10, 64, 5);
    private static final Projectile poisonProjectile = new Projectile(1293, 70, 15, 60, 15, 23, 64, 5);
    private static final Graphics poisonSplash = new Graphics(1294);
    private static final long FIFTEEN_MINUTES_IN_MILLIS = TimeUnit.MINUTES.toMillis(15);
    private long lastTime;
    private boolean hitAnyone;

    public LizardmanShaman(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 6766 || id == 6767 || id == 8565 || id == 7744 || id == 7745;
    }

    @Override
    public void dropItem(final Player killer, final Item item, final Location tile, boolean guaranteedDrop) {
        if (tile.getPlane() != respawnTile.getPlane()) {
            tile.setLocation(tile.getX(), tile.getY(), respawnTile.getPlane());
        }
        super.dropItem(killer, item, tile, guaranteedDrop);
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (lastTime < Utils.currentTimeMillis() && Utils.random(60) == 0) {
            spawnFollowers();
        }
    }

    @Override public void handleOutgoingHit(Entity target, Hit hit) {
        super.handleOutgoingHit(target, hit);
        if (hit.getDamage() > 0) {
            hitAnyone = true;
        }
    }

    private final void spawnFollowers() {
        //Reset the timer so we don't end up trying to spawn the purple creatures over and over.
        lastTime = Utils.currentTimeMillis() + 20000;
        final Entity target = combat.getTarget();
        if (!(target instanceof Player) || isProjectileClipped(target, true)) {
            return;
        }
        final Player player = (Player) target;
        final Location baseSpawnLocation = getBaseSpawnLocation(player);
        final List<Location> spawnTiles = getSpawnLocations(baseSpawnLocation);
        final boolean isTemple = getId() == 8565;
        //Spawn the purple followers.
        for (final Location spawnTile : spawnTiles) {
            new LizardmanSpawn(spawnTile, player, isTemple).spawn();
        }
        //Cause camera shake for all the players standing near the Shaman.
        CharacterLoop.forEach(getMiddleLocation(), 2, Player.class, p -> {
            if (p.isDead() || p.isFinished()) {
                return;
            }
            p.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, (byte) 5, (byte) 0, (byte) 0);
            WorldTasksManager.schedule(() -> p.getPacketDispatcher().resetCamera(), 2);
        });
    }

    @NotNull
    private final Location getBaseSpawnLocation(@NotNull final Player target) {
        //The default location is set to underneath the Player.
        final Location tile = new Location(target.getLocation());
        //Try and set the location away from underneath the Player, assuming it is still in an accessible location.
        for (int i = 0; i < 50; i++) {
            final Location potentialTile = new Location(target.getX() + Utils.random(-5, 5), target.getY() + Utils.random(-5, 5), target.getPlane());
            if (World.isFloorFree(potentialTile, 2) && !isProjectileClipped(potentialTile, true)) {
                tile.setLocation(potentialTile);
                break;
            }
        }
        return tile;
    }

    @NotNull
    private final List<Location> getSpawnLocations(@NotNull final Location tile) {
        final ObjectArrayList<Location> list = new ObjectArrayList<Location>(3);
        //Add the base location itself.
        list.add(new Location(tile));
        final Location garbage = new Location(0);
        //Run up to 100 iterations to find unique tiles to spawn the purple minions on.
        for (int i = 0; i < 100; i++) {
            garbage.setLocation(tile.getX() + Utils.random(-1, 1), tile.getY() + Utils.random(-1, 1), tile.getPlane());
            if (list.contains(garbage) || !World.isFloorFree(garbage, 1) || ProjectileUtils.isProjectileClipped(null, null, tile, garbage, true)) {
                continue;
            }
            list.add(new Location(garbage));
            if (list.size() >= 3) {
                break;
            }
        }
        //If we couldn't find a unique tile for all three, let's just stack them on the base tile.
        for (int i = list.size(); i < 3; i++) {
            list.add(new Location(tile));
        }
        Preconditions.checkArgument(list.size() == 3);
        return list;
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getAchievementDiaries().update(KourendDiary.KILL_A_LIZARDMAN);
            player.getAchievementDiaries().update(KourendDiary.KILL_A_LIZARDMAN_SHAMAN);
            AdventCalendarManager.increaseChallengeProgress(player, 2022, 2, 1);
            player.getCombatAchievements().complete(CAType.A_SCALEY_ENCOUNTER);
            if (!hitAnyone) {
                player.getCombatAchievements().complete(CAType.SHAYZIEN_PROTECTOR);
            }
        }
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    private final void jump(@NotNull final Entity target, @NotNull final Location tile) {
        setCantInteract(true);
        setAnimation(jump);
        WorldTasksManager.schedule(() -> {
            if (isDead()) {
                setCantInteract(false);
                return;
            }
            setCantInteract(false);
            setLocation(tile);
            setAnimation(land);
            for (final Player p : CharacterLoop.find(tile, 1, Player.class, player -> !player.isDead() && (isMultiArea() || player == target))) {
                delayHit(this, 0, target, new Hit(this, Utils.random(20, 25), HitType.REGULAR));
                p.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, (byte) 10, (byte) 0, (byte) 0);
                WorldTasksManager.schedule(() -> p.getPacketDispatcher().resetCamera(), 2);
            }
            lock(1);
            WorldTasksManager.schedule(() -> combat.setTarget(target));
        }, 4);
    }

    private final void shootPoison(@NotNull final Entity target) {
        setAnimation(attack);
        final Location tt = new Location(target.getLocation().getX(), target.getLocation().getY(), target.getLocation().getPlane());
        final int perfectTime = poisonProjectile.getProjectileDuration(getLocation(), tt);
        World.sendGraphics(new Graphics(poisonSplash.getId(), perfectTime - 10, 0), tt);
        final Location startTile = getFaceLocation(target);
        final int delay = World.sendProjectile(startTile.matches(target) ? getLocation() : startTile, tt, poisonProjectile);
        WorldTasksManager.schedule(() -> {
            if (!isDead()) {
                for (final Player p : CharacterLoop.find(tt, 1, Player.class, player -> !player.isDead() && (isMultiArea() || player == target))) {
                    delayHit(-1, target, new Hit(LizardmanShaman.this, (int) (Utils.random(20, 35) * protectionModifier(p)), HitType.POISON).onLand(hit -> {
                        if (hit.getDamage() > 0) {
                            p.getToxins().applyToxin(Toxins.ToxinType.POISON, 10, LizardmanShaman.this);
                        }
                    }));
                }
            }
        }, delay);
    }

    @Override
    public int attack(final Entity target) {
        int style = Utils.random(isWithinMeleeDistance(this, target) ? 6 : 5);
        //If jumping, we validate that the monster can actually perform the jump.
        if (style == 5) {
            final Location targetTile = target.getLocation().transform(-1, -1, 0);
            if (World.isFloorFree(targetTile, getSize()) && !ProjectileUtils.isProjectileClipped(null, null, targetTile, target.getLocation(), true)) {
                jump(target, targetTile);
                return 8;
            }
            //If the jump isn't going to happen, set the style to something that isn't the jump attack and continue with the script.
            if ((style = Utils.random(isWithinMeleeDistance(this, target) ? 5 : 4)) == 5) {
                style = 6;
            }
        }
        switch (style) {
        case 6: 
            setAnimation(meleeAnim);
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), AttackType.CRUSH, target), HitType.MELEE));
            break;
        case 4: 
            shootPoison(target);
            break;
        default: 
            setAnimation(attack);
            final Location startTile = getFaceLocation(target);
            final int delay = World.sendProjectile(startTile.matches(target) ? getLocation() : startTile, target, rangedProjectile);
            delayHit(this, delay, target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, target), HitType.RANGED));
            break;
        }
        return 4;
    }

    public static final float protectionModifier(final Player player) {
        float modifier = 1.0F;
        final Equipment equipment = player.getEquipment();
        final Item helmet = player.getHelmet();
        if (equipment.getId(EquipmentSlot.HELMET) == ItemId.SHAYZIEN_HELM_5 || (helmet != null && helmet.getName().toLowerCase().contains("slayer helm"))) {
            modifier -= 0.2F;
        }
        if (equipment.getId(EquipmentSlot.PLATE) == ItemId.SHAYZIEN_PLATEBODY_5) {
            modifier -= 0.2F;
        }
        if (equipment.getId(EquipmentSlot.LEGS) == ItemId.SHAYZIEN_GREAVES_5) {
            modifier -= 0.2F;
        }
        if (equipment.getId(EquipmentSlot.HANDS) == ItemId.SHAYZIEN_GLOVES_5) {
            modifier -= 0.2F;
        }
        if (equipment.getId(EquipmentSlot.BOOTS) == ItemId.SHAYZIEN_BOOTS_5) {
            modifier -= 0.2F;
        }
        return modifier;
    }
}
