package com.zenyte.game.content.tombsofamascut.encounter;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.tombsofamascut.InvocationType;
import com.zenyte.game.content.tombsofamascut.npc.*;
import com.zenyte.game.content.tombsofamascut.raid.EncounterStage;
import com.zenyte.game.content.tombsofamascut.raid.EncounterType;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidArea;
import com.zenyte.game.content.tombsofamascut.raid.TOARaidParty;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.model.music.Music;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.RSColour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.FullMovementPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Savions.
 */
public class WardenEncounter extends TOARaidArea implements CycleProcessPlugin, FullMovementPlugin {

    public static final int OSMUMTEN_NPC_ID = 11690;
    public static final int ELIDINIS_NPC_ID = 11746;
    public static final int TUMEKEN_NPC_ID = 11747;
    public static final int ELIDINIS_MAGE_ID = 11753;
    public static final int ELIDINIS_RANGE_ID = 11754;
    public static final int ELIDINIS_DOWN_ID = 11755;
    public static final int TUMEKEN_MAGE_ID = 11756;
    public static final int TUMEKEN_RANGE_ID = 11757;
    public static final int TUMEKEN_DOWN_ID = 11758;
    private static final int ELIDINIS_ROTATING_TRAP_OBJECT_ID = 45748;
    private static final int TUMEKEN_ROTATING_TRAP_OBJECT_ID = 45749;
    private static final Location[] ELIDINIS_ROTATING_TRAP_LOCATIONS = {new Location(3798, 5144, 1), new Location(3798, 5150, 1), new Location(3798, 5156, 1), new Location(3798, 5162, 1), new Location(3801, 5141, 1), new Location(3801, 5147, 1), new Location(3801, 5153, 1), new Location(3801, 5159, 1), new Location(3801, 5165, 1), new Location(3804, 5144, 1), new Location(3804, 5150, 1), new Location(3804, 5156, 1), new Location(3804, 5162, 1), new Location(3807, 5141, 1), new Location(3807, 5147, 1), new Location(3807, 5159, 1), new Location(3807, 5165, 1), new Location(3810, 5144, 1), new Location(3810, 5150, 1), new Location(3810, 5156, 1), new Location(3810, 5162, 1), new Location(3813, 5141, 1), new Location(3813, 5147, 1), new Location(3813, 5153, 1), new Location(3813, 5159, 1), new Location(3813, 5165, 1), new Location(3816, 5144, 1), new Location(3816, 5150, 1), new Location(3816, 5156, 1), new Location(3816, 5162, 1)};
    private static final Location[] TUMEKEN_ROTATING_TRAP_LOCATIONS = {new Location(3798, 5141, 1), new Location(3798, 5147, 1), new Location(3798, 5153, 1), new Location(3798, 5159, 1), new Location(3798, 5165, 1), new Location(3801, 5144, 1), new Location(3801, 5150, 1), new Location(3801, 5156, 1), new Location(3801, 5162, 1), new Location(3804, 5141, 1), new Location(3804, 5147, 1), new Location(3804, 5153, 1), new Location(3804, 5159, 1), new Location(3804, 5165, 1), new Location(3807, 5144, 1), new Location(3807, 5150, 1), new Location(3807, 5156, 1), new Location(3807, 5162, 1), new Location(3810, 5141, 1), new Location(3810, 5147, 1), new Location(3810, 5153, 1), new Location(3810, 5159, 1), new Location(3810, 5165, 1), new Location(3813, 5144, 1), new Location(3813, 5150, 1), new Location(3813, 5156, 1), new Location(3813, 5162, 1), new Location(3816, 5141, 1), new Location(3816, 5147, 1), new Location(3816, 5153, 1), new Location(3816, 5159, 1), new Location(3816, 5165, 1)};
    private static final Animation ROTATING_TRAP_START_ANIMATION = new Animation(9524);
    private static final Animation ROTATING_TRAP_END_ANIMATION = new Animation(9526);
    private static final Animation PLAYER_STONE_START_ANIMATION = new Animation(9713);
    private static final Animation PLAYER_STONE_MID_ANIMATION = new Animation(9714);
    private static final Animation PLAYER_STONE_END_ANIMATION = new Animation(9715);
    private static final Location OBELISK_LOCATION = new Location(3807, 5153, 1);
    private static final Location ELIDINIS_LOCATION = new Location(3792, 5152, 1);
    private static final Location TUMEKEN_LOCATION = new Location(3820, 5152, 1);
    private static final Location ELIDINIS_ORB_SPAWN_LOCATION = new Location(3806, 5154, 1);
    private static final Location[][] ELIDINIS_ORB_PATH = {
            {new Location(3797, 5154, 1)},
            {new Location(3805, 5154, 1), new Location(3803, 5156, 1), new Location(3797, 5156, 1)},
            {new Location(3805, 5154, 1), new Location(3803, 5152, 1), new Location(3797, 5152, 1)}
    };
    private static final Location TUMEKEN_ORB_SPAWN_LOCATION = new Location(3810, 5154, 1);
    private static final Location[][] TUMEKEN_ORB_PATH = {
            {new Location(3811, 5154, 1), new Location(3813, 5152, 1), new Location(3819, 5152, 1)},
            {new Location(3819, 5154, 1)},
            {new Location(3811, 5154, 1), new Location(3813, 5156, 1), new Location(3819, 5156, 1)}
    };
    private static final SoundEffect CHALLENGE_START_SOUND_EFFECT = new SoundEffect(6081,15);
    private static final SoundEffect ROTATING_TRAP_LANDING_SOUND = new SoundEffect(6132);
    private static final SoundEffect ELIDINIS_PROJECTILE_SEND_SOUND = new SoundEffect(6119, 30);
    private static final SoundEffect TUMEKEN_PROJECTILE_SEND_SOUND = new SoundEffect(6148, 30);
    private static final SoundEffect PROJECTILE_LANDING_SOUND = new SoundEffect(6073, 15);
    private static final SoundEffect PLAYER_STONE_SOUND_EFFECT = new SoundEffect(4207);
    private static final SoundEffect CORE_RETURN_SOUND = new SoundEffect(6083);
    private static final SoundEffect FLYING_WARDEN_REVIVE_SOUND = new SoundEffect(167);
    private static final SoundEffect[] OBELISK_PHASE_TWO_SOUNDS = {new SoundEffect(6131), new SoundEffect(4386, 1, 0, 4)};
    private static final SoundEffect[] OBELISK_PHASE_TWO_SECOND_SOUNDS = {new SoundEffect(6065), new SoundEffect(6205, 17), new SoundEffect(6249, 109),
            new SoundEffect(6167, 164), new SoundEffect(6150, 197), new SoundEffect(6091, 233), new SoundEffect(6178, 269), new SoundEffect(6101, 309), new SoundEffect(6037, 348)};
    private static final SoundEffect[] MOVING_WARDEN_EXPLODE_SOUNDS = {new SoundEffect(6228, 1, 125), new SoundEffect(6109, 1, 249), new SoundEffect(6196, 1, 54)};
    public static final RSColour[] WARDEN_HUD_COLOURS = { new RSColour(5, 5, 0), new RSColour(20, 19, 0), new RSColour(27, 26, 0)};
    private static final Animation OSMUMTEN_FLY_AWAY_ANIM = new Animation(5546);
    private static final Animation OBELISK_ORB_SPAWN_ANIMATION = new Animation(9721);
    private static final Animation OBELISK_DEFAULT_ANIMATION = new Animation(9723);
    private static final Animation PLAYER_MOVE_ANIM = new Animation(1114);
    private static final Animation MOVING_WARDEN_DEATH_ANIMATION = new Animation(9662);
    private static final Animation FLYING_WARDEN_REVIVE_ANIMATOON = new Animation(9664, 120);
    private static final Graphics FLYING_WARDEN_REVIVE_GFX = new Graphics(2218, 120, 0);
    private static final Projectile ELIDINIS_PROJECTILE = new Projectile(2238, 87, 20, 30, 40, 390, 64, 0);
    private static final Projectile TUMEKEN_PROJECTILE = new Projectile(2237, 87, 20, 30, 40, 390, 64, 0);
    private static final Projectile CORE_PROJECTILE = new Projectile(2240, 20, 62, 60, 12, 30, 64, 0);
    private static final Graphics PROJECTILE_LANDING_GFX = new Graphics(1605);
    private static final Graphics CORE_EXPLODE_GFX = new Graphics(2157, 30, 50);
    private final List<String> playersReady = new ArrayList<>();
    private final boolean ancientHaste;
    private final boolean acceleration;
    private final boolean penetration;
    private int phase;
    private boolean elidinisStart;
    private WardensOsmumten osmumten;
    private WardensObelisk obelisk;
    private StaticWardenNPC elidinisWarden;
    private StaticWardenNPC tumekenWarden;
    private MovingWardenNPC movingWardenNPC;
    private WardenCoreNPC wardenCoreNPC;
    private FlyingWardenNPC flyingWardenNPC;
    private int obeliskTicks = 2;
    private List<WardenChargingOrb> chargingOrbs = new ArrayList<>();
    private int elidinisOrbIndex = 0;
    private int tumekenOrbIndex = 0;;
    private boolean[] checkRotatingTraps = new boolean[2];

