package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

/**
 * @author Tommeh | 29 nov. 2017 : 22:02:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Rockslug extends NPC implements Spawnable {
    public static final Item BAG_OF_SALT = new Item(4161);
    private static final Item BRINE_SABRE = new Item(11037);

    public Rockslug(final int id, final Location tile, final Direction direction, final int radius) {
        super(id, tile, direction, radius);
    }

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

    @Override
    public boolean validate(final int id, final String name) {
        return id == 421 || id == 422;
    }
}
