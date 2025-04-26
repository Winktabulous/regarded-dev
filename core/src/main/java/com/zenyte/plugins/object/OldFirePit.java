package com.zenyte.plugins.object;

import com.zenyte.ContentConstants;
import com.zenyte.game.content.skills.firemaking.FiremakingTool;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.VarManager;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import mgi.types.config.ObjectDefinitions;
import mgi.utilities.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Kris | 10/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OldFirePit implements ObjectAction {

    private enum FireType {
        FIRE_OF_ETERNAL_LIGHT(35, new Item(2347, 1), new Item(8794, 1), new Item(590, 1), new Item(8782, 2), new Item(2353, 2), new Item(22593, 300), new Item(22595, 100), new Item(22597, 50)),
        FIRE_OF_NOURISHMENT(35, new Item(2347, 1), new Item(8794, 1), new Item(590, 1), new Item(8782, 2), new Item(2353, 2), new Item(22597, 150), new Item(22595, 100), new Item(22593, 50)),
        FIRE_OF_DEHUMIDIFICATION(50, new Item(2347, 1), new Item(8794, 1), new Item(590, 1), new Item(8782, 2), new Item(2353, 2), new Item(22595, 300), new Item(22597, 100), new Item(22593, 50)),
        FIRE_OF_UNSEASONAL_WARMTH(60, new Item(2347, 1), new Item(8794, 1), new Item(590, 1), new Item(8782, 2), new Item(2353, 2), new Item(22597, 300), new Item(22593, 100), new Item(22595, 50));

        private final int level;
        private final Item[] requiredItems;

        FireType(final int level, final Item... requiredItems) {
            this.level = level;
            this.requiredItems = requiredItems;
        }
    }


    public enum FirePit {
        WEISS_FIRE(33334, FireType.FIRE_OF_NOURISHMENT, null),
        GODWARS_DUNGEON_FIRE(33335, FireType.FIRE_OF_UNSEASONAL_WARMTH, player -> player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY)),
        GIANT_MOLE_FIRE(33336, FireType.FIRE_OF_ETERNAL_LIGHT, player -> player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY)),
        LUMBRIDGE_SWAMP_CAVES_FIRE(33337, FireType.FIRE_OF_ETERNAL_LIGHT, player -> player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY)),
        MOS_LE_HARMLESS_FIRE(33338, FireType.FIRE_OF_ETERNAL_LIGHT, player -> player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY)),
        MORT_MYRE_SWAMP_FIRE(33339, FireType.FIRE_OF_DEHUMIDIFICATION, null);

        private static final FirePit[] pits = values();
        private final int objectId;
        private final FireType type;
        private final Consumer<Player> onBuild;
        private final int varbitId;

        public boolean isBuilt(@NotNull final Player player) {
            return player.getVarManager().getBitValue(varbitId) == (this == WEISS_FIRE ? 205 : 1);
        }

        static {
            for (final OldFirePit.FirePit pit : pits) {
                VarManager.appendPersistentVarbit(pit.varbitId);
            }
        }

        FirePit(int objectId, FireType type, Consumer<Player> onBuild) {
            this.objectId = objectId;
            this.type = type;
            this.onBuild = onBuild;
            this.varbitId = Objects.requireNonNull(ObjectDefinitions.get(objectId)).getVarbit();
        }
    }

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final OldFirePit.FirePit pit = CollectionUtils.findMatching(FirePit.pits, p -> p.objectId == object.getId());
        assert pit != null;
        if (pit.isBuilt(player)) {
            player.getDialogueManager().start(new PlainChat(player, "You've already built this fire pit."));
            return;
        }
        player.getActionManager().setAction(new Action() {
            @Override
            public boolean start() {
                final Item[] items = pit.type.requiredItems;
                if (!player.getInventory().containsItems(items)) {
                    final StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < items.length; i++) {
                        final Item item = items[i];
                        builder.append(item.getAmount()).append(" x ").append(item.getName()).append(i == items.length - 2 ? " and " : ", ");
                    }
                    builder.delete(builder.length() - 2, builder.length());
                    player.getDialogueManager().start(new PlainChat(player, "You need " + builder + " to build the fire pit."));
                    return false;
                }
                if (ContentConstants.CONSTRUCTION) {
                    if (player.getSkills().getLevel(SkillConstants.CONSTRUCTION) < pit.type.level) {
                        player.getDialogueManager().start(new PlainChat(player, "You need a Construction level of at least " + pit.type.level + " to build the fire pit."));
                        return false;
                    }
                }
                delay(2);
                player.setAnimation(FiremakingTool.TINDERBOX.getAnimation());
                return true;
            }
            @Override
            public boolean process() {
                return true;
            }
            @Override
            public int processWithDelay() {
                final Inventory inventory = player.getInventory();
                for (final Item item : pit.type.requiredItems) {
                    if (item.getAmount() <= 1) {
                        continue;
                    }
                    inventory.deleteItem(item);
                }
                if (pit.onBuild != null) {
                    pit.onBuild.accept(player);
                }
                player.getVarManager().sendBit(pit.varbitId, pit == FirePit.WEISS_FIRE ? 205 : 1);
                player.getSkills().addXp(SkillConstants.FIREMAKING, 300);
                if (ContentConstants.CONSTRUCTION) {
                    player.getSkills().addXp(SkillConstants.CONSTRUCTION, pit.type.level * 10);
                }
                player.setAnimation(Animation.STOP);
                return -1;
            }
            @Override
            public boolean interruptedByCombat() {
                return false;
            }
        });
    }

    @Override
    public Object[] getObjects() {
        final IntArrayList list = new IntArrayList();
        for (final OldFirePit.FirePit pit : FirePit.pits) {
            list.add(pit.objectId);
        }
        return list.toArray();
    }

}
