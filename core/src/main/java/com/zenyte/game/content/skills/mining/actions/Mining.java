package com.zenyte.game.content.skills.mining.actions;

import com.near_reality.game.content.crystal.CrystalShardKt;
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalTool;
import com.near_reality.game.content.skills.mining.PickAxeDefinition;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.near_reality.game.world.entity.player.PlayerSkillingModifiersKt;
import com.zenyte.game.content.achievementdiary.AchievementDiaries;
import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.content.boons.impl.IWantItAll;
import com.zenyte.game.content.boons.impl.MinerFortyNiner;
import com.zenyte.game.content.boons.impl.SwissArmyMan;
import com.zenyte.game.content.minigame.castlewars.CastlewarsRockPatch;
import com.zenyte.game.content.minigame.motherlode.OreVein;
import com.zenyte.game.content.minigame.motherlode.Paydirt;
import com.zenyte.game.content.minigame.motherlode.UpperMotherlodeArea;
import com.zenyte.game.content.skills.mining.MiningDefinitions;
import com.zenyte.game.content.skills.mining.MiningDefinitions.OreDefinitions;
import com.zenyte.game.content.skills.mining.MiningDefinitions.PickaxeDefinitions;
import com.zenyte.game.content.skills.mining.MiningDefinitions.ShapeDefinitions;
import com.zenyte.game.content.stars.ShootingStar;
import com.zenyte.game.content.treasuretrails.ClueItem;
import com.zenyte.game.content.treasuretrails.ClueItemUtil;
import com.zenyte.game.content.treasuretrails.clues.CharlieTask;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.content.xamphur.XamphurBoost;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.model.item.containers.GemBag;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCObjectEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dailychallenge.DailyChallengeManager;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.itemonnpc.ItemOnBarricadeAction;
import com.zenyte.plugins.itemonobject.PotionOnCastlewarsRocks;
import org.apache.commons.lang3.ArrayUtils;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.near_reality.game.world.entity.player.PlayerSkillingModifiersKt.*;
import static com.zenyte.game.content.skills.woodcutting.actions.Woodcutting.BURN_GFX;

/**
 * @author Noele | Nov 9, 2017 : 12:22:34 AM
 * see <a href="https://noeles.life">...</a> ||  noele@zenyte.com
 */
public class Mining extends Action {
    @Override
    public boolean interruptedByCombat() {
        return false;
    }

    private final OreDefinitions ore;
    private WorldObject rock;
    private NPC npc;
    private PickAxeDefinition tool;
    private Item pickaxe;
    private Container container;
    private int slotId;
    public static final Graphics ROCKFALL_EXPLOSION = new Graphics(305);
    public static final Projectile ROCKFALL_PROJECTILE = new Projectile(645, 255, 0, 0, 0, 25, 64, 5);

    public Mining(final WorldObject rock, final OreDefinitions ore) {
        this.rock = rock;
        this.ore = ore;
    }

    public Mining(final WorldObject rock) {
        this(rock, OreDefinitions.getDef(rock.getId()));
    }

    public Mining(final OreDefinitions ore, final NPC npc) {
        this.ore = Objects.requireNonNull(ore);
        this.npc = npc;
    }

    @Override
    public boolean start() {
        if (!checkTool()) return false;

        if (!check()) return false;

        if (ore.isShootingStar()) {
            if (rock instanceof ShootingStar star) {
                if (star.isUndiscovered()) {
                    player.sendMessage("You receive a reward as the first player to discover this crashed star!");
                    player.getInventory().addOrDrop(new Item(ItemId.COINS_995, 150_000), new Item(ItemId.STARDUST, 50), new Item(ItemId.TOME_OF_EXPERIENCE_30215));
                    star.setUndiscovered(false);
                }
            } else {
                player.sendMessage("You may only mine shooting stars that have recently crashed.");
                return false;
            }
        }

        if (ore.isShootingStar()) {
            player.sendFilteredMessage("You swing your pick at the star.");
        } else {
            player.sendFilteredMessage("You swing your pick at the rock.");
        }
        final int time = (ore.equals(OreDefinitions.ESSENCE) || ore.equals(OreDefinitions.DAEYALT_ESSENCE)) ? 1 : tool.getMineTime();
        delay(time);
        return true;
    }

