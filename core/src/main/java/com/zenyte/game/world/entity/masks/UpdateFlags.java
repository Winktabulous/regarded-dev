package com.zenyte.game.world.entity.masks;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.player.Player;

import java.util.EnumSet;

/**
 * Holds update flags.
 *
 * @author Graham Edgecombe
 */
public class UpdateFlags {

	public UpdateFlags(final Entity entity) {
		this.entity = entity;
		flags = EnumSet.noneOf(UpdateFlag.class);
		if (entity instanceof Player) {
            flags.add(UpdateFlag.APPEARANCE);
        }
	}
	
	private final Entity entity;
	
    /**
     * The bitset (flag data).
     */
    private final EnumSet<UpdateFlag> flags;

    /**
     * Checks if an update required.
     *
     * @return <code>true</code> if 1 or more flags are set, <code>false</code>
     * if not.
     */
    public boolean isUpdateRequired() {
        return !flags.isEmpty();
    }

    /**
     * Flags (sets to true) a flag.
     *
     * @param flag The flag to flag.
     */
    public  void flag(final UpdateFlag flag) {
       set(flag, true);
    }

    /**
     * Sets a flag.
     *
     * @param flag  The flag.
     * @param value The value.
     */
    public synchronized void set(final UpdateFlag flag, final boolean value) {
    	if (entity instanceof Player) {
    		if (flag.getPlayerMask() == -1) {
				return;
			}
    	} else {
    		if (flag.getNpcMask() == -1) {
				return;
			}
    	}
    	if (value) {
			flags.add(flag);
		} else {
			flags.remove(flag);
		}
    }

    /**
     * Gets the value of a flag.
     *
     * @param flag The flag to get the value of.
     * @return The flag value.
     */
    public boolean get(final UpdateFlag flag) {
        return flags.contains(flag);
    }

    /**
     * Resest all update flags.
     */
    public void reset() {
        if (!flags.isEmpty()) {
            flags.clear();
        }
    }

    public EnumSet<UpdateFlag> getFlags() {
        return flags;
    }


}
