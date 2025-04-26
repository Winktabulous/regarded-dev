package com.zenyte.plugins.object;

import com.zenyte.game.content.boons.impl.BoneCruncher;
import com.zenyte.game.content.skills.prayer.actions.Bones;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.Objects;

/**
 * @author Kris | 25/04/2019 20:42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ChaosAltar implements ItemOnObjectAction {

    private static final Location tile = new Location(2947, 3820, 0);
    private static final Location LEGENDARY_TILE = new Location(3381, 7570, 0);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CHAOS_ALTAR_411 };
    }

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
        if (!object.matches(tile) && !object.matches(LEGENDARY_TILE)) {
            player.sendMessage("Nothing interesting happens.");
            return;
        }
        final Bones bone = Bones.getBone(item.getId());
        if (bone == null) {
            player.sendMessage("You can only offer bones to the gods.");
            return;
        }
        player.getActionManager().setAction(new OfferingAction(bone, item, object));
    }

    @Override
    public Object[] getItems() {
        final ArrayList<Object> list = new ArrayList<Object>(Bones.VALUES.length);
        for (final Bones bone : Bones.VALUES) {
            for (final Item b : bone.getItems()) {
                list.add(b.getId());
            }
        }
        return list.toArray(new Object[0]);
    }

    private static final class OfferingAction extends Action {

        private static final String CHAOS_ALTAR_MESSAGE = "The Dark Lord spares your sacrifice but still rewards you for your efforts.";

        private static final Animation OFFERING_ANIM = new Animation(3705);

        OfferingAction(final Bones bone, final Item item, final WorldObject altar) {
            this.bone = bone;
            this.item = item;
            this.altar = altar;
        }

        private final Item item;

        private final Bones bone;

        private final WorldObject altar;

        @Override
        public boolean initiateOnPacketReceive() {
            return true;
        }

        @Override
        public boolean start() {
            if (!player.getInventory().containsItem(item)) {
                player.sendMessage("You don't have any " + item.getName().toLowerCase() + " to sacrifice.");
                return false;
            }
            if (bone == Bones.SUPERIOR_DRAGON_BONES) {
                if (player.getSkills().getLevelForXp(SkillConstants.PRAYER) < 70) {
                    player.sendMessage("You need a Prayer level of at least 70 to sacrifice superior dragon bones.");
                    return false;
                }
            }
            return true;
        }

        @Override
        public void stop() {
            player.getActionManager().setActionDelay(1);
        }

        @Override
        public boolean process() {
            return true;
        }

        @Override
        public int processWithDelay() {
            if (!player.getInventory().containsItem(item)) {
                return -1;
            }
            player.setAnimation(OFFERING_ANIM);
            player.faceObject(altar);
            if (bone.equals(Bones.SUPERIOR_DRAGON_BONES)) {
                player.getDailyChallengeManager().update(SkillingChallenge.OFFER_SUPERIOR_DRAGON_BONES_CHAOS_ALTAR);
            }
            if (Utils.random(1) == 0) {
                player.sendFilteredMessage(CHAOS_ALTAR_MESSAGE);
            } else {
                if(player.getBoonManager().hasBoon(BoneCruncher.class) && BoneCruncher.roll()) {
                    player.sendFilteredMessage("You sacrifice the " + bone.getName() + ".");
                    player.sendFilteredMessage(Colour.RS_GREEN.wrap("Your Bone Cruncher boon has saved your resources"));
                } else {
                    player.getInventory().deleteItem(item);
                    player.sendFilteredMessage("You sacrifice the " + bone.getName() + ".");
                }
            }
            player.getSkills().addXp(SkillConstants.PRAYER, bone.getXp() * 3.5F);
            return 3;
        }
    }
}
