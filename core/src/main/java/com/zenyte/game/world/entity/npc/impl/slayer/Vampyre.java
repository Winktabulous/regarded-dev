package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

public class Vampyre extends NPC implements Spawnable {

    public Vampyre(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
        forceAggressive = true;
        aggressionDistance = 5;
        deathDelay = 1;
    }

    @Override
    public float getXpModifier(Hit hit) {
        if (hit.getSource() instanceof Player player) {
            int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
            if (weaponId != ItemId.IVANDIS_FLAIL && weaponId != ItemId.BLISTERWOOD_FLAIL) {
                hit.setDamage(0);
                player.sendMessage("You must use a Blisterwood Flail, or Ivandis flail to harm Vampyres.");
                return 0;
            }
        }

        return super.getXpModifier(hit);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("Vampyre Juvinate") || name.equalsIgnoreCase("Vampyre Juvenile")
                || name.equalsIgnoreCase("Vyrewatch Sentinel") || name.equalsIgnoreCase("Vyre") || name.equalsIgnoreCase("Vyrewatch");
    }

    @Override
    public boolean canBeMulticannoned(@NotNull Player player) {
        return false;
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

}
