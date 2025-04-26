package com.zenyte.game.content.event.christmas2019;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.WorldThread;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.utilities.CollectionUtils;

/**
 * @author Corey
 * @since 17/12/2019
 */
public class ScourgeCupboard implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.SPOKEN_TO_SCOURGE)) {
            player.sendMessage("You probably don't want to do that right now with Scourge wandering around.");
            return;
        }
        if (AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.SANTA_FREED)) {
            player.sendMessage("You probably shouldn't be rummaging through Scourge's cupboards anymore.");
            return;
        }
        final ScourgeCupboard.Cupboard cupboard = Cupboard.forObjectId(object.getId());
        switch (option) {
        case "Search": 
            if (player.getNumericTemporaryAttribute("Christmas scourge cupboard use delay").longValue() >= WorldThread.WORLD_CYCLE) {
                return;
            }
            cupboard.search(player);
            break;
        case "Open": 
            if (player.getNumericTemporaryAttribute("Christmas scourge cupboard use delay").longValue() >= WorldThread.WORLD_CYCLE) {
                return;
            }
            cupboard.open(player);
            break;
        case "Close": 
            player.getTemporaryAttributes().put("Christmas scourge cupboard use delay", WorldThread.WORLD_CYCLE);
            cupboard.close(player);
            break;
        }
    }

    @Override
    public Object[] getObjects() {
        return Cupboard.byId.keySet().toArray(new Object[0]);
    }


    private enum Cupboard {
        NORTH_WEST(46098, ChristmasConstants.NW_CUPBOARD_VAR) {
            @Override
            public void search(final Player player) {
                player.sendMessage("You find a small cabbage.");
                player.getInventory().addItem(new Item(ItemId.CABBAGE));
                player.getTemporaryAttributes().put("Christmas scourge cupboard use delay", WorldThread.WORLD_CYCLE + 3);
                WorldTasksManager.schedule(() -> {
                    if (player.getVarManager().getBitValue(this.getVarbit()) != 0) {
                        player.sendMessage("The cupboard slams shut!");
                        close(player);
                    }
                }, 2);
            }
        },
        SOUTH_WEST(46096, ChristmasConstants.SW_CUPBOARD_VAR) {
            @Override
            public void search(final Player player) {
                final Item bedsheets = new Item(ChristmasConstants.BEDSHEETS_ID);
                if (player.getInventory().containsItem(bedsheets) || AChristmasWarble.progressedAtLeast(player, AChristmasWarble.ChristmasWarbleProgress.HAS_GHOST_COSTUME)) {
                    player.sendMessage("You don't find anything of use.");
                } else {
                    player.sendMessage("You find some crisp, folded bedsheets.");
                    if (player.getInventory().addItem(bedsheets).isFailure()) {
                        player.sendMessage("But your inventory is too full.");
                    }
                }
                player.getTemporaryAttributes().put("Christmas scourge cupboard use delay", WorldThread.WORLD_CYCLE + 3);
                WorldTasksManager.schedule(() -> {
                    if (player.getVarManager().getBitValue(this.getVarbit()) != 0) {
                        player.sendMessage("The cupboard slams shut!");
                        close(player);
                    }
                }, 2);
            }
        },
        SOUTH_EAST(46095, ChristmasConstants.SE_CUPBOARD_VAR) {
            @Override
            public void search(final Player player) {
                player.sendMessage("You find a needle and some thread.");
                player.getTemporaryAttributes().put("Christmas scourge cupboard use delay", WorldThread.WORLD_CYCLE + 3);
                player.getInventory().addOrDrop(new Item(ItemId.NEEDLE), new Item(ItemId.THREAD));
                WorldTasksManager.schedule(() -> {
                    if (player.getVarManager().getBitValue(this.getVarbit()) != 0) {
                        player.sendMessage("The cupboard slams shut!");
                        close(player);
                    }
                }, 2);
            }
        },
        NORTH_EAST(46097, ChristmasConstants.NE_CUPBOARD_VAR) {
            @Override
            public void search(final Player player) {
                player.sendMessage("The cupboard is empty except for a small family of spiders.");
                player.getTemporaryAttributes().put("Christmas scourge cupboard use delay", WorldThread.WORLD_CYCLE + 3);
                WorldTasksManager.schedule(() -> {
                    if (player.getVarManager().getBitValue(this.getVarbit()) != 0) {
                        player.sendMessage("The cupboard slams shut!");
                        close(player);
                    }
                }, 2);
            }
        };
        private static final Cupboard[] values = values();
        public static final Int2ObjectMap<Cupboard> byId;

        static {
            CollectionUtils.populateMap(values, byId = new Int2ObjectOpenHashMap<>(values.length), Cupboard::getObjectId);
        }

        private final int objectId;
        private final int varbit;

        public static Cupboard forObjectId(final int id) {
            return byId.get(id);
        }

        public abstract void search(final Player player);

        public void open(final Player player) {
            player.setAnimation(new Animation(536));
            player.getVarManager().sendBit(this.getVarbit(), 1);
        }

        public void close(final Player player) {
            player.getVarManager().sendBit(this.getVarbit(), 0);
        }

        Cupboard(int objectId, int varbit) {
            this.objectId = objectId;
            this.varbit = varbit;
        }

        public int getObjectId() {
            return objectId;
        }

        public int getVarbit() {
            return varbit;
        }
    }
}
