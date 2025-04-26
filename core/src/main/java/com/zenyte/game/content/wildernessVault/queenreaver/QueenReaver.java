package com.zenyte.game.content.wildernessVault.queenreaver;

import com.zenyte.game.content.boons.impl.HolierThanThou;
import com.zenyte.game.content.wildernessVault.WildernessVaultConstants;
import com.zenyte.game.content.wildernessVault.WildernessVaultHandler;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
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
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.*;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.RSPolygon;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.*;
import java.util.function.Consumer;

import static com.zenyte.game.world.object.WorldObject.DEFAULT_TYPE;

public class QueenReaver extends NPC implements CombatScript {

    public static final Projectile shadowProjectile = new Projectile(1999, 30, 30, 45, 6, 60, 64, 0);
    private static final Animation ENTRY_ANIMATION = new Animation(7766);
    private static final Animation AOE_ANIMATION = new Animation(7772);
    private static final Animation MELEE_ATTACK_ANIM = new Animation(7769);
    private static final Animation RANGED_ATTACK_ANIM = new Animation(7770);
    private static final Projectile FIRE_PROJECTILE = new Projectile(156, 25, 20, 27, 20, 10, 0, 5);
    public ObjectArrayList<Integer> bloodSplatTargets = new ObjectArrayList<>();
    public Set<Integer> bloodSplatTiles = new HashSet<>();
    public ObjectArrayList<BloodSpawn> bloodSpawns = new ObjectArrayList<>();
    public ObjectArrayList<BloodTrail> bloodTrails = new ObjectArrayList<>();
    private boolean death;
    private WorldTask darknessPulse;
    private int attackCounter;
    private SpecialAttack specialAttack;
    private SpecialAttack lastSpecialAttack;
    private BossPhase bossPhase = BossPhase.REGULAR;
    public ObjectArrayList<WorldObject> fires = new ObjectArrayList<>();
    private int absorbHpMod = 1;
    private static final Graphics LIGHTNING_GFX = new Graphics(281);
    private static final Projectile LIGHTNING_PROJ = new Projectile(280, 69, 30, 55, 65, 60, 32, 5);
    private static final byte[][][] LIGHTNING_OFFSETS = new byte[][][] {new byte[][] {new byte[] {1, 0}, new byte[] {0, 1}}, new byte[][] {new byte[] {0, -1}, new byte[] {1, 0}}, new byte[][] {new byte[] {-1, 0}, new byte[] {0, -1}}, new byte[][] {new byte[] {-1, 0}, new byte[] {0, 1}}};
    private final Projectile bloodProjectile = new Projectile(1578, 135, 0, 20, 11, 100, 0, 0);
    private final Projectile autoProjectile = new Projectile(2002, 135, 0, 20, 11, 100, 0, 0);
    private final Graphics bloodSplatGraphic = new Graphics(1579);
    public static final Projectile soulsplitProjectile = new Projectile(2009, 30, 50, 0, 0, 50, 64, 0);

    public QueenReaver() {
        super(WildernessVaultConstants.BOSS_ID, new Location(1954, 7008), Direction.NORTH, 0);
        hitBar = new EntityHitBar(this) {
            @Override
            public int getType() {
                return 21;
            }
        };
        attackDistance = 40;
        maxDistance = 40;
        this.spawned = true;
        this.supplyCache = false;

        openHud();
    }

    @Override
    protected void updateCombatDefinitions() {
        super.updateCombatDefinitions();

        final NPCCombatDefinitions cachedDefs = NPCCombatDefinitions.clone(getId(), NPCCDLoader.get(getId()));
        final int size = getPlayers().size();
        final int hpMod = size / 5 * 1000;
        cachedDefs.setHitpoints(cachedDefs.getHitpoints() + hpMod);
        setCombatDefinitions(cachedDefs);
        setHitpoints(combatDefinitions.getHitpoints());
    }

    @Override
    public void addReceivedDamage(Entity source, int amount, HitType type) {
        super.addReceivedDamage(source, amount, type);
        isHpStage(amount);
    }

    public boolean isHpStage(int amt) {
        BossPhase next = bossPhase.nextPhase();
        if(next == null)
            return false;

        int triggerHp = next.getHpTrigger();

        if(getHitpoints() - amt <= triggerHp) {
            //System.out.println("CHANGE");
            changePhase(next);
            return true;
        }
        return false;
    }

