package com.zenyte.game.world.entity.player;

import com.google.common.collect.ObjectArrays;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.near_reality.api.service.sanction.SanctionCommands;
import com.near_reality.api.service.sanction.SanctionPlayerExtKt;
import com.near_reality.api.service.user.UserPlayerHandler;
import com.near_reality.game.content.UniversalShop;
import com.near_reality.game.content.commands.AdministratorCommands;
import com.near_reality.game.content.commands.CustomCommands;
import com.near_reality.game.content.commands.DeveloperCommands;
import com.near_reality.game.content.commands.PlayerCommands;
import com.near_reality.game.content.crystal.CrystalCommands;
import com.near_reality.game.content.middleman.MiddleManCommands;
import com.near_reality.tools.BotPrevention;
import com.near_reality.tools.logging.GameLogMessage;
import com.near_reality.tools.logging.GameLogger;
import com.zenyte.game.GameConstants;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.Book;
import com.zenyte.game.content.achievementdiary.AchievementDiaries;
import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.content.advent.AdventCalendarRaffle;
import com.zenyte.game.content.boons.impl.AnimalTamer;
import com.zenyte.game.content.boss.BossRespawnTimer;
import com.zenyte.game.content.boss.cerberus.area.CerberusLairInstance;
import com.zenyte.game.content.boss.grotesqueguardians.instance.GrotesqueGuardiansInstance;
import com.zenyte.game.content.chambersofxeric.ChambersCommands;
import com.zenyte.game.content.chambersofxeric.Raid;
import com.zenyte.game.content.chambersofxeric.map.RaidArea;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.content.donation.DonationToggle;
import com.zenyte.game.content.event.christmas2019.AChristmasWarble;
import com.zenyte.game.content.event.christmas2019.ChristmasUtils;
import com.zenyte.game.content.event.christmas2019.cutscenes.PresentScourgeCutscene;
import com.zenyte.game.content.event.christmas2019.cutscenes.ScourgeHouseInstance;
import com.zenyte.game.content.event.halloween2019.HalloweenUtils;
import com.zenyte.game.content.lootkeys.LootkeyConstants;
import com.zenyte.game.content.lootkeys.LootkeySettings;
import com.zenyte.game.content.minigame.barrows.Barrows;
import com.zenyte.game.content.minigame.fightcaves.FightCaves;
import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.model.InfernoWave;
import com.zenyte.game.content.minigame.inferno.npc.impl.zuk.TzKalZuk;
import com.zenyte.game.content.minigame.pestcontrol.PestControlUtilities;
import com.zenyte.game.content.minigame.wintertodt.Wintertodt;
import com.zenyte.game.content.partyroom.BirthdayEventRewardList;
import com.zenyte.game.content.serverevent.WorldBoost;
import com.zenyte.game.content.serverevent.WorldBoostType;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.teleports.SpellbookTeleport;
import com.zenyte.game.content.skills.magic.spells.teleports.Teleport;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportType;
import com.zenyte.game.content.skills.slayer.*;
import com.zenyte.game.content.stars.ScheduledShootingStarSpawn;
import com.zenyte.game.content.stars.ShootingStarLocation;
import com.zenyte.game.content.stars.ShootingStars;
import com.zenyte.game.content.universalshop.UniversalShopCommands;
import com.zenyte.game.content.universalshop.UniversalShopInterface;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.content.xamphur.XamphurHandler;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.BonusXpManager;
import com.zenyte.game.model.item.degradableitems.DegradableItem;
import com.zenyte.game.model.item.enums.RareDrop;
import com.zenyte.game.model.shop.Shop;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.testinterfaces.GameNoticeboardInterface;
import com.zenyte.game.model.ui.testinterfaces.ServerEventsInterface;
import com.zenyte.game.packet.out.Heatmap;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.drop.matrix.NPCDrops;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerPolicy;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.login.InvitedPlayersList;
import com.zenyte.game.world.entity.player.login.LoginManager;
import com.zenyte.game.world.entity.player.perk.PerkCommands;
import com.zenyte.game.world.entity.player.privilege.*;
import com.zenyte.game.world.entity.player.teleportsystem.PortalTeleport;
import com.zenyte.game.world.entity.player.variables.PlayerVariables;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.*;
import com.zenyte.game.world.region.area.EvilBobIsland;
import com.zenyte.game.world.region.area.plugins.RandomEventRestrictionPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfBoundaryException;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import com.zenyte.net.NetworkConstants;
import com.zenyte.plugins.dialogue.CountDialogue;
import com.zenyte.plugins.dialogue.OptionsMenuD;
import com.zenyte.plugins.item.DiceItem;
import com.zenyte.tools.AnimationExtractor;
import com.zenyte.utils.StringUtilities;
import com.zenyte.utils.TextUtils;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import kotlin.Unit;
import kotlinx.datetime.Instant;
import mgi.Indice;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.ObjectDefinitions;
import mgi.types.config.VarbitDefinitions;
import mgi.types.config.enums.EnumDefinitions;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.CollectionUtils;
import mgi.utilities.StringFormatUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.near_reality.game.item.HiddenItems.HIDDEN_ITEMS;
import static com.zenyte.game.GameConstants.isOwner;
import static com.zenyte.game.world.entity.player.Emote.GIVE_THANKS_VARP;
import static com.zenyte.game.world.entity.player.MessageType.GLOBAL_BROADCAST;
import static java.lang.Integer.parseInt;

/**
 * @author Tom
 */
public final class GameCommands {
    private static final Logger log = LoggerFactory.getLogger(GameCommands.class);
    private static final Map<String, Command> COMMANDS = new HashMap<>();

