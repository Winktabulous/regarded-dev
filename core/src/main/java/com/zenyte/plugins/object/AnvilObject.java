package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.smithing.BarbarianWeapon;
import com.zenyte.game.content.skills.smithing.Smithing;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.dialogue.skills.AncientForgeD;
import com.zenyte.plugins.dialogue.skills.BarbarianSmithingD;
import com.zenyte.plugins.dialogue.skills.BluriteSmithingD;
import com.zenyte.plugins.interfaces.SmithingInterface;

import static com.zenyte.game.world.entity.player.privilege.MemberRank.PREMIUM;

public class AnvilObject implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (object.getId() == ObjectId.ANVIL_32215) {
            player.getDialogueManager().start(new AncientForgeD(player));
            return;
        }
        if (object.getName().equals("Barbarian anvil")) {
            BarbarianWeapon weapon = null;
            for (int i = 0; i < 28; i++) {
                final Item item = player.getInventory().getItem(i);
                if (item == null) {
                    continue;
                }
                weapon = BarbarianWeapon.get(item.getId());
                if (weapon != null) {
                    break;
                }
            }
            if (weapon != null) {
                player.getDialogueManager().start(new BarbarianSmithingD(player, weapon));
            } else {
                player.sendMessage("You don't have the right materials with you right now.");
            }
            return;
        }

        boolean skipLevelCheck = false;

        if (player.getMemberRank().equalToOrGreaterThan(PREMIUM) && object.getLocation().equals(3358, 8156, 2)) {
            skipLevelCheck = true;
        }

        if (object.getId() == ObjectId.ANVIL_6150 && !skipLevelCheck) {
            if (player.getSkills().getLevel(SkillConstants.SMITHING) < 60) {
                player.getDialogueManager().start(new NPCChat(player, NpcId.BLAST_FURNACE_FOREMAN, "Ay mate, you need a Smithing level of at least 60 before I let you use those anvils."));
                return;
            }
        }

        if (!player.getInventory().containsAnyOf(Smithing.BARS) || !player.getInventory().containsItems(Smithing.HAMMER) || Smithing.getBarInInventory(player) == null) {
            if (!player.getInventory().containsAnyOf(Smithing.BARS) || Smithing.getBarInInventory(player) == null) {
                player.getDialogueManager().start(new PlainChat(player, "You should select an item from your inventory and use it on the anvil."));
            } else if (!player.getInventory().containsItems(Smithing.HAMMER)) {
                player.getDialogueManager().start(new PlainChat(player, "You need a hammer to work the metal with."));
            } else {
                player.getDialogueManager().start(new PlainChat(player, "You should select an item from your inventory and use it on the anvil."));
            }
        } else {
            if (Smithing.getBarInInventory(player) != null && Smithing.getBarInInventory(player).getId() == 9467) {
                player.getDialogueManager().start(new BluriteSmithingD(player, object.getId()));
            } else {
                SmithingInterface.openInterface(player, SmithingInterface.getTierForBar(Smithing.getBarInInventory(player)), object.getId());
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { "Anvil", "Barbarian anvil" };
    }
}
