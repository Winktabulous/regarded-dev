package com.zenyte.game.content.achievementdiary.plugins.item;

import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.plugins.DiaryItem;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.spells.teleports.TeleportCollection;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.GameTab;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.Arrays;

/**
 * @author Tommeh | 19-11-2018 | 15:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ExplorersRing extends DiaryItem {
    public static final int FREE_ALCHEMY_CASTS = 30;
    public static final IntList rings = new IntArrayList(Arrays.asList(ItemId.EXPLORERS_RING_1, ItemId.EXPLORERS_RING_2, ItemId.EXPLORERS_RING_3, ItemId.EXPLORERS_RING_4));

    @Override
    public void handle() {
        bind("Energy", (player, item, slotId) -> energy(player, item));
        bind("Alchemy", (player, item, slotId) -> alchemy(player));
        bind("Functions", (player, item, slotId) -> {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options(TITLE, "Recharge run energy", "Alchemy").onOptionOne(() -> energy(player, item)).onOptionTwo(() -> alchemy(player));
                }
            });
        });
        bind("Teleport", (player, item, slotId) -> {
            final int usedTeleports = player.getVariables().getCabbageFieldTeleports();
            if (item.getId() == ItemId.EXPLORERS_RING_2 && usedTeleports >= 3) {
                player.sendMessage(Colour.RED.wrap("You have used up your daily Cabbage Field teleports for today."));
                return;
            }
            if (item.getId() == ItemId.EXPLORERS_RING_2) {
                player.getVariables().setCabbageFieldTeleports(usedTeleports + 1);
                player.getTemporaryAttributes().put("cabbage field restricted teleport", true);
            }
            TeleportCollection.EXPLORERS_RING_CABBAGE_TELEPORT.teleport(player);
        });
    }

    private void energy(final Player player, final Item item) {
        final int limit = item.getId() == ItemId.EXPLORERS_RING_1 ? 2 : item.getId() == ItemId.EXPLORERS_RING_2 || item.getId() == ItemId.EXPLORERS_RING_4 ? 3 : 4;
        final int replenishments = player.getVariables().getRunReplenishments();
        if (replenishments >= limit) {
            player.sendMessage("You have used up your daily run replenishments for today.");
            return;
        }
        player.getVariables().setRunReplenishments(replenishments + 1);
        if (item.getId() != ItemId.EXPLORERS_RING_4) {
            player.sendMessage("Your run energy was replenished by 50%.");
            player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + 50);
        } else {
            player.sendMessage("Your run energy was fully replenished.");
            player.getVariables().setRunEnergy(100);
        }
        player.sendMessage("You have used " + (replenishments + 1) + " of your " + limit + " restores for today.");
    }

    private void alchemy(final Player player) {
        final boolean hasCast = player.getVariables().getFreeAlchemyCasts() < FREE_ALCHEMY_CASTS;
        if (!hasCast) {
            player.sendMessage("You have no charges for today.");
            return;
        }
        player.getInterfaceHandler().openGameTab(GameTab.SPELLBOOK_TAB);
        GameInterface.EXPLORER_RING_ALCH.open(player);
    }

    public static boolean hasAnyRing(final Player player) {
        return player.carryingAny(ItemId.EXPLORERS_RING_1, ItemId.EXPLORERS_RING_2, ItemId.EXPLORERS_RING_3, ItemId.EXPLORERS_RING_4);
    }

    public static boolean check(final Player player, final SpellState state) {
        if (!ExplorersRing.hasAnyRing(player)) {
            return state.check();
        } else if (!hasCastRemaining(player)) {
            if (player.getInterfaceHandler().isPresent(GameInterface.EXPLORER_RING_ALCH)) {
                player.sendMessage("You have no charges for today.");
                player.getInterfaceHandler().sendInterface(GameInterface.SPELLBOOK);
                return false;
            } else {
                return state.check();
            }
        }
        return true;
    }

    public static boolean usingRing(final Player player) {
        return player.getInterfaceHandler().isPresent(GameInterface.EXPLORER_RING_ALCH) || (hasAnyRing(player) && hasCastRemaining(player));
    }

    private static boolean hasCastRemaining(final Player player) {
        return player.getVariables().getFreeAlchemyCasts() < ExplorersRing.FREE_ALCHEMY_CASTS;
    }

    @Override
    public int[] getItems() {
        return rings.toIntArray();
    }
}
