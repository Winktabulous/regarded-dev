package com.zenyte.game.content.pyramidplunder.object;

import com.zenyte.game.content.pyramidplunder.PlunderRewardTier;
import com.zenyte.game.content.pyramidplunder.PlunderRoom;
import com.zenyte.game.content.pyramidplunder.PlunderableObject;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.IntListUtils;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.IntList;
import mgi.types.config.ObjectDefinitions;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Christopher
 * @since 4/1/2020
 */
public class Urn implements ObjectAction, PlunderableObject {
    private static final IntList urnIds = IntListUtils.unmodifiable(ArrayUtils.add(IntStream.rangeClosed(26600, 26613).toArray(), 26580));
    private static final int CLOSED = 0;
    private static final int LOOTED = 1;
    private static final int SNAKE = 2;
    private static final int CHARMED = 3;
    private static final Animation searchAnim = new Animation(4340);
    private static final Animation injuredAnim = new Animation(4341);
    private static final Animation charmSnakeAnim = new Animation(1877);
    private static final Animation snakeAwakeAnim = new Animation(4335);
    private static final Animation successAnim = new Animation(4342);
    private static final SoundEffect openUrnSound = new SoundEffect(1217);
    private static final SoundEffect snakeSound = new SoundEffect(1216);
    private static final SoundEffect snakeCharmSound = new SoundEffect(1219);
    private static final ForceTalk failTalk = new ForceTalk("Ow!");
    private static final List<PlunderRewardTier> rewards = new ArrayList<>();

    static {
        rewards.addAll(Collections.nCopies(2, PlunderRewardTier.IVORY));
        rewards.addAll(Collections.nCopies(9, PlunderRewardTier.POTTERY));
        rewards.addAll(Collections.nCopies(3, PlunderRewardTier.STONE));
        rewards.addAll(Collections.nCopies(1, PlunderRewardTier.GOLD));
    }

    public static void reset(@NotNull final Player player) {
        for (int urnId : urnIds) {
            player.getVarManager().sendBit(ObjectDefinitions.getOrThrow(urnId).getVarbitId(), CLOSED);
        }
    }

    @Override
    public void handleObjectAction(final Player player, final WorldObject urnObject, final String name, final int optionId, final String option) {
        final int objectStage = player.getVarManager().getBitValue(urnObject.getDefinitions().getVarbitId());
        if (objectStage == LOOTED) {
            player.sendFilteredMessage("You have already looted this.");
            return;
        }
        final boolean failure = Utils.random(4) == 0 && objectStage != CHARMED;
        switch (option.toLowerCase()) {
        case "search": 
            if (failure) {
                fail(player);
            } else {
                succeed(player, urnObject);
            }
            break;
        case "check for snakes": 
            checkForSnakes(player, urnObject);
            break;
        case "charm snake": 
            charmSnake(player, urnObject);
            break;
        }
    }

    private void checkForSnakes(@NotNull final Player player, @NotNull final WorldObject urnObject) {
        final PlunderRoom currentRoom = PlunderRoom.get(player.getVarManager().getBitValue(PlunderDoor.ROOM_VARBIT));
        World.sendObjectAnimation(urnObject, snakeAwakeAnim);
        player.getVarManager().sendBit(urnObject.getDefinitions().getVarbitId(), SNAKE);
        player.getSkills().addXp(SkillConstants.THIEVING, currentRoom.getUrnExp() / 3);
        player.sendSound(snakeSound);
    }

    private void charmSnake(@NotNull final Player player, @NotNull final WorldObject urnObject) {
        if (!player.carryingItem(ItemId.SNAKE_CHARM)) {
            player.sendFilteredMessage("You need a snake charm to do this.");
            return;
        }
        player.lock(2);
        player.setAnimation(charmSnakeAnim);
        WorldTasksManager.schedule(() -> {
            player.sendSound(snakeCharmSound);
            player.getVarManager().sendBit(urnObject.getDefinitions().getVarbitId(), CHARMED);
        });
    }

    private void succeed(@NotNull final Player player, @NotNull final WorldObject urnObject) {
        final PlunderRoom currentRoom = PlunderRoom.get(player.getVarManager().getBitValue(PlunderDoor.ROOM_VARBIT));
        final int varbitId = urnObject.getDefinitions().getVarbitId();
        /*
         * If player has not checked urn for snakes we give full exp, otherwise we give the difference between full
         * experience and check experience, e.g totalExp - (totalExp / 3). This ensures that no matter what player does,
         * they'll always get same amount of experience for looting an urn.
         */
        final double exp = player.getVarManager().getBitValue(varbitId) == CLOSED ? currentRoom.getUrnExp() : currentRoom.getUrnExp() - (currentRoom.getUrnExp() / 3);
        player.lock(3);
        player.setAnimation(searchAnim);
        player.sendSound(openUrnSound);
        player.faceObject(urnObject);
        WorldTasksManager.schedule(() -> {
            player.faceObject(urnObject);
            player.setAnimation(successAnim);
            player.getSkills().addXp(SkillConstants.THIEVING, exp);
        });
        WorldTasksManager.schedule(() -> {
            reward(player);
            player.getVarManager().sendBit(varbitId, LOOTED);
        }, 2);
    }

    private void fail(@NotNull final Player player) {
        player.lock(3);
        player.setAnimation(injuredAnim);
        WorldTasksManager.schedule(() -> {
            player.setForceTalk(failTalk);
            player.applyHit(new Hit(Utils.random(1, 4), HitType.REGULAR).setExecuteIfLocked());
            player.getToxins().applyToxin(Toxins.ToxinType.POISON, 2);
        });
    }

    @Override
    public Object[] getObjects() {
        return urnIds.toArray();
    }

    @Override
    public List<PlunderRewardTier> getRewardTiers() {
        return rewards;
    }
}
