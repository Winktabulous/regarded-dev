package com.zenyte.game.content.minigame.blastfurnace;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.skills.smithing.SmeltableBar;
import com.zenyte.game.content.skills.smithing.Smelting;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.SkillcapePerk;
import com.zenyte.game.packet.out.LocAnim;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.events.InitializationEvent;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class BlastFurnace {
    private final transient Player player;
    // This is a startable/stoppable WorldTask set during the processBarDispenser() action, used to keep only one running at a time for players.
    private transient WorldTask smeltTask;
    private final Object2IntOpenHashMap<String> ores = new Object2IntOpenHashMap<>();
    // Used to keep track of the players dialogue status, if processBarsFromDialogue is true, that means we are waiting for them to close the dialogue to smelt more bars.
    // This is a confirmed OSRS feature.
    private transient boolean processBarsFromDialogue;
    // This is to kill our WorldTask early in the case of cooling the bars with a water bucket or ice gloves.
    private transient boolean earlyCool = false;
    private final Int2IntOpenHashMap bars = new Int2IntOpenHashMap();
    private transient int oresOnBelt;

    public BlastFurnace(final Player player) {
        this.player = player;
    }

    public int checkPrimaryOres() {
        int amount = 0;
        for (final Object2IntOpenHashMap.Entry<String> entry : ores.object2IntEntrySet()) {
            if (!entry.getKey().equals(BlastFurnaceOre.COAL.toString()) && !entry.getKey().equals(BlastFurnaceOre.TIN_ORE.toString())) {
                amount += entry.getIntValue();
            }
        }
        return amount;
    }

    public boolean hasSecondaryOres() {
        return player.getInventory().containsAnyOf(BlastFurnaceOre.TIN_ORE.getItemId(), BlastFurnaceOre.COAL.getItemId());
    }

    public boolean hasBars() {
        for (final Int2IntOpenHashMap.Entry entry : bars.int2IntEntrySet()) {
            if (entry.getIntValue() > 0) {
                return true;
            }
        }
        return false;
    }

    public int getTotalBars() {
        int amount = 0;
        for (final Int2IntOpenHashMap.Entry entry : bars.int2IntEntrySet()) {
            amount += entry.getIntValue();
        }
        return amount;
    }

    public void setDispenser(final int value) {
        player.getVarManager().sendBit(936, value);
        if (value == 1) {
            player.getAttributes().put("blast_furnace_dispenser", 2);
        } else {
            player.getAttributes().put("blast_furnace_dispenser", value);
        }
    }

    public void addCoffer(final int amount) {
        player.getAttributes().put("blast_furnace_coffer", getCoffer() + amount);
    }

    public int getDispenserState() {
        return player.getNumericAttribute("blast_furnace_dispenser").intValue();
    }

    public void setCoffer(final int amount) {
        player.getAttributes().put("blast_furnace_coffer", amount);
    }

    public int getCoffer() {
        return player.getNumericAttribute("blast_furnace_coffer").intValue();
    }

    public void addOre(@NotNull final BlastFurnaceOre ore, final int amount) {
        getOres().put(ore.toString(), amount + getOre(ore));
    }

    public void subOre(@NotNull final BlastFurnaceOre ore, final int amount) {
        getOres().put(ore.toString(), getOre(ore) - amount);
    }

    public void setOre(@NotNull final BlastFurnaceOre ore, final int amount) {
        getOres().put(ore.toString(), amount);
    }

    public int getOre(@NotNull final BlastFurnaceOre ore) {
        return getOres().getInt(ore.toString());
    }

    public int getBar(@NotNull final SmeltableBar bar) {
        return getBars().getOrDefault(bar.getProduct().getId(), 0);
    }

    public void setBar(@NotNull final SmeltableBar bar, final int amount) {
        getBars().put(bar.getProduct().getId(), amount);
    }

    public void subBars(@NotNull final SmeltableBar bar, final int amount) {
        getBars().put(bar.getProduct().getId(), getBar(bar) - amount);
    }

    public void addBars(@NotNull final SmeltableBar bar, final int amount) {
        getBars().put(bar.getProduct().getId(), getBar(bar) + amount);
    }

    public Item[] constructBarArray() {
        final List<Item> list = new ArrayList<>();
        for (final Int2IntOpenHashMap.Entry entry : player.getBlastFurnace().getBars().int2IntEntrySet()) if (entry.getIntValue() > 0) list.add(new Item(entry.getIntKey(), entry.getIntValue()));
        return list.toArray(new Item[list.size()]);
    }

    public void processVarbits() {
        for (final BlastFurnaceOre ore : BlastFurnaceOre.VALUES) {
            final int oreAmt = player.getBlastFurnace().getOre(ore);
            final BlastFurnaceVarbit varbit = BlastFurnaceVarbit.oreVarbits.get(ore);
            if (player.getVarManager().getBitValue(varbit.getVarbit()) != oreAmt) {
                player.getVarManager().sendBit(varbit.getVarbit(), oreAmt);
            }
        }
        for (final SmeltableBar bar : SmeltableBar.bfValues) {
            if (bar.equals(SmeltableBar.BLURITE_BAR)) {
                continue;
            }
            final int barAmt = player.getBlastFurnace().getBar(bar);
            final BlastFurnaceVarbit varbit = BlastFurnaceVarbit.barVarbits.get(bar);
            if (player.getVarManager().getBitValue(varbit.getVarbit()) != barAmt) {
                player.getVarManager().sendBit(varbit.getVarbit(), barAmt);
            }
        }
    }

    public void processBars() {
        boolean barsMade = false;
        // check available ores and combos
        // also does amount checking to prevent making more bars than possible
        barLoop:
        for (final SmeltableBar bar : SmeltableBar.bfValues) {
            if (bar.equals(SmeltableBar.BLURITE_BAR)) {
                continue;
            }
            if (getBar(bar) == 28) {
                continue;
            }
            /** check to make sure the user has all resources in pot */
            for (final Item oreItem : bar.getMaterials()) {
                final BlastFurnaceOre ore = BlastFurnaceOre.getOre(oreItem.getId());
                if (ore.equals(BlastFurnaceOre.COAL) && getOre(ore) < (oreItem.getAmount() / 2)) {
                    continue barLoop;
                }
                if (!ore.equals(BlastFurnaceOre.COAL) && getOre(ore) < oreItem.getAmount()) {
                    continue barLoop;
                }
            }
            if (bar.getMaterials().length > 1) {
                final Item primary = bar.getMaterials()[0];
                final Item secondary = bar.getMaterials()[1];
                final BlastFurnaceOre primaryE = BlastFurnaceOre.getOre(primary.getId());
                final BlastFurnaceOre secondaryE = BlastFurnaceOre.getOre(secondary.getId());
                final int secondaryAmt = secondaryE.equals(BlastFurnaceOre.COAL) ? getOre(secondaryE) / (secondary.getAmount() / 2) : getOre(secondaryE);
                int amount = getOre(primaryE) > secondaryAmt ? secondaryAmt : getOre(primaryE);
                amount = (getBar(bar) + amount) > 28 ? 28 - getBar(bar) : amount;
                subOre(primaryE, amount);
                if (bar.equals(SmeltableBar.BRONZE_BAR)) {
                    subOre(secondaryE, amount);
                } else {
                    subOre(secondaryE, amount * (secondary.getAmount() / 2));
                }
                addBars(bar, amount);
                player.getSkills().addXp(SkillConstants.SMITHING, (bar.getXp() * amount));
                Smelting.handleDaily(player, bar, amount);
            } else {
                final Item ore = bar.getMaterials()[0];
                final BlastFurnaceOre oreE = BlastFurnaceOre.getOre(ore.getId());
                int amount = getOre(oreE);
                amount = (getBar(bar) + amount) > 28 ? 28 - getBar(bar) : amount;
                addBars(bar, amount);
                subOre(oreE, amount);
                final Item gloves = player.getEquipment().getItem(EquipmentSlot.HANDS);
                Smelting.handleDaily(player, bar, amount);
                if (oreE.equals(BlastFurnaceOre.GOLD_ORE)) {
                    if (gloves == null) {
                        player.getSkills().addXp(SkillConstants.SMITHING, (bar.getXp() * amount));
                    } else {
                        if (player.getEquipment().getItem(EquipmentSlot.HANDS).getId() == 776 || SkillcapePerk.SMITHING.isEffective(player)) {
                            player.getSkills().addXp(SkillConstants.SMITHING, (bar.getXp() + 33.7) * amount);
                        } else {
                            player.getSkills().addXp(SkillConstants.SMITHING, (bar.getXp() * amount));
                        }
                    }
                } else {
                    player.getSkills().addXp(SkillConstants.SMITHING, (bar.getXp() * amount));
                }
            }
            barsMade = true;
        }
        processVarbits();
        if (barsMade) {
            processBarDispenser();
        }
    }

    // only used on-login and on-enter
    /**
     * now that we've confirmed we have all resources, let's make bars
     */
    public void processCooling() {
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if (isEarlyCool() || ticks == 15) {
                    setDispenser(3);
                    stop();
                    return;
                }
                ticks++;
            }
        }, 0, 0);
    }

    public void processBarDispenser() {
        setEarlyCool(false);
        if (smeltTask != null) {
            smeltTask.stop();
        }
        smeltTask = new WorldTask() {
            private int ticks;
            final WorldObject barDispenser = BlastFurnaceArea.BAR_DISPENSER;
            @Override
            public void run() {
                if (isEarlyCool()) {
                    setDispenser(3);
                    stop();
                    return;
                }
                if (ticks == 0 || ticks == 1) {
                    if (ticks == 0) {
                        setDispenser(1);
                    }
                    if (player.isVisibleInViewport(barDispenser)) player.sendZoneUpdate(barDispenser.getX(), barDispenser.getY(), new LocAnim(barDispenser, BlastFurnaceArea.LAVA_FLOW));
                } else if (ticks == 2) {
                    setDispenser(2);
                } else if (ticks == 18) {
                    setDispenser(3);
                    stop();
                    return;
                }
                ticks++;
            }
        };
        WorldTasksManager.schedule(smeltTask, 1, 0);
    }

    @Subscribe
    public static final void onInit(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final BlastFurnace parserData = event.getSavedPlayer().getBlastFurnace();
        if (parserData == null) {
            return;
        }
        player.getBlastFurnace().getOres().putAll(parserData.ores);
        player.getBlastFurnace().getBars().putAll(parserData.bars);
    }

    public int getOresOnBelt() {
        return oresOnBelt;
    }

    public void setOresOnBelt(int oresOnBelt) {
        this.oresOnBelt = oresOnBelt;
    }

    public boolean isProcessBarsFromDialogue() {
        return processBarsFromDialogue;
    }

    public void setProcessBarsFromDialogue(boolean processBarsFromDialogue) {
        this.processBarsFromDialogue = processBarsFromDialogue;
    }

    public boolean isEarlyCool() {
        return earlyCool;
    }

    public void setEarlyCool(boolean earlyCool) {
        this.earlyCool = earlyCool;
    }

    public Object2IntOpenHashMap<String> getOres() {
        return ores;
    }

    public Int2IntOpenHashMap getBars() {
        return bars;
    }
}
