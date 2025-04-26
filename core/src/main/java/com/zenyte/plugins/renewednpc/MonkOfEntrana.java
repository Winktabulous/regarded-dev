package com.zenyte.plugins.renewednpc;

import com.google.common.collect.ImmutableList;
import com.zenyte.game.content.sailing.Sailing;
import com.zenyte.game.content.treasuretrails.TreasureTrail;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Kris | 25/11/2018 20:02
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MonkOfEntrana extends NPCPlugin {

    private static final List<Integer> ALLOWED_ITEMS = ImmutableList.of(88, 1033, 1035, 1011, 1013, 1015, 1017, 1019, 1021, 1023, 1025, 1027, 1029, 1031, 1005, 10069, 10071);

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            final boolean toEntrana = npc.getId() <= 1167;
            player.getDialogueManager().start(new Dialogue(player, npc) {

                @Override
                public void buildDialogue() {
                    if (toEntrana) {
                        npc("Do you seek passage to holy Entrana? If so, you must<br>leave your weaponry and armour " + "behind. This is<br>Saradomin's will.");
                        options("What would you like to say?", "No, not right now.", "Yes, okay, I'm ready to go.").onOptionOne(() -> setKey(15)).onOptionTwo(() -> setKey(5));
                        player(5, "Yes, okay, I'm ready to go.");
                        npc("Very well. One moment please.");
                        plain("The monk quickly searches you.").executeAction(() -> {
                            if (!isAllowed(player)) {
                                if (TreasureTrail.talk(player, npc)) {
                                    return;
                                }
                                player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You are not allowed to take any equipment and weaponry to Entrana!"));
                                return;
                            }
                            player.sendMessage("After a quick search, the monk smiles at you and allows you to board.");
                            Sailing.sail(player, "Port Sarim", "Entrana");
                        });
                        player(15, "No, not right now.");
                    } else {
                        npc("Do you seek passage to Port Sarim?");
                        options("What would you like to say?", "No, not right now.", "Yes, okay, I'm ready to go.").onOptionOne(() -> setKey(15)).onOptionTwo(() -> setKey(5));
                        player(5, "Yes, okay, I'm ready to go.");
                        npc("Very well. One moment please.");
                        plain("The monk quickly searches you.").executeAction(() -> {
                            player.sendMessage("You board the ship and sail to Port Sarim.");
                            Sailing.sail(player, "Entrana", "Port Sarim");
                        });
                        player(15, "No, not right now.");
                    }
                }
            });
        });
        bind("Take-boat", (player, npc) -> {
            final boolean toEntrana = npc.getId() <= 1167;
            if (toEntrana && !isAllowed(player)) {
                player.getDialogueManager().start(new NPCChat(player, npc.getId(), "You are not allowed to take any equipment and weaponry to Entrana!"));
                return;
            }
            player.sendMessage(toEntrana ? "After a quick search, the monk smiles at you and allows you to board." : "You board the ship and sail to Port Sarim.");
            if (toEntrana) {
                Sailing.sail(player, "Port Sarim", "Entrana");
            } else {
                Sailing.sail(player, "Entrana", "Port Sarim");
            }
        });
    }

    private static final boolean isJewellery(final Item item) {
        final String name = item.getName().toLowerCase();
        return name.contains("amulet") || name.contains("ring") || name.contains("necklace") || name.contains("bracelet") || item.getName().contains("pendant") || item.getName().contains("symbol");
    }

    public static boolean isAllowed(final Player player) {
        return Stream.of(player.getEquipment().getContainer().getItems().values(), player.getInventory().getContainer().getItems().values()).flatMap(Collection::stream).noneMatch(MonkOfEntrana::isForbiddenOnEntrana);
    }

    public static final boolean isForbiddenOnEntrana(@Nullable final Item item) {
        return Objects.nonNull(item) && Objects.nonNull(item.getDefinitions()) && (item.isWieldable() && !isJewellery(item) && !ALLOWED_ITEMS.contains(item.getId()) && !item.getName().startsWith("Graceful"));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.MONK_OF_ENTRANA, NpcId.MONK_OF_ENTRANA_1166, NpcId.MONK_OF_ENTRANA_1167, NpcId.MONK_OF_ENTRANA_1168, NpcId.MONK_OF_ENTRANA_1169 };
    }
}