    public WardenEncounter(AllocatedArea allocatedArea, int copiedChunkX, int copiedChunkY, TOARaidParty party, EncounterType encounterType) {
        super(allocatedArea, copiedChunkX, copiedChunkY, party, encounterType);
        ancientHaste = party.getPartySettings().isActive(InvocationType.ANCIENT_HASTE);
        acceleration = party.getPartySettings().isActive(InvocationType.ACCELERATION);
        penetration = party.getPartySettings().isActive(InvocationType.PENETRATION);
    }

    @Override
    public void enter(Player player) {
        super.enter(player);
        player.getMusic().stop();
    }

    @Override
    public void leave(Player player, boolean logout) {
        super.leave(player, logout);
        if (EncounterStage.NOT_STARTED.equals(stage)) {
            playersReady.remove(player.getUsername());
            checkPlayersReady(false);
        }
    }

    @Override
    public void constructed() {
        super.constructed();
        spawnOsmumten();
        spawnObelisk();
        spawnStaticWardens();
    }

    @Override
    public void onRoomStart() {
        if (osmumten != null) {
            osmumten.flyAway();
        }
        if (obelisk != null) {
            obelisk.setTransformation(WardensObelisk.ID + 1);
            obelisk.setMaxHealth();
        }
        if (elidinisWarden != null) {
            elidinisWarden.setTransformation(ELIDINIS_NPC_ID + 2);
            elidinisWarden.setMaxHealth();
        }
        if (tumekenWarden != null) {
            tumekenWarden.setTransformation(TUMEKEN_NPC_ID + 2);
            tumekenWarden.setMaxHealth();
        }
        players.forEach(p -> {
            p.sendMessage("Challenge started: " + getCurrentChallengeName(false));
            p.getMusic().unlock(Music.get(encounterType.getSoundTrack()));
            p.getHpHud().open(obelisk.getId(), obelisk.getMaxHitpoints());
        });
        World.sendSoundEffect(getLocation(OBELISK_LOCATION).transform(1, 1), CHALLENGE_START_SOUND_EFFECT);
    }

