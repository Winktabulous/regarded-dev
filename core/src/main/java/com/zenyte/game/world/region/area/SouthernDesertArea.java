package com.zenyte.game.world.region.area;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.equipment.Equipment;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentUtils;
import com.zenyte.game.world.region.RSPolygon;
import com.zenyte.game.world.region.area.plugins.CycleProcessPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Kris | 16. mai 2018 : 20:40:16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class SouthernDesertArea extends DesertArea implements CycleProcessPlugin {
	private static final Logger log = LoggerFactory.getLogger(SouthernDesertArea.class);

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] {new RSPolygon(new int[][] {{3302, 3116}, {3302, 3113}, {3300, 3112}, {3295, 3112}, {3283, 3134}, {3252, 3135}, {3215, 3133}, {3201, 3130}, {3200, 3070}, {3159, 3062}, {3144, 3044}, {3136, 3005}, {3134, 2879}, {3199, 2810}, {3202, 2810}, {3203, 2811}, {3245, 2811}, {3246, 2810}, {3248, 2810}, {3249, 2811}, {3264, 2811}, {3265, 2812}, {3267, 2810}, {3274, 2810}, {3276, 2812}, {3277, 2811}, {3282, 2811}, {3283, 2810}, {3285, 2810}, {3286, 2811}, {3323, 2811}, {3324, 2810}, {3324, 2752}, {3328, 2752}, {3328, 2816}, {3392, 2816}, {3392, 2880}, {3392, 2944}, {3456, 2944}, {3456, 3072}, {3520, 3072}, {3512, 3115}, {3469, 3149}, {3419, 3174}, {3414, 3168}, {3411, 3161}, {3403, 3160}, {3398, 3163}, {3395, 3162}, {3395, 3155}, {3391, 3143}, {3381, 3124}, {3372, 3131}, {3367, 3131}, {3365, 3129}, {3352, 3124}, {3352, 3126}, {3353, 3128}, {3362, 3135}, {3362, 3138}, {3356, 3143}, {3350, 3148}, {3350, 3150}, {3353, 3153}, {3353, 3154}, {3352, 3156}, {3344, 3160}, {3336, 3159}, {3334, 3152}, {3333, 3150}, {3335, 3149}, {3332, 3142}, {3330, 3137}, {3326, 3136}, {3324, 3137}, {3318, 3137}, {3314, 3135}, {3310, 3116}, {3328, 3008}, {3328, 2944}, {3360, 2944}, {3364, 2950}, {3375, 2960}, {3380, 2960}, {3384, 2977}, {3381, 2988}, {3380, 2992}, {3370, 3008}, {3328, 3008}, {3310, 3116}, {3191, 3004}, {3136, 3004}, {3135, 2958}, {3152, 2958}, {3158, 2961}, {3161, 2961}, {3163, 2957}, {3171, 2956}, {3174, 2951}, {3191, 2950}, {3200, 2953}, {3200, 2975}, {3196, 2982}, {3196, 2996}, {3191, 3004}, {3302, 3116}, {3310, 3116}, {3191, 3004}})};
	}

	private final Set<Player> desertPlayers = new HashSet<>();

	@Override
	public void enter(final Player player) {
		final Object time = player.getTemporaryAttributes().getOrDefault("desertHeatCounter", 0);
		if (!(time instanceof Integer)) {
			return;
		}
		final int seconds = (int) time;
		if (seconds == 0) {
			player.getTemporaryAttributes().put("desertHeatCounter", (int) (getDuration(player) / 0.6F));
		}
		desertPlayers.add(player);
	}

	@Override
	public void leave(final Player player, boolean logout) {
		desertPlayers.remove(player);
	}

	@Override
	public String name() {
		return "Southern Desert";
	}

	private int ticks;

	@Override
	public void process() {
		if (desertPlayers.isEmpty()) {
			return;
		}
		if (++ticks % 50 == 0) {
			desertPlayers.removeIf(player -> player.isNulled() || player.isFinished());
		}
		loop:
		for (final Player player : desertPlayers) {
			try {
				if (player.isNulled()) {
					continue;
				}
				if (EquipmentUtils.containsDesertAmulet4(player) || !player.inArea(name())) {
					continue;
				}
				final Map<Object, Object> attributes = player.getTemporaryAttributes();
				final Object time = attributes.getOrDefault("desertHeatCounter", 0);
				if (!(time instanceof Integer)) {
					return;
				}
				final int ticks = (int) time;
				if (ticks == 0) {
					attributes.put("desertHeatCounter", (int) (getDuration(player) / 0.6F));
					final Container container = player.getInventory().getContainer();
					for (int slot = 0; slot < container.getContainerSize(); slot++) {
						final Item item = container.get(slot);
						if (item == null) {
							continue;
						}
						final int id = item.getId();
						if (id == 1829 || id == 1827 || id == 1825 || id == 1823 || id == 6794) {
							if (id == 6794) {
								container.remove(slot, 1);
								container.refresh(player);
								player.sendMessage("You eat a choc-ice to protect yourself against the desert heat.");
								player.heal(6);
							} else {
								container.set(slot, new Item(id + 2, 1));
								container.refresh(player);
								player.sendMessage("You drink from your waterskin to protect yourself from the desert heat.");
							}
							continue loop;
						}
					}
					player.applyHit(new Hit(Utils.random(1, 10), HitType.REGULAR));
					player.sendMessage("You take some damage from the desert heat.");
				} else {
					attributes.put("desertHeatCounter", ticks - 1);
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	private static final int getDuration(final Player player) {
		int time = 90;
		final Equipment equipment = player.getEquipment();
		if (equipment.getId(EquipmentSlot.BOOTS) == 1837) {
			time += 6;
		}
		final int top = equipment.getId(EquipmentSlot.PLATE);
		if (top == 1833 || top == 6384) {
			time += 12;
		} else if (top == 6388) {
			time += 6;
		}
		final int legs = equipment.getId(EquipmentSlot.LEGS);
		if (legs == 1835 || legs == 6386 || legs == 6390) {
			time += 12;
		}
		final int hat = equipment.getId(EquipmentSlot.HELMET);
		if (hat == 6392 || hat == 6400) {
			time += 12;
		}
		return time;
	}
}
