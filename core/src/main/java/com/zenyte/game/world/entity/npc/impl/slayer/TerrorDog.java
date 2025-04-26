package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.achievementdiary.diaries.MorytaniaDiary;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;

/**
 * @author Tommeh | 5-11-2018 | 16:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class TerrorDog extends NPC implements Spawnable {
    public TerrorDog(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getAchievementDiaries().update(MorytaniaDiary.KILL_A_TERRORDOG);
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return name.equals("terror dog");
    }
}