    public boolean success() {
        if (ore.equals(OreDefinitions.ESSENCE) || ore.equals(OreDefinitions.DAEYALT_ESSENCE)) {
            return true;
        }
        assert ore.getSpeed() > 0;
        int level = player.getSkills().getLevel(SkillConstants.MINING) + (player.inArea("Mining Guild") ? 7 : 0);
        if (player.getEquipment().getId(EquipmentSlot.RING) == ItemId.CELESTIAL_RING || player.getEquipment().getId(EquipmentSlot.RING) == ItemId.CELESTIAL_RING_UNCHARGED) {
            level += 4; // Celestial ring provides invisible 4 level boost.
        }
        if (ore == OreDefinitions.GEM) {
            final Item amulet = player.getAmulet();
            if (amulet != null && amulet.getName().toLowerCase().contains("glory")) {
                level += 30;
            }
        }
        final int advancedLevels = level - ore.getSpeed();
        if (ore == OreDefinitions.PAYDIRT) {
            return 25 + (level / 2.25F) > Utils.random(100);
        }
        final int chance = Math.max(Math.min(Math.round(advancedLevels * 0.8F) + 20, 70), 4) * 2;
        final boolean isBot = PlayerAttributesKt.getFlaggedAsBot(player);
        if (isBot) {
            if (!Utils.randomBoolean(60))
                return false;
        }
        return chance > Utils.random(100);
    }

    @Override
    public boolean process() {
        if (!check()) {
            return false;
        }
        final boolean altAnim = this.ore == OreDefinitions.AMETHYST || this.ore == OreDefinitions.PAYDIRT || this.ore == OreDefinitions.CWARS_WALL;
        player.setAnimation(altAnim ? tool.getAlternateAnimation() : tool.getAnim());
        return checkObject();
    }

    private static int randomGem() {
        final int random = Utils.random(127);
        if (random < 60) {
            return 1625;
        } else if (random < 90) {
            return 1627;
        } else if (random < 105) {
            return 1629;
        } else if (random < 114) {
            return 1623;
        } else if (random < 119) {
            return 1621;
        } else if (random < 124) {
            return 1619;
        }
        return 1617;
    }

