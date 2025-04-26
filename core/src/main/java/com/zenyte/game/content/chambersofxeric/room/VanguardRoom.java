package com.zenyte.game.content.chambersofxeric.room;

import com.zenyte.game.content.chambersofxeric.CombatPointCapCalculator;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.chambersofxeric.map.RaidRoom;
import com.zenyte.game.content.chambersofxeric.npc.Vanguard;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Kris | 16. nov 2017 : 2:34.56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class VanguardRoom extends RaidArea {

    /**
     * The animation the magic vanguard plays as it falls down.
     */
    public static final Animation magicFallAnimation = new Animation(7437);

    /**
     * The animation the melee vanguard plays as it falls down.
     */
    public static final Animation meleeFallAnimation = new Animation(7442);

    /**
     * The animation the ranged vanguard plays as it falls down.
     */
    public static final Animation rangedFallAnimation = new Animation(7447);

    /**
     * A 2D array containing coordinates to where the vanguards spawn from.
     */
    private static final Location[][] vanguardPositions = new Location[][] { new Location[] { new Location(3285, 5329, 0), new Location(3277, 5334, 0), new Location(3276, 5324, 0) }, new Location[] { new Location(3317, 5330, 0), new Location(3309, 5334, 0), new Location(3309, 5324, 0) }, new Location[] { new Location(3347, 5325, 0), new Location(3341, 5334, 0), new Location(3337, 5325, 0) } };

    /**
     * The id of the crystal object that blocks the exit.
     */
    private static final int CRYSTAL = 30017;

    /**
     * A list of vanguard ids.
     */
    private static final IntArrayList ids = new IntArrayList(new int[] { 7526, 7527, 7528 });

    /**
     * The animation the vanguards play when they rise.
     */
    private static final Animation rise = new Animation(7428);

    /**
     * The second half of the rising animation that the melee vanguard plays.
     */
    private static final Animation meleeContinuationAnimation = new Animation(7438);

    /**
     * The second half of the rising animation that the ramged vanguard plays.
     */
    private static final Animation rangedContinuationAnimation = new Animation(7443);

    /**
     * The second half of the rising animation that the magic vanguard plays.
     */
    private static final Animation magicContinuationAnimation = new Animation(7433);

    /**
     * An array containing coordinates to where the crystal blocking the exit is.
     */
    private static final Location[] crystalLocations = new Location[] { new Location(3268, 5327, 0), new Location(3311, 5339, 0), new Location(3350, 5328, 0) };

    /**
     * An array of the three vanguards.
     */
    private Vanguard[] vanguards = new Vanguard[3];

    /**
     * The stage of the vanguards, either 0 - not started, 1 - started or 2 - moving.
     */
    private int stage;

    /**
     * The time in milliseconds until the next switch.
     */
    private long nextSwitch;

    /**
     * The crystal blocking the exit.
     */
    private WorldObject blockingCrystal;

    private boolean vanguardsHealed;
    private long vanguardDeathTimer;

    public VanguardRoom(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(type, raid, rotation, size, regionX, regionY, chunkX, chunkY, fromPlane, toPlane);
    }

    @Override
    public CombatPointCapCalculator buildPointsCap() {
        final CombatPointCapCalculator calculator = new CombatPointCapCalculator();
        // Uncap it because the health of the vanguards is set later in the fight and there's only three of them anyways, cannot abuse it.
        calculator.appendExtra(Integer.MAX_VALUE >> 1);
        return calculator;
    }

    /*@Override
    public boolean canPass(final Player player, final WorldObject object) {
        for (final Vanguard vanguard : vanguards) {
            if (!vanguard.isDead() && !vanguard.isFinished()) {
                player.sendMessage("You need to defeat the vanguards before passing on to the next room.");
                return false;
            }
        }
        return true;
    }*/
    /**
     * Checks if all vanguards are dead, and if so, clears the blocking crystal.
     */
    public void clearCrystal() {
        for (final Vanguard vang : vanguards) {
            if (!vang.isDead() && !vang.isFinished()) {
                return;
            }
        }
        if (TimeUnit.MILLISECONDS.toSeconds(Utils.currentTimeMillis() - vanguardDeathTimer) <= 10) {
            getRaid().getPlayers().forEach(p -> p.getCombatAchievements().complete(CAType.TOGETHER_WELL_FALL));
        }
        World.spawnObject(new WorldObject(30026, 10, 0, new Location(vanguards[0].getRespawnTile())));
        Raid.shatterCrystal(blockingCrystal);
        if (!vanguardsHealed) {
            getRaid().getPlayers().forEach(p -> p.getCombatAchievements().complete(CAType.PERFECTLY_BALANCED));
        }
    }

    /**
     * Launches the room, unleashes the vanguards.
     */
    public void launch() {
        if (stage != 0) {
            return;
        }
        final IntArrayList ids = new IntArrayList(VanguardRoom.ids);
        int count = 0;
        while (!ids.isEmpty() && ++count < 1000) {
            Vanguard vanguard = null;
            int subCount = 0;
            while (++subCount < 1000) {
                vanguard = vanguards[Utils.random(vanguards.length - 1)];
                if (vanguard.getId() == NpcId.VANGUARD) {
                    break;
                }
            }
            if (vanguard == null) {
                continue;
            }
            final int id = ids.getInt(Utils.random(ids.size() - 1));
            vanguard.setRadius(0);
            vanguard.setTransformation(id);
            vanguard.setAnimation(rise);
            ids.rem(id);
        }
        WorldTasksManager.schedule(new WorldTask() {

            private boolean second;

            @Override
            public void run() {
                if (raid.isDestroyed()) {
                    stop();
                    return;
                }
                if (!second) {
                    second = true;
                    for (final Vanguard vanguard : vanguards) {
                        if (vanguard.getId() == NpcId.VANGUARD_7527) {
                            vanguard.setAnimation(meleeContinuationAnimation);
                            vanguard.getCombatDefinitions().setAttackStyle("Melee");
                        } else if (vanguard.getId() == NpcId.VANGUARD_7528) {
                            vanguard.setAnimation(rangedContinuationAnimation);
                            vanguard.getCombatDefinitions().setAttackStyle("Ranged");
                        } else if (vanguard.getId() == NpcId.VANGUARD_7526) {
                            vanguard.setTransformation(7529);
                            vanguard.setAnimation(magicContinuationAnimation);
                            vanguard.getCombatDefinitions().setAttackStyle("Magic");
                        }
                    }
                } else {
                    for (final Vanguard vanguard : vanguards) {
                        vanguard.setCantInteract(false);
                    }
                    nextSwitch = Utils.currentTimeMillis() + 10000;
                    stage = 1;
                    stop();
                }
            }
        }, 1, 1);
    }

    /**
     * Checks if the vanguards need to be healed, which only occurs if the difference between any of the alive vanguards is more than 33% of the original health they spawned with. If
     * that is the case, all alive vanguards heal to full health.
     */
    private void heal() {
        final Vanguard[] vanguards = getVanguards();
        final int amount = (int) (vanguards[0].getCombatDefinitions().getHitpoints() * 0.33);
        final Vanguard zero = vanguards[0];
        final Vanguard one = vanguards[1];
        final Vanguard two = vanguards[2];
        if ((!zero.isFinished() && !one.isFinished() && (zero.getHitpoints() + amount < one.getHitpoints() || zero.getHitpoints() - amount > one.getHitpoints())) || (!one.isFinished() && !two.isFinished() && (one.getHitpoints() + amount < two.getHitpoints() || one.getHitpoints() - amount > two.getHitpoints())) || (!two.isFinished() && !zero.isFinished() && (two.getHitpoints() + amount < zero.getHitpoints() || two.getHitpoints() - amount > zero.getHitpoints()))) {
            for (final Vanguard vanguard : vanguards) {
                final int hitpoints = vanguard.getHitpoints();
                if (vanguard.isDead()) {
                    continue;
                }
                vanguard.setHitpoints(vanguard.getMaxHitpoints());
                if (vanguard.getMaxHitpoints() - hitpoints < 0) {
                    continue;
                }
                vanguardsHealed = true;
                vanguard.applyHit(new Hit(vanguard.getMaxHitpoints() - hitpoints, HitType.HEALED));
            }
        }
    }

    /**
     * Switches the locations of the vanguards.
     */
    public void switchLocations() {
        if (stage != 1) {
            return;
        }
        heal();
        stage = 2;
        final boolean clockwise = Utils.random(1) == 0;
        for (final Vanguard vanguard : vanguards) {
            if (vanguard.isDead()) {
                continue;
            }
            vanguard.getCombat().removeTarget();
            vanguard.setCantInteract(true);
            if (vanguard.getId() == NpcId.VANGUARD_7529) {
                vanguard.setAnimation(magicFallAnimation);
            } else if (vanguard.getId() == NpcId.VANGUARD_7527) {
                vanguard.setAnimation(meleeFallAnimation);
            } else {
                vanguard.setAnimation(rangedFallAnimation);
            }
        }
        WorldTasksManager.schedule(new WorldTask() {

            final Vanguard[] tempVanguards = new Vanguard[3];

            final Int2IntMap ids = new Int2IntOpenHashMap();

            final Location firstTile = getLocation(vanguardPositions[index][0]).moveLocation(-1, -1, 0);

            final Location secondTile = getLocation(vanguardPositions[index][1]).moveLocation(-1, -1, 0);

            final Location thirdTile = getLocation(vanguardPositions[index][2]).moveLocation(-1, -1, 0);

            private boolean second;

            private int count;

            @Override
            public void run() {
                if (raid.isDestroyed()) {
                    stop();
                    return;
                }
                if (!second) {
                    second = true;
                    for (int i = 0; i < 3; i++) {
                        ids.put(i, vanguards[i].getId());
                    }
                    for (final Vanguard vanguard : vanguards) {
                        if (vanguard.isDead()) {
                            continue;
                        }
                        vanguard.setTransformation(7526);
                    }
                    // Vanguards go full jesus when rotating, meaning they'll just walk through walls and whatnot.
                    if (clockwise) {
                        vanguards[0].addWalkSteps(secondTile.getX(), secondTile.getY(), -1, false);
                        vanguards[1].addWalkSteps(thirdTile.getX(), thirdTile.getY(), -1, false);
                        vanguards[2].addWalkSteps(firstTile.getX(), firstTile.getY(), -1, false);
                    } else {
                        vanguards[0].addWalkSteps(thirdTile.getX(), thirdTile.getY(), -1, false);
                        vanguards[1].addWalkSteps(firstTile.getX(), firstTile.getY(), -1, false);
                        vanguards[2].addWalkSteps(secondTile.getX(), secondTile.getY(), -1, false);
                    }
                    for (int i = 0; i < 3; i++) {
                        tempVanguards[i] = clockwise ? (vanguards[i == 0 ? 2 : (i - 1)]) : (vanguards[(i + 1) % 3]);
                    }
                } else {
                    for (final Vanguard vanguard : vanguards) {
                        if (vanguard.isDead()) {
                            continue;
                        }
                        vanguard.setTransformation(ids.get(count++));
                        if (vanguard.getId() == NpcId.VANGUARD_7527) {
                            vanguard.setAnimation(meleeContinuationAnimation);
                        } else if (vanguard.getId() == NpcId.VANGUARD_7528) {
                            vanguard.setAnimation(rangedContinuationAnimation);
                        } else if (vanguard.getId() == NpcId.VANGUARD_7529) {
                            vanguard.setAnimation(magicContinuationAnimation);
                        }
                        vanguard.setCantInteract(false);
                        vanguard.checkAggressivity();
                    }
                    vanguards = tempVanguards;
                    updateTimer();
                    stage = 1;
                    stop();
                }
            }
        }, 1, 10);
    }

    /**
     * Sets the time of the next switch to 20 seconds from the time of the call.
     */
    private void updateTimer() {
        nextSwitch = Utils.currentTimeMillis() + 20000;
    }

    @Override
    public void loadRoom() {
        for (int i = 0; i < 3; i++) {
            vanguards[i] = (Vanguard) new Vanguard(raid, this, 7525, getLocation(vanguardPositions[index][i]).moveLocation(-1, -1, 0)).spawn();
        }
        for (int i = 0; i < 3; i++) {
            vanguards[i].setFaceLocation(new Location(vanguards[(i + 1) % 3].getLocation()));
        }
        World.spawnObject(blockingCrystal = new WorldObject(CRYSTAL, 10, getRotation(), getCrystalTile(crystalLocations[index])));
    }

    @Override
    public String name() {
        return "Chambers of Xeric: Vanguard room";
    }

    public Vanguard[] getVanguards() {
        return vanguards;
    }

    public int getStage() {
        return stage;
    }

    public long getNextSwitch() {
        return nextSwitch;
    }

    public void setNextSwitch(long nextSwitch) {
        this.nextSwitch = nextSwitch;
    }

    public void startVanguardDeathTimer() { vanguardDeathTimer = Utils.currentTimeMillis(); }
}
