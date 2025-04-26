package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.woodcutting.TreeDefinitions;
import com.zenyte.game.content.skills.woodcutting.actions.Woodcutting;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.enums.FungicideSpray;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

public final class WoodcuttingTreeObjects implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (object.getId() == ObjectId.TREE_10041) {
            player.getDialogueManager().start(new PlainChat(player, "Will you stop that?"));
            return;
        } else if (object.getId() == ObjectId.TREE_2409) {
            player.getDialogueManager().start(new NPCChat(player, 1162, "Hey! Yer big elephant! Don't go choppin' " + "down me house, now!"));
            return;
        } else if (object.getId() == ObjectId.TREE_2447) {
            player.useStairs(828, new Location(2485, 3465, 2), 1, 2);
            return;
        } else if (object.getId() == 2448 || object.getId() == 28800) {
            player.useStairs(828, new Location(2484, 3463, 1), 1, 2);
            return;
        }
        if (option.equalsIgnoreCase("inject") && name.equalsIgnoreCase("sulliuscep")) {
            Object[] spray = FungicideSpray.get(player);
            if (player.carryingItem(ItemId.PROBOSCIS) && spray != null) {
                player.sendSound(new SoundEffect(1967));
                player.sendMessage("You inject the fungicide into the Sulliuscep with help of the proboscis - instantly killing it.");
                player.getInventory().deleteItem(new Item(ItemId.PROBOSCIS, 1));
                player.getInventory().replaceItem(((FungicideSpray) spray[0]).getNextCharge().getId(), 1, (int) spray[1]);
                final int currentIndex = player.getVarManager().getBitValue(Woodcutting.SULLIUSCEP_INDEX_VARBIT);
                player.getVarManager().sendBit(Woodcutting.SULLIUSCEP_INDEX_VARBIT, currentIndex < 6 ? currentIndex + 1 : 0);
            } else {
                player.sendMessage("You need a proboscis and fungicide spray to do this.");
            }
            return;
        }
        switch(name.toLowerCase()) {
            case "tree":
            case "evergreen":
            case "dead tree":
            case "dying tree":
            case "jungle tree":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.TREE));
                return;
            case "achey tree":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.ACHEY_TREE));
                return;
            case "teak":
            case "teak tree":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.TEAK_TREE));
                return;
            case "oak":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.OAK));
                return;
            case "hollow tree":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.HOLLOW_TREE));
                return;
            case "mahogany":
            case "mahogany tree":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAHOGANY_TREE));
                return;
            case "arctic pine":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.ARCTIC_PINE));
                return;
            case "willow":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.WILLOW_TREE));
                return;
            case "maple tree":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAPLE_TREE));
                return;
            case "yew":
            case "wilderness yew tree":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.YEW_TREE));
                return;
            case "magic tree":
            case "wilderness magic tree":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.MAGIC_TREE));
                return;
            case "sulliuscep":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.SULLIUSCEP_TREE));
                return;
            case "redwood":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.REDWOOD_TREE));
                return;
            case "blisterwood tree":
                player.getActionManager().setAction(new Woodcutting(object, TreeDefinitions.BLISTERWOOD, () -> {}));
                return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { "tree", "evergreen", "dead tree", "dying tree", "achey tree", "blisterwood Tree", "jungle tree", "teak", "teak tree", "oak", "hollow tree", "mahogany tree", "mahogany", "arctic pine", "willow", "maple tree", "yew", "magic tree", "redwood", "sulliuscep", "wilderness magic tree", "wilderness yew tree" };
    }
}
