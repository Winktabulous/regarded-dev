package com.zenyte.game.content.minigame.barrows;

import com.google.common.eventbus.Subscribe;
import com.zenyte.CacheManager;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.boons.impl.CryptKeeper;
import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.model.item.degradableitems.DegradableItem;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.ProjectileUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.RandomLocation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.RunePouch;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;
import com.zenyte.plugins.events.InitializationEvent;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import mgi.tools.jagcached.cache.Cache;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * @author Kris | 28/11/2018 21:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class Barrows {
    private static final int MAXIMUM_POTENTIAL = 1000;
    private static final int MOUND_RADIUS = 3;
    private static final int SHUT_DOORWAYS = 6;
    private static final int SLAIN_WIGHT_VARBIT = 457;
    private static final int POTENTIAL_VARBIT = 463;
    private static final int LADDER_VARBIT = 4743;
    private static final int MINIMUM_TIMER = 15;
    private static final int MAXIMUM_TIMER = 85;
    private static final int LADDER_DISTANCE = 16;
    private static final int SPAWN_ATTEMPT_COUNT = 100;
    private static final int DEFAULT_SPAWN_DISTANCE = 2;
    private static final int CRYPT_NPC_WEIGHT = 6;
    static final int CHEST_VARBIT = 1394;
    private static final IntArrayList cryptMonsters = IntArrayList.wrap(new int[] {1678, 1679, 1685, 1686, 1687, 1688});

    public static final int CA_TASK_MAGIC_DAMAGE_ONLY = 1;
    public static final int CA_TASK_NO_DAMAGE = 1;
    public static final int CA_TASK_NO_MELEE_DAMAGE_DHAROCK = 1;
    public static final int CA_TASK_NO_MELEE_DAMAGE_VERAC = 2;
    public static final int CA_TASK_NO_MELEE_DAMAGE_TORAG = 4;
    public static final int CA_TASK_NO_MELEE_DAMAGE_GUTHAN = 8;
    public static final int CA_TASK_FAITHLESS_RUN = 1;

    public Barrows(final Player player) {
        this.player = player;
        this.container = new Container(ContainerPolicy.ALWAYS_STACK, ContainerType.BARROWS_CHEST, Optional.empty());
        this.slainWights = new HashSet<>(BarrowsWight.values.length);
        this.shutDoorways = new HashSet<>(SHUT_DOORWAYS);
        this.puzzle = new BarrowsPuzzle(player);
        resetTimer();
        reset();
    }

    private final transient Player player;
    private final transient Container container;
    private transient int timer;
    private final transient BarrowsPuzzle puzzle;
    private boolean skipTunnels;
    private BarrowsWight hiddenWight;
    private Set<BarrowsWight> slainWights;
    private final Set<CryptDoorway> shutDoorways;
    private CryptDoorway openDoorway;
    private BarrowsCorner corner;
    private int potential;
    private boolean looted;
    private boolean puzzleSolved;
    private transient BarrowsWightNPC currentWight;

    public void setMaximumReward(final int rp) {
        this.potential = rp;
        slainWights.addAll(Arrays.asList(BarrowsWight.values));
    }

    @Subscribe
    public static final void onInit(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player parser = event.getSavedPlayer();
        final Barrows parserBarrows = parser.getBarrows();
        if (parserBarrows == null) return;
        final Barrows barrows = player.getBarrows();
        barrows.hiddenWight = parserBarrows.hiddenWight;
        barrows.slainWights = parserBarrows.slainWights;
        barrows.corner = parserBarrows.corner;
        barrows.potential = parserBarrows.potential;
        barrows.looted = parserBarrows.looted;
        barrows.skipTunnels = parserBarrows.skipTunnels;
    }

    public void resetTimer() {
        timer = 30;
    }

    /**
     * Gets the actual potential the player currently has accumulated.
     * @return full potential accumulated, including slain wights.
     */
    private int getFullPotential() {
        int potential = this.potential;
        for (final BarrowsWight slain : slainWights) {
            potential += slain.getCombatLevel();
        }
        return Math.min(MAXIMUM_POTENTIAL, potential) + (slainWights.size() << 1);
    }

    /**
     * Gets a random barrows wight still alive. Returns an empty optional if all are slain.
     * @return an optional barrows wight, or none if all are slain.
     */
    private Optional<BarrowsWight> getRandomAliveWight() {
        final ArrayList<BarrowsWight> list = Utils.getArrayList(BarrowsWight.values);
        list.removeAll(slainWights);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(Utils.random(list.size() - 1)));
    }

    /**
     * Gets a random barrows wight that has been already slain. Returns an empty optional if none have been slain.
     * @return an optional barrows wight, or none if none has been slain.
     */
    Optional<BarrowsWight> getRandomSlainWight() {
        if (slainWights.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Utils.getRandomCollectionElement(slainWights));
    }

    /**
     * Resets the player's barrows settings to new randomly generated ones.
     */
    public void reset() {
        hiddenWight = Utils.getRandomElement(BarrowsWight.values);
        corner = Utils.getRandomElement(BarrowsCorner.values);
        shiftDoorways();
        potential = 0;
        container.clear();
        looted = false;
        slainWights.clear();
        puzzleSolved = player != null && player.getPerkManager().isValid(PerkWrapper.RIDDLE_IN_THE_TUNNELS);
        skipTunnels = Utils.random(3) == 0;
    }

    void shiftCorner() {
        final BarrowsCorner oldCorner = corner;
        int tryCount = 100;
        while (--tryCount > 0 && corner == oldCorner) {
            corner = Utils.getRandomElement(BarrowsCorner.values);
        }
    }

    public void enter(final BarrowsWight wight) {
        player.sendMessage("You break into the crypt.");
        player.setLocation(wight.getInChamber());
    }

    public Optional<BarrowsWight> getMound() {
        final Location location = player.getLocation();
        for (final BarrowsWight wight : BarrowsWight.values) {
            if (location.withinDistance(wight.getMoundCenter(), MOUND_RADIUS)) {
                return Optional.of(wight);
            }
        }
        return Optional.empty();
    }

    void refreshInterface() {
        final VarManager varManager = player.getVarManager();
        for (final BarrowsWight wight : BarrowsWight.values) {
            varManager.sendBit(SLAIN_WIGHT_VARBIT + wight.ordinal(), slainWights.contains(wight));
        }
        varManager.sendBit(POTENTIAL_VARBIT, (int) ((float) Math.min(MAXIMUM_POTENTIAL, getFullPotential()) / (float) MAXIMUM_POTENTIAL * 1000.0F));
    }

    void refreshDoors() {
        final VarManager varManager = player.getVarManager();
        for (final CryptDoorway doorway : CryptDoorway.values) {
            varManager.sendBit(doorway.varbitId, shutDoorways.contains(doorway));
        }
    }

    void refreshShaking() {
        if (!isLooted() || !player.inArea("Barrows chambers")) return;
        player.getPacketDispatcher().sendCameraShake(CameraShakeType.LEFT_AND_RIGHT, 5, 0, 0);
    }

    void refreshLadder(final Location position) {
        final VarManager varManager = player.getVarManager();
        final boolean showing = varManager.getBitValue(LADDER_VARBIT) == 1;
        if (position.withinDistance(corner.ladder, LADDER_DISTANCE) != showing) {
            varManager.sendBit(LADDER_VARBIT, !showing);
        }
    }

    public static void main(String[] args) {
        Cache cache = CacheManager.loadCache(Cache.openCache("cache/data/cache"));

        new ItemDefinitions().load(cache);

        simulate(656, 1000);
        simulate(656, 10000);

        simulate(880, 1000);
        simulate(880, 10000);

        simulate(1000, 1000);
        simulate(1000, 10000);
    }

    private static void simulate(int potential, int amount) {
        System.out.println("Simulating " + amount + " with " + potential + " potential");
        Container container = new Container(ContainerType.BANK, ContainerPolicy.ALWAYS_STACK, 1000, Optional.empty());
        for (int i = 0; i < amount; i++) {
            Barrows barrows = new Barrows(null);
            barrows.setMaximumReward(potential);
            barrows.calculateLoot();
            for (Item item : barrows.container.getItems().values()) {
                container.add(item);
            }
        }

        int pieces = 0;
        for (Item item : container.getItems().values()) {
            System.out.println(item);
            if (BarrowsWight.ALL_WIGHT_EQUIPMENT.contains(new Item(item.getId()))) {
                pieces += item.getAmount();
            }
        }

        System.out.println("Total barrows pieces: " + pieces);

        System.out.println("-------------------");
    }

    void calculateLoot() {
        int slainAmount = slainWights.size();
        int totalRolls = Math.max(1, slainAmount);

        if(World.hasBoost(XamphurBoost.BONUS_BARROWS_DR_25))
            totalRolls *= 2;

        if(player.getBoonManager().hasBoon(CryptKeeper.class))
            totalRolls += 2;

        int barrowsRolls = 0;
        final ArrayList<Item> availableBarrowsLoot = new ArrayList<Item>(slainAmount * 4);
        for (final BarrowsWight wight : slainWights) {
            availableBarrowsLoot.addAll(Arrays.asList(wight.getArmour()));
        }
        while (barrowsRolls++ < totalRolls) {
            final int n = (int) ((450 - (58 * slainAmount)) * 0.26);
            if (Utils.random(n) == 0) {
                final Item item = availableBarrowsLoot.remove(Utils.random(availableBarrowsLoot.size() - 1));
                item.setCharges(DegradableItem.getDefaultCharges(item.getId(), 0));
                container.add(item);
            }
        }
        final int potential = getFullPotential();
        int remainingRolls = totalRolls - container.getSize();
        final boolean isMorytaniaCompleted = DiaryUtil.eligibleFor(DiaryReward.MORYTANIA_LEGS3, player);
        boolean clue = false;
        while (remainingRolls-- > 0) {
            if (!clue) {
                if (Utils.random(199) == 0) {
                    clue = true;
                    container.add(new Item(ClueItem.ELITE.getScrollBox()));
                }
            }
            final int roll = Utils.random(potential);
            final int static_roll = Utils.random(400);
            if(static_roll == 0) {
                container.add(new Item(ItemId.BOOK_OF_THE_DEAD));
                continue;
            }
            if (roll >= 1006) {
                if (roll == 1012) {
                    container.add(new Item(BarrowsReward.DRAGON_MED_HELM.item.getId(), 1));
                } else {
                    container.add(new Item((Utils.random(1) == 0 ? BarrowsReward.LOOP_KEY_HALF : BarrowsReward.TOOTH_KEY_HALF).item.getId(), 1));
                }
                continue;
            }
            for (final BarrowsReward reward : BarrowsReward.values) {
                if (reward.maximumPotential >= roll) {
                    float modifier = 1.0F;
                    if (potential < reward.maximumPotential) {
                        final int base = reward.maximumPotential - reward.requiredPotential;
                        final int usersBase = potential - reward.requiredPotential;
                        modifier = (float) usersBase / (float) base;
                    }
                    float amount = reward.item.getAmount() * modifier;
                    if (reward.diaryMod && isMorytaniaCompleted) {
                        amount *= 1.5f;
                    }
                    container.add(new Item(reward.item.getId(), Utils.random(1, (int) amount)));
                    break;
                }
            }
        }
        if (slainAmount == 6) {
            if (Utils.random(99) == 0) {
                container.add(new Item(12851, 1));
            }
            if (Utils.randomBoolean(4)) {
                container.add(new Item(32168, Utils.random(2, 20)));
            }
        }
    }

    /**
     * Adds the loot to the player's inventory, or drops it under them. Refreshes the containers.
     */
    void addLoot() {
        if (container.isEmpty()) return;
        final Container inventory = player.getInventory().getContainer();
        container.getItems().int2ObjectEntrySet().fastForEach(entry -> {
            player.getCollectionLog().add(entry.getValue());
            final boolean addToRunePouch;
            if (player.getInventory().containsAnyOf(RunePouch.POUCHES)) {
                final int amountInRunePouch = player.getRunePouch().getAmountOf(entry.getValue().getId());
                addToRunePouch = amountInRunePouch > 0 && (amountInRunePouch + entry.getValue().getAmount()) < 16000;
            } else {
                addToRunePouch = false;
            }
            final boolean addToQuiver = (player.getEquipment().getId(EquipmentSlot.AMMUNITION) == entry.getValue().getId() || (entry.getValue().isStackable() && player.getEquipment().getId(EquipmentSlot.WEAPON) == entry.getValue().getId()));
            final Container container = addToQuiver ? player.getEquipment().getContainer() : addToRunePouch ? player.getRunePouch().getContainer() : inventory;            container.add(entry.getValue()).onFailure(remainder -> World.spawnFloorItem(remainder, player));
        });
        player.getRunePouch().getContainer().refresh(player);
        player.getEquipment().getContainer().refresh(player);
        inventory.refresh(player);
        container.refresh(player);
        container.clear();
        player.getInterfaceHandler().closeInterface(GameInterface.BARROWS_OVERLAY);
    }

    void shiftDoorways() {
        shutDoorways.clear();
        if (true || player.getPerkManager().isValid(PerkWrapper.RIDDLE_IN_THE_TUNNELS)) {
            return;
        }
        final ArrayList<CryptDoorway> cornerDoorways = new ArrayList<CryptDoorway>(SHUT_DOORWAYS >> 1);
        final ArrayList<CryptDoorway> centerDoorways = new ArrayList<CryptDoorway>(SHUT_DOORWAYS >> 1);
        cornerDoorways.addAll(Arrays.asList(corner.room.doorways));
        cornerDoorways.remove(Utils.random(cornerDoorways.size() - 1));
        centerDoorways.addAll(Arrays.asList(CryptDoorway.centerDoorways));
        this.openDoorway = centerDoorways.remove(Utils.random(centerDoorways.size() - 1));
        shutDoorways.addAll(cornerDoorways);
        shutDoorways.addAll(centerDoorways);
    }

    void onDeath(final BarrowsNPC npc) {
        if (npc instanceof BarrowsWightNPC) {
            if (isLooted()) return;
            BarrowsWight wight = ((BarrowsWightNPC) npc).getWight();
            if (wight == null) return;
            slainWights.add(wight);
        } else {
            if (isLooted()) return;
            potential = Math.min(MAXIMUM_POTENTIAL, potential + npc.getCombatLevel());
        }
        refreshInterface();
    }

    void onFinish(final BarrowsNPC npc) {
        if (npc instanceof BarrowsWightNPC) {
            player.getPacketDispatcher().resetHintArrow();
        }
    }

    void removeTarget() {
        if (currentWight == null) return;
        player.getPacketDispatcher().resetHintArrow();
        if (!currentWight.isDead()) {
            currentWight.finish();
        }
        currentWight = null;
    }

    void sendRandomTarget(final Location position) {
        final int random = Utils.random(CRYPT_NPC_WEIGHT + BarrowsWight.values.length - slainWights.size());
        if (currentWight == null && random > CRYPT_NPC_WEIGHT) {
            getRandomAliveWight().ifPresent(wight -> sendWight(wight, position, null));
        } else {
            final List<BarrowsNPC> wightsList = BarrowsNPC.getWightsList(player);
            if (wightsList.size() >= 9) {
                return;
            }
            sendCryptNPC(position);
        }
    }

    void sendWight(final BarrowsWight wight, final Location location, final String message) {
        if (!player.inArea("Barrows chambers")) throw new RuntimeException("Unable to invocate target outside of barrows chambers.");
        final NPCDefinitions definitions = NPCDefinitions.getOrThrow(wight.getNpcId());
        final int size = definitions.getSize();
        final NPC npc = World.invoke(wight.getNpcId(), getSpawnTile(location, size), Direction.SOUTH, 5);
        this.currentWight = (BarrowsWightNPC) npc;
        currentWight.owner = new WeakReference<>(player);
        player.getCombatAchievements().addCurrentTaskFlag(CAType.DEFENCE_WHAT_DEFENCE, CA_TASK_MAGIC_DAMAGE_ONLY);
        if (slainWights.size() == 0) {
            player.getCombatAchievements().addCurrentTaskFlag(CAType.PRAY_FOR_SUCCESS, CA_TASK_NO_DAMAGE);
            player.getCombatAchievements().addCurrentTaskFlag(CAType.CANT_TOUCH_ME, CA_TASK_NO_MELEE_DAMAGE_TORAG);
            player.getCombatAchievements().addCurrentTaskFlag(CAType.CANT_TOUCH_ME, CA_TASK_NO_MELEE_DAMAGE_DHAROCK);
            player.getCombatAchievements().addCurrentTaskFlag(CAType.CANT_TOUCH_ME, CA_TASK_NO_MELEE_DAMAGE_VERAC);
            player.getCombatAchievements().addCurrentTaskFlag(CAType.CANT_TOUCH_ME, CA_TASK_NO_MELEE_DAMAGE_GUTHAN);
            if (player.getPrayerManager().getPrayerPoints() == 0) {
                player.getCombatAchievements().addCurrentTaskFlag(CAType.FAITHLESS_CRYPT_RUN, CA_TASK_FAITHLESS_RUN);
            }
        }
        npc.spawn();
        npc.setSpawned(true);
        npc.setForceAttackable(true);
        player.getPacketDispatcher().sendHintArrow(new HintArrow(npc));
        if (message != null) {
            npc.setForceTalk(new ForceTalk(message));
        }
        WorldTasksManager.schedule(() -> npc.getCombat().forceTarget(player));
    }

    private void sendCryptNPC(final Location location) {
        if (!player.inArea("Barrows chambers")) throw new RuntimeException("Unable to invocate target outside of barrows chambers.");
        final int id = cryptMonsters.getInt(Utils.random(cryptMonsters.size() - 1));
        final NPCDefinitions definitions = NPCDefinitions.getOrThrow(id);
        final int size = definitions.getSize();
        final NPC npc = World.invoke(id, getSpawnTile(location, size), Direction.SOUTH, 5);
        ((BarrowsNPC) npc).owner = new WeakReference<>(player);
        npc.setSpawned(true);
        npc.spawn();
        npc.freeze(1);
        npc.getCombat().forceTarget(player);
        npc.getCombat().setCombatDelay(2);
    }

    private Location getSpawnTile(final Location tile, final int size) {
        int count = SPAWN_ATTEMPT_COUNT;
        Location spawnTile;
        while (ProjectileUtils.isProjectileClipped(null, null, tile, (spawnTile = RandomLocation.create(tile, DEFAULT_SPAWN_DISTANCE + size)), true) || !World.isFloorFree(spawnTile, size) || tile.matches(spawnTile)) {
            if (--count == 0) {
                return tile;
            }
        }
        return spawnTile;
    }

    int getAndDecrementTimer() {
        final int timer = Math.max(0, --this.timer);
        if (timer == 0) {
            resetTimer();
        }
        return timer;
    }

    public Player getPlayer() {
        return player;
    }

    public Container getContainer() {
        return container;
    }

    public int getTimer() {
        return timer;
    }

    public BarrowsPuzzle getPuzzle() {
        return puzzle;
    }

    public boolean isSkipTunnels() {
        return skipTunnels;
    }

    public BarrowsWight getHiddenWight() {
        return hiddenWight;
    }

    public Set<BarrowsWight> getSlainWights() {
        return slainWights;
    }

    public Set<CryptDoorway> getShutDoorways() {
        return shutDoorways;
    }

    public CryptDoorway getOpenDoorway() {
        return openDoorway;
    }

    public BarrowsCorner getCorner() {
        return corner;
    }

    public int getPotential() {
        return potential;
    }

    public boolean isLooted() {
        return looted;
    }

    public void setLooted(boolean looted) {
        this.looted = looted;
    }

    public boolean isPuzzleSolved() {
        return puzzleSolved;
    }

    public void setPuzzleSolved(boolean puzzleSolved) {
        this.puzzleSolved = puzzleSolved;
    }

    public BarrowsWightNPC getCurrentWight() {
        return currentWight;
    }
}
