package com.zenyte.plugins.itemonobject;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.WorldObject;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Kris | 9. juuni 2018 : 05:50:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class FountainOfRunePlugin implements ItemOnObjectAction {

    public static final Animation ANIM = new Animation(832);

    public static final int BASIC_CHARGED_GLORY = 11978;

    public static final int TRIMMED_CHARGED_GLORY = 11964;

    public static final int BASIC_CHARGED_ROW = 11980;

    public static final int TRIMMED_CHARGED_ROW = 20786;

    public static final int CHARGED_SKILLS_NECKLACE = 11968;

    public static final int CHARGED_COMBAT_BRACELET = 11972;

    public static final int ETERNAL_GLORY = 19707;

    public static final int[] BASIC_GLORIES = new int[] { 1704, 1706, 1708, 1710, 1712, 11976 };

    public static final int[] TRIMMED_GLORIES = new int[] { 10362, 10360, 10358, 10356, 10354, 11966 };

    public static final int[] SKILLS_NECKLACES = new int[] { 11113, 11111, 11109, 11107, 11105, 11970 };

    public static final int[] COMBAT_BRACELETS = new int[] { 11126, 11124, 11122, 11120, 11118, 11974 };

    public static final int[] RINGS_OF_WEALTH = new int[] { 2572, 11988, 11986, 11984, 11982, 11980 };

    public static final int[] TRIMMED_RINGS_OF_WEALTH = new int[] { 12785, 20790, 20789, 20788, 20787, 20786 };

    private static final Location PROJECTILE_START_LOCATION = new Location(3374, 3893, 0);

    private static final List<Projectile> PROJECTILES = new ArrayList<>();

    public static final void initiate(final Set<Player> players) {
        WorldTasksManager.schedule(() -> {
            if (players == null || players.isEmpty()) {
                return;
            }
            int count = 0;
            for (int i = 0; i < Utils.random(1, 2); i++) {
                for (final Player player : players) {
                    if (count >= 10)
                        /* Cap to a maximum of 10 projectiles per tick. */
                        break;
                    if (Utils.random(2) == 0 && players.size() == 1)
                        continue;
                    final Location playerTile = player.getLocation();
                    Location tile = null;
                    int tryCount = 100;
                    while (--tryCount > 0) {
                        if ((tile = playerTile.transform(Utils.random(-2, 2), Utils.random(-2, 2), 0)).withinDistance(PROJECTILE_START_LOCATION, 1)) {
                            break;
                        }
                    }
                    final Projectile projectile = PROJECTILES.get(Utils.random(PROJECTILES.size() - 1));
                    projectile.setDelay(Utils.random(30));
                    /* Randomize the delay so the runes come out flowingly and randomly. */
                    World.sendProjectile(PROJECTILE_START_LOCATION, tile, projectile);
                    count++;
                }
            }
        }, 0, 0);
    }

    static {
        for (int i = 1261; i <= 1271; i++) {
            PROJECTILES.add(new Projectile(i, 100, 0, 0, 60, 58, 0, 5));
        }
    }

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
        player.lock(1);
        player.setAnimation(ANIM);
        WorldTasksManager.schedule(() -> {
            final Container container = player.getInventory().getContainer();
            container.setFullUpdate(true);
            for (int i = container.getContainerSize(); i >= 0; i--) {
                final Item containerItem = container.get(i);
                if (containerItem == null) {
                    continue;
                }
                final int id = containerItem.getId();
                final boolean isUnchargedGlory = ArrayUtils.contains(BASIC_GLORIES, id);
                final boolean isTrimmedUnchargedGlory = ArrayUtils.contains(TRIMMED_GLORIES, id);
                if (isUnchargedGlory || isTrimmedUnchargedGlory) {
                    if (Utils.random(2500) == 0) {
                        containerItem.setId(ETERNAL_GLORY);
                        player.sendMessage(Colour.RS_PURPLE + "The power of the fountain is transferred into an amulet of eternal glory. It will now have unlimited charges.");
                        continue;
                    }
                    containerItem.setId(isUnchargedGlory ? BASIC_CHARGED_GLORY : TRIMMED_CHARGED_GLORY);
                    continue;
                }
                final boolean isUnchargedRow = ArrayUtils.contains(RINGS_OF_WEALTH, id);
                final boolean isTrimmedRow = ArrayUtils.contains(TRIMMED_RINGS_OF_WEALTH, id);
                if (isUnchargedRow || isTrimmedRow) {
                    containerItem.setId(isUnchargedRow ? BASIC_CHARGED_ROW : TRIMMED_CHARGED_ROW);
                    continue;
                }
                if (ArrayUtils.contains(SKILLS_NECKLACES, id)) {
                    containerItem.setId(CHARGED_SKILLS_NECKLACE);
                    continue;
                }
                if (ArrayUtils.contains(COMBAT_BRACELETS, id)) {
                    containerItem.setId(CHARGED_COMBAT_BRACELET);
                    continue;
                }
            }
            container.refresh(player);
            player.getDialogueManager().start(new Dialogue(player) {

                @Override
                public void buildDialogue() {
                    item(item, "You feel a power emanating from the fountain as it recharges your jewellery.");
                }
            });
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1704, 1706, 1708, 1710, 1712, 11976, 10360, 10358, 10356, 10354, 11966, 11113, 11111, 11109, 11107, 11105, 11970, 11126, 11124, 11122, 11120, 11118, 11974, 2572, 11988, 11986, 11984, 11982, 11980, 12785, 20790, 20789, 20788, 20787, 20786 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 26782 };
    }
}
