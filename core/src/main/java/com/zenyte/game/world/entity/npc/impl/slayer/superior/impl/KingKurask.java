package com.zenyte.game.world.entity.npc.impl.slayer.superior.impl;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KingKurask extends SuperiorNPC {
    public KingKurask(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7405, tile);
        this.deathDelay = 1;
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    private static final Item LEAF_BLADED_SPEAR = new Item(4158);
    private static final Item LEAF_BLADED_SWORD = new Item(11902);
    private static final Item LEAF_BLADED_BATTLEAXE = new Item(20727);
    private static final Item BROAD_ARROW = new Item(4160);
    private static final Item BROAD_BOLT = new Item(11875);
    private static final Item AMETHYST_BROAD_BOLT = new Item(21316);

    @Override
    public void handleIngoingHit(final Hit hit) {
        if (hit.getSource() instanceof Player) {
            final Player player = (Player) hit.getSource();
            final Item weapon = player.getEquipment().getItem(EquipmentSlot.WEAPON.getSlot()) == null ? new Item(-1)
                                                                                                      : player.getEquipment().getItem(EquipmentSlot.WEAPON.getSlot());
            final Item ammunition = player.getEquipment().getItem(EquipmentSlot.AMMUNITION.getSlot()) == null ? new Item(-1)
                                                                                                              : player.getEquipment().getItem(EquipmentSlot.AMMUNITION.getSlot());
            if ((hit.getHitType().equals(HitType.MELEE) && (weapon.getId() != LEAF_BLADED_SPEAR.getId()
                    && weapon.getId() != LEAF_BLADED_SWORD.getId() && weapon.getId() != LEAF_BLADED_BATTLEAXE.getId()))) {
                hit.setDamage(0);
            } else if (hit.getHitType().equals(HitType.RANGED) && (ammunition.getId() != BROAD_ARROW.getId()
                    && ammunition.getId() != BROAD_BOLT.getId() && ammunition.getId() != AMETHYST_BROAD_BOLT.getId())) {
                hit.setDamage(0);
            } else if (hit.getHitType().equals(HitType.MAGIC) && hit.getWeapon() != CombatSpell.MAGIC_DART) {
                hit.setDamage(0);
            }
        }
        super.handleIngoingHit(hit);
    }

    @Override
    public float getXpModifier(final Hit hit) {
        if (hit.getSource() instanceof Player) {
            final Player player = (Player) hit.getSource();
            final Item weapon = player.getEquipment().getItem(EquipmentSlot.WEAPON.getSlot()) == null ? new Item(-1)
                                                                                                      : player.getEquipment().getItem(EquipmentSlot.WEAPON.getSlot());
            final Item ammunition = player.getEquipment().getItem(EquipmentSlot.AMMUNITION.getSlot()) == null ? new Item(-1)
                                                                                                              : player.getEquipment().getItem(EquipmentSlot.AMMUNITION.getSlot());
            if ((hit.getHitType().equals(HitType.MELEE) && (weapon.getId() != LEAF_BLADED_SPEAR.getId()
                    && weapon.getId() != LEAF_BLADED_SWORD.getId() && weapon.getId() != LEAF_BLADED_BATTLEAXE.getId()))) {
                return 0;
            } else if (hit.getHitType().equals(HitType.RANGED) && (ammunition.getId() != BROAD_ARROW.getId()
                    && ammunition.getId() != BROAD_BOLT.getId() && ammunition.getId() != AMETHYST_BROAD_BOLT.getId())) {
                return 0;
            } else if (hit.getHitType().equals(HitType.MAGIC) && hit.getWeapon() != CombatSpell.MAGIC_DART) {
                return 0;
            }
        }
        return 1;
    }
}
