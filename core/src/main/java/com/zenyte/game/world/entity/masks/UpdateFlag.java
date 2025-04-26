package com.zenyte.game.world.entity.masks;

/**
 * @author Kris | 6. nov 2017 : 14:34.26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public enum UpdateFlag {

	/**
	 * Appearance update.
	 */
	APPEARANCE(0x4, -1),

	/**
	 * Graphics update.
	 */
	GRAPHICS(0x800, 0x1),

	/**
	 * Animation update.
	 */
	ANIMATION(0x2, 0x40),

	/**
	 * Forced chat update.
	 */
	FORCED_CHAT(0x1, 0x8),

	/**
	 * Interacting entity update.
	 */
	FACE_ENTITY(0x10, 0x10),

	/**
	 * Face coordinate entity update.
	 */
	FACE_COORDINATE(0x80, 0x80),

	/**
	 * Hit update.
	 */
	HIT(0x20, 0x20),

	/**
	 * Update flag used to define player's current movement type (walk or run)
	 */
	MOVEMENT_TYPE(0x4000, -1),

	/**
	 * Update flag used to force move player.
	 */
	FORCE_MOVEMENT(0x100, 0x100),

	/**
	 * Update flag used to set player's movement type for one tick (teleport or walk - supports run as well but never used)
	 */
	TEMPORARY_MOVEMENT_TYPE(0x200, -1),

	/**
	 * Update flag used to set player's right-click strings (before name, after name and after combat)
	 */
	NAMETAG(0x1000, -1),
	
	/**
	 * Update flag used for chat messages.
	 */
	CHAT(0x8, -1),

    /**
     * Update flag used to transform a npc to a different one.
     */
    TRANSFORMATION(-1, 0x4),

    /**
     * Update flag used to set an option on an npc.
     */
    OPTION(-1, 0x80),
	TINTING(0x400, 0x200),
	COMBAT_LEVEL_CHANGE(-1, 0x800),
	NAME_CHANGE(-1, 0x400),
	HIDE_OPTIONS(-1, 0x1000),
	MODEL_OVERRIDE(-1, 0x8000),
	NPC_PRAYER_OVERHEAD(-1, 0x20000)
	;

    public static final UpdateFlag[] VALUES = values();
    private final int playerMask, npcMask;

    UpdateFlag(final int playerMask, final int npcMask) {
        this.playerMask = playerMask;
        this.npcMask = npcMask;
    }

    public int getPlayerMask() {
        return playerMask;
    }

    public int getNpcMask() {
        return npcMask;
    }

}
