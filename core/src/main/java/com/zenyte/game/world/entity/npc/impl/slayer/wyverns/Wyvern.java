package com.zenyte.game.world.entity.npc.impl.slayer.wyverns;

import com.google.common.collect.ImmutableList;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.spelleffect.BindEffect;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

/**
 * @author Christopher
 * @since 2/28/2020
 */
public abstract class Wyvern extends NPC implements CombatScript, Spawnable {
    private static final Animation icyBreathAnim = new Animation(7657);
    private static final Graphics icyBreathGfx = new Graphics(501, 0, 184);
    private static final Graphics icyBreathHitGfx = new Graphics(502);

    public Wyvern(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    protected static final SoundEffect impactSound = new SoundEffect(170, 10, 0);

    protected void magicAttack(final Entity target) {
        attackSound();
        setAnimation(getIcyBreathAnim());
        setGraphics(getIcyBreathGfx());
        final int damage = magicAttackDamage(target);
        delayHit(this, 1, target, new Hit(this, damage, HitType.MAGIC).onLand(hit -> {
            if (!hasShield(target)) {
                if (target instanceof NPC) {
                    return;
                }
                final Player player = (Player) target;
                player.setGraphics(icyBreathHitGfx);
                drainSkill(player);
            }
            World.sendSoundEffect(new Location(target.getLocation()), impactSound);
            freeze(target);
        }));
    }

    protected Animation getIcyBreathAnim() {
        return icyBreathAnim;
    }

    protected Graphics getIcyBreathGfx() {
        return icyBreathGfx;
    }

    protected int magicAttackDamage(final Entity target) {
        return hasShield(target) ? Utils.random(1, 10) : Utils.random(10, 55);
    }

    private void drainSkill(final Player player) {
        for (int i = 0; i <= 6; i++) {
            if (i == 3) {
                continue;
            }
            player.drainSkill(i, 25.0);
        }
    }

    private void freeze(final Entity target) {
        if (target instanceof Player) {
            final Player player = (Player) target;
            final int shield = player.getEquipment().getId(EquipmentSlot.SHIELD);
            final boolean hasShield = shield == ItemId.ANCIENT_WYVERN_SHIELD || shield == ItemId.ANCIENT_WYVERN_SHIELD_21634;
            if (Utils.random(1) == 0 && !target.isFrozen()) {
                if (!hasShield) {
                    new BindEffect(8).spellEffect(null, target, 1);
                }
                target.applyHit(new Hit(this, hasShield ? Utils.random(1) : 1, HitType.MAGIC));
            }
        }
    }

    private boolean hasShield(final Entity target) {
        if (target instanceof NPC) {
            return false;
        }
        final Player player = (Player) target;
        final int shield = player.getEquipment().getId(EquipmentSlot.SHIELD.getSlot());
        return PROTECTIVE_SHIELDS.contains(shield);
    }

    @Override
    protected String notificationName(@NotNull final Player player) {
        return "fossil island wyvern";
    }

    private static final ImmutableList<Integer> PROTECTIVE_SHIELDS = ImmutableList.of(ItemId.ELEMENTAL_SHIELD, ItemId.MIND_SHIELD, ItemId.DRAGONFIRE_SHIELD, ItemId.DRAGONFIRE_SHIELD_11284, ItemId.ANCIENT_WYVERN_SHIELD, ItemId.ANCIENT_WYVERN_SHIELD_21634, ItemId.DRAGONFIRE_WARD, ItemId.DRAGONFIRE_WARD_22003);
}
