package com.zenyte.game.content.area.prifddinas.zalcano.combat.actions;

import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoConstants;
import com.zenyte.game.content.area.prifddinas.zalcano.ZalcanoInstance;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.calog.CAType;

import java.util.Set;

public class DroppingBouldersAction extends TickTask {

    private final Set<Location> locations;
    private final ZalcanoInstance instance;

    public DroppingBouldersAction(final Set<Location> locations, final ZalcanoInstance instance) {
        this.locations = locations;
        this.instance = instance;
    }

    @Override
    public void run() {
        switch (ticks) {
            case 0:
                for (Location location : locations) {
                    World.sendGraphics(new Graphics(ZalcanoConstants.FALLEN_BOULDER_GFX), location);
                }
                break;
            case 7:
                for (Location location : locations) {
                    for (Player player : instance.getPlayers()) {
                        if (player.getLocation().hashCode() == location.hashCode()) {
                            player.getCombatAchievements().setCurrentTaskValue(CAType.PERFECT_ZALCANO, 0);
                            CombatUtilities.processHit(player, new Hit(instance.getZalcano(), Utils.random(1, 40), HitType.REGULAR));
                        }
                    }
                }
                this.stop();
                return;
        }
        ticks++;
    }


}
