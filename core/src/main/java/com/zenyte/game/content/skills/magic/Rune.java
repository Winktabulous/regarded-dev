package com.zenyte.game.content.skills.magic;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import mgi.types.config.enums.EnumDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tommeh | 26 mrt. 2018 : 17:36:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public enum Rune {
    AIR(ItemId.AIR_RUNE), WATER(ItemId.WATER_RUNE), EARTH(ItemId.EARTH_RUNE), FIRE(ItemId.FIRE_RUNE), MIND(ItemId.MIND_RUNE), CHAOS(ItemId.CHAOS_RUNE), DEATH(ItemId.DEATH_RUNE), BLOOD(ItemId.BLOOD_RUNE), COSMIC(ItemId.COSMIC_RUNE), NATURE(ItemId.NATURE_RUNE), LAW(ItemId.LAW_RUNE), BODY(ItemId.BODY_RUNE), SOUL(ItemId.SOUL_RUNE), ASTRAL(ItemId.ASTRAL_RUNE), MIST(ItemId.MIST_RUNE), MUD(ItemId.MUD_RUNE), DUST(ItemId.DUST_RUNE), LAVA(ItemId.LAVA_RUNE), STEAM(ItemId.STEAM_RUNE), SMOKE(ItemId.SMOKE_RUNE), WRATH(ItemId.WRATH_RUNE);
    private static final Logger log = LoggerFactory.getLogger(Rune.class);
    private final int id;
    public static final Rune[] values = values();

    Rune(final int id) {
        this.id = id;
    }

    static {
        final Int2IntMap runeEnum = EnumDefinitions.getIntEnum(982).getValues();
        for (Rune rune : values) {
            if (!runeEnum.containsValue(rune.getId())) {
                log.error("", new IllegalArgumentException("Defined Rune with id " + rune.getId() + " not found within rune cache enum."));
            }
        }
        if (values.length != runeEnum.size()) {
            log.error("", new RuntimeException("Defined Rune enum size does not match rune cache enum size."));
        }
    }

    public static Rune getRune(final Item item) {
        for (Rune rune : values) if (item.getId() == rune.getId()) return rune;
        return null;
    }

    public static Rune getRune(final int item) {
        for (Rune rune : values) if (item == rune.getId()) return rune;
        return null;
    }

    public int getId() {
        return id;
    }
}
