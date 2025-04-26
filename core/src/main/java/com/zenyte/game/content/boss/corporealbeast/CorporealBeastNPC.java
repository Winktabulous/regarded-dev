package com.zenyte.game.content.boss.corporealbeast;

import com.near_reality.game.world.entity.AbstractEntityDropExtKt;
import com.zenyte.game.content.boons.impl.CorporealScrutiny;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.EntityHitBar;
import com.zenyte.game.world.entity.ReceivedDamage;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.action.combat.AttackStyleDefinition;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import kotlin.Pair;
import mgi.types.config.items.ItemDefinitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Kris | 9. veebr 2018 : 6:11.00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CorporealBeastNPC extends NPC implements CombatScript {

    private static final Animation stomp = new Animation(1686);
    private final RegionArea cavern;
    private DarkCoreNPC core;
    private boolean lastHitCrystalHalbertSpec;
    private boolean coreDamaged = false;
    private boolean pvmArenaVariant = false;

    @Override public void handleIngoingHit(Hit hit) {
        super.handleIngoingHit(hit);
        lastHitCrystalHalbertSpec = hit.isSpecial() && hit.getWeapon() instanceof Item item && item.getName() != null && item.getName().toLowerCase().contains("crystal halberd");
    }

    public CorporealBeastNPC(final Location tile, final RegionArea cavern) {
        super(319, tile, Direction.SOUTH, 5);
        setDamageCap(100);
        this.cavern = cavern;
        hitBar = new CorporealBeastHitBar(this);
        setAggressionDistance(25);
        this.maxDistance = 50;
        this.attackDistance = 15;
    }

    @Override
    protected void drop(final Location tile) {
        final List<java.util.Map.Entry<Pair<String, GameMode>, ObjectArrayList<ReceivedDamage>>> receivedDamage = AbstractEntityDropExtKt.sortedReceivedDamage(this);
        for (int i = 0, length = Math.min(pvmArenaVariant ? 10 : 3, receivedDamage.size()); i < length; i++) {
            final java.util.Map.Entry<Pair<String, GameMode>, ObjectArrayList<ReceivedDamage>> entry = receivedDamage.get(i);
            if (entry == null) break;
            final Player player = World.getPlayer(entry.getKey().getFirst()).orElse(null);
            if (player == null) continue;

            int damage = 0;
            for (ReceivedDamage pair : entry.getValue()) {
                damage += pair.getDamage();
            }

            if (damage <= 100) {
                player.sendMessage("You didn't do enough damage to the Corporeal Beast to obtain any loot.");
                continue;
            }

            sendNotifications(player);
            dropInner(player, tile);
        }
    }

    private void dropInner(final Player killer, final Location tile) {
        killer.getCombatAchievements().checkKcTask("corporeal beast", 25, CAType.CORPOREAL_BEAST_VETERAN);
        killer.getCombatAchievements().checkKcTask("corporeal beast", 50, CAType.CORPOREAL_BEAST_MASTER);
        if (getReceivedDamage().size() <= 1) {
            killer.getCombatAchievements().complete(CAType.CHICKEN_KILLER);
        }
        if (lastHitCrystalHalbertSpec) {
            killer.getCombatAchievements().complete(CAType.FINDING_THE_WEAK_SPOT);
        }
        if (!coreDamaged) {
            killer.getCombatAchievements().complete(CAType.HOT_ON_YOUR_FEET);
        }

        onDrop(killer);
        final List<DropProcessor> processors = DropProcessorLoader.get(id);
        if (processors != null) {
            for (final DropProcessor processor : processors) {
                processor.onDeath(this, killer);
            }
        }

        final NPCDrops.DropTable drops = NPCDrops.getTable(getId());
        if (drops == null) {
            return;
        }

        NPCDrops.rollTable(killer, drops, drop -> dropItem(killer, drop, tile));
    }

    @Override
    public int getRespawnDelay() {
        return 50;
    }

    @Override
    public boolean isTickEdible() {
        return false;
    }

    @Override
    public double getMagicPrayerMultiplier() {
        return 0.66;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0.66;
    }

    @Override
    public boolean isIntelligent() {
        return true;
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    public void refreshDamageDealt(final Player player, final int extra) {
        int damage = 0;
        final List<ReceivedDamage> damageList = getReceivedDamage().get(
                new Pair<>(player.getUsername(), player.getGameMode())
        );
        if (damageList != null) {
            for (final ReceivedDamage value : damageList) {
                if (value.getTime() < System.currentTimeMillis()) {
                    continue;
                }
                damage += value.getDamage();
            }
        }
        player.getVarManager().sendBit(999, damage + extra);
    }

    @Override
    public void applyHit(final Hit hit) {
        super.applyHit(hit);
        final Entity source = hit.getSource();
        retaliate(source);
        refreshDamageDealt(hit);
        checkCore(hit);
    }

    private void checkCore(final Hit hit) {
        if (hit.getDamage() <= 32 || Utils.random(7) != 0 || core != null && !core.isFinished() && !core.isDead()) {
            return;
        }
        if (core != null) {
            if (!core.isCanRespawn()) {
                return;
            }
        }
        core = (DarkCoreNPC) new DarkCoreNPC(new Location(getX() + 3, getY(), getPlane()), this).spawn();
    }

    private void refreshDamageDealt(final Hit hit) {
        if (hit.getDamage() <= 0) return;
        if (!(hit.getSource() instanceof final Player player)) return;
        final int dmg = hit.getDamage();
        refreshDamageDealt(player, Math.min(dmg, getHitpoints()));
    }


    private boolean halve(final Hit hit) {
        if(hit.getSource() instanceof Player player && player.getBoonManager().hasBoon(CorporealScrutiny.class)) {
            if(player.getWeapon() == null)
                return true;
            ItemDefinitions defs = ItemDefinitions.get(player.getWeapon().getId());
            if(defs == null)
                return true;
            int varbit = defs.getInterfaceVarbit();
            if(varbit == AttackStyleDefinition.PICKAXE.getVarbit()
                    || varbit == AttackStyleDefinition.SPEAR.getVarbit()
                    || varbit == AttackStyleDefinition.DAGGER.getVarbit()
                    || varbit == AttackStyleDefinition.MACE.getVarbit()
                    || varbit == AttackStyleDefinition.WHIP.getVarbit()
                    || varbit == AttackStyleDefinition.CLAWS.getVarbit()
                    || varbit == AttackStyleDefinition.HALBERD.getVarbit()
                    || varbit == AttackStyleDefinition.SCYTHE.getVarbit()
                    || varbit == AttackStyleDefinition.PARTISAN.getVarbit()
                    || varbit == AttackStyleDefinition.SCIMITAR.getVarbit()
            )
                return false;
        }


        final HitType type = hit.getHitType();
        if (type == HitType.MAGIC) return false;
        if (type != HitType.MELEE || hit.isSpecial()) return true;

        final Object weapon = hit.getWeapon();
        if (!(weapon instanceof Item item)) return true;

        final String name = ItemDefinitions.nameOf(item.getId()).toLowerCase();
        return !name.contains("spear") && !name.contains("halberd") && !name.contains("osmumten");
    }

    @Override
    public float getXpModifier(Hit hit) {
        if (halve(hit)) {
            hit.setDamage(hit.getDamage() >> 1);
            return 0.5F;
        }

        return super.getXpModifier(hit);
    }

    private void retaliate(final Entity source) {
        if (source == null) return;
        final NPCCombat combat = getCombat();
        final Entity target = combat.getTarget();
        if (target == source) return;
        combat.setTarget(source);
    }

    @Override
    public void onFinish(final Entity source) {
        drop(getMiddleLocation());
        reset();
        finish();
        if (!spawned) {
            setRespawnTask();
        }

        if (core != null) {
            core.finish();
            core = null;
        }
        for (final Player p : cavern.getPlayers()) {
            p.getVarManager().sendBit(999, 0);
        }
    }

    private final Set<Entity> underneathPlayers = new ObjectOpenHashSet<>();

    @Override
    public void processNPC() {
        if (!isDead()) {
            if (this.combat.getCombatDelay() <= 1) {
                final RegionArea cave = cavern == null ? GlobalAreaManager.get("Corporeal Beast cavern") : cavern;
                if (cave.getPlayers().size() > 0) {
                    underneathPlayers.clear();
                    CharacterLoop.populateEntityList(underneathPlayers, getLocation(), getSize(), Player.class, player -> !player.isDead() && !player.isLocked() && CollisionUtil.collides(getX(), getY(), getSize(), player.getX(), player.getY(), player.getSize()));
                    if (!underneathPlayers.isEmpty()) {
                        setAnimation(stomp);
                        for (final Entity player : underneathPlayers) {
                            if (player instanceof Player) {
                                ((Player) player).sendMessage("You get trampled under the Beast's massive legs.");
                                player.applyHit(new Hit(this, Utils.random(40, 51), HitType.DEFAULT));
                            }
                        }
                        combat.setCombatDelay(6);
                    }
                }
            }
        }
        super.processNPC();
    }

    private static final Animation HIGH_DAMAGE_ATTACK_ANIM = new Animation(1679);
    private static final Animation STAT_DRAIN_ATTACK_ANIM = new Animation(1680);
    private static final Animation AOE_ATTACK_ANIM = new Animation(1681);
    private static final Animation LEFT_CLAW_MELEE_ANIM = new Animation(1682);
    private static final Animation RIGHT_CLAW_MELEE_ANIM = new Animation(1683);
    private static final Projectile HIGH_DAMAGE_PROJ = new Projectile(316, 50, 25, 25, 15, 18, 64, 5);
    private static final Projectile STAT_DRAIN_PROJ = new Projectile(314, 50, 25, 25, 15, 18, 64, 5);
    private static final Projectile AOE_PROJ = new Projectile(315, 50, 5, 25, 15, 18, 64, 5);
    private static final Projectile SPLIT_AOE_PROJ = new Projectile(315, 5, 5, 0, 15, 28, 64, 5);
    private static final Graphics SPLITTING_GFX = new Graphics(1836);

    @Override
    public int attack(final Entity target) {
        final CorporealBeastNPC npc = this;
        final boolean meleeDistance = isWithinMeleeDistance(npc, target);
        if (meleeDistance && Utils.random(1) == 0) {
            npc.setAnimation(Utils.random(1) == 0 ? LEFT_CLAW_MELEE_ANIM : RIGHT_CLAW_MELEE_ANIM);
            delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, 51, MELEE, target), HitType.MELEE));
            return 6;
        }
        final Location center = npc.getMiddleLocation();
        final int type = Utils.random(2);
        if (type == 0) {
            npc.setAnimation(HIGH_DAMAGE_ATTACK_ANIM);
            World.sendProjectile(npc, target, HIGH_DAMAGE_PROJ);
            final int delay = HIGH_DAMAGE_PROJ.getTime(center, target.getLocation());
            delayHit(npc, delay, target, new Hit(npc, getRandomMaxHit(npc, 65, MAGIC, target), HitType.MAGIC));
        } else if (type == 1) {
            npc.setAnimation(STAT_DRAIN_ATTACK_ANIM);
            World.sendProjectile(npc, target, STAT_DRAIN_PROJ);
            final int delay = STAT_DRAIN_PROJ.getTime(center, target.getLocation());
            final int damage = getRandomMaxHit(npc, 50, MAGIC, target);
            delayHit(npc, delay, target, new Hit(npc, damage, HitType.MAGIC).onLand(hit -> {
                if (target.getEntityType() == EntityType.PLAYER && Utils.random(3) == 0) {
                    final Player player = (Player) target;
                    final int skill = Utils.random(1) == 0 ? SkillConstants.PRAYER : SkillConstants.MAGIC;
                    if (skill == SkillConstants.PRAYER) {
                        player.getPrayerManager().drainPrayerPoints(Utils.random(1, 5));
                    } else {
                        player.getSkills().drainSkill(SkillConstants.MAGIC, Utils.random(1, 5));
                    }
                    player.sendMessage("Your " + Skills.getSkillName(skill) + " has been slighly drained!");
                }
            }));
        } else {
            npc.setAnimation(AOE_ATTACK_ANIM);
            final Location tile = new Location(target.getLocation());
            World.sendProjectile(npc, tile, AOE_PROJ);
            final int delay = AOE_PROJ.getTime(center, tile);
            WorldTasksManager.schedule(new WorldTask() {
                private final List<Location> splitTiles = new ArrayList<>(5);
                private List<Entity> targets;
                private boolean split;
                @Override
                public void run() {
                    if (!split) {
                        World.sendGraphics(SPLITTING_GFX, tile);
                        targets = npc.getPossibleTargets(EntityType.PLAYER);
                        for (final Entity e : targets) {
                            if (!e.getLocation().withinDistance(tile, 1)) {
                                continue;
                            }
                            delayHit(npc, -1, target, new Hit(npc, getRandomMaxHit(npc, 30, MAGIC, target), HitType.MAGIC));
                        }
                        for (int i = 0; i < 5; i++) {
                            final int hash = tile.getPositionHash();
                            Location t = null;
                            int count = 50;
                            loop:
                            while (--count > 0) {
                                t = new Location(tile.getX() + Utils.random(-3, 3), tile.getY() + Utils.random(-3, 3), tile.getPlane());
                                final int posHash = t.getPositionHash();
                                if (posHash == hash || !World.isFloorFree(t, 1)) {
                                    continue;
                                }
                                for (final Location x : splitTiles) {
                                    if (posHash == x.getPositionHash()) {
                                        continue loop;
                                    }
                                }
                                break;
                            }
                            splitTiles.add(t);
                            World.sendProjectile(tile, t, SPLIT_AOE_PROJ);
                        }
                        split = true;
                        return;
                    }
                    for (final Location tile : splitTiles) {
                        World.sendGraphics(SPLITTING_GFX, tile);
                        for (final Entity e : targets) {
                            if (!e.getLocation().withinDistance(tile, 1)) {
                                continue;
                            }
                            delayHit(npc, -1, target, new Hit(npc, getRandomMaxHit(npc, 30, MAGIC, target), HitType.MAGIC));
                        }
                    }
                    stop();
                }
            }, delay, 0);
        }
        return 6;
    }


    private static final class CorporealBeastHitBar extends EntityHitBar {
        CorporealBeastHitBar(final Entity entity) {
            super(entity);
        }

        @Override
        public int getType() {
            return 22;
        }

        @Override
        public int getMultiplier() {
            return 160;
        }
    }

    public RegionArea getCavern() {
        return cavern;
    }

    public void setCoreDamaged() { coreDamaged = true; }

    public void setPvmArenaVariant(boolean pvmArenaVariant) {
        this.pvmArenaVariant = pvmArenaVariant;
    }
}
