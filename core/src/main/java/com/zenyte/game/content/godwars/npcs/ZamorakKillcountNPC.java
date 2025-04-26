package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;

/**
 * @author Kris | 19/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ZamorakKillcountNPC extends SpawnableKillcountNPC {

    public ZamorakKillcountNPC(final int id, final Location tile, final Direction direction, final int radius) {
        super(id, tile, direction, radius);
        setTargetType(EntityType.BOTH);
    }

    @Override
    public GodType type() {
        return GodType.ZAMORAK;
    }

    @Override
    public boolean validate(final int id, final String name) {
        if (SpawnableKillcountNPC.specialNPCs.contains(id)) {
            return false;
        }
        return id >= 3133 && id <= 3141 || id == 3159 || id == 3160;
    }

}
