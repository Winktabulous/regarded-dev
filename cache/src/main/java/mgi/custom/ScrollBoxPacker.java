package mgi.custom;

import mgi.types.config.items.ItemDefinitions;

import static com.zenyte.game.item.ItemId.*;

/**
 * @author Kris | 23/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ScrollBoxPacker {

    public void pack() {
        ItemDefinitions.getOrThrow(SCROLL_BOX_BEGINNER).toBuilder()
                .name("Scroll box (beginner)").isStackable(1).grandExchange(false).isMembers(true).inventoryOptions(new String[]{"Open", null, null, null, "Drop"})
                .price(50).shiftClickIndex(-2).zoom(770).offsetX(1).offsetY(-6).modelPitch(236).modelRoll(1697).modelYaw(0).inventoryModelId(53002).build().pack();

        ItemDefinitions.getOrThrow(SCROLL_BOX_EASY).toBuilder()
                .name("Scroll box (easy)").isStackable(1).grandExchange(false).isMembers(true).inventoryOptions(new String[]{"Open", null, null, null, "Drop"})
                .price(50).shiftClickIndex(-2).zoom(770).offsetX(1).offsetY(-6).modelPitch(236).modelRoll(1697).modelYaw(0).inventoryModelId(53003).build().pack();

        ItemDefinitions.getOrThrow(SCROLL_BOX_MEDIUM).toBuilder()
                .name("Scroll box (medium)").isStackable(1).grandExchange(false).isMembers(true).inventoryOptions(new String[]{"Open", null, null, null, "Drop"})
                .price(50).shiftClickIndex(-2).zoom(770).offsetX(1).offsetY(-6).modelPitch(236).modelRoll(1697).modelYaw(0).inventoryModelId(53005).build().pack();

        ItemDefinitions.getOrThrow(SCROLL_BOX_HARD).toBuilder()
                .name("Scroll box (hard)").isStackable(1).grandExchange(false).isMembers(true).inventoryOptions(new String[]{"Open", null, null, null, "Drop"})
                .price(50).shiftClickIndex(-2).zoom(770).offsetX(1).offsetY(-6).modelPitch(236).modelRoll(1697).modelYaw(0).inventoryModelId(53000).build().pack();

        ItemDefinitions.getOrThrow(SCROLL_BOX_ELITE).toBuilder()
                .name("Scroll box (elite)").isStackable(1).grandExchange(false).isMembers(true).inventoryOptions(new String[]{"Open", null, null, null, "Drop"})
                .price(50).shiftClickIndex(-2).zoom(770).offsetX(1).offsetY(-6).modelPitch(236).modelRoll(1697).modelYaw(0).inventoryModelId(53004).build().pack();

        ItemDefinitions.getOrThrow(SCROLL_BOX_MASTER).toBuilder()
                .name("Scroll box (master)").isStackable(1).grandExchange(false).isMembers(true).inventoryOptions(new String[]{"Open", null, null, null, "Drop"})
                .price(50).shiftClickIndex(-2).zoom(770).offsetX(1).offsetY(-6).modelPitch(236).modelRoll(1697).modelYaw(0).inventoryModelId(53001).build().pack();
    }

}