    @Override
    public void onRoomEnd() {
        players.forEach(p -> p.getHpHud().close());
    }

    @Override
    public void onRoomReset() {
        players.stream().filter(Objects::nonNull).forEach(p -> {
            p.getMusic().stop();
            p.getHpHud().close();
        });
        spawnOsmumten();
        spawnObelisk();
        spawnStaticWardens();
        playersReady.clear();
        phase = 0;
        elidinisOrbIndex = 0;
        tumekenOrbIndex = 0;
        removeChargingOrbs();
        checkRotatingTraps = new boolean[2];
        if (movingWardenNPC != null && !movingWardenNPC.isFinished()) {
            movingWardenNPC.finish();
        }
        if (wardenCoreNPC != null && !wardenCoreNPC.isFinished()) {
            wardenCoreNPC.finish();
        }
        if (flyingWardenNPC != null && !flyingWardenNPC.isFinished()) {
            flyingWardenNPC.finish();
        }
    }

    @Override
    public boolean processMovement(Player player, int x, int y) {
        return !isStoned(player);
    }

    private void spawnOsmumten() {
        if (osmumten != null && !osmumten.isFinished()) {
            osmumten.finish();
        }
        osmumten = new WardensOsmumten(OSMUMTEN_NPC_ID, getLocation(encounterType.getNpcLocation()), this);
        osmumten.spawn();
    }

    private void spawnObelisk() {
        if (obelisk != null && !obelisk.isFinished()) {
            obelisk.finish();
        }
        obelisk = new WardensObelisk(getLocation(OBELISK_LOCATION), this, acceleration, penetration);
        obelisk.spawn();
    }

