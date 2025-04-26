package com.zenyte.plugins.object;

import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.content.boons.impl.BoneCruncher;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.content.skills.prayer.actions.Bones;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentUtils;
import com.zenyte.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;

/**
 * @author Kris | 28. apr 2018 : 16:21.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class AltarOPlugin implements ObjectAction, ItemOnObjectAction {

    public static final Animation PRAY_ANIM = new Animation(645);

    private static final int HOME_ALTAR_OBJ = 409;

    private static final float HOME_ALTAR_PRAYER_XP_MOD = 3.5f;

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Pray-at") || option.equals("Pray")) {
            if (player.getPrayerManager().getPrayerPoints() >= player.getSkills().getLevelForXp(SkillConstants.PRAYER)) {
                player.sendMessage("You already have full prayer points.");
                return;
            }
            final int toRestore = player.getSkills().getLevelForXp(SkillConstants.PRAYER) - player.getPrayerManager().getPrayerPoints();
            if (EquipmentUtils.containsFullInitiate(player) && object.getId() == 410) {
                player.getAchievementDiaries().update(FaladorDiary.PRAY_ALTAR_OF_GUTHIX);
            } else if (EquipmentUtils.containsFullProselyte(player)) {
                player.getAchievementDiaries().update(FaladorDiary.RECHARGE_PRAYER);
            } else if (object.getId() == 10389) {
                player.getAchievementDiaries().update(DesertDiary.PRAY_AT_ELIDINIS_STATUETTE);
            } else if (toRestore >= 85 && object.getId() == 20377) {
                player.getAchievementDiaries().update(DesertDiary.RESTORE_85_PRAYER_POINTS);
            } else if (player.getPrayerManager().isActive(Prayer.SMITE)) {
                player.getAchievementDiaries().update(VarrockDiary.PRAY_AT_VARROCK_ALTAR);
                player.getAchievementDiaries().update(LumbridgeDiary.RECHARGE_PRAYER);
            }
            player.getAchievementDiaries().update(KourendDiary.PRAY_AT_ALTAR);
            player.getAchievementDiaries().update(WildernessDiary.PRAY_AT_CHAOS_ALTAR);
            player.getAchievementDiaries().update(ArdougneDiary.USE_EAST_ARDOUGNE_ALTAR);
            player.lock();
            player.sendMessage("You pray to the gods...");
            player.setAnimation(PRAY_ANIM);
            player.sendSound(2674);
            WorldTasksManager.schedule(() -> {
                player.getPrayerManager().restorePrayerPoints(99);
                player.sendMessage("... and recharge your prayer.");
                player.unlock();
            });
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        /* only home altar can be used as gilded altar */
        if (object.getId() != HOME_ALTAR_OBJ) {
            player.sendMessage("Nothing interesting happens.");
            return;
        }
        Bones bone = Bones.getBone(item.getId());
        if (bone == null) {
            player.sendMessage("You can only offer bones to the gods.");
            return;
        }
        player.getActionManager().setAction(new OfferingAction(bone, item, object, HOME_ALTAR_PRAYER_XP_MOD));
    }

    @Override
    public Object[] getItems() {
        final ArrayList<Object> list = new ArrayList<>(Bones.VALUES.length);
        for (final Bones bone : Bones.VALUES) {
            for (final Item b : bone.getItems()) {
                list.add(b.getId());
            }
        }
        return list.toArray(new Object[0]);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { "Altar of Guthix", "Chaos altar", "Altar", ObjectId.ALTAR_20377, 18258, ObjectId.ALTAR};
    }

    private static final class OfferingAction extends Action {

        private static final String OFFERING_MESSAGE = "The gods are very pleased with your offering.";

        private static final Animation OFFERING_ANIM = new Animation(3705);

        private static final Graphics OFFERING_GFX = new Graphics(624);

        public OfferingAction(final Bones bone, final Item item, final WorldObject altar, final float modifier) {
            this.bone = bone;
            this.item = item;
            this.altar = altar;
            this.modifier = modifier;
        }

        private final Item item;

        private final Bones bone;

        private final WorldObject altar;

        private final float modifier;

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
            if (bone.equals(Bones.DRAGON_BONES)) {
                player.getDailyChallengeManager().update(SkillingChallenge.OFFER_DRAGON_BONES);
            }
            if(player.getBoonManager().hasBoon(BoneCruncher.class) && BoneCruncher.roll()) {
                player.sendFilteredMessage("You sacrifice the " + bone.getName() + ".");
                player.sendFilteredMessage(Colour.RS_GREEN.wrap("Your Bone Cruncher boon has saved your resources"));
            } else {
                player.getInventory().deleteItem(item);
                player.sendFilteredMessage("You sacrifice the " + bone.getName() + ".");
            }
            player.getSkills().addXp(SkillConstants.PRAYER, bone.getXp() * modifier);
            World.sendGraphics(OFFERING_GFX, altar);
            return 3;
        }
    }
}
