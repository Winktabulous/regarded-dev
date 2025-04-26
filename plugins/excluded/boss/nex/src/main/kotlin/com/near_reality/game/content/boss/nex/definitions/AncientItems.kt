package com.near_reality.game.content.boss.nex.definitions

import com.near_reality.scripts.item.definitions.ItemDefinitionsScript
import com.zenyte.game.item.ItemId.ANCIENT_BLESSING
import com.zenyte.game.item.ItemId.ANCIENT_CLOAK
import com.zenyte.game.item.ItemId.ANCIENT_CROZIER
import com.zenyte.game.item.ItemId.ANCIENT_FULL_HELM
import com.zenyte.game.item.ItemId.ANCIENT_GODSWORD
import com.zenyte.game.item.ItemId.ANCIENT_KITESHIELD
import com.zenyte.game.item.ItemId.ANCIENT_MITRE
import com.zenyte.game.item.ItemId.ANCIENT_PLATEBODY
import com.zenyte.game.item.ItemId.ANCIENT_PLATELEGS
import com.zenyte.game.item.ItemId.ANCIENT_PLATESKIRT
import com.zenyte.game.item.ItemId.ANCIENT_ROBE_LEGS
import com.zenyte.game.item.ItemId.ANCIENT_ROBE_TOP
import com.zenyte.game.item.ItemId.ANCIENT_STOLE
import com.zenyte.game.item.ItemId.ARMADYL_CROSSBOW
import com.zenyte.game.item.ItemId.BANDOS_GODSWORD
import com.zenyte.game.item.ItemId.TORVA_FULLHELM
import com.zenyte.game.item.ItemId.TORVA_FULLHELM_DAMAGED
import com.zenyte.game.item.ItemId.TORVA_PLATEBODY
import com.zenyte.game.item.ItemId.TORVA_PLATEBODY_DAMAGED
import com.zenyte.game.item.ItemId.TORVA_PLATELEGS
import com.zenyte.game.item.ItemId.TORVA_PLATELEGS_DAMAGED
import com.zenyte.game.item.ItemId.ZAMORAK_CROZIER
import com.zenyte.game.item.ItemId.ZARYTE_CROSSBOW
import com.zenyte.game.world.entity.player.SkillConstants.ATTACK
import com.zenyte.game.world.entity.player.SkillConstants.DEFENCE
import com.zenyte.game.world.entity.player.SkillConstants.PRAYER
import com.zenyte.game.world.entity.player.SkillConstants.RANGED
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot.AMMUNITION
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot.AMULET
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot.CAPE
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot.HELMET
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot.PLATE
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot.SHIELD
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.DEFAULT
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.FULL_BODY
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.FULL_HELM
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.FULL_MASK

class AncientItems : ItemDefinitionsScript() {

