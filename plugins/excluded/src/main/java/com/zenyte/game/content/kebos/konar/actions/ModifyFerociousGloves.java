package com.zenyte.game.content.kebos.konar.actions;

import com.zenyte.game.content.skills.smithing.Smithing;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;

/**
 * @author Tommeh | 25/10/2019 | 00:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ModifyFerociousGloves extends Action {

    private static final Item hydraLeather = new Item(ItemId.HYDRA_LEATHER);
    private static final Item ferociousGloves = new Item(ItemId.FEROCIOUS_GLOVES);

    private static final Animation finalAnim = new Animation(811);
    private static final Animation objAnim1 = new Animation(7734);
    private static final Animation objAnim2 = new Animation(7735);

    private int ticks;
    private final WorldObject machinery;
    private final boolean create;

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public int processWithDelay() {
        if (ticks == 0) {
            player.setAnimation(Smithing.ANIMATION);
        } else if (ticks == 4) {
            player.setAnimation(finalAnim);
            World.sendObjectAnimation(machinery, objAnim1);
        } else if (ticks == 5) {
            World.sendObjectAnimation(machinery, objAnim2);
            if (create) {
                player.getInventory().deleteItemsIfContains(new Item[]{hydraLeather}, () -> {
                    player.getInventory().addItem(ferociousGloves);
                    player.getDialogueManager().start(new ItemChat(player, ferociousGloves, "By feeding the tough to work leather through the machine, you manage to form a pair of gloves."));
                });
            } else {
                player.getInventory().deleteItemsIfContains(new Item[]{ferociousGloves}, () -> {
                    player.getInventory().addItem(hydraLeather);
                    player.getDialogueManager().start(new ItemChat(player, hydraLeather, "By feeding the gloves through the machine, you manage to revert them into leather."));
                });
            }

            return ticks = -1;
        }
        ticks++;
        return 0;
    }

    public ModifyFerociousGloves(WorldObject machinery, boolean create) {
        this.machinery = machinery;
        this.create = create;
    }
}