    @Override
    public void processHit(Hit hit) {
        if(hit.getSource() instanceof Player p) {
            if(p.hasBoon(HolierThanThou.class))
                hit.setDamage((int) (hit.getDamage() * 1.15F));
        }
        super.processHit(hit);
    }

    @Override
    public int attack(Entity target) {
        Set<Player> targets = getPlayers();


        if(specialAttack == null && specialAttackReady()) {
            specialAttack = getSpecialAttack();
            return onSpecialChange(specialAttack);
        }

        if(specialAttack == SpecialAttack.FIRE) {
            return fireAttack();
        }

        if(specialAttack == SpecialAttack.DARKNESS) {
            return shadowAuto();
        }

        if(specialAttack == null) {
            return autoAttack(targets);
        }

        return 0;
    }

    @Override
    public void finish() {
        super.finish();
        clear();
    }

    int tick;

    @Override
    public void processNPC() {
        super.processNPC();

        if(getPlayers().size() == 0)
            finish();


        if (!canProcess()) {
            return;
        }
        tick++;

        processShadowEmbrace();
        if(tick % 2 == 0)
            for (WorldObject fire : fires) {
                for (Player player : getPlayers()) {
                    int distance = player.getLocation().getTileDistance(fire.getLocation());
                    if(distance <= 1) {
                        player.applyHit(new Hit(Utils.random(8), HitType.DEFAULT));
                    }
                }
            }

        handleBloodSplats();

    }


    @Override
    public void sendDeath() {
        super.sendDeath();
        clear();
        closeHud();
        death = true;
    }

    private void closeHud() {
        for (Player player : getPlayers()) {
            player.getHpHud().close();
        }
    }

