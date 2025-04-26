package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

import java.util.Map.Entry;

/**
 * @author Kris | 24. march 2018 : 16:09.34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WoodcuttingShrine implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.getActionManager().setAction(new ShrineAction(object));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SHRINE };
    }

    private static final class ShrineAction extends Action {

        private static final Projectile GREEN_PROJ = new Projectile(1309, 0, 255, 0, 15, 100, 0, 5);

        private static final Projectile RED_PROJ = new Projectile(1308, 0, 255, 0, 15, 100, 0, 5);

        private static final Projectile BLUE_PROJ = new Projectile(1307, 0, 255, 0, 15, 100, 0, 5);

        private static final Animation OFFERING_ANIM = new Animation(896);

        public ShrineAction(final WorldObject object) {
            from = new Location(object.getX() + 1, object.getY() + 1, object.getPlane());
            to = new Location(object.getX() + 2, object.getY() + 1, object.getPlane());
        }

        private final Location from, to;

        @Override
        public boolean start() {
            if (getEgg(false) == null) {
                player.sendMessage("You need a bird's egg to pray at the shrine.");
                return false;
            }
            return true;
        }

        @Override
        public boolean process() {
            return true;
        }

        @Override
        public int processWithDelay() {
            final Item egg = getEgg(true);
            if (egg == null) {
                return -1;
            }
            player.getSkills().addXp(SkillConstants.PRAYER, 100);
            player.setAnimation(OFFERING_ANIM);
            giveRewards();
            switch(egg.getId()) {
                case 5076:
                    World.sendProjectile(from, to, RED_PROJ);
                    break;
                case 5077:
                    World.sendProjectile(from, to, BLUE_PROJ);
                    break;
                default:
                    World.sendProjectile(from, to, GREEN_PROJ);
                    break;
            }
            return 3;
        }

        private static final int[] EVIL_CHICKEN_PIECES = new int[] { 20433, 20436, 20439, 20442 };

        /**
         * Gives the player a bird nest containing seeds, or if they're lucky, a piece of the evil chicken set.
         */
        private final void giveRewards() {
            if (Utils.random(300) == 0) {
                final Item piece = new Item(EVIL_CHICKEN_PIECES[Utils.random(EVIL_CHICKEN_PIECES.length - 1)]);
                player.getInventory().addItem(piece);
                final String name = piece.getName().toLowerCase();
                player.sendMessage("You offer the egg to the shrine and receive " + (name.endsWith("s") ? "" : "an ") + name + "!");
            } else {
                player.getInventory().addItem(new Item(5073));
                player.sendMessage("You offer the egg to the shrine and receive a bird nest.");
            }
        }

        /**
         * Gets the next egg in the players' inventory.
         *
         * @return the egg item, or null if they don't have any.
         */
        private final Item getEgg(final boolean delete) {
            for (final Entry<Integer, Item> entry : player.getInventory().getContainer().getItems().int2ObjectEntrySet()) {
                final Item item = entry.getValue();
                final int id = item.getId();
                if (id >= 5076 && id <= 5078) {
                    if (delete) {
                        player.getInventory().deleteItem(entry.getKey(), item);
                    }
                    return item;
                }
            }
            return null;
        }
    }
}
