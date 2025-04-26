package com.zenyte.game.world.entity.player;

import com.google.gson.annotations.Expose;
import com.near_reality.api.model.User;
import com.near_reality.api.service.sanction.SanctionPlayerExtKt;
import com.near_reality.api.service.user.UserPlayerAttributesKt;
import com.near_reality.game.content.araxxor.AraxxorInstance;
import com.near_reality.game.content.araxxor.items.AraneaBoots;
import com.near_reality.game.content.bountyhunter.BountyHunterController;
import com.near_reality.game.content.bountyhunter.WildyExtKt;
import com.near_reality.game.content.commands.DeveloperCommands;
import com.near_reality.game.content.middleman.MiddleManManager;
import com.near_reality.game.model.ui.chat_channel.ChatChannelPlayerExtKt;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.near_reality.game.world.entity.player.UsernameProvider;
import com.near_reality.game.world.entity.player.container.impl.SinglePlayerBank;
import com.near_reality.tools.PlayerUUID;
import com.near_reality.tools.logging.GameLogMessage;
import com.near_reality.tools.logging.GameLogger;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.GameConstants;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.AvasDevice;
import com.zenyte.game.content.GodBooks;
import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.content.RespawnPoint;
import com.zenyte.game.content.StorageRoom;
import com.zenyte.game.content.achievementdiary.AchievementDiaries;
import com.zenyte.game.content.achievementdiary.AdventurersLogIcon;
import com.zenyte.game.content.boons.Boon;
import com.zenyte.game.content.boons.BoonManager;
import com.zenyte.game.content.boons.impl.AnimalTamer;
import com.zenyte.game.content.boons.impl.ImRubberYoureGlue;
import com.zenyte.game.content.boons.impl.LunarEnthusiast;
import com.zenyte.game.content.boons.impl.SustainedAggression;
import com.zenyte.game.content.boss.grotesqueguardians.instance.GrotesqueGuardiansInstance;
import com.zenyte.game.content.bountyhunter.BountyHunter;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.party.RaidParty;
import com.zenyte.game.content.chambersofxeric.storageunit.PrivateStorage;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.content.compcapes.CompletionistCape;
import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.content.event.easter2020.EasterConstants;
import com.zenyte.game.content.flowerpoker.GambleBan;
import com.zenyte.game.content.follower.Follower;
import com.zenyte.game.content.follower.PetInsurance;
import com.zenyte.game.content.follower.PetWrapper;
import com.zenyte.game.content.gauntlet.GauntletItemStorage;
import com.zenyte.game.content.grandexchange.GrandExchange;
import com.zenyte.game.content.gravestones.Gravestone;
import com.zenyte.game.content.killstreak.KillstreakLog;
import com.zenyte.game.content.lootkeys.LootkeySettings;
import com.zenyte.game.content.magicstorageunit.MagicStorageUnit;
import com.zenyte.game.content.minigame.barrows.Barrows;
import com.zenyte.game.content.minigame.blastfurnace.BlastFurnace;
import com.zenyte.game.content.minigame.duelarena.Duel;
import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.multicannon.DwarfMultiCannon;
import com.zenyte.game.content.preset.PresetManager;
import com.zenyte.game.content.sailing.CharterLocation;
import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.farming.Farming;
import com.zenyte.game.content.skills.farming.seedvault.SeedVault;
import com.zenyte.game.content.skills.hunter.Hunter;
import com.zenyte.game.content.skills.magic.spells.arceuus.DeathChargeKt;
import com.zenyte.game.content.skills.magic.spells.lunar.SpellbookSwap;
import com.zenyte.game.content.skills.magic.spells.teleports.ForceTeleport;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.skills.prayer.PrayerManager;
import com.zenyte.game.content.skills.slayer.Slayer;
import com.zenyte.game.content.tombsofamascut.AbstractTOAManager;
import com.zenyte.game.content.tombsofamascut.AbstractTOARaidArea;
import com.zenyte.game.content.tombsofamascut.TOAPlayerData;
import com.zenyte.game.content.tombsofamascut.npc.AbstractTOANPC;
import com.zenyte.game.content.treasuretrails.clues.LightBox;
import com.zenyte.game.content.treasuretrails.clues.PuzzleBox;
import com.zenyte.game.content.treasuretrails.stash.Stash;
import com.zenyte.game.content.wheeloffortune.WheelOfFortune;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.BonusXpManager;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.model.item.containers.BonePouch;
import com.zenyte.game.model.item.containers.DragonhidePouch;
import com.zenyte.game.model.item.containers.GemBag;
import com.zenyte.game.model.item.containers.HerbSack;
import com.zenyte.game.model.item.degradableitems.ChargesManager;
import com.zenyte.game.model.item.degradableitems.DegradeType;
import com.zenyte.game.model.polls.PollManager;
import com.zenyte.game.model.shop.Shop;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfaceHandler;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingsInterface;
import com.zenyte.game.packet.GamePacketEncoder;
import com.zenyte.game.packet.PacketDispatcher;
import com.zenyte.game.packet.out.ChatFilterSettings;
import com.zenyte.game.packet.out.SetPrivateChatFilter;
import com.zenyte.game.packet.out.UpdateRebootTimer;
import com.zenyte.game.packet.out.UpdateUID192;
import com.zenyte.game.packet.out.UpdateZonePartialEnclosed;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.SceneSynchronization;
import com.zenyte.game.world.World;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.AbstractEntity;
import com.zenyte.game.world.entity.AnimationMap;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.HitEntry;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.WalkStep;
import com.zenyte.game.world.entity._Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Appearance;
import com.zenyte.game.world.entity.masks.ChatMessage;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.pathfinding.Flags;
import com.zenyte.game.world.entity.pathfinding.events.RouteEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.action.PlayerFollow;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.calog.CALog;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.collectionlog.CollectionLog;
import com.zenyte.game.world.entity.player.collectionlog.CollectionLogRewardManager;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.LootingBag;
import com.zenyte.game.world.entity.player.container.impl.PriceChecker;
import com.zenyte.game.world.entity.player.container.impl.RunePouch;
import com.zenyte.game.world.entity.player.container.impl.SeedBox;
import com.zenyte.game.world.entity.player.container.impl.Trade;
import com.zenyte.game.world.entity.player.container.impl.bank.Bank;
import com.zenyte.game.world.entity.player.container.impl.bank.BankPin;
import com.zenyte.game.world.entity.player.container.impl.bank.BankSetting;
import com.zenyte.game.world.entity.player.container.impl.death.DeathMechanics;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentUtils;
import com.zenyte.game.world.entity.player.controller.ControllerManager;
import com.zenyte.game.world.entity.player.cutscene.CutsceneManager;
import com.zenyte.game.world.entity.player.dailychallenge.DailyChallengeManager;
import com.zenyte.game.world.entity.player.dialogue.DialogueManager;
import com.zenyte.game.world.entity.player.login.Authenticator;
import com.zenyte.game.world.entity.player.loyalty.LoyaltyManager;
import com.zenyte.game.world.entity.player.perk.PerkManager;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;
import com.zenyte.game.world.entity.player.privilege.Crown;
import com.zenyte.game.world.entity.player.privilege.ExpConfiguration;
import com.zenyte.game.world.entity.player.privilege.ExpConfigurations;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.world.entity.player.teleports.TeleportsManager;
import com.zenyte.game.world.entity.player.teleportsystem.TeleportManager;
import com.zenyte.game.world.entity.player.update.NPCInfo;
import com.zenyte.game.world.entity.player.update.PlayerInfo;
import com.zenyte.game.world.entity.player.var.EventType;
import com.zenyte.game.world.entity.player.var.VarCollection;
import com.zenyte.game.world.entity.player.variables.PlayerVariables;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.Chunk;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.LocationMap;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.FullMovementPlugin;
import com.zenyte.game.world.region.area.plugins.GravestoneLocationPlugin;
import com.zenyte.game.world.region.area.plugins.LogoutPlugin;
import com.zenyte.game.world.region.area.plugins.MovementRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.PartialMovementPlugin;
import com.zenyte.game.world.region.area.plugins.TeleportMovementPlugin;
import com.zenyte.game.world.region.area.plugins.TempPlayerStatePlugin;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.game.world.region.areatype.AreaType;
import com.zenyte.game.world.region.areatype.AreaTypes;
import com.zenyte.net.Session;
import com.zenyte.net.io.RSBuffer;
import com.zenyte.plugins.ListenerType;
import com.zenyte.plugins.MethodicPluginHandler;
import com.zenyte.plugins.PluginManager;
import com.zenyte.plugins.dialogue.CountDialogue;
import com.zenyte.plugins.dialogue.ItemDialogue;
import com.zenyte.plugins.dialogue.NameDialogue;
import com.zenyte.plugins.dialogue.StringDialogue;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.plugins.events.LogoutEvent;
import com.zenyte.plugins.events.PlayerResetEvent;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import kotlinx.datetime.Instant;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.TransmogrifiableType;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.awt.*;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Queue;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static com.zenyte.game.GameConstants.WORLD_PROFILE;
import static com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables.*;
import static com.zenyte.game.world.object.ObjectId.ACID_POOL_54148;

