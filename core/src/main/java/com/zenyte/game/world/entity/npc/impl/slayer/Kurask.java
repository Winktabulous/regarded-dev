package com.zenyte.game.world.entity.npc.impl.slayer;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

/**
 * @author Tommeh | 11 dec. 2017 : 22:57:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Kurask extends NPC implements Spawnable {

	private static final Item LEAF_BLADED_SPEAR = new Item(4158);
	private static final Item LEAF_BLADED_SWORD = new Item(11902);
	private static final Item LEAF_BLADED_BATTLEAXE = new Item(20727);
	private static final Item BROAD_ARROW = new Item(4160);
	private static final Item BROAD_BOLT = new Item(11875);
	private static final Item AMETHYST_BROAD_BOLT = new Item(21316);

	public Kurask(final int id, final Location tile, final Direction direction, final int radius) {
		super(id, tile, direction, radius);
	}

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

	@Override protected void onDeath(Entity source) {
		super.onDeath(source);
		if (source instanceof final Player player) {
			player.getCombatAchievements().complete(CAType.MASTER_OF_BROAD_WEAPONRY);
		}
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

	@Override
	public boolean validate(final int id, final String name) {
		return id == 410 || id == 411;
	}

}