    private void spawnStaticWardens() {
        if (elidinisWarden != null && !elidinisWarden.isFinished()) {
            elidinisWarden.finish();
        }
        elidinisWarden = new StaticWardenNPC(ELIDINIS_NPC_ID, getLocation(ELIDINIS_LOCATION), Direction.EAST, this);
        elidinisWarden.spawn();
        if (tumekenWarden != null && !tumekenWarden.isFinished()) {
            tumekenWarden.finish();
        }
        tumekenWarden = new StaticWardenNPC(TUMEKEN_NPC_ID, getLocation(TUMEKEN_LOCATION), Direction.WEST, this);
        tumekenWarden.spawn();
    }

    public void setReady(final Player player) {
        if (!EncounterStage.NOT_STARTED.equals(stage)) {
            return;
        }
        final boolean wasReady = playersReady.contains(player.getUsername());
        if (!wasReady) {
            playersReady.add(player.getUsername());
        }
        checkPlayersReady(!wasReady);
        if (EncounterStage.NOT_STARTED.equals(stage)) {
            player.getDialogueManager().start(new PlainChat(player, "The challenge will either begin once everyone else in your party is ready or after 2 minutes have passed from the first ready up."));
        }
    }

    private void checkPlayersReady(boolean sendMessage) {
        final int required = players.size();
        final int current = playersReady.size();
        if (current == 0 || required == 0) {
            return;
        }
        if (current >= required) {
            startRoom();
        } else if (sendMessage) {
            players.stream().filter(Objects::nonNull).forEach(p -> p.sendMessage(current + "/" + required + " party members are now ready."));
        }
    }

    private void removeChargingOrbs() {
        chargingOrbs.removeIf(orb -> {
            if (!orb.isFinished()) {
                orb.finish();
            }
            return true;
        });
    }

    @Override
    public void process() {
        if (EncounterStage.STARTED.equals(stage)) {
            if (phase == 0 && obelisk != null) {
                if (obeliskTicks > 0 && (--obeliskTicks % 2) == 0) {
                    final WardenChargingOrb elidinisOrb = new WardenChargingOrb(getLocation(ELIDINIS_ORB_SPAWN_LOCATION), Direction.WEST, this, elidinisWarden, ELIDINIS_ORB_PATH[elidinisOrbIndex]);
                    elidinisOrbIndex++;
                    elidinisOrbIndex %= 3;
                    elidinisOrb.spawn();
                    chargingOrbs.add(elidinisOrb);
                    final WardenChargingOrb tumekenOrb = new WardenChargingOrb(getLocation(TUMEKEN_ORB_SPAWN_LOCATION), Direction.EAST, this, tumekenWarden, TUMEKEN_ORB_PATH[tumekenOrbIndex]);
                    tumekenOrbIndex++;
                    tumekenOrbIndex %= 3;
                    tumekenOrb.spawn();
                    chargingOrbs.add(tumekenOrb);
                    if (obeliskTicks <= 0) {
                        obelisk.setAnimation(OBELISK_ORB_SPAWN_ANIMATION);
                        obeliskTicks = 4;
                    }
                }
                if (checkRotatingTraps[0]) {
                    checkRotatingTraps(ELIDINIS_ROTATING_TRAP_LOCATIONS);
                }
                if (checkRotatingTraps[1]) {
                    checkRotatingTraps(TUMEKEN_ROTATING_TRAP_LOCATIONS);
                }
            } else if (phase == 1) {
                final Player[] players = getChallengePlayers();
                for (Player p : players) {
                    if (p != null && isStoned(p)) {
                        final long stoneDelay = (long) p.getTemporaryAttributes().getOrDefault("toa_wardens_stone_player", 0L);
                        if (stoneDelay >= WorldThread.getCurrentCycle()) {
                            final int remainingDelay = (int) (stoneDelay - WorldThread.getCurrentCycle());
                            p.setAnimation(remainingDelay >= 1 && remainingDelay <= 6 ? PLAYER_STONE_MID_ANIMATION : PLAYER_STONE_END_ANIMATION);
                        }
                    }
                }
            }
        }
    }

