package com.zenyte.game.content.minigame.inferno.instance;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.content.minigame.inferno.model.*;
import com.zenyte.game.content.minigame.inferno.npc.InfernoNPC;
import com.zenyte.game.content.minigame.inferno.npc.impl.JalZek;
import com.zenyte.game.content.minigame.inferno.npc.impl.zuk.TzKalZuk;
import com.zenyte.game.content.minigame.inferno.plugins.TzKalZukCutscene;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.skills.slayer.Assignment;
import com.zenyte.game.content.skills.slayer.RegularTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.music.Music;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCDLoader;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.*;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Tommeh | 26/11/2019 | 16:25
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class Inferno extends DynamicArea implements CycleProcessPlugin, LogoutPlugin, LogoutRestrictionPlugin, DeathPlugin, CannonRestrictionPlugin, TeleportPlugin, DropPlugin {
    private static final Logger log = LoggerFactory.getLogger(Inferno.class);
    public static final Location outsideLocation = new Location(2495, 5111, 0);
    private static final Location middleLocation = new Location(2271, 5343, 0);
    private static final Animation playerFallAnim = new Animation(4367);
    public static final Animation fallingRocksAnim = new Animation(7559);
    public static final Item infernalCape = new Item(ItemId.INFERNAL_CAPE);
    private final transient Player player;
    private InfernoState state;
    private InfernoWave wave;
    private final Rectangle room;
    private final WorldObject[] fallingRocks;
    private final WorldObject[] wallPatches;
    private final Location nibblerSpawnLocation;
    private final Location[] tripleJadSpawnLocations;
    private final Location[] spawnLocations;
    private final Location[] revivalLocations;
    private final Location[] dragBlockTiles;
    private List<RockySupport> rockySupports;
    private final List<InfernoNPC> npcs = new ArrayList<>();
    private Entity nibblerTarget;
    private final Location middle;
    private final Location singleJadSpawnLocation;
    private int targetIndex;
    private int cycle;
    private long duration;
    private boolean resuming;
    private boolean onAssignment;
    private final boolean practiceMode;
    private final int minShieldY = getY(5356);
    private final int maxShieldY = getY(5358);
    private InfernoWave startWave = InfernoWave.WAVE_67;

    public Inferno(final Player player, final AllocatedArea area, final boolean practiceMode) {
        super(area, 280, 664);
        this.player = player;
        this.practiceMode = practiceMode;
        middle = getLocation(middleLocation);
        singleJadSpawnLocation = getLocation(2265, 5348, 0);
        spawnLocations = new Location[] {middle.transform(3, 2, 0), middle.transform(-3, -2, 0), middle.transform(-3, -4, 0), middle.transform(5, -4, 0), middle.transform(7, 4, 0), middle.transform(-7, 6, 0)};
        revivalLocations = new Location[] {middle, getLocation(2274, 5339, 0), getLocation(2268, 5339, 0), getLocation(2274, 5347, 0), getLocation(2268, 5347, 0), getLocation(2273, 5345, 0)};
        nibblerSpawnLocation = getLocation(2266, 5345, 0);
        tripleJadSpawnLocations = new Location[] {getLocation(2274, 5346, 0), getLocation(2265, 5347, 0), getLocation(2268, 5335, 0)};
        dragBlockTiles = new Location[] {getLocation(2264, 5359, 0), getLocation(2278, 5359, 0)};
        wallPatches = new WorldObject[] {new WorldObject(30339, 10, 3, getLocation(2275, 5364, 0)), new WorldObject(30340, 10, 1, getLocation(2267, 5364, 0)), new WorldObject(30341, 10, 3, getLocation(2275, 5366, 0)), new WorldObject(30342, 10, 1, getLocation(2267, 5366, 0))};
        fallingRocks = new WorldObject[] {new WorldObject(30343, 10, 3, getLocation(2273, 5364, 0)), new WorldObject(30344, 10, 3, getLocation(2268, 5364, 0))};
        room = World.getRectangle(getX(2257), getX(2285), getY(5329), getY(5358));
        final Assignment task = player.getSlayer().getAssignment();
        onAssignment = task != null && task.getTask().equals(RegularTask.TZKAL_ZUK);
        state = InfernoState.START_CUTSCENE;
    }

    @Override
    public void constructed() {
        player.getAppearance().setInvisible(false);
        player.setViewDistance(Player.LARGE_VIEWPORT_RADIUS);
        if (resuming) {
            player.unlock();
            state = InfernoState.NEXT_WAVE;
            cycle = 12;
        } else {
            player.lock();
            player.setAnimation(playerFallAnim);
            player.getDialogueManager().start(new PlainChat(player, "You hit the ground in the centre of The Inferno.", false));
        }
        final int lastPosHash = player.getNumericAttribute("inferno_player_location").intValue();
        if (lastPosHash > 0) {
            player.setLocation(getLocation(new Location(lastPosHash)));
        } else {
            player.setLocation(getLocation(middleLocation));
        }
        player.getMusic().unlock(Music.get("Inferno"), true);
        rockySupports = new ArrayList<>();
        if (!resuming) {
            duration = Utils.currentTimeMillis();
            if (!practiceMode) {
                startWave = InfernoWave.WAVE_67;
                wave = startWave;
            }
        } else {
            final int wave = player.getNumericAttribute("inferno_progress").intValue();
            if (wave > 0) {
                final long duration = player.getNumericAttribute("inferno_duration").longValue();
                this.wave = startWave = InfernoWave.get(wave);
                this.duration = duration;
                if (this.wave.getWave() >= InfernoWave.WAVE_67.getWave()) {
                    removeSupports();
                }
            }
            for (int i = 0; i < spawnLocations.length; i++) {
                final int bitpacked = player.getNumericAttribute("inferno_npc_spawn_location_" + i).intValue();
                if (bitpacked == 0) {
                    continue;
                }
                spawnLocations[i] = getLocation(new Location(bitpacked));
            }
        }
        if (wave.getWave() < InfernoWave.WAVE_67.getWave()) {
            for (final RockySupportLocation location : RockySupportLocation.values) {
                final int hitpoints = player.getNumericAttribute("inferno_rocky_support_" + location).intValue();
                if (hitpoints >= 0) {
                    final RockySupport support = new RockySupport(location, this);
                    rockySupports.add((RockySupport) support.spawn());
                    support.setHitpoints(hitpoints == 0 ? support.getMaxHitpoints() : hitpoints);
                }
            }
        }
        for (final Location tile : dragBlockTiles) {
            World.spawnObject(new WorldObject(0, 10, 0, tile));
        }
        if (!resuming) {
            WorldTasksManager.schedule(() -> {
                player.unlock();
                player.getDialogueManager().finish();
                state = InfernoState.NEXT_WAVE;
                cycle = 11;
            }, 8);
        }
    }

    @Subscribe
    public static final void onLogin(final LoginEvent event) {
        final Player player = event.getPlayer();
        final Object attribute = player.getAttributes().get("inferno_progress");
        if (attribute == null) {
            return;
        }
        player.lock();
        try {
            final boolean practiceMode = player.getBooleanAttribute("inferno_practice_mode");
            final AllocatedArea area = MapBuilder.findEmptyChunk(8, 8);
            final Inferno inferno = new Inferno(player, area, practiceMode);
            inferno.setResuming(true);
            inferno.constructRegion();
        } catch (OutOfSpaceException e) {
            log.error("", e);
        }
    }

    @Override
    public void enter(Player player) {
    }

    private final int calculateHealthOfAllSlaughteredMonsters() {
        int totalHealth = 0;
        try {
            for (final InfernoWave wave : InfernoWave.getValues()) {
                if (startWave.ordinal() > wave.ordinal()) {
                    continue;
                }
                if (wave.ordinal() >= this.wave.ordinal()) {
                    break;
                }
                for (final WaveEntry entry : wave.getEntries()) {
                    if (entry.getNpc() == WaveNPC.JAL_NIB || entry.getNpc() == WaveNPC.TZKAL_ZUK) {
                        continue;
                    }
                    try {
                        if (entry.getNpc() == WaveNPC.JAL_AK) {
                            totalHealth += NPCCDLoader.get(WaveNPC.JAL_AKREK_MEJ.getBaseNPC()).getMaxHit() * 3;
                        }
                        totalHealth += NPCCDLoader.get(entry.getNpc().getBaseNPC()).getHitpoints() * entry.getCount();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failure to calculate the health of all slaughtered monsters combined.", e);
        }
        return totalHealth;
    }

    public void leave(final boolean completed) {
        player.lock(2);
        final int amount = completed ? 16440 : (calculateHealthOfAllSlaughteredMonsters() / 2);
        final Item tokkul = new Item(ItemId.TOKKUL, amount);
        if (DiaryUtil.eligibleFor(DiaryReward.KARAMJA_GLOVES4, player)) {
            tokkul.setAmount(tokkul.getAmount() * 2);
        }
        if (!practiceMode) {
            final Assignment task = player.getSlayer().getAssignment();
            if (task != null && task.getTask() == RegularTask.TZKAL_ZUK) {
                player.getSlayer().removeTask();
            }
        }
        player.setViewDistance(Player.SMALL_VIEWPORT_RADIUS);
        player.blockIncomingHits(5);
        if (completed) {
            if (!practiceMode) {
                final long time = System.currentTimeMillis() - duration;
                player.getBossTimer().inform("Inferno", time);
                if (TimeUnit.MILLISECONDS.toMinutes(time) <= 65) {
                    player.getCombatAchievements().complete(CAType.INFERNO_SPEED_RUNNER);
                }
                InfernoCompletions.add(player);
            }
            for (final InfernoNPC npc : npcs) {
                if (npc instanceof TzKalZuk) {
                    continue;
                }
                npc.sendDeath();
            }
            WorldTasksManager.schedule(() -> {
                if (!practiceMode) {
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            npc(7690, "You are very impressive for a JalYt. You managed to defeat TzKal-Zuk! Please accept this cape as a token of appreciation.", 1);
                            doubleItem(infernalCape, tokkul, "Tzhaar-Ket-Keh hands you an Infernal cape and<br>" + tokkul.getAmount() + " TokKul.");
                        }
                    });
                    player.getCollectionLog().add(infernalCape);
                    player.getInventory().addItem(infernalCape).onFailure(c -> World.spawnFloorItem(c, player, outsideLocation));
                    player.getInventory().addItem(tokkul).onFailure(t -> World.spawnFloorItem(t, player, outsideLocation));
                    WorldBroadcasts.broadcast(player, BroadcastType.INFERNO_COMPLETION);
                    BossPet.JAL_NIB_REK.roll(player, onAssignment ? 75 : 100);
                }
                player.setLocation(Inferno.outsideLocation);
            }, 8);
        } else {
            player.setLocation(Inferno.outsideLocation);
            if (!practiceMode) {
                if (wave.getWave() > 1) {
                    player.getDialogueManager().start(new Dialogue(player, 7690) {
                        @Override
                        public void buildDialogue() {
                            npc("Well done in the Inferno, you can have this TokKul as reward.");
                            item(tokkul, "Tzhaar-Ket-Keh hands you " + tokkul.getAmount() + " TokKul.");
                        }
                    });
                    player.getInventory().addItem(tokkul).onFailure(cape -> World.spawnFloorItem(tokkul, player, outsideLocation));
                } else {
                    player.getDialogueManager().start(new NPCChat(player, 7690, "Not a very good attempt JalYt. Better luck next time."));
                }
            }
            player.reset();
        }
    }

    private void removeSupports() {
        for (final RockySupport support : rockySupports) {
            support.setHitpoints(0);
        }
    }

    @Override
    public String restrictionMessage() {
        return "It's too hot in the inferno, the heat could damage the cannon.";
    }

    private void appendInfernoSettings() {
        player.putBooleanAttribute("inferno_practice_mode", practiceMode);
        player.addAttribute("inferno_progress", wave.getWave());
        player.addAttribute("inferno_duration", Utils.currentTimeMillis() - duration);
        player.addAttribute("inferno_player_location", getStaticLocation(player.getLocation()).getPositionHash());
        for (int i = spawnLocations.length - 1; i >= 0; i--) {
            player.addAttribute("inferno_npc_spawn_location_" + i, getStaticLocation(spawnLocations[i]).getPositionHash());
        }
        for (final RockySupport support : rockySupports) {
            player.addAttribute("inferno_rocky_support_" + support.getType(), support.isDead() ? -1 : support.getHitpoints());
        }
    }

    @Override
    public void leave(final Player player, final boolean logout) {
        if (logout) {
            appendInfernoSettings();
        } else {
            player.getAttributes().remove("inferno_practice_mode");
            player.getAttributes().remove("inferno_progress");
            player.getAttributes().remove("inferno_duration");
            player.getAttributes().remove("inferno_player_location");
            for (int i = spawnLocations.length - 1; i >= 0; i--) {
                player.getAttributes().remove("inferno_npc_spawn_location_" + i);
            }
            for (final RockySupportLocation location : RockySupportLocation.values) {
                player.getAttributes().remove("inferno_rocky_support_" + location);
            }
        }
        player.setViewDistance(Player.SMALL_VIEWPORT_RADIUS);
        player.getInterfaceHandler().closeInterface(InterfacePosition.MINIGAME_OVERLAY);
        player.getVarManager().sendBit(5652, 0);
    }

    @Override
    public boolean manualLogout(final Player player) {
        if (state.equals(InfernoState.PENDING_LOGOUT) || state.equals(InfernoState.LOGGING_OUT) || npcs.isEmpty()) {
            player.logout(false);
            return false;
        }
        player.sendMessage(Colour.RED + "Your logout request has been received. The minigame will be paused at the end of this wave.");
        player.sendMessage(Colour.RED + "If you try to log out before that, you will have to repeat this wave.");
        state = InfernoState.LOGGING_OUT;
        return false;
    }

    @Override
    public void onLogout(final @NotNull Player player) {
        player.setLocation(getLocation(outsideLocation));
    }

    @Override
    public Location onLoginLocation() {
        return outsideLocation;
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
        return null;
    }

    @Override
    public boolean sendDeath(final Player player, final Entity source) {
        player.setAnimation(Animation.STOP);
        player.lock();
        player.stopAll();
        if (player.getPrayerManager().isActive(Prayer.RETRIBUTION)) {
            player.getPrayerManager().applyRetributionEffect(source);
        }
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                if (player.isFinished() || player.isNulled()) {
                    stop();
                    return;
                }
                if (ticks == 1) {
                    player.setAnimation(Player.DEATH_ANIMATION);
                } else if (ticks == 4) {
                    player.getMusic().playJingle(90);
                    player.sendMessage("You have been defeated!");
                    player.reset();
                    player.setAnimation(Animation.STOP);
                    player.getVariables().setSkull(false);
                    leave(false);
                } else if (ticks == 5) {
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

    @Override
    public Location getRespawnLocation() {
        return outsideLocation;
    }

    private void executeWave(final InfernoWave wave) {
        player.sendMessage(Colour.RED.wrap("Wave: " + wave.getWave()));
        state = InfernoState.ONGOING_WAVE;
        if (wave.equals(InfernoWave.WAVE_69)) {
            player.getDialogueManager().start(new PlainChat(player, "A great power is starting to shake the cavern...", false));
            new FadeScreen(player).fade(5, false);
            WorldTasksManager.schedule(() -> player.getCutsceneManager().play(new TzKalZukCutscene(this)), 3);
            appendInfernoSettings();
            return;
        }
        //Do not shuffle locations if the player logs back into inferno, rather re-use old spawn locations.
        if (resuming) {
            resuming = false;
        } else {
            Collections.shuffle(Arrays.asList(spawnLocations));
        }
        setNibblerTarget();
        final WaveEntry[] entries = wave.getEntries();
        final Int2IntOpenHashMap tiles = new Int2IntOpenHashMap(6);
        for (int index = 0; index < entries.length; index++) {
            final WaveEntry entry = entries[index];
            final int amount = entry.getCount();
            for (int count = 0; count < amount; count++) {
                final WaveNPC n = entry.getNpc();
                try {
                    Location location;
                    if (n.equals(WaveNPC.JAL_NIB)) {
                        location = new Location(nibblerSpawnLocation);
                        final int x = nibblerSpawnLocation.getX() - 1;
                        final int y = nibblerSpawnLocation.getY() - 1;
                        for (int i = 0; i < 100; i++) {
                            location.setLocation(x + Utils.random(2), y + Utils.random(2), location.getPlane());
                            if (tiles.get(location.getPositionHash()) >= 2) {
                                continue;
                            }
                            tiles.put(location.getPositionHash(), tiles.getOrDefault(location.getPositionHash(), 0) + 1);
                            break;
                        }
                    } else if (wave.equals(InfernoWave.WAVE_67)) {
                        location = singleJadSpawnLocation;
                    } else if (wave.equals(InfernoWave.WAVE_68)) {
                        location = tripleJadSpawnLocations[count];
                    } else {
                        location = spawnLocations[amount > 1 ? count : index];
                    }
                    final InfernoNPC npc = entry.getNpc().getClazz().getDeclaredConstructor(Location.class, Inferno.class).newInstance(location, this);
                    npc.spawn();
                    if (wave.equals(InfernoWave.WAVE_68)) {
                        npc.getCombatDefinitions().setAttackSpeed(12);
                        npc.faceEntity(player);
                        npc.getCombat().setCombatDelay((4 * count) + 4);
                    } else if (wave.equals(InfernoWave.WAVE_67)) {
                        npc.getCombat().setCombatDelay(4);
                    }
                    if (!n.equals(WaveNPC.JAL_NIB)) {
                        npc.getCombat().setTarget(player);
                    }
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    log.error("", e);
                }
            }
        }
        appendInfernoSettings();
    }

    public void setNibblerTarget() {
        final ArrayList<RockySupport> supports = new ArrayList<RockySupport>(3);
        rockySupports.forEach(support -> {
            if (!support.isDead()) {
                supports.add(support);
            }
        });
        if (!supports.isEmpty()) {
            if (targetIndex >= supports.size()) {
                targetIndex = 0;
            }
            nibblerTarget = supports.get(targetIndex++);
        } else {
            nibblerTarget = player;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getNPCs(final Class<T> npc) {
        final ArrayList<T> list = new ArrayList<T>();
        for (final InfernoNPC n : npcs) {
            if (npc.isInstance(n)) {
                list.add((T) n);
            }
        }
        return list;
    }

    public void add(final InfernoNPC npc) {
        npcs.add(npc);
    }

    public void add(final InfernoNPC... npcs) {
        this.npcs.addAll(Arrays.asList(npcs));
    }

    public void skip(final InfernoWave wave) {
        this.wave = wave;
        for (final InfernoNPC npc : npcs) {
            npc.finish();
        }
        if (wave.getWave() >= InfernoWave.WAVE_67.getWave()) {
            removeSupports();
        }
        npcs.clear();
        executeWave(wave);
        cycle = 3;
    }

    public void check(final InfernoNPC npc, final Entity source) {
        if (!inside(player.getLocation())) {
            return;
        }
        if (npc instanceof TzKalZuk) {
            leave(true);
            return;
        }
        if (npcs.remove(npc) && npcs.isEmpty()) {
            if (state.equals(InfernoState.LOGGING_OUT)) {
                return;
            }
            player.sendMessage("Wave completed!");
            player.getMusic().playJingle(76);
            state = InfernoState.NEXT_WAVE;
            cycle = wave.equals(InfernoWave.WAVE_68) ? 2 : 8;
            if (wave.equals(InfernoWave.WAVE_66)) {
                removeSupports();
            }
            incrementWave();
        } else {
            if (source instanceof RockySupport) {
                return;
            }
            final java.util.List<JalZek> zeks = getNPCs(JalZek.class);
            if (zeks.isEmpty() || wave.equals(InfernoWave.WAVE_69)) {
                return;
            }
            for (final JalZek zek : zeks) {
                zek.queueRevival(npc);
            }
        }
    }

    private void incrementWave() {
        wave = wave == null ? InfernoWave.WAVE_1 : wave.increment();
        player.addAttribute("inferno_progress", wave.getWave());
    }

    public void playSound(final SoundEffect sound) {
        player.getPacketDispatcher().sendSoundEffect(sound);
    }

    @Override
    public void process() {
        if (!inside(player.getLocation())) {
            return;
        }
        if (state.equals(InfernoState.ONGOING_WAVE) || state.equals(InfernoState.PENDING_LOGOUT)) {
            return;
        }
        if (state.equals(InfernoState.LOGGING_OUT)) {
            if (!npcs.isEmpty()) {
                return;
            }
            incrementWave();
            player.sendMessage(Colour.RED + "The Inferno has been paused. You may now log out.");
            state = InfernoState.PENDING_LOGOUT;
            return;
        }
        if (--cycle <= 0 && state.equals(InfernoState.NEXT_WAVE)) {
            executeWave(wave);
        }
    }

    @Override
    public void cleared() {
        if (players.isEmpty()) {
            destroyRegion();
        }
    }

    @Override
    public String name() {
        return player.getName() + "'s Inferno Instance";
    }

    @Override
    public boolean canTeleport(final Player player, final Teleport teleport) {
        player.sendMessage("Use the portal if you want to leave.");
        return false;
    }

    @Override
    public int visibleTicks(final Player player, final Item item) {
        return (int) TimeUnit.HOURS.toTicks(5);
    }

    public Player getPlayer() {
        return player;
    }

    public InfernoWave getWave() {
        return wave;
    }

    public void setWave(InfernoWave wave) {
        this.wave = wave;
    }

    public Rectangle getRoom() {
        return room;
    }

    public WorldObject[] getFallingRocks() {
        return fallingRocks;
    }

    public WorldObject[] getWallPatches() {
        return wallPatches;
    }

    public Location[] getTripleJadSpawnLocations() {
        return tripleJadSpawnLocations;
    }

    public Location[] getSpawnLocations() {
        return spawnLocations;
    }

    public Location[] getRevivalLocations() {
        return revivalLocations;
    }

    public Location[] getDragBlockTiles() {
        return dragBlockTiles;
    }

    public List<RockySupport> getRockySupports() {
        return rockySupports;
    }

    public List<InfernoNPC> getNpcs() {
        return npcs;
    }

    public Entity getNibblerTarget() {
        return nibblerTarget;
    }

    public Location getMiddle() {
        return middle;
    }

    public Location getSingleJadSpawnLocation() {
        return singleJadSpawnLocation;
    }

    public void setResuming(boolean resuming) {
        this.resuming = resuming;
    }

    public void setOnAssignment(boolean onAssignment) {
        this.onAssignment = onAssignment;
    }

    public boolean isPracticeMode() {
        return practiceMode;
    }

    public int getMinShieldY() {
        return minShieldY;
    }

    public int getMaxShieldY() {
        return maxShieldY;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

}