    @Override
    public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) {
        return false;
    }

    @Override
    public boolean isForceAggressive() {
        return true;
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public boolean isTolerable() {
        return false;
    }


    @Override
    public boolean isProjectileClipped(Position target, boolean closeProximity) {
        return false;
    }

    @Override
    public void applyHit(Hit hit) {
        super.applyHit(hit);
    }


    @Override
    protected void onDeath(Entity source) {
        super.onDeath(source);
        WildernessVaultHandler.getInstance().setBossStatus(WildernessVaultHandler.BossStatus.KILLED);
    }

    public SpecialAttack getSpecialAttack() {
        if(!Utils.randomBoolean(1)) {
            return bossPhase.getSpecial();
        } else {
            SpecialAttack nextSpecial = SpecialAttack.getNextSpecial(lastSpecialAttack);
            lastSpecialAttack = SpecialAttack.VALUES[nextSpecial.ordinal()];
            return nextSpecial;
        }
    }

    public boolean specialAttackReady() {
        return attackCounter >= 5;
    }

    public int onSpecialChange(SpecialAttack phase) {
        attackCounter = 0;
        return switch (phase) {
            case DARKNESS -> shadowEmbrace();
            case MINIONS -> spawnMinions();
            case FIRE -> fireAttack();
            case MAIDEN_BLOOD -> bloodAttack();
            case VETION -> lightning();
        };
    }

    @Override
    protected void drop(Location tile) {
        final String name = notificationName(null);

        getReceivedDamage().entrySet().forEach(pairObjectArrayListEntry -> {
            Player player = World.getPlayer(pairObjectArrayListEntry.getKey().getFirst()).orElse(null);
            if (player != null) {
                int damage = 0;
                for (ReceivedDamage pair : pairObjectArrayListEntry.getValue()) {
                    damage += pair.getDamage();
                }

                if (damage >= 100) {
                    WildernessVaultHandler.canLoot.add(player.getName());
                    player.getNotificationSettings().increaseKill(name);
                    player.getNotificationSettings().sendBossKillCountNotification(name);
                    player.getBossTimer().finishTracking(name);
                }
            }
        });

        for (Player player : getPlayers()) {
            if (!WildernessVaultHandler.canLoot.contains(player.getName())) {
                player.sendMessage("You failed to do enough damage to receive the kill count and the rewards.");
            }
        }

        super.drop(tile);
    }

    public void clear() {
        for (WorldObject fire : fires) {
            World.removeObject(fire);
        }
        fires.clear();

        for (Integer bloodSplatTile : bloodSplatTiles) {
            World.removeObject(World.getObjectWithType(bloodSplatTile, DEFAULT_TYPE));
        }
        bloodSplatTiles.clear();

        for (BloodSpawn bloodSpawn : bloodSpawns) {
            bloodSpawn.finish();
        }
        bloodSpawns.clear();

        for (BloodTrail bloodTrail : bloodTrails) {
            World.removeObject(bloodTrail);
        }
        bloodTrails.clear();
    }

    public void incrementAttackCounter() {
        attackCounter++;
    }

    private void hitsplatHeal(int amount) {
        if (getHitpoints() < getMaxHitpoints())
            applyHit(new Hit(amount, HitType.HEALED));
    }

    private void resetSpecial() {
        attackCounter = 0;
        specialAttack = null;
    }

    public boolean canProcess() {
        return !death;
    }

    private Set<Player> getPlayers() {
        return new ObjectOpenHashSet<>(WildernessVaultHandler.getInstance().getPlayersInVault());
    }

    @Override
    protected void postHitProcess(Hit hit) {
        super.postHitProcess(hit);

        for (Player player : getPlayers()) {
            player.getHpHud().updateValue(getHitpoints());
        }
    }

    private void openHud() {
        for (Player player : getPlayers()) {
            player.getHpHud().open(WildernessVaultConstants.BOSS_ID, getMaxHitpoints());
            player.getBossTimer().startTracking(notificationName(null));
        }
    }

    private int fireAttack() {
        switch (attackCounter) {
            case 0 -> {
                attackSound();
                setAnimation(AOE_ANIMATION);
                incrementAttackCounter();
                return 2;
            }
            case 1 -> {
                ObjectArrayList<Location> borderPositions = WildernessVaultConstants.FIRE_SPAWN_AREA.getAllpositions(0);
                for (int i = 0; i < 10; i++) {
                    Location randomPosition = Utils.random(borderPositions);
                    if(randomPosition == null)
                        continue;

                    int build = FIRE_PROJECTILE.build(this, randomPosition);
                    WorldTasksManager.schedule(() -> {
                        WorldObject fire = new WorldObject(WildernessVaultConstants.FIRE_OBJECT, 10, 1, randomPosition);
                        World.spawnObject(fire);
                        fires.add(fire);
                    }, build);
                }
                resetSpecial();
                return 5;
            }
        }
        return 2;
    }

    private int autoAttack(Set<Player> targets) {
        incrementAttackCounter();
        setAnimation(MELEE_ATTACK_ANIM);
        attackSound();
        for (Player victim : targets) {
            int build = autoProjectile.build(this, victim);
            WorldTasksManager.schedule(() -> {
                List<Player> list = CharacterLoop.find(victim.getLocation(), 1, Player.class, player -> !player.isDead() && !player.isFinished() && targets.contains(player));
                int size = Math.min(list.size(), 9);
                for (int i = 0; i < size; i++) {
                    Player player = list.get(i);
                    player.scheduleHit(this, magic(player, getCombatDefinitions().getMaxHit()).onLand(hit -> {
                        int damage = hit.getDamage();

                        if (player == victim) {
                            handleSoulSplit(victim, hit.getDamage());
                        }

                        Graphics graphic;
                        if (damage <= 0) {
                            graphic = new Graphics(85, 0, 124);
                        } else {
                            graphic = new Graphics(2003);
                        }
                        player.setGraphics(graphic);
                    }), 0);
                }
            }, build);
        }
        return 3;
    }

    private int spawnMinions() {
        setForceTalk("Help me!");
        RSPolygon rsPolygon = RSPolygon.growAbsolute(getMiddleLocation(), 10);
        ObjectArrayList<Location> borderPositions = rsPolygon.getBorderPositions(0);
        Collections.shuffle(borderPositions);
        final int amt = Math.min(borderPositions.size(), getPlayers().size());
        for (int i = 0; i < amt; i++) {
            new QueenReaverMinion(borderPositions.get(i), this).spawn();
        }
        resetSpecial();
        return 3;
    }

    public void absorb(QueenReaverMinion minion) {
        hitsplatHeal(minion.getHitpoints() * 4 * absorbHpMod);
        if (absorbHpMod <= 8) {
            absorbHpMod++;
        }
    }

    public void forEachPlayer(Consumer<Player> consumer) {
        for (Player player : getPlayers()) {
            if (player != null) {
                consumer.accept(player);
            }
        }
    }

    private void processShadowEmbrace() {
        if (darknessPulse == null) {
            return;
        }

        Location queenLocation = getMiddleLocation().copy();
        forEachPlayer(player -> {
            int distance = player.getLocation().getTileDistance(queenLocation);
            int newTrans;
            if (distance <= 4) {
                newTrans = 100;
                final int warningTicks = player.getNumericTemporaryAttribute("nex_darkness_warning_ticks").intValue();
                if (distance <= 3) {
                    newTrans = 50;
                    if (warningTicks == 0) {
                        player.sendMessage(Colour.RS_PINK.wrap("Darkness begins to surround the " + getName() + "."));
                    }
                    //only increment warning ticks when directly next to her
                    player.addTemporaryAttribute("nex_darkness_warning_ticks", warningTicks + 1);
                }

                //if warning has been triggered and player was around her too long, do damage when *near* her every tick
                if (warningTicks >= 5) {
                    if (player.getNumericTemporaryAttribute("nex_darkness_warn").intValue() == 0) {
                        player.sendMessage(Colour.RS_PINK.wrap("The darkness starts to engulf you!"));
                        player.addTemporaryAttribute("nex_darkness_warn", 1);
                    }
                    player.applyHit(new Hit(Utils.random(8), HitType.DEFAULT));
                }
            } else if (distance <= 6) {
                newTrans = 150;
            } else {
                newTrans = 200;
            }

            player.getPacketDispatcher().sendClientScript(5313, newTrans);
        });
    }

    private int shadowEmbrace() {
        setForceTalk("Embrace darkness!");
        if (darknessPulse != null) {
            darknessPulse.stop();
        }

        darknessPulse = this::stopShadowEmbrace;
        WorldTasksManager.schedule(darknessPulse, 30);
        return 3;
    }

    public void stopShadowEmbrace() {
        if (darknessPulse != null) {
            darknessPulse.stop();
            forEachPlayer(player -> player.getPacketDispatcher().sendClientScript(5313, 255));
            darknessPulse = null;
        }
    }

    private int shadowAuto() {
        setAnimation(AOE_ANIMATION);
        attackSound();
        forEachPlayer(player -> {
            int ticks = shadowProjectile.build(this, player);
            WorldTasksManager.schedule(() -> {
                int distance = getMiddleLocation().getTileDistance(player.getLocation());
                int maxHit = Utils.interpolate(60, 2, 1, 10, Math.max(Math.min(distance, 10), 1));
                player.scheduleHit(this, ranged(player, maxHit), -1);
            }, ticks);
        });

        incrementAttackCounter();
        if (attackCounter >= 5  && Utils.randomBoolean()) {
            resetSpecial();
        }
        return 5;
    }

    private int lightning() {
        for (Player target : getPlayers()) {
            final ArrayList<Location> tiles = new ArrayList<Location>();
            final Location location = new Location(target.getLocation());
            final int random = Utils.random(LIGHTNING_OFFSETS.length - 1);
            for (int i = 0; i <= 1; i++) {
                tiles.add(new Location(location.getX() + LIGHTNING_OFFSETS[random][0][i], location.getY() + LIGHTNING_OFFSETS[random][1][i]));
            }
            tiles.add(location);
            for (final Location tile : tiles) {
                World.sendProjectile(this, tile, LIGHTNING_PROJ);
            }
            WorldTasksManager.schedule(() -> {
                for (final Location tile : tiles) {
                    World.sendGraphics(LIGHTNING_GFX, tile);
                    if (target.getX() == tile.getX() && target.getY() == tile.getY()) {
                        delayHit(this, 0, target, new Hit(this, Utils.random(10, 30), HitType.REGULAR));
                    }
                }
            }, LIGHTNING_PROJ.getTime(this, location));
        }
        resetSpecial();
        return 2;
    }

    private int bloodAttack() {
        bloodSplatTargets.clear();
        Set<Player> validTargets = getPlayers();
        List<Location> locations = validTargets.stream().map(p -> p.getLocation().copy()).toList();
        Player randomPlayer = Utils.getRandomCollectionElement(validTargets);
        if(randomPlayer == null)
            return 1;
        faceEntity(randomPlayer);
        for (Location location : locations) {
            fireBloodProjectile(location);
        }
        WorldTasksManager.schedule(() -> {
            if(!canProcess())
                return;
            int amount = Utils.random(1, 2);
            for (int i = 0; i < amount; i++) {
                fireBloodProjectile(randomPlayer.getLocation().random(2));
            }
        });
        resetSpecial();
        return 6;
    }

    private void fireBloodProjectile(Location tile) {
        if (!bloodSplatTargets.contains(tile.getPositionHash())
                && !splatExists(tile)
                && World.canPlaceObjectWithoutCollisions(tile, WorldObject.DEFAULT_TYPE)
                && World.isFloorFree(tile, 1)
        ) bloodSplatTargets.add(tile.getPositionHash());
        else return;

        int delay = World.sendProjectile(this, tile, bloodProjectile);
        WorldTasksManager.schedule(() -> {
            if(!canProcess())
                return;

            if (addSplat(tile)) {
                World.sendGraphics(bloodSplatGraphic, tile);
                boolean attackHitTemp = false;
                for (Player p : getPlayers()) {
                    if (p.getLocation().getPositionHash() == tile.getPositionHash()) {
                        attackHitTemp = true;
                        break;
                    }
                }
                boolean attackHit = attackHitTemp;
                WorldTasksManager.schedule(() -> rollBloodSpawn(tile, attackHit), 9);
                WorldTasksManager.schedule(() -> removeSplat(tile), 10);
            }
        }, delay);
    }

    private void rollBloodSpawn(Location tile, boolean hit) {
        int BLOOD_SPAWN_MAX_COUNT = 5;
        if (bloodSpawns.size() >= BLOOD_SPAWN_MAX_COUNT)
            return;

        if (!canProcess()) return;

        int chance = hit ? 5 : 10;
        if (Utils.random(Utils.getRandom(), 1, chance) == 1) {
            BloodSpawn bloodSpawn = new BloodSpawn(this, tile);
            bloodSpawn.spawn();
            bloodSpawns.add(bloodSpawn);
        }
    }

    private void handleBloodSplats() {
        if (bloodSplatTiles.isEmpty()) return;

        for (Player p : getPlayers()) {
            if (bloodSplatTiles.contains(p.getLocation().getPositionHash())) {
                int BLOOD_SPLAT_MIN_HIT = 5;
                int BLOOD_SPLAT_MAX_HIT = 10;
                int damage = Utils.random(Utils.getRandom(), BLOOD_SPLAT_MIN_HIT, (int) (double) BLOOD_SPLAT_MAX_HIT);
                p.applyHit(new Hit(damage, HitType.REGULAR));
                p.getPrayerManager().drainPrayerPoints(damage / 2);
                hitsplatHeal(damage);
            }
        }
        if (!bloodTrails.isEmpty())
            bloodTrails.removeIf(it -> !it.process());
    }

    public boolean splatExists(Location tile) {
        return bloodSplatTiles.contains(tile.getPositionHash());
    }

    public boolean addSplat(Location tile) {
        return bloodSplatTiles.add(tile.getPositionHash());
    }

    public void removeSplat(Location tile) {
        bloodSplatTiles.remove((Integer) tile.getPositionHash());
    }


    public void changePhase(BossPhase bossPhase) {
        this.bossPhase = bossPhase;
        if(bossPhase == BossPhase.PHASE_3) {
            setTransformation(WildernessVaultConstants.BOSS_ID_SOULSPLIT);
        }
    }

    @Override
    protected boolean preserveStatsOnTransformation() {
        return true;
    }

    private void handleSoulSplit(Player entity, int damage) {
        if (bossPhase != BossPhase.PHASE_3) {
            return;
        }

        int ticks = soulsplitProjectile.build(entity, this);
        scheduleHit(this, new Hit(damage, HitType.HEALED), ticks);
    }

    public enum SpecialAttack {

        FIRE,//
        DARKNESS,//
        MINIONS,//
        VETION,
        MAIDEN_BLOOD//
        ;

        public static SpecialAttack getNextSpecial(SpecialAttack exception) {
            ObjectArrayList<SpecialAttack> specs = new ObjectArrayList<>(new SpecialAttack[]{FIRE, DARKNESS, MINIONS});
            specs.remove(exception);
            return Utils.random(specs);
        }

        public static SpecialAttack[] VALUES = values();
    }

    public enum BossPhase {
        REGULAR(4000, SpecialAttack.FIRE),
        PHASE_1(3000, SpecialAttack.DARKNESS),
        PHASE_2(1500, SpecialAttack.MAIDEN_BLOOD),
        PHASE_3(500, SpecialAttack.VETION)

        ;
        private int hpTrigger;
        private SpecialAttack attack;

        public static BossPhase[] VALUES = values();

        public BossPhase nextPhase() {
            if(this == PHASE_3)
                return null;
            return VALUES[this.ordinal() + 1];
        }
        BossPhase(int hpTrigger, SpecialAttack attack) {
            this.hpTrigger = hpTrigger;
            this.attack = attack;
        }

        public int getHpTrigger() {
            return hpTrigger;
        }

        public SpecialAttack getSpecial() {
            return attack;
        }
    }
}
