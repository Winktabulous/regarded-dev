package com.zenyte.game.content.area.prifddinas.zalcano;

import com.zenyte.game.content.area.prifddinas.zalcano.combat.ZalcanoAttack;
import com.zenyte.game.content.area.prifddinas.zalcano.combat.impl.*;
import com.zenyte.game.content.boss.wildernessbosses.callisto.Callisto;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.LocationUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.HitBar;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.calog.CAType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.zenyte.game.content.skills.thieving.actions.Pickpocket.STUN_GFX;

public class ZalcanoBoss extends NPC implements CombatScript, Spawnable {

    /**
     * How often an attack will occur
     */
    private static final int TIME_PROCESS_ATTACK = 9;
    public static final int MAX_HP = 500;
    private ZalcanoInstance instance;
    private int ticks = 0;
    private ZalcanoPhase phase;
    private final List<ZalcanoAttack> attacks = new ArrayList<>();
    private int realHitpoints = MAX_HP;
    private int downedTimer = 0;
    private int hpDamagedSinceLastDown = 0;
    private ZalcanoAttack lastAttack;
    private ZalcanoLootHandler loot;
    private int lastGolemSpawn;
    private int nextHitFormationCycle;

    public ZalcanoBoss(int id, Location tile, Direction facing, int radius) {
      super(id, tile, facing, radius);
    }

    public ZalcanoBoss(ZalcanoInstance instance) {
        super(ZalcanoConstants.ZALCANO_DEFAULT, ZalcanoConstants.ZALCANO_SPAWN_LOCATION, Direction.NORTH, 5);
        this.instance = instance;
        this.attacks.add(new DroppingBouldersAttack());
        this.attacks.add(new DroppingPebblesAttack());
        this.attacks.add(new SpawnSymbolsAttack());
        this.attacks.add(new SpawnGolemAttack());
        this.attacks.add(new HitFormationAttack());

    }

    @Override
    public void onDrop(@NotNull Player killer) {
    }

    /**
     * Get's all valid attacks
     */
    public List<ZalcanoAttack> getValidAttacks() {
        if (instance == null) return new ArrayList<>(0);

        var list = new ArrayList<ZalcanoAttack>();
        for (var attack : attacks) {
                if (attack.canProcess(instance)) {
                    list.add(attack);
                }
            }
        return list;
    }

    @Override
    public NPC spawn() {
        super.spawn();

        this.combatDefinitions.setAggressionType(null);
        phase = ZalcanoPhase.COMBAT_READY;

        if (instance != null) {
            this.instance.getRockFormationHandler().deactivateAllFormations();
            this.instance.activateFormation();
            this.loot = new ZalcanoLootHandler(instance);
        }

        this.unclip();

        hitBar = new ZalcanoBossHitBar(this);
        return this;
    }

    private final HitBar removeRegularHpBar = new HitBar() {
        @Override
        public int getType() {
            return 21;
        }

        @Override
        public int interpolateTime() {
            return 32767;
        }

        @Override
        public int getPercentage() {
            return 0;
        }
    };

    private final HitBar removeShieldBar = new HitBar() {
        @Override
        public int getType() {
            return 29;
        }

        @Override
        public int getPercentage() {
            return 0;
        }

        @Override
        public int interpolateTime() {
            return 32767;
        }

    };

    @Override
    public boolean setHitpoints(int amount) {

        if (phase == ZalcanoPhase.DOWNED) {
            realHitpoints = amount;
        }

        if (isShieldPhase() && amount <= 0) {
            phase = ZalcanoPhase.START_DOWNED;
            amount = realHitpoints;
        }

        if (amount >= MAX_HP) amount = MAX_HP;

        return super.setHitpoints(amount);
    }

    @Override
    public void applyHit(Hit hit) {
        super.applyHit(hit);
        if (phase == ZalcanoPhase.DOWNED) {
            hpDamagedSinceLastDown += hit.getDamage();
        }
        loot.addDamage((Player)hit.getSource(), hit.getDamage(), isShieldPhase());
    }

    public boolean isShieldPhase() {
        return phase == ZalcanoPhase.COMBAT_READY;
    }

