package com.zenyte.game.content.clans;

import com.zenyte.utils.Ordinal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 22. march 2018 : 23:26.11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>
 */
@Ordinal
public enum ClanRank {

	ANYONE(-1, 0, "Anyone"),
	FRIENDS(0, 0, "Any friends"),
	RECRUIT(1, 0, "Recruit+"),
	CORPORAL(2, 1, "Corporal+"),
	SERGEANT(3, 2, "Sergeant+"),
	LIEUTENANT(4, 3, "Lieutenant+"),
	CAPTAIN(5, 4, "Captain+"),
	GENERAL(6, 5, "General+"),
	OWNER(7, 6, "Only me"),
	ADMINISTRATOR(127, 6, "Administrator"),
	NO_RESTRICTION(-1, 0, "No restriction");

	private final int id, kickId;
	private final String label;

    public static final ClanRank[] VALUES = values();
    private static final Map<Integer, ClanRank> MAPPED_RANKS = new HashMap<>();

    static {
        for (final ClanRank rank : VALUES) {
            MAPPED_RANKS.put(rank.id, rank);
        }
    }

    ClanRank(final int id, final int kickId, final String label) {
        this.id = id;
        this.kickId = kickId;
        this.label = label;
    }

    public static final ClanRank getRank(final int id) {
        return MAPPED_RANKS.get(id);
    }

    public int getId() {
        return id;
    }

    public int getKickId() {
        return kickId;
    }

    public String getLabel() {
        return label;
    }

}
