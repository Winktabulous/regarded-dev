package com.zenyte.game.content.magicstorageunit.enums;

import com.zenyte.game.content.magicstorageunit.StorableSetPiece;
import com.zenyte.game.content.magicstorageunit.StorageUnitElement;
import com.zenyte.game.item.ItemId;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum StorableDresses implements StorageUnitElement {
    MIME_COSTUME(ItemId.MIME_MASK_10629, new StorableSetPiece(ItemId.MIME_MASK), new StorableSetPiece(ItemId.MIME_TOP), new StorableSetPiece(ItemId.MIME_LEGS), new StorableSetPiece(ItemId.MIME_GLOVES), new StorableSetPiece(ItemId.MIME_BOOTS)),
    ROYAL_FROG_COSTUME(ItemId.PRINCESS_BLOUSE_10630, new StorableSetPiece(ItemId.PRINCE_TUNIC, ItemId.PRINCESS_BLOUSE), new StorableSetPiece(ItemId.PRINCE_LEGGINGS, ItemId.PRINCESS_SKIRT)),
    FROG_MASK(ItemId.FROG_MASK_10721, new StorableSetPiece(ItemId.FROG_MASK)),
    ZOMBIE_OUTFIT(ItemId.ZOMBIE_SHIRT_10631, new StorableSetPiece(ItemId.ZOMBIE_MASK), new StorableSetPiece(ItemId.ZOMBIE_SHIRT), new StorableSetPiece(ItemId.ZOMBIE_TROUSERS), new StorableSetPiece(ItemId.ZOMBIE_GLOVES), new StorableSetPiece(ItemId.ZOMBIE_BOOTS)),
    CAMO_OUTFIT(ItemId.CAMO_TOP_10632, new StorableSetPiece(ItemId.CAMO_HELMET), new StorableSetPiece(ItemId.CAMO_TOP), new StorableSetPiece(ItemId.CAMO_BOTTOMS)),
    LEDERHOSEN_OUTFIT(ItemId.LEDERHOSEN_TOP_10633, new StorableSetPiece(ItemId.LEDERHOSEN_HAT), new StorableSetPiece(ItemId.LEDERHOSEN_TOP), new StorableSetPiece(ItemId.LEDERHOSEN_SHORTS)),
    SHADE_ROBES(ItemId.SHADE_ROBE_10634, new StorableSetPiece(ItemId.SHADE_ROBE_TOP), new StorableSetPiece(ItemId.SHADE_ROBE)),
    ;

    private final int displayItem;
    private final StorableSetPiece[] pieces;

    StorableDresses(final int displayItem, final StorableSetPiece... pieces) {
        this.displayItem = displayItem;
        this.pieces = pieces;
    }

    public int getDisplayItem() {
        return displayItem;
    }

    public StorableSetPiece[] getPieces() {
        return pieces;
    }
}
