package com.zenyte.game.content.skills.farming.plugins;

import com.zenyte.game.content.skills.farming.*;
import com.zenyte.game.content.skills.farming.actions.*;
import com.zenyte.game.content.skills.woodcutting.actions.Woodcutting;
import com.zenyte.game.world.entity.player.ActionManager;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.SpiritTreeD;
import com.zenyte.plugins.dialogue.SpiritTreeMenuD;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import mgi.types.config.enums.Enums;

/**
 * @author Kris | 10. nov 2017 : 22:27.04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public final class PatchPlugin implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final FarmingSpot spot = player.getFarming().create(object);
        final ActionManager manager = player.getActionManager();
        switch (option) {
            case "Guide" -> {
                player.getSkills().sendSkillMenu(Enums.SKILL_GUIDES_ENUM.getKey(SkillConstants.SKILLS[SkillConstants.FARMING]).orElseThrow(RuntimeException::new), spot.getPatch().getType().getVarbit());
                return;
            }
            case "Rake" -> manager.setAction(new Raking(spot));
            case "Clear" -> manager.setAction(new Clearing(spot));
            case "Inspect" -> manager.setAction(new Inspecting(spot));
            case "Check-health" -> manager.setAction(new HealthChecking(spot));
            case "Cut", "Chop", "Chop down", "Chop-down" -> {
                assert spot.getProduct().isTree();
                manager.setAction(new Woodcutting(object, spot.getProduct().getTreeDefinitions(), () -> spot.setChoppedDown(object.getId())));
            }
            case "Cure", "Prune" -> manager.setAction(new Curing(object, spot));
            case "Remove" -> {
                assert spot.getProduct() == FarmingProduct.SCARECROW;
                manager.setAction(new ScarecrowRemoval(spot));
            }
            case "Talk-to" -> {
                assert spot.getProduct() == FarmingProduct.SPIRIT_TREE;
                player.getDialogueManager().start(new SpiritTreeD(player));
            }
            case "Travel" -> {
                assert spot.getProduct() == FarmingProduct.SPIRIT_TREE;
                player.getDialogueManager().start(new SpiritTreeMenuD(player));
            }
            case "Take" -> {
                assert spot.getPatch().getType() == PatchType.COMPOST_BIN;
                manager.setAction(new BinClearing(spot));
            }
            case "Dump", "Open", "Close" -> {
                assert spot.getPatch().getType() == PatchType.COMPOST_BIN;
                if (option.equalsIgnoreCase("Dump")) {
                    player.getDialogueManager().start(new Dialogue(player) {
                        @Override
                        public void buildDialogue() {
                            options("Dump the entire contents of the bin?", new DialogueOption("Yes, throw it all away.", () -> manager.setAction(new BinInteraction(spot, option))), new DialogueOption("No, keep it."));
                        }
                    });
                    return;
                }
                manager.setAction(new BinInteraction(spot, option));
            }
            case "Harvest", "Pick", "Pick-spine", "Pick-coconut", "Pick-dragonfruit", "Pick-fruit", "Pick-pineapple", "Pick-leaf", "Pick-orange", "Pick-banana", "Pick-apple", "Pick-from" -> {
                if (player.getTemporaryAttributes().containsKey("harvester") && (System.currentTimeMillis() - ((long) player.getTemporaryAttributes().get("harvester")) < 2_000)) {
                    Harvesting action = new Harvesting(spot);
                    if (action.isFast())
                        return;
                    //System.out.println("making fast");
                    action.setFast(true);
                    manager.setAction(action);
                } else
                    manager.setAction(new Harvesting(spot));
                player.getTemporaryAttributes().put("harvester", System.currentTimeMillis());
            }
            case "Dig" -> {
                assert spot.getPatch().getType() == PatchType.GRAPEVINE_PATCH;
                manager.setAction(new GrapevineTreating(spot));
            }
        }
    }

    @Override
    public Object[] getObjects() {
        final IntOpenHashSet list = new IntOpenHashSet();
        for (final FarmingPatch patch : FarmingPatch.values) {
            for (int i : patch.getIds()) {
                list.add(i);
            }
        }
        for (final RedwoodTree.RedwoodTreeBranch redwood : RedwoodTree.RedwoodTreeBranch.values()) {
            list.add(redwood.getId());
        }
        return list.toArray();
    }

    private static final int REDWOOD_PATCH_CENTER_OBJECT = 34055;
    public static final int VARROCK_OBJECT_OBJECT = 8390;

    @Override
    public int getStrategyDistance(WorldObject obj) {
        return switch (obj.getId()) {
            case VARROCK_OBJECT_OBJECT -> 1;
            case REDWOOD_PATCH_CENTER_OBJECT -> 3;
            default -> ObjectAction.super.getStrategyDistance(obj);
        };
    }

    @Override
    public int getDelay() {
        return 1;
    }
}
