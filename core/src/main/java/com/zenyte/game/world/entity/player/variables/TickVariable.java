package com.zenyte.game.world.entity.player.variables;

import com.near_reality.game.content.consumables.drinks.DivinePotion;
import com.zenyte.game.content.boons.impl.TrackStar;
import com.zenyte.game.content.consumables.ConsumableEffects;
import com.zenyte.game.content.kebos.alchemicalhydra.instance.AlchemicalHydraInstance;
import com.zenyte.game.content.skills.farming.Seedling;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.PowerOfDeathVariableTask;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.container.impl.bank.Bank;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.region.RegionArea;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;

/**
 * @author Kris | 5. juuni 2018 : 02:32:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
//These variables reset on death and other places!
public enum TickVariable {
    ANTIFIRE((player, ticks) -> {
        if (ticks % 30 == 0) {
            ConsumableEffects.deductVariable(player, ConsumableEffects.ANTIFIRE_REFRESHES_REMAINING);
        }
    }, new VariableMessage(15, Colour.RS_PURPLE + "Your antifire potion is about to expire."),
            new VariableMessage(0, "<col=7f007f>Your antifire potion has expired.</col>")),
    SUPER_ANTIFIRE((player, ticks) -> {
        if (ticks % 20 == 0) {
            ConsumableEffects.deductVariable(player, ConsumableEffects.SUPER_ANTIFIRE_REFRESHES_REMAINING);
        }
    }, new VariableMessage(15, Colour.RS_PURPLE + "Your super antifire potion is about to expire."),
            new VariableMessage(0, "<col=7f007f>Your super antifire potion has expired.</col>")),
    TELEBLOCK((player, ticks) -> player.getVarManager().sendBit(4163, 100 + ticks)),
    TELEBLOCK_IMMUNITY,
    POISON_IMMUNITY,
    VENOM_IMMUNITY,

    BOUNTY_HUNTER_TARGET_LOSS((player, ticks) -> {
        if (ticks == 100) {
            player.getBountyHunter().halfTimeNotification();
        } else if (ticks == 0) {
            player.getBountyHunter().abandonTarget();
        }
    }),
    BH_V2_COOLDOWN,
    BH_V2_INTERFACE_COOLDOWN,
    WINE_FERMENTATION((player, ticks) -> {
        if (ticks == 0) {
            final int cooking = player.getSkills().getLevel(SkillConstants.COOKING);
            final Inventory inventory = player.getInventory();
            int successfulCount = 0;
            for (final Int2ObjectMap.Entry<Item> entry : inventory.getContainer().getItems().int2ObjectEntrySet()) {
                final Item item = entry.getValue();
                if (item == null || (item.getId() != 1995 && item.getId() != 20752)) continue;
                final double percentage = 0.5F + ((cooking - 35.0F) * 1.52) / 100.0F;
                if (percentage < Utils.randomDouble()) {
                    item.setId(1991);
                    continue;
                }
                item.setId(item.getId() == 1995 ? 1993 : 245);
                successfulCount++;
            }
            inventory.refreshAll();
            int wineCount = 0;
            int zamorakWineCount = 0;
            int badWineCount = 0;
            final Bank bank = player.getBank();
            for (final Int2ObjectMap.Entry<Item> entry : bank.getContainer().getItems().int2ObjectEntrySet()) {
                final Item item = entry.getValue();
                if (item == null || (item.getId() != 1995 && item.getId() != 20752)) continue;
                for (int i = 0; i < item.getAmount(); i++) {
                    final double percentage = 0.5F + ((cooking - 35.0F) * 1.52) / 100.0F;
                    if (percentage < Utils.randomDouble()) {
                        badWineCount++;
                        continue;
                    }
                    if (item.getId() == 1995) {
                        wineCount++;
                    } else {
                        zamorakWineCount++;
                    }
                    successfulCount++;
                }
                bank.set(entry.getIntKey(), null);
            }
            if (zamorakWineCount > 0) {
                bank.add(new Item(245, zamorakWineCount)).onFailure(remaining -> {
                    World.spawnFloorItem(new Item(246, remaining.getAmount()), player);
                    player.sendMessage(Colour.RS_RED.wrap("Some of your wines of zamorak were placed on the ground as" +
							" your bank is full."));
                });
            }
            if (wineCount > 0) {
                bank.add(new Item(1993, wineCount)).onFailure(remaining -> {
                    World.spawnFloorItem(new Item(1994, remaining.getAmount()), player);
                    player.sendMessage(Colour.RS_RED.wrap("Some of your wines were placed on the ground as your bank " +
							"is full."));
                });
            }
            if (badWineCount > 0) {
                //The wines vanish if there's not enough space.
                bank.add(new Item(1991, badWineCount));
            }
            if (successfulCount > 0) {
                player.getSkills().addXp(SkillConstants.COOKING, successfulCount * 200);
                bank.getContainer().setFullUpdate(true);
                bank.getContainer().refresh(player);
                player.sendMessage("Your wines have finished fermenting.");
            }
        }
    }),
    SEEDLING_SPROUT((player, ticks) -> {
        if (ticks == 0) {
            final Inventory inventory = player.getInventory();
            final ArrayList<Item> list = new ArrayList<Item>();
            final ObjectArrayList<Item> wiped = new ObjectArrayList<Item>();
            for (final Int2ObjectMap.Entry<Item> entry : inventory.getContainer().getItems().int2ObjectEntrySet()) {
                final Item item = entry.getValue();
                if (item == null) continue;
                final Seedling seedling = Seedling.getWateredSeedling(item.getId());
                if (seedling == null) continue;
                wiped.add(entry.getValue());
                list.add(new Item(seedling.getSapling(), item.getAmount(), item.getCharges()));
            }
            wiped.forEach(inventory::deleteItem);
            list.forEach(inventory::addOrDrop);
            wiped.clear();
            list.clear();
            inventory.refreshAll();
            final Bank bank = player.getBank();
            for (final Int2ObjectMap.Entry<Item> entry : bank.getContainer().getItems().int2ObjectEntrySet()) {
                final Item item = entry.getValue();
                if (item == null) continue;
                final Seedling seedling = Seedling.getWateredSeedling(item.getId());
                if (seedling == null) continue;
                wiped.add(entry.getValue());
                list.add(new Item(seedling.getSapling(), item.getAmount(), item.getCharges()));
            }
            wiped.forEach(bank::remove);
            list.forEach(item -> bank.add(item).onFailure(it -> World.spawnFloorItem(it, player)));
            bank.getContainer().setFullUpdate(true);
            bank.getContainer().refresh(player);
            player.sendFilteredMessage("Your watered seedlings have sprouted to saplings.");
        }
    }),
    STAMINA_ENHANCEMENT((player, ticks) -> {
        if (ticks == 0) {
            player.getVarManager().sendBit(25, 0);
        }
    }, new VariableMessage(0, "<col=8f4808>Your stamina potion has expired.</col>")),
    OVERLOAD((player, ticks) -> {
        if (ticks == 0) {
            ConsumableEffects.resetOverload(player);
        } else if (ticks % 25 == 0) {
            ConsumableEffects.applyOverload(player);
        }
    }, new VariableMessage(25, Colour.RED + "Your overload effect has almost ran out!")),
    SMOULDERING_HEART((player, ticks) -> {
        if (ticks == 0) {
            ConsumableEffects.resetOverload(player);
        } else if (ticks % 25 == 0) {
            ConsumableEffects.applyHeart(player);
        }
    }, new VariableMessage(25, Colour.RED + "Your Smouldering has almost ran out!"),
        new VariableMessage(0, "<col=ff3045>The boost from the smouldering heart has worn off!</col>")),

    SALT((player, ticks) -> {
        if (ticks % 25 == 0) {
            ConsumableEffects.applySalt(player);
        }
    }, new VariableMessage(25, "<col=ef1020>Your smelling salts effect has almost run out!"),
            new VariableMessage(0, "<col=ff3045>The boost from the smelling salts has worn off!</col>")),
    LIQUID_ADRENALINE(new VariableMessage(0, "<col=ff3045>The energy cost of your special attacks has returned to normal.</col>")),
    SILK_DRESSING((player, ticks) -> {
        if (ticks % 5 == 0) {
            player.heal(5);
        }
    }),
    BLESSED_SCARAB((player, ticks) -> {
        if (ticks % 4 == 0) {
            player.getPrayerManager().restorePrayerPoints(8);
        }
    }),
    PRAYER_ENHANCE((player, ticks) -> {
        if (ticks % 6 == 0) {
            player.getPrayerManager().restorePrayerPoints(1);
        }
    }, new VariableMessage(0, Colour.RED + "Your prayer enhance effect has worn off.")),
    IMBUED_HEART_COOLDOWN(new VariableMessage(0, "<col=ef1020>Your imbued heart has regained its magical power.")),
    SATURATED_HEART_COOLDOWN((player, ticks) -> {
        if (ticks == 0) {
            ConsumableEffects.resetSaturatedHeart(player);
        } else if (ticks % 25 == 0) {
            ConsumableEffects.applySaturatedHeart(player);
        }
    }, new VariableMessage(0, "<col=ef1020>Your saturated heart has regained its magical power.")),
    SKULL((player, ticks) -> {
        if (ticks > 2000 && player.getEquipment().getId(EquipmentSlot.AMULET) != 22557) {
            //Cannot do a direct reference to itself so we invoke it through reflection.
            player.getVariables().cancel(TickVariable.valueOf("SKULL"));
            player.getVariables().setSkull(true);
        }
        if (ticks == 0) {
            player.getVariables().setSkull(false);
        }
    }),
    POWER_OF_DEATH(PowerOfDeathVariableTask.INSTANCE),
    HAMSTRUNG,
    CHARGE(new VariableMessage(0, "<col=ef1020>Your magical charge fades away.</col>")),
    MAGIC_IMBUE((player, ticks) -> {
        if (ticks == 0) {
            player.getVarManager().sendBit(5438, 0);
            player.sendMessage("Your Magic Imbue charge has ended.");
        }
    }),
    VENGEANCE((player, ticks) -> {
        if (ticks == 0) {
            player.getVarManager().sendBit(2451, 0);
        }
    }),
    TRACK_STAR_XP_DROP((player, ticks) -> {
        if (player.getBoonManager().hasBoon(TrackStar.class) && ticks == 0 && player.isRun()) {
            player.getSkills().addXp(SkillConstants.AGILITY, 1);
        }
    }),
    BOX_OF_RESTORATION,
    YELL,
    HYDRA_BLEED((player, ticks) -> {
        final RegionArea area = player.getArea();
        if (!(area instanceof AlchemicalHydraInstance) || player.isDead() || player.isFinished()) {
            return;
        }
        final AlchemicalHydraInstance instance = (AlchemicalHydraInstance) area;
        final Location nextLocation = player.getNextLocation();
        if (nextLocation != null) {
            if (!instance.inside(nextLocation)) {
                return;
            }
        }
        if (ticks < 25 && ticks % 5 == 0) {
            CombatUtilities.delayHit(null, -1, player, new Hit(5, HitType.REGULAR));
        }
    }),
    VARDORVIS_BLEED((player, ticks) -> {
        if (player.isDead() || player.isFinished()) {
            return;
        }
        if (player.bleedNextTick || (ticks < 14 && ticks % 3 == 0)) {
            if(player.bleedNextTick)
                player.bleedNextTick = false;
            CombatUtilities.delayHit(null, -1, player, new Hit(3, HitType.TYPELESS));
        }
    }),
    DIVINE_BASTION_POTION(DivinePotion.Bastion.INSTANCE::pulse),
    DIVINE_BATTLEMAGE_POTION(DivinePotion.BattleMage.INSTANCE::pulse),
    DIVINE_MAGIC_POTION(DivinePotion.Magic.INSTANCE::pulse),
    DIVINE_RANGING_POTION(DivinePotion.Ranging.INSTANCE::pulse),
    DIVINE_SUPER_ATTACK_POTION(DivinePotion.SuperAttack.INSTANCE::pulse),
    DIVINE_SUPER_COMBAT_POTION(DivinePotion.SuperCombat.INSTANCE::pulse),
    DIVINE_SUPER_DEFENCE_POTION(DivinePotion.SuperDefence.INSTANCE::pulse),
    DIVINE_SUPER_STRENGTH_POTION(DivinePotion.SuperStrength.INSTANCE::pulse),

    FORINTHRY_SURGE((player, ticks) -> {
        if (ticks == 0) {
            player.sendMessage("<col=4f006f>You have lost your Forinthry surge.</col>");
            player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        }
    }),
    ANCIENT_SMOKE((player, ticks) -> {
        if (ticks == 0)
            player.sendMessage("The ancient toxic smoke leaves your lungs.");
    });
    final VariableTask task;
    final VariableMessage[] messages;

    TickVariable(final VariableMessage... messages) {
        this(null, messages);
    }

    TickVariable(final VariableTask task, final VariableMessage... messages) {
        this.messages = messages;
        this.task = task;
    }

}
