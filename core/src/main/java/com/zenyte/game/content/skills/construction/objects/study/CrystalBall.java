package com.zenyte.game.content.skills.construction.objects.study;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.SkillDialogue;

import java.util.HashMap;
import java.util.Map;

import static com.zenyte.game.content.skills.magic.spells.MagicSpell.*;

/**
 * @author Kris | 25. veebr 2018 : 15:23.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CrystalBall implements ItemOnObjectAction {

    private enum Staff {

        STAFF_OF_AIR(1381, new Animation(4043), new Animation(4044)),
        STAFF_OF_WATER(1383, new Animation(4047), new Animation(4048)),
        STAFF_OF_EARTH(1385, new Animation(4045), new Animation(4046)),
        STAFF_OF_FIRE(1387, new Animation(4049), new Animation(4050)),
        AIR_BATTLESTAFF(1397, new Animation(4051), new Animation(4052), new Item(AIR_RUNE, 100)),
        WATER_BATTLESTAFF(1395, new Animation(4055), new Animation(4056), new Item(WATER_RUNE, 100)),
        EARTH_BATTLESTAFF(1399, new Animation(4053), new Animation(4054), new Item(EARTH_RUNE, 100)),
        FIRE_BATTLESTAFF(1393, new Animation(4057), new Animation(4058), new Item(FIRE_RUNE, 100)),
        MYSTIC_AIR_STAFF(1405, new Animation(4059), new Animation(4060), new Item(AIR_RUNE, 1000)),
        MYSTIC_WATER_STAFF(1403, new Animation(4063), new Animation(4064), new Item(WATER_RUNE, 1000)),
        MYSTIC_EARTH_STAFF(1407, new Animation(4061), new Animation(4062), new Item(EARTH_RUNE, 1000)),
        MYSTIC_FIRE_STAFF(1401, new Animation(4065), new Animation(4066), new Item(FIRE_RUNE, 1000));

        private final int staffId;

        private final Animation start, end;

        private final Item cost;

        private static final Item[][] STAFF_GROUPS = new Item[][] { new Item[] { new Item(1381), new Item(1383), new Item(1385), new Item(1387) }, new Item[] { new Item(1397), new Item(1395), new Item(1399), new Item(1393) }, new Item[] { new Item(1405), new Item(1403), new Item(1407), new Item(1401) } };

        private static final Staff[] VALUES = values();

        private static final Map<Integer, Staff> MAP = new HashMap<Integer, Staff>(VALUES.length);

        static {
            for (Staff value : VALUES) MAP.put(value.staffId, value);
        }

        Staff(final int staffId, final Animation start, final Animation end) {
            this(staffId, start, end, null);
        }

        Staff(final int staffId, final Animation start, final Animation end, final Item cost) {
            this.staffId = staffId;
            this.start = start;
            this.end = end;
            this.cost = cost;
        }
    }

    private static final class StaffDialogue extends SkillDialogue {

        public StaffDialogue(Player player, Item[] items, final Staff staff) {
            super(player, "Choose an element.", items);
            this.staff = staff;
        }

        private final Staff staff;

        @Override
        public void run(final int slotId, final int amount) {
            if (slotId < 0 || slotId >= items.length)
                return;
            final Staff toStaff = Staff.MAP.get(items[slotId].getId());
            if (toStaff == null)
                return;
            if (toStaff == staff) {
                player.sendMessage("This staff already is of that element.");
                return;
            }
            player.getActionManager().setAction(new StaffAction(staff, toStaff, amount));
        }
    }

    private static final class StaffAction extends Action {

        public StaffAction(final Staff fromStaff, final Staff toStaff, final int amount) {
            this.fromStaff = fromStaff;
            this.toStaff = toStaff;
            this.amount = amount;
        }

        private final Staff fromStaff, toStaff;

        private int amount;

        @Override
        public boolean start() {
            if (toStaff.cost != null) {
                if (!player.getInventory().containsItem(toStaff.cost)) {
                    player.sendMessage("You need at least " + toStaff.cost.getAmount() + " " + toStaff.cost.getName() + "s to change the staves' element.");
                    return false;
                }
            }
            if (!player.getInventory().containsItem(fromStaff.staffId, 1)) {
                final String name = fromStaff.toString().toLowerCase().replaceAll("_", " ");
                player.sendMessage("You need " + (Utils.startWithVowel(name) ? "an " : "a ") + name + " to do this.");
                return false;
            }
            return true;
        }

        @Override
        public boolean process() {
            return true;
        }

        @Override
        public int processWithDelay() {
            if (amount-- <= 0)
                return -1;
            if (toStaff.cost != null && !player.getInventory().containsItem(toStaff.cost))
                return -1;
            if (!player.getInventory().containsItem(fromStaff.staffId, 1))
                return -1;
            player.setAnimation(fromStaff.start);
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    player.setAnimation(toStaff.end);
                    player.getInventory().deleteItem(fromStaff.staffId, 1);
                    player.getInventory().addItem(toStaff.staffId, 1);
                    if (toStaff.cost != null)
                        player.getInventory().deleteItem(toStaff.cost);
                }
            });
            return 2;
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (player.getCurrentHouse() == null)
            return;
        if (player.getCurrentHouse().isBuildingMode()) {
            player.sendMessage("You can't do this in build mode.");
            return;
        }
        final Staff staff = Staff.MAP.get(item.getId());
        if (staff == null)
            return;
        if (object.getId() == 13659 && item.getId() > 1387) {
            player.sendMessage("You can only change the element of basic staves on this crystal ball.");
            return;
        } else if (object.getId() == 13660 && item.getId() > 1399) {
            player.sendMessage("You can only change the element of basic staves and battlestaves on this elemental sphere.");
            return;
        }
        player.getDialogueManager().start(new StaffDialogue(player, Staff.STAFF_GROUPS[staff.ordinal() / 4], staff));
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1381, 1383, 1385, 1387, 1393, 1395, 1397, 1399, 1401, 1403, 1405, 1407 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CRYSTAL_BALL_13659, ObjectId.ELEMENTAL_SPHERE, ObjectId.CRYSTAL_OF_POWER };
    }
}
