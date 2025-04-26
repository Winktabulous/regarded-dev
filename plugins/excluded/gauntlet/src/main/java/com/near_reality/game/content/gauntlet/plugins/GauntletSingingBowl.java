package com.near_reality.game.content.gauntlet.plugins;

import com.near_reality.game.content.gauntlet.Gauntlet;
import com.near_reality.game.content.gauntlet.GauntletConstants;
import com.near_reality.game.content.gauntlet.GauntletPlayerAttributesKt;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import com.zenyte.plugins.dialogue.SkillDialogue;
import mgi.types.config.items.ItemDefinitions;

import java.util.Arrays;
import java.util.Optional;

import static com.near_reality.game.content.gauntlet.GauntletConstants.*;
import static com.zenyte.game.item.ItemId.*;

@SuppressWarnings("unused")
public final class GauntletSingingBowl implements ObjectAction {

    private static final int SINGING_BOWL = 36063;

    private static final int SINGING_BOWL_CORRUPTED = 35966;

    private static final int LINUM_TIRINUM_CORRUPTED = 23836;
    private static final int CORRUPTED_ORE = 23837;
    private static final int PHREN_BARK_CORRUPTED = 23838;

    private static final int LINUM_TIRINUM = 23876;
    private static final int CRYSTAL_ORE = 23877;
    private static final int PHREN_BARK = 23878;

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        openInterface(player, object.getId() == SINGING_BOWL_CORRUPTED);
    }

    private void openInterface(Player player, boolean corrupted) {
        SingingBowlItem[] options = getItemOptions(player, corrupted);
        Item[] items = Arrays.stream(options).map(i -> new Item(i.id)).toArray(Item[]::new);

        player.getDialogueManager().start(new SkillDialogue(player, "What would you like to make?", items) {
            @Override
            public void run(int slotId, int amount) {
                SingingBowlItem item = options[slotId];

                boolean hasRequirements = true;
                for (Item req: item.requirements) {
                    if (!player.getInventory().containsItem(req) && !player.getEquipment().containsItem(req)){
                        hasRequirements = false;
                        break;
                    }
                }

                int shards = corrupted ? CORRUPTED_SHARDS : CRYSTAL_SHARDS;
                if (!player.getInventory().containsItem(shards, item.shards) || !hasRequirements) {
                    player.getDialogueManager().start(new PlainChat(player, "You do not have the materials required to make that."));
                    return;
                }

                switch (item) {
                    case CRYSTAL_BOW_ATTUNED, CRYSTAL_HALBERD_ATTUNED, CRYSTAL_STAFF_ATTUNED, CORRUPTED_BOW_ATTUNED, CORRUPTED_HALBERD_ATTUNED, CORRUPTED_STAFF_ATTUNED -> {
                        final Gauntlet gauntlet = GauntletPlayerAttributesKt.getGauntlet(player);
                        if (gauntlet != null) {
                            gauntlet.attunedWeaponCount++;
                        }
                    }
                    case CORRUPTED_BODY_BASIC, CORRUPTED_BODY_ATTUNED, CORRUPTED_BODY_PERFECTED, CORRUPTED_HELM_BASIC, CORRUPTED_HELM_ATTUNED, CORRUPTED_HELM_PERFECTED, CORRUPTED_LEGS_BASIC, CORRUPTED_LEGS_ATTUNED, CORRUPTED_LEGS_PERFECTED,
                            CRYSTAL_BODY_BASIC, CRYSTAL_BODY_ATTUNED, CRYSTAL_BODY_PERFECTED, CRYSTAL_HELM_BASIC, CRYSTAL_HELM_ATTUNED, CRYSTAL_HELM_PERFECTED, CRYSTAL_LEGS_BASIC, CRYSTAL_LEGS_ATTUNED, CRYSTAL_LEGS_PERFECTED -> {
                        final Gauntlet gauntlet = GauntletPlayerAttributesKt.getGauntlet(player);
                        if (gauntlet != null) {
                            gauntlet.armorMade = true;
                        }
                    }
                }

                boolean replaceEquipment = false;
                for (Item req: item.requirements) {
                    if (player.getEquipment().containsItem(req)) {
                        player.getEquipment().deleteItem(req);
                        replaceEquipment = true;
                    } else if (player.getInventory().containsItem(req))
                        player.getInventory().deleteItems(req);
                }

                player.getInventory().deleteItem(shards, item.shards);

                final int slot = Optional.ofNullable(ItemDefinitions.get(item.id)).map(ItemDefinitions::getSlot).orElse(-1);
                if (replaceEquipment &&  slot != -1)
                    player.getEquipment().set(slot, new Item(item.id));
                else
                    player.getInventory().addOrDrop(new Item(item.id));

                player.getSkills().addXp(SkillConstants.CRAFTING, item.experienceGain);
                player.getSkills().addXp(SkillConstants.SMITHING, item.experienceGain);

                player.sendFilteredMessage("With the help of the crystal bowl, you sing a beautiful song and shape the crystals.");

                player.getInterfaceHandler().closeInterface(InterfacePosition.DIALOGUE);
                openInterface(player, corrupted);
            }

            @Override
            public int getMaximumAmount() {
                return 28;
            }

            @Override
            public boolean closeOnRun() {
                return false;
            }
        });
    }


    public SingingBowlItem[] getItemOptions(Player player, boolean corrupted) {
        SingingBowlItem helm = getHelm(player, corrupted);
        SingingBowlItem body = getBody(player, corrupted);
        SingingBowlItem legs = getLegs(player, corrupted);

        SingingBowlItem halberd = getHalberd(player, corrupted);
        SingingBowlItem staff = getStaff(player, corrupted);
        SingingBowlItem bow = getBow(player, corrupted);

        return new SingingBowlItem[]{
                corrupted ? SingingBowlItem.TELEPORT_CRYSTAL : SingingBowlItem.CORRUPTED_TELEPORT_CRYSTAL,
                SingingBowlItem.VIAL,
                helm,
                body,
                legs,
                halberd,
                staff,
                bow,
                corrupted ? SingingBowlItem.CORRUPTED_PADDLEFISH : SingingBowlItem.CRYSTAL_PADDLEFISH,
                corrupted ? SingingBowlItem.ESCAPE_CRYSTAL_CORRUPTED : SingingBowlItem.ESCAPE_CRYSTAL
        };
    }

    public SingingBowlItem getHelm(Player player, boolean corrupted) {
        if (corrupted) {
            if (hasItem(player, SingingBowlItem.CORRUPTED_HELM_ATTUNED)) {
                return SingingBowlItem.CORRUPTED_HELM_PERFECTED;
            } else if (hasItem(player, SingingBowlItem.CORRUPTED_HELM_BASIC)) {
                return SingingBowlItem.CORRUPTED_HELM_ATTUNED;
            } else {
                return SingingBowlItem.CORRUPTED_HELM_BASIC;
            }
        } else {
            if (hasItem(player, SingingBowlItem.CRYSTAL_HELM_ATTUNED)) {
                return SingingBowlItem.CRYSTAL_HELM_PERFECTED;
            } else if (hasItem(player, SingingBowlItem.CRYSTAL_HELM_BASIC)) {
                return SingingBowlItem.CRYSTAL_HELM_ATTUNED;
            } else {
                return SingingBowlItem.CRYSTAL_HELM_BASIC;
            }
        }
    }
    public SingingBowlItem getBody(Player player, boolean corrupted) {
        if (corrupted) {
            if (hasItem(player, SingingBowlItem.CORRUPTED_BODY_ATTUNED)) {
                return SingingBowlItem.CORRUPTED_BODY_PERFECTED;
            } else if (hasItem(player, SingingBowlItem.CORRUPTED_BODY_BASIC)) {
                return SingingBowlItem.CORRUPTED_BODY_ATTUNED;
            } else {
                return SingingBowlItem.CORRUPTED_BODY_BASIC;
            }
        } else {
            if (hasItem(player, SingingBowlItem.CRYSTAL_BODY_ATTUNED)) {
                return SingingBowlItem.CRYSTAL_BODY_PERFECTED;
            } else if (hasItem(player, SingingBowlItem.CRYSTAL_BODY_BASIC)) {
                return SingingBowlItem.CRYSTAL_BODY_ATTUNED;
            } else {
                return SingingBowlItem.CRYSTAL_BODY_BASIC;
            }
        }
    }

    public SingingBowlItem getLegs(Player player, boolean corrupted) {
        if (corrupted) {
            if (hasItem(player, SingingBowlItem.CORRUPTED_LEGS_ATTUNED)) {
                return SingingBowlItem.CORRUPTED_LEGS_PERFECTED;
            } else if (hasItem(player, SingingBowlItem.CORRUPTED_LEGS_BASIC)) {
                return SingingBowlItem.CORRUPTED_LEGS_ATTUNED;
            } else {
                return SingingBowlItem.CORRUPTED_LEGS_BASIC;
            }
        } else {
            if (hasItem(player, SingingBowlItem.CRYSTAL_LEGS_ATTUNED)) {
                return SingingBowlItem.CRYSTAL_LEGS_PERFECTED;
            } else if (hasItem(player, SingingBowlItem.CRYSTAL_LEGS_BASIC)) {
                return SingingBowlItem.CRYSTAL_LEGS_ATTUNED;
            } else {
                return SingingBowlItem.CRYSTAL_LEGS_BASIC;
            }
        }
    }

    public SingingBowlItem getHalberd(Player player, boolean corrupted) {
        if (corrupted) {
            if (hasItem(player, SingingBowlItem.CORRUPTED_HALBERD_ATTUNED)) {
                return SingingBowlItem.CORRUPTED_HALBERD_PERFECTED;
            } else if (hasItem(player, SingingBowlItem.CORRUPTED_HALBERD_BASIC)) {
                return SingingBowlItem.CORRUPTED_HALBERD_ATTUNED;
            } else {
                return SingingBowlItem.CORRUPTED_HALBERD_BASIC;
            }
        } else {
            if (hasItem(player, SingingBowlItem.CRYSTAL_HALBERD_ATTUNED)) {
                return SingingBowlItem.CRYSTAL_HALBERD_PERFECTED;
            } else if (hasItem(player, SingingBowlItem.CRYSTAL_HALBERD_BASIC)) {
                return SingingBowlItem.CRYSTAL_HALBERD_ATTUNED;
            } else {
                return SingingBowlItem.CRYSTAL_HALBERD_BASIC;
            }
        }
    }

    public SingingBowlItem getStaff(Player player, boolean corrupted) {
        if (corrupted) {
            if (hasItem(player, SingingBowlItem.CORRUPTED_STAFF_ATTUNED)) {
                return SingingBowlItem.CORRUPTED_STAFF_PERFECTED;
            } else if (hasItem(player, SingingBowlItem.CORRUPTED_STAFF_BASIC)) {
                return SingingBowlItem.CORRUPTED_STAFF_ATTUNED;
            } else {
                return SingingBowlItem.CORRUPTED_STAFF_BASIC;
            }
        } else {
            if (hasItem(player, SingingBowlItem.CRYSTAL_STAFF_ATTUNED)) {
                return SingingBowlItem.CRYSTAL_STAFF_PERFECTED;
            } else if (hasItem(player, SingingBowlItem.CRYSTAL_STAFF_BASIC)) {
                return SingingBowlItem.CRYSTAL_STAFF_ATTUNED;
            } else {
                return SingingBowlItem.CRYSTAL_STAFF_BASIC;
            }
        }
    }

    public SingingBowlItem getBow(Player player, boolean corrupted) {
        if (corrupted) {
            if (hasItem(player, SingingBowlItem.CORRUPTED_BOW_ATTUNED)) {
                return SingingBowlItem.CORRUPTED_BOW_PERFECTED;
            } else if (hasItem(player, SingingBowlItem.CORRUPTED_BOW_BASIC)) {
                return SingingBowlItem.CORRUPTED_BOW_ATTUNED;
            } else {
                return SingingBowlItem.CORRUPTED_BOW_BASIC;
            }
        } else {
            if (hasItem(player, SingingBowlItem.CRYSTAL_BOW_ATTUNED)) {
                return SingingBowlItem.CRYSTAL_BOW_PERFECTED;
            } else if (hasItem(player, SingingBowlItem.CRYSTAL_BOW_BASIC)) {
                return SingingBowlItem.CRYSTAL_BOW_ATTUNED;
            } else {
                return SingingBowlItem.CRYSTAL_BOW_BASIC;
            }
        }
    }

    private static boolean hasItem(Player player, SingingBowlItem item) {
        return player.getEquipment().containsItem(item.id) || player.getInventory().containsItem(item.id);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{SINGING_BOWL, SINGING_BOWL_CORRUPTED};
    }

    private enum SingingBowlItem {
        VIAL(23879, 5, 10),
        TELEPORT_CRYSTAL(ItemId.TELEPORT_CRYSTAL, 20, 40),
        CORRUPTED_TELEPORT_CRYSTAL(ItemId.CORRUPTED_TELEPORT_CRYSTAL, 20, 40),


        CRYSTAL_HELM_BASIC(23886, 20, 40, new Item(CRYSTAL_ORE), new Item(PHREN_BARK), new Item(LINUM_TIRINUM)),
        CRYSTAL_BODY_BASIC(23889, 20, 40, new Item(CRYSTAL_ORE), new Item(PHREN_BARK), new Item(LINUM_TIRINUM)),
        CRYSTAL_LEGS_BASIC(23892, 20, 40, new Item(CRYSTAL_ORE), new Item(PHREN_BARK), new Item(LINUM_TIRINUM)),

        CORRUPTED_HELM_BASIC(23840, 20, 40, new Item(CORRUPTED_ORE), new Item(PHREN_BARK_CORRUPTED), new Item(LINUM_TIRINUM_CORRUPTED)),
        CORRUPTED_BODY_BASIC(23843, 20, 40, new Item(CORRUPTED_ORE), new Item(PHREN_BARK_CORRUPTED), new Item(LINUM_TIRINUM_CORRUPTED)),
        CORRUPTED_LEGS_BASIC(23846, 20, 40, new Item(CORRUPTED_ORE), new Item(PHREN_BARK_CORRUPTED), new Item(LINUM_TIRINUM_CORRUPTED)),

        CRYSTAL_HELM_ATTUNED(23887, 30, 60, new Item(CRYSTAL_HELM_BASIC.id), new Item(CRYSTAL_ORE), new Item(PHREN_BARK), new Item(LINUM_TIRINUM)),
        CRYSTAL_BODY_ATTUNED(23890, 30, 60, new Item(CRYSTAL_BODY_BASIC.id), new Item(CRYSTAL_ORE, 2), new Item(PHREN_BARK, 2), new Item(LINUM_TIRINUM, 2)),
        CRYSTAL_LEGS_ATTUNED(23893, 30, 60, new Item(CRYSTAL_LEGS_BASIC.id), new Item(CRYSTAL_ORE), new Item(PHREN_BARK), new Item(LINUM_TIRINUM)),

        CORRUPTED_HELM_ATTUNED(23841, 30, 60, new Item(CORRUPTED_HELM_BASIC.id), new Item(CORRUPTED_ORE), new Item(PHREN_BARK_CORRUPTED), new Item(LINUM_TIRINUM_CORRUPTED)),
        CORRUPTED_BODY_ATTUNED(23844, 30, 60, new Item(CORRUPTED_BODY_BASIC.id), new Item(CORRUPTED_ORE, 2), new Item(PHREN_BARK_CORRUPTED, 2), new Item(LINUM_TIRINUM_CORRUPTED, 2)),
        CORRUPTED_LEGS_ATTUNED(23847, 30, 60, new Item(CORRUPTED_LEGS_BASIC.id), new Item(CORRUPTED_ORE), new Item(PHREN_BARK_CORRUPTED), new Item(LINUM_TIRINUM_CORRUPTED)),

        CRYSTAL_HELM_PERFECTED(23888, 80, 60, new Item(CRYSTAL_HELM_ATTUNED.id), new Item(CRYSTAL_ORE, 2), new Item(PHREN_BARK, 2), new Item(LINUM_TIRINUM, 2)),
        CRYSTAL_BODY_PERFECTED(23891, 80, 60, new Item(CRYSTAL_BODY_ATTUNED.id), new Item(CRYSTAL_ORE, 2), new Item(PHREN_BARK, 2), new Item(LINUM_TIRINUM, 2)),
        CRYSTAL_LEGS_PERFECTED(23894, 80, 60, new Item(CRYSTAL_LEGS_ATTUNED.id), new Item(CRYSTAL_ORE, 2), new Item(PHREN_BARK, 2), new Item(LINUM_TIRINUM, 2)),

        CORRUPTED_HELM_PERFECTED(23842, 80, 60, new Item(CORRUPTED_HELM_ATTUNED.id), new Item(CORRUPTED_ORE, 2), new Item(PHREN_BARK_CORRUPTED, 2), new Item(LINUM_TIRINUM_CORRUPTED, 2)),
        CORRUPTED_BODY_PERFECTED(23845, 80, 60, new Item(CORRUPTED_BODY_ATTUNED.id), new Item(CORRUPTED_ORE, 2), new Item(PHREN_BARK_CORRUPTED, 2), new Item(LINUM_TIRINUM_CORRUPTED, 2)),
        CORRUPTED_LEGS_PERFECTED(23848, 80, 60, new Item(CORRUPTED_LEGS_ATTUNED.id), new Item(CORRUPTED_ORE, 2), new Item(PHREN_BARK_CORRUPTED, 2), new Item(LINUM_TIRINUM_CORRUPTED, 2)),

        CRYSTAL_HALBERD_BASIC(23895, 10, 20, new Item(WEAPON_FRAME_23871)),
        CRYSTAL_BOW_BASIC(23901, 10, 20, new Item(WEAPON_FRAME_23871)),
        CRYSTAL_STAFF_BASIC(23898, 10, 20, new Item(WEAPON_FRAME_23871)),

        CORRUPTED_HALBERD_BASIC(23849, 10, 20, new Item(WEAPON_FRAME)),
        CORRUPTED_BOW_BASIC(23855, 10, 20, new Item(WEAPON_FRAME)),
        CORRUPTED_STAFF_BASIC(23852, 10, 20, new Item(WEAPON_FRAME)),

        CRYSTAL_HALBERD_ATTUNED(23896, 30, 60, new Item(CRYSTAL_HALBERD_BASIC.id)),
        CRYSTAL_BOW_ATTUNED(23902, 30, 60, new Item(CRYSTAL_BOW_BASIC.id)),
        CRYSTAL_STAFF_ATTUNED(23899, 30, 60, new Item(CRYSTAL_STAFF_BASIC.id)),

        CORRUPTED_HALBERD_ATTUNED(23850, 30, 60, new Item(CORRUPTED_HALBERD_BASIC.id)),
        CORRUPTED_BOW_ATTUNED(23856, 30, 60, new Item(CORRUPTED_BOW_BASIC.id)),
        CORRUPTED_STAFF_ATTUNED(23853, 30, 60, new Item(CORRUPTED_STAFF_BASIC.id)),

        CRYSTAL_HALBERD_PERFECTED(23897, 30, 0, new Item(CRYSTAL_HALBERD_ATTUNED.id), new Item(CRYSTAL_SPIKE)),
        CRYSTAL_BOW_PERFECTED(23903, 30, 0, new Item(CRYSTAL_BOW_ATTUNED.id), new Item(CRYSTALLINE_BOWSTRING)),
        CRYSTAL_STAFF_PERFECTED(23900, 30, 0, new Item(CRYSTAL_STAFF_ATTUNED.id), new Item(CRYSTAL_ORB)),

        CORRUPTED_HALBERD_PERFECTED(23851, 30, 0, new Item(CORRUPTED_HALBERD_ATTUNED.id), new Item(CORRUPTED_SPIKE)),
        CORRUPTED_BOW_PERFECTED(23857, 30, 0, new Item(CORRUPTED_BOW_ATTUNED.id), new Item(CORRUPTED_BOWSTRING)),
        CORRUPTED_STAFF_PERFECTED(23854, 30, 0, new Item(CORRUPTED_STAFF_ATTUNED.id), new Item(CORRUPTED_ORB)),

        CRYSTAL_PADDLEFISH(25960, 30, 10, new Item(ItemId.PADDLEFISH)),
        CORRUPTED_PADDLEFISH(25958, 30, 10, new Item(ItemId.PADDLEFISH)),

        ESCAPE_CRYSTAL(25961, 200, 200),
        ESCAPE_CRYSTAL_CORRUPTED(25959, 200, 200);

        private final int id;

        private final int experienceGain;

        private final int shards;

        private final Item[] requirements;

        SingingBowlItem(int id, int experienceGain, int shards, Item... requirements) {
            this.id = id;
            this.experienceGain = experienceGain;
            this.shards = shards;
            this.requirements = requirements;
        }

    }
}
