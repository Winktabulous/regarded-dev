package com.near_reality.game.content.gauntlet.plugins;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.OptionsMenuD;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Andys1814.
 * @since 1/23/2022.
 */
public final class GauntletToolStorage implements ObjectAction {

    private enum ToolStorageOption {
        AXE("axe", 23862, 23821),
        PICKAXE("pickaxe", 23863, 23822),
        HARPOON("harpoon", 23864, 23823),
        PESTLE_AND_MORTAR("pestle and mortar", 23865, 23865),
        SCEPTRE("sceptre", 23861, 23820);

        private static final ToolStorageOption[] OPTIONS = values();

        private final String name;

        private final int itemId;

        private final int corruptedItemId;

        ToolStorageOption(String name, int itemId, int corruptedItemId) {
            this.name = name;
            this.itemId = itemId;
            this.corruptedItemId = corruptedItemId;
        }

        private static String[] getOptions() {
            Stream<String> options = Arrays.stream(OPTIONS).map(option -> "Take " + Utils.getAOrAn(option.name) + " " + option.name + ".");
            options = Stream.concat(options, Stream.of("Take everything."));
            return options.toArray(String[]::new);
        }

        private static int[] getItems() {
            return Arrays.stream(OPTIONS).mapToInt(option -> option.itemId).toArray();
        }
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        int tools = 0;
        for (ToolStorageOption tool : ToolStorageOption.OPTIONS) {
            if (player.getInventory().containsItem(tool.itemId)) {
                tools++;
            }
        }

        if (tools == ToolStorageOption.OPTIONS.length) {
            player.getDialogueManager().start(new PlainChat(player, "You do not need anything available here."));
            return;
        }

        player.getDialogueManager().start(new OptionsMenuD(player, "What would you like to take?", ToolStorageOption.getOptions()) {
            @Override
            public void handleClick(int slotId) {
                if (slotId == getOptions().length - 1) { // Take everything option.

                    List<ToolStorageOption> tools = new ArrayList<>();
                    for (ToolStorageOption option : ToolStorageOption.OPTIONS) {
                        if (!player.containsItem(option.itemId)) {
                            tools.add(option);
                        }
                    }

                    if (tools.isEmpty()) {
                        return;
                    }

                    StringBuilder sb = new StringBuilder("You take");
                    for (ToolStorageOption tool : tools) {
                        sb.append(" ");

                        int index = tools.indexOf(tool);
                        if (tools.size() > 1 && index == tools.size() - 1) {
                            sb.append("and ");
                        }

                        sb.append(Utils.getAOrAn(tool.name));
                        sb.append(" ");
                        sb.append(tool.name);

                        if (index != tools.size() - 1) {
                            sb.append(",");
                        } else if (index == tools.size() - 1) {
                            sb.append(".");
                        }
                        boolean isCorrupted = player.getVarManager().getBitValue(9292) == 1;
                        player.getInventory().addOrDrop(isCorrupted ? tool.corruptedItemId : tool.itemId, 1);
                    }

                    player.getDialogueManager().start(new PlainChat(player, sb.toString()));
                    return;
                }

                ToolStorageOption option = ToolStorageOption.OPTIONS[slotId];
                if (option == null) {
                    return;
                }

                if (!player.containsItem(option.itemId)) {
                    player.getInventory().addItem(new Item(option.itemId));
                    player.getDialogueManager().start(new PlainChat(player, "You take " + Utils.getAOrAn(option.name) + " " + option.name + "."));
                }

                player.getDialogueManager().finish();
            }
            @Override
            public boolean cancelOption() {
                return false;
            }
        });
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 36074, 35977 };
    }

}
