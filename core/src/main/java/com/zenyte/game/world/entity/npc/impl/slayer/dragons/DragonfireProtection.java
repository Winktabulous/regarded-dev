package com.zenyte.game.world.entity.npc.impl.slayer.dragons;

import com.near_reality.game.content.custom.SlayerHelmetEffects;
import com.near_reality.game.item.CustomItemId;
import com.zenyte.game.content.boons.impl.BurnBabyBurn;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.variables.PlayerVariables;
import com.zenyte.game.world.entity.player.variables.TickVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 31/10/2018 13:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum DragonfireProtection {

    PROTECT_FROM_MAGIC("Protect from Magic prayer", 0.75F),
    ANTI_DRAGON_SHIELD("shield", 0.75F),
    ELEMENTAL_SHIELD("shield", 0.75F),
    DRAGONFIRE_SHIELD("shield", 0.75F),
    ANTIFIRE_POTION("antifire potion", 0.5F),
    SUPER_ANTIFIRE_POTION("super antifire potion", 1.0F),
    BLACK_SLAYER_HELMET("black slayer helmet", 1.0F)
    ;

    final String protectionName;
    final float protectionTier;
    static final DragonfireProtection[] wyvernProtection = new DragonfireProtection[] {ELEMENTAL_SHIELD, DRAGONFIRE_SHIELD, BLACK_SLAYER_HELMET};
    static final DragonfireProtection[] defaultProtection = new DragonfireProtection[] {PROTECT_FROM_MAGIC, ANTI_DRAGON_SHIELD, DRAGONFIRE_SHIELD, ANTIFIRE_POTION, SUPER_ANTIFIRE_POTION, BLACK_SLAYER_HELMET};

    public static List<DragonfireProtection> getProtection(NPC source, final Player target) {
        return getProtection(source, target, false);
    }

    /**
     * Gets all of the active protection types for the given player. Only appends the strongest of collisions.
     *
     * @param target the player whose active protections to obtain.
     * @return a list of active dragonfire protections.
     */
    public static List<DragonfireProtection> getProtection(NPC source, final Player target, final boolean leather) {
        final List<DragonfireProtection> list = new ArrayList<>(3);
        if (leather) {
            if (target.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC)) {
                list.add(PROTECT_FROM_MAGIC);
            }
        }
        final PlayerVariables variables = target.getVariables();
        if (variables.getTime(TickVariable.SUPER_ANTIFIRE) > 0) {
            list.add(SUPER_ANTIFIRE_POTION);
        } else if (variables.getTime(TickVariable.ANTIFIRE) > 0) {
            list.add(ANTIFIRE_POTION);
        }
        switch (target.getEquipment().getId(EquipmentSlot.SHIELD)) {
            case 2890, 9731 -> list.add(ELEMENTAL_SHIELD);
            case 1540, 8282, 11710 -> list.add(ANTI_DRAGON_SHIELD);
            case 11283, 11284, 22002, 22003, 21633, 21634, CustomItemId.DRAGON_KITE -> list.add(DRAGONFIRE_SHIELD);
        }
        if(target.getBoonManager().hasBoon(BurnBabyBurn.class) && !list.contains(DRAGONFIRE_SHIELD))
            list.add(DRAGONFIRE_SHIELD);
        if(SlayerHelmetEffects.INSTANCE.immuneToDragonfire(target, source))
            list.add(BLACK_SLAYER_HELMET);
        return list;
    }

    DragonfireProtection(String protectionName, float protectionTier) {
        this.protectionName = protectionName;
        this.protectionTier = protectionTier;
    }

    public String getProtectionName() {
        return protectionName;
    }
}
