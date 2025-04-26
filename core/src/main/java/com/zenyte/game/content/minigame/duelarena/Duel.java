package com.zenyte.game.content.minigame.duelarena;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.near_reality.game.model.item.ItemValueExtKt;
import com.near_reality.tools.logging.GameLogMessage;
import com.near_reality.tools.logging.GameLogger;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.content.follower.Pet;
import com.zenyte.game.content.follower.PetWrapper;
import com.zenyte.game.content.minigame.duelarena.area.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.HintArrow;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.PolygonRegionArea;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.utils.efficientarea.Polygon;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import kotlinx.datetime.Instant;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.function.Predicate;

import static com.zenyte.game.GameInterface.DUEL_CONFIRMATION;
import static com.zenyte.game.content.minigame.duelarena.DuelSetting.*;

/**
 * @author Tommeh | 28-11-2018 | 20:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class Duel {

    public static final int DUEL_HISTORY_CAPACITY = 50;

    private static final Deque<String> duelHistory = new ArrayDeque<>(DUEL_HISTORY_CAPACITY);

    public static Collection<String> getDuelHistory() {
        return duelHistory;
    }

    private static final Logger log = LoggerFactory.getLogger(Duel.class);
    public static final ArenaArea NORMAL_ARENA = new NormalArenaArea();
    public static final ArenaArea NO_MOVEMENT_ARENA = new NoMovementArena();
    public static final ArenaArea OBSTACLES_ARENA = new ObstaclesArenaArea();
    public static final PolygonRegionArea LOBBY = new DuelArenaLobbyArea();
    public static final int SCOREBOARD_INTERFACE = 108;
    public static final int WINNINGS_INTERFACE = 1610;
    public static final int CONFIRMATION_INTERFACE = 476;
    public static final int STAKING_INTERFACE = GameInterface.DUEL_STAKING.getId();
    public static final int SETTINGS_INTERFACE = GameInterface.DUEL_SETTINGS.getId();
    public static final int SETTINGS_INTERFACE_INFO = 91;
    public static final int SETTINGS_INTERFACE_PRESET = 99;
    public static final int SETTINGS_INTERFACE_LAST_DUEL = 101;
    public static final int BOXING_SETTINGS = 229630938;
    public static final int WHIP_SETTINGS = 229499866;
    public static final ImmutableList<Integer> FUN_WEAPONS = ImmutableList.of(8650, 8652, 8654, 8656, 8658, 8660, 8662, 8664, 8666, 8668, 8670, 8672, 8274, 8676, 8678, 8680, 6082, 2460, 2462, 2464, 2466, 2468, 2470, 2472, 2474, 2476, 751, 6541, 10150, 3695, 6773, 6774, 6775, 6776, 6777, 6778, 6779, 4566, 1419, 10501, 4086, 10487, ChristmasConstants.CHRISTMAS_SCYTHE);
    private static final ImmutableMap<Integer, InterfacePosition> CLOSED_TABS = ImmutableMap.<Integer, InterfacePosition>builder().put(593, InterfacePosition.COMBAT_TAB).put(320, InterfacePosition.SKILLS_TAB).put(399, InterfacePosition.JOURNAL_TAB_HEADER).put(149, InterfacePosition.INVENTORY_TAB).put(387, InterfacePosition.EQUIPMENT_TAB).put(541, InterfacePosition.PRAYER_TAB).put(218, InterfacePosition.SPELLBOOK_TAB).put(261, InterfacePosition.SETTINGS_TAB).put(216, InterfacePosition.EMOTE_TAB).build();
    private static final ForceTalk FIGHT = new ForceTalk("FIGHT!");
    private Player player;
    private Player opponent;
    private Map<Player, Container> containers;
    private Map<Player, DuelStage> stages;
    private Map<Player, List<Item>> ammunitions;
    private Map<Player, Long> waitTimers;
    private Map<Player, Location> startLocations;
    private ArenaArea arena;
    private int settings;
    private boolean countdown;
    private boolean completed;

    public Duel(final Player player, final Player opponent) {
        this.player = player;
        this.opponent = opponent;
        containers = new HashMap<>(2);
        stages = new HashMap<>(2);
        ammunitions = new HashMap<>(2);
        waitTimers = new HashMap<>(2);
        startLocations = new HashMap<>(2);
        stages.put(player, DuelStage.NONE);
        stages.put(opponent, DuelStage.NONE);
        ammunitions.put(player, new ArrayList<>());
        ammunitions.put(opponent, new ArrayList<>());
        player.setDuel(this);
        opponent.setDuel(this);
    }

    public static void beforeShutdown() {
        try {
            final RSPolygon lobbyPolygon = LOBBY.getPolygon(0);
            for (final Player player : World.getPlayers()) {
                if (player.getAreaManager() == null) {
                    continue;
                }
                if (player.getArea() instanceof ArenaArea) {
                    final Duel duel = player.getDuel();
                    final Player opponent = duel.getOpponent();
                    duel.containers.get(player).getItems().values().forEach(item -> player.getInventory().addItem(item).onFailure(remaining -> player.getBank().add(remaining)));
                    duel.containers.get(opponent).getItems().values().forEach(item -> opponent.getInventory().addItem(item).onFailure(remaining -> opponent.getBank().add(remaining)));
                    duel.getAmmunitions().get(player).stream().filter(Objects::nonNull).forEach(ammo -> player.getInventory().addItem(ammo).onFailure(i -> player.getBank().add(i)));
                    duel.getAmmunitions().get(opponent).stream().filter(Objects::nonNull).forEach(ammo -> opponent.getInventory().addItem(ammo).onFailure(i -> opponent.getBank().add(i)));
                    duel.containers.get(player).clear();
                    duel.containers.get(opponent).clear();
                    player.forceLocation(new Location(getRandomPoint(lobbyPolygon, 0, location -> World.isFloorFree(location, 1))));
                    opponent.forceLocation(new Location(getRandomPoint(lobbyPolygon, 0, location -> World.isFloorFree(location, 1))));
                    GlobalAreaManager.update(player, false, false);
                    GlobalAreaManager.update(opponent, false, false);
                    duel.reset(player);
                    duel.reset(opponent);
                    player.setDuel(null);
                    opponent.setDuel(null);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static Location getRandomPoint(final RSPolygon polygon, final int plane, final Predicate<Location> predicate) {
        if (!polygon.containsPlane(plane)) throw new RuntimeException("Polygon does not cover plane " + plane);
        final Polygon poly = polygon.getPolygon();
        final Rectangle2D box = poly.getBounds2D();
        int count = 1000;
        Location location = new Location(0);
        do {
            if (--count <= 0) {
                throw new RuntimeException("Unable to find a valid point in polygon.");
            }
            location.setLocation((int) box.getMinX() + Utils.random((int) box.getWidth()), (int) box.getMinY() + Utils.random((int) box.getHeight()), plane);
        } while (!poly.contains(location.getX(), location.getY()) || !predicate.test(location));
        return location;
    }

    public void setRules(final int settings) {
        for (final DuelSetting setting : DuelSetting.SETTINGS.values()) {
            final boolean currentValue = Utils.getShiftedBoolean(this.settings, setting.getBit());
            final boolean value = Utils.getShiftedBoolean(settings, setting.getBit());
            if (currentValue != value) {
                toggleRule(setting);
            }
        }
    }

    public void toggleRule(final DuelSetting setting) {
        if (player == null || opponent == null || !player.inArea("Duel Arena") || !opponent.inArea("Duel Arena")) {
            return;
        }
        switch (setting) {
        case NO_MELEE:
            if (hasRule(NO_WEAPON_SWITCH)) {
                player.sendMessage("You can't restrict attack types and have no weapon switching.");
                player.getVarManager().sendVar(286, player.getVarManager().getValue(286));
                return;
            }
            if (hasRule(NO_MAGIC) && hasRule(NO_RANGED)) {
                player.sendMessage("You can't have no melee, no magic, no ranged, how would you fight?");
                return;
            }
            break;
        case NO_MAGIC:
            if (hasRule(NO_WEAPON_SWITCH)) {
                player.sendMessage("You can't restrict attack types and have no weapon switching.");
                player.getVarManager().sendVar(286, player.getVarManager().getValue(286));
                return;
            }
            if (hasRule(NO_MELEE) && hasRule(NO_RANGED)) {
                player.sendMessage("You can't have no melee, no magic, no ranged, how would you fight?");
                return;
            }
            break;
        case NO_RANGED:
            if (hasRule(NO_WEAPON_SWITCH)) {
                player.sendMessage("You can't restrict attack types and have no weapon switching.");
                player.getVarManager().sendVar(286, player.getVarManager().getValue(286));
                return;
            }
            if (hasRule(NO_MELEE) && hasRule(NO_MAGIC)) {
                player.sendMessage("You can't have no melee, no magic, no ranged, how would you fight?");
                return;
            }
            break;
        case NO_WEAPON_SWITCH:
            if (hasRule(NO_MELEE) || hasRule(NO_MAGIC) || hasRule(NO_RANGED)) {
                player.sendMessage("You can't restrict attack types and have no weapon switching.");
                player.getVarManager().sendVar(286, player.getVarManager().getValue(286));
                return;
            }
            break;
        case NO_MOVEMENT:
            if (hasRule(OBSTACLES)) {
                player.sendMessage("You can't have obstacles if you want No Movement.");
                return;
            }
            break;
        case OBSTACLES:
            if (hasRule(NO_MOVEMENT)) {
                player.sendMessage("You can't have No Movement in an arena with obstacles.");
                return;
            }
            break;
        default:
            break;
        }
        settings = Utils.getShiftedValue(settings, setting.getBit());
        player.getVarManager().sendVar(286, settings);
        opponent.getVarManager().sendVar(286, settings);
        opponent.getPacketDispatcher().sendClientScript(968, (2000 << 16) + 106, setting.getBit());
        opponent.sendMessage("Duel Option change - " + setting.getName() + (hasRule(setting) ? " ON!" : " OFF!"));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, SETTINGS_INTERFACE_INFO, "<col=ff0000>An option has changed - check before accepting!");
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, SETTINGS_INTERFACE_INFO, "");
        player.getPacketDispatcher().sendComponentVisibility(SETTINGS_INTERFACE, SETTINGS_INTERFACE_PRESET, !(settings == player.getNumericAttribute("duelPresetSettings").intValue()));
        player.getPacketDispatcher().sendComponentVisibility(SETTINGS_INTERFACE, SETTINGS_INTERFACE_LAST_DUEL, !(settings == player.getNumericAttribute("lastDuelSettings").intValue()));
        opponent.getPacketDispatcher().sendComponentVisibility(SETTINGS_INTERFACE, SETTINGS_INTERFACE_PRESET, !(settings == opponent.getNumericAttribute("duelPresetSettings").intValue()));
        opponent.getPacketDispatcher().sendComponentVisibility(SETTINGS_INTERFACE, SETTINGS_INTERFACE_LAST_DUEL, !(settings == opponent.getNumericAttribute("lastDuelSettings").intValue()));
        player.getPacketDispatcher().sendClientScript(10590, settings == WHIP_SETTINGS ? 1 : 0, settings == BOXING_SETTINGS ? 1 : 0);
        opponent.getPacketDispatcher().sendClientScript(10590, settings == WHIP_SETTINGS ? 1 : 0, settings == BOXING_SETTINGS ? 1 : 0);
        stages.put(player, DuelStage.NONE);
        stages.put(opponent, DuelStage.NONE);
        waitTimers.put(opponent, Utils.currentTimeMillis() + 3000);
    }

    public void openChallenge() {
        if (player == null || opponent == null) {
            return;
        }
        player.stopAll();
        player.getVarManager().sendVar(286, settings = 0);
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, SETTINGS_INTERFACE);
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 35, "Dueling with: " + opponent.getPlayerInformation().getDisplayname());
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 34, Utils.getLevelColour(player.getSkills().getCombatLevel(), opponent.getSkills().getCombatLevel()) + "Combat level: " + opponent.getSkills().getCombatLevel());
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 8, Utils.getLevelColour(player.getSkills().getLevel(SkillConstants.ATTACK), opponent.getSkills().getLevel(SkillConstants.ATTACK)) + opponent.getSkills().getLevel(SkillConstants.ATTACK));
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 9, Utils.getLevelColour(player.getSkills().getLevelForXp(SkillConstants.ATTACK), opponent.getSkills().getLevelForXp(SkillConstants.ATTACK)) + opponent.getSkills().getLevelForXp(SkillConstants.ATTACK));
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 12, Utils.getLevelColour(player.getSkills().getLevel(SkillConstants.STRENGTH), opponent.getSkills().getLevel(SkillConstants.STRENGTH)) + opponent.getSkills().getLevel(SkillConstants.STRENGTH));
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 13, Utils.getLevelColour(player.getSkills().getLevelForXp(SkillConstants.STRENGTH), opponent.getSkills().getLevelForXp(SkillConstants.STRENGTH)) + opponent.getSkills().getLevelForXp(SkillConstants.STRENGTH));
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 16, Utils.getLevelColour(player.getSkills().getLevel(SkillConstants.DEFENCE), opponent.getSkills().getLevel(SkillConstants.DEFENCE)) + opponent.getSkills().getLevel(SkillConstants.DEFENCE));
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 17, Utils.getLevelColour(player.getSkills().getLevelForXp(SkillConstants.DEFENCE), opponent.getSkills().getLevelForXp(SkillConstants.DEFENCE)) + opponent.getSkills().getLevelForXp(SkillConstants.DEFENCE));
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 20, Utils.getLevelColour(player.getSkills().getLevel(SkillConstants.HITPOINTS), opponent.getSkills().getLevel(SkillConstants.HITPOINTS)) + opponent.getSkills().getLevel(SkillConstants.HITPOINTS));
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 21, Utils.getLevelColour(player.getSkills().getLevelForXp(SkillConstants.HITPOINTS), opponent.getSkills().getLevelForXp(SkillConstants.HITPOINTS)) + opponent.getSkills().getLevelForXp(SkillConstants.HITPOINTS));
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 24, Utils.getLevelColour(player.getPrayerManager().getPrayerPoints(), opponent.getPrayerManager().getPrayerPoints()) + opponent.getPrayerManager().getPrayerPoints());
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 25, Utils.getLevelColour(player.getSkills().getLevelForXp(SkillConstants.PRAYER), opponent.getSkills().getLevelForXp(SkillConstants.PRAYER)) + opponent.getSkills().getLevelForXp(SkillConstants.PRAYER));
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 28, Utils.getLevelColour(player.getSkills().getLevel(SkillConstants.RANGED), opponent.getSkills().getLevel(SkillConstants.RANGED)) + opponent.getSkills().getLevel(SkillConstants.RANGED));
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 29, Utils.getLevelColour(player.getSkills().getLevelForXp(SkillConstants.RANGED), opponent.getSkills().getLevelForXp(SkillConstants.RANGED)) + opponent.getSkills().getLevelForXp(SkillConstants.RANGED));
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 32, Utils.getLevelColour(player.getSkills().getLevel(SkillConstants.MAGIC), opponent.getSkills().getLevel(SkillConstants.MAGIC)) + opponent.getSkills().getLevel(SkillConstants.MAGIC));
        player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 33, Utils.getLevelColour(player.getSkills().getLevelForXp(SkillConstants.MAGIC), opponent.getSkills().getLevelForXp(SkillConstants.MAGIC)) + opponent.getSkills().getLevelForXp(SkillConstants.MAGIC));
        // player.setCloseInterfacesEvent(() -> close(true));
        opponent.stopAll();
        opponent.getVarManager().sendVar(286, settings = 0);
        opponent.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, SETTINGS_INTERFACE);
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 35, "Dueling with: " + player.getPlayerInformation().getDisplayname());
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 34, Utils.getLevelColour(opponent.getSkills().getCombatLevel(), player.getSkills().getCombatLevel()) + "Combat level: " + player.getSkills().getCombatLevel());
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 8, Utils.getLevelColour(opponent.getSkills().getLevel(SkillConstants.ATTACK), player.getSkills().getLevel(SkillConstants.ATTACK)) + player.getSkills().getLevel(SkillConstants.ATTACK));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 9, Utils.getLevelColour(opponent.getSkills().getLevelForXp(SkillConstants.ATTACK), player.getSkills().getLevelForXp(SkillConstants.ATTACK)) + player.getSkills().getLevelForXp(SkillConstants.ATTACK));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 12, Utils.getLevelColour(opponent.getSkills().getLevel(SkillConstants.STRENGTH), player.getSkills().getLevel(SkillConstants.STRENGTH)) + player.getSkills().getLevel(SkillConstants.STRENGTH));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 13, Utils.getLevelColour(opponent.getSkills().getLevelForXp(SkillConstants.STRENGTH), player.getSkills().getLevelForXp(SkillConstants.STRENGTH)) + player.getSkills().getLevelForXp(SkillConstants.STRENGTH));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 16, Utils.getLevelColour(opponent.getSkills().getLevel(SkillConstants.DEFENCE), player.getSkills().getLevel(SkillConstants.DEFENCE)) + player.getSkills().getLevel(SkillConstants.DEFENCE));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 17, Utils.getLevelColour(opponent.getSkills().getLevelForXp(SkillConstants.DEFENCE), player.getSkills().getLevelForXp(SkillConstants.DEFENCE)) + player.getSkills().getLevelForXp(SkillConstants.DEFENCE));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 20, Utils.getLevelColour(opponent.getSkills().getLevel(SkillConstants.HITPOINTS), player.getSkills().getLevel(SkillConstants.HITPOINTS)) + player.getSkills().getLevel(SkillConstants.HITPOINTS));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 21, Utils.getLevelColour(opponent.getSkills().getLevelForXp(SkillConstants.HITPOINTS), player.getSkills().getLevelForXp(SkillConstants.HITPOINTS)) + player.getSkills().getLevelForXp(SkillConstants.HITPOINTS));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 24, Utils.getLevelColour(opponent.getPrayerManager().getPrayerPoints(), player.getPrayerManager().getPrayerPoints()) + player.getPrayerManager().getPrayerPoints());
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 25, Utils.getLevelColour(opponent.getSkills().getLevelForXp(SkillConstants.PRAYER), player.getSkills().getLevelForXp(SkillConstants.PRAYER)) + player.getSkills().getLevelForXp(SkillConstants.PRAYER));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 28, Utils.getLevelColour(opponent.getSkills().getLevel(SkillConstants.RANGED), player.getSkills().getLevel(SkillConstants.RANGED)) + player.getSkills().getLevel(SkillConstants.RANGED));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 29, Utils.getLevelColour(opponent.getSkills().getLevelForXp(SkillConstants.RANGED), player.getSkills().getLevelForXp(SkillConstants.RANGED)) + player.getSkills().getLevelForXp(SkillConstants.RANGED));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 32, Utils.getLevelColour(opponent.getSkills().getLevel(SkillConstants.MAGIC), player.getSkills().getLevel(SkillConstants.MAGIC)) + player.getSkills().getLevel(SkillConstants.MAGIC));
        opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, 33, Utils.getLevelColour(opponent.getSkills().getLevelForXp(SkillConstants.MAGIC), player.getSkills().getLevelForXp(SkillConstants.MAGIC)) + player.getSkills().getLevelForXp(SkillConstants.MAGIC));
        startLocations.put(player, player.getLocation());
        startLocations.put(opponent, opponent.getLocation());
        //opponent.setCloseInterfacesEvent(() -> close(true));
    }

    public final void confirm(final DuelStage stage) {
        if (!player.inArea("Duel Arena") || !opponent.inArea("Duel Arena")) {
            return;
        }
        final Long timer = waitTimers.get(player);
        if (timer != null && timer > Utils.currentTimeMillis()) {
            return;
        }
        switch (stage) {
        case SETTINGS:
            player.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, SETTINGS_INTERFACE_INFO, "<col=ff0000>Waiting for other player...</col>");
            opponent.getPacketDispatcher().sendComponentText(SETTINGS_INTERFACE, SETTINGS_INTERFACE_INFO, "<col=ff0000>Other player has accepted.</col>");
            break;
        case STAKE:
            player.getPacketDispatcher().sendComponentText(STAKING_INTERFACE, 74, "<col=ff0000>Waiting for other player...</col>");
            opponent.getPacketDispatcher().sendComponentText(STAKING_INTERFACE, 74, "<col=ff0000>Other player has accepted.</col>");
            player.getInterfaceHandler().closeInput();
            opponent.getInterfaceHandler().closeInput();
            break;
        case CONFIRMATION:
            player.getPacketDispatcher().sendComponentText(CONFIRMATION_INTERFACE, 51, "Waiting for other player...");
            opponent.getPacketDispatcher().sendComponentText(CONFIRMATION_INTERFACE, 51, "Other player has accepted.");
            break;
        }
        stages.put(player, stage);
        final DuelStage opponentStage = stages.get(opponent);
        if (opponentStage == null) {
            return;
        }
        if (stage.equals(DuelStage.SETTINGS) && opponentStage.equals(DuelStage.SETTINGS)) {
            containers.put(player, new Container(ContainerPolicy.NORMAL, ContainerType.DUEL_STAKE, Optional.of(player)));
            containers.put(opponent, new Container(ContainerPolicy.NORMAL, ContainerType.DUEL_STAKE, Optional.of(opponent)));
            if (hasRule(LEFT_HAND) || hasRule(RIGHT_HAND)) {
                player.sendMessage("Beware: You won't be able to use two-handed weapons such as bows.");
                opponent.sendMessage("Beware: You won't be able to use two-handed weapons such as bows.");
            }
            Arrays.asList(player, opponent).forEach(GameInterface.DUEL_STAKING::open);
        } else
        /*Arrays.asList(player, opponent).forEach(p -> {
                p.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, STAKING_INTERFACE);
                p.getInterfaceHandler().sendInterface(InterfacePosition.SINGLE_TAB, 421);
                p.getPacketDispatcher().sendClientScript(149, 27590657, 93, 4, 7, 0, -1, "Use", "", "", "", "");
                p.getPacketDispatcher().sendComponentSettings(421, 1, 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP10);
                p.getPacketDispatcher().sendComponentSettings(STAKING_INTERFACE, 19, 0, 5, AccessMask.CLICK_OP1);
                p.getPacketDispatcher().sendComponentSettings(STAKING_INTERFACE, 20, 0, 5, AccessMask.CLICK_OP1);
                p.getPacketDispatcher().sendComponentText(STAKING_INTERFACE, 24, opponent.getName() + "'s stake:");
                p.getPacketDispatcher().sendUpdateItemContainer(containers.get(player));
                p.getPacketDispatcher().sendUpdateItemContainer(containers.get(opponent));
            });
            player.getPacketDispatcher().sendComponentText(STAKING_INTERFACE, 24, opponent.getName() + "'s stake:");
            opponent.getPacketDispatcher().sendComponentText(STAKING_INTERFACE, 24, player.getName() + "'s stake:");*/
        //updateInventories();
        if (stage.equals(DuelStage.STAKE) && opponentStage.equals(DuelStage.STAKE)) {
            final Container container = containers.get(player);
            final Container opponentContainer = containers.get(opponent);
            int size = 0;
            int sizePlayer = 0;
            int sizeOpponent = 0;
            for (final Item item : ItemUtil.concatenate(container.getItems().values(), opponentContainer.getItems().values())) {
                if (item.getDefinitions().isStackable() && player.getInventory().containsItem(item)) {
                    continue;
                }
                size++;
            }
            for (int i = HEAD.ordinal(); i <= AMMUNITION.ordinal(); i++) {
                if (Utils.getShiftedBoolean(settings, VALUES[i].getBit())) {
                    final int slot = i == LEG.ordinal() ? 7 : i == HAND.ordinal() ? 9 : i == FEET.ordinal() ? 10 : i == RING.ordinal() ? 12 : i == AMMUNITION.ordinal() ? 13 : i - 13;
                    if (player.getEquipment().getId(slot) != -1) {
                        sizePlayer++;
                    }
                    if (opponent.getEquipment().getId(slot) != -1) {
                        sizeOpponent++;
                    }
                }
            }
            for (final Item item : container.getItems().values()) {
                if (opponent.getInventory().getAmountOf(item.getId()) + item.getAmount() < 0) {
                    opponent.sendMessage("You are holding too many of the same item to continue this stake.");
                    player.sendMessage("Other player has declined the duel.");
                    close(false);
                    return;
                }
            }
            for (final Item item : opponentContainer.getItems().values()) {
                if (player.getInventory().getAmountOf(item.getId()) + item.getAmount() < 0) {
                    player.sendMessage("You are holding too many of the same item to continue this stake.");
                    opponent.sendMessage("Other player has declined the duel.");
                    close(false);
                    return;
                }
            }
            if (player.getInventory().getFreeSlots() < size + sizePlayer) {
                player.sendMessage("You don't have enough inventory space to accept this duel.");
                opponent.sendMessage("Other player has declined the duel.");
                close(false);
                return;
            }
            if (opponent.getInventory().getFreeSlots() < size + sizeOpponent) {
                opponent.sendMessage("You don't have enough inventory space to accept this duel.");
                player.sendMessage("Other player has declined the duel.");
                close(false);
                return;
            }
            final StringBuilder details = new StringBuilder();
            final StringBuilder detailsOpponent = new StringBuilder();
            DUEL_CONFIRMATION.open(player);
            DUEL_CONFIRMATION.open(opponent);
            player.getInterfaceHandler().closeInterface(InterfacePosition.SINGLE_TAB);
            opponent.getInterfaceHandler().closeInterface(InterfacePosition.SINGLE_TAB);

            player.getPacketDispatcher().sendClientScript(917, -1, -1); // clear varcs
            opponent.getPacketDispatcher().sendClientScript(917, -1, -1); // clear varcs

            CLOSED_TABS.forEach((id, type) -> {
                player.getInterfaceHandler().closeInterface(type);
                opponent.getInterfaceHandler().closeInterface(type);
            });
            details.append(player.getName()).append("<br>Combat level: ").append(player.getSkills().getCombatLevel()).append("<br>");
            detailsOpponent.append(opponent.getName()).append("<br>Combat level: ").append(opponent.getSkills().getCombatLevel()).append("<br>");
            for (int i = 0; i < 7; i++) {
                details.append(Skills.getSkillName(i)).append(": ").append(player.getSkills().getLevel(i)).append("/").append(player.getSkills().getLevelForXp(i)).append("<br>");
                detailsOpponent.append(Skills.getSkillName(i)).append(": ").append(opponent.getSkills().getLevel(i)).append("/").append(opponent.getSkills().getLevelForXp(i)).append("<br>");
            }

            player.getPacketDispatcher().sendComponentVisibility(CONFIRMATION_INTERFACE, 77, false);
            opponent.getPacketDispatcher().sendComponentVisibility(CONFIRMATION_INTERFACE, 77, false);

            player.getPacketDispatcher().sendComponentText(CONFIRMATION_INTERFACE, 72, detailsOpponent.toString());
            opponent.getPacketDispatcher().sendComponentText(CONFIRMATION_INTERFACE, 72, details.toString());

            player.getPacketDispatcher().sendUpdateItemContainer(134, -1, 64168, container);
            player.getPacketDispatcher().sendUpdateItemContainer(134, -2, 60937, opponentContainer);

            opponent.getPacketDispatcher().sendUpdateItemContainer(134, -1, 64168, opponentContainer);
            opponent.getPacketDispatcher().sendUpdateItemContainer(134, -2, 60937, container);

        } else if (stage.equals(DuelStage.CONFIRMATION) && opponentStage.equals(DuelStage.CONFIRMATION)) {
            if (player.getFollower() != null) {
                if (!player.getInventory().hasFreeSlots()) {
                    player.sendMessage("You currently have a follower, either pick it up or get some inventory space first.");
                    close(false);
                    return;
                }
                final Pet pet = PetWrapper.getByPet(player.getFollower().getId());
                player.getInventory().addItem(pet.itemId(), 1);
                player.getFollower().finish();
                player.setFollower(null);
            }
            if (opponent.getFollower() != null) {
                if (!opponent.getInventory().hasFreeSlots()) {
                    opponent.sendMessage("You currently have a follower, either pick it up or get some inventory space first.");
                    close(false);
                    return;
                }
                final Pet pet = PetWrapper.getByPet(opponent.getFollower().getId());
                opponent.getInventory().addItem(pet.itemId(), 1);
                opponent.getFollower().finish();
                opponent.setFollower(null);
            }
            initiateDuel();
        }
    }

    private boolean prepareDuel(final Player player) {
        if (Utils.getShiftedBoolean(settings, NO_FUN_WEAPONS.getBit())) {
            for (final Integer id : FUN_WEAPONS) {
                if (player.getEquipment().getId(EquipmentSlot.WEAPON) == id && !player.getEquipment().unequipItem(EquipmentSlot.WEAPON.getSlot())) {
                    player.sendMessage("Not enough space in your inventory.");
                    return false;
                }
            }
        }
        for (int i = HEAD.ordinal(); i <= AMMUNITION.ordinal(); i++) {
            if (Utils.getShiftedBoolean(settings, VALUES[i].getBit())) {
                final int slot = i == LEG.ordinal() ? 7 : i == HAND.ordinal() ? 9 : i == FEET.ordinal() ? 10 : i == RING.ordinal() ? 12 : i == AMMUNITION.ordinal() ? 13 : i - 13;
                if (slot == EquipmentSlot.SHIELD.getSlot()) {
                    if (hasRule(RIGHT_HAND)) {
                        final Item weapon = player.getWeapon();
                        if (weapon != null) {
                            if (weapon.getDefinitions().isTwoHanded()) {
                                if (!player.getEquipment().unequipItem(EquipmentSlot.WEAPON.getSlot())) {
                                    player.sendMessage("Not enough space in your inventory.");
                                    return false;
                                }
                            }
                        }
                    }
                }
                if (player.getEquipment().getId(slot) != -1 && !player.getEquipment().unequipItem(slot)) {
                    player.sendMessage("Not enough space in your inventory.");
                    return false;
                }
            }
        }
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        player.getInterfaceHandler().closeInterface(InterfacePosition.SINGLE_TAB);
        player.sendMessage("Accepted stake and duel options.");
        return true;
    }

    public void resetAttributes(final Player player) {
        if (player.getAttributes().remove("vengeance") != null) {
            player.sendMessage("Your Vengeance has been cleared!");
        }
        reset(player);
    }

    private void initiateDuel() {
        if (!prepareDuel(player) || !prepareDuel(opponent)) {
            stages.put(player, DuelStage.NONE);
            stages.put(opponent, DuelStage.NONE);
            close(true);
            return;
        }
        arena = hasRule(OBSTACLES) ? OBSTACLES_ARENA : hasRule(NO_MOVEMENT) ? NO_MOVEMENT_ARENA : NORMAL_ARENA;
        try {
            Location positionOpponent = getRandomPoint(arena.polygons()[0], 0, location -> hasRule(NO_MOVEMENT) || World.isFloorFree(location, 1));
            final Location position = getRandomPoint(arena.polygons()[0], 0, location -> hasRule(NO_MOVEMENT) || World.isFloorFree(location, 1));
            int counter = 0;
            while (position.equals(positionOpponent) && counter++ < 1000) {
                positionOpponent = getRandomPoint(arena.polygons()[0], 0, location -> hasRule(NO_MOVEMENT) || World.isFloorFree(location, 1));
            }
            player.setLocation(position);
            if (hasRule(NO_MOVEMENT)) {
                try {
                    final Location tile = getLocationNorthOrSouth(arena.polygons()[0], position);
                    opponent.setLocation(tile);
                } catch (final Exception e) {
                    log.error("", e);
                    opponent.setLocation(positionOpponent);
                }
            } else {
                opponent.setLocation(positionOpponent);
            }
        } catch (Exception e) {
            log.error("", e);
            player.sendMessage("Failed to initiate the duel.");
            opponent.sendMessage("Failed to initiate the duel.");
            close(false);
            return;
        }
        player.getVarManager().sendVar(1075, opponent.getIndex());
        opponent.getVarManager().sendVar(1075, player.getIndex());
        player.getPacketDispatcher().sendHintArrow(new HintArrow(opponent));
        opponent.getPacketDispatcher().sendHintArrow(new HintArrow(player));
        player.getInterfaceHandler().closeInterface(InterfacePosition.SINGLE_TAB);
        opponent.getInterfaceHandler().closeInterface(InterfacePosition.SINGLE_TAB);
        player.getInterfaceHandler().closeInput();
        opponent.getInterfaceHandler().closeInput();
        player.addAttribute("lastDuelSettings", settings);
        opponent.addAttribute("lastDuelSettings", settings);
        player.addAttribute("lastDuelStake", new Item[] {new Item(995, containers.get(player).getAmountOf(995)), new Item(13204, containers.get(player).getAmountOf(13204))});
        opponent.addAttribute("lastDuelStake", new Item[] {new Item(995, containers.get(opponent).getAmountOf(995)), new Item(13204, containers.get(opponent).getAmountOf(13204))});
        restoreTabs(player);
        restoreTabs(opponent);
        player.getInterfaceHandler().sendInterface(InterfacePosition.MINIGAME_OVERLAY, 105);
        opponent.getInterfaceHandler().sendInterface(InterfacePosition.MINIGAME_OVERLAY, 105);
        countdown = true;
        WorldTasksManager.schedule(new WorldTask() {
            int ticks = 6;
            @Override
            public void run() {
                if (ticks % 2 == 0) {
                    player.setForceTalk(new ForceTalk("" + (ticks / 2)));
                    opponent.setForceTalk(new ForceTalk("" + (ticks / 2)));
                } else if (ticks == 1) {
                    countdown = false;
                    player.setForceTalk(FIGHT);
                    opponent.setForceTalk(FIGHT);
                    stop();
                }
                ticks--;
            }
        }, 0, 1);
    }

    private void restoreTabs(@NotNull final Player player) {
        player.getInterfaceHandler().openJournal();
        GameInterface.COMBAT_TAB.open(player);
        GameInterface.SKILLS_TAB.open(player);
        GameInterface.INVENTORY_TAB.open(player);
        GameInterface.EQUIPMENT_TAB.open(player);
        GameInterface.PRAYER_TAB_INTERFACE.open(player);
        GameInterface.SPELLBOOK.open(player);
        GameInterface.SETTINGS.open(player);
        GameInterface.EMOTE_TAB.open(player);
    }

    public void finishDuel(final Player winner, final Player loser) {
        completed = true;
        final List<Player> players = Arrays.asList(winner, loser);
        for (final Player player : players) {
            try {
                final boolean win = player == winner;
                final Container container = containers.get(player);
                final Container opponentContainer = containers.get(player == winner ? loser : winner);
                player.blockIncomingHits();
                player.unlock();
                player.setAnimation(Animation.STOP);
                player.getPacketDispatcher().resetHintArrow();
                player.setLocation(startLocations.get(player));
                if (win) {
                    player.getMusic().playJingle(98);
                }
                reset(player);
                player.getInterfaceHandler().closeInterface(InterfacePosition.DIALOGUE);
                GameInterface.DUEL_OVERLAY.open(player);
                player.getPacketDispatcher().sendPlayerOption(1, "Challenge", false);
                player.addAttribute(win ? "DuelsWon" : "DuelsLost", player.getNumericAttribute(win ? "DuelsWon" : "DuelsLost").intValue() + 1);
                final int wins = player.getNumericAttribute("DuelsWon").intValue();
                final int losses = player.getNumericAttribute("DuelsLost").intValue();
                player.sendMessage((win ? "You won! " : "You were defeated! ") + "You have won " + wins + " duel" + (wins != 1 ? "s." : "."));
                player.sendMessage("You have now lost " + losses + " duel" + (losses != 1 ? "s." : "."));


                final PacketDispatcher winnerDispatcher = winner.getPacketDispatcher();
                winner.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, WINNINGS_INTERFACE);
                winnerDispatcher.sendComponentText(WINNINGS_INTERFACE, 22, loser.getCombatLevel());
                winnerDispatcher.sendComponentText(WINNINGS_INTERFACE, 23, loser.getName());
                winnerDispatcher.sendClientScript(149, WINNINGS_INTERFACE << 16 | 33, 541, 6, 6, 0, -1, "", "", "", "", "");
                winnerDispatcher.sendComponentSettings(WINNINGS_INTERFACE, 33, 0, 35, 1024);
                winnerDispatcher.sendUpdateItemContainer(541, -1, 63761, getContainer(loser));

                final Int2ObjectLinkedOpenHashMap<Item> itemsClone = container.getItems().clone();
                final Int2ObjectLinkedOpenHashMap<Item> opponentItemsClone = opponentContainer.getItems().clone();
                player.setCloseInterfacesEvent(() -> {
                    if (win) {
                        player.log(LogLevel.INFO, "Won stake of items: \nPlayer items: " + container.getItems() + "\nPartner items: " + opponentContainer.getItems());
                        GameLogger.log(Level.INFO, () -> new GameLogMessage.Duel(
                                Instant.Companion.now(),
                                winner.getUsername(),
                                winner.getUsername(),
                                loser.getUsername(),
                                itemsClone,
                                opponentItemsClone
                        ));
                        for (final Item item : ItemUtil.concatenate(container.getItems().values(), opponentContainer.getItems().values())) {
                            if (item == null)
                                continue;
                            final int wonAmount = item.getAmount();
                            final Item wonItem = new Item(item.getId(), wonAmount);
                            player.getInventory().addItem(wonItem).onFailure(i -> {
                                World.spawnFloorItem(i, player);
                                player.sendMessage(Colour.RED + "Some of the " + wonItem.getName() + " have been placed on the ground.");
                            });
                        }
                        container.clear();
                        opponentContainer.clear();
                    }
                    for (final Item ammo : ammunitions.get(player)) {
                        if (ammo == null) {
                            continue;
                        }
                        player.getInventory().addItem(ammo).onFailure(i -> {
                            World.spawnFloorItem(i, player);
                            player.sendMessage(Colour.RED + "Some of the ammunition has been placed on the ground.");
                        });
                    }
                });
            } catch (Exception e) {
                log.error("", e);
            }
        }
        arena = null;
    }

    public void sendSpoils(final Player player) {
        final Player winner = player.getName().equals(this.player.getName()) ? this.player : opponent;
        final Player loser = player.getName().equals(this.player.getName()) ? opponent : this.player;
        final Container container = containers.get(player);
        final Container opponentContainer = containers.get(opponent);
        winner.getPacketDispatcher().sendUpdateItemContainer(winner.getName().equals(this.player.getName()) ? opponentContainer : container, ContainerType.SPOILS_STAKE);
        loser.getPacketDispatcher().sendUpdateItemContainer(loser.getName().equals(this.player.getName()) ? container : opponentContainer, ContainerType.SPOILS_STAKE);
        player.blockIncomingHits();
    }

    public void close(final boolean message) {
        if (player == null || opponent == null) {
            return;
        }
        if (stages.get(player).equals(DuelStage.CONFIRMATION) && stages.get(opponent).equals(DuelStage.CONFIRMATION)) {
            return;
        }
        if (message) {
            player.sendMessage("You declined the duel.");
            opponent.sendMessage("The other player declined the duel.");
        }
        final Container container = containers.get(player);
        final Container opponentContainer = containers.get(opponent);
        if (container != null) {
            for (final Item item : container.getItems().values()) {
                if (item == null) {
                    continue;
                }
                player.getInventory().addOrDrop(item);
            }
            container.clear();
        }
        if (opponentContainer != null) {
            for (final Item item : opponentContainer.getItems().values()) {
                if (item == null) {
                    continue;
                }
                opponent.getInventory().addOrDrop(item);
            }
            opponentContainer.clear();
        }
        opponent.setDuel(null);
        player.setDuel(null);
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL, true, false, false);
        opponent.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL, true, false, false);
        player.getInterfaceHandler().closeInterface(InterfacePosition.SINGLE_TAB);
        opponent.getInterfaceHandler().closeInterface(InterfacePosition.SINGLE_TAB);
        player.getInterfaceHandler().closeInput();
        opponent.getInterfaceHandler().closeInput();
        restoreTabs(player);
        restoreTabs(opponent);
        //Refresh the inventories a tick after because of process order; Do not change this. Covers an edge case!
        WorldTasksManager.schedule(() -> {
            player.getInventory().refreshAll();
            opponent.getInventory().refreshAll();
        });
    }

    public void setItem(final int itemId, final int requestedAmount) {
        if (cannotStakeItems())
            return;
        final Container container = containers.get(player);
        if (container == null) {
            return;
        }
        final Inventory inventory = player.getInventory();
        final int previousAmount = container.getAmountOf(itemId);
        if (requestedAmount > previousAmount) {
            container.deposit(player, inventory.getContainer(), inventory.getContainer().getSlotOf(itemId), requestedAmount - previousAmount);
            inventory.refresh();
        } else {
            inventory.getContainer().deposit(player, container, container.getSlotOf(itemId), previousAmount - requestedAmount);
            inventory.refresh();
        }
        final int currentAmount = container.getAmountOf(itemId);
        if (currentAmount - previousAmount == 0) {
            return;
        }
//        opponent.getPacketDispatcher().sendClientScript(1450, 31522846, itemId, container.getAmountOf(itemId), 31522896, 31522906, 31522910);
        if (previousAmount < currentAmount) {
            opponent.sendMessage("Duel Stake addition: " + StringFormatUtil.format(currentAmount - previousAmount) + " x " + ItemDefinitions.get(itemId).getName() + " added!");
        } else {
            opponent.sendMessage("Duel Stake removal: " + StringFormatUtil.format(previousAmount - currentAmount) + " x " + ItemDefinitions.get(itemId).getName() + " removed!");
        }
        player.getPacketDispatcher().sendUpdateItemContainer(container, ContainerType.DUEL_STAKE);
        opponent.getPacketDispatcher().sendUpdateItemContainer(containers.get(opponent), ContainerType.DUEL_STAKE);
        stages.put(player, DuelStage.NONE);
        stages.put(opponent, DuelStage.NONE);
        waitTimers.put(opponent, Utils.currentTimeMillis() + 3000);
        updateValues(STAKING_INTERFACE, 17, 25);
    }

    private boolean cannotStakeItems() {
        if (player == null || opponent == null || !player.inArea("Duel Arena") || !opponent.inArea("Duel Arena") || !player.getInterfaceHandler().isPresent(GameInterface.DUEL_STAKING) || !opponent.getInterfaceHandler().isPresent(GameInterface.DUEL_STAKING)) {
            return true;
        }
        if (opponent.isIronman()) {
            player.sendMessage("You're dueling an Iron Man, so you can't stake items in a duel.");
            return true;
        }
        if (player.isIronman()) {
            player.sendMessage("You're an Iron Man, so you can't stake items in a duel.");
            return true;
        }
        return false;
    }

    public void addItem(final int itemId, final int requestedAmount) {

        if (cannotStakeItems())
            return;

        final Container container = containers.get(player);
        if (container == null) {
            return;
        }
        final int previousAmount = container.getAmountOf(itemId);
        final Inventory inventory = player.getInventory();
        container.deposit(player, inventory.getContainer(), inventory.getContainer().getSlotOf(itemId), requestedAmount);
        inventory.refresh();
        for (int slot : container.getModifiedSlots())
            opponent.getPacketDispatcher().sendClientScript(1450, (GameInterface.DUEL_STAKING.getId() << 16) | 28, slot, (1999 << 16) + 73);
        opponent.sendMessage("Duel Stake addition: " + StringFormatUtil.format(container.getAmountOf(itemId) - previousAmount) + " x " + ItemDefinitions.get(itemId).getName() + " added!");
        player.getPacketDispatcher().sendUpdateItemContainer(container, ContainerType.DUEL_STAKE);
        opponent.getPacketDispatcher().sendUpdateItemContainer(container, ContainerType.OPPONENT_STAKE);
        opponent.getPacketDispatcher().sendComponentText(STAKING_INTERFACE, 74, "<col=ff0000>Stake has changed - check before accepting!");
        player.getPacketDispatcher().sendComponentText(STAKING_INTERFACE, 74, "");
        if (hasRule(SHOW_INVENTORIES))
            DuelExtKt.updateInventories(this);
        stages.put(player, DuelStage.NONE);
        stages.put(opponent, DuelStage.NONE);
        waitTimers.put(opponent, Utils.currentTimeMillis() + 3000);
        updateValues(STAKING_INTERFACE, 17, 25);
    }

    public void removeItem(final int itemId, final int requestedAmount) {

        if (cannotStakeItems())
            return;

        final Container container = containers.get(player);
        if (container == null) {
            return;
        }
        final int previousAmount = container.getAmountOf(itemId);
        final int slot = container.getSlotOf(itemId);
        player.getInventory().getContainer().deposit(player, container, slot, requestedAmount);
        player.getInventory().refresh();
        opponent.getPacketDispatcher().sendClientScript(1450, (1999 << 16) + 28, slot, (1999 << 16) + 73);
        opponent.sendMessage("Duel Stake removal: " + StringFormatUtil.format(previousAmount - container.getAmountOf(itemId)) + " x " + ItemDefinitions.get(itemId).getName() + " removed!");
        player.getPacketDispatcher().sendUpdateItemContainer(container, ContainerType.DUEL_STAKE);
        opponent.getPacketDispatcher().sendUpdateItemContainer(container, ContainerType.OPPONENT_STAKE);
        opponent.getPacketDispatcher().sendComponentText(STAKING_INTERFACE, 74, "<col=ff0000>Stake has changed - check before accepting!");
        player.getPacketDispatcher().sendComponentText(STAKING_INTERFACE, 74, "");
        if (hasRule(SHOW_INVENTORIES))
            DuelExtKt.updateInventories(this);
        stages.put(player, DuelStage.NONE);
        stages.put(opponent, DuelStage.NONE);
        waitTimers.put(opponent, Utils.currentTimeMillis() + 3000);
        updateValues(STAKING_INTERFACE, 17, 25);
    }

    private long getValue(final Container container) {
        long value = 0;
        for (Item item : container.getItems().values())
            value += ItemValueExtKt.getValue(item);
        return value;
    }

    private void reset(final Player player) {
        player.reset();
        player.getCombatDefinitions().setSpecialEnergy(100);
        player.getCombatDefinitions().setAutocastSpell(null);
        player.getToxins().reset();

        player.resetAttackedByDelay();

        // this isn't necessary, because we're only looking to prevent them
        // from being unable to attack when the duel has started, not logging out.
        // - Jire
        //player.resetAttackedTick();

        player.getNextHits().clear();
    }

    private Location getLocationNorthOrSouth(final RSPolygon polygon, final Location tile) throws IllegalStateException {
        final Location north = tile.transform(0, 1, 0);
        final Location south = tile.transform(0, -1, 0);
        if (!polygon.contains(north)) {
            if (!polygon.contains(south)) {
                throw new IllegalStateException();
            }
            return south;
        }
        return north;
    }

    private void updateValues(final int interfaceId, final int first, final int second) {
        long value = getValue(containers.get(opponent));
        String suffix = value >= 1000 && value < 1000000 ? "k" : value >= 1000000 && value < 1000000000 ? "m" : value >= 1000000000 ? "b" : " gp";
        value = suffix.equals("k") ? (int) Math.floor(value / 1000) : suffix.equals("m") ? (int) Math.floor(value / 1000000) : suffix.equals("b") ? (int) Math.floor(value / 1000000000) : value;
        player.getPacketDispatcher().sendComponentText(interfaceId, second, value + suffix + (suffix.equals(" gp") ? "" : " gp"));
        opponent.getPacketDispatcher().sendComponentText(interfaceId, first, value + suffix + (suffix.equals(" gp") ? "" : " gp"));
        value = getValue(containers.get(player));
        suffix = value >= 1000 && value < 1000000 ? "k" : value >= 1000000 && value < 1000000000 ? "m" : value >= 1000000000 ? "b" : " gp";
        value = suffix.equals("k") ? (int) Math.floor(value / 1000) : suffix.equals("m") ? (int) Math.floor(value / 1000000) : suffix.equals("b") ? (int) Math.floor(value / 1000000000) : value;
        player.getPacketDispatcher().sendComponentText(interfaceId, first, value + suffix + (suffix.equals(" gp") ? "" : " gp"));
        opponent.getPacketDispatcher().sendComponentText(interfaceId, second, value + suffix + (suffix.equals(" gp") ? "" : " gp"));
    }

    public boolean hasRule(final DuelSetting setting) {
        return Utils.getShiftedBoolean(settings, setting.getBit());
    }

    public void registerDuelHistory(final Player winner, final Player loser) {
        final String entry = winner.getName()
                + " (" + winner.getSkills().getCombatLevel() + ") beat "
                + loser.getName() + " (" + loser.getSkills().getCombatLevel() + ")";
        if (duelHistory.offerLast(entry)) {
            while (duelHistory.size() > DUEL_HISTORY_CAPACITY) {
                final String first = duelHistory.pollFirst();
                if (first == null) break;
            }
        }
    }

    public Container getContainer(final Player player) {
        return containers.get(player);
    }

    public boolean inDuel() {
        return arena != null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public Map<Player, Container> getContainers() {
        return containers;
    }

    public void setContainers(Map<Player, Container> containers) {
        this.containers = containers;
    }

    public Map<Player, DuelStage> getStages() {
        return stages;
    }

    public void setStages(Map<Player, DuelStage> stages) {
        this.stages = stages;
    }

    public Map<Player, List<Item>> getAmmunitions() {
        return ammunitions;
    }

    public void setAmmunitions(Map<Player, List<Item>> ammunitions) {
        this.ammunitions = ammunitions;
    }

    public Map<Player, Long> getWaitTimers() {
        return waitTimers;
    }

    public void setWaitTimers(Map<Player, Long> waitTimers) {
        this.waitTimers = waitTimers;
    }

    public Map<Player, Location> getStartLocations() {
        return startLocations;
    }

    public void setStartLocations(Map<Player, Location> startLocations) {
        this.startLocations = startLocations;
    }

    public ArenaArea getArena() {
        return arena;
    }

    public void setArena(ArenaArea arena) {
        this.arena = arena;
    }

    public int getSettings() {
        return settings;
    }

    public void setSettings(int settings) {
        this.settings = settings;
    }

    public boolean isCountdown() {
        return countdown;
    }

    public void setCountdown(boolean countdown) {
        this.countdown = countdown;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
