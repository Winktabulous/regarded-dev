package com.zenyte.game.content.chambersofxeric.map;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.GameConstants;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.chambersofxeric.MountQuidamortemArea;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.RoomController;
import com.zenyte.game.content.chambersofxeric.greatolm.OlmRoom;
import com.zenyte.game.content.chambersofxeric.npc.JewelledCrab;
import com.zenyte.game.content.chambersofxeric.npc.RaidNPC;
import com.zenyte.game.content.chambersofxeric.party.RaidParty;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.music.Music;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.*;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.utils.TimeUnit;
import mgi.types.config.ObjectDefinitions;
import mgi.utilities.CollectionUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.zenyte.game.world.region.Region.CHUNK_SIZE;

/**
 * @author Kris | 12. juuli 2018 : 22:27:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class RaidArea extends DynamicArea implements RoomController, DropPlugin, DeathPlugin, HitProcessPlugin, CannonRestrictionPlugin, TeleportPlugin, LogoutPlugin, LogoutRestrictionPlugin, LayableObjectPlugin {
    private static final Logger log = LoggerFactory.getLogger(RaidArea.class);
    /**
     * The raid for which the area is built.
     */
    protected final Raid raid;
    /**
     * The index of the room layout out of the row on the static map. Each room has three versions of it, index defines which of the three it is, with values from 0 to 2.
     */
    protected final int index;
    /**
     * The rotation and from/to height levels of the area.
     */
    private final int rotation;
    private final int fromPlane;
    private final int toPlane;
    /**
     * The type of the room, or null if it's the Olm room.
     */
    private final RaidRoom type;
    /**
     * The bound tile to which the player is moved to when they climb up/down from the "attached" room on another height level.
     */
    private Location boundTile;
    /**
     * The time in game ticks when the room was entered.
     */
    private long enterTime;
    /**
     * The time in game ticks when the room was left, does not count if person leaves it the same way they entered. They must progress to the next room for it to count.
     */
    private long leaveTime;
    /**
     * The remaining combat points that can be granted within this raid room. Each room has a cap of points to avoid farming high tier rewards.
     */
    private int remainingCombatPoints;

    public RaidArea(final RaidRoom type, final Raid raid, final int rotation, final int size, final int regionX, final int regionY, final int chunkX, final int chunkY, final int fromPlane, final int toPlane) {
        super(size, size, regionX, regionY, chunkX, chunkY);
        this.type = type;
        this.rotation = rotation;
        this.fromPlane = fromPlane;
        this.toPlane = toPlane;
        this.raid = raid;
        index = regionX == 408 ? 0 : regionX == 412 ? 1 : 2;
    }

    /**
     * Calculates the remaining combat points for this room and sets the variable to that. Players in the raid can only get the equivalent amount of points for combat activities in
     * this room.
     */
    public void calculateRemainingCombatPoints() {
        this.remainingCombatPoints = buildPointsCap().build();
    }

    @Override
    public boolean drop(final Player player, final Item item) {
        if (this.raid.getStage() == 0) {
            player.sendMessage("You should keep your raiding dungeon tidy until your raid has begun.");
            return false;
        }
        return true;
    }

    @Override
    public boolean canTeleport(final Player player, final Teleport teleport) {
        player.sendMessage("You cannot teleport from the Chambers of Xeric.");
        return false;
    }

    public String restrictionMessage() {
        return "There's a sinister power at work in this cave, and it'll interfere with your cannon.";
    }

    @Override
    public boolean isRaidArea() {
        return true;
    }

    /**
     * Calculates the location of the crystal blocking the exit.
     */
    protected Location getCrystalTile(final Location location) {
        final ObjectDefinitions defs = Objects.requireNonNull(ObjectDefinitions.get(30018));
        final int sizeX = defs.getSizeX();
        final int sizeY = defs.getSizeY();
        return getObjectLocation(location, sizeX, sizeY, getRotation());
    }

    @Override
    public boolean manualLogout(final Player player) {
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                options("Are you sure you wish to log out?", new DialogueOption("Yes", () -> player.logout(false)), new DialogueOption("No."));
            }
        });
        return false;
    }

    protected Location[] getStorageUnitLocations() {
        return null;
    }

    /**
     * Refreshes the storage unit hotspot objects across the raid by spawning the upgraded version in them.
     * @param storageUnitId the upgraded storage unit object id.
     */
    public void refreshStorageUnits(final int storageUnitId) {
        final Location[] units = getStorageUnitLocations();
        if (units == null) {
            return;
        }
        if (index >= units.length) {
            return;
        }
        final Location tile = getLocation(units[index]);
        final WorldObject existingObject = World.getObjectWithType(tile, 10);
        if (existingObject == null || !Objects.requireNonNull(existingObject.getName()).toLowerCase().contains("storage unit")) {
            return;
        }
        World.spawnObject(new WorldObject(storageUnitId, existingObject.getType(), existingObject.getRotation(), existingObject));
    }

    /**
     * Gets the highest level in the requested skill out of the players still in the raid.
     *
     * @param skill the skills which to check.
     * @return the highest level, or a minimum of 1, if no one is present.
     */
    protected int getHighestLevel(final int skill) {
        int level = 1;
        for (final Player player : raid.getPlayers()) {
            final int lv = player.getSkills().getLevelForXp(skill);
            if (lv > level) {
                level = lv;
            }
        }
        return level;
    }

    /**
     * Gets the lowest level in the requested skill out of the players still in the raid.
     *
     * @param skill the skill which to check.
     * @return the lowest level, or a maximum of 99 if no one is present.
     */
    protected int getLowestLevel(final int skill) {
        int level = 99;
        for (final Player player : raid.getPlayers()) {
            final int lv = player.getSkills().getLevelForXp(skill);
            if (lv < level) {
                level = lv;
            }
        }
        return level;
    }

    @Override
    public void constructRegion() {
        try {
            if (constructed) {
                return;
            }
            GlobalAreaManager.add(this);
            try {
                MapBuilder.copySquare(area, 4, staticChunkX, staticChunkY, fromPlane, chunkX, chunkY, toPlane, rotation);
            } catch (Exception e) {
                log.error("", e);
            }
            constructed = true;
            constructed();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public Location getLocation(final int x, final int y, final int height) {
        final int offsetX = x - (staticChunkX * 8);
        final int offsetY = y - (staticChunkY * 8);
        final double radians = Math.toRadians((rotation == 1 ? 3 : rotation == 3 ? 1 : rotation) * 90);
        final int transformedX = (int) Math.round(15.5 + ((offsetX - 15.5) * Math.cos(radians)) - ((offsetY - 15.5) * Math.sin(radians)));
        final int transformedY = (int) Math.round(15.5 + ((offsetX - 15.5) * Math.sin(radians)) + ((offsetY - 15.5) * Math.cos(radians)));
        return new Location((chunkX * 8) + transformedX, (chunkY * 8) + transformedY, toPlane);
    }

    @Override
    public Location getLocation(final Location tile) {
        return getLocation(tile.getX(), tile.getY(), tile.getPlane());
    }

    /**
     * Gets a tile specific to a NPC based on the input size. As the position of NPCs is always the south-western tile, unrelated to the room rotation, we need to account for that.
     *
     * @param tile    the tile which to transform.
     * @param npcSize the size of the npc.
     * @return the transformed tile accounting for the size of the NPC.
     */
    public Location getNPCLocation(final Location tile, final int npcSize) {
        return getObjectLocation(tile, npcSize, npcSize, rotation);
    }

    /**
     * Gets the object's transformed location based on the input tile, width and height of the object and the rotation of it.
     *
     * @param tile     the tile which to transform.
     * @param sizeX    the width of the object.
     * @param sizeY    the height of the object.
     * @param rotation the rotation of the object.
     * @return the transformed tile.
     */
    public Location getObjectLocation(final Location tile, final int sizeX, final int sizeY, final int rotation) {
        final Location t = getLocation(tile);
        final int sx = sizeX / 2;
        final int sy = sizeY / 2;
        return switch (rotation) {
            case 0 -> t;
            case 1 -> new Location(t.getX(), t.getY() - sx, t.getPlane());
            case 2 -> new Location(t.getX() - sx, t.getY() - sy, t.getPlane());
            default -> new Location(t.getX() - sy, t.getY(), t.getPlane());
        };
    }

    /**
     * Gets the respective tile out of the three input values, based on whichever room was copied off of the static map.
     *
     * @param west  the western room's location.
     * @param north the northern room's location.
     * @param east  the eastern room's location.
     * @return the position out of the lot, based on the room used in this area.
     */
    public Location getRespectiveTile(final Location west, final Location north, final Location east) {
        if (staticChunkX == 408) {
            return west;
        } else if (staticChunkX == 412) {
            return north;
        }
        return east;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ", Rotation: " + rotation + ", static: [" + staticChunkX + ", " + staticChunkY + ", " + fromPlane + "] + dynamic: [" + chunkX + ", " + chunkY + ", " + toPlane + "], ";
    }

    @Override
    public void constructed() {
    }

    @Override
    protected void cleared() {
    }

    @Override
    public void enter(final Player player) {
        if (enterTime == 0) {
            enterTime = WorldThread.WORLD_CYCLE;
        }
        if (raid == null) {
            return;
        }
        final Set<String> originalPlayers = raid.getOriginalPlayers();
        if (!originalPlayers.isEmpty() && !originalPlayers.contains(player.getUsername()) && !player.getPrivilege().inherits(PlayerPrivilege.ADMINISTRATOR)) {
            player.lock(1);
            player.sendMessage("You do not belong in this raid.");
            player.setLocation(Raid.outsideTile);
            return;
        }
        raid.refreshParty();
        raid.readd(player);
        final int height = player.getPlane();
        if (height == 3) {
            player.getMusic().unlock(Music.get("Upper Depths"));
        } else if (height == 2) {
            player.getMusic().unlock(Music.get("Lower Depths"));
        }
        updateStage(player);
    }

    /**
     * Updates the stage of the raid for all players in it. Whenever a player goes further in the raid, e.g. down a ladder to the lower floor, the raid tab information updates to
     * reflect on it.
     *
     * @param player the player whose location to check and update if necessary.
     */
    private void updateStage(@NotNull final Player player) {
        final int plane = player.getPlane();
        final int stage = raid.getStage();
        if (plane >= 3) {
            return;
        }
        final boolean update = plane == 2 && stage <= 1 || plane == 1 && stage <= 2 || type == null;
        if (!update) {
            return;
        }
        final RaidArea room = type == null ? this : CollectionUtils.findMatching(raid.getMap().getRaidChunks(), chunk -> chunk.getToPlane() == plane && (chunk.getType() == RaidRoom.FLOOR_START_UPSTAIRS || chunk.getType() == null));
        if (room == null) {
            return;
        }
        final Location boundTile = room.getBoundTile();
        if (boundTile == null) {
            return;
        }
        //Set raid as loaded if it hasn't been loaded yet, we're switching floors so it needs to be initiated by this time.
        raid.load();
        if (plane == 2) {
            if (stage <= 1) {
                raid.setStage(2);
            }
        } else if (plane == 1) {
            if (stage <= 2) {
                raid.setStage(3);
            }
        } else if (plane == 0) {
            if (stage <= 3) {
                raid.setStage(4);
            }
        }
        if (stage == raid.getStage()) {
            return;
        }
        final RaidParty party = raid.getParty();
        raid.getPlayers().forEach(party::refreshTab);
        raid.setRespawnTile(boundTile);
    }

    @Override
    public void leave(final Player player, boolean logout) {
        final RegionArea nextArea = GlobalAreaManager.getArea(player.getLocation());
        if (logout || !(nextArea instanceof RaidArea)) {
            player.stopAll();
            player.getVariables().resetScheduled();
            player.reset();
            player.getVarManager().sendBit(5425, 0);
            if (player.getInventory().containsItem(20884, 1)) {
                World.spawnFloorItem(new Item(20884), new Location(player.getLastLocation()), player, -1, -1);
                player.getInventory().deleteItem(20884, 28);
                player.sendMessage("Your keystone crystal was dropped underneath you.");
            }
            final MutableBoolean contained = new MutableBoolean();
            if (raid != null) {
                raid.dropInternalItems(player, new Location(player.getLastLocation()));
                contained.setValue(raid.getPlayers().remove(player));
                if ((raid.getStage() != 0 && raid.getStage() != 4) || raid.getPlayers().isEmpty()) {
                    MountQuidamortemArea.appiontAnotherPartyLeader(player);
                    raid.destroy(true, false);
                }
                raid.stashRewards(player);
                raid.refreshTab();
                for (final Player p : raid.getPlayers()) {
                    raid.getParty().refreshTab(p);
                }
                if (logout) {
                    player.getAttributes().put("lastRaidIndex", raid.getIndex());
                }
            }
            player.getPacketDispatcher().resetCamera();
            player.blockIncomingHits(5);
            player.getVarManager().sendVar(1431, 0);
            GameInterface.GROUPING_TAB.open(player);
            if (raid == null || contained.isTrue()) {
                player.getInterfaceHandler().closeInterface(GameInterface.RAID_OVERLAY);
            }
            player.setViewDistance(Player.SMALL_VIEWPORT_RADIUS);
        }
        if (this.leaveTime == 0 && !logout && nextArea instanceof RaidArea && (!player.getPrivilege().inherits(PlayerPrivilege.ADMINISTRATOR) || GameConstants.WORLD_PROFILE.isDevelopment())) {
            if (raid == null) {
                return;
            }
            final RaidMap map = raid.getMap();
            if (map == null) {
                return;
            }
            final int thisIndex = raid.getMap().getRaidChunks().indexOf(this);
            final int nextIndex = thisIndex == raid.getMap().getRaidChunks().size() - 1 && nextArea instanceof OlmRoom ? (thisIndex + 1) : raid.getMap().getRaidChunks().indexOf(nextArea);
            if (thisIndex == -1 || nextIndex == -1) {
                return;
            }
            if (nextIndex > thisIndex) {
                this.leaveTime = WorldThread.WORLD_CYCLE;
            }
        }
    }

    @Override
    public Location onLoginLocation() {
        return Raid.outsideTile;
    }

    @Override
    public String name() {
        return "Chambers of Xeric";
    }

    @Override
    public boolean inside(final Location location) {
        final int plane = location.getPlane();
        if (plane != toPlane) {
            return false;
        }
        final int x = location.getX();
        final int y = location.getY();
        final int areaX = chunkX * CHUNK_SIZE;
        final int areaY = chunkY * CHUNK_SIZE;
        final int areaLimitX = areaX + (sizeX * CHUNK_SIZE);
        final int areaLimitY = areaY + (sizeY * CHUNK_SIZE);
        return x >= areaX && x < areaLimitX && y >= areaY && y < areaLimitY;
    }

    @Override
    public int visibleTicks(final Player player, final Item item) {
        return -1;
    }

    @Override
    public int invisibleTicks(final Player player, final Item item) {
        return Raid.isInternalItem(item.getId()) ? -1 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isSafe() {
        return true;
    }

    @Override
    public boolean loseHardcoreIronGroupLive() {
        return true;
    }

    @Override
    public String getDeathInformation() {
        return "Deaths within the Chambers of Xerics are always safe.";
    }

    @Override
    public Location getRespawnLocation() {
        return null;
    }

    @Override
    public boolean sendDeath(final Player player, final Entity source) {
        player.setAnimation(Animation.STOP);
        player.lock();
        player.stopAll();
        player.getTemporaryAttributes().remove("acidDrip");
        player.getPacketDispatcher().resetCamera();
        player.getTemporaryAttributes().put("deathSpot", new Location(player.getLocation()));
        if (player.getPrayerManager().isActive(Prayer.RETRIBUTION)) {
            player.getPrayerManager().applyRetributionEffect(source);
        }
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                if (player.isFinished() || player.isNulled()) {
                    stop();
                    return;
                }
                if (raid.isDestroyed()) {
                    stop();
                    return;
                }
                if (ticks == 1) {
                    player.setAnimation(Player.DEATH_ANIMATION);
                } else if (ticks == 2) {
                    dispatcher.sendClientScript(1512);
                } else if (ticks == 3) {
                    dispatcher.sendClientScript(948, 0, 255, 0, 0, 15);//Fade
                    dispatcher.sendClientScript(1513);//fade in
                    final int points = raid.getPoints(player);
                    final int totalPoints = raid.getTotalPoints();
                    float deathPointsDecrease = getDeathPointsDecrease(player);
                    if (!raid.usingFakeScale && points >= 8400) {
                        raid.decrementPoints(player, (int) (points * deathPointsDecrease));
                    } else {
                        raid.decrementGroupPoints();
                    }
                    final StringBuilder builder = new StringBuilder(100);
                    final RaidRoom type = getType();
                    builder.append(player.getUsername())
                            .append(": Death at ")
                            .append(type == null ? "Great Olm" : type.getFormattedName())
                            .append(" room by ")
                            .append(Optional.ofNullable(PlayerAttributesKt.getKillingBlowHit(player))
                                    .map(Hit::getSource)
                                    .map(entity -> entity instanceof NPC ? ((NPC) entity).getName(player) : "A player")
                                    .orElse("Unknown source"))
                            .append(", losing ");
                    if (points >= 8400) {
                        builder.append((int) (points * deathPointsDecrease)).append(" personal points.");
                    } else {
                        builder.append((totalPoints - (int) (totalPoints * 0.9F))).append(" group points.");
                    }
                    raid.recordDeath(builder.toString());
                    player.reset();
                    player.blockIncomingHits(5);
                    final Inventory inventory = player.getInventory();
                    for (int i = 0; i < 28; i++) {
                        final Item item = inventory.getItem(i);
                        if (item == null) {
                            continue;
                        }
                        final int id = item.getId();
                        if (Raid.isInternalItem(id)) {
                            World.spawnFloorItem(item, player, -1, -1);
                            inventory.set(i, null);
                        }
                    }
                    inventory.refreshAll();
                    if (player.getInventory().containsItem(20884, 1)) {
                        World.spawnFloorItem(new Item(20884), player, -1, -1);
                        player.getInventory().deleteItem(20884, 1);
                        player.sendMessage("Your keystone crystal was dropped underneath you.");
                    }
                    player.setAnimation(Animation.STOP);
                    if (player.getVariables().isSkulled()) {
                        player.getVariables().setSkull(false);
                    }
                    player.setLocation(raid.getRespawnTile());
                } else if (ticks == 4) {
                    player.unlock();
                    player.getAppearance().resetRenderAnimation();
                    player.setAnimation(Animation.STOP);
                    stop();
                }
                ticks++;
            }
        }, 0, 1);
        return true;
    }

    private static float getDeathPointsDecrease(Player player) {
        return switch (player.getMemberRank()) {
            case PREMIUM -> 0.35F;
            case EXPANSION -> 0.33F;
            case EXTREME -> 0.3F;
            case RESPECTED -> 0.28F;
            case LEGENDARY -> 0.25F;
            case MYTHICAL -> 0.23F;
            case UBER, AMASCUT -> 0.2F;
            default -> 0.4F;
        };
    }
    @Override
    public boolean hit(final Player source, final Entity target, final Hit hit, final float modifier) {
        //You do not get points for hitting the crabs. It is processed within area code because crabs can be brought outside of their own room and then be slaughtered.
        if (target instanceof JewelledCrab crab) {
            hit.setDamage(0);
            if (hit.getHitType() == HitType.MELEE) {
                crab.setTransformation(7577);
            } else if (hit.getHitType() == HitType.RANGED) {
                crab.setTransformation(7578);
            } else if (hit.getHitType() == HitType.MAGIC) {
                crab.setTransformation(7579);
            }
            crab.setTime(Utils.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5));
            return true;
        }
        if (hit.getDamage() > 0) {
            if (target instanceof RaidNPC) {
                if (!((RaidNPC<?>) target).grantPoints()) {
                    return true;
                }
            }
            final float maximum = (Math.min(hit.getDamage(), target.getHitpoints())) * 5.0F;
            final float actualPointsBasedOnDamage = hit.getDamage() * ((target instanceof RaidNPC ? ((RaidNPC<?>) target).getPointsMultiplier(hit) : target.getXpModifier(hit)) * 5.0F);
            raid.addCombatPoints(source, (int) Math.min(maximum, actualPointsBasedOnDamage));
        }
        return true;
    }

    @Override
    public boolean canLay(@NotNull final Player player, @NotNull final LayableObjectType type) {
        if (type == LayableObjectType.MITHRIL_SEED) {
            player.sendMessage("You can't grow flowers here.");
            return false;
        }
        return true;
    }

    @Override
    public void onLogout(@NotNull Player player) {

    }

    public Raid getRaid() {
        return raid;
    }

    public int getIndex() {
        return index;
    }

    public int getRotation() {
        return rotation;
    }

    public int getFromPlane() {
        return fromPlane;
    }

    public int getToPlane() {
        return toPlane;
    }

    public RaidRoom getType() {
        return type;
    }

    public Location getBoundTile() {
        return boundTile;
    }

    public void setBoundTile(Location boundTile) {
        this.boundTile = boundTile;
    }

    public long getEnterTime() {
        return enterTime;
    }

    public long getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(long leaveTime) {
        this.leaveTime = leaveTime;
    }

    public int getRemainingCombatPoints() {
        return remainingCombatPoints;
    }

    public void setRemainingCombatPoints(int remainingCombatPoints) {
        this.remainingCombatPoints = remainingCombatPoints;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

}
