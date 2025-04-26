package com.zenyte.game.world.entity.player.action.combat;

import com.google.common.collect.ImmutableMap;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.player.SkillConstants;
import mgi.types.config.items.ItemDefinitions;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kris | 7. march 2018 : 4:30.50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum CombatSoundEffect {

    //Godsword 3847 normal, 3846 smash.
    //Godsword special: 3869
    GODSWORD(new SoundEffect(3846, 8), 11802, 11804, 11806, 11808, 20368, 20370, 20372, 20374),
    WANDS(new SoundEffect(2563, 8), 6908, 6910, 6912, 6914, 10150, 11012, 12422),
    GRANITE_MAUL(new SoundEffect(2714, 8), 4153),
    GUTHANS_SPEAR(new SoundEffect(1328, 8), 4726, 4910, 4911, 4912, 4913, 4914),
    VERACS_FLAIL(new SoundEffect(1323, 8), 4755, 4982, 4983, 4984, 4985, 4986),
    TORAGS_HAMMERS(new SoundEffect(1332, 8), 4747, 4958, 4959, 4960, 4961, 4962),
    DHAROKS_GREATAXE(new SoundEffect(1321, 8), 4718, 4886, 4887, 4888, 4889, 4890),
    TZHAAR_KET_OM(new SoundEffect(2520, 8), 6528, ItemId.TZHAARKETOM_T),
	FLAILS(new SoundEffect(1713, 8), ItemId.IVANDIS_FLAIL, ItemId.BLISTERWOOD_FLAIL),
	;

    private static final CombatSoundEffect[] VALUES = values();
    private static final Map<Integer, CombatSoundEffect> SOUND_EFFECTS = new HashMap<Integer, CombatSoundEffect>();
    private final SoundEffect sound;
    private final boolean special;
    private final int style;
    private final int[] weaponIds;
    private static final ImmutableMap<AttackStyleDefinition, SoundEffect> WEAPON_SOUNDS = ImmutableMap.<AttackStyleDefinition, SoundEffect>builder()
            .put(AttackStyleDefinition.SCIMITAR, new SoundEffect(2500, 8))
            .put(AttackStyleDefinition.UNARMED, new SoundEffect(2566, 8))
            .put(AttackStyleDefinition.CLAWS, new SoundEffect(2548, 8))
            .put(AttackStyleDefinition.CHINCHOMPA, new SoundEffect(2779, 8))
            .put(AttackStyleDefinition.TWO_HANDED, new SoundEffect(2503, 8))
            .put(AttackStyleDefinition.HALBERD, new SoundEffect(2524, 8))
            .put(AttackStyleDefinition.UNUSED_HALBERD, new SoundEffect(2524, 8))
            .put(AttackStyleDefinition.SPEAR, new SoundEffect(2556, 8))
            .put(AttackStyleDefinition.UNUSED_SPEAR, new SoundEffect(2556, 8))
            .put(AttackStyleDefinition.MACE, new SoundEffect(2508, 8))
            .put(AttackStyleDefinition.UNUSED_SCIMITAR, new SoundEffect(2500, 8))
            .put(AttackStyleDefinition.DAGGER, new SoundEffect(2517, 8))
            .put(AttackStyleDefinition.PICKAXE, new SoundEffect(2498, 8))
            .put(AttackStyleDefinition.STAFF, new SoundEffect(2555, 8))
            .put(AttackStyleDefinition.NON_AUTOCAST_STAFF, new SoundEffect(2555, 8))
            .put(AttackStyleDefinition.AXE, new SoundEffect(2498, 8))
            .put(AttackStyleDefinition.MAUL, new SoundEffect(2567, 8))
            .put(AttackStyleDefinition.UNUSED_MAUL, new SoundEffect(2567, 8))
            .put(AttackStyleDefinition.CROSSBOW, new SoundEffect(2695, 8))
            .put(AttackStyleDefinition.BOW, new SoundEffect(2693, 8))
            .put(AttackStyleDefinition.THROWN, new SoundEffect(2696, 8))
            .put(AttackStyleDefinition.WHIP, new SoundEffect(2720, 8))
            .build();

	static {
		for (CombatSoundEffect effect : VALUES) {
			for (int i : effect.weaponIds)
				SOUND_EFFECTS.put(i, effect);
		}
	}
	
	public static final CombatSoundEffect getSound(final int weaponId) {
        return SOUND_EFFECTS.get(weaponId);
	}
	
	public static final SoundEffect getDefaultSoundEffect(final int weaponId) {
		AttackStyleDefinition styleDefinition;
		final ItemDefinitions defs = ItemDefinitions.get(weaponId);
		final int varbit = defs == null ? 0 : defs.getInterfaceVarbit();
		if (varbit > 0 && varbit < 28)
			styleDefinition = AttackStyleDefinition.values[varbit];
		else
			styleDefinition = AttackStyleDefinition.UNARMED;
		return WEAPON_SOUNDS.get(styleDefinition);
	}
	
	CombatSoundEffect(final SoundEffect sound, final int... weaponIds) {
        this(sound, false, SkillConstants.ATTACK, weaponIds);
    }

    CombatSoundEffect(final SoundEffect sound, final boolean special, final int style, final int... weaponIds) {
        this.sound = sound;
        this.special = special;
        this.style = style;
        this.weaponIds = weaponIds;
    }

    public SoundEffect getSound() {
        return sound;
    }

    public boolean isSpecial() {
        return special;
    }

    public int getStyle() {
        return style;
    }

}
