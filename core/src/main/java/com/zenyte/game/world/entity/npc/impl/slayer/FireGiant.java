package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;

/**
 * @author Tommeh | 5-11-2018 | 18:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FireGiant extends NPC implements Spawnable {
    public FireGiant(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getAchievementDiaries().update(KandarinDiary.KILL_A_FIRE_GIANT);
            player.getCombatAchievements().complete(CAType.THE_WALKING_VOLCANO);
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return name.equals("fire giant");
    }
}
