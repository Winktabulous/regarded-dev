package com.zenyte.game.content.event.christmas2019.cutscenes;

import com.google.common.base.Preconditions;
import com.zenyte.game.content.event.christmas2019.ChristmasConstants;
import com.zenyte.game.content.event.christmas2019.ChristmasUtils;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.Cutscene;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraLookAction;
import com.zenyte.game.world.entity.player.cutscene.actions.CameraPositionAction;
import com.zenyte.game.world.entity.player.cutscene.actions.DialogueAction;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import mgi.types.config.ObjectDefinitions;
import mgi.utilities.CollectionUtils;

import java.util.EnumMap;

/**
 * @author Kris | 19/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PastScourgeCutsceneP2 extends Cutscene {
    private final Player player;
    private final LandOfSnowInstance instance;
    private final Runnable unfadeRunnable;
    private CutsceneImp imp;
    private final EnumMap<FrozenGuest, NPC> frozenGuestsMap = new EnumMap<>(FrozenGuest.class);

    @Override
    public void build() {
        final String impName = ChristmasUtils.getImpName(player);
        addActions(0, () -> player.setLocation(instance.getLocation(2079, 5404, 0)), () -> player.getAppearance().setInvisible(true), () -> player.getVarManager().sendBit(4606, 1), () -> {
            player.getVarManager().sendBit(15018, 0);
            for (final FrozenGuest guest : FrozenGuest.getValues()) {
                final NPC npc = new NPC(guest.getCutsceneBaseNPC(), instance.getLocation(guest.getTile()), guest.getDirection(), 0);
                npc.spawn();
                frozenGuestsMap.put(guest, npc);
                player.getVarManager().sendBit(ObjectDefinitions.getOrThrow(guest.getBaseObject()).getVarbitId(), 0);
            }
        });
        addActions(1, new CameraPositionAction(player, instance.getLocation(2079, 5398, 0), 1600, -128, 0), new CameraLookAction(player, instance.getLocation(2079, 5401, 0), 200, -128, 0));
        addActions(2, unfadeRunnable);
        addActions(6, () -> {
            imp = new CutsceneImp(player.getLocation().transform(Direction.SOUTH, 1));
            imp.spawn();
            imp.setGraphics(new Graphics(2508));
        });
        addActions(9, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "What's the big deal? They all look fine. A " +
                        "bit bored, mebbe.", Expression.HIGH_REV_NORMAL);
            }
        }));
        addActions(11, () -> {
            //perform animation
            frozenGuestsMap.forEach((guest, npc) -> {
                npc.setInvalidAnimation(guest.getPreFreezeAnimation());
                World.sendGraphics(new Graphics(2507), npc.getLocation());
            });
        });
        addActions(12, () -> {
            player.getVarManager().sendBit(15018, 1);//Hide all npcs.
            for (final FrozenGuest guest : FrozenGuest.getValues()) {
                player.getVarManager().sendBit(ObjectDefinitions.getOrThrow(guest.getBaseObject()).getVarbitId(), 1);
            }
        });
        final String chars = ChristmasUtils.getFrozenGuestOrder(player);
        final int length = chars.length();
        for (int i = 0; i < length; i++) {
            final char character = chars.charAt(i);
            addActions(16 + (i * 2), () -> {
                final FrozenGuest guest = CollectionUtils.findMatching(FrozenGuest.getValues(), g -> g.getConstant() == character);
                Preconditions.checkArgument(guest != null);
                final Location tile = instance.getLocation(guest.getTile());
                final WorldObject object = World.getObjectWithId(tile, guest.getBaseObject());
                World.sendObjectAnimation(object, new Animation(15108));
                player.getPacketDispatcher().sendGraphics(new Graphics(1010, 0, 150), tile);
                imp.setFaceLocation(tile);
            });
        }
        addActions(28, new DialogueAction(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                npcWithId(ChristmasConstants.PERSONAL_SNOW_IMP, impName, "Oh man!", Expression.HIGH_REV_NORMAL);
            }
        }));
        addActions(30, () -> {
            imp.finish();
            World.sendGraphics(new Graphics(2508), imp.getLocation());
        });
        final FadeScreen fadeScreen = new FadeScreen(player);
        addActions(32, fadeScreen::fade);
        addActions(34, () -> {
            try {
                player.getAppearance().setInvisible(false);
                final ScourgeHouseInstance instance = new ScourgeHouseInstance(MapBuilder.findEmptyChunk(4, 4));
                instance.constructRegion();
                player.getCutsceneManager().play(new PastScourgeCutsceneP3(player, instance, () -> fadeScreen.unfade(false)));
            } catch (OutOfSpaceException e) {
                e.printStackTrace();
            }
        });
    }

    public PastScourgeCutsceneP2(Player player, LandOfSnowInstance instance, Runnable unfadeRunnable) {
        this.player = player;
        this.instance = instance;
        this.unfadeRunnable = unfadeRunnable;
    }
}
