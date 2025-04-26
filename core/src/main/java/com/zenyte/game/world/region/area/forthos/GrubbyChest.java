package com.zenyte.game.world.region.area.forthos;

import com.zenyte.game.content.drops.table.RollResult;
import com.zenyte.game.content.drops.table.impl.ImmutableDropTable;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

import java.util.function.Consumer;

/**
 * @author Andys1814
 */
public final class GrubbyChest implements ObjectAction {

    private static final ImmutableDropTable food = new ImmutableDropTable();

    private static final ImmutableDropTable potions = new ImmutableDropTable();

    private static final ImmutableDropTable main = new ImmutableDropTable();

    static {
        food.append(ItemId.EGG_POTATO, 6, 4);
        food.append(ItemId.SHARK, 5, 4);
        food.append(ItemId.SARADOMIN_BREW2, 1, 3);
        food.append(ItemId.SUPER_RESTORE2, 1, 1);

        potions.append(ItemId.SUPER_DEFENCE2, 9, 1);
        potions.append(ItemId.SUPER_ATTACK2, 9, 1);
        potions.append(ItemId.SUPER_STRENGTH2, 9, 1);
        potions.append(ItemId.RANGING_POTION2, 9, 1);
        potions.append(ItemId.PRAYER_POTION3, 3, 1);
        potions.append(ItemId.SUPER_RESTORE3, 1, 1);

        main.append(ItemId.LAW_RUNE, 10, 200);
        main.append(ItemId.DEATH_RUNE, 10, 200);
        main.append(ItemId.ASTRAL_RUNE, 10, 200);
        main.append(ItemId.BLOOD_RUNE, 10, 200);
        main.append(ItemId.GRIMY_TOADFLAX, 8, 10);
        main.append(ItemId.GRIMY_RANARR_WEED, 8, 10);
        main.append(ItemId.COINS_995, 8, 10_000);
        main.append(ItemId.GRIMY_SNAPDRAGON, 7, 10);
        main.append(ItemId.GRIMY_TORSTOL, 7, 5);
        main.append(ItemId.CRYSTAL_KEY, 6, 1);
        main.append(ItemId.DRAGON_BONES, 4, 10);
        main.append(ItemId.RED_DRAGONHIDE, 4, 10);
        main.append(ItemId.DRAGON_DART_TIP, 1, 50);
        main.append(ItemId.DRAGON_ARROWTIPS, 1, 100);
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (optionId) {
            case 1:
                open(player);
                return;
            case 2:
                check(player);
                return;
        }
    }

    private static void open(Player player) {
        if (!player.getInventory().containsItem(ItemId.GRUBBY_KEY)) {
            player.sendMessage("You don't have a key which can open this chest.");
            return;
        }

        Consumer<Player> open = (p) -> {
            p.getInventory().deleteItem(ItemId.GRUBBY_KEY, 1);

            RollResult[] foods = food.roll(2);
            RollResult potion = potions.roll();
            RollResult mainRoll = main.roll();

            for (RollResult result : foods) {
                p.getInventory().addOrDrop(new Item(result.getId(), result.getQuantity()));
            }
            p.getInventory().addOrDrop(new Item(potion.getId(), potion.getQuantity()));
            p.getInventory().addOrDrop(new Item(mainRoll.getId(), mainRoll.getQuantity()));

            Item gotSac = null;
            if (Utils.randomBoolean(24)) {
                Item sac = new Item(ItemId.ORANGE_EGG_SAC);
                p.getInventory().addOrDrop(sac);
                p.getCollectionLog().add(sac);
                gotSac = sac;
            } else if (Utils.randomBoolean(24)) {
                Item sac = new Item(ItemId.BLUE_EGG_SAC);
                p.getInventory().addOrDrop(sac);
                p.getCollectionLog().add(sac);
                gotSac = sac;
            }

            Item finalGotSac = gotSac;
            p.getDialogueManager().start(new Dialogue(p) {
                @Override
                public void buildDialogue() {
                    if (finalGotSac != null) {
                        doubleItem(finalGotSac.getId(), mainRoll.getId(), "You find treasure, supplies, and a weirdly colored egg sac within the chest.");
                    } else {
                        doubleItem(Utils.random(foods).getId(), mainRoll.getId(), "You find treasure and supplies within the chest.");
                    }
                }
            });

            p.incrementNumericAttribute("grubby_chest_opens", 1);
            player.setAnimation(new Animation(832));
        };

        if (player.getInventory().getFreeSlots() < 15) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("You do require at least 15 spaces in your inventory to loot this chest.<br><col=FF0000>Any items you cannot hold will be dropped to the floor.", new DialogueOption("Open anyway", () -> {
                        open.accept(player);
                    }), new DialogueOption("Do not open"));
                }
            });
        } else {
            open.accept(player);
        }
    }

    private static void check(Player player) {
        int opens = player.getNumericAttribute("grubby_chest_opens").intValue();
        player.sendMessage("You have opened the Grubby Chest " + opens + " times.");
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 34901 };
    }

}