    private void checkRotatingTraps(Location[] locations) {
        for (Location location : locations) {
            final Location loc = getLocation(location);
            for (Player p : getChallengePlayers()) {
                if (p != null && p.getX() >= loc.getX() && p.getX() <= loc.getX() + 2 && p.getY() >= loc.getY() && p.getY() <= loc.getY() + 2) {
                    final int baseHit = 11;
                    p.applyHit(new Hit(obelisk, (int) (baseHit * party.getDamageMultiplier()) + Utils.random(2), HitType.DEFAULT));
                }
            }
        }
    }


    public void sendRotatingTraps(boolean elidinis) {
        final Location[] locations = elidinis ? ELIDINIS_ROTATING_TRAP_LOCATIONS : TUMEKEN_ROTATING_TRAP_LOCATIONS;
        final int trapId = elidinis ? ELIDINIS_ROTATING_TRAP_OBJECT_ID : TUMEKEN_ROTATING_TRAP_OBJECT_ID;
        WorldTasksManager.schedule(addRunningTask(new WorldTask() {
            int ticks = 0;
            @Override
            public void run() {
                if (++ticks == 2) {
                    for (Location loc : locations) {
                        World.spawnTemporaryObject(new WorldObject(trapId, 10, 0, getLocation(loc)), 0);
                    }
                    for (Player p : getChallengePlayers()) {
                        if (p != null) {
                            p.sendSound(ROTATING_TRAP_LANDING_SOUND);
                        }
                    }
                } else if (ticks == 3) {
                    for (Location loc : locations) {
                        World.spawnTemporaryObject(new WorldObject(trapId + 2, 10, 0, getLocation(loc)), 8);
                    }
                } else if (ticks == 5) {
                    checkRotatingTraps[elidinis ? 0 : 1] = true;
                } else if (ticks == 6) {
                    for (Location loc : locations) {
                        World.sendObjectAnimation(trapId + 2, 10, 0, getLocation(loc), ROTATING_TRAP_START_ANIMATION);
                    }

                } else if (ticks == 10) {
                    for (Location loc : locations) {
                        World.sendObjectAnimation(trapId + 2, 10, 0, getLocation(loc), ROTATING_TRAP_END_ANIMATION);
                    }
                    checkRotatingTraps[elidinis ? 0 : 1] = false;
                    stop();
                }
            }
        }), 1, 0);
    }

    public void sendFirstPhaseProjectile(boolean isElidinis) {
        WorldTasksManager.schedule(addRunningTask(new WorldTask() {
            int ticks = 0;
            @Override
            public void run() {
                final Player[] players = getChallengePlayers();
                if (obelisk.isDead() || obelisk.isFinished() || phase != 0 || players.length < 1) {
                    stop();
                    return;
                }
                if (ticks++ == 0) {
                    for (Player p : players) {
                        if (p != null) {
                            p.sendSound(isElidinis ? ELIDINIS_PROJECTILE_SEND_SOUND : TUMEKEN_PROJECTILE_SEND_SOUND);
                            World.sendProjectile(isElidinis ? elidinisWarden : tumekenWarden, p, isElidinis ? ELIDINIS_PROJECTILE : TUMEKEN_PROJECTILE);
                            p.sendMessage((isElidinis ? "<col=3366ff>" : "<col=ff8e32>") + "A large ball of energy is shot your way...</col>");
                        }
                    }
                } else if (ticks == 13) {
                    for (Player p : players) {
                        if (p != null) {
                            p.sendSound(PROJECTILE_LANDING_SOUND);
                            p.setGraphics(PROJECTILE_LANDING_GFX);
                            if (isElidinis) {
                                final int baseDamage = (int) (12 * party.getDamageMultiplier());
                                int damage = baseDamage;
                                for (Player p1 : players) {
                                    if (p1 != null && !p1.getUsername().equals(p.getUsername()) && p.getLocation().getTileDistance(p1.getLocation()) <= 1) {
                                        damage += (baseDamage * 3);
                                    }
                                }
                                final int quarterDamage = damage / 4;
                                for (int i = 0; i < 4; i++) {
                                    p.scheduleHit(obelisk, new Hit(obelisk, quarterDamage + (i < 2 ? 1 : 0), HitType.DEFAULT), 0);
                                }
                            } else {
                                final int baseDamage = (int) (13 * party.getDamageMultiplier());
                                int damage = baseDamage;
                                for (Player p1 : players) {
                                    if (p1 != null && !p1.getUsername().equals(p.getUsername()) && p.getLocation().getTileDistance(p1.getLocation()) > 0) {
                                        damage += (baseDamage * 3);
                                    }
                                }
                                p.scheduleHit(obelisk, new Hit(obelisk, damage, HitType.DEFAULT), 0);
                            }
                        }
                    }
                    stop();
                }
            }
        }), 2, 0);
    }

