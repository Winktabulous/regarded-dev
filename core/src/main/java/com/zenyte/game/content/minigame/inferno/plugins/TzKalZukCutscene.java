package com.zenyte.game.content.minigame.inferno.plugins;

import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.npc.impl.zuk.AncestralGlyph;
import com.zenyte.game.content.minigame.inferno.npc.impl.zuk.TzKalZuk;
import com.zenyte.game.model.CameraShakeType;
import com.zenyte.game.model.MinimapState;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.cutscene.Cutscene;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraResetAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraShakeAction;
import com.zenyte.game.world.entity.player.dialogue.impl.NPCChat;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Tommeh | 12/12/2019 | 00:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class TzKalZukCutscene extends Cutscene {
    private static final Location playerStartLocation = new Location(2271, 5356, 0);
    private final Inferno inferno;
    private AncestralGlyph glyph;
    private TzKalZuk zuk;

    public TzKalZukCutscene(final Inferno inferno) {
        this.inferno = inferno;
    }

    @Override
    public void build() {
        addActions(0, () -> {
            player.setLocation(inferno.getLocation(playerStartLocation));
            player.getPacketDispatcher().sendMinimapState(MinimapState.MAP_DISABLED);
            player.getVarManager().sendBit(4606, 1);
            player.getVarManager().sendBit(5652, 1);
            zuk = (TzKalZuk) new TzKalZuk(inferno).spawn();
            glyph = (AncestralGlyph) new AncestralGlyph(inferno).spawn();
            zuk.setGlyph(glyph);
            inferno.add(zuk);
            zuk.setAnimation(new Animation(7563));
            player.faceEntity(zuk);
            player.lock();
            World.removeObject(new WorldObject(30338, 10, 0, inferno.getLocation(AncestralGlyph.spawnLocation)));
            for (final WorldObject rocks : inferno.getFallingRocks()) {
                World.spawnObject(rocks);
            }
            for (final WorldObject patch : inferno.getWallPatches()) {
                World.spawnObject(patch);
            }
            player.getDialogueManager().start(new NPCChat(player, 7690, "Oh no! TzKal-Zuk's prison is breaking down. " +
                    "This not meant to have happened. There's nothing I can do for you now JalYt!", false));
        });
        addActions(2, new CameraLookAction(player, inferno.getLocation(new Location(2270, 5364, 0)), 0, 100, 100), new CameraPositionAction(player, inferno.getLocation(new Location(2274, 5347, 0)), 800, 100, 100), new CameraShakeAction(player, CameraShakeType.UP_AND_DOWN, (byte) 10, (byte) 5, (byte) 0));
        addActions(3, () -> {
            for (final WorldObject rocks : inferno.getFallingRocks()) {
                World.sendObjectAnimation(rocks, Inferno.fallingRocksAnim);
            }
            glyph.addWalkSteps(inferno.getX(2270), inferno.getY(5361), -1, false);
            player.getVarManager().sendBit(5653, zuk.getMaxHitpoints());
            player.getVarManager().sendBit(5654, zuk.getMaxHitpoints());
        });
        addActions(11, () -> {
            zuk.start();
            glyph.setMovable(true);
            player.getPacketDispatcher().sendMinimapState(MinimapState.ENABLED);
            player.getInterfaceHandler().sendInterface(InterfacePosition.MINIGAME_OVERLAY, 596);
            player.getVarManager().sendBit(4606, 0);
            player.getDialogueManager().finish();
            player.unlock();
        }, new CameraResetAction(player));
    }
}
