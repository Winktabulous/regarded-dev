package com.zenyte.game.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * @author Kris | 2. apr 2018 : 0:19.46
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum AccessMask {

	NONE(0),
	CONTINUE(1 << 0),
	CLICK_OP1(1 << 1),
	CLICK_OP2(1 << 2),
	CLICK_OP3(1 << 3),
	CLICK_OP4(1 << 4),
	CLICK_OP5(1 << 5),
	CLICK_OP6(1 << 6),
	CLICK_OP7(1 << 7),
	CLICK_OP8(1 << 8),
	CLICK_OP9(1 << 9),
	CLICK_OP10(1 << 10),
	USE_ON_GROUND_ITEMS(1 << 11),
	USE_ON_NPCS(1 << 12),
	USE_ON_OBJECTS(1 << 13),
	USE_ON_PLAYERS(1 << 14),
	USE_ON_INVENTORY(1 << 15),
	USE_ON_COMPONENT(1 << 16),
	DRAG_DEPTH1(1 << 17),
	DRAG_DEPTH2(2 << 17),
	DRAG_DEPTH3(3 << 17),
	DRAG_DEPTH4(4 << 17),
	DRAG_DEPTH5(5 << 17),
	DRAG_DEPTH6(6 << 17),
	DRAG_DEPTH7(7 << 17),
	DRAG_TARGETABLE(1 << 20),
	COMPONENT_TARGETABLE(1 << 21);
	
	private static final AccessMask[] VALUES = values();
	private static final Int2ObjectOpenHashMap<AccessMask> MAP = new Int2ObjectOpenHashMap<AccessMask>(VALUES.length);
	
	static {
		for (final AccessMask mask : VALUES) {
			MAP.put(mask.getValue(), mask);
		}
	}
	
	public static final AccessMask getMask(final int bitId) {
		return MAP.get(bitId);
	}
	
    private final int value;

    AccessMask(final int value) {
        this.value = value;
    }

	/**
	 * Gets a new mask builder object in direct code form, based on the mask input itself.
	 * @param mask the mask to decompile.
	 * @param asBuilder whether to represent the string as a maskbuilder object or an array of masks.
	 * @return the builder string.
	 */
	public static final String getBuilder(final int mask, final boolean asBuilder) {
		final StringBuilder b = new StringBuilder();
		if (asBuilder) {
			b.append("new MaskBuilder(");
		}
		for (int i = 0; i < 32; i++) {
            if (((mask >> i) & 0x1) != 0) {
                b.append("AccessMask." + AccessMask.getMask(1 << i) + ", ");
            }
        }
        b.delete(b.length() - 2, b.length());
        if (asBuilder) {
            b.append(")");
        }
        return b.toString();
    }

    public int getValue() {
        return value;
    }
}
