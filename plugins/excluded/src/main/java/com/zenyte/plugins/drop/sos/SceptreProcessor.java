
package com.zenyte.plugins.drop.sos;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.region.GlobalAreaManager;
import com.zenyte.game.world.region.RegionArea;

/**
 * @author Kris | 31/01/2019 01:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class SceptreProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(getId(), 1, 1, 33.333));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (drop.isAlways()) return item;
        if (random(100) < 3) {
            final RegionArea area = GlobalAreaManager.get("Stronghold of Security");
            if (area != null && area.inside(npc.getLocation())) {
                return new Item(getId());
            }
        }
        return item;
    }

    public abstract int getId();
}