    @Override
    public int processWithDelay() {
        if (!success()) {
            return (ore.equals(OreDefinitions.ESSENCE) || ore.equals(OreDefinitions.DAEYALT_ESSENCE)) ? 1 : tool.getMineTime();
        }
        if (ore == OreDefinitions.CWARS_WALL) {
            final CastlewarsRockPatch wallData = CastlewarsRockPatch.getData(rock);
            if (wallData == null) return -1;
            player.sendMessage("You've collapsed the tunnel!");
            World.sendGraphics(ItemOnBarricadeAction.EXPLOSION, wallData.getPatch());
            World.spawnObject(wallData.getPatch());
            PotionOnCastlewarsRocks.processVarbits(rock, true);
            CharacterLoop.forEach(wallData.getPatch(), 1, Entity.class, entity -> {
                if (CollisionUtil.collides(wallData.getPatch().getX(), wallData.getPatch().getY(), 2, entity.getX(), entity.getY(), entity.getSize())) {
                    if (entity instanceof Player) {
                        entity.applyHit(new Hit(entity.getHitpoints(), HitType.REGULAR));
                    }
                }
            });
            return -1;
        }
        if (ore == OreDefinitions.CWARS_ROCKS) {
            final boolean initial = rock.getId() == 4437;
            if (initial) {
                rock = new WorldObject(4438, rock.getType(), rock.getRotation(), rock);
                World.spawnObject(rock);
            } else {
                World.removeObject(rock);
                PotionOnCastlewarsRocks.processVarbits(rock, false);
            }
            return initial ? tool.getMineTime() : -1;
        }
        final Skills skills = player.getSkills();
        if (ore == OreDefinitions.ROCKFALL) {
            skills.addXp(SkillConstants.MINING, ore.getXp());
            World.removeObject(rock);
            WorldTasksManager.schedule(() -> {
                final int[] elements = new int[] {-1, 1};
                World.sendProjectile(rock.transform(elements[Utils.random(elements.length - 1)], elements[Utils.random(elements.length - 1)], 0), rock, ROCKFALL_PROJECTILE);
                World.sendProjectile(rock.transform(elements[Utils.random(elements.length - 1)], elements[Utils.random(elements.length - 1)], 0), rock, ROCKFALL_PROJECTILE);
                WorldTasksManager.schedule(() -> {
                    CharacterLoop.forEach(rock, 1, Entity.class, entity -> {
                        if (CollisionUtil.collides(rock.getX(), rock.getY(), 1, entity.getX(), entity.getY(), entity.getSize())) {
                            if (entity instanceof Player) {
                                entity.applyHit(new Hit(Utils.random(1, 4), HitType.DEFAULT));
                            }
                            if (entity instanceof Player) {
                                entity.setRouteEvent(new ObjectEvent(((Player) entity), new ObjectStrategy(rock), null));
                            } else {
                                entity.setRouteEvent(new NPCObjectEvent(((NPC) entity), new ObjectStrategy(rock)));
                            }
                        }
                    });
                    World.sendGraphics(ROCKFALL_EXPLOSION, rock);
                    World.spawnObject(rock);
                });
            }, this.ore.getTime());
            return -1;
        }
        if (ore == OreDefinitions.PAYDIRT) {
            if(!player.hasBoon(MinerFortyNiner.class)) {
                if (rock instanceof OreVein) {
                    ((OreVein) rock).start();
                }
                if (!UpperMotherlodeArea.polygon.contains(rock) && Utils.random(0, 2) == 0) {
                    final int emptyId = rock.getId() + 4;
                    final WorldObject empty = new WorldObject(emptyId, rock.getType(), rock.getRotation(), rock.getX(), rock.getY(), rock.getPlane());
                    World.spawnObject(empty);
                    final int size = World.getPlayers().size();
                    final float percentage = Math.min(size, 500) / 1000.0F;
                    final int time = (int) (ore.getTime() - (ore.getTime() * percentage));
                    WorldTasksManager.schedule(() -> World.spawnObject(rock), time);
                }
            }
        }
        final Inventory inventory = player.getInventory();
        if (ore == OreDefinitions.VOLCANIC_ASH) {
            skills.addXp(SkillConstants.MINING, ore.getXp());
            final int level = skills.getLevel(SkillConstants.MINING);
            int amount = level >= 97 ? 6 : level >= 82 ? 5 : level >= 67 ? 4 : level >= 52 ? 3 : level >= 37 ? 2 : 1;
            amount = (int) (amount * determineGatheringMultiplier(player).orElse(1.0));
            final Item ore = new Item(this.ore.getOre(), amount);
            player.sendFilteredMessage("You manage to mine some " + ore.getName().toLowerCase() + ".");
            handleCelestialRing();
            inventory.addItem(ore);
            ClueItemUtil.roll(player, this.ore.getBaseClueGeodeChance(), skills.getLevel(SkillConstants.MINING), ClueItem::getClueGeode);
            if (Utils.random(8) == 0) {
                final WorldObject empty = new WorldObject(ShapeDefinitions.getEmpty(rock.getId()), rock.getType(), rock.getRotation(), rock.getX(), rock.getY(), rock.getPlane());
                World.spawnObject(empty);
                WorldTasksManager.schedule(() -> World.spawnObject(rock), this.ore.getTime());
                return -1;
            }
            return tool.getMineTime();
        }
        if (ore == OreDefinitions.GEM) {
            skills.addXp(SkillConstants.MINING, ore.getXp());
            final Item gem = new Item(randomGem());
            if (gem.getId() == ItemId.UNCUT_RED_TOPAZ) {
                player.getAchievementDiaries().update(KaramjaDiary.MINE_A_RED_TOPAZ);
            }
            player.sendFilteredMessage("You manage to mine some " + gem.getName().toLowerCase().replace("uncut ", "") + ".");

            GemBag gemBag = player.getGemBag();
            if(gemBag != null && player.getInventory().containsItem(GemBag.GEM_BAG_OPEN) && gemBag.isOpen()
                    && !gemBag.isFull(gem) && ArrayUtils.contains(GemBag.IDS, gem.getId())) {
                gemBag.getContainer().add(gem);
                gemBag.getContainer().refresh(player);
            } else
                inventory.addItem(gem);
            final boolean deplete = !player.getBoonManager().hasBoon(MinerFortyNiner.class);
            ClueItemUtil.roll(player, this.ore.getBaseClueGeodeChance(), skills.getLevel(SkillConstants.MINING), ClueItem::getClueGeode);
            if (deplete) {
                final WorldObject empty = new WorldObject(ShapeDefinitions.getEmpty(rock.getId()), rock.getType(), rock.getRotation(), rock.getX(), rock.getY(), rock.getPlane());
                World.spawnObject(empty);
                WorldTasksManager.schedule(() -> World.spawnObject(rock), ore.getTime());
                return -1;
            } else
                return 1;
        }
        final boolean valid = player.getPerkManager().isValid(PerkWrapper.MASTER_MINER);
        int amount = ore.equals(OreDefinitions.DAEYALT_ESSENCE) ? Utils.random(2,3) : player.getPerkManager().isValid(PerkWrapper.MASTER_MINER) && Utils.random(0, 100) <= 15 ? 2 : 1;
        amount = (int) (amount * determineGatheringMultiplier(player).orElse(1.0));
        if (ore.isExtraOre() && Utils.random(99) < 5 && SkillcapePerk.MINING.isEffective(player)) {
            amount++;
        }
        onGather(player);

        if(ore == OreDefinitions.ANCIENT_ESSENCE) {
            int playerLevel = player.getSkills().getLevel(SkillConstants.MINING);
            amount += calculateAncientEssence(playerLevel);
        }

        if (ore == OreDefinitions.TE_SALT || ore == OreDefinitions.EFH_SALT || ore == OreDefinitions.URT_SALT) {
            amount = Utils.random(2, 5);
        }

        if (ore == OreDefinitions.SALAX_SALT) {
            amount = Utils.random(3, 6);
            if(player.getInventory().getAmountOf(ItemId.SALAX_SALT) > 30) {
                player.sendMessage("I should use some of this before mining any more.");
                return -1;
            }
        }

        if(player.getBoonManager().hasBoon(IWantItAll.class) && IWantItAll.roll())
            amount *= 2;

        final int body = player.getEquipment().getId(EquipmentSlot.PLATE);
        if (body >= 13104 && body <= 13107) {
            final MiningDefinitions.OreDefinitions limit = body == 13104 ? OreDefinitions.COAL : body == 13105 ? OreDefinitions.MITHRIL : body == 13106 ? OreDefinitions.ADAMANTITE : OreDefinitions.AMETHYST;
            if (Utils.random(10) == 0 && (ore == OreDefinitions.ROCK_FORMATION || ore == OreDefinitions.GLOWING_ROCK_FORMATION || ore.ordinal() <= limit.ordinal())) {
                amount++;
            }
        }
        boolean deplete = ore.getDepletionRate() > 0 && Utils.random(ore.getDepletionRate() - 1) == 0;
        if(player.getBoonManager().hasBoon(MinerFortyNiner.class) && ore != OreDefinitions.ANCIENT_ESSENCE) {
            deplete = false;
        }
        if (deplete && npc == null) {
            World.spawnObject(new WorldObject(ShapeDefinitions.getEmpty(rock.getId()), rock.getType(), rock.getRotation(), rock.getX(), rock.getY(), rock.getPlane()));
            WorldTasksManager.schedule(() -> World.spawnObject(rock), ore.getTime());
        }
        if (ore.equals(OreDefinitions.SANDSTONE)) {
            final int type = Utils.random(3);
            final int ore = 6971 + (type * 2);
            final int experience = 30 + (type * 10);
            mineOre(skills, inventory, valid, amount, ore, experience, "sandstone");
            return -1;
        } else if (ore.equals(OreDefinitions.GRANITE)) {
            final int type = Utils.random(2);
            final int ore = 6979 + (type * 2);
            final int experience = type == 0 ? 50 : type == 1 ? 60 : 75;
            mineOre(skills, inventory, valid, amount, ore, experience, "granite");
            return -1;
        }
        if (ore.equals(OreDefinitions.RUNITE_GOLEM_ROCKS)) {
            if (npc != null) {
                npc.finish();
            }
        }
        if (ore.equals(OreDefinitions.IRON)) {
            CharlieTask.MINE_IRON.progress(player);
        }

        updateMiningDiaries();

        int exp = (int) (ore.equals(OreDefinitions.DAEYALT_ESSENCE) ? ore.getXp() : (amount * ore.getXp()));
        skills.addXp(SkillConstants.MINING, exp);

        if (ore.getIncinerationExperience() > 0 && (player.hasBoon(SwissArmyMan.class) || pickaxe.getCharges() > 0) && tool.equals(PickaxeDefinitions.INFERNAL) && Utils.random(2) == 0) {

            skills.addXp(SkillConstants.SMITHING, ore.getIncinerationExperience());
            if(slotId != -1)
                player.getChargesManager().removeCharges(pickaxe, 1, container, slotId);
            player.setAnimation(Animation.STOP);
            player.sendFilteredMessage("You manage to mine some " + ore.getName() + ".");
            handleCelestialRing();
            player.sendSound(2725);
            player.setGraphics(BURN_GFX);
            ClueItemUtil.roll(player, this.ore.getBaseClueGeodeChance(), skills.getLevel(SkillConstants.MINING), ClueItem::getClueGeode);
            return ore.equals(OreDefinitions.ESSENCE) || ore.equals(OreDefinitions.DAEYALT_ESSENCE) ? 1 : -1;
        } else {
            final int essence = skills.getLevel(SkillConstants.MINING) < 30 ? 1436 : 7936;
            if (ore.equals(OreDefinitions.CLAY)) {
                final Item bracelet = player.getEquipment().getItem(EquipmentSlot.HANDS);
                if (bracelet != null && bracelet.getId() == ItemId.BRACELET_OF_CLAY) {
                    player.getChargesManager().removeCharges(bracelet, 1, player.getEquipment().getContainer(), EquipmentSlot.HANDS.getSlot());
                    inventory.addOrDrop(new Item(1761, amount));
                    handleCelestialRing();
                } else {
                    inventory.addItem(ore.getOre(), amount).onFailure(remainder -> World.spawnFloorItem(remainder, player));
                    handleCelestialRing();
                    // no experience gained so not advantage gained from other degrade-ables,
                    // so we don't deplete charges here.
                }
                ClueItemUtil.roll(player, this.ore.getBaseClueGeodeChance(), skills.getLevel(SkillConstants.MINING), ClueItem::getClueGeode);
            } else {
                if (ore.getOre() != -1) {
                    if (ore == OreDefinitions.PAYDIRT) {
                        final Optional<Paydirt> generated = Paydirt.generate(player);
                        generated.ifPresent(payDirt -> {
                            boolean goldenNuggetsBoost = payDirt == Paydirt.GOLDEN_NUGGET && World.hasBoost(XamphurBoost.GOLDEN_NUGGETS_X2);
                            final Item item = new Item(ore.getOre(), goldenNuggetsBoost ? 2 : 1);
                            item.setAttribute("paydirt ore id", payDirt.getId());
                            inventory.addItem(item).onFailure(remainder -> World.spawnFloorItem(remainder, player));
                            player.sendFilteredMessage("You manage to mine some pay-dirt.");
                            releaseChargeIfCrystalPickaxe();
                        });
                    } else {
                        if (ore.isShootingStar() && rock instanceof ShootingStar star) {
                            star.onHarvest();
                            if (WildernessArea.isWithinWilderness(player) && Utils.roll(10)) {
                                player.getInventory().addOrDrop(ItemId.BLOOD_MONEY);
                                player.sendMessage("You receive " + Colour.RS_RED + "1x Blood money</col> from mining the star in the wilderness.");
                            }
                        }
                        inventory.addItem(ore.equals(OreDefinitions.ESSENCE) ? essence : ore.getOre(), amount).onFailure(remainder -> World.spawnFloorItem(remainder, player));
                        handleCelestialRing();
                        ClueItemUtil.roll(player, this.ore.getBaseClueGeodeChance(), skills.getLevel(SkillConstants.MINING), ClueItem::getClueGeode);
                        releaseChargeIfCrystalPickaxe();
                    }
                }
            }
        }
        if (valid) {
            player.getPerkManager().consume(PerkWrapper.MASTER_MINER);
        }
        return ore.equals(OreDefinitions.ESSENCE) || ore.equals(OreDefinitions.DAEYALT_ESSENCE) ? 1 : deplete ? -1 : tool.getMineTime();
    }