/**
 * @author Kris | 29. dets 2017 : 3:52.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
@SuppressWarnings("FieldMayBeFinal")
public class Player extends AbstractEntity implements UsernameProvider {

    private static final Logger log = LoggerFactory.getLogger(Player.class);

    public static final int SCENE_DIAMETER = 104;
    public static final int SCENE_RADIUS = SCENE_DIAMETER >> 1;
    public static final int SMALL_VIEWPORT_RADIUS = 15;
    public static final int LARGE_VIEWPORT_RADIUS = 127;

    public static final Animation DEATH_ANIMATION = new Animation(836);
    private static final ForceTalk VENGEANCE = new ForceTalk("Taste vengeance!");
    private static final HitType[] PROCESSED_HIT_TYPES = new HitType[]{HitType.MELEE, HitType.RANGED, HitType.MAGIC,
            HitType.DEFAULT};
    private static final Graphics ELYSIAN_EFFECT_GFX = new Graphics(321);
    private static final Graphics BULWARK_GFX = new Graphics(1336);
    private static final Animation BULWARK_ANIM = new Animation(7512);
    private static final Animation PLAIN_DEFENCE_ANIM = new Animation(424);
    private static final String[] deathMessages = new String[]{"You have defeated %s.", "You were clearly a better " +
            "fighter " +
            "than %s.", "%s was no match for you.", "With a crushing blow you finish %s.", "%s didn't stand a chance " +
            "against" +
            " you.", "Can anyone defeat you? Certainly not %s.", "%s falls before your might.", "A humiliating defeat" +
            " for " +
            "%s.", "What was %s thinking challenging you...", "What an embarrassing performance by %s.", "RIP %s.",
            "%s " +
                    "probably logged out after that beating.", "Such a shame that %s can't play this game.", "How not" +
            " to do " +
            "it " +
            "right: Written by %s.", "A certain, crouching-over-face animation would be suitable for %s right now.",
            "%s" +
                    " got rekt.", "%s was made to sit down.", "The struggle for %s is real.", "MUM! GET THE CAMERA, I" +
            " JUST " +
            "KILLED %s!", "%s will probably tell you %gender% wanted a free teleport after that performance.", //he/she
            "%s should take lessons from you. You're clearly too good for %gender%."};
    public transient int currentViewIndex = 0;
    //him/her

    private transient User user;
    private transient boolean isLoggedIn = false;
    public transient boolean disconnected = false;

    public transient DynamicArea mapInstance = null;

    public User getUser() {
        return user;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setUser(User user) {
        this.user = user;
        UserPlayerAttributesKt.onSetUser(this, user);
    }

    private final AchievementDiaries achievementDiaries = new AchievementDiaries(this);
    private final transient CutsceneManager cutsceneManager = new CutsceneManager(this);
    private final transient PuzzleBox puzzleBox = new PuzzleBox(this);
    private final transient LightBox lightBox = new LightBox(this);
    private final transient ChargesManager chargesManager = new ChargesManager(this);
    private final transient PollManager pollManager = new PollManager(this);
    private final AreaManager areaManager = new AreaManager(this);
    private final GodBooks godBooks = new GodBooks();
    @Expose
    private final BossTimer bossTimer = new BossTimer(this);
    private final CollectionLog collectionLog = new CollectionLog(this);

    private final CALog caLog = new CALog(this);
    @Expose
    private final TOAPlayerData toaPlayerData = new TOAPlayerData();

    public transient int currentTOAPartyViewingValue = 0;
    public transient int currentTOAPartyManagementTab = 0;

    public TOAPlayerData getToaPlayerData() { return toaPlayerData; }

    @Expose(deserialize = false, serialize = false)
    private final transient AbstractTOAManager toaManager = AbstractTOAManager.getForPlayer(this);

    public AbstractTOAManager getTOAManager() { return toaManager; }

    private final transient DialogueManager dialogueManager = new DialogueManager(this);
    @Expose
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();
    @Expose
    private final ControllerManager controllerManager = new ControllerManager(this);
    @Expose
    private final MusicHandler music = new MusicHandler(this);
    private final PresetManager presetManager = new PresetManager(this);
    @Expose
    private final EmotesHandler emotesHandler = new EmotesHandler(this);
    @Expose
    private final InterfaceHandler interfaceHandler = new InterfaceHandler(this);
    private final List<Integer> trackedHolidayItems = new IntArrayList();
    @Expose
    private final Appearance appearance = new Appearance(this);
    private final transient Set<Container> pendingContainers = new LinkedHashSet<>();
    @Expose
    private final SocialManager socialManager = new SocialManager(this);
    @Expose
    private CombatDefinitions combatDefinitions = new CombatDefinitions(this);
    @Expose
    private final BoonManager boonManager = new BoonManager(this);

    @Expose
    private final CollectionLogRewardManager clRewardManager = new CollectionLogRewardManager(this);
    public CollectionLogRewardManager getCollectionLogRewardManager() {
        return clRewardManager;
    }
    public BoonManager getBoonManager() {
        return this.boonManager;
    }
    private final MagicStorageUnit magicStorageUnit = new MagicStorageUnit();
    @Expose
    private final KillstreakLog killstreakLog = new KillstreakLog();
    private final HpHud hpHud = new HpHud(this);

    @Expose
    private LootkeySettings lootkeySettings;


    public StorageRoom getStorageRoom() {
        return storageRoom;
    }

    private StorageRoom storageRoom = new StorageRoom();
    @Expose
    private DwarfMultiCannon dwarfMulticannon = new DwarfMultiCannon(this);
    /**
     * Always use getter for this field, as presets replace it with a temporary instance.
     */
    @Expose
    private Equipment equipment = new Equipment(this);
    private transient Equipment equipmentTemp = new Equipment(this, true);
    /**
     * Always use getter for this field, as presets replace it with a temporary instance.
     */
    @Expose
    private Inventory inventory = new Inventory(this);
    private transient Inventory inventoryTemp = new Inventory(this, true);
    private transient DeathMechanics deathMechanics = new DeathMechanics(this);
    @Expose
    private NotificationSettings notificationSettings = new NotificationSettings(this);
    @Expose
    private PriceChecker priceChecker = new PriceChecker(this);
    @Expose
    private transient Trade trade = new Trade(this);
    private SeedVault seedVault = new SeedVault(this);
    /**
     * Always use getter for this field, as presets replace it with a temporary instance.
     */
    @Expose
    private RunePouch runePouch = new RunePouch(this, TempPlayerStatePlugin.StateType.RUNE_POUCH);
    private transient RunePouch runePouchTemp = new RunePouch(this, TempPlayerStatePlugin.StateType.RUNE_POUCH, true);
    /**
     * Always use getter for this field, as presets replace it with a temporary instance.
     */
    private RunePouch secondaryRunePouch = new RunePouch(this, TempPlayerStatePlugin.StateType.RUNE_POUCH_SECONDARY);
    private transient RunePouch secondaryRunePouchTemp = new RunePouch(this, TempPlayerStatePlugin.StateType.RUNE_POUCH_SECONDARY, true);
    private SeedBox seedBox = new SeedBox(this);
    private LootingBag lootingBag = new LootingBag(this);
    private HerbSack herbSack = new HerbSack(this);
    private BonePouch bonePouch = new BonePouch(this);
    private DragonhidePouch dragonhidePouch = new DragonhidePouch(this);
    private GemBag gemBag = new GemBag(this);
    @Expose
    private Skills skills = new Skills(this);
    private Skills skillsTemp = new Skills(this, true);
    @Expose
    private Settings settings = new Settings(this);
    @Expose
    private Construction construction = new Construction(this);
    @Expose
    private PrayerManager prayerManager = new PrayerManager(this);
    @Expose
    private TeleportManager teleportManager = new TeleportManager(this);
    private VarManager varManager = new VarManager(this);
    private transient PlayerInfo playerViewport = new PlayerInfo(this);
    private transient NPCInfo npcViewport = new NPCInfo(this);
    @Expose
    private PlayerVariables variables = new PlayerVariables(this);
    private transient WorldMap worldMap = new WorldMap(this);
    @Expose
    private GrandExchange grandExchange = new GrandExchange(this);
    private transient Bonuses bonuses = new Bonuses(this);
    private transient String[] options = new String[9];
    private transient Object2LongOpenHashMap<String> attackedByPlayers = new Object2LongOpenHashMap<>();
    private PerkManager perkManager = new PerkManager(this);
    private transient ChatMessage chatMessage = ChatMessage.reusable();
    private Barrows barrows = new Barrows(this);
    private ItemRetrievalService retrievalService = new ItemRetrievalService(this);
    public transient Runnable closeInterfacesEvent;
    private transient boolean needRegionUpdate;
    private transient boolean initialized;
    private transient List<ProjPacket> tempList = new ArrayList<>();
    private transient ActionManager actionManager = new ActionManager(this);
    @Expose
    private PrivateStorage privateStorage = new PrivateStorage(this);
    @Expose
    private GauntletItemStorage gauntletItemStorage = new GauntletItemStorage(this);
    @Expose
    private PlayerInformation playerInformation;
    private transient Entity lastTarget;
    private transient DelayedActionManager delayedActionManager = new DelayedActionManager(this);
    @Expose
    private Farming farming = new Farming(this);
    private transient PacketDispatcher packetDispatcher = new PacketDispatcher(this);
    private PetInsurance petInsurance = new PetInsurance(this);
    @Expose
    private transient Follower follower;
    private int petId;
    private transient boolean canPvp;
    @Expose
    private Stash stash = new Stash(this);
    private transient boolean maximumTolerance;
    private transient Duel duel;
    @Expose
    private SinglePlayerBank bank = new SinglePlayerBank(this);
    @Expose
    private BankPin bankPin = new BankPin();
    private transient AtomicBoolean forceReloadMap = new AtomicBoolean(false);
    private transient int viewDistance = 15;
    private Slayer slayer = new Slayer(this);
    private Hunter hunter = new Hunter(this);

    public Gravestone getGravestone() {
        return gravestone;
    }

    public BankPin getBankPin() {
        return bankPin;
    }
    private Gravestone gravestone = new Gravestone(this);
    private BlastFurnace blastFurnace = new BlastFurnace(this);
    @Expose
    private RespawnPoint respawnPoint = RespawnPoint.EDGEVILLE;
    private DailyChallengeManager dailyChallengeManager = new DailyChallengeManager(this);
    private transient Optional<GrotesqueGuardiansInstance> grotesqueGuardiansInstance;

    private transient AraneaBoots araneaBoots = new AraneaBoots(this);

    public AraneaBoots getAraneaBoots() {
        return araneaBoots;
    }

    public void setRespawnPoint(final RespawnPoint point) {
        this.respawnPoint = point;
    }

    private transient int pid;
    private transient boolean loadingRegion;
    private transient long movementLock;
    private transient long diceDelay;
    private transient String[] nametags;
    @Expose
    private GameMode gameMode = GameMode.REGULAR;
    private MemberRank memberRank = MemberRank.NONE;
    @Expose
    private PlayerPrivilege privilege = PlayerPrivilege.PLAYER;
    private transient LogoutType logoutType = LogoutType.NONE;
    private WheelOfFortune wheelOfFortune = new WheelOfFortune(this);
    private transient boolean updatingNPCOptions = true;
    private transient boolean updateNPCOptions;
    private transient IntSet pendingVars = new IntLinkedOpenHashSet(100);
    private transient Runnable pathfindingEvent;
    private transient RouteEvent<Player, EntityStrategy> combatEvent;
    private transient Rectangle sceneRectangle;
    private transient Int2ObjectMap<List<GamePacketEncoder>> zoneFollowPackets = new Int2ObjectOpenHashMap<>();
    private transient boolean heatmap;
    private transient IntOpenHashSet chunksInScope =
            new IntOpenHashSet(SceneSynchronization.CHUNK_SYNCHRONIZATION_MAX_COUNT);
    private transient int heatmapRenderDistance = SMALL_VIEWPORT_RADIUS;
    private transient boolean hidden;
    private transient int damageSound = -1;
    private IntList paydirt = new IntArrayList();
    private LoyaltyManager loyaltyManager = new LoyaltyManager(this);
    private transient long lastReceivedPacket = System.currentTimeMillis();
    private Authenticator authenticator;
    private transient List<Runnable> postPacketProcessingRunnables = new LinkedList<>();
    private Location lastStep = null;
    private Location followTile = null;
    private transient int cachedPlayerLocationHash, cachedPlayerX, cachedPlayerY, cachedPlayerPlane, cachedPlayerlocation18BitHash; // We cache these for players as it's faster than calculating it based on location hash
    private transient int fakeEquipmentState = TempPlayerStatePlugin.STILL_REAL;
    private transient int fakeInventoryState = TempPlayerStatePlugin.STILL_REAL;
    private transient int fakeRunePouchState = TempPlayerStatePlugin.STILL_REAL;
    private transient int fakeSecondaryRunePouchState = TempPlayerStatePlugin.STILL_REAL;
    private transient int fakeSkillsState = TempPlayerStatePlugin.STILL_REAL;

    private final TeleportsManager teleportsManager = new TeleportsManager(this);

    public TeleportsManager getTeleportsManager() {
        return teleportsManager;
    }

    public void addPostProcessRunnable(@NotNull final Runnable runnable) {
        postPacketProcessingRunnables.add(runnable);
    }

    public void addMovementLock(final MovementLock lock) {
        movementLocks.add(lock);
    }

    public boolean isFullMovementLocked() {
        if (movementLocks.isEmpty()) {
            return false;
        }
        for (MovementLock next : movementLocks) {
            if (!next.isFullLock()) {
                continue;
            }
            if (!next.canWalk(this, false)) {
                return true;
            }
        }
        return false;
    }

    public boolean isMovementLocked(final boolean executeAttachments) {
        if (movementLocks.isEmpty()) {
            return false;
        }
        final Iterator<MovementLock> iterator = movementLocks.iterator();
        while (iterator.hasNext()) {
            final MovementLock next = iterator.next();
            if (!next.canWalk(this, executeAttachments)) {
                return true;
            }
            iterator.remove();
        }
        return false;
    }

    @Expose
    private HashSet<byte[]> UUIDS = new HashSet<>();

    private transient boolean newUUID = false;

    public Player(final PlayerInformation information, final Authenticator authenticator) {
        this.authenticator = authenticator == null ? new Authenticator() : authenticator;
        forceLocation(new Location(GameConstants.REGISTRATION_LOCATION));
        setLastLocation(new Location(getLocation()));
        playerInformation = information;
        getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        getUpdateFlags().flag(UpdateFlag.TEMPORARY_MOVEMENT_TYPE);
        setTeleported(true);
    }

    public void setUUID() {
        if (UUIDS == null)
            UUIDS = new HashSet<>();
        byte[] UUID = playerInformation.getUUID();
        if (PlayerUUID.INSTANCE.isEmpty(UUID)){
            newUUID = true;
            UUID = UUIDS.stream()
                    .findFirst()
                    .orElse(PlayerUUID.INSTANCE.generateUUID());
            playerInformation.setUUID(UUID);
        }
        UUIDS.add(UUID);
    }

    public Set<byte[]> getUUIDS() {
        return UUIDS;
    }

    public boolean canHit(final Player other) {
        if (!other.isCanPvp() || !isCanPvp()) {
            return false;
        }
        final OptionalInt level = WildernessArea.getWildernessLevel(getLocation());
        if (level.isPresent()) {
            return Math.abs(getSkills().getCombatLevel() - other.getSkills().getCombatLevel()) <= level.getAsInt();
        }
        return true;
    }

    public void resetViewDistance() {
        this.viewDistance = SMALL_VIEWPORT_RADIUS;
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance < 1 ? 1 : Math.min(viewDistance, 104);
    }

    private transient Session session;

    public Session getSession() {
        return session;
    }

    public void setSession(final Session session) {
        this.session = session;
    }

    public RegionArea getArea() {
        return areaManager.getArea();
    }

    public boolean isOnMobile() {
        return playerInformation.isOnMobile();
    }

    public boolean updateNPCOptions(final NPC npc) {
        final NPCDefinitions definitions = NPCDefinitions.get(getTransmogrifiedId(npc.getDefinitions(), npc.getId()));
        if (definitions == null) return false;
        return definitions.getFilterFlag() > 0;
    }

    public void teleport(@NotNull final Location location) {
        new ForceTeleport(location).teleport(this);
    }

    /**
     * Checks whether the player is tolerant to the entities around.
     *
     * @return whether it's tolerant or not.
     */
    public final boolean isTolerant(@NotNull final Location tile) {
        boolean hasPerk = getBoonManager() != null && getBoonManager().hasBoon(SustainedAggression.class);
        return this.variables.getToleranceTimer() > TimeUnit.MINUTES.toTicks(hasPerk ? 15 : 10) && inTolerantPosition(tile);
    }

    /**
     * Sets the force movement for the player for the teleportation-type. This means that the player will be
     * teleported to the end destination right as the force movement starts, and the
     * force-movement rewinds the player to the start location client-sided and rolls from the start. To the user,
     * this is seamless and not visible.
     *
     * @param tile                  the tile to which the player is force-moved.
     * @param direction             the direction value which the player will be facing throughout the force
     *                              movement; If absent, default face direction of where the player moves to is
     *                              calculated.
     * @param delayInTicks          the delay in ticks until the player starts the force movement action - a value of
     *                              0 would mean instant start.
     * @param speedInTicks          the delay in ticks for how long the player will be moved through the force
     *                              movement. Minimum value is 1!
     * @param startConsumer         the optional consumer that is executed instantly as the method is executed, this
     *                              will not wait for the force movement to begin.
     * @param movementStartConsumer the optional consumer that is executed as soon as the force movement itself begins.
     * @param endConsumer           the consumer that is executed when the player finishes the force movement.
     */
    public void setTeleportForceMovement(@NotNull final Location tile, @NotNull final OptionalInt direction,
                                         final int delayInTicks, final int speedInTicks,
                                         @NotNull final Optional<Consumer<Location>> startConsumer,
                                         @NotNull final Optional<Consumer<Location>> movementStartConsumer,
                                         @NotNull final Optional<Consumer<Location>> endConsumer) {
        if (speedInTicks < 1) {
            throw new IllegalStateException("Speed must always be positive.");
        }
        startConsumer.ifPresent(consumer -> consumer.accept(tile));
        WorldTasksManager.scheduleOrExecute(() -> {
            movementStartConsumer.ifPresent(consumer -> consumer.accept(tile));
            final Location currentTile = new Location(getLocation());
            setForceMovement(new ForceMovement(new Location(getLocation()), 0, tile, speedInTicks * 30,
                    direction.orElse(DirectionUtil.getFaceDirection(tile.getX() - currentTile.getX(),
                            tile.getY() - currentTile.getY()))));
            setLocation(tile);
            endConsumer.ifPresent(consumer -> WorldTasksManager.scheduleOrExecute(() -> consumer.accept(tile),
                    speedInTicks - 1));
        }, delayInTicks - 1);
    }

    public void autoForceMovement(final Location tile, final int speed) {
        final Location currentTile = new Location(getLocation());
        setLocation(tile);
        final ForceMovement fm = new ForceMovement(currentTile, 1, tile, speed,
                DirectionUtil.getFaceDirection(tile.getX() - currentTile.getX(), tile.getY() - currentTile.getY()));
        setForceMovement(fm);
    }

    public void autoForceMovement(final Location tile, final int delay, final int totalDuration, final int direction) {
        autoForceMovement(getLocation(), tile, delay, totalDuration, direction);
    }

    public void autoForceMovement(final Location tile, final int delay, final int totalDuration, final int direction, Runnable onLand) {
        autoForceMovement(getLocation(), tile, delay, totalDuration, direction, onLand);
    }

    public void autoForceMovement(final Location start, final Location tile, final int delay, final int totalDuration, final int direction) {
        autoForceMovement(getLocation(), tile, delay, totalDuration, direction, null);
    }

    public void autoForceMovement(final Location start, final Location tile, final int delay, final int totalDuration, final int direction, Runnable onLand) {        /*if ((totalDuration) % 30 != 0) {
            throw new RuntimeException("Unable to synchronize players location with forcemovement due to" + " delay
            and duration not being in synchronization with game ticks.");
        }
        if (delay == totalDuration) {
            throw new RuntimeException("Delay cannot be equal to speed.");
        }*/
        final Runnable landRunnable = () -> {
            setLocation(tile);
            if (onLand != null) {
                onLand.run();
            }
        };

        final Location currentTile = new Location(start);
        final ForceMovement fm = new ForceMovement(currentTile, delay, tile, totalDuration, direction);
        setForceMovement(fm);
        WorldTasksManager.schedule(landRunnable::run, (int) Math.ceil(totalDuration / 30.0F) - 1);
    }

    public void autoForceMovement(final Location tile, final int delay, final int totalDuration) {
        autoForceMovement(tile, delay, totalDuration, null);
    }

    public void autoForceMovement(final Location tile, final int delay, final int totalDuration, Runnable onLand) {
        final Location currentTile = new Location(getLocation());
        final int direction = DirectionUtil.getFaceDirection(tile.getX() - currentTile.getX(),
                tile.getY() - currentTile.getY());
        autoForceMovement(tile, delay, totalDuration, direction, onLand);
    }

    public void autoForceMovement(final Location start, final Location dest, final int delay, final int totalDuration) {
        final Location currentTile = new Location(start);
        final int direction = DirectionUtil.getFaceDirection(dest.getX() - start.getX(),
                dest.getY() - start.getY());
        autoForceMovement(start, dest, delay, totalDuration, direction);
    }

    public boolean eligibleForShiftTeleportation() {
        return privilege.eligibleTo(PlayerPrivilege.DEVELOPER) || (privilege.eligibleTo(PlayerPrivilege.ADMINISTRATOR) && !(getArea() instanceof Inferno));
    }

    public void notification(String title, String text, int color) {
        GameInterface.NOTIFICATION.open(this);
        packetDispatcher.sendClientScript(3343, title, text, color);
        WorldTasksManager.schedule(() -> interfaceHandler.closeInterface(GameInterface.NOTIFICATION), 13);
    }

    public void setNametag(final int index, final String string) {
        if (index < 0 || index >= 3) {
            return;
        }
        if (nametags == null) {
            nametags = new String[3];
        }
        nametags[index] = string;
        updateFlags.flag(UpdateFlag.NAMETAG);
    }

    public void resetNametags() {
        nametags = null;
        updateFlags.flag(UpdateFlag.NAMETAG);
    }

    /**
     * Gets the player's current display name.
     *
     * @return current display name.
     */
    public String getName() {
        return playerInformation.getDisplayname();
    }

    public boolean inArea(@NotNull final Class<? extends RegionArea> clazz) {
        final RegionArea area = GlobalAreaManager.getNullableArea(clazz);
        return area != null && inArea(area.name());
    }

    public boolean inArea(final String areaName) {
        final RegionArea area = areaManager.getArea();
        if (area == null) return false;

        final Location location = getLocation();
        final String name = areaName.toLowerCase();

        RegionArea superArea = area;
        do {
            if (name.equals(superArea.name().toLowerCase())
                    && superArea.inside(location)) {
                return true;
            }
            superArea = superArea.getSuperArea();
        } while (superArea != null);
        return false;
    }

    @Override
    public void resetFreeze() {
        super.resetFreeze();
        PlayerAttributesKt.setFreezeCaster(this, null);
    }

    @Override
    public boolean isFrozen() {
        final boolean frozen = super.isFrozen() && getTemporaryAttributes().get("ignoreWalkingRestrictions") == null;
        if (frozen) {
            final Entity freezeCaster = PlayerAttributesKt.getFreezeCaster(this);
            if (freezeCaster != null) {
                final Location freezeCasterLocation = freezeCaster.getLocation();
                final Location myLocation = getLocation();

                final int distanceX = Math.abs(freezeCasterLocation.getX() - myLocation.getX());
                final int distanceY = Math.abs(freezeCasterLocation.getY() - myLocation.getY());
                if (Math.abs(distanceX) > 10 || Math.abs(distanceY) > 10) {
                    resetFreeze();
                    sendDeveloperMessage("Removing freeze because caster is too far away.");
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void setDefaultSettings() {
        temporaryAttributes.put("fresh_account", 1);
        settings.setSettingNoRefresh(Setting.AUTO_MUSIC, 1);
        setQuestPoints(250);
        attributes.put("LEVEL_99_DIALOGUES", 75);
        attributes.put("RING_OF_RECOIL", 40);
        attributes.put("RING_OF_FORGING", 140);
        attributes.put("checking combat in slayer", 1);
        attributes.put("recoil effect", 1);
        attributes.put("looting_bag_amount_prompt", 1);
        attributes.put("first_99_skill", -1);
        for (final GameSetting setting : GameSetting.ALL) {
            if (setting == GameSetting.YELL_FILTER
                    || setting == GameSetting.ALWAYS_SHOW_LATEST_UPDATE
                    || setting == GameSetting.CONFIRMATION_WHEN_NOTING_OR_UNNOTING
            ) {
                continue;
            }
            attributes.put(setting.toString(), 1);

        }
        attributes.put("quest_points", 250);//To unlock slayer rewards.
        SettingsInterface.setDefaultKeybinds(this);

        bank.toggleSetting(BankSetting.DEPOSIT_INVENTOY_ITEMS, true);
        varManager.sendVar(SettingVariables.PLAYER_ATTACK_OPTIONS_VARP_ID, 2);
        varManager.sendVar(SettingVariables.NPC_ATTACK_OPTIONS_VARP_ID, 2);
        varManager.sendBit(SettingsInterface.COLLECTION_LOG_NEW_ADDITIONS_VARBIT_ID, 3);
        varManager.sendBit(ESC_CLOSES_THE_CURRENT_INTERFACE_VARBIT_ID, 1);

        varManager.sendBit(SHOW_WARNING_WHEN_CASTING_TELEPORT_TO_TARGET_VARBIT_ID, 1);
        varManager.sendBit(SHOW_WARNING_WHEN_CASTING_DAREEYAK_TELEPORT_VARBIT_ID, 1);
        varManager.sendBit(SHOW_WARNING_WHEN_CASTING_CARRALLANGAR_TELEPORT_VARBIT_ID, 1);
        varManager.sendBit(SHOW_WARNING_WHEN_CASTING_ANNAKARL_TELEPORT_VARBIT_ID, 1);
        varManager.sendBit(SHOW_WARNING_WHEN_CASTING_GHORROCK_TELEPORT_VARBIT_ID, 1);
    }

    @Override
    public void processHit(final Hit hit) {
        if (hit.getScheduleTime() < protectionDelay) {
            return;
        }
        if (isImmune(hit.getHitType())) {
            hit.setDamage(0);
        }
        final Action action = actionManager.getAction();
        if (action != null && action.interruptedByCombat()) {
            actionManager.forceStop();
        }
        if (hit.getDamage() > Short.MAX_VALUE) {
            hit.setDamage(Short.MAX_VALUE);
        }
        getUpdateFlags().flag(UpdateFlag.HIT);
        nextHits.add(hit);
        if (hitBars.isEmpty()) {
            hitBars.add(hitBar);
        }
        final HitType type = hit.getHitType();
        if (HitType.DISEASED.equals(type)) {
            return;
        }
        if (HitType.HEALED.equals(type)) {
            heal(hit.getDamage());
        } else if (!HitType.SHIELD_DOWN.equals(type) && !HitType.CORRUPTION.equals(type)) {
            if (hit.getSource() instanceof AbstractTOANPC) {
                toaManager.setDamageTaken(toaManager.getDamageTaken() + Math.min(hitpoints, hit.getDamage()));
            }
            removeHitpoints(hit);
        }
    }

    public void sendAdventurersEntry(final AdventurersLogIcon icon, final String message) {
        sendAdventurersEntry(icon.getLink(), message, false);
    }

    public void sendAdventurersEntry(final String icon, final String message, final boolean pvp) {
//        CoresManager.getServiceProvider().submit(() -> new ApiAdventurersLogRequest(this, icon, message).execute());
    }

    public void refreshDirection() {
        if (faceEntity >= 0) {
            final Entity target = faceEntity >= 65536 ? World.getPlayers().get(faceEntity - 65536) :
                    World.getNPCs().get(faceEntity);
            if (target != null) {
                direction =
                        DirectionUtil.getFaceDirection(target.getLocation().getCoordFaceX(target.getSize()) - getX(),
                                target.getLocation().getCoordFaceY(target.getSize()) - getY());
            }
        }
    }

    private transient int lastWalkX;
    private transient int lastWalkY;

    @Override
    public void processMovement() {
        if(areaManager.getArea() != null)
            areaManager.getArea().move(this);
        refreshDirection();
        this.walkDirection = -1;
        this.runDirection = -1;
        this.crawlDirection = -1;
        final RegionArea area = getArea();
        if (nextLocation != null) {
            final RegionArea nextArea = GlobalAreaManager.getArea(nextLocation);
            if (nextArea instanceof MovementRestrictionPlugin plugin) {
                if (!plugin.canMoveToLocation(this, nextLocation)) {
                    teleported = false;
                    nextLocation = null;
                    return;
                }
            }
            lastStep = null;
            if (lastLocation == null) {
                lastLocation = new Location(location);
            } else {
                lastLocation.setLocation(location);
            }
            unclip();
            LocationMap.remove(this);
            if (area instanceof TeleportMovementPlugin) {
                ((TeleportMovementPlugin) area).processMovement(this, nextLocation);
            }
            forceLocation(nextLocation);
            clip();
            LocationMap.add(this);
            nextLocation = null;
            updateFlags.flag(UpdateFlag.TEMPORARY_MOVEMENT_TYPE);
            teleported = true;
            refreshToleranceRectangle();
            if(getBooleanAttribute("registered") && !getBooleanTemporaryAttribute("gauntlet-started")) {
                if(interfaceHandler.containsInterface(InterfacePosition.CENTRAL) && !interfaceHandler.isWalkablePresent())
                    interfaceHandler.closeInterface(InterfacePosition.CENTRAL);
            }
            World.updateEntityChunk(this, false);
            controllerManager.teleport(location);
            farming.refresh();
            if (interfaceHandler.isVisible(GameInterface.WORLD_MAP.getId())) {
                worldMap.updateLocation();
            }
            if (needMapUpdate()) {
                setNeedRegionUpdate(true);
                setLoadingRegion(true);
            }
            resetWalkSteps();
            return;
        }
        teleported = false;
        if (walkSteps.isEmpty()) {
            return;
        }
        if (isDead()) {
            return;
        }
        if (lastLocation == null) {
            lastLocation = new Location(location);
        } else {
            lastLocation.setLocation(location);
        }
        lastWalkX = 0;
        lastWalkY = 0;
        if(getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL) && !getBooleanTemporaryAttribute("gauntlet-started") && !interfaceHandler.isWalkablePresent()) {
            getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        }
        if (isRun() && !isSilentRun()) {
            if(getBooleanAttribute("registered") && !getBooleanTemporaryAttribute("gauntlet-started")) {
                if(interfaceHandler.containsInterface(InterfacePosition.CENTRAL) && !interfaceHandler.isWalkablePresent())
                    interfaceHandler.closeInterfaces();
            }
            int runStep = walkSteps.size() > 2 ? walkSteps.nthPeek(2) : 0;
            if (actionManager.getAction() instanceof PlayerFollow || runStep != 0 && WalkStep.getDirection(runStep) != -1) {
                double energyLost = ((Math.min(getInventory().getWeight() + getEquipment().getWeight(), 64) / 100) + 0.64);
                if (variables.getTime(TickVariable.STAMINA_ENHANCEMENT) > 0) {
                    energyLost *= 0.3;
                }
                if (variables.getTime(TickVariable.HAMSTRUNG) > 0) {
                    energyLost *= 6;
                }
                final boolean inWilderness = WildernessArea.isWithinWilderness(this);
                /*if (!inWilderness) {
                    if (memberRank.equalToOrGreaterThan(MemberRank.MYTHICAL)) {
                        energyLost *= 0.8F;
                    } else if (memberRank.equalToOrGreaterThan(MemberRank.LEGENDARY)) {
                        energyLost *= 0.85F;
                    } else if (memberRank.equalToOrGreaterThan(MemberRank.EXTREME)) {
                        energyLost *= 0.9F;
                    } else if (memberRank.equalToOrGreaterThan(MemberRank.PREMIUM)) {
                        energyLost *= 0.95F;
                    }
                }*/
                if(variables.getTime(TickVariable.TRACK_STAR_XP_DROP) == 0 && !(actionManager.getAction() instanceof PlayerFollow)) {
                    variables.schedule(10, TickVariable.TRACK_STAR_XP_DROP);
                }
                if (variables.getRunEnergy() >= 0) {
                    variables.setRunEnergy(Math.max(0, variables.getRunEnergy() - energyLost), inWilderness);
                    if (variables.getRunEnergy() == 0) {
                        setRun(false);
                        varManager.sendVar(173, 0);
                    }
                }
            }
        }
        final boolean isCrawling = isCrawling();
        final boolean canMove = !isCrawling || (crawlInterval & 0x1) != 0;
        if (canMove) {
            int steps = Math.min(silentRun ? 1 : run ? 2 : 1, walkSteps.size());
            int stepCount;
            for (stepCount = 0; stepCount < steps; stepCount++) {
                final int nextStep = getNextWalkStep();
                if (nextStep == 0) {
                    break;
                }
                final int dir = WalkStep.getDirection(nextStep);
                if ((WalkStep.check(nextStep) && !World.checkWalkStep(getPlane(), getX(), getY(), dir, getSize(),
                        false, true))) {
                    resetWalkSteps();
                    break;
                }
                final int x = Utils.DIRECTION_DELTA_X[dir];
                final int y = Utils.DIRECTION_DELTA_Y[dir];
                if (area instanceof FullMovementPlugin) {
                    if (!((FullMovementPlugin) area).processMovement(this, getX() + x, getY() + y)) {
                        break;
                    }
                }
                if (isCrawling) {
                    crawlDirection = dir;
                    lastWalkX = -x;
                    lastWalkY = -y;
                } else if (stepCount == 0) {
                    walkDirection = dir;
                    lastWalkX = -x;
                    lastWalkY = -y;
                } else {
                    runDirection = dir;
                }
                if(getBooleanAttribute("registered") && !getBooleanTemporaryAttribute("gauntlet-started")) {
                    if(interfaceHandler.containsInterface(InterfacePosition.CENTRAL) && !interfaceHandler.isWalkablePresent())
                        interfaceHandler.closeInterfaces();
                }
                lastStep = new Location(location);
                controllerManager.move((getWalkSteps().size() > 0 && steps == 2) ? stepCount == 1 : stepCount == 0,
                        getX() + x, getY() + y);
                unclip();
                LocationMap.remove(this);
                location.moveLocation(x, y, 0);
                clip();
                LocationMap.add(this);
                if (interfaceHandler.isVisible(GameInterface.WORLD_MAP.getId())) {
                    worldMap.updateLocation();
                }
            }
            if (area instanceof PartialMovementPlugin) {
                if (!(area instanceof FullMovementPlugin)) {
                    ((PartialMovementPlugin) area).processMovement(this, getX(), getY());
                }
            }
            final int type = crawlDirection != -1 ? 0 : runDirection == -1 ? 1 : 2;
            if (type != lastMovementType) {
                if (stepCount == 1 && run) {
                    updateFlags.flag(UpdateFlag.TEMPORARY_MOVEMENT_TYPE);
                } else {
                    lastMovementType = type;
                    updateFlags.flag(UpdateFlag.MOVEMENT_TYPE);
                }
            }
        }

        if (faceEntity < 0) {
            direction = DirectionUtil.getFaceDirection(location.getX() - lastLocation.getX(),
                    location.getY() - lastLocation.getY());
        }
        refreshToleranceRectangle();
        //TODO check why double.
        World.updateEntityChunk(this, false);
        farming.refresh();
        if (needMapUpdate()) {
            setNeedRegionUpdate(true);
            setLoadingRegion(true);
        }
    }

    @Override
    public void forceLocation(Location location) {
        super.forceLocation(location);
        updatedCachedLocation();
    }

    private void updateCachedLocationIfChanged() {
        if (cachedPlayerLocationHash != location.getPositionHash()) {
            updatedCachedLocation();
        }
    }

    private void updatedCachedLocation() {
        this.cachedPlayerLocationHash = location.getPositionHash();
        this.cachedPlayerX = this.location.getX();
        this.cachedPlayerY = this.location.getY();
        this.cachedPlayerPlane = this.location.getPlane();
        this.cachedPlayerlocation18BitHash = this.location.get18BitHash();
    }

    @Override
    public int getX() {
        updateCachedLocationIfChanged();
        return cachedPlayerX;
    }

    @Override
    public int getY() {
        updateCachedLocationIfChanged();
        return cachedPlayerY;
    }

    @Override
    public int getPlane() {
        updateCachedLocationIfChanged();
        return cachedPlayerPlane;
    }

    public int getLocation18BitHash() {
        updateCachedLocationIfChanged();
        return cachedPlayerlocation18BitHash;
    }

    private transient List<MovementLock> movementLocks = new LinkedList<>();

    private boolean inTolerantPosition(final Location t) {
        for (final Location tile : tolerancePositionQueue) {
            if (tile.withinDistance(t, 10)) {
                return true;
            }
        }
        return false;
    }

    private void refreshToleranceRectangle() {
        if (inTolerantPosition(getLocation())) {
            return;
        }
        if (tolerancePositionQueue.size() >= 2) {
            //Remove the earliest tolerance position.
            tolerancePositionQueue.poll();
        }
        //Add a new position to the tolerance queue.
        tolerancePositionQueue.add(new Location(getLocation()));
        //And every time the player's tolerance position(s) change, we reset the timer again.
        variables.setToleranceTimer(0);
    }

    public boolean canLogout(boolean message) {
        if (!isInitialized()) {
            return false;
        }
        if (isLocked()) {
            if (message) {
                this.sendMessage("You can't log out while performing an action.");
            }
            return false;
        }
        if (isLogoutPrevented()) {
            if (message) {
                this.sendMessage("You can't log out until 10 seconds after the end of combat.");
            }
            return false;
        }
        return true;
    }

    public void logout(final boolean force) {
        if (!force && !canLogout(true)) {
            return;
        }
        setLogoutType(force ? LogoutType.FORCE : LogoutType.REQUESTED);
    }

    public BountyHunter getBountyHunter() {
        return bountyHunter;
    }

    private final BountyHunter bountyHunter = new BountyHunter(this);

    public void sendInputString(final String question, final StringDialogue dialogue) {
        packetDispatcher.sendClientScript(110, question);
        temporaryAttributes.put("interfaceInput", dialogue);
    }

    public void sendInputName(final String question, final NameDialogue dialogue) {
        packetDispatcher.sendClientScript(109, question);
        temporaryAttributes.put("interfaceInput", dialogue);
    }

    public void sendInputInt(final String question, final CountDialogue dialogue) {
        packetDispatcher.sendClientScript(108, question);
        temporaryAttributes.put("interfaceInput", dialogue);
    }

    public void awaitInputString(final StringDialogue dialogue) {
        temporaryAttributes.put("interfaceInput", dialogue);
    }

    public void awaitInputInt(final CountDialogue dialogue) {
        temporaryAttributes.put("interfaceInput", dialogue);
    }

    public void awaitInputIntNoClose(final CountDialogue dialogue) {
        temporaryAttributes.put("interfaceInput", dialogue);
        temporaryAttributes.put("interfaceInputNoCloseOnButton", true);
    }

    public void sendInputItem(final String question, final ItemDialogue dialogue) {
        packetDispatcher.sendClientScript(750, question, 1, -1);
        temporaryAttributes.put("interfaceInput", dialogue);
    }

    public Construction getCurrentHouse() {
        final Object object = getTemporaryAttributes().get("VisitingHouse");
        if (!(object instanceof Construction)) {
            return null;
        }
        return (Construction) object;
    }

    @Override
    public void reset() {
        try {
            try {
                super.reset();
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                for (int i = 0; i < 23; i++) {
                    getSkills().setLevel(i, getSkills().getLevelForXp(i));
                }
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                attackedByPlayers.clear();
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                PluginManager.post(new PlayerResetEvent(this));
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                toxins.reset();
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                attributes.remove("vengeance");
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                variables.resetScheduled();
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                prayerManager.deactivateActivePrayers();
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                variables.setRunEnergy(100);
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                combatDefinitions.setSpecial(false, true);
                combatDefinitions.setSpecialEnergy(100);
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                resetAttackedByDelay();
                setAttackingDelay(0);
            } catch (Exception e) {
                log.error("", e);
            }
            try {
                resetFreeze();
            } catch (Exception e) {
                log.error("", e);
            }
            temporaryAttributes.remove("mark_of_darkness_effect");

        } catch (Exception e) {
            log.error("", e);
        }
    }

    public void setLunarDelay(final long delay) {
        getTemporaryAttributes().put("spellDelay", Utils.currentTimeMillis() + delay);
    }

    @Override
    public List<Entity> getPossibleTargets(final EntityType type, final int radius) {
        if (!possibleTargets.isEmpty()) {
            possibleTargets.clear();
        }
        CharacterLoop.populateEntityList(possibleTargets,
                this.getLocation(), radius, type.getClazz(),
                this::isPotentialTarget);
        return possibleTargets;
    }

    @Override
    public int getPossibleTargetsDefaultRadius() {
        return 15;
    }

    @Override
    public boolean isAcceptableTarget(final Entity entity) {
        return true;
    }

    @Override
    public boolean isPotentialTarget(final Entity entity) {
        final int entityX = entity.getX();
        final int entityY = entity.getY();
        final int entitySize = entity.getSize();
        final int x = getX();
        final int y = getY();
        final int size = getSize();
        return entity != this && !entity.isDead() && !entity.isMaximumTolerance() && (entity.isMultiArea() || entity.getAttackedBy() == this) && (!isProjectileClipped(entity, false) || CollisionUtil.collides(x, y, size, entityX, entityY, entitySize)) && (!(entity instanceof NPC) || ((NPC) entity).isAttackableNPC()) && (!(entity instanceof Player) || ((Player) entity).isCanPvp());
    }

    public long generateSnowflake() {
        return Utils.generateSnowflake(playerInformation.getUserIdentifier());
    }


    public final Number getNumericAttribute(final String key) {
        final Object object = attributes.get(key);
        if (!(object instanceof Number))
            return 0;
        return (Number) object;
    }
    public final Number getNumericAttributeOrDefault(final String key, final int defaultValue) {
        final Object object = attributes.get(key);
        if (object == null || !(object instanceof Number)) {
            return defaultValue;
        }
        return (Number) object;
    }


    /**
     * Adds or subtracts a numeric attribute by specified amount.
     *
     * @param key    the key of the attribute to apply the arithmetic operation to.
     * @param amount the amount to add or subtract from current value of attribute. If no value is found then
     *               operation is applied to 0.
     * @return the new value for the numeric attribute.
     */
    public final Number incrementNumericAttribute(@NotNull final String key, final int amount) {
        final Object object = attributes.get(key);
        if (object != null && !(object instanceof Number)) {
            throw new IllegalArgumentException("Attribute with key [" + key + "] is not numeric.");
        }
        final int newAmount = object == null ? amount : ((Number) object).intValue() + amount;
        attributes.put(key, newAmount);
        return newAmount;
    }


    /**
     * Adds or subtracts a numeric temporary attribute by specified amount.
     *
     * @param key    the key of the attribute to apply the arithmetic operation to.
     * @param amount the amount to add or subtract from current value of attribute. If no value is found then
     *               operation is applied to 0.
     * @return the new value for the numeric attribute.
     */
    public final Number incrementNumericTemporaryAttribute(@NotNull final String key, final int amount) {
        final Object object = temporaryAttributes.get(key);
        if (object != null && !(object instanceof Number)) {
            throw new IllegalArgumentException("Attribute with key [" + key + "] is not numeric.");
        }
        final int newAmount = object == null ? amount : ((Number) object).intValue() + amount;
        temporaryAttributes.put(key, newAmount);
        return newAmount;
    }

    @SuppressWarnings("unchecked")
    public final <T> T getAttributeOrDefault(final String key, final T defaultValue) {
        final Object object = attributes.get(key);
        if (object == null) {
            return defaultValue;
        }
        try {
            return (T) object;
        } catch (final Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public int getIntSetting(final Setting setting) {
        return getNumericAttribute(setting.toString()).intValue();
    }

    public void addAttribute(final String key, final Object value) {
        if (value == null || value instanceof Number && ((Number) value).longValue() == 0) {
            attributes.remove(key);
        } else {
            attributes.put(key, value);
        }
    }

    public void addTemporaryAttribute(final String key, final Object value) {
        if (value instanceof Number && ((Number) value).longValue() == 0) {
            temporaryAttributes.remove(key);
        } else {
            temporaryAttributes.put(key, value);
        }
    }

    public int getTransmogrifiedId(@NotNull final TransmogrifiableType type, final int defaultValue) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final int[] array = type.getTransmogrifiedIds();
        if (array == null) return defaultValue;
        final int varbit = type.getVarbitId();
        final int varp = type.getVarpId();
        final int index = varbit == -1 ? varManager.getValue(varp) : varManager.getBitValue(varbit);
        if (index < 0) return defaultValue;
        if (index >= array.length) {
            return type.defaultId();
        }
        return array[index];
    }

    public int getKillcount(final NPC npc) {
        return this.notificationSettings.getKillcount(npc.getName(this));
    }

    public void toggleBooleanAttribute(final String key) {
        if (key == null) {
            return;
        }
        final int value = getNumericAttribute(key).intValue();
        if (value == 0) {
            addAttribute(key, 1);
            return;
        }
        addAttribute(key, 0);
    }

    public boolean getBooleanAttribute(final String key) {
        if (key == null) {
            return false;
        }
        final int value = getNumericAttribute(key).intValue();
        return value == 1;
    }

    public boolean getBooleanTemporaryAttribute(final String key) {
        if (key == null) {
            return false;
        }
        final int value = getNumericTemporaryAttribute(key).intValue();
        return value == 1;
    }

    public void putBooleanTemporaryAttribute(final String key, final boolean bool) {
        if (key == null) {
            return;
        }
        addTemporaryAttribute(key, bool ? 1 : 0);
    }

    public void putBooleanAttribute(final String key, final boolean bool) {
        if (key == null) {
            return;
        }
        addAttribute(key, bool ? 1 : 0);
    }

    public boolean getBooleanSetting(final Setting key) {
        if (key == null) {
            return false;
        }
        final int value = getNumericAttribute(key.toString()).intValue();
        return value == 1;
    }

    @Override
    public boolean addWalkStep(final int nextX, final int nextY, final int lastX, final int lastY,
                               final boolean check) {
        final int dir = DirectionUtil.getMoveDirection(nextX - lastX, nextY - lastY);
        if (dir == -1) {
            return false;
        }
        if (check && !World.checkWalkStep(getPlane(), lastX, lastY, dir, getSize(), false, true)) {
            return false;
        }
        if (!controllerManager.canMove(dir, nextX, nextY)) {
            return false;
        }
        getWalkSteps().enqueue(WalkStep.getHash(dir, nextX, nextY, check));
        return true;
    }

    public void openShop(final String name) {
        //Different shop across the world, same npc.
        if (name.equals("Trader Stan's Trading Post")) {
            final CharterLocation charterLocation = Utils.getOrDefault(CharterLocation.getLocation(getLocation()),
                    CharterLocation.BRIMHAVEN);
            Shop.get(name + "<" + charterLocation.getShopPrefix() + ">", isIronman(), this).open(this);
            return;
        }
        Shop.get(name, isIronman(), this).open(this);
    }

    public void setFarming(final Farming farming) {
        this.farming = new Farming(this, farming);
    }

    @Override
    public double getMagicPrayerMultiplier() {
        return 0.6;
    }

    @Override
    public double getRangedPrayerMultiplier() {
        return 0.6;
    }

    @Override
    public double getMeleePrayerMultiplier() {
        return 0.6;
    }

    @Override
    public void heal(int amount) {
        final int hitpoints = getHitpoints();
        if (hitpoints >= getMaxHitpoints()) {
            return;
        }
        if (getVariables().getTime(TickVariable.ANCIENT_SMOKE) > 0) {
            sendMessage("*cough* You cannot seem to eat with this smoke in your lungs");
            amount = ((int) (amount * 0.8));
        }
        setHitpoints((hitpoints + amount) >= (getMaxHitpoints()) ? (getMaxHitpoints()) : (hitpoints + amount));
    }

    @Override
    public void unclip() {
        final int size = getSize();
        final int x = getX();
        final int y = getY();
        final int z = getPlane();
        int hash;
        int lastHash = -1;
        Chunk chunk = null;
        for (int x1 = x; x1 < (x + size); x1++) {
            for (int y1 = y; y1 < (y + size); y1++) {
                if ((hash = Chunk.getChunkHash(x1 >> 3, y1 >> 3, z)) != lastHash) {
                    chunk = World.getChunk(lastHash = hash);
                }
                assert chunk != null;
                //if (collides(chunk.getPlayers(), x1, y1) || collides(chunk.getNPCs(), x1, y1)) continue;
                World.getRegion(_Location.getRegionId(x1, y1), true).removeFlag(z, x1 & 63, y1 & 63,
                        Flags.OCCUPIED_BLOCK_NPC);
            }
        }
    }

    @Override
    public void clip() {
        if (isFinished()) {
            return;
        }
        final int size = getSize();
        final int x = getX();
        final int y = getY();
        final int z = getPlane();
        for (int x1 = x; x1 < (x + size); x1++) {
            for (int y1 = y; y1 < (y + size); y1++) {
                World.getRegion(_Location.getRegionId(x1, y1), true).addFlag(z, x1 & 63, y1 & 63,
                        Flags.OCCUPIED_BLOCK_NPC);
            }
        }
    }

    @Override
    public void processEntity() {
        try {
            /* This step is actually done in a normal queue in OSRS, but since the system doesn't exist here... */
            checkGraniteMaulAutoAggression();
        } catch (Exception e) {
            log.error("", e);
        }
        try {
            final RouteEvent<?, ?> event = routeEvent;
            if (event != null) {
                if (event.process()) {
                    if (routeEvent == event) {
                        routeEvent = null;
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        try {
            if (!postPacketProcessingRunnables.isEmpty()) {
                postPacketProcessingRunnables.forEach(runnable -> {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        log.error("", e);
                    }
                });
                postPacketProcessingRunnables.clear();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        try {
            try {
                actionManager.process();
            } catch (final Exception e) {
                log.error("", e);
            }
            variables.process();
            if (getCape() != null) {
                AvasDevice.collectMetal(this);
            }
            try {
                controllerManager.process();
            } catch (final Exception e) {
                log.error("", e);
            }
            try {
                gravestone.process();
            } catch (final Exception e) {
                log.error("", e);
            }
            cutsceneManager.process();
            music.processMusicPlayer();
            if (getAttackedByDelay() > WorldThread.getCurrentCycle() && getAttackedBy() != null || actionManager.getAction() instanceof PlayerCombat && getAttacking() != null) {
                chargesManager.removeCharges(DegradeType.TICKS);
            }
            farming.processAll();
            hunter.process();
            prayerManager.process();
            var acidPool = World.getObjectWithId(this.location, ACID_POOL_54148);
            if (acidPool != null) {
                applyHit(new Hit(4, HitType.VENOM));
                toxins.applyToxin(Toxins.ToxinType.VENOM, 4);
            }
            if (araneaBoots.isPlayerWearingBoots())
                araneaBoots.processBootTick();
            var attribute = getTemporaryAttributes().get("araxxor_acid_drip");
            if (attribute != null) {
                var drip = Integer.parseInt(attribute.toString());
                if (drip > 0) {
                    ((AraxxorInstance) mapInstance).spawnAcidPool(location);
                    getTemporaryAttributes().put("araxxor_acid_drip", drip - 1);
                }
                if (drip == 0)
                    temporaryAttributes.remove("araxxor_acid_drip");
            }
            final double energy = variables.getRunEnergy();
            boolean inWildy = WildernessArea.isWithinWilderness(this);
            if (energy < 100 && getRunDirection() == -1) {
                float restore = ((8.0F + (getSkills().getLevel(SkillConstants.AGILITY) / 6.0F)) / 0.6F / 100.0F) * 0.6F;
                double boost = 1;
                if (EquipmentUtils.containsFullGraceful(this)) {
                    boost += 0.3F;
                }
                if (perkManager.ifValidConsume(PerkWrapper.ATHLETIC_RUNNER) && !inWildy) {
                    boost += 0.25F;
                }
                if (getSkillingXPRate() == 10) {
                    boost += 0.02F;
                } else if (getSkillingXPRate() == 5) {
                    boost += 0.03F;
                }
                variables.setRunEnergy(energy + (restore * boost), inWildy);
            }
        } catch (final Exception e) {
            log.error("", e);
        }
        super.processEntity();
        try {
            actionManager.processAfterMovement();
        } catch (final Exception e) {
            log.error("", e);
        }
        appendNearbyNPCs();
        if (damageSound != -1) {
            sendSound(new SoundEffect(damageSound));
            damageSound = -1;
        }
        checkFacedEntity();
        if (teleported && updateFlags.get(UpdateFlag.FORCE_MOVEMENT)) {
            updateFlags.set(UpdateFlag.TEMPORARY_MOVEMENT_TYPE, true);
        }
    }

    public LootkeySettings getLootkeySettings() {
        return lootkeySettings;
    }

    public void setLootkeySettings(LootkeySettings lootkeySettings) {
        this.lootkeySettings = lootkeySettings;
    }

    /**
     * @return the fractional bonus appended to a 100% drop rate (i.e. 0.10D for a 10% boost)
     */
    public double getDropRateBonus() {
        ExpConfiguration configuration = ExpConfigurations.getForPlayer(this);
        double gameMode = configuration.getDropRateIncrease() / 100.0D;
        double donor = getMemberRank().getDR();
        double pin = getBooleanAttribute("drop_rate_pin_claimed") ? 0.05D : 0.0D;
        double pet = getBoonManager().hasBoon(AnimalTamer.class) && getPetId() != -1 ? 0.02D : 0.0D;
        double compCape = getCompletionistCapeDRBoost();
        return ((gameMode + donor + pin + pet + compCape) * 100.0D);
    }

    @NotNull
    public double getExchangeBonus() {
        if(this.isIronman())
            return 1.10;
        return 1.0;
    }

    public boolean tryAddInventoryThenBank(Item item) {
        if(getInventory().hasSpaceFor(item)) {
            getInventory().addOrDrop(item);
            return true;
        }
        else if(bank != null && gameMode != null && bank.getContainer().getFreeSlotsSize() > 0 && gameMode != GameMode.ULTIMATE_IRON_MAN) {
            bank.add(item);
            return true;
        }
        return false;
    }

    public boolean isInRaid() {
        RegionArea currentArea = this.getArea();
        if(currentArea == null)
            return false;
        return currentArea.isRaidArea();
    }

    @Override
    public int getMagicLevel() {
        return getSkills().getLevel(SkillConstants.MAGIC);
    }

    public boolean isGroupIronman() {
        return this.gameMode.isGroupIronman();
    }

    public double getCompletionistCapeDRBoost() {
        Item cape = getCape();
        if(cape == null)
            return 0.0D;
        int tier = CompletionistCape.isCompletionistCape(getCape().getId());
        return switch (tier) {
            case 1 -> 0.01D;
            case 2 -> 0.02D;
            case 3 -> 0.03D;
            default -> 0.0D;
        };
    }

    public boolean hasBoon(Class<? extends Boon> lookupBoon) {
        return boonManager != null && boonManager.hasBoon(lookupBoon);
    }

    private boolean torvaHPBoosted = false;

    public void setTorvaBoosted(boolean b) {
        torvaHPBoosted = b;
        getSkills().refresh(Skills.HITPOINTS);
    }

    public boolean isTorvaHpBoosted() {
        return torvaHPBoosted;
    }

    public void setRunePouch(RunePouch runePouch) {
        this.runePouch = runePouch;
    }

    public void setSecondaryRunePouch(RunePouch secondaryRunePouch) {
        this.secondaryRunePouch = secondaryRunePouch;
    }

    public void setSkills(Skills skills) {
        this.skills = skills;
    }

    public boolean hasSpawnRights() {
        return privilege.inherits(PlayerPrivilege.DEVELOPER);
    }

    public enum FollowStage {
        STARTED,
        FOLLOWING,
    }

    private FollowStage followStage = null;

    public void setFollowStage(FollowStage followStage) {
        this.followStage = followStage;
    }

    public FollowStage getFollowStage() {
        return followStage;
    }

    public Location getFollowTile() {
        return followTile;
    }

    public void setFollowTile(Location followTile) {
        this.followTile = followTile;
    }

    public void updateFollowTile() {
        if (lastStep != null) followTile = isTeleported() ? null : lastStep;
    }

    public void processFollowing() {
        if (this.followStage != FollowStage.STARTED) return;
        this.followStage = FollowStage.FOLLOWING;
        // this.processMovement();
        if(lastStep != followTile) {
            this.interfaceHandler.closeInterfaces(true);
        }
        if (lastStep != null) followTile = isTeleported() ? null : lastStep;
    }

    public void postProcess() {
        try {
            delayedActionManager.process();
            processTempState();
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    @Override
    public void appendHitEntry(final HitEntry entry) {
        if (!entry.isFreshEntry()) {
            return;
        }
        entry.setFreshEntry(false);
        final Entity source = entry.getSource();
        if (source != null) {
            final Hit hit = entry.getHit();
            final HitType type = hit.getHitType();
            final float multiplierAddition = getArea() instanceof final AbstractTOARaidArea area && area.isQuietPrayers() ? .1F : 0;
            if (type == HitType.MELEE) {
                if (prayerManager.isActive(Prayer.PROTECT_FROM_MELEE)) {
                    hit.setDamage((int) Math.ceil(hit.getDamage() * Math.min(1, source.getMeleePrayerMultiplier() + multiplierAddition)));
                }
            } else if (type == HitType.RANGED) {
                if (prayerManager.isActive(Prayer.PROTECT_FROM_MISSILES)) {
                    hit.setDamage((int) Math.ceil(hit.getDamage() * Math.min(1, source.getRangedPrayerMultiplier() + multiplierAddition)));
                }
            } else if (type == HitType.MAGIC) {
                if (prayerManager.isActive(Prayer.PROTECT_FROM_MAGIC)) {
                    hit.setDamage((int) Math.ceil(hit.getDamage() * Math.min(1, source.getMagicPrayerMultiplier() + multiplierAddition)));
                }
            }
        }
    }

    private void appendNearbyNPCs() {
        CharacterLoop.forEach(getLocation(), 25, NPC.class, npc -> {
            if (npc.getTargetType() != EntityType.PLAYER || npc.isDead()) return;
            NPC.pendingAggressionCheckNPCs.add(npc.getIndex());
        });
    }

    public void finish() {
        if (isFinished()) {
            return;
        }
        try {
            if (log.isInfoEnabled())
                log.info("'" + getName() + "' has logged out.");
            GameLogger.log(Level.INFO, () -> new GameLogMessage.Logout(
                    Instant.Companion.now(),
                    getUsername(),
                    getIP()
            ));
            SpellbookSwap.checkSpellbook(this);
            final Object loc = getTemporaryAttributes().get("oculusStart");
            if (loc instanceof Location) {
                setLocation((Location) loc);
            }
            controllerManager.logout();
            variables.cancel(TickVariable.TELEBLOCK);
            variables.cancel(TickVariable.TELEBLOCK_IMMUNITY);
            final RegionArea area = getArea();
            if (area instanceof LogoutPlugin) {
                ((LogoutPlugin) area).onLogout(this);
            }
            construction.getTipJar().onLogout();
            setFinished(true);
            World.updateEntityChunk(this, true);
            LocationMap.remove(this);
            getInterfaceHandler().closeInterface(GameInterface.TOURNAMENT_SPECTATING);
            GlobalAreaManager.update(this, false, true);
            if (getTemporaryAttributes().get("cameraShake") != null) {
                packetDispatcher.resetCamera();
            }
            if (follower != null) {
                follower.finish();
            }
            ClanManager.leave(this, false);
            socialManager.updateStatus();
            interfaceHandler.closeInterfaces();
            MethodicPluginHandler.invokePlugins(ListenerType.LOGOUT, this);
            PluginManager.post(new LogoutEvent(this));
            MiddleManManager.INSTANCE.onLogout(this);
            CoresManager.getServiceProvider().submit(logger::shutdown);
            /*appender.getManager().flush();
            appender.stop();
            LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
            loggerConfig.removeAppender(appender.getName());
            ctx.updateLoggers();*/
            try {
                final Session session = getSession();
                session.flush();
            } catch(Exception ex) {
                log.warn("Failed to flush session as session was not initialized. (likely nulled player)");
            }
            Analytics.logLogout(this);

            postFinish();
        } catch (final Exception e) {
            log.error("", e);
        }
    }

    private void postFinish() {
        temporaryAttributes.clear();
        pendingContainers.clear();
        attackedByPlayers.clear();
        pendingVars.clear();
        zoneFollowPackets.clear();
        tempList.clear();
        chunksInScope.clear();
        receivedHits.clear();
        nextHits.clear();
        hitBars.clear();
        npcViewport.reset();
        playerViewport.reset();
        chatMessage.close();
        appearance.close();

        getBankPin().loggedOut(this);

        final Session session = getSession();
        if (session != null) {
            try {
                session.close();
            } catch (final Throwable t) {
                if (log.isErrorEnabled()) {
                    log.error("Failed to close \"" + getUsername() + "\"'s session", t);
                }
            }
        }

        postSaveFunction = this::postSave;
    }

    private transient Runnable postSaveFunction;
    private transient boolean nulled;

    private void postSave() {
        try {
            if (isNulled()) {
                return;
            }
            setNulled(true);
            unlink();
            final Field[] fields = getClass().getDeclaredFields();
            for (final Field field : fields) {
                final int modifier = field.getModifiers();
                if (Modifier.isStatic(modifier) || field.getType().isPrimitive()) {
                    continue;
                }
                field.setAccessible(true);
                try {
                    field.set(this, null);
                } catch (IllegalAccessException e) {
                    log.error("", e);
                }
            }
            this.postSaveFunction = null;
        } catch (Exception e) {
            log.error("", e);
        }
    }


    public void processEntityUpdate() {
        if (!pendingContainers.isEmpty()) {
            if (pendingContainers.contains(getInventory().getContainer()) || pendingContainers.contains(getEquipment().getContainer())) {
                packetDispatcher.sendWeight();
            }
            for (final Container container : pendingContainers) {
                if (container.isFullUpdate() || container.getModifiedSlots().size() >= (container.getContainerSize() * 0.67F)) {
                    packetDispatcher.sendUpdateItemContainer(container);
                } else {
                    packetDispatcher.sendUpdateItemsPartial(container);
                }
            }
            pendingContainers.clear();
        }
        getSkills().sendQueuedFakeExperienceDrops();
        final boolean regionUpdate = isNeedRegionUpdate();
        if (regionUpdate) {
            loadMapRegions(false);
        }
        send(playerViewport.cache());
        if (!pendingVars.isEmpty()) {
            IntIterator it = pendingVars.intIterator();
            while (it.hasNext()) {
                int var = it.nextInt();
                packetDispatcher.sendConfig(var, varManager.getValue(var));
                it.remove();
            }
        }
        if (teleported) {
            if (lastLocation != null && getPlane() != lastLocation.getPlane()) {
                zoneFollowPackets.clear();
            }
            updateScopeInScene();
        }
        if (!tempList.isEmpty()) {
            for (final Player.ProjPacket proj : tempList) {
                sendZoneUpdate(proj.sender.getX(), proj.sender.getY(), proj.packet);
            }
            tempList.clear();
        }
        if (!zoneFollowPackets.isEmpty()) {
            for (Int2ObjectMap.Entry<List<GamePacketEncoder>> entry : zoneFollowPackets.int2ObjectEntrySet()) {
                final int key = entry.getIntKey();
                final java.util.List<GamePacketEncoder> packets = entry.getValue();
//                if (packets.size() == 1) {
//                    send(new UpdateZonePartialFollows((key & 2047) << 3, (key >> 11 & 2047) << 3, this));
//                    send(packets.get(0));
//                } else {
                    final UpdateZonePartialEnclosed zonePacket = new UpdateZonePartialEnclosed((key & 2047) << 3,
                            (key >> 11 & 2047) << 3, this);
                    for (int i = packets.size() - 1; i >= 0; i--) {
                        zonePacket.append(packets.get(i));
                    }
                    send(zonePacket);
                //}
            }
            zoneFollowPackets.clear();
        }
        send(npcViewport.cache());
        if (regionUpdate) {
            setNeedRegionUpdate(false);
        }
        //getSession().flush();
    }

    private void processTempState() {
//        System.out.println("player.processTempState("+getUsername()+", "+getArea()+", "+fakeEquipmentState+")");
        TempPlayerStatePlugin.getIfStateSwitched(this, fakeEquipmentState, equipmentTemp, equipment).ifPresent(pair -> {
//            System.out.println("player.processTempState("+getUsername()+", "+fakeEquipmentState+") -> "+pair);
            pendingContainers.remove(equipment.getContainer());
            pendingContainers.remove(equipmentTemp.getContainer());
            fakeEquipmentState = pair.getFirst();
            pair.getSecond().refreshAll();
            getCombatDefinitions().refresh();
            appearance.resetRenderAnimation();
            bonuses.update();
        });
        TempPlayerStatePlugin.getIfStateSwitched(this, fakeInventoryState, inventoryTemp, inventory).ifPresent(pair -> {
//            System.out.println("player.processTempState("+getUsername()+", "+fakeInventoryState+") -> "+pair);
            pendingContainers.remove(inventory.getContainer());
            pendingContainers.remove(inventoryTemp.getContainer());
            fakeInventoryState = pair.getFirst();
            pair.getSecond().refreshAll();
        });
        TempPlayerStatePlugin.getIfStateSwitched(this, fakeRunePouchState, runePouchTemp, runePouch).ifPresent(pair -> {
            pendingContainers.remove(runePouch.getContainer());
            pendingContainers.remove(runePouchTemp.getContainer());
            fakeRunePouchState = pair.getFirst();
            pair.getSecond().getContainer().refresh(this);
        });
        TempPlayerStatePlugin.getIfStateSwitched(this, fakeSecondaryRunePouchState, secondaryRunePouchTemp, secondaryRunePouch).ifPresent(pair -> {
            pendingContainers.remove(secondaryRunePouch.getContainer());
            pendingContainers.remove(secondaryRunePouchTemp.getContainer());
            fakeSecondaryRunePouchState = pair.getFirst();
            pair.getSecond().getContainer().refresh(this);
        });
        TempPlayerStatePlugin.getIfStateSwitched(this, fakeSkillsState, skillsTemp, getSkills()).ifPresent(pair -> {
            fakeSkillsState = pair.getFirst();
            pair.getSecond().refresh();
        });
        final CombatDefinitions combatDefinitions = getCombatDefinitions();
        TempPlayerStatePlugin.getIfStateSwitched(
                this, combatDefinitions.getFakeSpellbookState(),
                combatDefinitions.getTempSpellbook(),
                combatDefinitions.getSpellbook()
        ).ifPresent(pair -> {
            combatDefinitions.setFakeSpellbookState(pair.getFirst());
            pair.getSecond().refresh(this);
        });
    }

    public void sendZoneUpdate(final int tileX, final int tileY, final GamePacketEncoder packet) {
        final int chunkX = tileX >> 3;
        final int chunkY = tileY >> 3;
        final int hash = chunkX | chunkY << 11;
        java.util.List<GamePacketEncoder> list = zoneFollowPackets.get(hash);
        if (list == null) {
            list = new ArrayList<>();
            zoneFollowPackets.put(hash, list);
        }
        list.add(packet);
    }

    /**
     * TODO: Temporary; testing to see if this fixes the issue!
     */
    public void addProj(final Location sender, final GamePacketEncoder packet) {
        tempList.add(new ProjPacket(sender, packet));
    }

    public void sendSound(final int id) {
        if (id <= -1) {
            return;
        }
        this.packetDispatcher.sendSoundEffect(SoundEffect.get(id));
    }

    public void sendSound(final SoundEffect sound) {
        this.packetDispatcher.sendSoundEffect(sound);
    }

    @Override
    public void setAnimation(final Animation animation) {
        if (appearance.getRenderAnimation() != null && appearance.getRenderAnimation().getWalk() == 772) {
            return;
        }
        this.animation = animation;
        if (animation == null) {
            updateFlags.set(UpdateFlag.ANIMATION, false);
            lastAnimation = 0;
        } else {
            if (!AnimationMap.isValidAnimation(appearance.getNpcId(), animation.getId())) {
                return;
            }
            updateFlags.flag(UpdateFlag.ANIMATION);
            final AnimationDefinitions defs = AnimationDefinitions.get(animation.getId());
            if (defs != null) {
                lastAnimation = Utils.currentTimeMillis() + defs.getDuration();
            } else {
                lastAnimation = Utils.currentTimeMillis();
            }
        }
    }

    @Override
    public void setInvalidAnimation(final Animation animation) {
        this.animation = animation;
        if (animation == null) {
            updateFlags.set(UpdateFlag.ANIMATION, false);
            lastAnimation = 0;
        } else {
            updateFlags.flag(UpdateFlag.ANIMATION);
            final AnimationDefinitions defs = AnimationDefinitions.get(animation.getId());
            if (defs != null) {
                lastAnimation = Utils.currentTimeMillis() + defs.getDuration();
            } else {
                lastAnimation = Utils.currentTimeMillis();
            }
        }
    }

    public int getExperienceRate(final int skill) {
        return Skills.isCombatSkill(skill) ? getCombatXPRate() : getSkillingXPRate();
    }

    public int getSkillingXPRate() {
        return Math.max(1, getNumericAttribute("skilling_xp_rate").intValue());
    }

    public int getCombatXPRate() {
        return Math.max(1, getNumericAttribute("combat_xp_rate").intValue());
    }

    public void setExperienceMultiplier(final ExpConfiguration configuration) {
        setExperienceMultiplier(configuration.getCombatModifier(), configuration.getSkillingExpModifier());
    }

    public void setExperienceMultiplier(final int combat, final int skilling) {
        addAttribute("skilling_xp_rate", Math.max(1, skilling));
        addAttribute("combat_xp_rate", Math.max(1, combat));
        if (getNumericAttribute("Xp Drops Multiplied").intValue() == 1) {
            if (getNumericAttribute("Xp Drops Wildy Only").intValue() == 0 || WildernessArea.isWithinWilderness(getX(), getY())) {
                getVarManager().sendVar(3504, 1);
            }
        }
        final Optional<Interface> optionalPlugin = GameInterface.GAME_NOTICEBOARD.getPlugin();
        if (optionalPlugin.isPresent()) {
            final Interface plugin = optionalPlugin.get();
            packetDispatcher.sendComponentText(plugin.getInterface(), plugin.getComponent("XP rate"), "XP: " +
                    "<col=ffffff>" + getCombatXPRate() + "x Combat & " + getSkillingXPRate() + "x Skilling</col>");
        }
    }

    public boolean isFloorItemDisplayed(final FloorItem item) {
        if (getNumericAttribute(GameSetting.HIDE_ITEMS_YOU_CANT_PICK.toString()).intValue() == 0)
            return true;
        if (!isIronman() || item.isVisibleToIronmen())
            return true;
        return !item.hasOwner() || item.isOwner(this);
    }

    public boolean isXPDropsMultiplied() {
        return getNumericAttribute("Xp Drops Multiplied").intValue() == 1;
    }

    public boolean isXPDropsWildyOnly() {
        return getNumericAttribute("Xp Drops Wildy Only").intValue() == 1;
    }

    public void setXpDropsMultiplied(final boolean value) {
        addAttribute("Xp Drops Multiplied", value ? 1 : 0);
    }

    public void setXPDropsWildyOnly(final boolean value) {
        addAttribute("Xp Drops Wildy Only", value ? 1 : 0);
    }

    @Override
    public void setUnprioritizedAnimation(final Animation animation) {
        if (lastAnimation > Utils.currentTimeMillis() || updateFlags.get(UpdateFlag.ANIMATION)) {
            return;
        }
        if (appearance.getRenderAnimation() != null && appearance.getRenderAnimation().getWalk() == 772) {
            return;
        }
        if (animation != null && !AnimationMap.isValidAnimation(appearance.getNpcId(), animation.getId())) {
            return;
        }
        this.animation = animation;
        updateFlags.set(UpdateFlag.ANIMATION, animation != null);
    }

    public void forceAnimation(final Animation animation) {
        this.animation = animation;
        if (animation == null) {
            updateFlags.set(UpdateFlag.ANIMATION, false);
            lastAnimation = 0;
        } else {
            updateFlags.flag(UpdateFlag.ANIMATION);
            final AnimationDefinitions defs = AnimationDefinitions.get(animation.getId());
            if (defs != null) {
                lastAnimation = Utils.currentTimeMillis() + defs.getDuration();
            } else {
                lastAnimation = Utils.currentTimeMillis();
            }
        }
    }

    public boolean send(final GamePacketEncoder encoder) {
        try {
            final Session session = getSession();
            if (session == null) return false;
            return session.send(encoder);
        } catch (final Throwable e) {
            log.error("", e);
            return false;
        } finally {
            try {
                encoder.logChecked(this);
            } catch (final Throwable e) {
                log.warn("Failed to log checked", e);
            }
        }
    }

    public void init(final Player player) {
        run = player.run;
        gameMode = player.gameMode;
        memberRank = Utils.getOrDefault(player.memberRank, MemberRank.NONE);
        privilege = player.privilege;
        respawnPoint = player.respawnPoint;
        if (player.paydirt != null) {
            paydirt.addAll(player.paydirt);
        }
        if (player.trackedHolidayItems != null) {
            trackedHolidayItems.addAll(player.trackedHolidayItems);
        }
        areaManager.setLastDynamicAreaName(player.getAreaManager().getLastDynamicAreaName());
        areaManager.setOnEnterLocation(player.getAreaManager().getOnEnterLocation());
    }

    @Override
    public void loadMapRegions(final boolean init) {
        super.loadMapRegions(init);
        this.setNeedRegionUpdate(false);

        if (init) return;

        if (isInitialized() && isAtDynamicRegion()) {
            packetDispatcher.sendDynamicMapRegion();
        } else {
            packetDispatcher.sendStaticMapRegion();
        }
        afterLoadMapRegions();
    }

    public void afterLoadMapRegions() {
        final Location tile = getLastLoadedMapRegionTile();
        final int swx = ((tile.getChunkX() - 6) << 3) + 1;
        final int swy = ((tile.getChunkY() - 6) << 3) + 1;
        this.sceneRectangle = World.getRectangle(swx, swx + 102, swy, swy + 102);
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public int getSize() {
        if(appearance == null)
            return 1;
        try {
            final int npcId = appearance.getNpcId();
            if (npcId != -1) {
                return NPCDefinitions.get(npcId).getSize();
            }
        } catch (final Exception e) {
            log.error("", e);
        }
        return 1;
    }

    @Override
    public int getHitpoints() {
        return isNulled() ? 0 : getSkills().getLevel(SkillConstants.HITPOINTS);
    }

    @Override
    public boolean setHitpoints(final int hitpoints) {
        final boolean dead = isDead();
        getSkills().setLevel(SkillConstants.HITPOINTS, hitpoints);
        this.hitpoints = hitpoints;
        if (!dead && this.hitpoints <= 0) {
            sendDeath();
            return true;
        }
        return false;
    }

    private transient Location lastAttackPosition = null;
    private transient Location lastAttackTargetPosition = null;

    public int getLastOutgoingAttackCycle() {
        return lastOutgoingAttackCycle;
    }

    private transient int lastOutgoingAttackCycle = 0;
    private transient WeakReference<Entity> targetReference = null;

    public void outgoingHit(Entity target) {
        if (wieldingGraniteMaul()) {
            return;
        }
        this.targetReference = new WeakReference<>(target);
        this.lastAttackPosition = new Location(location);
        this.lastAttackTargetPosition = new Location(target.getLocation());
        this.lastOutgoingAttackCycle = (int) WorldThread.getCurrentCycle();
    }

    public void checkGraniteMaulAutoAggression() {
        if (!inRangeForGraniteMaulAutoAggression()) {
            return;
        }
        WeakReference<Entity> targetReference = this.targetReference;
        if (targetReference == null) {
            return;
        }
        final Entity target = targetReference.get();
        if (target == null || !wieldingGraniteMaul() || !combatDefinitions.isUsingSpecial()) {
            return;
        }
        PlayerCombat.attackEntity(this, target, null);
    }

    public boolean wieldingGraniteMaul() {
        final int weaponId = getEquipment().getId(EquipmentSlot.WEAPON);
        return weaponId == ItemId.GRANITE_MAUL || weaponId == ItemId.GRANITE_MAUL_12848 ||
                weaponId == ItemId.GRANITE_MAUL_20557 || weaponId == ItemId.GRANITE_MAUL_24225 ||
                weaponId == ItemId.GRANITE_MAUL_24227;
    }

    private boolean inRangeForGraniteMaulAutoAggression() {
        int lastCycle = this.lastOutgoingAttackCycle;
        if (lastCycle < WorldThread.getCurrentCycle() - 8) {
            return false;
        }
        Location lastAttackPosition = this.lastAttackPosition;
        if (lastAttackPosition == null || !lastAttackPosition.withinDistance(location, 1)) {
            return false;
        }
        Location lastAttackTargetPosition = this.lastAttackTargetPosition;
        WeakReference<Entity> targetReference = this.targetReference;
        if (lastAttackTargetPosition == null || targetReference == null) {
            return false;
        }
        final Entity target = targetReference.get();
        if (target == null) {
            return false;
        }
        return target.getLocation().withinDistance(lastAttackTargetPosition, 1);
    }

    @Override
    public void unlink() {
    }

    @Override
    public int getMaxHitpoints() {
        return torvaHPBoosted ? getSkills().getLevelForXp(SkillConstants.HITPOINTS) + 21 : getSkills().getLevelForXp(SkillConstants.HITPOINTS);
    }

    @Override
    public int getClientIndex() {
        return getIndex() + 32768;
    }

    @Override
    public boolean isDead() {
        return getHitpoints() == 0;
    }

    @Override
    public void cancelCombat() {
        if (actionManager.getAction() instanceof PlayerCombat) {
            actionManager.forceStop();
        }
    }

    public boolean inCombatWithPlayer() {
        if(actionManager.getAction() instanceof PlayerCombat combat)
            return combat.getTarget() instanceof Player;
        else return false;
    }

    public void setFollower(final Follower follower) {
        if (this.follower != null && follower == null) {
            this.follower.finish();
            petId = -1;
            this.follower = null;
            return;
        }
        this.follower = follower;
        petId = follower == null ? -1 : follower.getId();
        if (follower != null) {
            follower.spawn();
        }
        varManager.sendVar(447, follower == null ? -1 : follower.getIndex());
    }

    public void stopAll() {
        this.stopAll(true);
    }

    public void stopAll(final boolean stopWalk) {
        this.stopAll(stopWalk, true);
    }

    public void stopAll(final boolean stopWalk, final boolean stopInterface) {
        this.stopAll(stopWalk, stopInterface, true);
    }

    public void useStairs(final int emoteId, final Location dest, final int useDelay, final int totalDelay) {
        this.useStairs(emoteId, dest, useDelay, totalDelay, null);
    }

    public void useStairs(final int emoteId, final Location dest, final int useDelay, final int totalDelay,
                          final String message) {
        this.useStairs(emoteId, dest, useDelay, totalDelay, message, false);
    }

    public void useStairs(final int emoteId, final Location dest, final int useDelay, final int totalDelay,
                          final String message, final boolean resetAnimation) {
        this.stopAll();
        this.lock(totalDelay);
        if (emoteId != -1) {
            setAnimation(new Animation(emoteId));
        }
        if (useDelay == 0) {
            teleport(dest);
        } else {
            WorldTasksManager.schedule(() -> {
                if (isDead()) {
                    return;
                }
                if (resetAnimation) {
                    setAnimation(Animation.STOP);
                }
                teleport(dest);
                if (message != null) {
                    sendMessage(message);
                }
            }, useDelay - 1);
        }
    }

    public void stopAll(final boolean stopWalk, final boolean stopInterfaces, final boolean stopActions) {
        setRouteEvent(null);
        if (getFaceEntity() >= 0) {
            setFaceEntity(null);
        }
        varManager.sendBit(5983, 0);
        if (getTemporaryAttributes().get("CreatingRoom") != null) {
            construction.roomPreview((RoomReference) getTemporaryAttributes().get("CreatingRoom"), true);
            getTemporaryAttributes().remove("CreatingRoom");
        }
        interfaceHandler.closeInput();
        if (stopInterfaces) {
            getTemporaryAttributes().remove("skillDialogue");
            interfaceHandler.closeInterfaces();
            if (worldMap.isVisible() && worldMap.isFullScreen()) {
                worldMap.close();
            }
        }
        if (stopWalk) {
            getPacketDispatcher().resetMapFlag();
            resetWalkSteps();
        }
        if (stopActions) {
            actionManager.forceStop();
            delayedActionManager.forceStop();
        }
    }

    @Override
    public void resetMasks() {
        if (updateFlags.isUpdateRequired()) {
            updateFlags.reset();
        }
        if (!hitBars.isEmpty()) {
            hitBars.clear();
        }
        if (!nextHits.isEmpty()) {
            nextHits.clear();
        }
        this.updateNPCOptions = false;
        RSBuffer buffer = appearance.getBuffer();
        if (buffer != null) {
            buffer.clear();
        }
        if (isCrawling()) {
            this.crawlInterval++;
        }
        this.walkDirection = -1;
        this.runDirection = -1;
        this.crawlDirection = -1;
    }

    @Override
    public void resetWalkSteps() {
        super.resetWalkSteps();
        pathfindingEvent = null;
    }

    public void stop(final StopType... types) {
        for (final Player.StopType type : types) {
            type.consumer.accept(this);
        }
    }

    public void stopAllExclWorldMap() {
        setRouteEvent(null);
        if (varManager.getBitValue(5983) != 0) {
            varManager.sendBit(5983, 0);
        }
        if (getTemporaryAttributes().get("CreatingRoom") != null) {
            construction.roomPreview((RoomReference) getTemporaryAttributes().get("CreatingRoom"), true);
            getTemporaryAttributes().remove("CreatingRoom");
        }
        getTemporaryAttributes().remove("skillDialogue");
        interfaceHandler.closeInterfaces();
        resetWalkSteps();
        actionManager.forceStop();
        delayedActionManager.forceStop();
        setAnimation(Animation.STOP);
    }

    @Override
    public void applyHit(final Hit hit) {
        if (isNulled() || immune) {
            return;
        }
        super.applyHit(hit);
        interfaceHandler.closeInterfaces(true);
        if (worldMap.isVisible() && worldMap.isFullScreen()) {
            worldMap.close();
        }
    }

    private final void reflectDamage(final Hit hit) {
        final Entity source = hit.getSource();
        final int damage = hit.getDamage();
        if (damage <= 0)
            return;

        if(source == null)
            return;

        boolean recoiled = false;
        if(getBoonManager().hasBoon(ImRubberYoureGlue.class) && hit.getHitType() != HitType.VENOM && hit.getHitType() != HitType.POISON && hit.getHitType() != HitType.TYPELESS && !(hit.getSource() instanceof Player)) {
            recoiled = true;
            final int reflected = (int) Math.floor(damage / 10.0F) + 1;
            WorldTasksManager.schedule(() -> source.applyHit(new Hit(this, reflected, HitType.REGULAR)));
        }
        if (hit.getHitType() == HitType.REGULAR) {
            return;
        }
        final int amuletId = getEquipment().getId(EquipmentSlot.AMULET);

        if ((amuletId == 12851 || amuletId == 12853) && Utils.random(3) == 0 && CombatUtilities.hasFullBarrowsSet(this, "Dharok's")) {
            WorldTasksManager.schedule(() -> source.applyHit(new Hit(this, (int) (damage * 0.15F), HitType.REGULAR)));
        }
        final boolean hasVengeance = getAttributes().remove("vengeance") != null;
        if (hasVengeance && (hit.getHitType() != HitType.POISON && hit.getHitType() != HitType.VENOM)) {
            setForceTalk(VENGEANCE);
            float multiplier = (getBoonManager().hasBoon(LunarEnthusiast.class) && !(hit.getSource() instanceof Player)) ? 1.50F : 0.75F;
            final Hit vengHit = new Hit(this, Math.max(1, (int) Math.floor(damage * multiplier)), HitType.REGULAR);
            vengHit.putAttribute("vengeance_spell", Boolean.TRUE);
            source.applyHit(vengHit);
            if (hit.containsAttribute("sire_explosion")) {
                getCombatAchievements().complete(CAType.DEMONIC_REBOUND);
            }
            if (source instanceof NPC npc && (npc.getId() == NpcId.ZULRAH || npc.getId() == NpcId.ZULRAH_2043 || npc.getId() == NpcId.ZULRAH_2044) && source.isDead()) {
                getCombatAchievements().complete(CAType.SNAKE_REBOUND);
            }
        }
        final int ring = getEquipment().getId(EquipmentSlot.RING);
        if (!recoiled && ring == 2550 || ((ring == 19710 || ring == 20655 || ring == 20657))) {
            if (ring == 2550 || getBooleanAttribute("recoil effect")) {
                final Item ringItem = getRing();
                int charges = ring == 2550 ? getNumericAttribute("RING_OF_RECOIL").intValue() : ringItem.getCharges();
                if (ring == 2550 && charges == 0) {
                    charges = 40;
                }
                final int reflected = Math.min((int) Math.floor(damage / 10.0F) + 1, charges);
                chargesManager.removeCharges(ringItem, reflected, getEquipment().getContainer(),
                        EquipmentSlot.RING.getSlot());
                WorldTasksManager.schedule(() -> source.applyHit(new Hit(this, reflected, HitType.REGULAR)));
            }
        }
    }

    private void applySmite(final Hit hit) {
        final Entity source = hit.getSource();
        if (!(source instanceof Player)) {
            return;
        }
        final int damage = Math.min(hit.getDamage(), getHitpoints());
        if (((Player) source).getPrayerManager() != null && ((Player)source).getPrayerManager().isActive(Prayer.SMITE)) {
            final int drain = damage / 4;
            if (drain > 0 && prayerManager != null) {
                prayerManager.drainPrayerPoints(drain);
            }
        }
    }

    private void applyDamageReducers(final Hit hit, final Entity source) {
        int damage = hit.getDamage();
        final int weaponId = getEquipment().getId(EquipmentSlot.WEAPON);
        final int shieldId = getEquipment().getId(EquipmentSlot.SHIELD);
        final HitType type = hit.getHitType();
        if (source.getEntityType() == EntityType.NPC) {
            if (weaponId == 21015) {
                final long delay = getNumericTemporaryAttribute("dinhsbulwarkdelay").longValue();
                if (Utils.currentTimeMillis() > delay) {
                    damage = (int) (damage * 0.8F);
                }
            }
            if (CombatUtilities.hasFullJusticiarSet(this) && type != HitType.DEFAULT) {
                final int bonus = bonuses.getBonus(type == HitType.MELEE ? 7 : type == HitType.MAGIC ? 8 : 9);
                if (bonus > 0) {
                    final double percentage = bonus / 3000.0;
                    final int reduced = (int) (damage * percentage);
                    damage -= reduced;
                }
            }
        }
        if (type != HitType.DEFAULT && CombatUtilities.isElysianSpiritShield(shieldId)) {
            if (Utils.randomDouble() < 0.7F) {
                final int reduced = (int) (damage * 0.25F);
                setGraphics(ELYSIAN_EFFECT_GFX);
                damage -= reduced;
            }
        }
        if (hit.getDamage() != damage) {
            hit.setDamage(Math.max(0, damage));
        }
    }

    private static final int[] maleDamageSounds = new int[]{518, 519, 521};
    private static final int[] femaleDamageSounds = new int[]{509, 510};

    @Override
    public void postProcessHit(final Hit hit) {
        applySmite(hit);
        reflectDamage(hit);
        if (!HitType.HEALED.equals(hit.getHitType()) && !HitType.CORRUPTION.equals(hit.getHitType())) {
            if (damageSound == -1 || damageSound == 511) {
                if (hit.getDamage() > 0) {
                    final int[] array = appearance.isMale() ? maleDamageSounds : femaleDamageSounds;
                    damageSound = array[Utils.random(array.length - 1)];
                } else {
                    damageSound = 511;
                }
            }
        }
    }

    @Override
    public void handleIngoingHit(final Hit hit) {
        final Entity source = hit.getSource();
        if (source == null) {
            return;
        }
        if (hit.getDamage() > 0) {
            chargesManager.removeCharges(DegradeType.INCOMING_HIT);
        }
        final HitType type = hit.getHitType();
        if (!ArrayUtils.contains(PROCESSED_HIT_TYPES, type)) {
            return;
        }
        applyDamageReducers(hit, source);
    }

    public final void sendDeveloperMessage(final String message) {
        if (WORLD_PROFILE.isBeta() || WORLD_PROFILE.isDevelopment() || hasPrivilege(PlayerPrivilege.DEVELOPER))
            sendMessage(Colour.RS_PINK.wrap("[DEV]") + " " + message);
    }

    public final void sendMessage(final String message) {
        packetDispatcher.sendGameMessage(message, false);
    }

    public final void sendFilteredMessage(final String message) {
        packetDispatcher.sendGameMessage(message, true);
    }

    public final void sendMessage(final String message, final MessageType type) {
        packetDispatcher.sendGameMessage(message, type);
    }

    public final void sendMessage(final String message, final MessageType type, final String extension) {
        packetDispatcher.sendMessage(message, type, extension);
    }

    private Queue<Location> tolerancePositionQueue = new LinkedList<>();

    /*private transient LoggerConfig loggerConfig;
    private transient FileAppender appender;
    @Getter
    private transient Logger logger;*/
    public final void createLogger() {
        /*val name = getUsername();
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        Layout<? extends Serializable> layout = PatternLayout.newBuilder().withAlwaysWriteExceptions(true)
        .withPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} " + "[%thread] %-5level - %msg%n").withConfiguration(config).build();

        appender =
                FileAppender.newBuilder().withFileName("data/logs/player/" + name + ".log").withName("File")
                .withImmediateFlush(false).withBufferedIo(true).withBufferSize(4096).withLayout(layout)
                .setConfiguration(config).build();

        appender.start();
        config.addAppender(appender);
        AppenderRef ref = AppenderRef.createAppenderRef("File", null, null);
        AppenderRef[] refs = new AppenderRef[] { ref };
        loggerConfig = LoggerConfig.createLogger(false, Level.INFO, "org.apache.logging.log4j", "true", refs, null,
        config, null);
        loggerConfig.addAppender(appender, null, null);
        config.addLogger("Player logger: " + name, loggerConfig);
        ctx.updateLoggers();
        this.logger = ctx.getLogger("Player logger: " + name);*/
        try {
            logger = new PlayerLogger(this);
        } catch (final Exception e) {
            if (log.isErrorEnabled())
                log.error("Failed to create logger for \"" + getUsername() + "\"", e);
        }
    }

    public void log(final LogLevel level, final String info) {
        if (logger != null)
            logger.log(level, info);
    }

    public void errorlog(final String info) {
        log(LogLevel.ERROR, info);
    }

    @Override
    public void checkMultiArea() {
        AreaType lastAreaType = getAreaType();
        AreaType areaType = isForceMultiArea()
                ? AreaTypes.MULTIWAY
                : World.checkAreaType(getLocation());
        if (!areaType.equals(lastAreaType)) {
            setAreaType(areaType);
            WorldTasksManager.schedule(() -> {
                varManager.sendBit(4605, AreaTypes.MULTIWAY.equals(areaType));
                varManager.sendBit(5961, AreaTypes.SINGLES_PLUS.equals(areaType));
            });
        }
    }

    @Override
    public boolean canAttackSingles(final Entity target, final boolean message) {
        if (target.isForceAttackable()) return true;
        if (target.isMultiArea()) return true;

        final AreaType areaType = target.getAreaType();
        if (AreaTypes.MULTIWAY.equals(areaType)) return true;


        final Entity attacker = getAttackedBy();
        if (attacker == target) return true;
        if (attacker != null) {
            if (!(attacker instanceof NPC && (attacker.isDead() || attacker.isFinished()))) {
                if (!(AreaTypes.SINGLES_PLUS.equals(areaType) && attacker instanceof NPC)) {
                    if (isUnderCombat(0)) {
                        if (message) sendMessage("You are already in combat.");
                        return false;
                    }
                }
            }
        }

        final Entity attacker_ = target.getAttackedBy();
        if (attacker_ == this) return true;
        if (attacker_ != null) {
            if (!(AreaTypes.SINGLES_PLUS.equals(areaType) && attacker_ instanceof NPC)) {
                if (target.isUnderCombat(0)) {
                    if (message) sendMessage("That "
                            + (target instanceof Player ? "player" : "npc")
                            + " is already in combat.");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Multi icon is updated in synchronization with the client.
     */
    @Override
    public void removeHitpoints(final Hit hit) {
        if (isDead()) {
            return;
        }
        final int hitpoints = getHitpoints();
        int damage = hit.getDamage();
        if (damage > hitpoints) {
            damage = hitpoints;
        }
        addReceivedDamage(hit.getSource(), damage, hit.getHitType());
        final boolean dead = setHitpoints(hitpoints - damage);
        if (dead)
            PlayerAttributesKt.setKillingBlowHit(this, hit);
        if (!isDead()) {
            if (getHitpoints() < getMaxHitpoints() * 0.1F && prayerManager.isActive(Prayer.REDEMPTION)) {
                prayerManager.applyRedemptionEffect();
            }
            if (getHitpoints() < getMaxHitpoints() * 0.2F) {
                if (getEquipment().getId(EquipmentSlot.AMULET) == 21157) {
                    getEquipment().set(EquipmentSlot.AMULET, null);
                    prayerManager.restorePrayerPoints((int) (getSkills().getLevelForXp(SkillConstants.PRAYER) * 0.1F));
                    sendFilteredMessage("Your necklace of faith degrades to dust.");
                }
            }
            final Item necklace = this.getAmulet();
            if (necklace != null && necklace.getId() == 11090 && getHitpoints() < (getMaxHitpoints() * 0.2F) && getDuel() == null) {
                this.heal((int) (this.getMaxHitpoints() * 0.3F));
                sendMessage("Your phoenix necklace heals you, but is destroyed in the process.");
                getEquipment().set(EquipmentSlot.AMULET, null);
                getEquipment().refresh();
            }
            if (!HitType.HEALED.equals(hit.getHitType()) && hit.getSource() != null && !hit.getSource().equals(this)
                    && getArea() instanceof final AbstractTOARaidArea toaRaidArea && toaRaidArea.isDeadlyPrayers()) {
                prayerManager.drainPrayerPoints(damage / 5);
            }
            if (getHitpoints() <= (getMaxHitpoints() * 0.1F)) {
                final int ring = getEquipment().getId(EquipmentSlot.RING);
                final RegionArea area = getArea();
                if (area instanceof DeathPlugin && !((DeathPlugin) area).isRingOfLifeEffective()) {
                    return;
                }
                if (!(ring == 2570 || (SkillcapePerk.DEFENCE.isEffective(this) && getBooleanAttribute("Skillcape ring" +
                        " of life teleport")))) {
                    return;
                }
                if (variables.getTime(TickVariable.TELEBLOCK) > 0) {
                    return;
                }
                final OptionalInt level = WildernessArea.getWildernessLevel(getLocation());
                if (level.isPresent() && level.getAsInt() > 30) {
                    return;
                }
                stopAll();
                if (SkillcapePerk.DEFENCE.isEffective(this) && getBooleanAttribute("Skillcape ring of life teleport")) {
                    sendMessage("Your cape saves you.");
                } else {
                    getEquipment().set(EquipmentSlot.RING, null);
                    sendMessage("Your Ring of Life saves you and is destroyed in the process.");
                    updateFlags.flag(UpdateFlag.APPEARANCE);
                }
                final Teleport teleport = new Teleport() {
                    @Override
                    public TeleportType getType() {
                        return TeleportType.REGULAR_TELEPORT;
                    }

                    @Override
                    public Location getDestination() {
                        return respawnPoint.getLocation();
                    }

                    @Override
                    public int getLevel() {
                        return 0;
                    }

                    @Override
                    public double getExperience() {
                        return 0;
                    }

                    @Override
                    public int getRandomizationDistance() {
                        return 0;
                    }

                    @Override
                    public Item[] getRunes() {
                        return null;
                    }

                    @Override
                    public int getWildernessLevel() {
                        return 30;
                    }

                    @Override
                    public boolean isCombatRestricted() {
                        return false;
                    }
                };
                teleport.teleport(this);
            }
        }
    }

    private transient PlayerLogger logger;

    @Override
    public void sendDeath() {
        final Player source = getMostDamagePlayer();
        if (!controllerManager.sendDeath(source) || areaManager.sendDeath(this, source)) {
            return;
        }
        if (animation != null) {
            animation = null;
            updateFlags.set(UpdateFlag.ANIMATION, false);
        }
        lock();
        stopAll();
        if (prayerManager.isActive(Prayer.RETRIBUTION)) {
            prayerManager.applyRetributionEffect(source);
        }

        if (source != null) {
            DeathChargeKt.invokeDeathChargeEffect(source);
        }

        if (WildyExtKt.isBountyPaired(this)) {
            WildyExtKt.processBountyDeath(this);
            BountyHunterController.completeBounty(source, this);
        }

        WorldTasksManager.schedule(new WorldTask() {
            int ticks;

            @Override
            public void run() {
                if (isFinished() || isNulled()) {
                    stop();
                    return;
                }
                if (ticks == 1) {
                    performDeathAnimation();
                } else if (ticks == 4) {

                    if (privilege.inherits(PlayerPrivilege.ADMINISTRATOR) && !DeveloperCommands.INSTANCE.getAdminsLoseItemsOnDeath())
                        sendMessage(Colour.RED.wrap("You do not lose items upon death as you're a " + privilege.getPrettyName()));
                    else if(privilege.is(PlayerPrivilege.FORUM_MODERATOR))
                        sendMessage("You do not lose items on death cause ya test stuff");
                    else
                        deathMechanics.death(source, getArea() instanceof GravestoneLocationPlugin ? ((GravestoneLocationPlugin) getArea()).getGravestoneLocation() : null);

                    if (source != null) {
                        final int index = Utils.random(deathMessages.length - 1);
                        String message = deathMessages[index];
                        if (index >= deathMessages.length - 2) {
                            if (index == deathMessages.length - 1) {
                                message = message.replace("%gender%", getAppearance().isMale() ? "him" : "her");
                            } else {
                                message = message.replace("%gender%", getAppearance().isMale() ? "he" : "she");
                            }
                        }
                        source.sendMessage(String.format(message, getName()));
                    }
                    sendMessage("Oh dear, you have died.");
                    getMusic().playJingle(90);
                    reset();
                    blockIncomingHits(2);
                    setAnimation(Animation.STOP);
                    variables.setSkull(false);
                    final RegionArea area = getArea();
                    final DeathPlugin plugin = area instanceof DeathPlugin ? (DeathPlugin) area : null;
                    final Location respawnLocation = plugin == null ? null : plugin.getRespawnLocation();
                    setLocation(respawnLocation != null ? respawnLocation : respawnPoint.getLocation());
                } else if (ticks == 5) {
                    unlock();
                    setAnimation(Animation.STOP);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PLAYER;
    }

    public int getQuestPoints() {
        return getNumericAttribute("quest_points").intValue();
    }

    public void setQuestPoints(final int amount) {
        addAttribute("quest_points", amount);
    }

    public void refreshQuestPoints() {
        getVarManager().sendVar(101, getQuestPoints());
    }

    public boolean isVisibleInViewport(final Position position) {
        if (position instanceof Player otherPlayer) {
            // We optimise the checks for players, this is relevant in the PlayerInfo packet construction,
            // reducing overhead by roughly 10% during high load.
            if (otherPlayer.getPlane() != getPlane())
                return false;
            if (!sceneRectangle.contains(otherPlayer.getX(), otherPlayer.getY()))
                return false;
            final int distance = getViewDistance();
            final int deltaX = otherPlayer.getX() - getX();
            final int deltaY = otherPlayer.getY() - getY();
            return deltaX <= distance && deltaX >= -distance && deltaY <= distance && deltaY >= -distance;
        }
        return isVisibleInScene(position) && location.withinDistance(position, getViewDistance());
    }

    public boolean isVisibleInScene(final Position position) {
        final Location pos = position.getPosition();
        return sceneRectangle.contains(pos.getX(), pos.getY());
    }

    void refreshScopedGroundItems(final boolean add) {
        SceneSynchronization.refreshScopedGroundItems(this, add);
    }

    public void updateScopeInScene() {
        SceneSynchronization.updateScopeInScene(this);
    }

    @Override
    public void setLocation(final Location tile) {
        if (tile == null) {
            return;
        }
        nextLocation = new Location(tile);
    }

    /**
     * Equipment getters - a better form of this should be made (kotlins extension / proxy functions would be nice)
     */
    public Item getHelmet() {
        return getEquipment().getItem(0);
    }

    public Item getCape() {
        return getEquipment().getItem(1);
    }

    public Item getAmulet() {
        return getEquipment().getItem(2);
    }

    public Item getWeapon() {
        return getEquipment().getItem(3);
    }

    public Item getChest() {
        return getEquipment().getItem(4);
    }

    public Item getShield() {
        return getEquipment().getItem(5);
    }

    public Item getLegs() {
        return getEquipment().getItem(7);
    }

    public Item getGloves() {
        return getEquipment().getItem(9);
    }

    public Item getBoots() {
        return getEquipment().getItem(10);
    }

    public Item getRing() {
        return getEquipment().getItem(12);
    }

    public Item getAmmo() {
        return getEquipment().getItem(13);
    }

    @Override
    public Location getMiddleLocation() {
        if (middleTile == null) {
            middleTile = new Location(getLocation());
        } else {
            middleTile.setLocation(getLocation());
        }
        return middleTile;
    }

    private static final Animation candyCaneBlockAnimation = new Animation(15086);
    private static final Animation easterCarrotBlockAnimation = new Animation(15162);

    private Animation getDefenceAnimation() {
        final int weaponId = getEquipment().getId(EquipmentSlot.WEAPON);
        if (weaponId == 21015) {
            return BULWARK_ANIM;
        }
        if (weaponId == ChristmasConstants.CANDY_CANE) {
            return candyCaneBlockAnimation;
        }
        if (weaponId == EasterConstants.EasterItem.EASTER_CARROT.getItemId()) {
            return easterCarrotBlockAnimation;
        }
        if (weaponId == 4084) {
            return new Animation(1466);
        }

        final int shieldId = getEquipment().getId(EquipmentSlot.SHIELD);
        final ItemDefinitions shieldDefinitions = ItemDefinitions.get(shieldId);
        if (shieldId != -1) {
            if ((shieldId >= 8844 && shieldId <= 8850) || shieldId == 12954 || shieldId == 19722 || shieldId == 22322 || shieldId == ItemId.RUNE_DEFENDER_T) {
                return new Animation(4177);
            }
            if (shieldDefinitions != null && shieldDefinitions.getName().toLowerCase().contains("book")) {
                return new Animation(420);
            }
            return new Animation(1156);
        }

        final ItemDefinitions weaponDefinitions = ItemDefinitions.get(weaponId);
        if (weaponDefinitions == null) {
            return PLAIN_DEFENCE_ANIM;
        }

        final int blockAnimation = weaponDefinitions.getBlockAnimation();
        if (!AnimationMap.isValidAnimation(appearance.getNpcId(), blockAnimation)) {
            return PLAIN_DEFENCE_ANIM;
        }
        return new Animation(blockAnimation);
    }

    public void setCanPvp(final boolean canPvp) {
        setCanPvp(canPvp, false);
    }

    public void setCanPvp(final boolean canPvp, final boolean duel) {
        if (this.canPvp == canPvp && this.flagDuel == duel) {
            return;
        }
        this.canPvp = canPvp;
        this.flagDuel = duel;
        this.setPlayerAttackable(this.canPvp);
        this.setPlayerChallengeable(!canPvp && this.flagDuel);
    }

    @Override
    public void performDefenceAnimation(Entity attacker) {
        if (getWeapon() != null && getWeapon().getId() == 21015) {
            setGraphics(BULWARK_GFX);
        }
        setUnprioritizedAnimation(getDefenceAnimation());
    }

    @Override
    public void performDeathAnimation() {
        setAnimation(DEATH_ANIMATION);
    }

    @Override
    public int drainSkill(final int skill, final double percentage) {
        if (skill == SkillConstants.PRAYER) {
            return prayerManager.drainPrayerPoints(percentage, 0);
        }
        return getSkills().drainSkill(skill, percentage, 0);
    }

    @Override
    public int drainSkill(final int skill, final double percentage, final int minimumDrain) {
        if (skill == SkillConstants.PRAYER) {
            return prayerManager.drainPrayerPoints(percentage, minimumDrain);
        }
        return getSkills().drainSkill(skill, percentage, minimumDrain);
    }

    @Override
    public int drainSkill(final int skill, final int amount) {
        if (skill == SkillConstants.PRAYER) {
            return prayerManager.drainPrayerPoints(amount);
        }
        return getSkills().drainSkill(skill, amount);
    }

    @Override
    public boolean canAttack(final Player source) {
        return true;
    }

    @Override
    public void autoRetaliate(final Entity source) {
        if (source == this) {
            return;
        }
        if (!combatDefinitions.isAutoRetaliate() || !source.triggersAutoRetaliate() || actionManager.hasSkillWorking() || hasWalkSteps() || isLocked()) {
            return;
        }
        PlayerCombat.attackEntity(this, source, null);
    }

    /**
     * Gets the players current username.
     *
     * @return current username.
     */
    @Override
    public String getUsername() {
        return playerInformation.getUsername();
    }

    /**
     * Returns the player's current username.
     */
    @Override
    public String toString() {
        return playerInformation.getUsername();
    }

    @Override
    public void handleOutgoingHit(final Entity target, final Hit hit) {
        if (target == null || hit == null) {
            return;
        }
        if (target.getHitpoints() - hit.getDamage() <= 0) {
            if (target instanceof NPC) {
                handleNpcKill((NPC) target, hit);
            } else {
                handlePlayerKill((Player) target, hit);
            }
        }
        //controllerManager.processOutgoingHit(target, hit); UNUSED
    }

    private void handleNpcKill(final NPC target, final Hit hit) {
        if (getNumericAttribute("demon_kills").intValue() < 100 && CombatUtilities.isDemon(target)) {
            final int weapon = getEquipment().getId(EquipmentSlot.WEAPON);
            if (weapon != 2402) {
                //silverlight
                return;
            }
            if (!hit.getHitType().equals(HitType.MELEE)) {
                return;
            }
            addAttribute("demon_kills", getNumericAttribute("demon_kills").intValue() + 1);
            final int kills = getNumericAttribute("demon_kills").intValue();
            if (kills % 25 == 0 && kills < 100) {
                final int remaining = 100 - kills;
                sendMessage("You've reached a demon kill checkpoint! You need to kill " + remaining + " more demon" + (kills == 1 ? "" : "s") + " to upgrade your Silverlight.");
            } else if (kills == 100) {
                getEquipment().set(EquipmentSlot.WEAPON, new Item(6746));
                getUpdateFlags().flag(UpdateFlag.APPEARANCE);
                sendMessage("You've reached 100 demon kills, your Silverlight has been upgraded into a Darklight!");
            }
        }
    }

    private void handlePlayerKill(final Player target, final Hit hit) {
    }

    public void refreshTitles() {
        //setNametag(0,
        //       (!privilege.equals(Privilege.PLAYER) ? privilege.getCrown() + " " : "") + (!gameMode.equals
        //       (GameMode.REGULAR) ? gameMode.getCrown() + GameMode.getTitle(this) + " " : ""));
    }

    public void setPrivilege(final PlayerPrivilege privilege, final boolean updateComponentText) {
        this.privilege = privilege;

        if(!isLoggedIn)
            return;

        if (!updateComponentText) return;

        final Optional<Interface> optionalPlugin = GameInterface.GAME_NOTICEBOARD.getPlugin();
        if (optionalPlugin.isPresent()) {
            final Interface plugin = optionalPlugin.get();
            packetDispatcher.sendComponentText(plugin.getInterface(), plugin.getComponent("Privilege"), "Privilege: " +
                    "<col=ffffff>" + privilege.crown().getCrownTag() + privilege.getPrettyName() + "</col>");
        }
    }

    public void setPrivilege(final PlayerPrivilege privilege) {
        setPrivilege(privilege, true);
    }

    public transient boolean immune = false;
    public transient boolean pendingRaidBypass = false;

    public boolean hasPrivilege(PlayerPrivilege privilege) {
        if(this.privilege == null)
            return false;
        return this.privilege.inherits(privilege);
    }

    public Optional<Raid> getRaid() {
        if (isNulled()) {
            return Optional.empty();
        }
        final ClanChannel channel = settings.getChannel();
        if (channel == null) {
            return Optional.empty();
        }
        final RaidParty party = channel.getRaidParty();
        if (party == null) {
            return Optional.empty();
        }
        final Raid raid = party.getRaid();
        if (raid == null) {
            return Optional.empty();
        }
        if (!raid.getPlayers().contains(this)) {
            return Optional.empty();
        }
        return Optional.of(raid);
    }
    public void setGameMode(final GameMode mode) {
        setGameMode(mode, false);
    }

    public void setGameMode(final GameMode mode, boolean autoload) {
        if (gameMode != GameMode.REGULAR && mode == GameMode.REGULAR) {
            if (getNumericAttribute(GameSetting.HIDE_ITEMS_YOU_CANT_PICK.toString()).intValue() == 1) {
                GameSetting.HIDE_ITEMS_YOU_CANT_PICK.handleSetting(this);
            }
        }
        gameMode = mode;
        if(autoload && !isLoggedIn)
            return;
        varManager.sendBit(1777, gameMode.ordinal());
        updateFlags.flag(UpdateFlag.APPEARANCE);
        ChatChannelPlayerExtKt.sendSocialTabs(this);
        final Optional<Interface> optionalPlugin = GameInterface.GAME_NOTICEBOARD.getPlugin();
        if (optionalPlugin.isPresent()) {
            final Interface plugin = optionalPlugin.get();
            packetDispatcher.sendComponentText(plugin.getInterface(), plugin.getComponent("Game Mode"), "Mode: " +
                    "<col=ffffff>" + getGameModeCrown().getCrownTag() + gameMode + "</col>");
        }
    }

    public void setMemberRank(final MemberRank rank) {
        setMemberRank(rank, false);
    }

    public void setMemberRank(final MemberRank rank, boolean autoLoad) {
        memberRank = rank;
        if(!isLoggedIn)
            return;
        final Optional<Interface> optionalPlugin = GameInterface.GAME_NOTICEBOARD.getPlugin();
        if (optionalPlugin.isPresent()) {
            final Interface plugin = optionalPlugin.get();
            packetDispatcher.sendComponentText(plugin.getInterface(), plugin.getComponent("Member Rank"), "Member: " +
                    "<col=ffffff>" + getMemberCrown().getCrownTag() + getMemberName().replace(" Member", "") + "</col>");
        }
        varManager.sendBit(16000, memberRank.equalToOrGreaterThan(MemberRank.PREMIUM) ? 1 : 0);
        getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

    public boolean isIronman() {
        return gameMode.isIronman();
    }

    public boolean containsItem(final int id) {
        return containsItem(new Item(id));
    }

    public boolean containsAnyItem(final Item... items) {
        for (final Item item : items) {
            return containsItem(item);
        }
        return false;
    }

    public boolean containsAnyItem(final int... ids) {
        for (final int id : ids) {
            return containsItem(id);
        }
        return false;
    }

    public boolean containsAny(final int... ids) {
        boolean contains = false;
        for (final int id : ids) {
            if (containsItem(id)) {
                contains = true;
            }
        }
        return contains;
    }

    public boolean containsItem(final Item item) {
        for (final Item i : getInventory().getContainer().getItems().values()) {
            if (i.getId() == item.getId()) {
                return true;
            }
        }
        for (final Item i : getEquipment().getContainer().getItems().values()) {
            if (i.getId() == item.getId()) {
                return true;
            }
        }
        for (final Item i : bank.getContainer().getItems().values()) {
            if (i.getId() == item.getId()) {
                return true;
            }
        }
        for (final Item i : retrievalService.getContainer().getItems().values()) {
            if (i.getId() == item.getId()) {
                return true;
            }
        }
        for (final Item i : getRunePouch().getContainer().getItems().values()) {
            if (i != null && i.getId() == item.getId()) {
                return true;
            }
        }
        for (final Item i : privateStorage.getContainer().getItems().values()) {
            if (i != null && i.getId() == item.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean carryingAny(final Collection<Integer> ids) {
        for (final Integer id : ids) {
            if (carryingItem(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean carryingAny(final int... ids) {
        for (final int id : ids) {
            if (carryingItem(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean carryingItem(final int id) {
        return carryingItem(new Item(id));
    }

    public boolean carryingItem(final Item item) {
        for (final Item i : getInventory().getContainer().getItems().values()) {
            if (i.getId() == item.getId()) {
                return true;
            }
        }
        for (final Item i : getEquipment().getContainer().getItems().values()) {
            if (i.getId() == item.getId()) {
                return true;
            }
        }
        return false;
    }

    public int getAmountOf(final int id) {
        int count = 0;
        for (final Item i : getInventory().getContainer().getItems().values()) {
            if (i.getId() == id) {
                count += i.getAmount();
            }
        }
        for (final Item i : getEquipment().getContainer().getItems().values()) {
            if (i.getId() == id) {
                count += i.getAmount();
            }
        }
        for (final Item i : bank.getContainer().getItems().values()) {
            if (i.getId() == id) {
                count += i.getAmount();
            }
        }
        for (final Item i : retrievalService.getContainer().getItems().values()) {
            if (i.getId() == id) {
                count += i.getAmount();
            }
        }
        for (final Item i : getRunePouch().getContainer().getItems().values()) {
            if (i != null && i.getId() == id) {
                count += i.getAmount();
            }
        }
        for (final Item i : privateStorage.getContainer().getItems().values()) {
            if (i != null && i.getId() == id) {
                count += i.getAmount();
            }
        }
        return count;
    }

    public long getAmountOfLong(final int id) {
        long count = 0L;
        for (final Item i : getInventory().getContainer().getItems().values()) {
            if (i.getId() == id) {
                count += i.getAmount();
            }
        }
        for (final Item i : getEquipment().getContainer().getItems().values()) {
            if (i.getId() == id) {
                count += i.getAmount();
            }
        }
        for (final Item i : bank.getContainer().getItems().values()) {
            if (i.getId() == id) {
                count += i.getAmount();
            }
        }
        for (final Item i : retrievalService.getContainer().getItems().values()) {
            if (i.getId() == id) {
                count += i.getAmount();
            }
        }
        for (final Item i : getRunePouch().getContainer().getItems().values()) {
            if (i != null && i.getId() == id) {
                count += i.getAmount();
            }
        }
        for (final Item i : privateStorage.getContainer().getItems().values()) {
            if (i != null && i.getId() == id) {
                count += i.getAmount();
            }
        }
        return count;
    }

    public long getTotalWealth() {
        long count = 0L;
        for (final Item i : getInventory().getContainer().getItems().values()) {
            if (i == null) continue;
            count += i.getAmount() * i.getSellPrice();
        }
        for (final Item i : getEquipment().getContainer().getItems().values()) {
            if (i == null) continue;
            count += i.getAmount() * i.getSellPrice();
        }
        for (final Item i : bank.getContainer().getItems().values()) {
            if (i == null) continue;
            count += i.getAmount() * i.getSellPrice();
        }
        for (final Item i : retrievalService.getContainer().getItems().values()) {
            if (i == null) continue;
            count += i.getAmount() * i.getSellPrice();
        }
        for (final Item i : getRunePouch().getContainer().getItems().values()) {
            if (i == null) continue;
            count += i.getAmount() * i.getSellPrice();
        }
        for (final Item i : privateStorage.getContainer().getItems().values()) {
            if (i == null) continue;
            count += i.getAmount() * i.getSellPrice();
        }
        return count;
    }

    public void removeItem(final Item item) {
        removeItem(item, false);
    }

    public boolean removeItem(final Item item, boolean once) {
        final ContainerWrapper[] wrappers = new ContainerWrapper[]{getInventory(), getEquipment()};
        for (final ContainerWrapper wrapper : wrappers) {
            for (int slot = 0; slot < wrapper.getContainer().getSize(); slot++) {
                final Item i = wrapper.getItem(slot);
                if (i == null || i.getId() != item.getId()) {
                    continue;
                }
                final RequestResult result = wrapper.deleteItem(i).getResult();

                if (wrapper instanceof Equipment) {
                    getUpdateFlags().flag(UpdateFlag.APPEARANCE);
                }
                if (once && RequestResult.SUCCESS.equals(result)) {
                    return true;
                }
            }
        }
        for (int slot = 0; slot < bank.getContainer().getSize(); slot++) {
            final Item i = bank.get(slot);
            if (i == null || i.getId() != item.getId()) {
                continue;
            }
            final RequestResult result = bank.remove(i).getResult();
            if (once && RequestResult.SUCCESS.equals(result)) {
                return true;
            }
        }
        return false;
    }

    public boolean addWalkSteps(final Direction direction, final int distance, final int maxStepsCount,
                                final boolean check) {
        final Location dest = getLocation().transform(direction, distance);
        return addWalkSteps(dest.getX(), dest.getY(), maxStepsCount, check);
    }

    @Override
    public boolean addWalkSteps(final int destX, final int destY, final int maxStepsCount, final boolean check) {
        final int[] lastTile = getLastWalkTile();
        int myX = lastTile[0];
        int myY = lastTile[1];
        int stepCount = 0;
        while (true) {
            stepCount++;
            if (myX < destX) {
                myX++;
            } else if (myX > destX) {
                myX--;
            }
            if (myY < destY) {
                myY++;
            } else if (myY > destY) {
                myY--;
            }
            if (!addWalkStep(myX, myY, lastTile[0], lastTile[1], check)) {
                return false;
            }
            if (stepCount == maxStepsCount) {
                return true;
            }
            lastTile[0] = myX;
            lastTile[1] = myY;
            if (lastTile[0] == destX && lastTile[1] == destY) {
                return true;
            }
        }
    }

    @Override
    public int getCombatLevel() {
        return getSkills().getCombatLevel();
    }

    public void sendPlayerOptions() {
        setPlayerFollowable(true);
        setPlayerTradeable(true);
    }


    private transient boolean flagDuel = false;

    public void clearTopLevelRowPlayerOptions() {
        setPlayerOption(0, "null", false);
    }

    public void setIronGroupInvitable(boolean invitable) {
        if(invitable) {
            setPlayerOption(1, "Invite", false);
        } else {
            clearPlayerOptionRow(1, "Invite");
        }
    }

    public void setIronGroupApplyable(boolean applyable) {
        if(applyable) {
            setPlayerOption(1, "Apply", false);
        } else {
            clearPlayerOptionRow(1, "Apply");
        }
    }

    private void clearPlayerOptionRow(int index, String query) {
        OptionalInt optionIdx = findPlayerOption(query);
        if(optionIdx.isPresent() && optionIdx.getAsInt() == index)
            setPlayerOption(index, "null", false);
    }

    public void setPlayerAttackable(boolean attackable) {
        if(attackable) {
            setPlayerOption(1, flagDuel ? "Fight" : "Attack", true);
        } else {
            clearPlayerOptionRow(1, flagDuel ? "Fight" : "Attack");
        }
    }

    public void setPlayerTakeFromAble(boolean takeFromAble) {
        if(takeFromAble) {
            setPlayerOption(5, "Take-from", false);
        } else {
            clearPlayerOptionRow(5, "Take-from");
        }
    }

    public void setPlayerGambleable(boolean gambleable) {
        if(gambleable) {
            setPlayerOption(1, "Gamble-With", false);
        } else {
            clearPlayerOptionRow(1, "Gamble-With");
        }
    }

    public void setPlayerTradeable(boolean tradeable) {
        if(tradeable) {
            setPlayerOption(4, "Trade with", false);
        } else {
            clearPlayerOptionRow(4, "Trade with");
        }
    }

    public void setPlayerFollowable(boolean followable) {
        if(followable) {
            setPlayerOption(3, "Follow", false);
        } else {
            clearPlayerOptionRow(3, "Follow");
        }
    }

    public void setPlayerChallengeable(boolean changeable) {
        if(changeable) {
            setPlayerOption(1, "Challenge", false);
        } else {
            clearPlayerOptionRow(1, "Challenge");
        }
    }

    public void setPlayerItemOnPlayerOption(String arg, boolean toggle, boolean top) {
        if(toggle) {
            setPlayerOption(2, arg, top);
        } else {
            clearPlayerOptionRow(2, arg);
        }
    }

    public void setPlayerItemOnPlayerOption(String arg, boolean toggle) {
        setPlayerItemOnPlayerOption(arg, toggle, false);
    }

    public void setPlayerOption(final int index, final String option, final boolean top) {
        options[index] = option;
        packetDispatcher.sendPlayerOption(index, option, top);
        if (options[index] != null) {
            if (options[index].equals("Attack") && (option == null || !option.equals("Attack"))) {
                setCanPvp(false);
            } else if (options[index].equals("Fight") && (option == null || !option.equals("Fight"))) {
                setCanPvp(false);
            }
        }
        if (Objects.equals(option, "Attack") || Objects.equals(option, "Fight")) {
            setCanPvp(true, option.equals("Fight"));
        }
    }

    public final OptionalInt findPlayerOption(@NotNull final String query) {
        for (int i = 0; i < options.length; i++) {
            if (Objects.equals(options[i], query)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    public String getIP() {
        Session session = getSession();
        return session == null ? "NA" : session.getHostAddress();
    }

    private static final Calendar thanksgivingStart;
    private static final Calendar thanksgivingEnd;

    static {
        thanksgivingStart = Calendar.getInstance();
        thanksgivingEnd = Calendar.getInstance();
        thanksgivingStart.set(2019, Calendar.NOVEMBER, 28);
        thanksgivingEnd.set(2019, Calendar.DECEMBER, 8);
    }

    public void onLogin() {
        isLoggedIn = true;
        // [-64, -45, -20, 94, 122, 20, 67, 29, -113, 38, 83, -100, 121, 117, -37, 98, 0, 0, 0, 0, 0, 0, 0, 0]
        if (log.isInfoEnabled())
            log.info("'" + getName() + "' has logged in with UUID "+Arrays.toString(playerInformation.getUUID())+".");
        sendMessage("Welcome to " + GameConstants.SERVER_NAME + ".");
        GameLogger.log(Level.INFO, () -> new GameLogMessage.Login(
                Instant.Companion.now(),
                getUsername(),
                getIP()
        ));
        getBankPin().loggedIn(this);
        if (Objects.equals(15515, getLocation().getRegionId()))
            setLocation(new Location(3808, 9755, 1));
        varManager.refreshDefaults();
        final String updateMessage =
                "Latest Update: " + GameConstants.UPDATE_LOG_BROADCAST + "|" + GameConstants.UPDATE_LOG_URL;
        if (getNumericAttribute(GameSetting.ALWAYS_SHOW_LATEST_UPDATE.toString()).intValue() == 1 || !Objects.equals(attributes.get("latest update message"), updateMessage)) {
            sendMessage(updateMessage, MessageType.GLOBAL_BROADCAST);
            addAttribute("latest update message", updateMessage);
        }
        if (WORLD_PROFILE.isBeta()) {
            sendMessage("This is a " + Colour.TURQOISE.wrap("Beta World") + "; your progress will not affect the main" +
                    " game.");
        }
        if (GameConstants.BOOSTED_XP) {
            sendMessage("<col=00FF00><shad=000000>Experience is boosted by 50% until " + new Date(BonusXpManager.expirationDate) + "!</col></shad>");
        }
        if (!attributes.containsKey("death timers info")) {
            attributes.put("death timers info", true);
            sendMessage(Colour.RS_GREEN.wrap("Info: Items lost on death will remain invisible on the ground for 3 " +
                    "minutes(boosted to 60 for UIM), followed by 5 minutes of visibility to everyone."));
        }
        if (!attributes.containsKey("Thanksgiving 2019 event")) {
            final Calendar date = Calendar.getInstance();
            if (thanksgivingStart.before(date) && thanksgivingEnd.after(date)) {
                attributes.put("Thanksgiving 2019 event", true);
                sendMessage(Colour.RS_PURPLE.wrap("Congratulations! You have unlocked the 'Give Thanks' emote."));
            }
        }
        if (!attributes.containsKey("treasure trails broadcasting")) {
            attributes.put("treasure trails broadcasting", 1);
            if (getNumericAttribute(GameSetting.TREASURE_TRAILS_BROADCASTS.toString()).intValue() == 0) {
                GameSetting.TREASURE_TRAILS_BROADCASTS.handleSetting(this);
            }
        }

        if(GambleBan.hasGambleBan(this)) {
               if(!GambleBan.isGambleBanValid(this)) {
                   sendMessage("Your gamble ban has expired.");
                   getAttributes().remove("GAMBLE_BAN");
               }
        }

//        varManager.sendVar(HalloweenUtils.COMPLETED_VARP, HalloweenUtils.isCompleted(this) ? 1 : 0);
//        varManager.sendVar(GIVE_THANKS_VARP, attributes.containsKey("Thanksgiving 2019 event") ? 1 : 0);
//        if (SplittingHeirs.progressedAtLeast(this, Stage.EVENT_COMPLETE)) {
//            emotesHandler.unlock(Emote.AROUND_THE_WORLD_IN_EGGTY_DAYS);
//            emotesHandler.unlock(Emote.RABBIT_HOP);
//        }

        if (getLootkeySettings() != null) {
            if (lootkeySettings.getCurrentItemsInChest() != null) {
                if (lootkeySettings.getCurrentItemsInChest().size() >= 1) {
                    LootkeySettings.sendOpenChest(this);
                }
            }
        }

//        varManager.sendBit(15026, attributes.containsKey("Christmas 2019 event") ? 1 : 0);
//        final boolean christmasEventCompleted = AChristmasWarble.progressedAtLeast(this,
//                AChristmasWarble.ChristmasWarbleProgress.EVENT_COMPLETE);
//        varManager.sendBit(15024, christmasEventCompleted ? 1 : 0);
//        varManager.sendBit(15025, christmasEventCompleted ? 1 : 0);
        varManager.sendBit(16000, memberRank.equalToOrGreaterThan(MemberRank.PREMIUM) ? 1 : 0);
        if (!getAuthenticator().isEnabled()) {
            sendMessage(Colour.RED.wrap("You do not have 2FA enabled. Please enable it for extra account security and" +
                    " extra vote rewards!"));
        }
        if (SanctionPlayerExtKt.isMuted(this))
            sendMessage("You are currently muted.");
        if (SanctionPlayerExtKt.isYellMuted(this))
            sendMessage("You are currently yell-muted.");

        updateScopeInScene();
        setRun(isRun());
        //Invokes the xp multiplier refresh.
        setExperienceMultiplier(getCombatXPRate(), getSkillingXPRate());
        refreshQuestPoints();
        getInventory().refreshAll();
        getEquipment().refreshAll();
        getSkills().refresh();
        toxins.refresh();
        settings.refresh();
        bonuses.update();
        appearance.resetRenderAnimation();
        packetDispatcher.sendRunEnergy();
        sendPlayerOptions();
        MethodicPluginHandler.invokePlugins(ListenerType.LOGIN, this);
        PluginManager.post(new LoginEvent(this));
        MiddleManManager.INSTANCE.onLogin(this);
        WorldBroadcasts.onLogin(this);
        controllerManager.login();
        GlobalAreaManager.update(this, true, false);
        World.updateEntityChunk(this, false);
        Analytics.logLogin(this);
        toaManager.onLogin();
        clip();
        LocationMap.add(this);
        final Calendar calendar = Calendar.getInstance();
        refreshGameClock();
        final long ticksUntilNextMinute =
                TimeUnit.MILLISECONDS.toTicks(60000 - (((calendar.get(Calendar.SECOND) * 1000) + calendar.get(Calendar.MILLISECOND)) % 60000)) + 1;
        if (ticksUntilNextMinute > 1) {
            WorldTasksManager.schedule(this::refreshGameClock, (int) ticksUntilNextMinute);
        }
        if (isOnMobile()) {
            packetDispatcher.sendClientScript(2644);
        }
        if (attributes.get("fixed respawn point teleport") == null) {
            attributes.put("fixed respawn point teleport", true);
            respawnPoint = RespawnPoint.EDGEVILLE;
        }
        if (isXPDropsWildyOnly()) {
            varManager.sendVar(3504, WildernessArea.isWithinWilderness(getX(), getY()) ? getSkillingXPRate() : 1);
        } else if (isXPDropsMultiplied()) {
            varManager.sendVar(3504, getSkillingXPRate());
        } else {
            varManager.sendVar(3504, 1);
        }
        packetDispatcher.resetCamera();
        // script for clearing pm history, synching all vars, send it tick later so server finishes transmitting varps and it synches, fixes roof and stuff
        WorldTasksManager.schedule(() -> packetDispatcher.sendClientScript(876, 0, 0, "", ""));
        //Blades by Urbi shop in Sophanem; Quest.
        varManager.sendBit(3275, 1);
        varManager.sendBit(8119, 1);
        variables.onLogin();
        music.refreshListConfigs();
        if (World.isUpdating()) {
            send(new UpdateRebootTimer(World.getUpdateTimer()));
        }
        if (!getBooleanAttribute("registered")) {
//            addAttribute("registered", 1);
            gameMode = GameMode.REGULAR;
            setLocation(GameConstants.REGISTRATION_LOCATION);
            if (getSettings().getChannelOwner() == null) {
                ClanManager.join(this, GameConstants.SERVER_CHANNEL_NAME);
            }
        }
        int unreadMessageCount = getNumericAttribute("unread message count").intValue();
        if (unreadMessageCount > 0) {
            sendMessage("You currently have <col=ff0000>" + unreadMessageCount + "</col> unread message" + (unreadMessageCount == 1 ? "" : "s") + "; visit the forums to check your inbox.");
        }

        if (newUUID) {
            final byte[] uniqueId = playerInformation.getUUID();
            send(new UpdateUID192(uniqueId));
        }
    }

    public void refreshGameClock() {
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        final int hours = calendar.get(Calendar.HOUR_OF_DAY);
        final int minutes = calendar.get(Calendar.MINUTE);
        varManager.sendBit(8354, (hours * 60) + minutes);
    }

    public void onLobbyClose() {
        pollManager.loadAnsweredPolls();
        varManager.sendVar(1050, 90);// chivalry/piety
        varManager.sendBit(598, 2);
        prayerManager.refreshQuickPrayers();
        if (petId != -1 && PetWrapper.getByPet(petId) != null) {
            if (follower == null) {
                setFollower(new Follower(petId, this));
            }
        }
        /*
         * if (player.getHelmet() != null && player.getHelmet().getId() >= 5525 && player.getHelmet().getId() <=
         * 5547) { final int bitId =
         * 599 + (player.getHelmet().getId() - 5525); player.getVarManager().sendBit(bitId, 1); }
         */
        combatDefinitions.refresh();
        socialManager.loadFriends();
        socialManager.loadIgnores();
        /*
         * final ClanChannel channel = player.getSettings().getChannel(); if (channel != null) { ClanManager.join
         * (player,
         * channel.getOwner()); } else { ClanManager.join(player, "kris"); }
         */
        socialManager.updateStatus();
        try {
            farming.refresh();
        } catch (Exception ex) {
            log.error("farming not working", ex);
        }

        getRunePouch().getContainer().refresh(this);
        grandExchange.updateOffers();
        VarCollection.updateType(this, EventType.POST_LOGIN);
        send(new ChatFilterSettings(this));
        send(new SetPrivateChatFilter(this));
        dailyChallengeManager.notifyUnclaimedChallenges();
        MethodicPluginHandler.invokePlugins(ListenerType.LOBBY_CLOSE, this);
        if (isDead()) {
            sendDeath();
        }
    }

    public Duel getDuel() {
        if (duel != null && duel.getPlayer() != this) {
            final Player opponent = duel.getPlayer();
            duel.setPlayer(this);
            duel.setOpponent(opponent);
        }
        return duel;
    }

    public String getTitleName() {
        StringBuilder sb = new StringBuilder();
        sb.append("<title>");
        putCrownInner(privilege.crown(), sb);
        putCrownInner(getGameModeCrown(), sb);
        putCrownInner(getMemberCrown(), sb);
        sb.append("</title>");
        if (!sb.toString().contains("img"))
            sb = new StringBuilder();
        sb.append(getPlayerInformation().getDisplayname());
        return sb.toString();
    }

    private void putCrownInner(Crown crown, StringBuilder sb) {
        if (crown.getId() != -1) {
            sb.append(crown.getCrownTag());
        }
    }

    public int getRankIcon() {
        if(privilege == PlayerPrivilege.TRUE_DEVELOPER)
            return 2;
        return privilege.crown().getId() + 1;
    }

    public int getGameModeIcon() {
        return getGameModeCrown().getId();
    }

    public Crown getMemberCrown() {
        return getMemberRank().crown();
    }

    public String getMemberName() {
        return getMemberRank().toString();
    }

    public boolean isMember() {
        return !getMemberRank().equals(MemberRank.NONE);
    }

    public boolean isStaff() {
        return privilege.ordinal() >= PlayerPrivilege.SUPPORT.ordinal();
    }

    public boolean hasGameMode() {
        return gameMode != GameMode.REGULAR;
    }

    public boolean hasMemberRank() {
        return memberRank != MemberRank.NONE;
    }

    public AchievementDiaries getAchievementDiaries() {
        return achievementDiaries;
    }

    public CutsceneManager getCutsceneManager() {
        return cutsceneManager;
    }

    public PuzzleBox getPuzzleBox() {
        return puzzleBox;
    }

    public LightBox getLightBox() {
        return lightBox;
    }

    public ChargesManager getChargesManager() {
        return chargesManager;
    }

    public PollManager getPollManager() {
        return pollManager;
    }

    public AreaManager getAreaManager() {
        return areaManager;
    }

    public GodBooks getGodBooks() {
        return godBooks;
    }

    public BossTimer getBossTimer() {
        return bossTimer;
    }

    public CollectionLog getCollectionLog() {
        return collectionLog;
    }

    public CALog getCombatAchievements() { return caLog; }

    public HpHud getHpHud() {
        return hpHud;
    }

    public DialogueManager getDialogueManager() {
        return dialogueManager;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public ControllerManager getControllerManager() {
        return controllerManager;
    }

    public MusicHandler getMusic() {
        return music;
    }

    public PresetManager getPresetManager() {
        return presetManager;
    }

    public EmotesHandler getEmotesHandler() {
        return emotesHandler;
    }

    public InterfaceHandler getInterfaceHandler() {
        return interfaceHandler;
    }

    public List<Integer> getTrackedHolidayItems() {
        return trackedHolidayItems;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public Set<Container> getPendingContainers() {
        return pendingContainers;
    }

    public SocialManager getSocialManager() {
        return socialManager;
    }

    public CombatDefinitions getCombatDefinitions() {
        if(combatDefinitions == null)
            combatDefinitions = new CombatDefinitions(this);
        return combatDefinitions;

    }

    public MagicStorageUnit getMagicStorageUnit() {
        return magicStorageUnit;
    }

    public DwarfMultiCannon getDwarfMulticannon() {
        return dwarfMulticannon;
    }

    public Equipment getEquipmentTemp() {
        return equipmentTemp;
    }

    public Equipment getEquipment() {
        fakeEquipmentState = TempPlayerStatePlugin.transformTempState(this, fakeEquipmentState, TempPlayerStatePlugin.StateType.EQUIPMENT);
        if (TempPlayerStatePlugin.isTemp(fakeEquipmentState)) {
            return equipmentTemp;
        } else {
            return equipment;
        }
    }

    public Inventory getInventoryTemp() {
        return inventoryTemp;
    }

    public Inventory getInventory() {
        fakeInventoryState = TempPlayerStatePlugin.transformTempState(this, fakeInventoryState, TempPlayerStatePlugin.StateType.INVENTORY);
        if (TempPlayerStatePlugin.isTemp(fakeInventoryState)) {
            return inventoryTemp;
        } else {
            return inventory;
        }
    }

    public DeathMechanics getDeathMechanics() {
        return deathMechanics;
    }

    public NotificationSettings getNotificationSettings() {
        return notificationSettings;
    }

    public PriceChecker getPriceChecker() {
        return priceChecker;
    }

    public Trade getTrade() {
        return trade;
    }

    public SeedVault getSeedVault() {
        return seedVault;
    }

    public RunePouch getRunePouch() {
        fakeRunePouchState = TempPlayerStatePlugin.transformTempState(this, fakeRunePouchState, TempPlayerStatePlugin.StateType.RUNE_POUCH);
        if (TempPlayerStatePlugin.isTemp(fakeRunePouchState)) {
            return runePouchTemp;
        } else {
            return runePouch;
        }
    }

    public RunePouch getRunePouchTemp() {
        return runePouchTemp;
    }

    public RunePouch getSecondaryRunePouch() {
        fakeSecondaryRunePouchState = TempPlayerStatePlugin.transformTempState(this, fakeSecondaryRunePouchState, TempPlayerStatePlugin.StateType.RUNE_POUCH_SECONDARY);
        if (TempPlayerStatePlugin.isTemp(fakeSecondaryRunePouchState)) {
            return secondaryRunePouchTemp;
        } else {
            return secondaryRunePouch;
        }
    }

    public RunePouch getSecondaryRunePouchTemp() {
        return secondaryRunePouchTemp;
    }

    public SeedBox getSeedBox() {
        return seedBox;
    }

    public LootingBag getLootingBag() {
        return lootingBag;
    }

    public HerbSack getHerbSack() {
        return herbSack;
    }

    public BonePouch getBonePouch() {
        return bonePouch;
    }

    public DragonhidePouch getDragonhidePouch() {
        return dragonhidePouch;
    }

    public GemBag getGemBag() {
        return gemBag;
    }

    public Skills getSkills() {
        fakeSkillsState = TempPlayerStatePlugin.transformTempState(this, fakeSkillsState, TempPlayerStatePlugin.StateType.SKILLS);
        if (TempPlayerStatePlugin.isTemp(fakeSkillsState)) {
            return skillsTemp;
        } else {
            return skills;
        }
    }

    public Skills getSkillsTemp() {
        return skillsTemp;
    }

    public Settings getSettings() {
        return settings;
    }

    public Construction getConstruction() {
        return construction;
    }

    public PrayerManager getPrayerManager() {
        return prayerManager;
    }

    public TeleportManager getTeleportManager() {
        return teleportManager;
    }

    public VarManager getVarManager() {
        return varManager;
    }

    public PlayerInfo getPlayerViewport() {
        return playerViewport;
    }

    public NPCInfo getNpcViewport() {
        return npcViewport;
    }

    public PlayerVariables getVariables() {
        return variables;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public GrandExchange getGrandExchange() {
        return grandExchange;
    }

    public Bonuses getBonuses() {
        return bonuses;
    }

    public String[] getOptions() {
        return options;
    }

    public Object2LongOpenHashMap<String> getAttackedByPlayers() {
        return attackedByPlayers;
    }

    public PerkManager getPerkManager() {
        return perkManager;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public Barrows getBarrows() {
        return barrows;
    }

    public ItemRetrievalService getRetrievalService() {
        return retrievalService;
    }

    public Runnable getCloseInterfacesEvent() {
        return closeInterfacesEvent;
    }

    public void setCloseInterfacesEvent(Runnable closeInterfacesEvent) {
        this.closeInterfacesEvent = closeInterfacesEvent;
    }

    public boolean isNeedRegionUpdate() {
        return needRegionUpdate;
    }

    public void setNeedRegionUpdate(boolean needRegionUpdate) {
        this.needRegionUpdate = needRegionUpdate;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public PrivateStorage getPrivateStorage() {
        return privateStorage;
    }

    public void setPrivateStorage(PrivateStorage privateStorage) {
        this.privateStorage = privateStorage;
    }

    public GauntletItemStorage getGauntletItemStorage() {
        return gauntletItemStorage;
    }

    public void setGauntletItemStorage(GauntletItemStorage gauntletItemStorage) {
        this.gauntletItemStorage = gauntletItemStorage;
    }

    public PlayerInformation getPlayerInformation() {
        return playerInformation;
    }

    public Entity getLastTarget() {
        return lastTarget;
    }

    public void setLastTarget(Entity lastTarget) {
        this.lastTarget = lastTarget;
    }

    public DelayedActionManager getDelayedActionManager() {
        return delayedActionManager;
    }

    public Farming getFarming() {
        return farming;
    }

    public PacketDispatcher getPacketDispatcher() {
        return packetDispatcher;
    }

    public PetInsurance getPetInsurance() {
        return petInsurance;
    }

    public void setPetInsurance(PetInsurance petInsurance) {
        this.petInsurance = petInsurance;
    }

    public Follower getFollower() {
        return follower;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public boolean isCanPvp() {
        return canPvp;
    }

    public Stash getStash() {
        return stash;
    }

    public void setStash(Stash stash) {
        this.stash = stash;
    }

    public boolean isMaximumTolerance() {
        return maximumTolerance;
    }

    public void setMaximumTolerance(boolean maximumTolerance) {
        this.maximumTolerance = maximumTolerance;
    }

    public void setDuel(Duel duel) {
        this.duel = duel;
    }

    public Bank getBank() {
        return getPersonalBank();
    }

    public void setPersonalBank(SinglePlayerBank bank) {
        this.bank = bank;
    }

    public SinglePlayerBank getPersonalBank() {
        return bank;
    }

//    public BankPinSettingsInterface getBankPinSettings() {
//        return bankPinSettings;
//    }

    public AtomicBoolean getForceReloadMap() {
        return forceReloadMap;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public Slayer getSlayer() {
        return slayer;
    }

    public void setSlayer(Slayer slayer) {
        this.slayer = slayer;
    }

    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunter) {
        this.hunter = hunter;
    }

    public BlastFurnace getBlastFurnace() {
        return blastFurnace;
    }

    public RespawnPoint getRespawnPoint() {
        return respawnPoint;
    }


    public KillstreakLog getKillstreakLog() {
        return killstreakLog;
    }

    public DailyChallengeManager getDailyChallengeManager() {
        return dailyChallengeManager;
    }

    public Optional<GrotesqueGuardiansInstance> getGrotesqueGuardiansInstance() {
        return grotesqueGuardiansInstance;
    }

    public void setGrotesqueGuardiansInstance(Optional<GrotesqueGuardiansInstance> grotesqueGuardiansInstance) {
        this.grotesqueGuardiansInstance = grotesqueGuardiansInstance;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public boolean isLoadingRegion() {
        return loadingRegion;
    }

    public void setLoadingRegion(boolean loadingRegion) {
        this.loadingRegion = loadingRegion;
    }

    public long getMovementLock() {
        return movementLock;
    }

    public void setMovementLock(long movementLock) {
        this.movementLock = movementLock;
    }

    public long getDiceDelay() {
        return diceDelay;
    }

    public void setDiceDelay(long diceDelay) {
        this.diceDelay = diceDelay;
    }

    public String[] getNametags() {
        return nametags;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Crown getGameModeCrown() {
        if (getCombatXPRate() == 5 || getSkillingXPRate() == 5) {
            return gameMode.getCrownRealist();
        }

        return gameMode.crown();
    }

    public MemberRank getMemberRank() {
        if (getBooleanAttribute("amascut_donator")) {
            return MemberRank.AMASCUT;
        }
        return memberRank;
    }

    public PlayerPrivilege getPrivilege() {
        return privilege;
    }

    @Override
    public boolean isLogoutPrevented(int ticksAfterAttacked) {
        return super.isLogoutPrevented(ticksAfterAttacked)
                || getBooleanTemporaryAttribute("gambling");
    }

    public LogoutType getLogoutType() {
        return logoutType;
    }

    public void setLogoutType(LogoutType logoutType) {
        this.logoutType = logoutType;
    }

    public WheelOfFortune getWheelOfFortune() {
        return wheelOfFortune;
    }

    public boolean isUpdatingNPCOptions() {
        return updatingNPCOptions;
    }

    public void setUpdatingNPCOptions(boolean updatingNPCOptions) {
        this.updatingNPCOptions = updatingNPCOptions;
    }

    public boolean isUpdateNPCOptions() {
        return updateNPCOptions;
    }

    public void setUpdateNPCOptions(boolean updateNPCOptions) {
        this.updateNPCOptions = updateNPCOptions;
    }

    public IntSet getPendingVars() {
        return pendingVars;
    }

    public Runnable getPathfindingEvent() {
        return pathfindingEvent;
    }

    public void setPathfindingEvent(Runnable pathfindingEvent) {
        this.pathfindingEvent = pathfindingEvent;
    }

    public RouteEvent<Player, EntityStrategy> getCombatEvent() {
        return combatEvent;
    }

    public void setCombatEvent(RouteEvent<Player, EntityStrategy> combatEvent) {
        this.combatEvent = combatEvent;
    }

    public Int2ObjectMap<List<GamePacketEncoder>> getZoneFollowPackets() {
        return zoneFollowPackets;
    }

    public boolean isHeatmap() {
        return heatmap;
    }

    public void setHeatmap(boolean heatmap) {
        this.heatmap = heatmap;
    }

    public IntSet getChunksInScope() {
        return chunksInScope;
    }

    public int getHeatmapRenderDistance() {
        return heatmapRenderDistance;
    }

    public void setHeatmapRenderDistance(int heatmapRenderDistance) {
        this.heatmapRenderDistance = heatmapRenderDistance;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getDamageSound() {
        return damageSound;
    }

    public void setDamageSound(int damageSound) {
        this.damageSound = damageSound;
    }

    public IntList getPaydirt() {
        return paydirt;
    }

    public LoyaltyManager getLoyaltyManager() {
        return loyaltyManager;
    }

    public long getLastReceivedPacket() {
        return lastReceivedPacket;
    }

    public void setLastReceivedPacket(long lastReceivedPacket) {
        this.lastReceivedPacket = lastReceivedPacket;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }

    public int getLastWalkX() {
        return lastWalkX;
    }

    public int getLastWalkY() {
        return lastWalkY;
    }

    public List<MovementLock> getMovementLocks() {
        return movementLocks;
    }

    public Runnable getPostSaveFunction() {
        return postSaveFunction;
    }

    public void setPostSaveFunction(Runnable postSaveFunction) {
        this.postSaveFunction = postSaveFunction;
    }

    public boolean isNulled() {
        return nulled;
    }

    public void setNulled(boolean nulled) {
        this.nulled = nulled;
    }

    public Queue<Location> getTolerancePositionQueue() {
        return tolerancePositionQueue;
    }

    public enum StopType {
        ROUTE_EVENT(p -> {
            p.setRouteEvent(null);
            if (p.getFaceEntity() >= 0) {
                p.setFaceEntity(null);
            }
        }),
        INTERFACES(p -> {
            p.getTemporaryAttributes().remove("skillDialogue");
            p.getInterfaceHandler().closeInterfaces();
        }),
        WORLD_MAP(p -> {
            if (p.getWorldMap().isVisible() && p.getWorldMap().isFullScreen()) {
                p.getWorldMap().close();
            }
        }),
        WALK(p -> {
            p.getPacketDispatcher().resetMapFlag();
            p.resetWalkSteps();
        }),
        ACTIONS(p -> {
            p.getActionManager().forceStop();
            p.getDelayedActionManager().forceStop();
        }),
        ANIMATIONS(p -> p.setAnimation(Animation.STOP));
        private final Consumer<Player> consumer;

        StopType(Consumer<Player> consumer) {
            this.consumer = consumer;
        }
    }

    private static final class ProjPacket {
        private final Location sender;
        private final GamePacketEncoder packet;

        public ProjPacket(Location sender, GamePacketEncoder packet) {
            this.sender = sender;
            this.packet = packet;
        }
    }

    @Override
    public void setRun(boolean run) {
        if (getBooleanTemporaryAttribute("nightmare_drowsy")) {
            sendMessage("You're too drowsy to run!");
            getVarManager().sendVar(173, 0);
            return;
        }
        if (PlayerAttributesKt.getPvmArenaInRevivalState(this)) {
            sendMessage("You're too weak to run!");
            getVarManager().sendVar(173, 0);
            return;
        }

        super.setRun(run);
        getVarManager().sendVar(173, isRun() ? 1 : 0);
    }

    public boolean isQATeam() {
        return this.getPrivilege().is(PlayerPrivilege.FORUM_MODERATOR);
    }

    public boolean isModerator() {
        return this.getPrivilege().inherits(PlayerPrivilege.MODERATOR);
    }

    public boolean isSeniorModerator() {
        return this.getPrivilege().inherits(PlayerPrivilege.SENIOR_MODERATOR);
    }

    public boolean isAdministrator() {
        return this.getPrivilege().inherits(PlayerPrivilege.ADMINISTRATOR);
    }

    public boolean isManager() {
        return this.getPrivilege().inherits(PlayerPrivilege.DEVELOPER);
    }

    public boolean isDeveloper() {
        if (getPrivilege() != null)
            return getPrivilege().inherits(PlayerPrivilege.TRUE_DEVELOPER);
        else
            return false;
    }

    public transient boolean bleedNextTick = false;

    public boolean isSessionActive() {
        if (session == null)
            return false;
        else
            return session.isActive();
    }

    public boolean isSessionExpired() {
        if (session == null)
            return true;
        else
            return session.isExpired();
    }
}