    public void startMovingWardenPhase() {
        phase = 1;
        obelisk.setTransformation(WardensObelisk.ID + 2);
        obelisk.setAnimation(OBELISK_DEFAULT_ANIMATION);
        elidinisWarden.removeObeliskHitBar();
        tumekenWarden.removeObeliskHitBar();
        elidinisStart = elidinisWarden.getTotalCharged() > tumekenWarden.getTotalCharged();
        final Location[][] path = elidinisStart ? TUMEKEN_ORB_PATH : ELIDINIS_ORB_PATH;
        final Location startTile = elidinisStart ? TUMEKEN_ORB_SPAWN_LOCATION.transform(1, 0) : ELIDINIS_ORB_SPAWN_LOCATION.transform(-1, 0);
        for (Location[] subPath : path) {
            int x = startTile.getX();
            int y = startTile.getY();
            final Location end = subPath[subPath.length - 1];
            int distance = 0;
            while(x != end.getX() || y != end.getY()) {
                final Location next = getLocation(new Location(x, y, 1));
                World.sendGraphics(new Graphics(2196, 6 + (distance++ * 6), 0), next);
                if ((distance + 1) % 4 == 0) {
                    World.sendSoundEffect(next, new SoundEffect(6191, 15, ((distance + 1) / 4) - 1));
                }
                if (x > end.getX()) {
                    x--;
                } else if (x < end.getX()) {
                    x++;
                }
                if (y > end.getY()) {
                    y--;
                } else if (y < end.getY()) {
                    y++;
                }
            }
            World.sendGraphics(new Graphics(2158, 6 + (distance * 6), 60), getLocation(end));
        }
        players.forEach(p -> {
            if (p != null) {
                for (SoundEffect sound : OBELISK_PHASE_TWO_SOUNDS) {
                    p.sendSound(sound);
                }
            }
        });
        removeChargingOrbs();
        WorldTasksManager.schedule(addRunningTask(new WorldTask() {
            int ticks = 0;
            @Override
            public void run() {
                final Player[] players = getChallengePlayers();
                if (players.length < 1) {
                    stop();
                    return;
                }
                if (ticks++ == 0) {
                    final StaticWardenNPC alive = elidinisStart ? elidinisWarden : tumekenWarden;
                    final StaticWardenNPC death = elidinisStart ? tumekenWarden : elidinisWarden;
                    movingWardenNPC = new MovingWardenNPC(elidinisStart ? ELIDINIS_MAGE_ID : TUMEKEN_RANGE_ID, new Location(alive.getLocation()), alive.getSpawnDirection(), WardenEncounter.this);
                    movingWardenNPC.spawn();
                    movingWardenNPC.setMaxHealth();
                    alive.finish();
                    death.setTransformation((elidinisStart ? TUMEKEN_NPC_ID : ELIDINIS_NPC_ID) + 13);
                    death.applyHit(new Hit(death.getMaxHitpoints(), HitType.DEFAULT));
                    for (Player p : players) {
                        if (p != null) {
                            p.sendMessage(elidinisStart ? "<col=3366ff>As Tumeken's Warden falls, Elidinis' Warden powers up!</col>" :
                                    "<col=ff8e32>As Elidinis' Warden falls, Tumeken's Warden powers up!</col>");
                            p.getHpHud().open(elidinisStart ? ELIDINIS_MAGE_ID : TUMEKEN_RANGE_ID, movingWardenNPC.getMaxHitpoints());
                            p.getHpHud().sendColorChange(WARDEN_HUD_COLOURS[0], WARDEN_HUD_COLOURS[1], WARDEN_HUD_COLOURS[2]);
                            p.getHpHud().updateValue(0);
                            for (SoundEffect sound : OBELISK_PHASE_TWO_SECOND_SOUNDS) {
                                p.sendSound(sound);
                            }
                        }
                    }
                } else if (ticks == 10) {
                    if (elidinisStart) {
                        tumekenWarden.setSendLegsGfx();
                    } else {
                        elidinisWarden.setSendLegsGfx();
                    }
                } else if (ticks == 15) {
                    movingWardenNPC.addWalkSteps(movingWardenNPC.getX() + (elidinisStart ? 5 : -6), movingWardenNPC.getY(), 64, false);
                } else if (ticks == 18) {
                    movingWardenNPC.setCantInteract(false);
                } else if (ticks == 21) {
                    movingWardenNPC.setCanAttack(true);
                    stop();
                }
            }
        }), 1, 0);
    }

