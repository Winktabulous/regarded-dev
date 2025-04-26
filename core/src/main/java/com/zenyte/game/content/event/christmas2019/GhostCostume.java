package com.zenyte.game.content.event.christmas2019;

import com.google.common.primitives.Ints;
import com.zenyte.game.content.follower.impl.MiscPet;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.plugins.equipment.equip.EquipPlugin;
import it.unimi.dsi.fastutil.ints.IntArraySet;

import java.util.Arrays;
import java.util.List;

/**
 * @author Corey
 * @since 19/12/2019
 */
public class GhostCostume implements EquipPlugin {
    private static final List<Integer> costumePieces = Arrays.asList(ChristmasConstants.GHOST_HOOD_ID, ChristmasConstants.GHOST_TOP_ID, ChristmasConstants.GHOST_BOTTOMS_ID);

    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        return wearCostume(player, item);
    }

    @Override
    public int[] getItems() {
        return Ints.toArray(costumePieces);
    }

    private boolean wearCostume(final Player player, final Item toEquip) {
        if (!AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.HAS_GHOST_COSTUME)) {
            player.sendMessage("You must have participated in the 2019 Christmas event to wear this.");
            return false;
        }
        // don't continue the quest and execute the dialogue if it has already been done
        if (AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.FIND_OUT_ABOUT_SCOURGES_PAST)) {
            return true;
        }
        if (player.getFollower() == null || player.getFollower().getPet() != MiscPet.AREA_LOCKED_SNOW_IMP) {
            return true;
        }
        // only continue the quest if the player has the area-locked snow imp follower
        if (ghostPiecesWorn(toEquip, player.getEquipment().getItem(EquipmentSlot.HELMET), player.getEquipment().getItem(EquipmentSlot.PLATE), player.getEquipment().getItem(EquipmentSlot.LEGS)) == 3) {
            AChristmasWarble.progress(player, AChristmasWarble.ChristmasWarbleProgress.FIND_OUT_ABOUT_SCOURGES_PAST);
            player.getDialogueManager().start(new Dialogue(player, ChristmasConstants.PERSONAL_SNOW_IMP) {
                @Override
                public void buildDialogue() {
                    final String impName = ChristmasUtils.getImpName(player);
                    player.faceEntity(player.getFollower());
                    npc(impName, "Wow, that looks proper scary, mate!", Expression.HIGH_REV_JOLLY);
                    player("I can barely breathe.");
                    npc(impName, "Sure you can. Nows, " + player.getName() + ", what we really need to do is find out" +
                            " something about Scourge's past.", Expression.HIGH_REV_NORMAL);
                    player("Like what?");
                    npc(impName, "Some mistake he made. Something he did that he regrets. We can use it to make 'im " +
                            "feel bad about the past.", Expression.HIGH_REV_NORMAL);
                    npc(impName, "We should go to the feast. Someone there will know him.", Expression.HIGH_REV_NORMAL);
                    player("Really? This whole plan sounds kinda far-fetch-");
                    npc(impName, "Let's go!", Expression.HIGH_REV_JOLLY);
                }
            });
        }
        return true;
    }

    private int ghostPiecesWorn(final Item... items) {
        int count = 0;
        final IntArraySet seen = new IntArraySet();
        for (final Item item : items) {
            if (item == null) {
                continue;
            }
            if (seen.contains(item.getId())) {
                continue;
            }
            if (costumePieces.contains(item.getId())) {
                seen.add(item.getId());
                count++;
            }
        }
        return count;
    }
}