    private int calculateAncientEssence(int playerLevel) {
        if(playerLevel < 78)
            return Utils.random(1, 2);
        if(playerLevel < 90)
            return Utils.random(2, 3);
        else return Utils.random(3, 4);
    }

    private void updateMiningDiaries() {
        final AchievementDiaries diaries = player.getAchievementDiaries();
        final DailyChallengeManager daily = player.getDailyChallengeManager();
        if (ore.equals(OreDefinitions.ESSENCE)) {
            daily.update(SkillingChallenge.MINE_ESSENCE);
        } else if (ore.equals(OreDefinitions.CLAY)) {
            if (player.getX() >= 3399 && player.getX() <= 3424 && player.getY() >= 3152 && player.getY() <= 3169) {
                diaries.update(DesertDiary.MINE_CLAY);
            }
            daily.update(SkillingChallenge.MINE_CLAY);
        } else if (ore.equals(OreDefinitions.IRON)) {
            diaries.update(WildernessDiary.MINE_IRON_ORE);
            diaries.update(VarrockDiary.MINE_IRON);
            diaries.update(LumbridgeDiary.MINE_IRON);
            diaries.update(WesternProvincesDiary.MINE_IRON_NEAR_PISCATORIS);
            diaries.update(KourendDiary.MINE_IRON_IN_MT_KARUULM);
            daily.update(SkillingChallenge.MINE_IRON_ORES);
        } else if (ore.equals(OreDefinitions.COAL)) {
            diaries.update(KandarinDiary.MINE_COAL);
            diaries.update(FremennikDiary.MINE_COAL_IN_RELLEKKA);
            AdventCalendarManager.increaseChallengeProgress(player, 2022, 21, 1);
        } else if (ore.equals(OreDefinitions.SILVER)) {
            diaries.update(FremennikDiary.CRAFT_A_TIARA, 1);
            daily.update(SkillingChallenge.MINE_SILVER_ORES);
        } else if (ore.equals(OreDefinitions.GOLD)) {
            diaries.update(FaladorDiary.MINE_GOLD_ORE);
            diaries.update(KaramjaDiary.MINE_GOLD);
            daily.update(SkillingChallenge.MINE_GOLD_ORES);
        } else
            //TODO: Convert the boundary to diary restriction on its own.
            if (ore.equals(OreDefinitions.MITHRIL)) {
                diaries.update(WildernessDiary.MINE_MITHRIL_ORE);
                diaries.update(MorytaniaDiary.MINE_MITHRIL_IN_ABANDONED_MINE);
                daily.update(SkillingChallenge.MINE_MITHRIL_ORES);
                SherlockTask.MINE_MITHRIL_ORE.progress(player);
            } else if (ore.equals(OreDefinitions.ADAMANTITE)) {
                diaries.update(FremennikDiary.MINE_ADAMANTITE_ORE);
                diaries.update(WesternProvincesDiary.MINE_ADAMANTITE_IN_TIRANNWN);
            } else if (ore.equals(OreDefinitions.RUNITE) || ore.equals(OreDefinitions.RUNITE_GOLEM_ROCKS)) {
                daily.update(SkillingChallenge.MINE_RUNITE_ORES);
            } else if (ore.equals(OreDefinitions.AMETHYST)) {
                daily.update(SkillingChallenge.MINE_AMETHYST);
            } else if (ore.equals(OreDefinitions.LOVAKITE)) {
                diaries.update(KourendDiary.MINE_SOME_LOVAKITE);
            }
    }