    @Override
    public boolean canAttack(Player source) {

        if (phase==ZalcanoPhase.DEAD) {
            return false;
        }

        if (isShieldPhase()) {
            // If we have tephra
            if (source.getInventory().containsItem(ZalcanoConstants.IMBUED_TEPHRA_ITEM_ID)
                    || source.getEquipment().containsItem(ZalcanoConstants.IMBUED_TEPHRA_ITEM_ID)) {
                    return super.canAttack(source);
            } else {
                source.sendMessage("You must take Zalcano down before damaging her.");
                return false;
            }
        }

        return phase == ZalcanoPhase.DOWNED;
    }

    @Override
    public boolean isAttackableNPC() {
        return true;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == ZalcanoConstants.ZALCANO_DEFAULT || id == ZalcanoConstants.ZALCANO_MINABLE;
    }

    @Override
    public boolean checkAggressivity() {
        return false;
    }

    @Override
    public int attack(Entity target) {
        return -1;
    }

    @Override
    public boolean triggersAutoRetaliate() {
        return false;
    }

    boolean canProcess = true;

    @Override
    public void setInteractingWith(Entity entity) {
        super.setInteractingWith(null);
    }

    @Override
    protected boolean canMove(int fromX, int fromY, int direction) {

        if (!isShieldPhase()) {
            return false;
        }

        return super.canMove(fromX, fromY, direction);
    }

    @Override
    public void processNPC() {
        if (instance == null) {
            super.processNPC();
            return;
        }

        if (!canProcess) return;

        Set<Player> players = instance.getPlayers();
        if (players.size() == 0) {
            super.processNPC();
            return;
        }

        if (phase == ZalcanoPhase.START_DOWNED) {
            setHitpoints(realHitpoints);
            setAnimation(new Animation(8437));

            // TODO: update hitbar

            phase = ZalcanoPhase.DOWNED;
            getUpdateFlags().flag(UpdateFlag.HIT);
            getUpdateFlags().flag(UpdateFlag.APPEARANCE);

            canProcess = false;

            lastAttack.interrupt();

            for (var golem : instance.getGolems()) {
                golem.setHitpoints(0);
            }

            for (Player player : players) {
                player.getActionManager().forceStop();
                player.setAnimation(Animation.STOP);
            }

            instance.clearSymbols();
            setTransformation(ZalcanoConstants.ZALCANO_MINABLE);

            WorldTasksManager.schedule(() -> {
                canProcess = true;
                getUpdateFlags().flag(UpdateFlag.HIT);
            }, 6);
            return;
        }

        super.processNPC();

        if (isShieldPhase()) {
            if (ticks % TIME_PROCESS_ATTACK == 0) {
                var random = Utils.getRandomCollectionElement(getValidAttacks());
                if (random != null) {
                    lastAttack = random;
                    random.execute(instance);
                }
            }
        }

        if(phase == ZalcanoPhase.DOWNED) {
            downedTimer++;
            damagePlayersUnderBoss(players);
        }

        if (downedTimer >= 30 || hpDamagedSinceLastDown >= 300) {
            phase = ZalcanoPhase.END_DOWNED;
            setAnimation(new Animation(8439));
            canProcess = false;
            downedTimer = 0;

            hpDamagedSinceLastDown = 0;
            for (Player player : players) {
                player.getActionManager().forceStop();
                player.setAnimation(Animation.STOP);
            }
            WorldTasksManager.schedule(() -> {
                canProcess = true;
                phase = ZalcanoPhase.COMBAT_READY;
                this.resetFreeze();
                this.setTransformation(ZalcanoConstants.ZALCANO_DEFAULT);
                setHitpoints(300);
                ZalcanoInstance.deleteFloorTephra();
                for (Player player : players) {
                    ZalcanoInstance.deleteTephra(player);
                    if (CombatUtilities.isWithinMeleeDistance(this, player)) {
                        player.getCombatAchievements().setCurrentTaskValue(CAType.PERFECT_ZALCANO, 0);
                        player.setGraphics(STUN_GFX);
                        player.sendFilteredMessage("You have been stunned.");

                        final Location middle = getMiddleLocation();
                        double degrees = Math.toDegrees(Math.atan2(player.getY() - middle.getY(), player.getX() - middle.getX()));
                        if (degrees < 0) {
                            degrees += 360;
                        }
                        final double angle = Math.toRadians(degrees);
                        final int px = (int) Math.round(middle.getX() + (getSize()) * Math.cos(angle));
                        final int py = (int) Math.round(middle.getY() + (getSize()) * Math.sin(angle));
                        final List<Location> tiles = LocationUtil.calculateLine(player.getX(), player.getY(), px, py, player.getPlane());
                        if (!tiles.isEmpty()) tiles.remove(0);
                        final Location destination = new Location(player.getLocation());
                        for (final Location tile : tiles) {
                            final int dir = DirectionUtil.getMoveDirection(tile.getX() - destination.getX(), tile.getY() - destination.getY());
                            if (dir == -1) {
                                continue;
                            }
                            if (!World.checkWalkStep(destination.getPlane(), destination.getX(), destination.getY(), dir, player.getSize(), false, false)) break;
                            destination.setLocation(tile);
                        }
                        final int direction = DirectionUtil.getFaceDirection(player.getX() - destination.getX(), player.getY() - destination.getY());
                        if (!destination.matches(player)) {
                            player.setForceMovement(new ForceMovement(destination, 30, direction));
                            player.lock();
                        }
                        player.faceEntity(this);
                        final Location from = new Location(getLocation().getCoordFaceX(getSize()), getLocation().getCoordFaceY(getSize()), getPlane());
                        World.sendGraphics(Callisto.KNOCKBACK_PLAYER_GRAPHICS, player.getLocation());
                        player.setAnimation(Callisto.KNOCKBACK_ANIMATION);
                        delayHit(this, 0, player, new Hit(this, 3, HitType.REGULAR));
                        WorldTasksManager.schedule(() -> {
                            player.setLocation(destination);
                            player.unlock();
                        });

                        // Push back

                    }
                }
            }, 6);
        }

        ticks++;
    }

