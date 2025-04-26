package com.near_reality.game.content.gauntlet;

import com.near_reality.game.content.gauntlet.rewards.GauntletRewardType;
import com.near_reality.game.world.entity.player.TempIntefaceHandlerKt;
import com.zenyte.game.GameInterface;
import com.near_reality.game.content.gauntlet.map.GauntletMap;
import com.near_reality.game.content.gauntlet.map.GauntletRoomType;
import com.near_reality.game.content.gauntlet.hunllef.Hunllef;
import com.near_reality.game.content.gauntlet.hunllef.HunllefType;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.BossTimer;
import com.zenyte.game.world.entity.player.CombatDefinitions;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.Pair;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import static com.near_reality.game.content.gauntlet.GauntletConstants.*;
import static com.zenyte.game.item.ItemId.*;

/**
 * Handles functionality for a single running instance of The Gauntlet minigame.
 *
 * @author Andys1814.
 * @since 1/19/2022.
 */
public final class Gauntlet {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Gauntlet.class);

    /** Player gets finite time to prepare before being forced into the boss fight. */
    private long prepTime;

    private final Player player;

    private final GauntletType type;

    private GauntletStage stage = GauntletStage.PREP;

    private final TickTask timer;

    private final GauntletMap map;

    private long startTime;
    private long totalPrepTicks;
    private long hunleffTime;
    private Hunllef hunllef;
    public int attunedWeaponCount;
    public boolean armorMade;

    public Gauntlet(Player player, GauntletType type, GauntletMap map) {
        this.player = player;
        this.type = type;
        this.timer = initTimer();
        this.map = map;

        // Compute prep time depending on the type of this Gauntlet instance.
        if (type == GauntletType.STANDARD) {
            totalPrepTicks = TimeUnit.MINUTES.toTicks(10);
        } else {
            totalPrepTicks = TimeUnit.MINUTES.toTicks(7) + TimeUnit.SECONDS.toTicks(30);
        }

        prepTime = totalPrepTicks;
    }

    public static Gauntlet construct(Player player, GauntletType type) {
        try {
            // Find a 14x14 grid of empty chunks.
            AllocatedArea area = MapBuilder.findEmptyChunk(GauntletMap.ROOM_WIDTH * 2, GauntletMap.ROOM_HEIGHT * 2);

            // Construct our Gauntlet map with the allocated area that was found above.
            GauntletMap map = new GauntletMap(area, type);
            map.constructRegion();

            return new Gauntlet(player, type, map);
        } catch (OutOfSpaceException e) {
            log.error("", e);
            return null;
        }
    }

    public void start() {
        player.reset();

        new FadeScreen(player, () -> {
            Location startingLocation = new Location(map.getBaseXForNode(map.getStartingRoomX()) + Utils.random(5, 6), map.getBaseYForNode(map.getStartingRoomY()) + Utils.random(5, 6), 1);

            player.getInventoryTemp().getContainer().clear();
            player.getEquipmentTemp().getContainer().clear();

            player.setLocation(startingLocation);

            player.sendMessage("You enter the Gauntlet.");

            final CombatDefinitions combatDefinitions = player.getCombatDefinitions();
            combatDefinitions.setAutocastSpell(null);
            combatDefinitions.refresh();
            player.getAppearance().resetRenderAnimation();

            /* Initialize our TickTask timer that starts the boss fight after prep time is over. */
            WorldTasksManager.schedule(timer, 0, 0);
            startTime = System.currentTimeMillis();
            player.getBossTimer().startTracking("gauntlet");

            // "plugin system" they said :)
            TempIntefaceHandlerKt.tempInterfaceBind(player, GameInterface.ORBS, "View World Map", this::openMap);

            player.getVarManager().sendBit(9292, type.getVarbit()); // Varbit to define type of gauntlet (standard or corrupted)
            player.getVarManager().sendBitInstant(9178, 1); // Varbit to replace world map with Gauntlet Map.

            player.getPacketDispatcher().sendClientScript(2914, (int) prepTime);
            player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 637);

            // Reset map varbits
            for (int i = 9240; i < 9240 + 49; i++) {
                int bitValue = player.getVarManager().getBitValue(i);
                if (bitValue == 1) {
                    player.getVarManager().sendBit(i, 0);
                }
            }

            player.getVarManager().sendBitInstant(9291, (map.getStartingRoomY() * 7) + map.getStartingRoomX()); // Starting room.

            int[] roomCoordinates = map.chunkToRoomCoords(player.getLocation().getChunkX(), player.getLocation().getChunkY());
            player.getVarManager().sendBitInstant(9289, roomCoordinates[0]);
            player.getVarManager().sendBitInstant(9290, roomCoordinates[1]);

            player.getVarManager().sendBitInstant(9240 + (3 * 7) + 3, 1); // Mark hunllef room
            player.getVarManager().sendBitInstant(9240 + (map.getStartingRoomY() * 7) + map.getStartingRoomX(), 1); // mark starting room
            player.putBooleanTemporaryAttribute("egniol_potion_made", false);
        }).fade(2);

        // Base x and y coordinates of the chunk
        int bossRoomBaseX = map.getBaseXForNode(3);
        int bossRoomBaseY = map.getBaseYForNode(3);
        Location hunllefLocation = new Location(bossRoomBaseX + 6, bossRoomBaseY + 7, 1);

        // Base x and y coordiantes of the walkable room.
        // Used to calculate tornado and tile offsets during the boss fight.
        int bossWalkableBaseX = bossRoomBaseX + 2;
        int bossWalkableBaseY = bossRoomBaseY + 2;

        // Spawn Hunllef
        HunllefType hunllefType = HunllefType.random();
        hunllef = new Hunllef(hunllefType, type, hunllefLocation, bossWalkableBaseX, bossWalkableBaseY, this);
        hunllef.spawn();

        GauntletPlayerAttributesKt.setGauntlet(player, this);
    }

    public void openMap(Player player) {
        int[] roomCoordinates = map.chunkToRoomCoords(player.getLocation().getChunkX(), player.getLocation().getChunkY());

        player.getVarManager().sendBitInstant(9289, roomCoordinates[0]);
        player.getVarManager().sendBitInstant(9290, roomCoordinates[1]);

        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 638); //1352
        player.getPacketDispatcher().sendClientScript(2908);
    }

    private TickTask initTimer() {
        return new TickTask() {
            @Override
            public void run() {
                if (map.getPlayers().isEmpty()) {
                    stop();
                    return;
                }

                if (prepTime > 0) {
                    prepTime--;

                    if (prepTime % 50 == 0) {
                        player.getPacketDispatcher().sendClientScript(2914, (int) prepTime);
                    } else if (prepTime < 15) {
                        player.getPacketDispatcher().sendClientScript(2914, (int) prepTime);
                    }
                }

                if (prepTime == 0) {
                    startBossPhase(true);
                }
            }
        };
    }

    public void startBossPhase() {
        startBossPhase(false);
    }

    public void startBossPhase(boolean force) {
        player.getVarManager().sendBitInstant(9177, 1); // Varbit to start boss phase
        stage = GauntletStage.BOSS;
        hunleffTime = WorldThread.getCurrentCycle();
        hunllef.onPlayerEntered();
        player.getHpHud().open(hunllef.getId(), hunllef.getMaxHitpoints());

        player.setAnimation(Animation.STOP);

        if (force) {
            // Teleport player to boss fight.
            Location location = new Location(map.getBaseXForNode(3) + 4, map.getBaseYForNode(3) + 4, 1);
            player.setLocation(location);
        }

        player.stopAll();
        player.getPacketDispatcher().sendClientScript(2916);

        // Stop timer
        timer.stop();
    }

    private boolean hasSceptre(Player player) {
        if (player.getEquipment().getId(EquipmentSlot.WEAPON) == CRYSTAL_SCEPTRE || player.getInventory().containsItem(CRYSTAL_SCEPTRE)) {
            return true;
        }

        return player.getEquipment().getId(EquipmentSlot.WEAPON) == CORRUPTED_SCEPTRE || player.getInventory().containsItem(CORRUPTED_SCEPTRE);
    }

    public void lightRoom(Player player, WorldObject object) {
        if (!hasSceptre(player)) {
            player.getDialogueManager().start(new PlainChat(player, "You need something to light the nodes with."));
            return;
        }

        // Compute the room coordinates of the object that was clicked by using its chunk x and y.
        int[] roomCoordinates = map.chunkToRoomCoords(object.getChunkX(), object.getChunkY());
        if (roomCoordinates[0] == map.getBossRoomX() && roomCoordinates[1] == map.getBossRoomY()) {
            return;
        }

        // Get the cartesian translation needed to produce a room in the correct direction.
        Direction litRoomDirection = object.getId() == UNLIT_NODE_RIGHT || object.getId() == 35998 ? object.getFaceDirection().getCounterClockwiseDirection(4) : object.getFaceDirection();
        Pair<Integer, Integer> translation = map.getTranslation(litRoomDirection);

        // Add the room with the new coordinates.
        int newRoomX = roomCoordinates[0] + translation.first();
        int newRoomY = roomCoordinates[1] + translation.second();
        if (newRoomX == map.getBossRoomX() && newRoomY == map.getBossRoomY()) {
            return;
        }

        // 1, 1 => 8
        // 0, 1

        int varbitId = 9240 + (newRoomY * 7) + newRoomX;
        player.getVarManager().sendBit(varbitId, 1);
        GauntletRoomType type = map.getRequiredNodeType(newRoomX, newRoomY);
        map.addRoom(player, type, newRoomX, newRoomY);

        player.sendMessage("You light the nodes in the corridor to help guide the way.");
    }

    public static void rollShards(Player player, boolean corrupted) {
        int shards = corrupted ? CORRUPTED_SHARDS : CRYSTAL_SHARDS;

        if (!player.getInventory().hasSpaceFor(shards)) {
            return;
        }

        int chance = Utils.random(4);
        if (chance > 0) {
            return;
        }

        int amount = Utils.random(5, 30);
        player.getInventory().addItem(shards, amount);

        player.sendMessage("You find " + amount + " " + (corrupted ? "corrupted" : "crystal") + " shards.");
    }

    public void end(GauntletRewardType rewardType, boolean leaveMessage, boolean instant) {
        GauntletPlayerAttributesKt.setGauntlet(player, null);
        GauntletPlayerAttributesKt.setGauntletWeakMonsterKills(player, 0);
        GauntletPlayerAttributesKt.setGauntletStrongMonsterKills(player, 0);
        GauntletPlayerAttributesKt.setGauntletReceivedWeaponFrame(player, false);
        GauntletPlayerAttributesKt.setGauntletReceivedBowString(player, false);
        GauntletPlayerAttributesKt.setGauntletReceivedSpike(player, false);
        GauntletPlayerAttributesKt.setGauntletReceivedOrb(player, false);
        player.getHpHud().close();
        player.getVarManager().sendBitInstant(9178, 0); // Reset world map
        player.getVarManager().sendBitInstant(9177, 0); // Varbit to end boss phase

        player.blockIncomingHits(5);
        final Runnable leaveAction = onLeave(rewardType, leaveMessage);
        if (instant)
            leaveAction.run();
        else
            new FadeScreen(player, leaveAction).fade(2);
    }

    @NotNull
    private Runnable onLeave(GauntletRewardType rewardType, boolean leaveMessage) {
        return () -> {
            player.setLocation(GauntletMap.OUTSIDE_LOCATION);

            // Reset skills, prayers, etc,.
            player.reset();

            if (leaveMessage)
                player.sendMessage("You leave the Gauntlet.");

            if (rewardType == GauntletRewardType.FULLY_COMPLETED) {
                long completionTime = System.currentTimeMillis() - player.getBossTimer().getCurrentTracker();
                long completionMinutes = TimeUnit.MILLISECONDS.toMinutes(completionTime);
                int prepTimeTotal = (int) (totalPrepTicks - prepTime);
                int hunleffKillTime = (int) (WorldThread.getCurrentCycle() - hunleffTime);
                player.sendMessage("Preparation time: " + Colour.RED.wrap(BossTimer.formatBestTime((int) TimeUnit.TICKS.toSeconds(prepTimeTotal))) + ". Hunllef kill time: " + Colour.RED.wrap(BossTimer.formatBestTime((int) TimeUnit.TICKS.toSeconds(hunleffKillTime))) + ".");

                if(player.containsItem(23859) && !player.getCollectionLog().getContainer().contains(23859, 1)) {
                    player.getCollectionLog().add(new Item(23859));
                }

                if (type == GauntletType.STANDARD || type == GauntletType.STANDARD_NO_PREP) {
                    /* No prep modes do not count toward global time bests or time-based Combat Achivements */
                    if(type == GauntletType.STANDARD) {
                        player.getBossTimer().inform("gauntlet", completionTime, "Challenge");
                        if (!player.hasPrivilege(PlayerPrivilege.ADMINISTRATOR))
                            GauntletModule.updateCompletionStatistics(completionTime);
                        else
                            player.sendMessage(Colour.RED.wrap("Your time was not counted towards global best cuz u " + Utils.random(REASONS_ADMINS_SUCK) + "."));
                        if (completionMinutes < 6) {//Original 5, but we make it 6
                            player.getCombatAchievements().complete(CAType.GAUNTLET_SPEED_CHASER);
                        }
                        if (completionMinutes < 5) {//Original 4, but we make it 5
                            player.getCombatAchievements().complete(CAType.GAUNTLET_SPEED_RUNNER);
                        }
                        int kc = player.getNumericAttribute("gauntlet_completions").intValue() + 1;
                        player.addAttribute("gauntlet_completions", kc);
                        player.sendMessage("Your Gauntlet completion count is: " + Colour.RED.wrap(kc) + ".");
                        if (kc >= 5) {
                            player.getCombatAchievements().complete(CAType.GAUNTLET_VETERAN);
                        }
                        if (kc >= 20) {
                            player.getCombatAchievements().complete(CAType.GAUNTLET_MASTER);
                        }
                    } else {
                        int kc = player.getNumericAttribute("gauntlet_completions_noprep").intValue() + 1;
                        player.addAttribute("gauntlet_completions_noprep", kc);
                        player.sendMessage("Your Gauntlet completion (no-prep) count is: " + Colour.RED.wrap(kc) + ".");
                    }

                } else {
                    /* No prep modes do not count toward global time bests or time-based Combat Achivements */
                    if(type == GauntletType.CORRUPTED) {
                        player.getBossTimer().inform("corrupt_gauntlet", completionTime, "Challenge");
                        if (!player.hasPrivilege(PlayerPrivilege.ADMINISTRATOR))
                            GauntletModule.updateCorruptedCompletionStatistics(completionTime);
                        else
                            player.sendMessage(Colour.RED.wrap("Your time was not counted towards global best cuz u " + Utils.random(REASONS_ADMINS_SUCK) + "."));

                        if (completionMinutes < 8) {//Original 7:30, but we make it 8
                            player.getCombatAchievements().complete(CAType.CORRUPTED_GAUNTLET_SPEED_CHASER);
                        }

                        final int gauntletCapeID = 23859;
                        if (!player.containsItem(gauntletCapeID)) {
                            player.getInventory().addOrDrop(new Item(gauntletCapeID));
                            player.sendMessage("For completing the Corrupted Gauntlet, you have been awarded a Gauntlet Cape.");
                            player.getCollectionLog().add(new Item(gauntletCapeID));
                        } else if(!player.getCollectionLog().getContainer().contains(23859, 1)) {
                            player.getCollectionLog().add(new Item(gauntletCapeID));
                        }

                        int kc = player.getNumericAttribute("corrupted_gauntlet_completions").intValue() + 1;
                        player.addAttribute("corrupted_gauntlet_completions", kc);
                        player.sendMessage("Your Corrupted Gauntlet completion count is: " + Colour.RED.wrap(kc) + ".");
                        if (kc >= 5) {
                            player.getCombatAchievements().complete(CAType.CORRUPTED_GAUNTLET_VETERAN);
                        }
                        if (kc >= 10) {
                            player.getCombatAchievements().complete(CAType.CORRUPTED_GAUNTLET_MASTER);
                        }
                        if (kc >= 50) {
                            player.getCombatAchievements().complete(CAType.CORRUPTED_GAUNTLET_GRANDMASTER);
                        }
                    } else {
                        int kc = player.getNumericAttribute("corrupted_gauntlet_completions_noprep").intValue() + 1;
                        player.addAttribute("corrupted_gauntlet_completions_noprep", kc);
                        player.sendMessage("Your Corrupted Gauntlet completion (no-prep) count is: " + Colour.RED.wrap(kc) + ".");
                    }

                }
            }

            if (rewardType != GauntletRewardType.NONE) {
                GauntletPlayerAttributesKt.setGauntletTypeForReward(player, type);
                GauntletPlayerAttributesKt.setGauntletRewardType(player, rewardType);
                player.sendMessage("Your reward awaits you in the nearby chest.");
            }

            player.getVarManager().sendBit(9178, 0); // Reset world map

            TempIntefaceHandlerKt.removeTempInterfaceBind(player, GameInterface.ORBS, "View World Map");

            sendRewardChestVarbit(player);
            sendCompletionVarbit(player);
        };
    }

    public static String[] REASONS_ADMINS_SUCK = new String[] {
            "smell", "stink", "reek", "are not as cool as Stan", "are a pleb", "are submissive to Stan",
            "cannot win without cheating", "have a smoll pp"
    };

    public static void sendRewardChestVarbit(Player player) {
        int value = GauntletPlayerAttributesKt.getGauntletRewardType(player) != null ? 1 : 0;
        VarManager.appendPersistentVarbit(9179);
        player.getVarManager().sendBit(9179, value);
    }

    public static void sendCompletionVarbit(Player player) {
        int value = player.getNumericAttribute("gauntlet_completions").intValue() > 0 || player.getNumericAttribute("corrupted_gauntlet_completions").intValue() > 0 ? 1 : 0;
        VarManager.appendPersistentVarp(2353);
        player.getVarManager().sendVar(2353, value);
    }

    public GauntletMap getMap() {
        return map;
    }

    public GauntletStage getStage() {
        return stage;
    }

    public Hunllef getHunllef() {
        return hunllef;
    }

    public long getPrepTime() {
        return prepTime;
    }

}