    private void mineOre(Skills skills, Inventory inventory, boolean valid, int amount, int ore, int experience, String oreName) {
        skills.addXp(SkillConstants.MINING, experience);
        inventory.addOrDrop(new Item(ore, amount));
        ClueItemUtil.roll(player, this.ore.getBaseClueGeodeChance(), skills.getLevel(SkillConstants.MINING), ClueItem::getClueGeode);
        player.sendFilteredMessage("You manage to mine some " + oreName + ".");
        if (valid)
            player.getPerkManager().consume(PerkWrapper.MASTER_MINER);
        if (player.inArea("Trahaearn mine"))
            CrystalShardKt.tryFindRandom(player, 127);
        releaseChargeIfCrystalPickaxe();
    }

    private void releaseChargeIfCrystalPickaxe() {
        if (tool instanceof CrystalTool.Pickaxe)
            player.getChargesManager().removeCharges(pickaxe, 1, container, slotId);
    }

    @Override
    public void stop() {
        player.setAnimation(Animation.STOP);
    }

    private boolean check() {
        if (ore.equals(OreDefinitions.ROCKFALL)) return checkLevel();
        return (checkLevel() && player.getInventory().checkSpace());
    }

    private boolean checkTool() {
        final Optional<MiningDefinitions.PickaxeDefinitions.PickaxeResult> axe = PickaxeDefinitions.get(player, true);
        if (axe.isEmpty()) {
            player.getDialogueManager().start(new PlainChat(player, "You need a pickaxe to mine this rock. You do not have a pickaxe which you have the Mining level to use."));
            return false;
        }
        final MiningDefinitions.PickaxeDefinitions.PickaxeResult definitions = axe.get();
        this.slotId = definitions.getSlot();
        this.tool = definitions.getDefinition();
        this.container = definitions.getContainer();
        this.pickaxe = definitions.getItem();
        return true;
    }

