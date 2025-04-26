package com.zenyte.game.world.entity.npc.impl.slayer.superior.impl;

import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:08
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see com.zenyte.game.content.godwars.npcs.combat.BloodveldCombat for combat script.
 */
public class InsatiableBloodveld extends SuperiorNPC {
    public InsatiableBloodveld(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7397, tile);
    }

    @Override protected void onDeath(Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            ((Player) source).getCombatAchievements().complete(CAType.THE_DEMONIC_PUNCHING_BAG);
        }
    }
}
