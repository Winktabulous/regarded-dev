package com.zenyte.game.content.skills.construction.objects.superiorgarden;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.FurnitureData;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.constants.Furniture;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 26. veebr 2018 : 2:50.07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 * TODO: Check if the player has killed that specific boss or not.
 */
public final class TopiaryBush implements ObjectInteraction {

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TOPIARY_BUSH, ObjectId.KRAKEN_TOPIARY, ObjectId.ZULRAH_TOPIARY, ObjectId.KALPHITE_QUEEN_TOPIARY, ObjectId.CERBERUS_TOPIARY, ObjectId.ABYSSAL_SIRE_TOPIARY, ObjectId.SKOTIZO_TOPIARY, ObjectId.VORKATH_TOPIARY };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        if (option.equals("clip")) {
            player.getDialogueManager().start(new TopiarySelectionD(player, reference, object.getId()));
        }
    }

    public static final class TopiarySelectionD extends Dialogue {

        public static final Animation SECATEURS_ANIM = new Animation(2278);

        public static final Animation MAGIC_SECATEURS_ANIM = new Animation(3341);

        public TopiarySelectionD(final Player player, final RoomReference reference, final int objectId) {
            super(player);
            this.reference = reference;
            this.objectId = objectId;
        }

        private final int objectId;

        private final RoomReference reference;

        @Override
        public void buildDialogue() {
            options("Select a type.", "Kraken topiary.", "Zulrah topiary.", "Kalphite queen topiary.", "Cerberus topiary.", "Next selection.").onOptionOne(() -> {
                clipTopiary(0);
                finish();
            }).onOptionTwo(() -> {
                clipTopiary(1);
                finish();
            }).onOptionThree(() -> {
                clipTopiary(2);
                finish();
            }).onOptionFour(() -> {
                clipTopiary(3);
                finish();
            }).onOptionFive(() -> setKey(5));
            options(5, "Select a type.", "Abyssal sire topiary.", "Skotizo topiary.", "Vorkath topiary.", "Previous selection.").onOptionOne(() -> {
                clipTopiary(4);
                finish();
            }).onOptionTwo(() -> {
                clipTopiary(5);
                finish();
            }).onOptionThree(() -> {
                clipTopiary(6);
                finish();
            }).onOptionFour(() -> setKey(1));
        }

        private final void clipTopiary(final int id) {
            Furniture furniture;
            if (objectId == 29230) {
                furniture = Furniture.TOPIARY_BUSH;
            } else {
                final int index = Furniture.KRAKEN_TOPIARY.ordinal() + (objectId == 31985 ? 6 : (objectId - 29231));
                if (index < 0 || index >= Furniture.VALUES.length)
                    return;
                final Furniture furn = Furniture.VALUES[index];
                if (furn == null || !furn.toString().contains("topiary"))
                    return;
                furniture = furn;
            }
            final FurnitureData data = reference.getFurniture(furniture);
            if (data == null)
                return;
            final int index = Furniture.KRAKEN_TOPIARY.ordinal() + id;
            if (index < 0 || index >= Furniture.VALUES.length)
                return;
            final Furniture furn = Furniture.VALUES[index];
            if (furn == null || !furn.toString().contains("topiary"))
                return;
            if (furn == furniture) {
                player.sendMessage("The bush is already of that style.");
                return;
            }
            if (!player.getInventory().containsItem(5329, 1) && !player.getInventory().containsItem(7409, 1)) {
                player.sendMessage("You need some secateurs to clip the bush.");
                return;
            }
            player.lock();
            data.setFurniture(furn);
            final int x = (reference.getX() * 8 - (player.getConstruction().getYardOffset() * 8)) % 64;
            final int y = (reference.getY() * 8 - (player.getConstruction().getYardOffset() * 8)) % 64;
            final Location tile = new Location(player.getConstruction().getChunkX() * 8 + x + data.getLocation().getX(), player.getConstruction().getChunkY() * 8 + y + data.getLocation().getY(), player.getPlane());
            if (player.getInventory().containsItem(7409, 1))
                player.setAnimation(MAGIC_SECATEURS_ANIM);
            else
                player.setAnimation(SECATEURS_ANIM);
            WorldTasksManager.schedule(new WorldTask() {

                @Override
                public void run() {
                    player.unlock();
                    World.spawnObject(new WorldObject(data.getFurniture().getObjectId(), 10, data.getRotation(), tile));
                }
            }, 6);
        }
    }
}
