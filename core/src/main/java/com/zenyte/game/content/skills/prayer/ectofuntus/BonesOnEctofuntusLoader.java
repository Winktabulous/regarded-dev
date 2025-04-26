package com.zenyte.game.content.skills.prayer.ectofuntus;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

/**
 * @author Kris | 23/06/2019 12:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BonesOnEctofuntusLoader implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        if (item.getId() == 22124) {
            if (player.getSkills().getLevelForXp(SkillConstants.PRAYER) < 70) {
                player.getDialogueManager().start(new ItemChat(player, item, "You need a Prayer level of at least 70 to grind superior dragon bones."));
                return;
            }
        }
        player.getActionManager().setAction(new BoneGrinding(BoneGrinding.Stage.ADDING_BONES, item));
    }

    @Override
    public Object[] getItems() {
        final IntOpenHashSet list = new IntOpenHashSet();
        for (final BoneGrinding.Bonemeal bonemeal : BoneGrinding.Bonemeal.values) {
            for (final Item item : bonemeal.getBones().getItems()) {
                list.add(item.getId());
            }
        }
        return list.toArray();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LOADER };
    }
}
