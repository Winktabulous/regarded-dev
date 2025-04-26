package com.zenyte.plugins.itemonobject;

import com.zenyte.game.content.minigame.blastfurnace.BlastFurnaceArea;
import com.zenyte.game.content.minigame.blastfurnace.BlastFurnaceOre;
import com.zenyte.game.content.skills.smithing.SmeltableBar;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.impl.blastfurnace.ConveyerBeltOreNPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class OreOnConveyorBelt implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        final BlastFurnaceOre ore = BlastFurnaceOre.getOre(item.getId());
        final SmeltableBar bar = SmeltableBar.getDataByBar(ore.getBarId());
        if (!player.getSkills().checkLevel(SkillConstants.SMITHING, bar.getLevel(), "do this")) {
            return;
        }
        if (player.getBlastFurnace().getBar(bar) >= 28) {
            player.getDialogueManager().start(new PlainChat(player, "You should collect your bars before making any more."));
            return;
        }
        final boolean oreOverflow = ore.isPrimaryOre() ? player.getBlastFurnace().getOre(ore) + 1 >= 28 : player.getBlastFurnace().getOre(ore) + 1 >= 254;
        if (oreOverflow || player.getBlastFurnace().checkPrimaryOres() >= 28) {
            player.getDialogueManager().start(new PlainChat(player, "You should make sure all your ore smelts before adding any more."));
            return;
        }
        player.getInventory().deleteItem(slot, item);
        player.getBlastFurnace().setOresOnBelt(player.getBlastFurnace().getOresOnBelt() + 1);
        World.spawnNPC(new ConveyerBeltOreNPC(ore.getNpcId(), BlastFurnaceArea.ORE_CONVEYER_START, Direction.SOUTH, 0, player, item));
    }

    @Override
    public Object[] getItems() {
        final List<Integer> list = new ArrayList<>();
        for (final BlastFurnaceOre ore : BlastFurnaceOre.VALUES) list.add(ore.getItemId());
        return list.toArray();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CONVEYOR_BELT };
    }
}