    init {
        ANCIENT_GODSWORD(BANDOS_GODSWORD) {
            weight = 11F
            equipment {
                requirements { ATTACK(75) }
                bonuses(0, 132, 80, 0, 0, 0, 0, 0, 0, 0, 132, 0, 0, 8)
                weapon { attackSpeed = 4 }
            }
        }
        ANCIENT_FULL_HELM.defence(HELMET, FULL_MASK, 40, 2.7F, 0, 0, 0, -6, -2, 30, 32, 27, -1, 30, 0, 0, 0, 0)
        ANCIENT_PLATEBODY.defence(PLATE, FULL_BODY, 40, 9.9F, 0, 0, -6, -2, 30, 32, 27, -1, 30, 0, 0, 0, 0)
        ANCIENT_PLATELEGS.defence(EquipmentSlot.LEGS, DEFAULT, 40, 9.0F, 0, 0, 0, -21, -7, 51, 49, 47, -4, 49, 0, 0, 0, 0)
        ANCIENT_PLATESKIRT.defence(EquipmentSlot.LEGS, DEFAULT, 40, 8.0F, 0, 0, 0, -21, -7, 51, 49, 47, -4, 49, 0, 0, 0, 0)
        ANCIENT_KITESHIELD.defence(SHIELD, DEFAULT, 40, 5.4F, 0, 0, 0, -8, -2, 44, 48, 46, -1, 46, 0, 0, 0, 0)

        ANCIENT_BLESSING {
            slot = AMMUNITION.slot
        }

        ANCIENT_MITRE.prayer(HELMET, FULL_HELM, 40, 0.0F, 0, 0, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 5)
        ANCIENT_STOLE.prayer(AMULET, DEFAULT, 60, 0.0F, 0, 0, 0, 2, 0, 0, 0, 0, 2, 0, 0, 0, 0, 10)
        ANCIENT_CLOAK.prayer(CAPE, DEFAULT, 40, 1.0F, 0, 0, 0, 1, 0, 3, 3, 3, 3, 3, 0, 0, 0, 3)
        ANCIENT_ROBE_LEGS.prayer(EquipmentSlot.LEGS, DEFAULT, 20, 2.0F, 0, 0, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 5)
        ANCIENT_ROBE_TOP.prayer(PLATE, FULL_BODY, 20, 2.0F, 0, 0, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 6)

        ANCIENT_CROZIER(ZAMORAK_CROZIER){
            weight = 2F
            equipment {
                requirements { PRAYER(60) }
                bonuses(7, -1, 25, 10, 0, 2, 3, 1, 10, 0, 32, 0, 0, 6)
                weapon { attackSpeed = 5 }
            }
        }

        ZARYTE_CROSSBOW(ARMADYL_CROSSBOW) {
            weight = 6F
            equipment {
                requirements { RANGED(80) }
                bonuses(0, 0, 0, 0, 110, 14, 14, 12, 15, 16, 0, 0, 0, 1)
                weapon {
                    attackSpeed = 4
                    blockAnimation = 424
                    standAnimation = 4591
                    walkAnimation = 4226
                    runAnimation = 4228
                    standTurnAnimation = 823
                    rotate90Animation = 821
                    rotate180Animation = 4227
                    rotate270Animation = 822
                    accurateAnimation = 9166
                    aggressiveAnimation = 9166
                    controlledAnimation = 9166
                    defensiveAnimation = 9166
                    interfaceVarbit = 5
                    normalAttackDistance = 7
                    longAttackDistance = 9
                }
            }
        }

        TORVA_FULLHELM.defence(HELMET, FULL_MASK, 80, 2.7F, 0, 0, 0, -5, -5, 59, 60, 62, -2, 57, 8, 0, 0, 1)
        TORVA_FULLHELM_DAMAGED.defence(HELMET, FULL_MASK, 0, 2.7F)

        TORVA_PLATELEGS.defence(EquipmentSlot.LEGS, DEFAULT, 80, 9F, 0, 0, 0, -24, -11, 87, 78, 79, -9, 102, 4, 0, 0, 1)
        TORVA_PLATELEGS_DAMAGED.defence(EquipmentSlot.LEGS, DEFAULT, 0, 9F)

        TORVA_PLATEBODY.defence(PLATE, FULL_BODY, 80, 9.9F, 0, 0, 0, -18, -14, 117, 111, 117, -11, 142, 6, 0, 0, 1)
        TORVA_PLATEBODY_DAMAGED.defence(PLATE, FULL_BODY, 0, 9.9F)
    }

    fun Int.defence(
        slot: EquipmentSlot,
        type: EquipmentType = DEFAULT,
        defenceLevel: Int = 40,
        weight: Float = 0.0F,
        vararg bonuses: Int,
    ) = invoke {
        this.weight = weight
        this.slot = slot.slot
        equipment(type) {
            if (defenceLevel > 1)
                requirements { DEFENCE(defenceLevel) }
            if (bonuses.isNotEmpty())
                bonuses(*bonuses)
        }
    }

    fun Int.prayer(
        slot: EquipmentSlot,
        type: EquipmentType = DEFAULT,
        prayerLevel: Int,
        weight: Float = 0.0F,
        vararg bonuses: Int,
    )= invoke {
        this.weight = weight
        this.slot = slot.slot
        equipment(type) {
            if (prayerLevel > 1)
                requirements { PRAYER(prayerLevel) }
            if (bonuses.isNotEmpty())
                bonuses(*bonuses)
        }
    }

}