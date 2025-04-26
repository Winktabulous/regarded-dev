package com.zenyte.game.world.entity.npc.impl.slayer.superior.impl;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:01
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GiantRockslug extends SuperiorNPC {
    public GiantRockslug(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7392, tile);
    }

    private static final Item BAG_OF_SALT = new Item(4161);
    private static final Item BRINE_SABRE = new Item(11037);
    private boolean dying;

    @Override
    public NPC spawn() {
        dying = false;
        return super.spawn();
    }

    @Override
    public void sendDeath() {
        if (dying) {
            return;
        }
        final Player source = getMostDamagePlayerCheckIronman();
        if (source == null) {
            super.sendDeath();
            return;
        }
        final boolean isUnlocked = source.getSlayer().isUnlocked("Slug salter");
        if (getHitpoints() == 0 && ((source.getEquipment().getId(EquipmentSlot.WEAPON) != BRINE_SABRE.getId()) && (!isUnlocked || !source.getInventory().containsItem(BAG_OF_SALT)))) {
            heal(1);
        } else {
            dying = true;
            source.getInventory().deleteItem(BAG_OF_SALT);
            source.sendMessage("The rockslug shrivels up and dies.");
            super.sendDeath();
        }
    }
}
