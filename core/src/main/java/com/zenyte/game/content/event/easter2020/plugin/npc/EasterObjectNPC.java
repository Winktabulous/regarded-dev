package com.zenyte.game.content.event.easter2020.plugin.npc;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.plugins.SkipPluginScan;

/**
 * @author Kris | 08/04/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SkipPluginScan
public class EasterObjectNPC extends NPC implements Spawnable {
    public EasterObjectNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(int id, String name) {
        return id >= 15214 && id <= 15261;
    }

    @Override
    public void setInteractingWith(final Entity entity) {}

    @Override
    public void finishInteractingWith(final Entity entity) {}

    @Override
    public void setFaceEntity(final Entity entity) {}

    @Override
    public void setFaceLocation(final Location tile) {}
}
