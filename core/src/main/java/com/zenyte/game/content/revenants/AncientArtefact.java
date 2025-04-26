package com.zenyte.game.content.revenants;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public enum AncientArtefact {

    EMBLEM(21807, 500000),
    TOTEM(21810, 1000000),
    STATUETTE(21813, 2000000),
    MEDALLION(22299, 4000000),
    EFFIGY(22302, 8000000),
    RELIC(22305, 16000000);

    private static final AncientArtefact[] values = values();

    private final int id;

    private final int coins;

    AncientArtefact(int id, int coins) {
        this.id = id;
        this.coins = coins;
    }

    public static void sell(final Player player) {
        player.getDialogueManager().finish();
        final Inventory inventory = player.getInventory();
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null)
                continue;
            final int id = item.getId();
            final int notedId = item.getDefinitions().getUnnotedOrDefault();
            for (final AncientArtefact artefact : AncientArtefact.values) {
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
        player.sendMessage("You have exchanged all your emblems");
    }

    public static boolean hasArtefact(final Player player) {
        final Inventory inventory = player.getInventory();
        for (int i = 0; i < 28; i++) {
            final Item item = inventory.getItem(i);
            if (item == null)
                continue;
            final int id = item.getId();
            final int notedId = item.getDefinitions().getUnnotedOrDefault();
            for (final AncientArtefact artefact : AncientArtefact.values) {
                if (artefact.id == id || artefact.id == notedId) {
                    return true;
                }
            }
        }
        return false;
    }
}