    public void turnIntoStone(Player player) {
        player.sendMessage("<col=ff3045>You've been entombed in stone!</col>");
        player.sendSound(PLAYER_STONE_SOUND_EFFECT);
        player.setAnimation(PLAYER_STONE_START_ANIMATION);
        player.getTemporaryAttributes().put("toa_wardens_stone_player", WorldThread.getCurrentCycle() + 7L);
    }

    public boolean isStoned(Player player) {
        return WorldThread.getCurrentCycle() < (long) player.getTemporaryAttributes().getOrDefault("toa_wardens_stone_player", 0L);
    }

    @Override
    public boolean sendDeath(Player player, Entity source) {
        player.getTemporaryAttributes().remove("toa_wardens_stone_player");
        return super.sendDeath(player, source);
    }

    public void spawnCore(Location coreLoc, Direction facing, int aliveTicks) {
        wardenCoreNPC = new WardenCoreNPC(coreLoc, facing, aliveTicks, this, movingWardenNPC);
        wardenCoreNPC.spawn();
        for (Player p : getChallengePlayers()) {
            if (p != null && coreLoc.equals(p.getLocation())) {
                movePlayer(p);
            }
        }
    }

    public static void movePlayer(final Player player) {
        Location nextLocation = null;
        radiusLoop:
        for (int radius = 1; radius <= 2; radius++) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    if (x != -radius && x != radius && y != -radius && y != radius) {
                        continue;
                    }
                    final Location attemptLocation = player.getLocation().transform(x, y);
                    if (World.getObjectWithType(attemptLocation, 10) == null) {
                        nextLocation = attemptLocation;
                        break radiusLoop;
                    }
                }
            }
        }
        if (nextLocation != null) {
            player.stopAll();
            player.lock(1);
            player.setAnimation(PLAYER_MOVE_ANIM);
            player.autoForceMovement(player.getLocation(), nextLocation, 0, 29);
        }
    }

    public void sendCoreBack() {
        World.sendProjectile(wardenCoreNPC, movingWardenNPC, CORE_PROJECTILE);
        for (Player p : getChallengePlayers()) {
            if (p != null) {
                p.sendSound(CORE_RETURN_SOUND);
            }
        }
        wardenCoreNPC.finish();
        WorldTasksManager.schedule(() -> {
            if (EncounterStage.STARTED.equals(stage)) {
                movingWardenNPC.revive();
            }
        }, 2);
    }

    public void killMovingWarden() {
        if (wardenCoreNPC != null && !wardenCoreNPC.isFinished()) {
            World.sendGraphics(CORE_EXPLODE_GFX, wardenCoreNPC.getLocation());
            wardenCoreNPC.finish();
        }
        players.forEach(p -> {
            if (p != null) {
                p.sendMessage(!elidinisStart ? "<col=3366ff>Tumeken's Warden uses the last of its power to restore Elidinis' Warden!</col>" :
                   "<col=ff8e32>Elidinis' Warden uses the last of its power to restore Tumeken's Warden!</col>");
                for (SoundEffect sound : MOVING_WARDEN_EXPLODE_SOUNDS) {
                    p.sendSound(sound);
                }
            }
        });
        if (movingWardenNPC != null) {
            movingWardenNPC.setAnimation(MOVING_WARDEN_DEATH_ANIMATION);
        }
        final StaticWardenNPC revivedWarden = elidinisStart ? tumekenWarden : elidinisWarden;
        if (revivedWarden != null) {
            revivedWarden.setAnimation(FLYING_WARDEN_REVIVE_ANIMATOON);
            revivedWarden.setGraphics(FLYING_WARDEN_REVIVE_GFX);
        }
        WorldTasksManager.schedule(addRunningTask(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                if (ticks == 0) {
                    for (Player p : getChallengePlayers()) {
                        if (p != null) {
                            p.getPacketDispatcher().resetCamera();
                            p.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, 4, 0, 0);
                            p.getPacketDispatcher().sendCameraShake(CameraShakeType.UP_AND_DOWN, 2, 0, 0);
                            p.getPacketDispatcher().sendCameraShake(CameraShakeType.FRONT_AND_BACK, 4, 0, 0);
                            p.sendSound(FLYING_WARDEN_REVIVE_SOUND);
                        }
                    }

                    flyingWardenNPC = new FlyingWardenNPC(elidinisStart ? FlyingWardenNPC.TUMEKEN_ID : FlyingWardenNPC.ELIDINIS_ID, getLocation(OBELISK_LOCATION).transform(0, 0, -1), WardenEncounter.this);
                    flyingWardenNPC.spawn();
                    flyingWardenNPC.setMaxHealth();
                    if (revivedWarden != null) {
                        revivedWarden.getCombatDefinitions().setHitpoints(flyingWardenNPC.getHitpoints());
                        revivedWarden.setHitpoints(0);
                        revivedWarden.applyHit(new Hit(movingWardenNPC, flyingWardenNPC.getHitpoints(), HitType.HEALED));
                        revivedWarden.removeObeliskHitBar();
                    }
                    if (obelisk != null) {
                        final Location middleTile = obelisk.getMiddleLocation();
                        final List<Location> obeliskExplosionTiles = new ArrayList<>();
                        for (int dx = -4; dx <= 4; dx++) {
                            for (int dy = -4; dy <= 4; dy++) {
                                if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
                                    obeliskExplosionTiles.add(middleTile.transform(dx, dy));
                                }
                            }
                        }
                        Collections.shuffle(obeliskExplosionTiles);
                        for (int i = 0; i < 7; i++) {
                            final int delay = i * 30 + 30;
                            World.sendSoundEffect(middleTile, new SoundEffect(WardensObelisk.SKULL_BOMB_THUNDER_SOUND.getId(), 15, delay));
                            obeliskExplosionTiles.subList(i * 5, i * 5 + 5).forEach(loc -> World.sendGraphics(new Graphics(Utils.random(1) == 0 ? 2157 : 2198, delay, 0), loc));
                        }
                    }
                } else if (ticks == 3) {
                    if (obelisk != null) {
                        obelisk.setAnimation(WardensObelisk.OBELISK_EXPLOSION_ANIMATION);
                    }
                } else if (ticks == 6) {
                    players.forEach(p -> {
                        if (p != null) {
                            p.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 174);
                            p.getPacketDispatcher().sendClientScript(948, 0, 255, 0, 0, 15);
                        }
                    });
                } else if (ticks == 9) {
                    startLastPhase();
                    stop();
                }
                ticks++;
            }
        }), 5, 0);
    }

    private void startLastPhase() {
        phase = 3;
        party.constructEncounter(EncounterType.WARDENS_SECOND_ROOM);
        if (party.getCurrentRaidArea() instanceof final SecondWardenEncounter encounter) {
            encounter.preparePhase(this, flyingWardenNPC);
        }
    }

    public int getPhase() { return phase; }

    public boolean isAncientHaste() { return ancientHaste; }

    public WardenCoreNPC getWardenCoreNPC() { return wardenCoreNPC; }

    public boolean isMovingWardenCanAttack() { return movingWardenNPC != null && movingWardenNPC.isCanAttack(); }

    public WardensObelisk getObelisk() { return obelisk; }

    static class WardensOsmumten extends NPC {

        private final WardenEncounter wardenEncounter;

        public WardensOsmumten(int id, Location tile, WardenEncounter encounter) {
            super(id, tile, Direction.NORTH, 0);
            this.wardenEncounter = encounter;
        }

        public void flyAway() {
            setAnimation(OSMUMTEN_FLY_AWAY_ANIM);
            WorldTasksManager.schedule(wardenEncounter.addRunningTask(() -> {
                if (!isFinished()) {
                    finish();
                }
            }), 1);
        }

        @Override public void setRespawnTask() {}

        @Override public void setTarget(Entity target, TargetSwitchCause cause) {}

        @Override public void setFaceLocation(Location tile) {}

        @Override public boolean isEntityClipped() { return false; }

        @Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }
    }
}
