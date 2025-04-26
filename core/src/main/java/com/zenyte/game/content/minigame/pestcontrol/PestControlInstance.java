package com.zenyte.game.content.minigame.pestcontrol;

import com.zenyte.cores.CoresManager;
import com.zenyte.game.content.achievementdiary.diaries.WesternProvincesDiary;
import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.content.minigame.pestcontrol.npc.BrawlerNPC;
import com.zenyte.game.content.minigame.pestcontrol.npc.PestPortalNPC;
import com.zenyte.game.content.minigame.pestcontrol.npc.SpinnerNPC;
import com.zenyte.game.content.minigame.pestcontrol.npc.VoidKnightNPC;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.LogoutPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import it.unimi.dsi.fastutil.objects.Object2ByteMap;
import it.unimi.dsi.fastutil.objects.Object2ByteOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.npcs.NPCDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.zenyte.game.content.minigame.pestcontrol.PestControlUtilities.*;

/**
 * @author Kris | 26. juuni 2018 : 18:14:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public final class PestControlInstance extends DynamicArea implements DeathPlugin, CycleProcessPlugin, CannonRestrictionPlugin {
    private static final Logger log = LoggerFactory.getLogger(PestControlInstance.class);
    private static final int ACTIVITY_FLAG = 1;
    private static final int DAMAGE_FLAG = 2;
    public static final int KNIGHT_FLAG = 8;
    private static final int SHIELDS_FLAG = 4;
    private static final int TIME_FLAG = 16;
    private static final Class<?>[] ARGUMENTS = new Class[] {PestControlInstance.class, PestPortalNPC.class, int.class, Location.class};
    private final Location[] barricadeTiles = new Location[PestControlUtilities.BARRICADES.length];
    private final Object2ByteOpenHashMap<Player> flagMap;
    private final Location[] gateTiles = new Location[PestControlUtilities.GATES.length];
    private final Map<Player, PestControlStatistic> mappedPlayers;
    private final List<PestNPC> monsters = new LinkedList<>();
    private final List<BrawlerNPC> brawlers = new LinkedList<>();
    private final List<PestPortal> availablePortals = new ArrayList<>(Arrays.asList(PestPortal.VALUES));
    private final Map<PestPortal, PestPortalNPC> portals = new HashMap<>(PestPortal.VALUES.length);
    private final PortalSequence sequence = PortalSequence.VALUES[Utils.random(PortalSequence.VALUES.length - 1)];
    private final PestControlGameType type;
    private final RSPolygon[] watchtowers = new RSPolygon[4];
    private int timer;
    private int updateFlag;
    private boolean finished;
    private VoidKnightNPC voidKnight;

    public PestControlInstance(final PestControlGameType type, final Map<Player, PestControlStatistic> initialPlayers, final AllocatedArea area) {
        super(area, INSTANCE_CHUNK_X, INSTANCE_CHUNK_Y);
        this.type = type;
        mappedPlayers = initialPlayers;
        flagMap = new Object2ByteOpenHashMap<>(mappedPlayers.size());
        for (int i = PestControlUtilities.WATCHTOWER_POLYGON_POINTS.length - 1; i >= 0; i--) {
            watchtowers[i] = getPolygon(PestControlUtilities.WATCHTOWER_POLYGON_POINTS[i], 0);
        }
        for (int i = barricadeTiles.length - 1; i >= 0; i--) {
            barricadeTiles[i] = this.getLocation(PestControlUtilities.BARRICADES[i]);
        }
        for (int i = gateTiles.length - 1; i >= 0; i--) {
            gateTiles[i] = this.getLocation(PestControlUtilities.GATES[i]);
        }
    }

    /**
     * Adds activity to the player.
     *
     * @param player the player whom to add activity to.
     * @param amount the amount of activity to add.
     */
    public void addActivity(final Player player, final int amount) {
        final PestControlStatistic statistic = mappedPlayers.get(player);
        if (statistic == null) {
            return;
        }
        statistic.incrementActivity(amount);
    }

    /**
     * Adds damage dealt by this given player to the damage map.
     *
     * @param player the player who dealt the damage.
     * @param damage the amount of damage that was dealt.
     */
    void addDamageDealt(final Player player, final PestNPC npc, final int damage) {
        if (damage <= 0) {
            return;
        }
        final PestControlStatistic statistic = mappedPlayers.get(player);
        if (statistic == null) {
            return;
        }
        statistic.incrementDamageDealt(damage);
        statistic.incrementActivity(npc instanceof PestPortalNPC || npc instanceof SpinnerNPC ? HIGH_ACTIVITY_POINTS : MODERATE_ACTIVITY_POINTS);
        flagPlayer(player, DAMAGE_FLAG);
        flagPlayer(player, ACTIVITY_FLAG);
    }

    /**
     * Adds a NPC to the {@link #monsters} list.
     *
     * @param npc the npc to be added to the list.
     */
    void addNPC(final PestNPC npc) {
        monsters.add(npc);
        npc.spawn();
    }

    /**
     * Removes a NPC to the {@link #monsters} list.
     *
     * @param npc the npc to be removed from the list.
     */
    void removeNPC(final PestNPC npc) {
        monsters.remove(npc);
    }

    @Override
    public void constructed() {
        voidKnight = new VoidKnightNPC(this);
        new NPC(LANDER_LEAVE_SQUIRE, getLocation(LANDER_SQUIRE_LOCATION), Direction.SOUTH, 0).spawn();
        for (final PestPortal portal : PestPortal.VALUES) {
            portals.put(portal, new PestPortalNPC(this, portal));
        }
        for (final Map.Entry<Player, PestControlStatistic> entry : mappedPlayers.entrySet()) {
            final Player player = entry.getKey();
            if (player.isNulled()) {
                continue;
            }
            player.setLocation(getLocation(getRandomLanderLocation()));
            player.getCombatDefinitions().setSpecialEnergy(100);
            player.getDialogueManager().start(new Dialogue(player, LANDER_LEAVE_SQUIRE) {
                @Override
                public void buildDialogue() {
                    npc("You must defend the Void Knight while the portals are unsummoned. The ritual takes twenty minutes though, so you can help out by destroying them yourselves! Now GO GO GO!");
                }
            });
            player.unlock();
        }
    }

    public WorldObject destroyRavagableObject(WorldObject trackedObject) {
        if (trackedObject.getId() < 14233) {
            final int id = trackedObject.getId() + 3;
            World.removeObject(trackedObject);
            World.spawnObject(trackedObject = new WorldObject(id, id >= 14230 ? 22 : trackedObject.getType(), trackedObject.getRotation(), trackedObject));
        } else {
            final int id = trackedObject.getId() + 4;
            World.removeObject(trackedObject);
            World.spawnObject(trackedObject = new WorldObject(id, id >= 14245 ? 22 : trackedObject.getType(), trackedObject.getRotation(), trackedObject));
        }
        return trackedObject;
    }

    @Override
    public void enter(final Player player) {
        if (!mappedPlayers.containsKey(player)) {
            mappedPlayers.put(player, new PestControlStatistic(HALF_FULL_ACTIVITY_PERCENTAGE_VALUE));
        }
        sendGameInterface(player);
        updateActivity(player, this);
        final PestControlStatistic statistic = mappedPlayers.get(player);
        if (statistic != null) {
            updateDamageDealt(player, statistic.getDamageDealt());
        }
        updateShields(player, this);
        updateTime(player, getMinutesRemaining());
        updateVoidKnight(player, voidKnight.getHitpoints());
    }

    /**
     * Flags the given activity to be updated for all players the following tick.
     *
     * @param flag the activity's flag to be updated.
     */
    public void flag(final int flag) {
        updateFlag |= flag;
    }

    /**
     * Flags a specific activity for the player to be updated the following tick.
     *
     * @param player the player whom to update.
     * @param flag   the flag that's being updated.
     */
    private void flagPlayer(final Player player, final int flag) {
        final byte mask = flagMap.getOrDefault(player, (byte) 0);
        if ((mask & flag) == 0) {
            flagMap.put(player, (byte) (mask | flag));
        }
    }

    /**
     * Gets the activity value of the player.
     *
     * @param player the player whose activity to obtain.
     * @return the activity value of the given player.
     */
    int getActivity(final Player player) {
        PestControlStatistic pestControlStatistic = mappedPlayers.get(player);
        if (pestControlStatistic == null) return 0;
        return Math.max(0, pestControlStatistic.getActivity());
    }

    /**
     * Gets the amount of minutes remaining until the game is considered successful by default.
     *
     * @return the time remaining in minutes.
     */
    private int getMinutesRemaining() {
        return (int) Math.ceil((GAME_DURATION - timer) / 100.0F);
    }

    public List<WorldObject> getRavagableObjects() {
        final ObjectArrayList<WorldObject> ravagableObjects = new ObjectArrayList<WorldObject>();
        for (final Location tile : barricadeTiles) {
            WorldObject object = World.getObjectWithType(tile, 9);
            if (object == null) {
                object = World.getObjectWithType(tile, 0);
            }
            if (object != null) {
                if (ArrayUtils.contains(PestControlUtilities.VALID_RAVAGABLE_OBJECTS, object.getId())) {
                    ravagableObjects.add(object);
                }
            }
        }
        for (final Location tile : gateTiles) {
            final WorldObject object = World.getObjectWithType(tile, 0);
            if (object != null) {
                if (ArrayUtils.contains(PestControlUtilities.VALID_RAVAGABLE_OBJECTS, object.getId())) {
                    ravagableObjects.add(object);
                }
            }
        }
        return ravagableObjects;
    }

    /**
     * Whether the player is currently located at the top of any of the four watchtowers or not.
     *
     * @param player the player whom to test against.
     * @return whether the player is at watchtower.
     */
    public boolean isAtWatchtower(final Player player) {
        for (int i = watchtowers.length - 1; i >= 0; i--) {
            if (watchtowers[i].contains(player.getLocation())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void leave(final Player player, boolean logout) {
        mappedPlayers.remove(player);
        player.getInterfaceHandler().closeInterface(InterfacePosition.MINIGAME_OVERLAY);
        if (logout) {
            player.forceLocation(type.getExitPoint());
        }
    }

    @Override
    public String name() {
        return "Pest control " + type.toString() + " instance";
    }

    public void finish(final boolean successful) {
        if (finished) {
            return;
        }
        finished = true;
        int skipCount = 0;
        for (final Player player : players) {
            if (player.isNulled() || player.isLocked() || player.isDead()) {
                skipCount++;
                continue;
            }
            exportPlayer(player, successful);
        }
        if (skipCount > 0) {
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    players.removeIf(Player::isNulled);
                    for (final Player player : players) {
                        if (player.isNulled() || player.isLocked() || player.isDead()) {
                            continue;
                        }
                        exportPlayer(player, successful);
                    }
                    if (players.isEmpty()) {
                        stop();
                    }
                }
            }, 0, 0);
        }
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                if (!players.isEmpty()) {
                    return;
                }
                //For iteration safety, we make an array and iterate that.
                for (final NPC npc : monsters.toArray(new NPC[0])) {
                    npc.finish();
                }
                stop();
            }
        }, 0, 0);
    }

    @Override
    public Location onLoginLocation() {
        return type.getExitPoint();
    }

    private void exportPlayer(final Player player, final boolean successful) {
        player.lock(1);
        player.setLocation(type.getExitPoint());
        player.blockIncomingHits();
        player.reset();
        player.getCombatDefinitions().setSpecialEnergy(100);
        final int activity = this.getActivity(player);
        WorldTasksManager.schedule(() -> {
            if (successful) {
                if (activity <= 0) {
                    player.getDialogueManager().start(new NPCChat(player, type.getSquireId(), "You failed to keep your activity up during the game."));
                    return;
                }
                int points = type.getPointsPerGame();
                final MemberRank rank = player.getMemberRank();
                if (rank.equalToOrGreaterThan(MemberRank.MYTHICAL)) {
                    points += 5;
                } else if (rank.equalToOrGreaterThan(MemberRank.LEGENDARY)) {
                    points += 4;
                } else if (rank.equalToOrGreaterThan(MemberRank.EXTREME)) {
                    points += 3;
                } else if (rank.equalToOrGreaterThan(MemberRank.PREMIUM)) {
                    points += 1;
                }
                if (player.getCombatAchievements().hasTierCompleted(CATierType.HARD)) {
                    points += 1;
                }
                if (player.getCombatAchievements().hasTierCompleted(CATierType.MEDIUM)) {
                    points += 1;
                }
                if (player.getCombatAchievements().hasTierCompleted(CATierType.EASY)) {
                    points += 1;
                }
                if (World.hasBoost(XamphurBoost.PEST_CONTROL)) {
                    points *= 1.5;
                }
                AdventCalendarManager.increaseChallengeProgress(player, 2022, 7, points);
                player.addAttribute("pest_control_points", player.getNumericAttribute("pest_control_points").intValue() + points);
                player.getInventory().addOrDrop(new Item(995, player.getSkills().getCombatLevel() * 250));
                if (type.equals(PestControlGameType.NOVICE)) {
                    player.getAchievementDiaries().update(WesternProvincesDiary.COMPLETE_PEST_CONTROL_NOVICE);
                } else if (type.equals(PestControlGameType.INTERMEDIATE)) {
                    player.getAchievementDiaries().update(WesternProvincesDiary.COMPLETE_INTERMEDIATE_PEST_CONTROL_GAME);
                } else if (type.equals(PestControlGameType.VETERAN)) {
                    player.getAchievementDiaries().update(WesternProvincesDiary.COMPLETE_VETERAN_PEST_CONTROL_GAME);
                }
                player.getDialogueManager().start(new NPCChat(player, type.getSquireId(), "Congratulations! You managed to " + (timer == GAME_DURATION ? "protect the Void Knight" : "destroy all the portals") + "! We've awarded you " + points + " Void Knight Commendation points. Please also accept these coins as a reward."));
            } else {
                player.getDialogueManager().start(new NPCChat(player, type.getSquireId(), "You failed to protect the Void Knight."));
            }
        });
    }

    @Override
    public void process() {
        if (finished) {
            return;
        }
        timer++;
        if (timer == GAME_DURATION) {
            finish(true);
            return;
        }
        if (timer >= 10 && Utils.random(2) == 0) {
            final int count = availablePortals.size();
            if (Utils.random(100) <= count * 50) {
                spawn();
            }
        }
        if (timer == 25 || timer == 75 || timer == 125 || timer == 175) {
            dropShield();
        }
        if (timer > 30 && timer % 3 == 0) {
            mappedPlayers.forEach((p, s) -> {
                if (p.isNulled()) {
                    return;
                }
                flagPlayer(p, ACTIVITY_FLAG);
                s.decrementActivity();
            });
        }
        if (timer % 100 == 0) {
            final int time = getMinutesRemaining();
            mappedPlayers.keySet().forEach(p -> {
                if (p.isNulled()) {
                    return;
                }
                updateTime(p, time);
            });
        }
        if (!flagMap.isEmpty()) {
            for (final Object2ByteMap.Entry<Player> entry : flagMap.object2ByteEntrySet()) {
                final Player player = entry.getKey();
                if (player.isNulled()) {
                    continue;
                }
                final byte mask = entry.getByteValue();
                if ((mask & ACTIVITY_FLAG) != 0) {
                    updateActivity(player, this);
                }
                if ((mask & DAMAGE_FLAG) != 0) {
                    final PestControlStatistic statistic = mappedPlayers.get(player);
                    if (statistic != null) {
                        updateDamageDealt(player, statistic.getDamageDealt());
                    }
                }
            }
            flagMap.clear();
        }
        if (updateFlag != 0) {
            for (final Player player : players) {
                if (player.isNulled()) {
                    continue;
                }
                if ((updateFlag & SHIELDS_FLAG) != 0) {
                    updateShields(player, this);
                }
                if ((updateFlag & KNIGHT_FLAG) != 0) {
                    updateVoidKnight(player, voidKnight.getHitpoints());
                }
                if ((updateFlag & TIME_FLAG) != 0) {
                    updateTime(player, 0);
                }
            }
        }
    }

    private Location getRandomSpawnTile(final int npcId, final PestPortal portal) {
        final NPCDefinitions npcDefinitions = NPCDefinitions.get(npcId);
        if (npcDefinitions == null) {
            return null;
        }
        if (availablePortals.isEmpty()) {
            return null;
        }
        final int size = npcDefinitions.getSize();
        final Location baseTile = portal.getNpcSpawnTile();
        int attempts = 50;
        while (--attempts > 0) {
            final int hash = getHash(Location.getHash(baseTile.getX() + Utils.random(3), baseTile.getY() + Utils.random(3), 0));
            final int x = (hash >> 14) & 16383;
            final int y = hash & 16383;
            if (World.isFloorFree(0, x, y, size)) {
                return new Location(hash);
            }
        }
        return null;
    }

    private void spawn() {
        if (monsters.size() >= 150 || availablePortals.isEmpty()) {
            return;
        }
        final PestNPCTable type = PestNPCTable.VALUES[Utils.random(PestNPCTable.VALUES.length - 1)];
        final int[] array = this.type == PestControlGameType.NOVICE ? type.getNoviceIds() : this.type == PestControlGameType.INTERMEDIATE ? type.getIntermediateIds() : type.getVeteranIds();
        final int id = array[Utils.random(array.length - 1)];
        final PestPortal portal = availablePortals.get(Utils.random(availablePortals.size() - 1));
        final Location tile = getRandomSpawnTile(id, portal);
        if (tile == null) {
            return;
        }
        final Class<? extends PestNPC> npcClass = type.getNpcClass();
        try {
            npcClass.getDeclaredConstructor(ARGUMENTS).newInstance(this, portals.get(portal), id, tile);
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    private void dropShield() {
        final int index = timer == 25 ? 0 : (timer / 50);
        final PestPortal portal = sequence.getPortals()[index];
        flag(SHIELDS_FLAG);
        final PestPortalNPC portalNPC = portals.get(portal);
        portalNPC.disableShield();
        mappedPlayers.forEach((p, s) -> {
            if (p.isNulled()) {
                return;
            }
            p.sendMessage(portal.getDropMessage());
        });
    }

    @Override
    protected void cleared() {
        CoresManager.getServiceProvider().executeWithDelay(() -> {
            if (players.isEmpty()) {
                destroyRegion();
            }
        }, 5, TimeUnit.SECONDS);
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
        return "You're in a " + Colour.RED + "safe area" + Colour.END + ".<br>If you die you will keep all items you brought with you.";
    }

    @Override
    public Location getRespawnLocation() {
        return getLocation(getRandomLanderLocation());
    }

    public List<BrawlerNPC> getBrawlers() {
        return brawlers;
    }

    public List<PestPortal> getAvailablePortals() {
        return availablePortals;
    }

    public Map<PestPortal, PestPortalNPC> getPortals() {
        return portals;
    }

    public PestControlGameType getType() {
        return type;
    }

    public VoidKnightNPC getVoidKnight() {
        return voidKnight;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

}