    static {
        CustomCommands.INSTANCE.register();
        PlayerCommands.INSTANCE.register();
        CrystalCommands.INSTANCE.register();
        MiddleManCommands.INSTANCE.register();
        AdministratorCommands.INSTANCE.register();
        DeveloperCommands.INSTANCE.register();
        SanctionCommands.INSTANCE.register();
        BotPrevention.INSTANCE.registerCommands();
        ChambersCommands.INSTANCE.register();
        PerkCommands.INSTANCE.register();
        UniversalShopCommands.INSTANCE.register();


        new Command(PlayerPrivilege.PLAYER, "boosters", "Opens your active boosters.", (p, args) -> {
            openBoosters(p);
        });


        new Command(PlayerPrivilege.DEVELOPER, "cerbinst", (player, args) -> {
                player.lock();
                try {
                    final AllocatedArea area = MapBuilder.findEmptyChunk(6, 8);
                    final CerberusLairInstance instance = new CerberusLairInstance(player, area);
                    instance.constructRegion();
                } catch (OutOfSpaceException e) {
                    log.error("", e);
                }
        });


        new Command(PlayerPrivilege.DEVELOPER, "setadvent", (p, args) -> {
            int day = Integer.parseInt(args[0]);
            int value = Integer.parseInt(args[1]);
            AdventCalendarManager.setChallenge(p, day, value);
            p.sendMessage("Incresing Day " + day + " by " + value);
        });

        new Command(PlayerPrivilege.DEVELOPER, "setrafflewinner", (p, args) -> {
            int day = Integer.parseInt(args[0]);
            String name = TextUtils.formatName(args[1]);
            AdventCalendarRaffle.setAdventRaffleWinner(day, name);
            p.sendMessage("Set Day " + day + " advent raffle winner to '" + name + "'");
        });

        new Command(PlayerPrivilege.PLAYER, "toggles", "Opens your premium toggles.",(p, args) -> {
            if(p.isMember()) {
                DonationToggle.openInterface(p);
            } else {
                p.sendMessage("You need to be a premium to open your toggles.");
            }
        });


        new Command(PlayerPrivilege.ADMINISTRATOR, "star", (p, args) -> {
            p.getDialogueManager().start(new Dialogue(p) {
                @Override
                public void buildDialogue() {
                    options(new DialogueOption("Random Spawn", () -> {
                        if (ShootingStars.getSpawn() != null) {
                            ShootingStars.getSpawn().stop();
                        }
                        ScheduledShootingStarSpawn spawn = ScheduledShootingStarSpawn.atAnyRandomLocation(0);
                        spawn.run();
                    }), new DialogueOption("Wilderness Spawn", () -> {
                        if (ShootingStars.getSpawn() != null) {
                            ShootingStars.getSpawn().stop();
                        }
                        ShootingStarLocation[] wilderness = {
                                ShootingStarLocation.SOUTH_WILDERNESS_MINE,
                                ShootingStarLocation.SOUTH_WEST_WILDERNESS_MINE,
                                ShootingStarLocation.BANDIT_CAMP_MINE,
                                ShootingStarLocation.LAVA_MAZE_RUNITE_MINE,
//                                ShootingStarLocation.RESOURCE_AREA,
                                ShootingStarLocation.MAGE_ARENA,
                                ShootingStarLocation.PIRATES_HIDEOUT_MINE
                        };
                        ScheduledShootingStarSpawn spawn = ScheduledShootingStarSpawn.atLocation(Utils.random(wilderness), 0);
                        spawn.run();
                    }));
                }
            });
        });

        new Command(PlayerPrivilege.ADMINISTRATOR, "hphudopen", (p, args) -> {
            p.getHpHud().open(NpcId.NEX, 1500);
        });

        new Command(PlayerPrivilege.ADMINISTRATOR, "findobj", (p, args) -> {
            int objId = Integer.parseInt(args[0]);
            ObjectArrayList<Integer> found = new ObjectArrayList<>();
            for (int id = 0; id < MapBuilder.AMOUNT_OF_REGIONS; id++) {
                Region region = World.regions.get(id);
                if (region == null) {
                    region = new Region(id);
                    World.regions.put(id, region);
                }
                region.load();

                final int regionId = id;
                if(region.getObjects() != null)
                    region.getObjects().forEach((o, o1) -> {
                        if (o1.getId() == objId) {
                            if(!found.contains(regionId))
                                found.add(regionId);
                        }
                    });
            }
            found.forEach(i -> System.out.println("FOUND: "+i));
        });

        new Command(PlayerPrivilege.ADMINISTRATOR, "hphudclose", (p, args) -> {
            p.getHpHud().close();
        });

        new Command(PlayerPrivilege.ADMINISTRATOR, "lt-1", (p, args) -> {
            LootkeySettings.clear(p);
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "lt-n", (p, args) -> {
            Item item = new Item(LootkeyConstants.LOOT_KEY_ITEM_ID);

            Container container = new Container(ContainerPolicy.NORMAL, ContainerType.WILDERNESS_LOOT_KEY,
                    Optional.of(p));
            container.setItems(p.getInventory().getContainer().getItems());

            item.setAttribute(LootkeyConstants.LOOT_KEY_ITEM_LOOT_ITEMS_ATTR,
                    LoginManager.gson.get().toJson(container.getItems()));
            p.getInventory().addItem(item);
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "addspins", (p, args) -> {
            p.getWheelOfFortune().setSpins(p.getWheelOfFortune().getSpins() + parseInt(args[0]));
        });
        new Command(PlayerPrivilege.SENIOR_MODERATOR, "addbroadcast", (p, args) -> {
            final int id = parseInt(args[0]);
            p.getDialogueManager().start(new Dialogue(p) {
                @Override
                public void buildDialogue() {
                    options("Add " + ItemDefinitions.getOrThrow(id).getName() + " to broadcasts?",
                            new DialogueOption("Yes", () -> {
                                RareDrop.addDynamic(id);
                                p.sendMessage("Added " + ItemDefinitions.getOrThrow(id).getName() + " to custom " +
                                        "broadcasts.");
                            }), new DialogueOption("No"));
                }
            });
        });
        new Command(PlayerPrivilege.SENIOR_MODERATOR, "removebroadcast", (p, args) -> {
            final List<Integer> broadcasts = RareDrop.getDynamicItemIds().stream().toList();
            if (broadcasts.isEmpty()) {
                p.sendMessage("No dynamic broadcasts present.");
                return;
            }
            final ObjectArrayList<String> options = new ObjectArrayList<>();
            for (final Integer bc : broadcasts) {
                options.add(ItemDefinitions.getOrThrow(bc).getName());
            }
            options.add("All broadcasts");
            p.getDialogueManager().start(new OptionsMenuD(p, "Select broadcast to remove",
                    options.toArray(new String[0])) {
                @Override
                public void handleClick(int slotId) {
                    if (slotId >= broadcasts.size()) {
                        for (final Integer bc : broadcasts) {
                            RareDrop.removeDynamic(bc);
                        }
                        p.sendMessage("Wiped custom broadcasts.");
                        return;
                    }
                    final int id = broadcasts.get(slotId);
                    RareDrop.removeDynamic(id);
                    p.sendMessage("Removed " + ItemDefinitions.getOrThrow(id).getName() + " from broadcasts.");
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(PlayerPrivilege.DEVELOPER, "skotaltar", (p, args) -> {
            p.setLocation(new Location(1665, 10048, 0));
        });

        new Command(PlayerPrivilege.DEVELOPER, "skotroom", (p, args) -> {
            p.setLocation(new Location(1693, 9886, 0));
        });
        new Command(PlayerPrivilege.DEVELOPER, "dmgzuk", (p, args) -> {
            final RegionArea area = p.getArea();
            if (area instanceof Inferno inferno) {
                final TzKalZuk zuk = inferno.getNPCs(TzKalZuk.class).get(0);
                zuk.applyHit(new Hit(p, parseInt(args[0]), HitType.REGULAR));
            }
        });
        new Command(PlayerPrivilege.DEVELOPER, "skipwave", (p, args) -> {
            if (!p.inArea(p.getName() + "'s Inferno Instance")) {
                p.sendMessage("You must be in the Inferno to do this.");
                return;
            }
            final Inferno inferno = (Inferno) p.getArea();
            final InfernoWave wave = InfernoWave.get(parseInt(args[0]));
            p.sendMessage("Skipped to wave " + wave.getWave() + ".");
            inferno.skip(wave);
        });
        new Command(PlayerPrivilege.DEVELOPER, "inferno", (p, args) -> {
            p.setLocation(new Location(2496, 5115, 0));
        });
        new Command(PlayerPrivilege.DEVELOPER, "cutscene", (p, args) -> {
            try {
                final ScourgeHouseInstance instance = new ScourgeHouseInstance(MapBuilder.findEmptyChunk(4, 4));
                instance.constructRegion();
                final FadeScreen fadeScreen = new FadeScreen(p);
                fadeScreen.fade();
                p.getCutsceneManager().play(new PresentScourgeCutscene(p, instance, () -> fadeScreen.unfade(false)));
            } catch (OutOfSpaceException e) {
                e.printStackTrace();
            }
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "eventstage", (p, args) -> p.sendInputName("Whose event stage to " +
                "update?", name -> World.getPlayer(name).ifPresent(target -> {
            final AChristmasWarble.ChristmasWarbleProgress[] stages = AChristmasWarble.ChristmasWarbleProgress.values();
            final ObjectArrayList<String> stageNames = new ObjectArrayList<>();
            for (final AChristmasWarble.ChristmasWarbleProgress stage : stages) {
                stageNames.add(stage.toString());
            }
            p.getDialogueManager().start(new OptionsMenuD(p, "Select stage", stageNames.toArray(new String[0])) {
                @Override
                public void handleClick(int slotId) {
                    target.getAttributes().put(AChristmasWarble.ChristmasWarbleProgress.EVENT_ATTRIBUTE_KEY,
                            stages[slotId].getStage());
                    if (stages[slotId].ordinal() <= AChristmasWarble.ChristmasWarbleProgress.FROZEN_GUESTS.ordinal()) {
                        target.getAttributes().remove("A Christmas Warble unfrozen guests hash");
                    }
                    ChristmasUtils.refreshAllVarbits(target);
                }

                @Override
                public boolean cancelOption() {
                    return false;
                }
            });
        })));

        new Command(PlayerPrivilege.ADMINISTRATOR, "bonusxp", (p, args) -> p.sendInputString("Enter bonus xp expiration " +
                "date: " +
                "(format: YYYY/MM/DD/HH)", value -> {
            final String[] split = value.split("/");
            if (split.length != 4) {
                p.sendMessage("Invalid format.");
                return;
            }
            final Calendar instance = Calendar.getInstance();
            instance.set(parseInt(split[0]), parseInt(split[1]) - 1, parseInt(split[2]),
                    parseInt(split[3]), 0, 0);
            p.getDialogueManager().start(new Dialogue(p) {
                @Override
                public void buildDialogue() {
                    options("Set bonus experience expiration date to <br>" + instance.getTime() + "?",
                            new DialogueOption("Yes.", () -> BonusXpManager.set(instance.getTimeInMillis())),
                            new DialogueOption("No."));
                }
            });
        }));
        new Command(PlayerPrivilege.ADMINISTRATOR, "objs", (p, args) -> {
            final WorldObject[] objects = World.getRegion(p.getLocation().getRegionId()).getObjects(p.getPlane(),
                    p.getX() & 63, p.getY() & 63);
            if (objects == null) {
                p.sendMessage("No objects detected on this location.");
                return;
            }
            for (final WorldObject object : objects) {
                if (object == null) {
                    continue;
                }
                p.sendMessage("Object: " + object.getId() + ", type: " + object.getType() + ", rotation: " + object.getRotation() + ", location: " + object.getX() + ", " + object.getY() + ", " + object.getPlane() + ".");
            }
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "gc", (p, args) -> {
            if (!GameConstants.WORLD_PROFILE.isDevelopment()) {
                return;
            }
            System.gc();
        });

        new Command(PlayerPrivilege.SUPPORT, "timeout", "Toggles staff logout-timer for current session.",
                (p, args) -> {
                    p.getTemporaryAttributes().put("staff timeout disabled", p.getNumericTemporaryAttribute("staff " +
                            "timeout " +
                            "disabled").intValue() == 1 ? 0 : 1);
                    p.sendMessage("Timeout setting: " + (p.getNumericTemporaryAttribute("staff timeout disabled").intValue() == 1 ? "enabled" : "disabled") + ".");
                });
        new Command(PlayerPrivilege.MODERATOR, "bosstimers", "Opens boss spawn timer menu.",
                (p, args) -> BossRespawnTimer.open(p));
        new Command(PlayerPrivilege.ADMINISTRATOR, "disablewintertodt", "Toggles access to the Wintertodt's prison.",
                (p,
                 args) -> {
                    Wintertodt.setDisabled(!Wintertodt.isDisabled());
                    p.sendMessage("Wintertodt disabled: " + Wintertodt.isDisabled());
                });
        new Command(PlayerPrivilege.PLAYER, "xplock", "Locks your combat experience.", (p, args) -> {
            boolean current = p.getBooleanAttribute("combat_xp_lock");
            p.putBooleanAttribute("combat_xp_lock", !current);

            p.sendMessage("Your combat XP gain is currently " + (current ? "un" : "") + "locked.");
        });
        new Command(PlayerPrivilege.PLAYER, new String[]{"gambling", "gamble", "dice", "dicing", "fp", "flowerpoker",
                "flower"}, "Teleports you to the gambling zone.",(p, args) -> {
            if (p.isLocked()) {
                return;
            }
            p.getDialogueManager().start(new Dialogue(p) {
                @Override
                public void buildDialogue() {
                    plain(DiceItem.GAMBLE_WARNING);
                    options(TITLE, "Take me there", "Cancel").onOptionOne(() -> {
                        final Teleport teleport = new Teleport() {
                            @Override public TeleportType getType() {return TeleportType.NEAR_REALITY_PORTAL_TELEPORT;}
                            @Override public Location getDestination() {return new Location(3366, 6939, 2);}
                            @Override public int getLevel() {return 0;}
                            @Override public double getExperience() {return 0;}
                            @Override public int getRandomizationDistance() {return 0;}
                            @Override public Item[] getRunes() {return new Item[0];}
                            @Override public int getWildernessLevel() {return 0;}
                            @Override public boolean isCombatRestricted() {return true;}
                            @Override public void onUsage(Player player) {
                                player.lock(3);
                                new FadeScreen(player, () -> {}).fade(3);
                            }
                        };
                        teleport.teleport(player);
                    });
                }
            });
        });

        new Command(PlayerPrivilege.PLAYER, "skull", "Skulls your character without another player.",(p, args) -> {
            p.getDialogueManager().start(new Dialogue(p) {
                @Override
                public void buildDialogue() {
                    options("Are you sure you want a skull?",
                            "Yes, skull me up!",
                            "No, I've changed my mind!")
                            .onOptionOne(() ->  p.getVariables().setSkull(true));
                }
            });

        });

        new Command(PlayerPrivilege.DEVELOPER, "gg", (p, args) -> {
            p.setLocation(GrotesqueGuardiansInstance.OUTSIDE_LOCATION);
        });
        new Command(PlayerPrivilege.DEVELOPER, "sumonatask", (p, args) -> {
            List<SlayerTask> tasks = new ArrayList<>(Arrays.asList(BossTaskSumona.VALUES));
            tasks.sort(Comparator.comparing(Object::toString));
            final ArrayList<String> names = new ArrayList<>();
            for (final SlayerTask task : tasks) {
                names.add(task.getTaskName());
            }
            p.getDialogueManager().start(new OptionsMenuD(p, "Select the task to receive",
                    names.toArray(new String[0])) {
                @Override
                public void handleClick(final int slotId) {
                    if (slotId >= tasks.size()) {
                        return;
                    }
                    final SlayerTask task = tasks.get(slotId);
                    player.sendInputInt("Enter kill count requirement:", new CountDialogue() {
                        @Override
                        public void run(int amount) {
                            final Assignment assignment = new Assignment(player, player.getSlayer(), task,
                                    task.getEnumName(), amount, amount, SlayerMaster.SUMONA);
                            p.getSlayer().setAssignment(assignment);
                            p.getSlayer().setMaster(SlayerMaster.SUMONA);
                            p.getDialogueManager().start(new Dialogue(p, p.getSlayer().getMaster().getNpcId()) {
                                @Override
                                public void buildDialogue() {
                                    npc("Your new task is to kill " + assignment.getAmount() + " " + assignment.getTask().toString() + ".");
                                }
                            });
                        }
                    });
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "uninvite", "Revoke beta access of a user. Usage: ::uninvite " +
                "player_name", (p, args) -> {
            final String name = StringFormatUtil.formatUsername(StringUtilities.compile(args, 0, args.length, '_'));
            if (name.length() >= 1 && name.length() <= 12) {
                if (ArrayUtils.contains(GameConstants.owners, name)) {
                    p.sendMessage("You cannot uninvite owners.");
                    return;
                }
                InvitedPlayersList.invitedPlayers.remove(name);
                p.sendMessage("Revoked beta access from " + name + ".");
            }
        });
        new Command(PlayerPrivilege.MODERATOR, "related", "See others users with the same ip or mac address. Usage: " +
                "::related player name",
                (p, args) -> p.sendMessage("Please user the discord bot in order to find related account with the command /search_related_players <name>"));
        new Command(PlayerPrivilege.DEVELOPER, "clearnullednpcs", (p, args) -> {
            //Clears the npcs which are visible in the player's viewport and have died - if they're still in dead
            // status 10 ticks after the check.
            CharacterLoop.forEach(p.getLocation(), p.getViewDistance(), NPC.class, npc -> {
                if (npc.isDead()) {
                    p.sendMessage("Dead NPC: " + npc.getName(p) + ": " + npc.getLocation());
                    WorldTasksManager.schedule(() -> {
                        if (npc.isDead() && !npc.isFinished()) {
                            npc.setRespawnTask();
                        }
                    }, 10);
                }
            });
        });

        new Command(PlayerPrivilege.DEVELOPER, "resetecto", (p, args) -> World.getPlayer(StringUtilities.compile(args
                , 0,
                args.length, ' ')).ifPresent(user -> {
            user.addAttribute("ectofuntus bone status", 0);
            user.addAttribute("ectofuntus grinded bone", 0);
            p.sendMessage(user.getName() + "'s ectofuntus settings reset.");
        }));
        new Command(PlayerPrivilege.DEVELOPER, "evilbob", (p, args) -> {
            EvilBobIsland.teleport(p);
        });
        new Command(PlayerPrivilege.MODERATOR, "checkrandom", (p, args) -> {
            if (GameConstants.WORLD_PROFILE.isBeta() && !isOwner(p)) {
                return;
            }
            if (!p.isLocked() && !p.isFinished() && !p.isDead()) {
                final RegionArea area = p.getArea();
                if (!(area instanceof RandomEventRestrictionPlugin)) {
                    p.getAttributes().put("evil bob complete", true);
                    p.getAttributes().put("observing random event", true);
                    EvilBobIsland.teleport(p);
                } else {
                    p.sendMessage("You cannot teleport to the random event island from here.");
                }
            } else {
                p.sendMessage("You can't do that right now.");
            }
        });
        new Command(PlayerPrivilege.MODERATOR, "random", "Initiate a random event for a user. Usage: ::random player " +
                "name",
                (p, args) -> World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(target -> {
                    if (GameConstants.WORLD_PROFILE.isBeta() && !isOwner(p)) {
                        return;
                    }
                    final long lastEvent = target.getNumericAttribute("last random event").longValue();
                    if (!p.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR) && lastEvent + TimeUnit.MINUTES.toMillis(45) > System.currentTimeMillis()) {
                        p.sendMessage("That user has already played through a random event within the past 45 minutes" +
                                ".");
                        return;
                    }
                    target.log(LogLevel.INFO, "Forced random event by " + p.getName() + ".");
                    EvilBobIsland.teleport(target);
                }));
        new Command(PlayerPrivilege.MODERATOR, "movehome", "Moves another user home if they accept the request. " +
                "Usage: " +
                "::movehome player name",
                (p, args) -> World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(target -> {
                    if (p == target) {
                        p.sendMessage("You can't teleport yourself.");
                        return;
                    }
                    target.setLocation(new Location(3087, 3489, 0));
                }));
        new Command(PlayerPrivilege.SUPPORT, "clearfriendlist", "Remove all entries from friend list.", (p, args) -> {
            p.getSocialManager().getFriends().clear();
            p.sendMessage("Relog to refresh your friends list.");
        });
        new Command(PlayerPrivilege.PLAYER, "ccban", "Ban given user from your clan chat. Usage: ::ccban player name"
                , (p,
                   args) -> ClanManager.permban(p, StringUtilities.compile(args, 0, args.length, ' ')));
        new Command(PlayerPrivilege.PLAYER, "ccunban", "Unban given user from your clan chat. Usage: ::ccunban player" +
                " name"
                , (p, args) -> ClanManager.permunban(p, StringUtilities.compile(args, 0, args.length, ' ')));

//        new Command(PlayerPrivilege.ADMINISTRATOR, "npcinfo", (p, args) -> {
//            p.sendMessage("Currently " + World.getNPCs().size() + " in the game.");
//        });
        new Command(PlayerPrivilege.SUPPORT, "checkinv", "Check inv of given player. Usage: ::checkinv player name", (p,
                                                                                                                      args) -> {
            final String target = StringUtilities.compile(args, 0, args.length, ' ');
            final Optional<Player> targetPlayer = World.getPlayer(target);
            if (!targetPlayer.isPresent()) {
                p.sendMessage(target + " is not online.");
                return;
            }
            final Player tp = targetPlayer.get();
            if (tp == p) {
                p.sendMessage("You can't do this on yourself.");
                return;
            }
            p.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 12);
            p.getPacketDispatcher().sendUpdateItemContainer(tp.getInventory().getContainer(), ContainerType.BANK);
            p.getPacketDispatcher().sendComponentSettings(12, 13, 0, 1000, AccessMask.CLICK_OP10);
        });
        new Command(PlayerPrivilege.SUPPORT, "checkbank", "Check bank of given player. Usage: ::checkbank player " +
                "name", (p,
                         args) -> {
            final String target = StringUtilities.compile(args, 0, args.length, ' ');
            final Optional<Player> targetPlayer = World.getPlayer(target);
            if (!targetPlayer.isPresent()) {
                p.sendMessage(target + " is not online.");
                return;
            }
            final Player tp = targetPlayer.get();
            if (tp == p) {
                p.sendMessage("You can't do this on yourself.");
                return;
            }
            p.getTemporaryAttributes().put("viewing another bank", true);
            p.setCloseInterfacesEvent(() -> p.getTemporaryAttributes().remove("viewing another bank"));
            p.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 12);
            p.getPacketDispatcher().sendUpdateItemContainer(tp.getBank().getContainer(), ContainerType.BANK);
            tp.getBank().refreshBankSizes(p);
            p.getPacketDispatcher().sendComponentSettings(12, 13, 0, 1000, AccessMask.CLICK_OP10);
        });
        new Command(PlayerPrivilege.SUPPORT, "checkgear", "Check current worn equipment of a player. Usage: " +
                "::checkgear " +
                "player name", (p, args) -> {
            final String target = StringUtilities.compile(args, 0, args.length, ' ');
            final Optional<Player> targetPlayer = World.getPlayer(target);
            if (!targetPlayer.isPresent()) {
                p.sendMessage(target + " is not online.");
                return;
            }
            final Player tp = targetPlayer.get();
            if (tp == p) {
                p.sendMessage("You can't do this on yourself.");
                return;
            }
            p.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 12);
            p.getPacketDispatcher().sendUpdateItemContainer(tp.getEquipment().getContainer(), ContainerType.BANK);
        });
        new Command(PlayerPrivilege.DEVELOPER, "antiknox", (p, args) -> {
            GameConstants.ANTIKNOX = !GameConstants.ANTIKNOX;
            p.sendMessage("Antiknox: " + GameConstants.ANTIKNOX);
        });
        new Command(PlayerPrivilege.DEVELOPER, "purgechunks", (p, args) -> {
            GameConstants.PURGING_CHUNKS = !GameConstants.PURGING_CHUNKS;
            p.sendMessage("Purging chunks: " + GameConstants.PURGING_CHUNKS);
        });
        new Command(PlayerPrivilege.DEVELOPER, "huntercheck", (p, args) -> {
            GameConstants.CHECK_HUNTER_TRAPS_QUANTITY = !GameConstants.CHECK_HUNTER_TRAPS_QUANTITY;
            p.sendMessage("Checking hunter trap quantity: " + GameConstants.CHECK_HUNTER_TRAPS_QUANTITY);
        });

        new Command(PlayerPrivilege.DEVELOPER, "whitelisting", (p, args) -> {
            GameConstants.WHITELISTING = !GameConstants.WHITELISTING;
            p.sendMessage("Whitelisting: " + GameConstants.WHITELISTING);
        });
        new Command(PlayerPrivilege.DEVELOPER, "whitelist", (p, args) -> {
            GameConstants.whitelistedUsernames.add(StringFormatUtil.formatUsername(StringUtilities.compile(args, 0,
                    args.length, ' ')));
        });
        new Command(PlayerPrivilege.DEVELOPER, "teleparty", (p, args) -> {
            if (!isOwner(p)) {
                return;
            }
            final RegionArea area = p.getArea();
            if (!(area instanceof RaidArea)) {
                return;
            }
            final RaidArea raidArea = (RaidArea) area;
            final Raid raid = raidArea.getRaid();
            final Set<Player> members = raid.getPlayers();
            for (final Player member : members) {
                member.setLocation(p.getLocation());
            }
        });
        new Command(PlayerPrivilege.MODERATOR, "ip", "Get the IP address of a user.", (p, args) -> p.sendInputString(
                "Whose" +
                        " IP address to obtain?", value -> {
                    if (GameConstants.WORLD_PROFILE.isDevelopment() || GameConstants.WORLD_PROFILE.isBeta()) {
                        p.sendMessage("This command is disabled on this world.");
                        return;
                    }
                    final Optional<Player> player = World.getPlayer(value);
                    if (!player.isPresent()) {
                        p.sendMessage("Player is not online.");
                        return;
                    }
                    final Player target = player.get();
                    p.sendMessage("IP address for " + target.getName() + " is: " + target.getIP());
                }));
        new Command(PlayerPrivilege.DEVELOPER, "superiorrate", (p, args) -> {
            p.getTemporaryAttributes().put("superior rate", Math.max(0, parseInt(args[0]) - 1));
            p.sendMessage("Superiors will now appear at a rate of 1/" + (p.getNumericTemporaryAttribute("superior " +
                    "rate").intValue() + 1) + ".");
        });
        new Command(PlayerPrivilege.DEVELOPER, "js5duplicates", (p, args) -> {
            GameConstants.FILTERING_DUPLICATE_JS5_REQUESTS = !GameConstants.FILTERING_DUPLICATE_JS5_REQUESTS;
            p.sendMessage("JS5 duplicates filtering: " + GameConstants.FILTERING_DUPLICATE_JS5_REQUESTS);
        });

        new Command(PlayerPrivilege.DEVELOPER, "resetge", (p, args) -> p.getGrandExchange().resetExistingOffers());
        new Command(PlayerPrivilege.DEVELOPER, "multigfx", (p, args) -> {
            int id = parseInt(args[0]);
            final int px = p.getX();
            final int py = p.getY();
            for (int x = px - 10; x < px + 10; x++) {
                for (int y = py - 10; y < py + 10; y++) {
                    final Projectile proj = new Projectile(id++, 50, 50, 0, 0, 5000, 0, 5);
                    World.sendProjectile(new Location(x, y, p.getPlane()), new Location(x + 5, y, p.getPlane()), proj);
                }
            }
            p.sendMessage("Last: " + (id - 1));
        });
        new Command(PlayerPrivilege.DEVELOPER, "barrows", (p, args) -> {
            if (args.length < 2) {
                p.sendMessage("Arguments are <Number of kills> <Reward potential>");
                return;
            }
            final Barrows barrows = p.getBarrows();
            final int number = Math.min(100, parseInt(args[0]));
            final int rp = Math.max(0, parseInt(args[1]) - 668);
            for (int i = 0; i < number; i++) {
                barrows.setMaximumReward(rp);
                GameInterface.BARROWS_REWARDS.open(p);
            }
            p.sendMessage("Rolled " + number + " Barrows rewards at a reward potential of " + (rp + 668) + ".");
        });
        new Command(PlayerPrivilege.DEVELOPER, "defencemultiplier", (p, args) -> {
            final double dbl = Double.parseDouble(args[0]);
            GameConstants.defenceMultiplier = Math.max(0.5, Math.min(2, dbl));
            for (final Player player : World.getPlayers()) {
                player.sendMessage("PvP Defence multiplier has been set to " + GameConstants.defenceMultiplier,
                        GLOBAL_BROADCAST);
            }
        });
        new Command(PlayerPrivilege.DEVELOPER, "birthdayeventreload", (p, args) -> {
            BirthdayEventRewardList.reload();
            p.sendMessage("Birthday event reward list reloaded.");
        });
        new Command(PlayerPrivilege.DEVELOPER, "addbirthdayreward", (p, args) -> {
            final String username = StringUtilities.compile(args, 0, args.length, '_');
            BirthdayEventRewardList.addUsername(username);
            p.sendMessage(username + " added to birthday event reward list.");
        });

        new Command(PlayerPrivilege.MODERATOR, "pc", "Opens the pest control modification menu.", (p, args) -> {
            p.getDialogueManager().start(new OptionsMenuD(p, "Select the setting to change", "Minimum players " +
                    "requirement: " + Colour.RS_GREEN.wrap(PestControlUtilities.MINIMUM_PLAYERS_LIMIT + ""), "Maximum" +
                    " " +
                    "players requirement: " + Colour.RS_GREEN.wrap(PestControlUtilities.MAXIMUM_PLAYERS_LIMIT + ""),
                    "Time " +
                            "until deporting: " + Colour.RS_GREEN.wrap(PestControlUtilities.TIME_UNTIL_GAME_START +
                            "")) {
                @Override
                public void handleClick(int slotId) {
                    if (slotId == 0) {
                        player.sendInputInt("Enter minimum players requirement", PestControlUtilities::setMinimum);
                    } else if (slotId == 1) {
                        player.sendInputInt("Enter maximum players requirement", PestControlUtilities::setMaximum);
                    } else if (slotId == 2) {
                        player.sendInputInt("Enter delay between deportation in ticks", PestControlUtilities::setTime);
                    }
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(PlayerPrivilege.DEVELOPER, "multispawn", (p, args) -> {
            final int id = parseInt(args[0]);
            final int radius = args.length == 1 ? 10 : parseInt(args[1]);
            final NPCDefinitions defs = Objects.requireNonNull(NPCDefinitions.get(id));
            final int size = defs.getSize();
            final int px = p.getX();
            final int py = p.getY();
            int count = 0;
            for (int x = px - radius; x <= px + radius; x += size) {
                for (int y = py - radius; y <= py + radius; y += size) {
                    final Location tile = new Location(x, y, p.getPlane());
                    if (p.isProjectileClipped(tile, true)) {
                        continue;
                    }
                    if (++count > 250) {
                        break;
                    }
                    final NPC npc = World.spawnNPC(id, tile);
                    npc.setSpawned(true);
                }
            }
        });
        new Command(PlayerPrivilege.DEVELOPER, "smoke", (p, args) -> {
            final int radius = args.length == 0 ? p.getViewDistance() : parseInt(args[0]);
            final ArrayList<NPC> list = new ArrayList<>();
            final boolean unrestricted = args.length == 2;
            CharacterLoop.forEach(p.getLocation(), Math.min(p.getViewDistance(), radius), NPC.class, n -> {
                if (n.isAttackable() && n.isAttackable(p) && (unrestricted || !p.isProjectileClipped(n, false))) {
                    list.add(n);
                }
            });
            final Projectile projectile = new Projectile(310, 34, 50, 0, 0, 20, 0, 5);
            for (final NPC npc : list) {
                World.scheduleProjectile(p, npc, projectile).schedule(() -> npc.applyHit(new Hit(p,
                        npc.getHitpoints(), HitType.REGULAR)));
            }
        });
        new Command(PlayerPrivilege.DEVELOPER, "resetfarming", (p, args) -> {
            p.getFarming().reset();
        });
        new Command(PlayerPrivilege.DEVELOPER, "cycle", (p, args) -> {
            GameConstants.CYCLE_DEBUG = !GameConstants.CYCLE_DEBUG;
            p.sendMessage("Cycle debug: " + GameConstants.CYCLE_DEBUG);
        });
        new Command(PlayerPrivilege.MODERATOR, "heatmaps", "Toggles heatmap mode. Usage: ::heatmaps [0 = off, 1 = " +
                "on]", (p,
                        args) -> {
            final Integer value = Integer.valueOf(args[0]);
            final int distance = 16383;//Integer.valueOf(args[1]);
            final boolean bool = value == 1;
            p.setHeatmap(bool);
            p.setHeatmapRenderDistance(distance);
            p.send(new Heatmap(bool));
        });

        new Command(PlayerPrivilege.DEVELOPER, "completetask", "Completes a slayer task.", (p, args) -> {
            Assignment assignment = p.getSlayer().getAssignment();
            if (assignment == null) {
                p.sendMessage("No assignment to complete.");
                return;
            }

            assignment.setAmount(0);
            p.getSlayer().finishAssignment();
        });
        new Command(PlayerPrivilege.DEVELOPER, "task", "Choose a slayer task.", (p, args) -> {
            List<SlayerTask> tasks = new ArrayList<>();
            tasks.addAll(Arrays.asList(ObjectArrays.concat(RegularTask.VALUES, BossTask.VALUES, SlayerTask.class)));
            tasks.sort(Comparator.comparing(Object::toString));
            final ArrayList<String> names = new ArrayList<>();
            for (final SlayerTask task : tasks) {
                names.add(task.getTaskName());
            }
            p.getDialogueManager().start(new OptionsMenuD(p, "Select the task to receive",
                    names.toArray(new String[0])) {
                @Override
                public void handleClick(final int slotId) {
                    if (slotId >= tasks.size()) {
                        return;
                    }
                    final SlayerTask task = tasks.get(slotId);
                    //noinspection Convert2Lambda
                    player.sendInputInt("Enter kill count requirement:", new CountDialogue() {
                        @Override
                        public void run(int amount) {
                            final Assignment assignment = new Assignment(player, player.getSlayer(), task,
                                    task.getEnumName(), amount, amount, player.getSlayer().getMaster());
                            p.getSlayer().setAssignment(assignment);
                            p.getDialogueManager().start(new Dialogue(p, p.getSlayer().getMaster().getNpcId()) {
                                @Override
                                public void buildDialogue() {
                                    npc("Your new task is to kill " + assignment.getAmount() + " " + assignment.getTask().toString() + ".");
                                }
                            });
                        }
                    });
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });


        new Command(PlayerPrivilege.PLAYER, "duel", "Teleport to duel arena.", (p, args) -> {
            if (p.isLocked()) {
                return;
            }
            final Teleport teleport = new Teleport() {
                @Override
                public TeleportType getType() {
                    return TeleportType.REGULAR_TELEPORT;
                }

                @Override
                public Location getDestination() {
                    return new Location(3366, 3266, 0);
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
                    return 3;
                }

                @Override
                public Item[] getRunes() {
                    return null;
                }

                @Override
                public int getWildernessLevel() {
                    return WILDERNESS_LEVEL;
                }

                @Override
                public boolean isCombatRestricted() {
                    return UNRESTRICTED;
                }
            };
            teleport.teleport(p);
        });
        new Command(PlayerPrivilege.SUPPORT, "si", "Teleport to the staff area.", (p, args) -> {
            if (p.isLocked()) {
                return;
            }
            final Teleport teleport = new Teleport() {
                @Override
                public TeleportType getType() {
                    return TeleportType.REGULAR_TELEPORT;
                }

                @Override
                public Location getDestination() {
                    return new Location(2078 ,7840 , 0);
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
                    return 3;
                }

                @Override
                public Item[] getRunes() {
                    return null;
                }

                @Override
                public int getWildernessLevel() {
                    return WILDERNESS_LEVEL;
                }

                @Override
                public boolean isCombatRestricted() {
                    return UNRESTRICTED;
                }
            };
            teleport.teleport(p);
        });
        new Command(PlayerPrivilege.PLAYER, "train", "Teleport to rock crabs.", (p, args) -> {
            if (p.isLocked()) {
                return;
            }
            final Teleport teleport = new Teleport() {
                @Override
                public TeleportType getType() {
                    return TeleportType.REGULAR_TELEPORT;
                }

                @Override
                public Location getDestination() {
                    return new Location(2707, 3705, 0);
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
                    return 3;
                }

                @Override
                public Item[] getRunes() {
                    return null;
                }

                @Override
                public int getWildernessLevel() {
                    return WILDERNESS_LEVEL;
                }

                @Override
                public boolean isCombatRestricted() {
                    return UNRESTRICTED;
                }
            };
            teleport.teleport(p);
        });

        new Command(PlayerPrivilege.PLAYER, "edge", "Teleport to edgeville.", (p, args) -> {
            if (p.isLocked()) {
                return;
            }
            SpellbookTeleport.EDGE_TELEPORT.teleport(p);
        });

        new Command(PlayerPrivilege.PLAYER, "shops", "Opens the universal shop interface.", (p, args) -> {
            if (p.isLocked() || p.getActionManager().wasInCombatThisTick()) {
                return;
            }
            UniversalShopInterface.openInterfaceToTab(p, 0);
        });

        new Command(PlayerPrivilege.PLAYER, "home", "Teleport home.", (p, args) -> {
            if (p.isLocked()) {
                return;
            }
            PlayerPrivilege privilege = p.getPrivilege();
            final boolean adminPlus = privilege.ordinal() >= PlayerPrivilege.ADMINISTRATOR.ordinal();
            if (!adminPlus) {
                SpellbookTeleport.HOME_TELEPORT.teleport(p);
            } else {
                final Teleport teleport = new Teleport() {
                    @Override
                    public TeleportType getType() {
                        return TeleportType.NEAR_REALITY_PORTAL_TELEPORT;
                    }

                    @Override
                    public Location getDestination() {
                        return new Location(3087, 3490, 0);
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
                        return 3;
                    }

                    @Override
                    public Item[] getRunes() {
                        return null;
                    }

                    @Override
                    public int getWildernessLevel() {
                        return 100;
                    }

                    @Override
                    public boolean isCombatRestricted() {
                        return false;
                    }
                };
                teleport.teleport(p);
            }
        });
        new Command(PlayerPrivilege.PLAYER, "event", "Teleport to event boss.", (p, args) -> {
            if (p.isLocked()) {
                return;
            }
                final Teleport teleport = new Teleport() {
                    @Override
                    public TeleportType getType() {
                        return TeleportType.NEAR_REALITY_PORTAL_TELEPORT;
                    }

                    @Override
                    public Location getDestination() {
                        return new Location(3360, 7057, 0);
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
                        return 0;
                    }

                    @Override
                    public boolean isCombatRestricted() {
                        return false;
                    }
                };
                teleport.teleport(p);
        });

        new Command(PlayerPrivilege.DEVELOPER, "spawning", (p, args) -> {
            try {
                final AllocatedArea area = MapBuilder.findEmptyChunk(8, 8);
                final DynamicArea dynamicArea = new DynamicArea(area, 0, 0) {
                    @Override
                    public void enter(Player player) {
                    }

                    @Override
                    public void leave(Player player, boolean logout) {
                    }

                    @Override
                    public String name() {
                        return "Spawning area";
                    }

                    @Override
                    public Location onLoginLocation() {
                        return new Location(3222, 3219, 0);
                    }

                    @Override
                    public void constructed() {
                        p.setLocation(new Location((area.getChunkX() + 4) << 3, (area.getChunkY() + 4) << 3, 0));
                    }

                    @Override
                    public void constructRegion() {
                        if (constructed) {
                            return;
                        }
                        GlobalAreaManager.add(this);
                        try {
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; y++) {
                                    MapBuilder.copySquare(area, 1, 396, 441, 0, x + area.getChunkX(),
                                            y + area.getChunkY(), 0, 0);
                                }
                            }
                        } catch (OutOfBoundaryException e) {
                            log.error("", e);
                        }
                        constructed = true;
                        constructed();
                    }
                };
                dynamicArea.constructRegion();
            } catch (OutOfSpaceException e) {
                log.error("", e);
            }
        });
        new Command(PlayerPrivilege.DEVELOPER, "toggleoptions", (p, args) -> {
            p.setUpdatingNPCOptions(!p.isUpdatingNPCOptions());
            p.setUpdateNPCOptions(true);
        });

        new Command(PlayerPrivilege.FORUM_MODERATOR, new String[]{"unlimitedrunes", "unlrunes", "runes"}, "Grants you " +
                "unlimited " +
                "runes.", (p, args) -> {
            if (!isLiveEligible(p, PlayerPrivilege.DEVELOPER, PlayerPrivilege.FORUM_MODERATOR))
                return;
            p.getVarManager().sendBit(4145, 1);
            p.sendMessage(Colour.RS_GREEN.wrap("Fountain of Rune effect activated - no runes are required, and you " +
                    "get no base experience for casting spells."));
        });

        new Command(PlayerPrivilege.DEVELOPER, "wave", (p, args) -> {
            if (!p.inArea("Fight caves")) {
                p.sendMessage("You must be in fight caves to do this.");
                return;
            }
            final FightCaves caves = (FightCaves) p.getArea();
            caves.skip(parseInt(args[0]));
        });

        new Command(PlayerPrivilege.DEVELOPER, new String[]{"update", "shutdown"}, (p, args) -> {
            if (!isOwner(p)) {
                p.sendMessage("You are not authorized to use this command!");
                return;
            }
            p.sendInputInt("How many ticks until server shutdown?",
                    value -> p.getDialogueManager().start(new Dialogue(p) {
                        @Override
                        public void buildDialogue() {
                            options("Shut the server down in " + TimeUnit.TICKS.toSeconds(value) + " seconds?",
                                    new DialogueOption("Shut it down.", () -> World.startUpdateTimer(value)),
                                    new DialogueOption("Keep it" +
                                            " running."));
                        }
                    }));
        });
        new Command(PlayerPrivilege.DEVELOPER, new String[]{"killshutdown", "killupdate"}, (p, args) -> {
            if (!isOwner(p)) {
                p.sendMessage("You are not authorized to use this command!");
            } else World.killShutdown();
        });
        new Command(PlayerPrivilege.DEVELOPER, "xp", "Sets your experience modifier to suggested value(s)",
                (p, args) -> {
                    try {
                        final int combat = Integer.parseInt(args[0]);
                        final int skilling = Integer.parseInt(args[1]);
                        if (combat < 1 || skilling < 1) {
                            p.sendMessage("Minimum experience rate value permitted is 1!");
                            return;
                        }
                        if (combat > 1000 || skilling > 1000) {
                            p.sendMessage("Maximum experience rate value permitted is 1000!");
                            return;
                        }
                        p.setExperienceMultiplier(combat, skilling);
                        p.sendMessage("Experience rate set to x" + combat + " & x" + skilling + ".");
                        GameInterface.GAME_NOTICEBOARD.open(p);
                    } catch (final Exception e) {
                        e.printStackTrace();
                        p.sendMessage("Format is ::xp combat_rate_value skilling_rate_value");
                    }
                });
        new Command(PlayerPrivilege.ADMINISTRATOR, "attr", (p, args) -> {
            final String attr = StringUtilities.compile(args, 0, args.length, ' ');
            p.sendMessage("Value for attr: " + attr + ", " + p.getAttributes().get(attr));
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "setattr", (p, args) -> p.getTemporaryAttributes().put(args[0],
                args[1]));
        /*new Command(Privilege.SPAWN_ADMINISTRATOR, "doublexp", (p, args) -> {
            Constants.BOOSTED_XP = !Constants.BOOSTED_XP;
            p.sendMessage("Boosted xp: " + Constants.BOOSTED_XP);
        });

        new Command(Privilege.SPAWN_ADMINISTRATOR, "setboostedxp", (p, args) -> {
            p.sendInputInt("Set the xp boost % (eg. 50 for 1.5x xp) to?", amount -> {
                Constants.BOOSTED_XP_MODIFIER = amount;
                val boost = (1F + Constants.BOOSTED_XP_MODIFIER / 100D);
                p.sendMessage("XP boost set to " + boost + "x (" + Constants.BOOSTED_XP_MODIFIER + "%).");
            });
        });*/
        new Command(PlayerPrivilege.DEVELOPER, "ironman", "Sets your ironman mode", (p, args) -> {
            final String rights = args[0];
            final String name = StringUtilities.compile(args, 1, args.length, ' ');
            World.getPlayer(name).ifPresent(a -> {
                GameMode mode;
                if (rights.startsWith("reg") || rights.startsWith("ironman")) {
                    mode = GameMode.STANDARD_IRON_MAN;
                } else if (rights.startsWith("ult")) {
                    mode = GameMode.ULTIMATE_IRON_MAN;
                } else if (rights.startsWith("hard")) {
                    mode = GameMode.HARDCORE_IRON_MAN;
                } else if (rights.startsWith("group")) {
                    mode = GameMode.GROUP_IRON_MAN;
                } else {
                    mode = GameMode.REGULAR;
                }
                UserPlayerHandler.INSTANCE.updateGameMode(a, mode, (success) -> Unit.INSTANCE);
            });
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "worldboost", "Lets you choose boost to active.", (p, args) -> {
            int length = XamphurBoost.VALUES.length;
            String[] names = new String[length];
            for (int i = 0; i < length; i++) {
                names[i] = XamphurBoost.VALUES[i].getMssg();
            }

            p.getDialogueManager().start(new OptionsMenuD(p, "Choose a boost", names) {
                @Override
                public void handleClick(int slotId) {
                    p.sendInputInt("Enter hours of the boost.", hours -> {
                        if (hours <= 0) {
                            return;
                        }
                        XamphurHandler.activateBoost(XamphurBoost.VALUES[slotId], hours);
                    });
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });

        new Command(PlayerPrivilege.ADMINISTRATOR, "removeboost", "Lets you choose a boost to remove.", (p, args) -> {
            int length = XamphurBoost.VALUES.length;
            String[] names = new String[length];
            for (int i = 0; i < length; i++) {
                names[i] = XamphurBoost.VALUES[i].getMssg();
            }

            p.getDialogueManager().start(new OptionsMenuD(p, "Choose a boost to remove", names) {
                @Override
                public void handleClick(int slotId) {
                    WorldBoostType type = XamphurBoost.VALUES[slotId];
                    List<WorldBoost> activeBoosts = World.getWorldBoosts().stream().filter(it -> it.getBoostType() == type).toList();

                    if(activeBoosts.size() != 0) {
                        for(WorldBoost boost: activeBoosts)
                            World.getWorldBoosts().remove(boost);

                        for (Player player : World.getPlayers())
                            ServerEventsInterface.update(player);

                        player.sendMessage("Deactivated all boosts of type: " + type.getMssg());
                    } else {
                        player.sendMessage("No valid boosts of this type were found to be active.");
                    }
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(PlayerPrivilege.DEVELOPER, "removehome", "Removes home attribute from a player", (p, args) -> {
            final String name = StringUtilities.compile(args, 0, args.length, ' ');
            World.getPlayer(name).ifPresent(a -> {
                a.getAttributes().remove("HOME_TELEPORT");
                p.sendMessage("Removed home teleport from " + a.getName());
            });
        });
        new Command(PlayerPrivilege.DEVELOPER, "member", "Sets your member rank", (p, args) -> {
            final String name = StringUtilities.compile(args, 0, args.length, ' ');
            World.getPlayer(name).ifPresent(a -> {
                int length = MemberRank.values.length;
                String[] names = new String[length];
                for (int i = 0; i < length; i++) {
                    names[i] = MemberRank.values[i].toString();
                }

                p.getDialogueManager().start(new OptionsMenuD(p, "Choose a rank", names) {
                    @Override
                    public void handleClick(int slotId) {
                        if (slotId == length - 1) {
                            a.putBooleanAttribute("amascut_donator", true);
                            a.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
                        } else {
                            a.putBooleanAttribute("amascut_donator", false);
                            a.setMemberRank(MemberRank.values[slotId]);
                        }
                    }

                    @Override
                    public boolean cancelOption() {
                        return true;
                    }
                });
            });
        });
        new Command(PlayerPrivilege.DEVELOPER, "shufflepids", "Shuffles all of the players' PIDs", (p, args) -> {
            World.shufflePids();
            p.sendMessage("Your new PID is: " + p.getPid());
        });
        new Command(PlayerPrivilege.DEVELOPER, "questpoints", "Sets your quest points to the defined value.", (p,
                                                                                                               args) -> {
            p.setQuestPoints(Math.max(0, parseInt(args[0])));
            p.refreshQuestPoints();
        });
        new Command(PlayerPrivilege.DEVELOPER, "slayerpoints", "Sets your slayer points to the defined value.", (p,
                                                                                                                 args) -> {
            p.getSlayer().setSlayerPoints(parseInt(args[0]), true);
        });
        new Command(PlayerPrivilege.DEVELOPER, "loyaltypoints", "Sets your loyalty points to the defined value.", (p,
                                                                                                                 args) -> {
            p.getLoyaltyManager().setLoyaltyPoints(parseInt(args[0]));
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "checkarea", (p, args) -> {
            final RegionArea area = GlobalAreaManager.getArea(p);
            if (area == null) {
                p.sendMessage("Currently not in any defined area.");
                return;
            }
            p.sendMessage("Checked current area: " + area.name());
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "area", (p, args) -> {
            final RegionArea area = p.getArea();
            if (area == null) {
                p.sendMessage("Currently not in any defined area.");
                return;
            }
            p.sendMessage("Current area: " + area.name());
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "subareas", (p, args) -> {
            final RegionArea area = p.getArea();
            if (area == null) {
                p.sendMessage("Currently not in any defined area.");
                return;
            }
            p.sendMessage("Current area: " + area.name());
            p.sendMessage("Sub areas: " + area.getSubAreas());
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "areas", (p, args) -> {
            World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(target -> {
                final RegionArea area = target.getArea();
                if (area == null) {
                    p.sendMessage("Currently not in any defined areas.");
                    return;
                }
                final List<RegionArea> areas = new ArrayList<>();
                RegionArea extension = area;
                while (extension.getSuperArea() != null && extension.getSuperArea().inside(p.getLocation())) {
                    extension = extension.getSuperArea();
                }
                areas.add(extension);
                /*RegionArea a;
                while (!extension.getExtendAreas().isEmpty()) {
                    a = extension;
                    for (int i = extension.getExtendAreas().size() - 1; i >= 0; i--) {
                        final RegionArea nextPick = extension.getExtendAreas().get(i);
                        if (nextPick.inside(p.getLocation())) {
                            extension = nextPick;
                            areas.add(extension);
                            break;
                        }
                    }
                    if (extension == a) {
                        break;
                    }
                }*/
                p.sendMessage("Current areas: " + areas);
            });
        });
        new Command(PlayerPrivilege.DEVELOPER, "setlevelother", "Set one of your own levels. Usage: ::setlevelother " +
                "[Optional 'temp'] [Skillname or id] [Level]", (p, args) -> {
            p.sendInputName("Whose levels to change?", n -> {
                final Optional<Player> target = World.getPlayer(n);
                if (!target.isPresent()) {
                    p.sendMessage("User not online.");
                    return;
                }
                try {
                    final Player t = target.get();
                    final boolean temporary = args[0].equals("temp");
                    final boolean isNumber = NumberUtils.isCreatable(args[temporary ? 1 : 0]);
                    final EnumDefinitions e = EnumDefinitions.get(680);
                    if (isNumber) {
                        final Integer number = Integer.valueOf(args[temporary ? 1 : 0]);
                        if (number < 0 || number >= e.getSize()) {
                            p.sendMessage("Invalid skill id of " + number + ", valid values are 0-" + e.getSize() +
                                    ".");
                            return;
                        }
                        if (temporary) {
                            final int level = Math.min(255, Math.max(0, parseInt(args[2])));
                            t.getSkills().setLevel(number, level);
                            t.log(LogLevel.INFO, Skills.getSkillName(number) + " has been temporarily boosted to " +
                                    "level " + level + " by " + p.getName() + ".");
                            t.sendMessage(Skills.getSkillName(number) + " has been temporarily boosted to level " + level + ".");
                            p.sendMessage(Skills.getSkillName(number) + " has been temporarily boosted to level " + level + " for " + t.getName() + ".");
                            t.getAppearance().resetRenderAnimation();
                        } else {
                            final int level = Math.min(99, Math.max(1, parseInt(args[1])));
                            t.getSkills().setSkill(number, level, Skills.getXPForLevel(level));
                            t.log(LogLevel.INFO, Skills.getSkillName(number) + " has been set to level " + level + " " +
                                    "by " + p.getName() + ".");
                            t.sendMessage(Skills.getSkillName(number) + " has been set to level " + level + ".");
                            p.sendMessage(Skills.getSkillName(number) + " has been set to level " + level + " for " + t.getName() + ".");
                            t.getAppearance().resetRenderAnimation();
                        }
                    } else {
                        final String name = args[temporary ? 1 : 0].toLowerCase();
                        for (int i = e.getSize() - 1; i >= 0; i--) {
                            final String skillName = e.getStringValue(i);
                            if (skillName.toLowerCase().startsWith(name)) {
                                if (temporary) {
                                    final int level = Math.min(255, Math.max(0, parseInt(args[2])));
                                    t.getSkills().setLevel(i, level);
                                    t.log(LogLevel.INFO,
                                            skillName + " has been temporarily boosted to level " + level + " by " + p.getName() + ".");
                                    t.sendMessage(skillName + " has been temporarily boosted to level " + level + ".");
                                    p.sendMessage(skillName + " has been temporarily boosted to level " + level + " " +
                                            "for " + t.getName() + ".");
                                    t.getAppearance().resetRenderAnimation();
                                } else {
                                    final int level = Math.min(99, Math.max(1, parseInt(args[1])));
                                    t.getSkills().setSkill(i, level, Skills.getXPForLevel(level));
                                    t.log(LogLevel.INFO,
                                            skillName + " has been set to level " + level + " by " + p.getName() + ".");
                                    t.sendMessage(skillName + " has been set to level " + level + ".");
                                    p.sendMessage(skillName + " has been set to level " + level + " for " + t.getName() + ".");
                                    t.getAppearance().resetRenderAnimation();
                                }
                                return;
                            }
                        }
                    }
                } catch (final Exception e) {
                    p.sendMessage("Invalid syntax. Use command as: ;;setlevelother [Optional 'temp'] [Skillname or " +
                            "id] [Level]");
                }
            });
        });
        new Command(PlayerPrivilege.FORUM_MODERATOR, "setlevel", "Set one of your own levels. Usage: ::setlevel [Optional " +
                "'temp']" +
                " [Skillname or id] [Level]", (p, args) -> {
            try {
                if(!isLiveEligible(p, PlayerPrivilege.DEVELOPER, PlayerPrivilege.FORUM_MODERATOR)) {
                    return;
                }
                final boolean temporary = args[0].equals("temp");
                final boolean isNumber = NumberUtils.isCreatable(args[temporary ? 1 : 0]);
                final EnumDefinitions e = EnumDefinitions.get(680);
                if (isNumber) {
                    final Integer number = Integer.valueOf(args[temporary ? 1 : 0]);
                    if (number < 0 || number >= e.getSize()) {
                        p.sendMessage("Invalid skill id of " + number + ", valid values are 0-" + e.getSize() + ".");
                        return;
                    }
                    if (temporary) {
                        final int level = Math.min(255, Math.max(0, parseInt(args[2])));
                        p.getSkills().setLevel(number, level);
                        p.sendMessage(Skills.getSkillName(number) + " has been temporarily boosted to level " + level + ".");
                        p.getAppearance().resetRenderAnimation();
                    } else {
                        final int level = Math.min(99, Math.max(1, parseInt(args[1])));
                        p.getSkills().setSkill(number, level, Skills.getXPForLevel(level));
                        p.sendMessage(Skills.getSkillName(number) + " has been set to level " + level + ".");
                        p.getAppearance().resetRenderAnimation();
                    }
                } else {
                    final String name = args[temporary ? 1 : 0].toLowerCase();
                    for (int i = e.getSize() - 1; i >= 0; i--) {
                        final String skillName = e.getStringValue(i);
                        if (skillName.toLowerCase().startsWith(name)) {
                            if (temporary) {
                                final int level = Math.min(255, Math.max(0, parseInt(args[2])));
                                p.getSkills().setLevel(i, level);
                                p.sendMessage(skillName + " has been temporarily boosted to level " + level + ".");
                                p.getAppearance().resetRenderAnimation();
                            } else {
                                final int level = Math.min(99, Math.max(1, parseInt(args[1])));
                                p.getSkills().setSkill(i, level, Skills.getXPForLevel(level));
                                p.sendMessage(skillName + " has been set to level " + level + ".");
                                p.getAppearance().resetRenderAnimation();
                            }
                            return;
                        }
                    }
                }
            } catch (final Exception e) {
                p.sendMessage("Invalid syntax. Use command as: ;;setlevel [Optional 'temp'] [Skillname or id] [Level]");
            }
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "nametag", (p, args) -> {
            final int index = parseInt(args[0]);
            final String tag = StringUtilities.compile(args, 1, args.length, ' ');
            p.setNametag(index, tag);
        });
        new Command(PlayerPrivilege.FORUM_MODERATOR, "kick", "Disconnects a user.", (p, args) -> {
            if(!isLiveEligible(p, PlayerPrivilege.SUPPORT, PlayerPrivilege.FORUM_MODERATOR))
                return;

            final Optional<Player> t = World.getPlayer(StringUtilities.compile(args, 0, args.length, ' '));
            if (t.isEmpty()) {
                p.sendMessage("Player was not found.");
                return;
            }
            final Player target = t.get();
            if(isOwner(target) && !p.getPrivilege().inherits(PlayerPrivilege.DEVELOPER)) {
                p.sendMessage("You cannot kick this user");
                return;
            }
            target.log(LogLevel.INFO, "Forcefully kicked by " + p.getName() + ".");
            target.logout(true);
            p.sendMessage("Successfully kicked <col=C22731>" + target.getUsername() + "</col>!");
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "unlock", (p, args) -> {
            final Player target = World.getPlayerByUsername(String.valueOf(args[0]));
            if (target == null) {
                p.sendMessage("Player was not found.");
                return;
            }
            target.unlock();
            p.sendMessage("Target unlocked.");
        });
        new Command(PlayerPrivilege.DEVELOPER, "bigpacket", (p, args) -> {
            ThreadLocalRandom r = ThreadLocalRandom.current();
            byte[] bytes = new byte[250];
            for (int i = 0; i < NetworkConstants.MAX_OUTBOUND_PACKETS_PER_TICK; i++) {
                r.nextBytes(bytes);
                String s = new String(bytes);
                p.sendMessage(s);
            }
        });
        new Command(PlayerPrivilege.DEVELOPER, "killme", (p, args) -> {
            p.applyHit(new Hit(p.getHitpoints(), HitType.REGULAR));
        });
        new Command(PlayerPrivilege.DEVELOPER, "poison", (p, args) -> {
            p.getToxins().applyToxin(ToxinType.POISON, parseInt(args[0]));
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "printmasks", (p, args) -> {
            final int val = parseInt(args[0]);
            log.info("Masks for value {}: {}", val, AccessMask.getBuilder(val, false));
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "duration", (p, args) -> {
            final int anim = parseInt(args[0]);
            final AnimationDefinitions defs = AnimationDefinitions.get(anim);
            p.sendMessage("Duration: " + defs.getDuration());
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "objvar", (p, args) -> {
            int varbit = ObjectDefinitions.get(parseInt(args[0])).getVarbit();
            if (varbit == -1) {
                varbit = ObjectDefinitions.get(parseInt(args[0])).getVarp();
                if (varbit == -1) {
                    p.sendMessage("No varps or varbits found for that object.");
                    return;
                }
                p.sendMessage("Varp for " + args[0] + " is " + varbit);
                p.getVarManager().sendVar(varbit, 1);
            } else {
                p.sendMessage("Varbit for " + args[0] + " is " + varbit);
                p.getVarManager().sendBit(varbit, 1);
            }
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "extract", (p, args) -> {
            new AnimationExtractor().extract();
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "tolerance", (p, args) -> {
            final int value = parseInt(args[0]);
            p.setMaximumTolerance(value == 1);
            p.sendMessage("Maximum tolerance set to: " + value);
        });
        new Command(PlayerPrivilege.PLAYER, "commands", (p, args) -> {
            final ArrayList<String> entries = new ArrayList<>();
            COMMANDS.values().stream().filter(distinctByKey(c -> c.name)).sorted().forEach(c -> {
                if (!c.eligible(p) && !isLiveEligible(p, c.privilege, PlayerPrivilege.FORUM_MODERATOR)) {
                    return;
                }
                if (c.description != null) {
                    final String[] lines = Book.splitIntoLine(c.description, 55);
                    entries.add(c.privilege.crown().getCrownTag() + "<col=ffff00> ::" + c.name);
                    entries.addAll(Arrays.asList(lines));
                }
            });
            Diary.sendJournal(p, "Commands list", entries);
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "testvarp", (p, args) -> {
            for (int i = 0; i < CollectionUtils.getIndiceSize(Indice.VARBIT_DEFINITIONS); i++) {
                final VarbitDefinitions def = VarbitDefinitions.get(i);
                if (def.getBaseVar() == parseInt(args[0])) {
                    log.info("Varbit: {}, from bitshift: {}, till bitshift: {}", i, def.getStartBit(), def.getEndBit());
                }
            }
        });
        new Command(PlayerPrivilege.PLAYER, "ping", "Sends a pulse to the client, after which the client will " +
                "respond to the server with your current FPS, GC count & the time it took to respond.", (p, args) -> {
            p.sendMessage(Colour.RS_RED.wrap("To view your ping, please click the world icon in the runelite sidebar."));
        });
        new Command(PlayerPrivilege.FORUM_MODERATOR, "hide", "Hides or unhides your character.", (p, args) -> {
            if(!isLiveEligible(p, PlayerPrivilege.ADMINISTRATOR, PlayerPrivilege.FORUM_MODERATOR))
                return;
            p.getAppearance().setInvisible(!p.isHidden());
            p.setHidden(!p.isHidden());
            p.setMaximumTolerance(p.isHidden());
            if (p.isHidden()) {
                p.sendMessage(Colour.RS_GREEN.wrap("You are now hidden from other players and monsters will not be " +
                        "aggressive towards you."));
            } else {
                p.sendMessage(Colour.RS_RED.wrap("You are no longer hidden from other players and monsters are now " +
                        "aggressive towards you again..."));
            }
        });
        new Command(PlayerPrivilege.DEVELOPER, "gamemode", "Change ironman mode of a player.", (p, args) -> {
            p.sendInputString("Whose Game Mode to change?", name -> {
                final Optional<Player> player = World.getPlayer(name);
                if (!player.isPresent()) {
                    p.sendMessage("That player is not online.");
                    return;
                }
                final Player targetPlayer = player.get();
                int length = GameMode.values.length;
                String[] names = new String[length];
                for (int i = 0; i < length; i++) {
                    names[i] = TextUtils.formatName(GameMode.values[i].name(), true, true);
                }

                p.getDialogueManager().start(new OptionsMenuD(p, "Choose a game mode", names) {
                    @Override
                    public void handleClick(int slotId) {
                        final GameMode m = GameMode.values[slotId];
                        p.getDialogueManager().start(new Dialogue(p) {
                            @Override
                            public void buildDialogue() {
                                plain("Set the Game Mode of player " + Colour.RS_GREEN.wrap(targetPlayer.getName()) + " " +
                                        "to " + Colour.RS_GREEN.wrap(m.toString().toLowerCase()) + "?");
                                options("Change the Game Mode?", new DialogueOption("Yes.", () -> {
                                    UserPlayerHandler.INSTANCE.updateGameMode(targetPlayer, m, (success) -> {
                                        if (success) {
                                            player.sendMessage("Your game mode has been changed to " + targetPlayer.getGameMode().toString().toLowerCase() + ".");
                                            p.sendMessage(targetPlayer.getName() + "'s game mode has been changed to " + m.toString().toLowerCase() + ".");
                                        } else {
                                            p.sendMessage("Could not set game-mode of "+targetPlayer.getName()+" due to API failure.");
                                        }
                                        return Unit.INSTANCE;
                                    });
                                }), new DialogueOption("No."));
                            }
                        });
                    }

                    @Override
                    public boolean cancelOption() {
                        return true;
                    }
                });
            });
        });
        new Command(PlayerPrivilege.DEVELOPER, "rights", "Sets your character rights to the specified rights. Valid " +
                "arguments: player/nor, mod, admin", (p, args) -> {
            p.sendInputString("Whose rank to change?", name -> {
                final Optional<Player> player = World.getPlayer(name);
                if (player.isEmpty()) {
                    p.sendMessage("That player is not online.");
                    return;
                }
                final Player targetPlayer = player.get();
                p.sendInputString("What rank to set them to?", rights -> {
                    PlayerPrivilege privilege = null;
                    if (rights.startsWith("player") || rights.startsWith("nor") || rights.startsWith("reg")) {
                        privilege = PlayerPrivilege.PLAYER;
                    } else if (rights.startsWith("mod")) {
                        privilege = PlayerPrivilege.MODERATOR;
                    } else if (rights.startsWith("spawn admin") || rights.startsWith("dev")) {
                        privilege = PlayerPrivilege.DEVELOPER;
                    } else if (rights.startsWith("admin")) {
                        privilege = PlayerPrivilege.ADMINISTRATOR;
                    } else if (rights.startsWith("global")) {
                        privilege = PlayerPrivilege.SENIOR_MODERATOR;
                    } else if (rights.startsWith("testing")) {
                        privilege = PlayerPrivilege.FORUM_MODERATOR;
                    } else if (rights.startsWith("sup")) {
                        privilege = PlayerPrivilege.SUPPORT;
                    } else if (rights.startsWith("youtube")) {
                        privilege = PlayerPrivilege.YOUTUBER;
                    } else if (rights.startsWith("hidden")) {
                        privilege = PlayerPrivilege.HIDDEN_ADMINISTRATOR;
                    }
                    if (privilege == null) {
                        p.sendMessage("Privilege by the name of " + rights + " not found.");
                        return;
                    }
                    final PlayerPrivilege priv = privilege;
                    p.getDialogueManager().start(new Dialogue(p) {
                        @Override
                        public void buildDialogue() {
                            plain("Set the rights of player " + Colour.RS_GREEN.wrap(targetPlayer.getName()) + " to " + Colour.RS_GREEN.wrap(priv.toString().toLowerCase()) + "?");
                            options("Change the rights?", new DialogueOption("Yes.", () -> {
                                targetPlayer.setPrivilege(priv);
                                targetPlayer.sendMessage("Your privileges have been changed to " + targetPlayer.getPrivilege().toString().toLowerCase() + ".");
                                p.sendMessage(targetPlayer.getName() + "'s privileges have been changed to " + priv.toString().toLowerCase() + ".");
                            }), new DialogueOption("No."));
                        }
                    });
                });
            });
        });
        new Command(PlayerPrivilege.PLAYER, "wiki", "Opens the OSRSWikia page requested.", (p, args) -> {
            final String page = "https://oldschool.runescape.wiki/w/";
            final String arguments = StringUtilities.compile(args, 0, args.length, '_');
            p.getPacketDispatcher().sendURL(page + arguments);
        });
        new Command(PlayerPrivilege.PLAYER, "benefits", "Opens the OSNR Wiki Benefits Page.", (p, args) -> {
            final String page = "https://docs.google.com/spreadsheets/d/12OU9VOdT5dFKDS1AXq1Nldl82LioMPtI6FkZ4LLbpis";
            p.getPacketDispatcher().sendURL(page);
        });
        new Command(PlayerPrivilege.DEVELOPER, "itemdef-p", (p, args) -> {
            final int id = parseInt(args[0]);
            final EnumDefinitions map = EnumDefinitions.get(id);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(ItemDefinitions.get(id)));
        });

        new Command(PlayerPrivilege.ADMINISTRATOR, "printenum", (p, args) -> {
            final int id = parseInt(args[0]);
            final EnumDefinitions map = EnumDefinitions.get(id);
            if (map.getValues() == null) {
                return;
            }
            final boolean itemn = args.length > 1 && args[1].equals("item");
            map.getValues().forEach((k, v) -> {
                if (itemn) {
                    System.out.println(k + ": " + ItemDefinitions.get((int) v).getName() + "(" + v + ")");
                } else {
                    System.out.println(k + ": " + v);
                }
            });
        });
        new Command(PlayerPrivilege.FORUM_MODERATOR, "copy", "Copies the requested player's inventory and equipment.",
                (p, args) -> {
                    if(!isLiveEligible(p, PlayerPrivilege.DEVELOPER, PlayerPrivilege.FORUM_MODERATOR)) {
                        return;
                    }
                    final StringBuilder bldr = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        bldr.append(args[i] + ((i == args.length - 1) ? "" : " "));
                    }
                    final String name = StringFormatUtil.formatString(bldr.toString());
                    final Player player = World.getPlayerByDisplayname(name);
                    if (player == null) {
                        p.sendMessage("Could not find player.");
                        return;
                    }
                    p.getInventory().setInventory(player.getInventory());
                    p.getEquipment().setEquipment(player.getEquipment());
                    p.getInventory().refreshAll();
                    p.getEquipment().refreshAll();
                    p.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
                    p.sendMessage("Inventory & Equipment copied from " + player.getPlayerInformation().getDisplayname() + ".");
                }).orAllowIf(__ -> GameConstants.WORLD_PROFILE.isBeta());
        new Command(PlayerPrivilege.DEVELOPER, "copyinv", "Copies the requested player's inventory.", (p, args) -> {
            final StringBuilder bldr = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                bldr.append(args[i] + ((i == args.length - 1) ? "" : " "));
            }
            final String name = StringFormatUtil.formatString(bldr.toString());
            final Player player = World.getPlayerByDisplayname(name);
            if (player == null) {
                p.sendMessage("Could not find player.");
                return;
            }
            p.getInventory().setInventory(player.getInventory());
            p.getInventory().refreshAll();
            p.sendMessage("Inventory copied from " + player.getPlayerInformation().getDisplayname() + ".");
        }).orAllowIf(__ -> GameConstants.WORLD_PROFILE.isBeta());
        new Command(PlayerPrivilege.DEVELOPER, "copyequipment", "Copies the requested player's equipment.",
                (p, args) -> {
                    final StringBuilder bldr = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        bldr.append(args[i] + ((i == args.length - 1) ? "" : " "));
                    }
                    final String name = StringFormatUtil.formatString(bldr.toString());
                    final Player player = World.getPlayerByDisplayname(name);
                    if (player == null) {
                        p.sendMessage("Could not find player.");
                        return;
                    }
                    p.getEquipment().setEquipment(player.getEquipment());
                    p.getEquipment().refreshAll();
                    p.sendMessage("Equipment copied from " + player.getPlayerInformation().getDisplayname() + ".");
                }).orAllowIf(__ -> GameConstants.WORLD_PROFILE.isBeta());
        new Command(PlayerPrivilege.ADMINISTRATOR, "region", (p, args) -> {
            final int regionId = parseInt(args[0]);
            final int x = (regionId >> 8) << 6;
            final int y = (regionId & 255) << 6;
            p.setLocation(new Location(x, y, p.getPlane()));
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "rdrops", (p, args) -> NPCDrops.init());
        //new Command(Privilege.ADMINISTRATOR, "rinfo", (p, args) -> RaidFloorOverviewD.open(p));
        new Command(PlayerPrivilege.DEVELOPER, "maxbank", "Sets your bank to a preset.",
                (p, args) -> BankPreset.setBank(p));
        new Command(PlayerPrivilege.FORUM_MODERATOR, "god", "Sets all your bonuses to 15000.", (p, arags) -> {
            if(!isLiveEligible(p, PlayerPrivilege.DEVELOPER, PlayerPrivilege.FORUM_MODERATOR))
                return;
            p.sendMessage("Your bonuses have been set to 15000.");
            for (int i = 0; i < 12; i++) {
                p.getBonuses().setBonus(i, 15000);
            }
        });
        new Command(PlayerPrivilege.DEVELOPER, "ca", "Complete all combat achievements", (p, arags) -> {
            CAType[] types = CAType.values();

            Arrays.stream(types)
                    .filter(type -> !type.isDisabled())
                    .forEach(type -> p.getCombatAchievements().completeSilent(type));
            p.getCombatAchievements().complete(CAType.INSECT_DEFLECTION);
        });
        new Command(PlayerPrivilege.DEVELOPER, "god2", "Sets all your bonuses to a specified amount.", (p, args) -> {
            for (int i = 0; i < 12; i++) {
                p.getBonuses().setBonus(i, Integer.parseInt(args[0]));
            }
        });
        new Command(PlayerPrivilege.DEVELOPER, "analytics", "Toggles analytics", (p, args) -> {
            Analytics.enabled = !Analytics.enabled;
            p.sendMessage("Analytics are now " + (Analytics.enabled ? "enabled" : "disabled") + ".");
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "raids", "Teleports you to raids recruiting board.",
                (p, args) -> p.setLocation(new Location(1246, 3562, 0)));
        new Command(PlayerPrivilege.ADMINISTRATOR, "enter", (p, args) -> {
            p.getConstruction().enterHouse(p.getConstruction().isBuildingMode());
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "leave", (p, args) -> {
            p.getConstruction().leaveHouse();
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "spellbook", "Switches your spellbook to the requested book. " +
                "Argument: " +
                "0-3/name of the spellbook.", (p, args) -> {
            final String arg = args[0].toLowerCase();
            if (arg.startsWith("r") || arg.startsWith("norm")) {
                p.getCombatDefinitions().setSpellbook(Spellbook.NORMAL, true);
            } else if (arg.startsWith("an")) {
                p.getCombatDefinitions().setSpellbook(Spellbook.ANCIENT, true);
            } else if (arg.startsWith("l")) {
                p.getCombatDefinitions().setSpellbook(Spellbook.LUNAR, true);
            } else if (arg.startsWith("ar")) {
                p.getCombatDefinitions().setSpellbook(Spellbook.ARCEUUS, true);
            } else {
                p.getCombatDefinitions().setSpellbook(Spellbook.getSpellbook(parseInt(args[0])), true);
            }
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "gfx", "Performs the requested graphics. Argument: id",
                (p, args) -> p.setGraphics(new Graphics(parseInt(args[0]))));
        new Command(PlayerPrivilege.DEVELOPER, "testgfx", "Performs the requested graphics. Argument: id",
                (p, args) -> {
                    WorldTasksManager.schedule(new TickTask() {
                        int start = parseInt(args[0]);
                        @Override
                        public void run() {
                            if(ticks % 5 == 0) {
                                p.sendMessage("Testing gfx: " + start + ticks % 5);
                                p.setGraphics(new Graphics(start + ticks % 5));
                            }
                            if(ticks % 5 + start > start + 50)
                                stop();

                            ticks++;
                        }
                    }, 0, 1);
                }
        );
        new Command(PlayerPrivilege.DEVELOPER, "spec", "Sets your special energy to 100 or requested value. " +
                "Arguments: " +
                "<Optional>amount", (p, args) -> {
            int amount = 100;
            if (args.length > 0) {
                amount = parseInt(args[0]);
            }
            p.getCombatDefinitions().setSpecialEnergy(amount);
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "sound",
                (p, args) -> p.getPacketDispatcher().sendSoundEffect(new SoundEffect(parseInt(args[0]), 1, 0, 0)));
        new Command(PlayerPrivilege.FORUM_MODERATOR, new String[]{"heal", "hitpoints", "hp"}, "Sets your health to your" +
                " max " +
                "or requested value. Argument: <Optional>amount", (p, args) -> {
            if(!isLiveEligible(p, PlayerPrivilege.ADMINISTRATOR, PlayerPrivilege.FORUM_MODERATOR))
                return;
            int amount = p.getSkills().getLevelForXp(SkillConstants.HITPOINTS);
            if (args.length > 0) {
                amount = parseInt(args[0]);
            }
            p.setHitpoints(amount);
            if (p.getPrayerManager().getPrayerPoints() < p.getSkills().getLevelForXp(SkillConstants.PRAYER)) {
                p.getPrayerManager().setPrayerPoints(p.getSkills().getLevelForXp(SkillConstants.PRAYER));
            }
            if (p.getCombatDefinitions().getSpecialEnergy() < 100) {
                p.getCombatDefinitions().setSpecialEnergy(100);
            }
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, new String[]{"pray", "prayer"}, "Sets your prayer to your max or " +
                "requested value. Argument: <Optional>amount", (p, args) -> {
            int amount = p.getSkills().getLevelForXp(SkillConstants.PRAYER);
            if (args.length > 0) {
                amount = parseInt(args[0]);
            }
            p.getPrayerManager().setPrayerPoints(amount);
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, new String[]{"run", "runenergy"}, "Sets your run energy to your " +
                "max or " +
                "requested value. Argument: <Optional>amount", (p, args) -> {
            int amount = 100;
            if (args.length > 0) {
                amount = parseInt(args[0]);
            }
            p.getVariables().forceRunEnergy(amount);
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "replenish", (p, args) -> {
            p.setHitpoints(1000000);
            p.getPrayerManager().setPrayerPoints(1000000);
            p.getCombatDefinitions().setSpecialEnergy(1000000);
            p.getVariables().forceRunEnergy(1000000);
        });
        new Command(PlayerPrivilege.DEVELOPER, "object", "Spawns an object underneath you. Arguments: id, " +
                "<Optional>type, <Optional>rotation", (p, args) -> {
//            if (!GameConstants.isOwner(p) && !p.inArea("Spawning area")) {
//                p.sendMessage("You can only spawn objects within the spawning area. ::spawning to enter.");
//                return;31858
//            }
            final int objectId = parseInt(args[0]);
            int type = 10;
            int rotation = 0;
            if (args.length > 1) {
                type = parseInt(args[1]);
            }
            if (args.length > 2) {
                rotation = parseInt(args[2]);
            }
            final ObjectDefinitions defs = ObjectDefinitions.get(objectId);
            /*if (defs != null) {
                if (defs.getTypes() == null) {
                    if (type != 10) {
                        type = 10;
                        p.sendMessage("Object " + objectId + " spawned with type " + type + ", as input type was
                        invalid.");
                    }
                } else {
                    if (!ArrayUtils.contains(defs.getTypes(), type)) {
                        type = defs.getTypes()[0];
                        p.sendMessage("Object " + objectId + " spawned with type " + type + ", as input type was
                        invalid.");
                    }
                }
            }*/
            if (objectId < 0) {
                World.removeObject(World.getObjectWithType(p.getLocation(), type));
            } else {
                World.spawnObject(new WorldObject(objectId, type, rotation, p.getLocation()));
            }
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "npc", "Spawns a NPC underneath you. Argument: id", (p, args) -> {
//            if (!GameConstants.isOwner(p) && !p.inArea("Spawning area")) {
//                p.sendMessage("You can only spawn NPCs within the spawning area. ::spawning to enter.");
//                return;
//            }
            World.spawnNPC(parseInt(args[0]), new Location(p.getLocation())).setSpawned(true);
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "npcperm", "Spawns a NPC underneath you. Argument: id", (p, args) -> {
            World.spawnNPC(parseInt(args[0]), new Location(p.getLocation()));
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "pnpc", "Transmogrifies you to a NPC. Argument: id", (p, args) -> {
            final Integer id = Integer.valueOf(args[0]);
            if (id >= 0 && NPCDefinitions.get(id) == null) {
                p.sendMessage("Invalid transformation.");
                return;
            }
            p.setAnimation(Animation.STOP);
            p.getAppearance().setNpcId(Math.max(-1, id));
            p.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        });
        new Command(PlayerPrivilege.DEVELOPER, "clog", (p, args) -> {
            final int itemId = parseInt(args[0]);
            final int amount = args.length > 1 ? parseInt(args[1]) : 1;
            Item item = new Item(itemId, amount);
            p.getCollectionLog().add(item);
            p.sendMessage("Added " + item.getName() + " to your collection log.");
        });
        new Command(PlayerPrivilege.FORUM_MODERATOR, "item", "Spawns an item in your inventory. If undefined, amount is set" +
                " to 1 " +
                "and charges are set to the default of said item. Arguments: id <Optional>amount <Optional>charges", (p,
                                                                                                                      args) -> {
            if(!(GameConstants.WORLD_PROFILE.isBeta() || GameConstants.WORLD_PROFILE.isDevelopment() )&& !p.getPrivilege().inherits(PlayerPrivilege.ADMINISTRATOR)) {
                p.sendMessage("You only have access to this command in beta worlds.");
                return;
            }
            final int itemId = parseInt(args[0]);
            final int amount = args.length > 1 ? parseInt(args[1]) : 1;
            final int charges = args.length > 2 ? parseInt(args[2]) : Math.max(0,
                    DegradableItem.getDefaultCharges(itemId, 0));
            p.getInventory().addItem(itemId, amount, charges);
        });
        new Command(PlayerPrivilege.FORUM_MODERATOR, "master", "Sets all your levels to 99.", (p, args) -> {

            if(!(GameConstants.WORLD_PROFILE.isBeta() || GameConstants.WORLD_PROFILE.isDevelopment() )&& !p.getPrivilege().inherits(PlayerPrivilege.DEVELOPER)) {
                p.sendMessage("You only have access to this command in beta worlds.");
                return;
            }
            if (p.getNumericAttribute("first_99_skill").intValue() == -1) {
                p.addAttribute("first_99_skill", 0);
            }
            for (int i = 0; i < 23; i++) {
                p.getSkills().setSkill(i, 99, 13034431);
            }
            p.getAppearance().resetRenderAnimation();
        });
        new Command(PlayerPrivilege.FORUM_MODERATOR, "unmaster", "Sets all your levels to 1.", (p, args) -> {

            if(!(GameConstants.WORLD_PROFILE.isBeta() || GameConstants.WORLD_PROFILE.isDevelopment() )&& !p.getPrivilege().inherits(PlayerPrivilege.DEVELOPER)) {
                p.sendMessage("You only have access to this command in beta worlds.");
                return;
            }
            p.getSkills().resetAll();
            p.getSkills().refresh();
            p.getAppearance().resetRenderAnimation();
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "teleloc",
                "Teleports you to one of the available teleportations.", (p, args) -> {
            final String query = StringUtilities.compile(args, 0, args.length, ' ').toLowerCase();
            final PortalTeleport[] teleports = PortalTeleport.values();
            for (final PortalTeleport teleport : teleports) {
                final String name = teleport.getSmallDescription().toLowerCase();
                if (name.startsWith(query)) {
                    teleport.teleport(p);
                    return;
                }
            }
        });
        new Command(PlayerPrivilege.FORUM_MODERATOR, "tele",
                "Teleports you to the requested coordinates. Arguments: x y " + "<Optional>z", (p, args) -> {

            if(!isLiveEligible(p, PlayerPrivilege.ADMINISTRATOR, PlayerPrivilege.FORUM_MODERATOR))
                return;

            final String arg1 = args[0];
            final String arg2 = args[1];
            int x = 0;
            int y = 0;
            if (arg1.equalsIgnoreCase("region")) {
                final int regionId = parseInt(arg2);
                x = (regionId >> 8) << 6;
                y = (regionId & 255) << 6;
                p.setLocation(new Location(x, y, p.getPlane()));
                return;
            }
            if (arg1.startsWith("rx")) {
                x = parseInt(arg1.substring(2)) << 6;
            } else if (arg1.startsWith("cx")) {
                x = parseInt(arg1.substring(2)) << 3;
            } else {
                x = parseInt(arg1);
            }
            if (arg2.startsWith("ry")) {
                y = parseInt(arg2.substring(2)) << 6;
            } else if (arg2.startsWith("cy")) {
                y = parseInt(arg2.substring(2)) << 3;
            } else {
                y = parseInt(arg2);
            }
            final int plane = args.length > 2 ? parseInt(args[2]) : p.getPlane();
            p.setLocation(new Location(x, y, plane));
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "telespecific", (p, args) -> {
            final String arg1 = args[0];
            final String arg2 = args[1];
            int x;
            int y;
            x = (parseInt(args[0]) << 6) + (parseInt(args[1]) << 3);
            y = (parseInt(args[2]) << 6) + (parseInt(args[3]) << 3);
            p.setLocation(new Location(x, y, p.getPlane()));
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "intertext", (p, args) -> {
            String text = "";
            for (int i = 1; i < args.length; i++) {
                text += args[i] + ((i == args.length - 1) ? "" : " ");
            }
            p.getPacketDispatcher().sendComponentText(482, parseInt(args[0]), text);
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "objtypes", "Sends a game message about the valid types of the " +
                "requested" +
                " object. Argument: id", (p, args) -> {
            if (args.length < 1) {
                p.sendMessage("Invalid syntax: Use as ::objtypes objectId");
                return;
            }
            final int value = parseInt(args[0]);
            final ObjectDefinitions defs = ObjectDefinitions.get(value);
            if (defs.getTypes() == null) {
                p.sendMessage("Object types for " + defs.getName() + "(" + value + "): [10]");
            } else {
                p.sendMessage("Object types for " + defs.getName() + "(" + value + "): " + ArrayUtils.toString(defs.getTypes()));
            }
        });
        new Command(PlayerPrivilege.FORUM_MODERATOR, "itemn", "Displays a list of items that meet the requested name " +
                "criteria" +
                ". Argument: name", (p, args) -> {
            if(!(GameConstants.WORLD_PROFILE.isBeta() || GameConstants.WORLD_PROFILE.isDevelopment()) && !p.getPrivilege().inherits(PlayerPrivilege.ADMINISTRATOR)) {
                p.sendMessage("You only have access to this command in beta worlds.");
                return;
            }
            final ObjectArrayList<ItemDefinitions> listOfItems = new ObjectArrayList<>(50);
            final ObjectArrayList<String> listOfNames = new ObjectArrayList<>(50);
            final String name = StringUtilities.compile(args, 0, args.length, ' ');
            int characterCount = 0;
            for (final ItemDefinitions defs : ItemDefinitions.getDefinitions()) {
                if (defs == null) {
                    continue;
                }
                if(HIDDEN_ITEMS.contains(defs.getId()))
                    continue;
                if (defs.getName().toLowerCase().contains(name)) {
                    listOfItems.add(defs);
                    final String string = defs.getId() + " - " + defs.getName() + (defs.getNotedTemplate() > 0 ?
                            "(noted)" : "");
                    listOfNames.add(string);
                    characterCount += string.length();
                    //Cap it out at 30kb for the payload, gives enough room for the header and rest of the packet.
                    if (characterCount >= 30000) {
                        break;
                    }
                }
            }
            p.getDialogueManager().start(new OptionsMenuD(p, "Query: " + name + " (Click to spawn one)",
                    listOfNames.toArray(new String[0])) {
                @Override
                public void handleClick(int slotId) {
                    p.getInventory().addItem(new Item(listOfItems.get(slotId).getId()));
                    p.getDialogueManager().start(this);
                }

                @Override
                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "raidlist", (p, args) -> {
            final ObjectArrayList<Raid> list = new ObjectArrayList<>(Raid.existingRaidsMap.values());
            final ObjectArrayList<String> nameList = new ObjectArrayList<>();
            for (final Raid raid : list) {
                if (nameList.size() >= 128) {
                    break;
                }
                nameList.add(raid.getParty().getChannel().getOwner() + "'s raid");
            }
            p.getDialogueManager().start(new OptionsMenuD(p, "Select existing raid", nameList.toArray(new String[0])) {
                public void handleClick(final int slotId) {
                    final Raid raid = list.get(slotId);
                    p.setLocation(raid.getRespawnTile());
                }

                public boolean cancelOption() {
                    return true;
                }
            });
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "objectn", "Displays a list of objects that meet the requested " +
                "name " +
                "criteria. Argument: name", (p, args) -> {
            String name = "";
            for (int i = 0; i < args.length; i++) {
                name += args[i] + (i == args.length - 1 ? "" : " ");
            }
            final ArrayList<String> entries = new ArrayList<>();
            for (int i = 0; i < ObjectDefinitions.definitions.length; i++) {
                final ObjectDefinitions defs = ObjectDefinitions.get(i);
                if (defs == null) {
                    continue;
                }
                if (defs.getName().toLowerCase().contains(name)) {
                    entries.add(defs.getId() + " - " + defs.getName() + ", types: " + (defs.getTypes() == null ? "[10" +
                            "]" : ArrayUtils.toString(defs.getTypes())));
                }
            }
            Diary.sendJournal(p, "Query: " + name, entries);
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "npcn", "Displays a list of npcs that meet the requested name " +
                "criteria. " +
                "Argument: name", (p, args) -> {
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                name.append(args[i]).append(i == args.length - 1 ? "" : " ");
            }
            final ArrayList<String> entries = new ArrayList<>();
            for (final NPCDefinitions defs : NPCDefinitions.getDefinitions()) {
                if (defs == null) continue;
                if (defs.getName().toLowerCase().contains(name.toString())) {
                    entries.add(defs.getId() + " - " + defs.getName() + " (lvl-" + defs.getCombatLevel() + ")");
                }
            }
            Diary.sendJournal(p, "Query: " + name, entries);
        });
        new Command(PlayerPrivilege.SUPPORT, "players", "Displays a list of players online and their coordinates/area" +
                ".", (p
                , args) -> {
            final ArrayList<String> entries = new ArrayList<>(World.MAX_JOURNAL_PLAYERS);
            final ArrayList<Player> players = new ArrayList<>(World.getPlayers().size());
            players.addAll(World.getPlayers());
            players.sort((a, b) -> a.getPlayerInformation().getDisplayname().compareToIgnoreCase(b.getPlayerInformation().getDisplayname()));
            for (final Player player : players) {
                if (player == null) {
                    continue;
                }
                final StringBuilder sb =
                        new StringBuilder(player.getPrivilege().crown().getCrownTag()).append(player.getPlayerInformation().getDisplayname());
                final RegionArea area = player.getArea();
                sb.append(" - ").append(area != null ? area.name() : "Unknown");
                if (p.getPrivilege().eligibleTo(PlayerPrivilege.SENIOR_MODERATOR)) {
                    sb.append(" - (").append(player.getX()).append(", ").append(player.getY()).append(", ").append(player.getPlane()).append(")");
                }
                if (player.isOnMobile()) {
                    sb.append(" - Mobile");
                }
                entries.add(sb.toString().trim());
                if (entries.size() >= World.MAX_JOURNAL_PLAYERS) {
                    break;
                }
            }
            Diary.sendJournal(p, "Players online: " + World.getDisplayedPlayerCount(), entries);
        });
        new Command(PlayerPrivilege.SUPPORT, "rp", "Spawns a rotten potato.", (p, args) -> {
            if (!p.getInventory().checkSpace()) {
                return;
            }
            if (p.carryingItem(ItemId.ROTTEN_POTATO)) {
                p.sendMessage("You can only have one rotten potato.");
                return;
            }
            p.getInventory().addItem(ItemId.ROTTEN_POTATO, 1);
        });
        new Command(PlayerPrivilege.MODERATOR, "mobileplayers", "Displays a list of mobile players online and their " +
                "coordinates.", (p, args) -> {
            final ArrayList<String> entries = new ArrayList<>();
            int count = 0;
            for (final Player player : World.getPlayers()) {
                if (player == null || !player.isOnMobile()) {
                    continue;
                }
                count++;
                entries.add(player.getPlayerInformation().getDisplayname() + " (" + player.getX() + ", " + player.getY() + ", " + player.getPlane() + ")");
            }
            Diary.sendJournal(p, "Players online on mobile: " + count, entries);
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "overlay", "Sends an overlay of the requested id. Argument: id", (p,
                                                                                                                     args) -> {
            final int interfaceId = parseInt(args[0]);
            if (interfaceId == -1) {
                p.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
            } else {
                p.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, interfaceId);
            }
        });
        new Command(PlayerPrivilege.SUPPORT, "teletome", "Teleports the requested player to you. Usage: ::teletome playername", (p, args) -> {
            World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(t -> {
                if (!p.getPrivilege().inherits(PlayerPrivilege.DEVELOPER)) {
                    final Optional<Raid> raid = t.getRaid();
                    if (raid.isPresent() && !t.getPrivilege().inherits(PlayerPrivilege.ADMINISTRATOR)) {
                        p.sendMessage("You cannot teleport non-administrators into a raid.");
                        return;
                    }
                    if (p.getArea() instanceof Inferno && !t.getPrivilege().inherits(PlayerPrivilege.DEVELOPER)) {
                        p.sendMessage("You cannot teleport a player into the Inferno.");
                        return;
                    }
                }
                t.log(LogLevel.INFO, "Force teleported by " + p.getName() + " to " + p.getLocation() + ".");
                t.setLocation(p.getLocation());
            });
        });
        new Command(PlayerPrivilege.FORUM_MODERATOR, "teleto", "Teleport to a player. Usage: ::teleto player name", (p, args) -> {
            if(!isLiveEligible(p, PlayerPrivilege.SUPPORT, PlayerPrivilege.FORUM_MODERATOR))
                return;
            World.getPlayer(StringUtilities.compile(args, 0, args.length, ' ')).ifPresent(t -> {
                if (!p.getPrivilege().inherits(PlayerPrivilege.DEVELOPER)) {
                    final Optional<Raid> raid = t.getRaid();
                    if (raid.isPresent() && !p.getPrivilege().inherits(PlayerPrivilege.ADMINISTRATOR)) {
                        p.sendMessage("You cannot teleport to a player in a raid as a non-administrator.");
                        return;
                    }
                    if (t.getArea() instanceof Inferno && !p.getPrivilege().inherits(PlayerPrivilege.DEVELOPER)) {
                        p.sendMessage("You cannot teleport to a player in the Inferno.");
                        return;
                    }
                }
                t.log(LogLevel.INFO, p.getName() + " force teleported to you at " + t.getLocation() + ".");
                p.setLocation(t.getLocation());
            });
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "varbits", (p, args) -> {
            final int low = parseInt(args[0]);
            final int high = parseInt(args[1]);
            final int value = parseInt(args[2]);
            for (int index = low; index < high; index++) {
                p.getVarManager().sendBit(index, value);
            }
            p.sendMessage("set values from " + low + " to " + high + " with value: " + value);
        });

        new Command(PlayerPrivilege.PLAYER, new String[]{"droprate", "dr"}, "Displays your drop rate.", (p, args) -> {
            ExpConfigurations config = ExpConfigurations.of(p.getGameMode());
            int currentIndex = config.getExpConfigurationIndex(p.getCombatXPRate(), p.getSkillingXPRate());
            ExpConfiguration[] configurations = ExpConfigurations.of(p.getGameMode()).getConfigurations();
            ExpConfiguration configuration = configurations[currentIndex];
            double gameMode = configuration.getDropRateIncrease() / 100.0D;
            double donor = p.getMemberRank().getDR();
            double pin = p.getBooleanAttribute("drop_rate_pin_claimed") ? 0.05D : 0.0D;
            double pet = p.getBoonManager().hasBoon(AnimalTamer.class) && p.getPetId() != -1 ? 0.02D : 0.0D;
            double compCape = p.getCompletionistCapeDRBoost();
            p.sendMessage("DropRate: " + (int) ((gameMode + donor + pin + pet + compCape) * 100D) + "%. Mode: " + (int) (gameMode * 100D) + "%. Donor: " + (int) (donor * 100D) + "%. Pin: " + (int) (pin * 100D) + "%. Pet: " + (int) (pet * 100D) +"%");
        });

        new Command(PlayerPrivilege.DEVELOPER, new String[]{"b", "bank"}, "Opens the bank.",
                (p, args) -> GameInterface.BANK.open(p));
        new Command(PlayerPrivilege.DEVELOPER, "var", "Sends a varp of the requested id and value. Arguments: id " +
                "value", (p
                , args) -> p.getVarManager().sendVar(parseInt(args[0]), parseInt(args[1])));
        new Command(PlayerPrivilege.DEVELOPER, "prayers", (p, args) -> {
            p.getSettings().setSetting(Setting.RIGOUR, 1);
            p.getSettings().setSetting(Setting.AUGURY, 1);
            p.getSettings().setSetting(Setting.PRESERVE, 1);
        });

        new Command(PlayerPrivilege.PLAYER, new String[]{"coords", "mypos", "coord"}, "Informs the player about their" + " " + "coordinates. (Used for debugging)", (p, args) -> {
            final String coordsPrint = "Coords: " + p.getX() + ", " + p.getY() + "," + " " + p.getPlane() + ", region:" + p.getLocation().getRegionId();
            System.out.println(coordsPrint);
            p.sendMessage(coordsPrint);
        });
        new Command(PlayerPrivilege.SUPPORT, "deepcoords", "Informs the player about their coordinates in depth.", (p, args) ->
                p.sendMessage(
                        "Coords: " + p.getX() + ", " + p.getY() + ", " + p.getPlane() +
                                ", regionId: " + p.getLocation().getRegionId() +
                                ", cx: " + p.getLocation().getChunkX() +
                                ", cy: " + p.getLocation().getChunkY() +
                                ", rx: " + p.getLocation().getRegionX() +
                                ", ry: " + p.getLocation().getRegionY() +
                                ", cxir: " + (p.getLocation().getChunkX() & 7) +
                                ", cyir: " + (p.getLocation().getChunkY() & 7) +
                                ", xic: " + p.getLocation().getXInChunk() +
                                ", yic: " + p.getLocation().getYInChunk() +
                                ", xir: " + p.getLocation().getXInRegion() +
                                ", yir: " + p.getLocation().getYInRegion() +
                                ", hash: " + p.getLocation().getPositionHash() +
                                ", name: " + (p.getArea() != null ? p.getArea().name() : "Empty")
                ));
        new Command(PlayerPrivilege.ADMINISTRATOR, "empty", "Clears the player's inventory.", (p, args) -> p.getInventory().clear());
        new Command(PlayerPrivilege.DEVELOPER, "emotes", "Unlocks all of the emotes.", (p, args) -> {
            p.getAttributes().put("Thanksgiving 2019 event", true);
            p.addAttribute("Halloween event 2019", 1);
            p.getVarManager().sendVar(GIVE_THANKS_VARP, 1);
            p.getVarManager().sendBit(1000, 1);
            p.getVarManager().sendVar(HalloweenUtils.COMPLETED_VARP, 1);
            for (final Emote e : Emote.VALUES) {
                p.getEmotesHandler().unlock(e);
            }
        });
        new Command(PlayerPrivilege.DEVELOPER, "music", (p, args) -> {
            for (final int id : MusicHandler.VARP_IDS) {
                p.getMusic().getUnlockedTracks().put(id, -1);
            }
            p.getMusic().refreshListConfigs();
        });
        new Command(PlayerPrivilege.DEVELOPER, "diaries", (p, args) -> {
            for (final Diary[] diary : AchievementDiaries.ALL_DIARIES) {
                for (final Diary d : diary) {
                    if (d.autoCompleted()) continue;
                    p.getAchievementDiaries().finish(d);
                }
            }
        });
        new Command(PlayerPrivilege.DEVELOPER, "resetdiaries", (p, args) -> {
            for (final Diary[] diary : AchievementDiaries.ALL_DIARIES) {
                for (final Diary d : diary) {
                    if (d.autoCompleted()) continue;
                    p.getAchievementDiaries().reset(d);
                }
            }
        });

        new Command(PlayerPrivilege.DEVELOPER, "addslayerpoints", "Give slayer points to a player. Usage: " +
                "::addslayerpoints" +
                " points player name", (p, args) -> {
            final Integer points = Integer.valueOf(args[0]);
            final String name = StringUtilities.compile(args, 1, args.length, ' ');
            World.getPlayer(name).ifPresent(a -> {
                a.addAttribute("slayer_points", a.getNumericAttribute("slayer_points").intValue() + points);
                a.getSlayer().refreshSlayerPoints();
                p.sendMessage("Added slayer points to user " + name + "; Amount: " + points);
            });
        });
        new Command(PlayerPrivilege.DEVELOPER, "addloyaltypoints", "Give loyalty points to a player. Usage: " +
                "::addloyaltypoints points player name", (p, args) -> {
            final Integer points = Integer.valueOf(args[0]);
            final String name = StringUtilities.compile(args, 1, args.length, ' ');
            World.getPlayer(name).ifPresent(a -> {
                a.getLoyaltyManager().setLoyaltyPoints(a.getLoyaltyManager().getLoyaltyPoints() + points);
                p.sendMessage("Added loyalty points to user " + name + "; Amount: " + points);
            });
        });

        new Command(PlayerPrivilege.PLAYER, "checkowner", (p, args) -> {
            if (isOwner(p)) {
                p.setPrivilege(PlayerPrivilege.DEVELOPER);
                p.sendMessage("Rights restored to admin.");
            }
        });
        new Command(PlayerPrivilege.ADMINISTRATOR, "restock", (p, args) -> {
            Shop.restockAllShops();
        });
        new Command(PlayerPrivilege.PLAYER, "thread", "Opens a forum thread. Usage: ::thread thread_id", (p, args) -> {
            try {
                int thread = Integer.parseInt(args[0]);
                p.getPacketDispatcher().sendURL(GameConstants.SERVER_FORUMS_URL + "/thread/" + thread);
            } catch (NumberFormatException e) {
                p.sendMessage("The right usage of the thread command is be: ;;thread 1 for example.");
            }
        });
        new Command(PlayerPrivilege.PLAYER, new String[]{"staff", "staffonline", "onlinestaff"}, "List staff members currently online.", (p, args) -> {
            GameNoticeboardInterface.showStaffOnline(p);
        });
        new Command(PlayerPrivilege.PLAYER, new String[]{"vote", "voting"}, (p, args) -> {
            GameInterface.VOTE.open(p);
        });
        new Command(PlayerPrivilege.PLAYER, new String[]{"store", "donate"}, (p, args) -> {
            GameInterface.CREDIT_STORE.open(p);
        });
        new Command(PlayerPrivilege.PLAYER, new String[]{"rules"}, (p, args) -> {
            p.getPacketDispatcher().sendURL(GameConstants.SERVER_RULES_URL);
        });
        new Command(PlayerPrivilege.PLAYER, new String[]{"hs", "highscores", "highscore", "hiscores", "hiscore"}, (p, args) -> {
            GameInterface.HISCORES.open(p);
        });
        new Command(PlayerPrivilege.PLAYER, "advent", (p, args) -> {
            GameInterface.ADVENT_CALENDAR.open(p);
        });
        new Command(PlayerPrivilege.PLAYER, "discord", (p, args) -> {
            p.getPacketDispatcher().sendURL(GameConstants.DISCORD_INVITE);
        });
        new Command(PlayerPrivilege.PLAYER, "2fa", (p, args) -> {
            p.getPacketDispatcher().sendURL("https://near-reality.com/account");
        });
        new Command(PlayerPrivilege.PLAYER, "youtube", (p, args) -> {
            p.getPacketDispatcher().sendURL("https://www.youtube.com/user/NRPKOfficial");
        });
        new Command(PlayerPrivilege.PLAYER, "yell", "Sends a global message across the game.", (p, args) -> {
            if (SanctionPlayerExtKt.isMuted(p)) {
                p.sendMessage("You are currently muted.");
                return;
            }
            if (SanctionPlayerExtKt.isYellMuted(p)) {
                p.sendMessage("You are currently yell-muted.");
                return;
            }

            if (!p.isStaff() && !p.isMember() && !p.getPrivilege().equals(PlayerPrivilege.YOUTUBER)) {
                p.sendMessage("You need to be a donator in order to yell.");
                return;
            }
            if (!p.isStaff() && p.getVariables().getTime(TickVariable.YELL) > 0) {
                final int totalSeconds = (int) (p.getVariables().getTime(TickVariable.YELL) * 0.6F);
                final int seconds = totalSeconds % 60;
                final int minutes = totalSeconds / 60;
                p.sendMessage("You need to wait another " + (minutes == 0 ? (seconds + " seconds") : (minutes + " " +
                        "minutes")) + " until you can yell again.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (String arg : args) {
                sb.append(arg);
            }
            GameLogger.log(Level.INFO, () -> new GameLogMessage.Message.Yell(
                    Instant.Companion.now(),
                    p.getUsername(),
                    sb.toString()
            ));
            final MemberRank member = p.getMemberRank();
            final PlayerPrivilege privilege = p.getPrivilege();
            final GameMode gameMode = p.getGameMode();
            final StringBuilder bldr = new StringBuilder();
            int delay = 0;
            if (p.isStaff()) {
                bldr.append("<col=").append(privilege.getYellColor()).append("><shad=000000>");
                if (p.isMember()) {
                    bldr.append("[").append(privilege.crown().getCrownTag()).append(gameMode.crown().getCrownTag()).append(member.crown().getCrownTag()).append(p.getName()).append("]");
                } else {
                    bldr.append("[").append(privilege.crown().getCrownTag()).append(gameMode.crown().getCrownTag()).append(p.getName()).append("]");
                }
                bldr.append("</col></shad>: ");
            } else if (p.getPrivilege().equals(PlayerPrivilege.FORUM_MODERATOR)) {
                bldr.append("<col=").append(privilege.getYellColor()).append("><shad=000000>");
                if (p.isMember()) {
                    bldr.append("[").append(privilege.crown().getCrownTag()).append(gameMode.crown().getCrownTag()).append(member.crown().getCrownTag()).append(p.getName()).append("]");
                } else {
                    bldr.append("[").append(privilege.crown().getCrownTag()).append(gameMode.crown().getCrownTag()).append(p.getName()).append("]");
                }
                bldr.append("</col></shad>: ");
            } else if (p.getPrivilege().equals(PlayerPrivilege.YOUTUBER)) {
                bldr.append("<col=ff0000><shad=000000>");
                if (p.isMember()) {
                    bldr.append("[").append(privilege.crown().getCrownTag()).append(gameMode.crown().getCrownTag()).append(member.crown().getCrownTag()).append(p.getName()).append("]");
                    delay = Math.min(member.getYellDelay(), 66);
                } else {
                    bldr.append("[").append(privilege.crown().getCrownTag()).append(gameMode.crown().getCrownTag()).append(p.getName()).append("]");
                    delay = 66;
                }
                bldr.append("</col></shad>: ");
            } else if (p.isMember()) {
                bldr.append("<col=").append(member.getYellColor()).append("><shad=000000>");
                bldr.append("[").append(gameMode.crown().getCrownTag()).append(member.crown().getCrownTag()).append(p.getName()).append("]");
                bldr.append("</col></shad>: ");
                delay = member.getYellDelay();
            }
            final StringBuilder messagebuilder = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                messagebuilder.append(StringUtilities.escapeBrackets(args[i])).append((i == args.length - 1) ?
                        "" : " ");
            }
            String message = TextUtils.censor(messagebuilder.toString().trim());
            bldr.append(TextUtils.capitalizeFirstCharacter(message));
            for (final Player player : World.getPlayers()) {
                if (player.getSocialManager().containsIgnore(p.getUsername()) && !p.isStaff()) {
                    continue;
                }
                if (p == player || player.getNumericAttribute(GameSetting.YELL_FILTER.toString()).intValue() == 0) {
                    player.sendMessage(bldr.toString());
                }
            }
            p.getVariables().schedule(delay, TickVariable.YELL);
        });
    }

    /**
     * String[] names = new String[]{
     * "Wolf bone", "Bat wing", "Rat bone", "Baby dragon bone", "Ogre ribs", "Jogre bone",
     * "Zogre bone", "Mogre bone", "Dagannoth ribs", "Snake spine", "Zombie bone",
     * "Werewolf bone", "Moss giant bone", "Fire giant bone", "Ice giant ribs",
     * "Terrorbird wing", "Ghoul bone", "Troll bone", "Seagull wing", "Undead cow ribs",
     * "Experiment bone", "Rabbit bone", "Basilisk bone", "Desert lizard bone",
     * "Cave goblin skull", "Vulture wing", "Jackal bone"
     * };
     * val map = new Int2ObjectOpenHashMap<Drop[]>();
     * NPCDrops.drops.forEach((k, v) -> {
     * val list = new ArrayList<Drop>();
     * for (val drop : v) {
     * if (drop.getItemIds() == 617) {
     * drop.setItemId((short) 995);
     * }
     * val name = ItemDefinitions.get(drop.getItemIds()).getAreaName();
     * <p>
     * if (name.startsWith("Clue scroll") || name.endsWith("champion scroll") || name.startsWith("Ensouled")
     * || name.equals("Looting bag") || name.equals("Slayer's enchantment"))
     * if (name.equals("Goblin skull") || name.equals("Big frog leg") || name.equals("Bear ribs")
     * || name.equals("Ram skull") || name.equals("Unicorn bone") || name.equals("Monkey paw")
     * || name.equals("Giant rat bone") || name.equals("Giant bat wing") || name.equals("Mysterious emblem"))
     * continue;
     * <p>
     * if (ArrayUtils.contains(names, name)) {
     * continue;
     * }
     * <p>
     * list.enqueue(drop);
     * }
     * if (!list.isEmpty())
     * map.put(k, list.toArray(new Drop[list.size()]));
     * });
     * NPCDrops.drops = map;
     * <p>
     * NPCDrops.save();
     */
    public static void process(final Player player, String command) {
        String[] parameters = new String[0];
        final String[] parts = command.split(" ");
        if (parts.length > 1) {
            parameters = new String[parts.length - 1];
            System.arraycopy(parts, 1, parameters, 0, parameters.length);
            command = parts[0];
        }
        int level = player.getPrivilege().ordinal();
        while (level-- >= 0) {
            if (!COMMANDS.containsKey(command.toLowerCase())) {
                continue;
            }
            final Command c = COMMANDS.get(command.toLowerCase());
            if (c.eligible(player)) {
                if(player.isStaff()) {
                    final String[] finalParameters = parameters;
                    GameLogger.log(Level.INFO, () -> new GameLogMessage.Command(
                            Instant.Companion.now(),
                            player.getUsername(),
                            c.name,
                            Arrays.asList(finalParameters)
                    ));
                }
                c.handler.accept(player, parameters);
                return;
            }
        }
        if (player.getPrivilege() == PlayerPrivilege.ADMINISTRATOR) {
            player.getPacketDispatcher().sendGameMessage("This command does not exist.", true);
        }
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    public static class Command implements Comparable<Command> {
        private final String name;
        private final PlayerPrivilege privilege;
        private final BiConsumer<Player, String[]> handler;
        private final String description;
        private Predicate<Player> orCondition;

        public Command(final PlayerPrivilege privilege, final String name, final BiConsumer<Player, String[]> handler) {
            this(privilege, name, null, handler);
        }

        public Command(final PlayerPrivilege privilege, final String name, final String description,
                       final BiConsumer<Player, String[]> handler) {
            this.name = name;
            this.privilege = privilege;
            this.handler = handler;
            this.description = description;
            COMMANDS.put(name, this);
        }

        public Command(final PlayerPrivilege privilege, final String[] names,
                       final BiConsumer<Player, String[]> handler) {
            this(privilege, names, null, handler);
        }

        public Command(final PlayerPrivilege privilege, final String[] names, final String description,
                       final BiConsumer<Player, String[]> handler) {
            this.name = Arrays.toString(names);
            this.privilege = privilege;
            this.handler = handler;
            this.description = description;
            for (final String name : names) {
                COMMANDS.put(name, this);
            }
        }

        @Override
        public int compareTo(@NotNull Command o) {
            return Integer.compare(this.privilege.ordinal(), o.privilege.ordinal());
        }

        public Command orAllowIf(Predicate<Player> condition) {
            this.orCondition = condition;
            return this;
        }

        public boolean eligible(Player player) {
            return (orCondition != null && orCondition.test(player)) || player.getPrivilege().eligibleTo(privilege);
        }
    }

    public static void openBoosters(Player p) {
        Analytics.flagInteraction(p, Analytics.InteractionType.CHECK_BOOSTERS);
        final PlayerVariables vars = p.getVariables();
        final Colour red = Colour.RS_RED;
        final Colour green = Colour.RS_GREEN;

        List<String> info = new ArrayList<>();
        info.add("Larran's key booster: " + (vars.getLarransKeyBoosterTick() > 0 ? green + "Active for " + Utils.ticksToTime(vars.getLarransKeyBoosterTick()) : red + "Inactive"));
        info.add("Ganodermic booster: " + (vars.getGanoBoosterKillsLeft() > 0 ? green + "Active for " + vars.getGanoBoosterKillsLeft() + " kills" : red + "Inactive"));
        info.add("Slayer booster: " + (vars.getSlayerBoosterTick() > 0 ? green + "Active for " + Utils.ticksToTime(vars.getSlayerBoosterTick()) : red + "Inactive"));
        info.add("Pet booster: " + (vars.getPetBoosterTick() > 0 ? green + "Active for " + Utils.ticksToTime(vars.getPetBoosterTick()) : red + "Inactive"));
        info.add("Gauntlet booster: " + (vars.getGauntletBoosterCompletionsLeft() > 0 ? green + "Active for " + vars.getGauntletBoosterCompletionsLeft() + " completions" : red + "Inactive"));
        info.add("Blood money booster: " + (vars.getBloodMoneyBoosterLeft() > 0 ? green + "Active for " + vars.getBloodMoneyBoosterLeft() + " PKs" : red + "Inactive"));
        info.add("Clue booster: " + (vars.getClueBoosterLeft() > 0 ? green + "Active for " + vars.getClueBoosterLeft() + " clues" : red + "Inactive"));
        info.add("ToB booster: " + (vars.getTobBoosterleft() > 0 ? green + "Active for " + vars.getTobBoosterleft() + " completions" : red + "Inactive"));
        info.add("Inferno wave skip scroll: " + (p.getBooleanAttribute("used_inferno_skip_scroll") ? green + "Active" : red + "Inactive"));
        info.add("Revenant booster: " + (vars.getRevenantBoosterTick() > 0 ? green + "Active for " + Utils.ticksToTime(vars.getRevenantBoosterTick()) : red + "Inactive"));
        info.add("Nex booster: " + (vars.getNexBoosterleft() > 0 ? green + "Active for " + vars.getNexBoosterleft() + " kills" : red + "Inactive"));

        Diary.sendJournal(p, "Boosters information", info);
    }

    public static boolean isLiveEligible(Player player, PlayerPrivilege liveRankRequired, PlayerPrivilege... exceptions) {
        boolean liveMode = !GameConstants.WORLD_PROFILE.isBeta() && !GameConstants.WORLD_PROFILE.isDevelopment();
        boolean isException = Arrays.stream(exceptions).anyMatch(it -> it == player.getPrivilege());
        boolean isLiveEligible = player.getPrivilege().inherits(liveRankRequired);

        /* Player is normally eligible based on rank inheritance, in live and beta */
        if(isLiveEligible)
            return true;

        /* Player privilege is exception to the rule, does not check inheritance */
        if(!liveMode && isException)
            return true;

        if(liveMode) {
            player.sendMessage("You do not have access to this command on this world.");
        }

        return false;

    }

}
