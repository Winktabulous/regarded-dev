package com.zenyte.game.content.donation;

import com.zenyte.game.content.skills.runecrafting.Runecrafting;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.degradableitems.ChargesManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Analytics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.plugins.dialogue.OptionsMenuD;
import com.zenyte.utils.StringUtilities;

import java.util.Arrays;

public enum DonationToggle {
    NOTE_BONES() {
        @Override
        public boolean matches(Item item) {
            return PICKUP_BONES.matches(item);
        }
    },
    NOTE_GEMS() {
        @Override
        public boolean matches(Item item) {
            return PICKUP_GEMS.matches(item);
        }
    },
    NOTE_HERBS() {
        @Override
        public boolean matches(Item item) {
            return PICKUP_HERBS.matches(item);
        }
    },
    NOTE_ENSOULED_HEADS() {
        @Override
        public boolean matches(Item item) {
            return PICKUP_ENSOULED_HEADS.matches(item);
        }
    },
    NOTE_DHIDES() {
        @Override
        public boolean matches(Item item) {
            return PICKUP_DHIDE.matches(item);
        }
    },
    PICKUP_BONES(true) {
        @Override
        public boolean matches(Item item) {
            return item.getName().toLowerCase().contains("bones");
        }
    },
    PICKUP_CLUES(true) {
        @Override
        public boolean matches(Item item) {
            return item.getName().startsWith("Scroll box") ||item.getName().contains("clue");
        }
    },
    PICKUP_HERBS(true) {
        @Override
        public boolean matches(Item item) {
            return item.getName().contains("Grimy");
        }
    },
    PICKUP_GEMS(true) {
        @Override
        public boolean matches(Item item) {
            return item.getName().contains("Uncut");
        }
    },
    PICKUP_RUNES(true) {
        @Override
        public boolean matches(Item item) {
            return Runecrafting.RUNE_IDS.contains(item.getId());
        }
    },
    PICKUP_ENSOULED_HEADS(true) {
        @Override
        public boolean matches(Item item) {
            return item.getName().contains("Ensouled");
        }
    },
    PICKUP_TOKKUL(true) {
        @Override
        public boolean matches(Item item) {
            return item.getId() == ItemId.TOKKUL;
        }
    },
    PICKUP_NUMULITE(true) {
        @Override
        public boolean matches(Item item) {
            return item.getId() == ItemId.NUMULITE;
        }
    },
    PICKUP_ANCIENT_SHARDS(true) {
        @Override
        public boolean matches(Item item) {
            return item.getId() == ItemId.ANCIENT_SHARD;
        }
    },
    PICKUP_DARK_TOTEMS(true){
        @Override
        public boolean matches(Item item) {
            return item.getName().contains("Dark totem");
        }
    },
    PICKUP_KEYS(true) {
        @Override
        public boolean matches(Item item) {
            return switch (item.getId()) {
                case ItemId.LARRANS_KEY, ItemId.BRIMSTONE_KEY, ItemId.TOOTH_HALF_OF_KEY, ItemId.LOOP_HALF_OF_KEY -> true;
                default -> false;
            };
        }
    },
    PICKUP_DHIDE(true) {
        @Override
        public boolean matches(Item item) {
            return item.getName().contains("dragonhide");
        }
    },
    ;

    public static final DonationToggle[] VALUES = values();

    private final boolean pickup;

    DonationToggle(boolean pickup) {
        this.pickup = pickup;
    }

    DonationToggle() {
        this.pickup = false;
    }

    public static boolean process(Player killer, Item item) {
        for (DonationToggle value : VALUES) {
            if(value.matches(item)) {
                if(!value.isEnabled(killer))
                    return false;
                if(!value.chance(killer))
                    return false;

                if(!value.pickup) {
                    item.setId(item.getDefinitions().getNotedOrDefault());
                    return false;
                }

                return !killer.getInventory().addItem(item).isFailure();
            }
        }

        return false;
    }

    public static String formatRate(MemberRank rank) {
        int rate = rank.getTogglesChance();
        if (rate <= 0) {
            return "100";
        }
        return ChargesManager.FORMATTER.format(1.0f / (float) rate * 100.0f);
    }

    public boolean chance(Player player) {
        int rate = player.getMemberRank().getTogglesChance();
        if (rate == -1) return false;
        if (rate == 0) return true;
        return Utils.randomNoPlus(rate) == 0;
    }

    public boolean matches(Item item) {
        return false;
    }

    public void set(Player player, boolean value) {
        player.getAttributes().put(name(), value);
    }

    public boolean isEnabled(Player player) {
        return (boolean) player.getAttributes().getOrDefault(name(), false);
    }

    public static void openInterface(Player player) {
        Analytics.flagInteraction(player, Analytics.InteractionType.PREMIUM_TOGGLES);
        player.getDialogueManager().start(new OptionsMenuD(player, "Donator Toggles (" + formatRate(player.getMemberRank()) + "%)",
                Arrays.stream(VALUES).map(value -> StringUtilities.formatEnum(value)+": "+value.isEnabled(player)).toArray(String[]::new)) {
            @Override
            public void handleClick(int slotId) {
                VALUES[slotId].set(player, !VALUES[slotId].isEnabled(player));
                openInterface(player);
            }

            @Override
            public boolean cancelOption() {
                return true;
            }
        });
    }
}
