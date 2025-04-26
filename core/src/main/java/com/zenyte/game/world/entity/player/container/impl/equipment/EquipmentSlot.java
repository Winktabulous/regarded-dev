package com.zenyte.game.world.entity.player.container.impl.equipment;

/**
 * @author Kris | 4. mai 2018 : 20:57:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum EquipmentSlot {

	HELMET(0),
	CAPE(1),
    AMULET(2),
    WEAPON(3),
    PLATE(4),
    SHIELD(5),
    LEGS(7),
    HANDS(9),
    BOOTS(10),
    RING(12),
    AMMUNITION(13);

    private final int slot;

    EquipmentSlot(final int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }
}
