package com.zenyte.game.content.boss.skotizo.plugins;

import com.zenyte.game.content.boss.skotizo.instance.SkotizoInstance;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tommeh | 05/03/2020 | 18:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class DarkAltar implements ObjectAction, ItemOnObjectAction {

    private static final Logger log = LoggerFactory.getLogger(DarkAltar.class);

    private static final Item darkTotem = new Item(ItemId.DARK_TOTEM);

    private static final Animation altarResetAnimation = new Animation(1471);

    private static final Animation altarAnimation = new Animation(1472);

    public static final Animation teleportAnimation = new Animation(3865);

    public static final Graphics teleportGraphics = new Graphics(1296);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Teleport")) {
            teleport(player, object);
        }
    }

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        teleport(player, object);
    }

    private void teleport(final Player player, final WorldObject object) {
        if (!player.getInventory().containsItem(darkTotem)) {
            player.sendMessage("You need a dark totem to teleport through the altar.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {

            @Override
            public void buildDialogue() {
                options("<col=ff0000>You will lose all of your items dropped if you die!</col>", "I know I\'m risking everything I have.", "I need to prepare some more.").onOptionOne(() -> setKey(5));
                options(5, "Are you sure?", "Yes, I know items dropped in the instance will be lost.", "On second thoughts, better not.").onOptionOne(() -> {
                    player.lock();
                    WorldTasksManager.schedule(new WorldTask() {

                        int ticks;

                        @Override
                        public void run() {
                            switch(ticks++) {
                                case 0:
                                    player.getInventory().deleteItem(darkTotem);
                                    World.sendObjectAnimation(object, altarAnimation);
                                    return;
                                case 1:
                                    player.setAnimation(teleportAnimation);
                                    player.setGraphics(teleportGraphics);
                                    return;
                                case 2:
                                    new FadeScreen(player).fade(3);
                                    return;
                                case 4:
                                    World.sendObjectAnimation(object, altarResetAnimation);
                                    return;
                                case 5:
                                    try {
                                        final AllocatedArea area = MapBuilder.findEmptyChunk(8, 8);
                                        final SkotizoInstance instance = new SkotizoInstance(player, area);
                                        instance.constructRegion();
                                    } catch (OutOfSpaceException e) {
                                        log.error("", e);
                                    }
                                    stop();
                                    return;
                            }
                        }
                    }, 0, 0);
                });
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.DARK_TOTEM };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ALTAR_28900 };
    }
}
