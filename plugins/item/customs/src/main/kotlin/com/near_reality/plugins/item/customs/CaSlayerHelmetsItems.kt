package com.near_reality.plugins.item.customs

import com.near_reality.scripts.item.definitions.ItemDefinitionsScript
import com.zenyte.game.item.ItemId.TZKAL_SLAYER_HELMET
import com.zenyte.game.item.ItemId.TZKAL_SLAYER_HELMET_I
import com.zenyte.game.item.ItemId.TZKAL_SLAYER_HELMET_I_25914
import com.zenyte.game.item.ItemId.TZTOK_SLAYER_HELMET
import com.zenyte.game.item.ItemId.TZTOK_SLAYER_HELMET_I
import com.zenyte.game.item.ItemId.TZTOK_SLAYER_HELMET_I_25902
import com.zenyte.game.item.ItemId.VAMPYRIC_SLAYER_HELMET
import com.zenyte.game.item.ItemId.VAMPYRIC_SLAYER_HELMET_I
import com.zenyte.game.item.ItemId.VAMPYRIC_SLAYER_HELMET_I_25908
import com.zenyte.game.world.entity.player.Bonuses.Bonus.ATT_MAGIC
import com.zenyte.game.world.entity.player.Bonuses.Bonus.ATT_RANGED
import com.zenyte.game.world.entity.player.Bonuses.Bonus.DEF_CRUSH
import com.zenyte.game.world.entity.player.Bonuses.Bonus.DEF_MAGIC
import com.zenyte.game.world.entity.player.Bonuses.Bonus.DEF_RANGE
import com.zenyte.game.world.entity.player.Bonuses.Bonus.DEF_SLASH
import com.zenyte.game.world.entity.player.Bonuses.Bonus.DEF_STAB
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.FULL_MASK
import mgi.types.config.items.JSONItemDefinitions

class CaSlayerHelmetsItems : ItemDefinitionsScript() {
    init {
        TZTOK_SLAYER_HELMET.slayerHelmet()
        VAMPYRIC_SLAYER_HELMET.slayerHelmet()
        TZKAL_SLAYER_HELMET.slayerHelmet()

        TZTOK_SLAYER_HELMET_I.slayerHelmetImbued()
        TZTOK_SLAYER_HELMET_I_25902.slayerHelmetImbued()
        VAMPYRIC_SLAYER_HELMET_I.slayerHelmetImbued()
        VAMPYRIC_SLAYER_HELMET_I_25908.slayerHelmetImbued()
        TZKAL_SLAYER_HELMET_I.slayerHelmetImbued()
        TZKAL_SLAYER_HELMET_I_25914.slayerHelmetImbued()
    }

    fun Int.slayerHelmet(build: JSONItemDefinitions.() -> Unit = {}) = invoke {
        this.weight = 2.2F
        this.slot = EquipmentSlot.HELMET.slot
        this.equipmentType = FULL_MASK
        equipment {
            slot = EquipmentSlot.HELMET.slot
            bonuses {
                ATT_MAGIC(-6)
                ATT_RANGED(-2)

                DEF_STAB(30)
                DEF_SLASH(32)
                DEF_CRUSH(27)
                DEF_MAGIC(-1)
                DEF_RANGE(30)
            }
        }
        build()
    }

    fun Int.slayerHelmetImbued(build: JSONItemDefinitions.() -> Unit = {}) = invoke {
        this.weight = 2.2F
        this.slot = EquipmentSlot.HELMET.slot
        this.equipmentType = FULL_MASK
        equipment {
            slot = EquipmentSlot.HELMET.slot
            bonuses {
                ATT_MAGIC(3)
                ATT_RANGED(3)

                DEF_STAB(30)
                DEF_SLASH(32)
                DEF_CRUSH(27)
                DEF_MAGIC(10)
                DEF_RANGE(30)
            }
        }
        build()
    }
}