    private void damagePlayersUnderBoss(Set<Player> players) {
        var middle = getMiddleLocation();
        for (Player player : players) {
            if (middle.getTileDistance(player.getLocation()) <= 2) {
                player.applyHit(new Hit(this, Utils.random(3, 10), HitType.REGULAR));
            }
        }
    }

    @Override
    public int getDeathDelay() {
        return 1;
    }

    @Override
    public void autoRetaliate(Entity source) {
        super.autoRetaliate(null);
    }

    @Override
    public boolean matches(Position other) {
        return super.matches(other);
    }


    @Override
    public void performDefenceAnimation(Entity attacker) {
    }

    @Override
    public void performDeathAnimation() {
    }

    private Location getDeathLocation;

    @Override
    protected void onDeath(Entity source) {
        super.onDeath(source);

        if (instance == null) return;

        instance.clearInstance();
        getDeathLocation = new Location(this.getMiddleLocation());
        deathDelay = 4;
        this.setAnimation(new Animation(8440));

        lastAttack = null;
        realHitpoints = MAX_HP;
        downedTimer = 0;

        phase = ZalcanoPhase.DEAD;

        for (Player player : instance.getPlayers()) {
            player.getActionManager().forceStop();
            player.setAnimation(Animation.STOP);
            ZalcanoInstance.deleteTephra(player);
        }

        ZalcanoInstance.deleteFloorTephra();

        WorldTasksManager.schedule(() -> {
            this.setTransformation(ZalcanoConstants.ZALCANO_DEFAULT);
            this.loot.rewardPlayers(this, getDeathLocation);
        }, 8);
    }

    @Override
    public boolean isMultiArea() {
        return true;
    }

    public ZalcanoPhase getPhase() {
        return phase;
    }

    public void setPhase(ZalcanoPhase phase) {
        this.phase = phase;
    }

    @Override
    public int getMaxHitpoints() {
        if(isShieldPhase()) return 300;

        return MAX_HP;
    }

    @Override
    public void setTransformation(int id) {
        super.setTransformation(id);

        getHitBars().clear();
        if (id == ZalcanoConstants.ZALCANO_MINABLE) {
            getHitBars().add(removeShieldBar);
        } else {
            getHitBars().add(removeRegularHpBar);
        }
        getHitBars().add(hitBar);

        getUpdateFlags().flag(UpdateFlag.HIT);
    }

    public int getLastGolemSpawn() {
        return lastGolemSpawn;
    }

    public void setLastGolemSpawn(int lastGolemSpawn) {
        this.lastGolemSpawn = lastGolemSpawn;
    }

    public void setNextHitFormationCycle(int nextHitFormationCycle) {
        this.nextHitFormationCycle = nextHitFormationCycle;
    }

    public int getNextHitFormationCycle() { return nextHitFormationCycle; }
}
