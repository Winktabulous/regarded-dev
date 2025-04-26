package com.zenyte.game.content.magicstorageunit;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface StorageUnitElement {
    /**
     * The item that is displayed on the interface to represent the singular item or a set of items.
     * @return the id of the displayed item.
     */
    int getDisplayItem();

    /**
     * An array of possible pieces; Only one element in {@link StorableSetPiece#getIds()} is necessary to render the given piece as obtained.
     * @return an array of pieces used to combine the full set.
     */
    StorableSetPiece[] getPieces();

    default void verifyExistence() {
        /*final StringEnum storageEnum = Enums.COSTUME_STORAGE_UNIT_ENUM;
        if (!storageEnum.getValue(getDisplayItem()).isPresent()) {
            throw new IllegalStateException("Costume element is not supported in the cache: " + getDisplayItem());
        }*/
    }

    /**
     * Whether or not only a single piece of every equipment slot should be notified to the player when they do not have all the necessary items to store something - used to distinguish item sets
     * with different parts that come with the same name.
     * @return whether or not only the first piece of each of the equipment slots of the set should be called out.
     */
    default boolean singular() {
        return false;
    }

    /**
     * Builds a default storage unit element for the back and more options on the interface.
     * @param id the id of the respective item.
     * @return the storage unit element.
     */
    static StorageUnitElement of(final int id) {
        return new StorageUnitElement() {
            @Override
            public int getDisplayItem() {
                return id;
            }
            @Override
            public StorableSetPiece[] getPieces() {
                return new StorableSetPiece[0];
            }
        };
    }
}
