package com.zenyte.plugins.object;

import com.zenyte.game.content.skills.slayer.Slayer;
import com.zenyte.game.content.skills.slayer.SlayerMountType;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

@SuppressWarnings("unused")
public class SlayerStatues implements ObjectAction, ItemOnObjectAction {

    private static final Animation DISMOUNT_ANIM = new Animation(3132);
    private static final Animation MOUNT_ANIM = new Animation(3071);
    private static final Item IMBUED_HELM = new Item(ItemId.SLAYER_HELMET_I);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Info")) {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    SlayerMountType slayerMountType = SlayerMountType.indexToType.get(player.getVarManager().getValue(Slayer.SLAYER_STATUES_VAR));
                    if (slayerMountType != null && slayerMountType.getItem() != null) {
                        item(slayerMountType.getItem(), "You have currently mounted the " + slayerMountType.getItem().getName() + " helmet.");
                    } else {
                        item(IMBUED_HELM, "You can mount this statue with your Imbued Slayer Helmet, which will allow you to receive the Slayer Helm boost for your tasks without having to wear it.");
                    }
                }
            });
        } else if (option.equalsIgnoreCase("Dismount")) {
            SlayerMountType slayerMountType = SlayerMountType.indexToType.get(player.getVarManager().getValue(Slayer.SLAYER_STATUES_VAR));
            if (slayerMountType == null) {
                return;
            }

            if (!player.getInventory().hasFreeSlots()) {
                player.sendMessage("You need to have some empty space in your inventory to dismount the helmet.");
                return;
            }

            final Item item = slayerMountType.getItem();
            if (item == null) {
                return;
            }

            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Do you want to dismount your current Slayer Helmet?", new DialogueOption("Yes.", () -> {
                        player.lock(1);
                        player.setAnimation(DISMOUNT_ANIM);
                        WorldTasksManager.schedule(() -> {
                            player.getInventory().addItem(item);
                            player.getVarManager().sendVar(Slayer.SLAYER_STATUES_VAR, SlayerMountType.NONE.getIndex());
                        });
                    }), new DialogueOption("No."));
                }
            });
        }
    }

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        SlayerMountType slayerMountType = SlayerMountType.helmToType.get(item.getId());
        if (slayerMountType == null) {
            return;
        }

        SlayerMountType currentSlayerMountType = SlayerMountType.indexToType.get(player.getVarManager().getValue(Slayer.SLAYER_STATUES_VAR));
        if (currentSlayerMountType != null && currentSlayerMountType.getItem() != null) {
            player.sendMessage("You cannot mount another helmet unless you dismount your current one.");
            return;
        }

        if (!player.getInventory().containsItem(slayerMountType.getItem())) {
            return;
        }

        player.lock(2);
        player.setAnimation(MOUNT_ANIM);
        WorldTasksManager.schedule(() -> {
            player.getInventory().deleteItem(slayerMountType.getItem());
            player.getVarManager().sendVar(Slayer.SLAYER_STATUES_VAR, slayerMountType.getIndex());
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    item(slayerMountType.getItem(), "You mount your " + slayerMountType.getItem().getName() + " to your statue.");
                }
            });
        }, 1);
    }

    @Override
    public Object[] getItems() {
        return new Object[]{ItemId.RED_SLAYER_HELMET_I, ItemId.HYDRA_SLAYER_HELMET_I, ItemId.TZTOK_SLAYER_HELMET_I, ItemId.GREEN_SLAYER_HELMET_I,
                ItemId.BLACK_SLAYER_HELMET_I, ItemId.TWISTED_SLAYER_HELMET_I, ItemId.SLAYER_HELMET_I, ItemId.PURPLE_SLAYER_HELMET_I, ItemId.TURQUOISE_SLAYER_HELMET_I,
                ItemId.VAMPYRIC_SLAYER_HELMET_I, ItemId.TZKAL_SLAYER_HELMET_I};
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {50103,50104,50105,50106,50107,50108,50109,50110,50111,50112,50113,50114,50115};
    }

}
