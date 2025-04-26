package com.zenyte.plugins.renewednpc;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnNPCAction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.types.config.items.ItemDefinitions;

import static com.zenyte.plugins.renewednpc.EdgevilleEmblemTrader.promptSkull;

/**
 * @author Kris | 24/01/2019 16:22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EmblemTrader extends NPCPlugin implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Would you like to sell your artefact to me?");
                options("Sell your artefact?", new DialogueOption("Sell the artefact.", () -> sell(player, item, npc)), new DialogueOption("Keep the artefact.", key(15)));
                player(15, "I'd rather keep it.");
                npc("Alright. If you change your mind, you know where to find me.");
            }
        });
    }

    private static void sell(final Player player, final Item item, final NPC npc) {
        player.getDialogueManager().finish();
        final Inventory inventory = player.getInventory();
        final int id = item.getId();
        final int notedId = item.getDefinitions().getUnnotedOrDefault();
        for (final EmblemTrader.AncientArtefact artefact : AncientArtefact.values) {
            if (artefact.id == id || artefact.id == notedId) {
                long amount = (long) item.getAmount() * (long) artefact.coins;
                if (amount > Integer.MAX_VALUE) {
                    amount = (Integer.MAX_VALUE / artefact.coins);
                }
                inventory.deleteItem(new Item(item.getId(), (int) amount / artefact.coins));
                inventory.addItem(new Item(995, (int) amount)).onFailure(rem -> World.spawnFloorItem(rem, player));
                break;
            }
        }
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Pleasure doing business with you.");
            }
        });
    }

    @Override
    public Object[] getItems() {
        final ObjectArrayList<Integer> list = new ObjectArrayList<Integer>();
        for (final EmblemTrader.AncientArtefact artefact : AncientArtefact.values) {
            list.add(artefact.id);
            list.add(ItemDefinitions.getOrThrow(artefact.id).getNotedId());
        }
        return list.toArray();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.APPLE_TREE_7941, ObjectId.APPLE_TREE_7943 };
    }

    private enum AncientArtefact {

        EMBLEM(21807, 500_000),
        TOTEM(21810, 1_000_000),
        STATUETTE(21813, 2_000_000),
        MEDALLION(22299, 4_000_000),
        EFFIGY(22302, 8_000_000),
        RELIC(22305, 16_000_000);

        private static final AncientArtefact[] values = values();

        private final int id;

        private final int coins;

        AncientArtefact(int id, int coins) {
            this.id = id;
            this.coins = coins * 2;
        }
    }

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    npc("Hello, Wanderer.");
                    npc("Don't suppose you've come across any strange... emblems or artifacts along your journey?");
                    if (!hasArtefact(player)) {
                        player("Not that I've seen.");
                        npc("If you do, please do let me know. I'll reward you handsomely.");
                        return;
                    }
                    player("Yes I have.");
                    npc("Would you like to sell them to me?");
                    options("Sell all your artefacts?", new DialogueOption("Sell the artefacts.", () -> sell(player, npc)), new DialogueOption("Keep the artefacts.", key(15)));
                    player(15, "I'd rather keep them.");
                    npc("Alright. If you change your mind, you know where to find me.");
                }
            });
        });
        bind("Skull", (player, npc) -> promptSkull(player));
    }

    private static void sell(final Player player, final NPC npc) {
        player.getDialogueManager().finish();
        final Inventory inventory = player.getInventory();
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null)
                continue;
            final int id = item.getId();
            final int notedId = item.getDefinitions().getUnnotedOrDefault();
            for (final EmblemTrader.AncientArtefact artefact : AncientArtefact.values) {
                if (artefact.id == id || artefact.id == notedId) {
                    long amount = (long) item.getAmount() * (long) artefact.coins;
                    if (amount > Integer.MAX_VALUE) {
                        amount = (Integer.MAX_VALUE / artefact.coins);
                    }
                    inventory.deleteItem(new Item(item.getId(), (int) amount / artefact.coins));
                    inventory.addItem(new Item(995, (int) amount)).onFailure(rem -> World.spawnFloorItem(rem, player));
                }
            }
        }
        player.getDialogueManager().start(new Dialogue(player, npc) {

            @Override
            public void buildDialogue() {
                npc("Pleasure doing business with you.");
            }
        });
    }

    private static boolean hasArtefact(final Player player) {
        final Inventory inventory = player.getInventory();
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null)
                continue;
            final int id = item.getId();
            final int notedId = item.getDefinitions().getUnnotedOrDefault();
            for (final EmblemTrader.AncientArtefact artefact : AncientArtefact.values) {
                if (artefact.id == id || artefact.id == notedId) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 7943 };
    }
}