    private boolean checkLevel() {
        if (player.getSkills().getLevel(SkillConstants.MINING) < ore.getLevel()) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Mining level of " + ore.getLevel() + " to mine this rock."));
            return false;
        }
        return true;
    }

    private boolean checkObject() {
        if (ore.equals(OreDefinitions.RUNITE_GOLEM_ROCKS)) {
            return npc != null && !npc.isFinished();
        }
        if (ore.equals(OreDefinitions.CWARS_ROCKS)) {
            return (World.getRegion(rock.getRegionId()).containsObject(4437, rock.getType(), rock) || World.getRegion(rock.getRegionId()).containsObject(4438, rock.getType(), rock));
        }
        return World.getRegion(rock.getRegionId()).containsObject(rock.getId(), rock.getType(), rock);
    }

    private static final Set<OreDefinitions> eligibleRocksForSignet = EnumSet.of(
            OreDefinitions.TIN,
            OreDefinitions.COPPER,
            OreDefinitions.CLAY,
            OreDefinitions.SOFT_CLAY,
            OreDefinitions.BLURITE,
            OreDefinitions.IRON,
            OreDefinitions.SILVER,
            OreDefinitions.COAL,
            OreDefinitions.GOLD,
            OreDefinitions.MITHRIL,
            OreDefinitions.ADAMANTITE,
            OreDefinitions.ROCK_FORMATION,
            OreDefinitions.GLOWING_ROCK_FORMATION
    );

    private void handleCelestialRing() {
        Item ring = player.getEquipment().getItem(EquipmentSlot.RING);
        if (ring == null || ring.getId() != ItemId.CELESTIAL_RING && ring.getId() != ItemId.CELESTIAL_SIGNET) {
            return;
        }

        if (ring.getCharges() <= 0 || !eligibleRocksForSignet.contains(ore)) {
            return;
        }
        if(slotId != -1)
            player.getChargesManager().removeCharges(ring, 1, container, slotId);

        if (Utils.roll(10)) {
            if (ore.getOre() != -1) {
                player.getInventory().addOrDrop(ore.getOre());
            }
            if (ore.getXp() != -1) {
                player.getSkills().addXp(SkillConstants.MINING, ore.getXp());
            }
            player.sendMessage("Your " + ring.getName() + " allows you to mine an additional ore.");
        }
    }

}
