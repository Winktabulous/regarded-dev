package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.content.event.christmas2019.cutscenes.FutureScourgeCutscene;
import com.zenyte.game.content.event.christmas2019.cutscenes.PastScourgeCutsceneP1;
import com.zenyte.game.content.event.christmas2019.cutscenes.PresentScourgeCutscene;
import com.zenyte.game.content.event.christmas2019.cutscenes.ScourgeHouseInstance;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.dialogue.Expression;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;

/**
 * @author Corey
 * @since 17/12/2019
 */
public class ScourgeStairs implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        switch (AChristmasWarble.getProgress(player)) {
        case SANTA_FREED: 
        case EVENT_COMPLETE: 
            player.getDialogueManager().start(new Dialogue(player, ChristmasConstants.PERSONAL_SNOW_IMP) {
                @Override
                public void buildDialogue() {
                    npc(ChristmasUtils.getImpName(player), "We've rescued Santa, we should let Scourge sleep now.", Expression.HIGH_REV_NORMAL);
                }
            });
            return;
        case SPOKEN_TO_PERSONAL_IMP: 
            player.getDialogueManager().start(new Dialogue(player, ChristmasConstants.SCOURGE_NPC_ID) {
                @Override
                public void buildDialogue() {
                    plain("A voice comes echoing through the mansion...");
                    npc("Don't you dare climb those stairs!", Expression.HIGH_REV_MAD);
                }
            });
            return;
        case FROZEN_GUESTS: 
            player.getDialogueManager().start(new Dialogue(player, ChristmasConstants.PERSONAL_SNOW_IMP) {
                @Override
                public void buildDialogue() {
                    npc(ChristmasUtils.getImpName(player), "We can't go back up there right now, we have to help the " +
                            "guests!", Expression.HIGH_REV_NORMAL);
                }
            });
            return;
        case GHOST_OF_CHRISTMAS_PAST: 
            if (!checkEquipment(player)) {
                return;
            }
            try {
                final ScourgeHouseInstance instance = new ScourgeHouseInstance(MapBuilder.findEmptyChunk(4, 4));
                instance.constructRegion();
                final FadeScreen fadeScreen = new FadeScreen(player);
                fadeScreen.fade();
                player.getCutsceneManager().play(new PastScourgeCutsceneP1(player, instance, () -> fadeScreen.unfade(false)));
            } catch (OutOfSpaceException e) {
                e.printStackTrace();
            }
            return;
        case GHOST_OF_CHRISTMAS_PRESENT: 
            if (!checkEquipment(player)) {
                return;
            }
            try {
                final ScourgeHouseInstance instance = new ScourgeHouseInstance(MapBuilder.findEmptyChunk(4, 4));
                instance.constructRegion();
                final FadeScreen fadeScreen = new FadeScreen(player);
                fadeScreen.fade();
                player.getCutsceneManager().play(new PresentScourgeCutscene(player, instance, () -> fadeScreen.unfade(false)));
            } catch (OutOfSpaceException e) {
                e.printStackTrace();
            }
            return;
        case GHOST_OF_CHRISTMAS_FUTURE: 
            if (!checkEquipment(player)) {
                return;
            }
            try {
                final ScourgeHouseInstance instance = new ScourgeHouseInstance(MapBuilder.findEmptyChunk(4, 4));
                instance.constructRegion();
                final FadeScreen fadeScreen = new FadeScreen(player);
                fadeScreen.fade();
                player.getCutsceneManager().play(new FutureScourgeCutscene(player, instance, () -> fadeScreen.unfade(false)));
            } catch (OutOfSpaceException e) {
                e.printStackTrace();
            }
            return;
        default: 
            player.getDialogueManager().start(new Dialogue(player, ChristmasConstants.PERSONAL_SNOW_IMP) {
                @Override
                public void buildDialogue() {
                    npc(ChristmasUtils.getImpName(player), "Scourge is up there, bud. We can't go up unprepared or " +
                            "he'll zap us.", Expression.HIGH_REV_NORMAL);
                }
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ChristmasConstants.SCOURGE_STAIRS};
    }

    private boolean checkEquipment(final Player player) {
        if (!ChristmasUtils.wearingGhostCostume(player)) {
            player.getDialogueManager().start(new Dialogue(player, ChristmasConstants.PERSONAL_SNOW_IMP) {
                @Override
                public void buildDialogue() {
                    player.faceEntity(player.getFollower());
                    npc(ChristmasUtils.getImpName(player), "You can't go up there without the costume on!", Expression.HIGH_REV_NORMAL);
                }
            });
            return false;
        }
        if (player.getEquipment().getItem(EquipmentSlot.CAPE) != null) {
            player.getDialogueManager().start(new Dialogue(player, ChristmasConstants.PERSONAL_SNOW_IMP) {
                @Override
                public void buildDialogue() {
                    player.faceEntity(player.getFollower());
                    npc(ChristmasUtils.getImpName(player), "You won't look much like a Ghost if ya wearing a cape, " +
                            "matey.", Expression.HIGH_REV_SCARED);
                    plain("The cape detracts from your disguise. You should take it off before climbing these stairs.");
                }
            });
            return false;
        }
        if (player.getEquipment().getItem(EquipmentSlot.WEAPON) != null) {
            player.getDialogueManager().start(new Dialogue(player, ChristmasConstants.PERSONAL_SNOW_IMP) {
                @Override
                public void buildDialogue() {
                    player.faceEntity(player.getFollower());
                    npc(ChristmasUtils.getImpName(player), "You won't look much like a Ghost if ya holding a weapon, " +
                            "matey.", Expression.HIGH_REV_SCARED);
                    plain("The weapon detracts from your disguise. You should take it off before climbing these stairs.");
                }
            });
            return false;
        }
        if (player.getEquipment().getItem(EquipmentSlot.SHIELD) != null) {
            player.getDialogueManager().start(new Dialogue(player, ChristmasConstants.PERSONAL_SNOW_IMP) {
                @Override
                public void buildDialogue() {
                    player.faceEntity(player.getFollower());
                    npc(ChristmasUtils.getImpName(player), "You won't look much like a Ghost if ya wielding a shield," +
                            " matey.", Expression.HIGH_REV_SCARED);
                    plain("The shield detracts from your disguise. You should take it off before climbing these stairs.");
                }
            });
            return false;
        }
        return true;
    }
}
