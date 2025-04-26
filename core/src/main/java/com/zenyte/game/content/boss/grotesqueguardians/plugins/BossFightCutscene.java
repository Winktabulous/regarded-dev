package com.zenyte.game.content.boss.grotesqueguardians.plugins;

import com.zenyte.game.content.boss.grotesqueguardians.instance.GrotesqueGuardiansInstance;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.cutscene.Cutscene;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraResetAction;
import com.zenyte.plugins.dialogue.PlainChat;

/**
 * @author Tommeh | 22/07/2019 | 13:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class BossFightCutscene extends Cutscene {
    
    private final GrotesqueGuardiansInstance instance;
    
    public BossFightCutscene(final GrotesqueGuardiansInstance instance) {
        this.instance = instance;
    }
    
    @Override
    public void build() {
        addActions(0, () -> {
            player.lock();
            player.getVarManager().sendBit(4606, 1);
            player.getDialogueManager().start(new PlainChat(player, "You ring the bell, the sound echoes out across the roof...", false));
        });
        addActions(2, new CameraLookAction(player, instance.getLocation(new Location(1705, 4569, 0)), 0, 8, 8),
                new CameraPositionAction(player, instance.getLocation(new Location(1705, 4571, 0)), 200, 8, 8));
        addActions(4, () -> instance.start(true),
                new CameraLookAction(player, instance.getLocation(new Location(1688, 4578, 0)), 0, 8, 8),
                new CameraPositionAction(player, instance.getLocation(new Location(1711, 4571, 0)), 600, 8, 8));
        addActions(19, () -> {
            player.getVarManager().sendBit(4606, 0);
            player.unlock();
        }, new CameraResetAction(player));
    }
}
