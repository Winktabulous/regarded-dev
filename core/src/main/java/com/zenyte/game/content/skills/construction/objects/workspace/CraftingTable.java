package com.zenyte.game.content.skills.construction.objects.workspace;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.SkillDialogue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 24. veebr 2018 : 19:50.34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CraftingTable implements ObjectInteraction {

    private enum Craftable {

        CLOCKWORK(new Item(8792), 8, 15, new Item(2353)),
        TOY_HORSEY(new Item(2526), 10, 15, new Item(960)),
        TOY_SOLDIER(new Item(7759), 13, 15, new Item(8792), new Item(960)),
        WOODEN_CAT(new Item(10891), 16, 15, new Item(960), new Item(6814)),
        TOY_DOLL(new Item(7763), 18, 15, new Item(8792), new Item(960)),
        SEXTANT(new Item(2574), 23, 15, new Item(2353)),
        WATCH(new Item(2575), 28, 15, new Item(8792), new Item(2353)),
        CLOCKWORK_SUIT(new Item(10595), 30, 15, new Item(8792), new Item(960), new Item(950)),
        TOY_MOUSE(new Item(7767), 33, 15, new Item(8792), new Item(960)),
        TOY_CAT(new Item(7771), 85, 15, new Item(8792), new Item(860));

        private static final Craftable[] VALUES = values();

        private static final Map<Integer, Craftable> MAP = new HashMap<Integer, Craftable>(VALUES.length);

        static {
            for (Craftable craft : VALUES) MAP.put(craft.item.getId(), craft);
        }

        private final Item item;

        private final int level;

        private final double experience;

        private final Item[] materials;

        Craftable(final Item item, final int level, final double experience, final Item... materials) {
            this.item = item;
            this.level = level;
            this.experience = experience;
            this.materials = materials;
        }

        public Item getItem() {
            return item;
        }

        public int getLevel() {
            return level;
        }

        public double getExperience() {
            return experience;
        }

        public Item[] getMaterials() {
            return materials;
        }
    }

    private static final Item[][] CRAFTING_TABLES = new Item[][] { new Item[] { Craftable.TOY_HORSEY.getItem(), Craftable.WOODEN_CAT.getItem() }, new Item[] { Craftable.CLOCKWORK.getItem(), Craftable.TOY_HORSEY.getItem(), Craftable.WOODEN_CAT.getItem() }, new Item[] { Craftable.CLOCKWORK.getItem(), Craftable.TOY_HORSEY.getItem(), Craftable.TOY_SOLDIER.getItem(), Craftable.WOODEN_CAT.getItem(), Craftable.TOY_DOLL.getItem(), Craftable.CLOCKWORK_SUIT.getItem() }, new Item[] { Craftable.CLOCKWORK.getItem(), Craftable.TOY_HORSEY.getItem(), Craftable.TOY_SOLDIER.getItem(), Craftable.WOODEN_CAT.getItem(), Craftable.TOY_DOLL.getItem(), Craftable.SEXTANT.getItem(), Craftable.WATCH.getItem(), Craftable.CLOCKWORK_SUIT.getItem(), Craftable.TOY_MOUSE.getItem(), Craftable.TOY_CAT.getItem() } };

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CLOCKMAKERS_BENCH, ObjectId.CLOCKMAKERS_BENCH_6797, ObjectId.CLOCKMAKERS_BENCH_6798, ObjectId.CLOCKMAKERS_BENCH_6799 };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (construction.isBuildingMode()) {
            player.sendMessage("You can't do this in build mode.");
            return;
        }
        player.getDialogueManager().start(new CraftingBenchD(player, CRAFTING_TABLES[object.getId() - 6796]));
    }

    private static final class CraftingBenchD extends SkillDialogue {

        public CraftingBenchD(Player player, Item[] items) {
            super(player, "What would you like to make?", items);
        }

        @Override
        public void run(int slotId, int amount) {
            if (slotId < 0 || slotId >= items.length)
                return;
            if (amount <= 0)
                return;
            final Item item = items[slotId];
            if (item == null)
                return;
            final Craftable craftable = Craftable.MAP.get(item.getId());
            if (craftable == null)
                return;
            player.getActionManager().setAction(new CraftingAction(craftable, amount));
        }
    }

    private static final class CraftingAction extends Action {

        private static final Animation SITTING_DOWN = new Animation(4103);

        private static final Animation SITTING_WORKING = new Animation(4109);

        private static final Animation STANDING_UP = new Animation(4105);

        public CraftingAction(final Craftable craftable, final int amount) {
            this.craftable = craftable;
            this.amount = amount;
        }

        private final Craftable craftable;

        private int amount;

        @Override
        public boolean start() {
            if (!player.getInventory().containsItems(craftable.getMaterials())) {
                final StringBuilder builder = new StringBuilder();
                for (Item i : craftable.getMaterials()) builder.append((Utils.startWithVowel(i.getName()) ? "an " : "a ") + i.getName().toLowerCase() + ", ");
                builder.delete(builder.length() - 2, builder.length());
                player.sendMessage("You need " + builder + " and " + craftable.getLevel() + " Crafting to craft a " + craftable.toString().toLowerCase().replaceAll("_", " ") + ".");
                return false;
            }
            if (player.getSkills().getLevel(SkillConstants.CRAFTING) < craftable.getLevel()) {
                player.sendMessage("You need a Crafting level of at least " + craftable.getLevel() + " to craft a " + craftable.toString().toLowerCase().replaceAll("_", " ") + ".");
                return false;
            }
            player.setAnimation(SITTING_DOWN);
            this.delay(1);
            return true;
        }

        @Override
        public boolean process() {
            return true;
        }

        @Override
        public int processWithDelay() {
            if (amount == -1)
                return -1;
            amount--;
            player.setAnimation(SITTING_WORKING);
            for (Item material : craftable.getMaterials()) player.getInventory().deleteItem(material);
            player.getInventory().addItem(craftable.getItem());
            player.getSkills().addXp(SkillConstants.CRAFTING, craftable.getExperience());
            if (amount <= 0 || !player.getInventory().containsItems(craftable.getMaterials())) {
                amount = -1;
                return 2;
            }
            return 2;
        }

        @Override
        public void stop() {
            player.setAnimation(STANDING_UP);
        }
    }
}
