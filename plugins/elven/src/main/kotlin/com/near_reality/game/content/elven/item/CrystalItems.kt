package com.near_reality.game.content.elven.item

import com.near_reality.scripts.item.definitions.ItemDefinitionsScript
import com.near_reality.scripts.item.definitions.WearableDefinitionBonusesBuilder
import com.zenyte.game.item.ItemId.BLADE_OF_SAELDOR
import com.zenyte.game.item.ItemId.BLADE_OF_SAELDOR_C
import com.zenyte.game.item.ItemId.BLADE_OF_SAELDOR_C_25870
import com.zenyte.game.item.ItemId.BLADE_OF_SAELDOR_C_25872
import com.zenyte.game.item.ItemId.BLADE_OF_SAELDOR_C_25874
import com.zenyte.game.item.ItemId.BLADE_OF_SAELDOR_C_25876
import com.zenyte.game.item.ItemId.BLADE_OF_SAELDOR_C_25878
import com.zenyte.game.item.ItemId.BLADE_OF_SAELDOR_C_25880
import com.zenyte.game.item.ItemId.BLADE_OF_SAELDOR_C_25882
import com.zenyte.game.item.ItemId.BLADE_OF_SAELDOR_INACTIVE
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25884
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25886
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25888
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25890
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25892
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25894
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_C_25896
import com.zenyte.game.item.ItemId.BOW_OF_FAERDHINEN_INACTIVE
import com.zenyte.game.item.ItemId.CORRUPTED_AXE
import com.zenyte.game.item.ItemId.CORRUPTED_BODY_ATTUNED
import com.zenyte.game.item.ItemId.CORRUPTED_BODY_BASIC
import com.zenyte.game.item.ItemId.CORRUPTED_BODY_PERFECTED
import com.zenyte.game.item.ItemId.CORRUPTED_BOW_ATTUNED
import com.zenyte.game.item.ItemId.CORRUPTED_BOW_BASIC
import com.zenyte.game.item.ItemId.CORRUPTED_BOW_PERFECTED
import com.zenyte.game.item.ItemId.CORRUPTED_HALBERD_ATTUNED
import com.zenyte.game.item.ItemId.CORRUPTED_HALBERD_BASIC
import com.zenyte.game.item.ItemId.CORRUPTED_HALBERD_PERFECTED
import com.zenyte.game.item.ItemId.CORRUPTED_HARPOON
import com.zenyte.game.item.ItemId.CORRUPTED_HELM_ATTUNED
import com.zenyte.game.item.ItemId.CORRUPTED_HELM_BASIC
import com.zenyte.game.item.ItemId.CORRUPTED_HELM_PERFECTED
import com.zenyte.game.item.ItemId.CORRUPTED_LEGS_ATTUNED
import com.zenyte.game.item.ItemId.CORRUPTED_LEGS_BASIC
import com.zenyte.game.item.ItemId.CORRUPTED_LEGS_PERFECTED
import com.zenyte.game.item.ItemId.CORRUPTED_PICKAXE
import com.zenyte.game.item.ItemId.CORRUPTED_SCEPTRE
import com.zenyte.game.item.ItemId.CORRUPTED_STAFF_ATTUNED
import com.zenyte.game.item.ItemId.CORRUPTED_STAFF_BASIC
import com.zenyte.game.item.ItemId.CORRUPTED_STAFF_PERFECTED
import com.zenyte.game.item.ItemId.CRYSTAL_AXE
import com.zenyte.game.item.ItemId.CRYSTAL_AXE_23862
import com.zenyte.game.item.ItemId.CRYSTAL_AXE_INACTIVE
import com.zenyte.game.item.ItemId.CRYSTAL_BODY
import com.zenyte.game.item.ItemId.CRYSTAL_BODY_ATTUNED
import com.zenyte.game.item.ItemId.CRYSTAL_BODY_BASIC
import com.zenyte.game.item.ItemId.CRYSTAL_BODY_INACTIVE
import com.zenyte.game.item.ItemId.CRYSTAL_BODY_PERFECTED
import com.zenyte.game.item.ItemId.CRYSTAL_BOW
import com.zenyte.game.item.ItemId.CRYSTAL_BOW_ATTUNED
import com.zenyte.game.item.ItemId.CRYSTAL_BOW_BASIC
import com.zenyte.game.item.ItemId.CRYSTAL_BOW_INACTIVE
import com.zenyte.game.item.ItemId.CRYSTAL_BOW_PERFECTED
import com.zenyte.game.item.ItemId.CRYSTAL_HALBERD
import com.zenyte.game.item.ItemId.CRYSTAL_HALBERD_ATTUNED
import com.zenyte.game.item.ItemId.CRYSTAL_HALBERD_BASIC
import com.zenyte.game.item.ItemId.CRYSTAL_HALBERD_INACTIVE
import com.zenyte.game.item.ItemId.CRYSTAL_HALBERD_PERFECTED
import com.zenyte.game.item.ItemId.CRYSTAL_HARPOON
import com.zenyte.game.item.ItemId.CRYSTAL_HARPOON_23864
import com.zenyte.game.item.ItemId.CRYSTAL_HARPOON_INACTIVE
import com.zenyte.game.item.ItemId.CRYSTAL_HELM
import com.zenyte.game.item.ItemId.CRYSTAL_HELM_ATTUNED
import com.zenyte.game.item.ItemId.CRYSTAL_HELM_BASIC
import com.zenyte.game.item.ItemId.CRYSTAL_HELM_INACTIVE
import com.zenyte.game.item.ItemId.CRYSTAL_HELM_PERFECTED
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS_ATTUNED
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS_BASIC
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS_INACTIVE
import com.zenyte.game.item.ItemId.CRYSTAL_LEGS_PERFECTED
import com.zenyte.game.item.ItemId.CRYSTAL_PICKAXE
import com.zenyte.game.item.ItemId.CRYSTAL_PICKAXE_23863
import com.zenyte.game.item.ItemId.CRYSTAL_PICKAXE_INACTIVE
import com.zenyte.game.item.ItemId.CRYSTAL_SCEPTRE
import com.zenyte.game.item.ItemId.CRYSTAL_SHIELD
import com.zenyte.game.item.ItemId.CRYSTAL_SHIELD_INACTIVE
import com.zenyte.game.item.ItemId.CRYSTAL_STAFF_ATTUNED
import com.zenyte.game.item.ItemId.CRYSTAL_STAFF_BASIC
import com.zenyte.game.item.ItemId.CRYSTAL_STAFF_PERFECTED
import com.zenyte.game.item.ItemId.ENHANCED_CRYSTAL_KEY
import com.zenyte.game.item.ItemId.ETERNAL_TELEPORT_CRYSTAL
import com.zenyte.game.world.entity.player.Bonuses.Bonus.ATT_CRUSH
import com.zenyte.game.world.entity.player.Bonuses.Bonus.ATT_MAGIC
import com.zenyte.game.world.entity.player.Bonuses.Bonus.ATT_RANGED
import com.zenyte.game.world.entity.player.Bonuses.Bonus.ATT_SLASH
import com.zenyte.game.world.entity.player.Bonuses.Bonus.ATT_STAB
import com.zenyte.game.world.entity.player.Bonuses.Bonus.DEF_CRUSH
import com.zenyte.game.world.entity.player.Bonuses.Bonus.DEF_MAGIC
import com.zenyte.game.world.entity.player.Bonuses.Bonus.DEF_RANGE
import com.zenyte.game.world.entity.player.Bonuses.Bonus.DEF_SLASH
import com.zenyte.game.world.entity.player.Bonuses.Bonus.DEF_STAB
import com.zenyte.game.world.entity.player.Bonuses.Bonus.PRAYER
import com.zenyte.game.world.entity.player.Bonuses.Bonus.RANGE_STRENGTH
import com.zenyte.game.world.entity.player.Bonuses.Bonus.STRENGTH
import com.zenyte.game.world.entity.player.SkillConstants.AGILITY
import com.zenyte.game.world.entity.player.SkillConstants.ATTACK
import com.zenyte.game.world.entity.player.SkillConstants.DEFENCE
import com.zenyte.game.world.entity.player.SkillConstants.RANGED
import com.zenyte.game.world.entity.player.SkillConstants.STRENGTH
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.DEFAULT
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.FULL_BODY
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.FULL_LEGS
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.FULL_MASK
import mgi.types.config.items.WearableDefinition

/*
 * Contains item definition for crystal armour
 *
 * TODO: correct animations
 */
class CrystalItems : ItemDefinitionsScript() {
    init {
        CRYSTAL_HELM.helm {
            ATT_MAGIC(-10)
            ATT_RANGED(9)

            DEF_STAB(12)
            DEF_SLASH(8)
            DEF_CRUSH(14)
            DEF_MAGIC(10)
            DEF_RANGE(18)

            PRAYER(2)
        }
        CRYSTAL_HELM_INACTIVE.helm()
        CRYSTAL_HELM_BASIC.helmGauntlet {
            ATT_STAB(2)
            ATT_SLASH(2)
            ATT_CRUSH(1)
            ATT_MAGIC(2)
            ATT_RANGED(2)

            DEF_STAB(28)
            DEF_SLASH(28)
            DEF_CRUSH(28)
            DEF_MAGIC(28)
            DEF_RANGE(28)

            PRAYER(1)
        }
        CRYSTAL_HELM_ATTUNED.helmGauntlet {
            ATT_STAB(6)
            ATT_SLASH(6)
            ATT_CRUSH(2)
            ATT_MAGIC(6)
            ATT_RANGED(6)

            DEF_STAB(48)
            DEF_SLASH(48)
            DEF_CRUSH(48)
            DEF_MAGIC(48)
            DEF_RANGE(48)

            PRAYER(2)
        }
        CRYSTAL_HELM_PERFECTED.helmGauntlet {
            ATT_STAB(10)
            ATT_SLASH(10)
            ATT_CRUSH(3)
            ATT_MAGIC(10)
            ATT_RANGED(10)

            DEF_STAB(68)
            DEF_SLASH(68)
            DEF_CRUSH(68)
            DEF_MAGIC(68)
            DEF_RANGE(68)

            PRAYER(3)
        }

        CORRUPTED_HELM_BASIC.helmGauntlet {
            ATT_STAB(2)
            ATT_SLASH(2)
            ATT_CRUSH(1)
            ATT_MAGIC(2)
            ATT_RANGED(2)

            DEF_STAB(28)
            DEF_SLASH(28)
            DEF_CRUSH(28)
            DEF_MAGIC(28)
            DEF_RANGE(28)

            PRAYER(1)
        }
        CORRUPTED_HELM_ATTUNED.helmGauntlet {
            ATT_STAB(6)
            ATT_SLASH(6)
            ATT_CRUSH(2)
            ATT_MAGIC(6)
            ATT_RANGED(6)

            DEF_STAB(48)
            DEF_SLASH(48)
            DEF_CRUSH(48)
            DEF_MAGIC(48)
            DEF_RANGE(48)

            PRAYER(2)
        }
        CORRUPTED_HELM_PERFECTED.helmGauntlet {
            ATT_STAB(10)
            ATT_SLASH(10)
            ATT_CRUSH(3)
            ATT_MAGIC(10)
            ATT_RANGED(10)

            DEF_STAB(68)
            DEF_SLASH(68)
            DEF_CRUSH(68)
            DEF_MAGIC(68)
            DEF_RANGE(68)

            PRAYER(3)
        }

        CRYSTAL_LEGS_INACTIVE.legs()
        CRYSTAL_LEGS.legs {
            ATT_MAGIC(-12)
            ATT_RANGED(18)

            DEF_STAB(26)
            DEF_SLASH(21)
            DEF_CRUSH(30)
            DEF_MAGIC(34)
            DEF_RANGE(38)

            PRAYER(2)
        }
        CRYSTAL_LEGS_BASIC.legsGauntlet {
            ATT_STAB(6)
            ATT_SLASH(6)
            ATT_CRUSH(2)
            ATT_MAGIC(6)
            ATT_RANGED(6)

            DEF_STAB(52)
            DEF_SLASH(52)
            DEF_CRUSH(52)
            DEF_MAGIC(52)
            DEF_RANGE(52)

            PRAYER(2)
        }
        CRYSTAL_LEGS_ATTUNED.legsGauntlet {
            ATT_STAB(10)
            ATT_SLASH(10)
            ATT_CRUSH(3)
            ATT_MAGIC(10)
            ATT_RANGED(10)

            DEF_STAB(74)
            DEF_SLASH(74)
            DEF_CRUSH(74)
            DEF_MAGIC(74)
            DEF_RANGE(74)

            PRAYER(3)
        }
        CRYSTAL_LEGS_PERFECTED.legsGauntlet {
            ATT_STAB(14)
            ATT_SLASH(14)
            ATT_CRUSH(4)
            ATT_MAGIC(14)
            ATT_RANGED(14)

            DEF_STAB(92)
            DEF_SLASH(92)
            DEF_CRUSH(92)
            DEF_MAGIC(92)
            DEF_RANGE(92)

            PRAYER(4)
        }

        CORRUPTED_LEGS_BASIC.legsGauntlet {
            ATT_STAB(6)
            ATT_SLASH(6)
            ATT_CRUSH(2)
            ATT_MAGIC(6)
            ATT_RANGED(6)

            DEF_STAB(52)
            DEF_SLASH(52)
            DEF_CRUSH(52)
            DEF_MAGIC(52)
            DEF_RANGE(52)

            PRAYER(2)
        }
        CORRUPTED_LEGS_ATTUNED.legsGauntlet {
            ATT_STAB(10)
            ATT_SLASH(10)
            ATT_CRUSH(3)
            ATT_MAGIC(10)
            ATT_RANGED(10)

            DEF_STAB(74)
            DEF_SLASH(74)
            DEF_CRUSH(74)
            DEF_MAGIC(74)
            DEF_RANGE(74)

            PRAYER(3)
        }
        CORRUPTED_LEGS_PERFECTED.legsGauntlet {
            ATT_STAB(14)
            ATT_SLASH(14)
            ATT_CRUSH(4)
            ATT_MAGIC(14)
            ATT_RANGED(14)

            DEF_STAB(92)
            DEF_SLASH(92)
            DEF_CRUSH(92)
            DEF_MAGIC(92)
            DEF_RANGE(92)

            PRAYER(4)
        }

        CRYSTAL_BODY_INACTIVE.body()
        CRYSTAL_BODY.body {
            ATT_MAGIC(-18)
            ATT_RANGED(31)

            DEF_STAB(46)
            DEF_SLASH(38)
            DEF_CRUSH(48)
            DEF_MAGIC(44)
            DEF_RANGE(68)

            PRAYER(3)
        }
        CRYSTAL_BODY_BASIC.bodyGauntlet {
            ATT_STAB(8)
            ATT_SLASH(8)
            ATT_CRUSH(3)
            ATT_MAGIC(8)
            ATT_RANGED(8)

            DEF_STAB(86)
            DEF_SLASH(86)
            DEF_CRUSH(86)
            DEF_MAGIC(86)
            DEF_RANGE(86)

            PRAYER(3)
        }
        CRYSTAL_BODY_ATTUNED.bodyGauntlet {
            ATT_STAB(12)
            ATT_SLASH(12)
            ATT_CRUSH(4)
            ATT_MAGIC(12)
            ATT_RANGED(12)

            DEF_STAB(102)
            DEF_SLASH(102)
            DEF_CRUSH(102)
            DEF_MAGIC(102)
            DEF_RANGE(102)

            PRAYER(4)
        }
        CRYSTAL_BODY_PERFECTED.bodyGauntlet {
            ATT_STAB(15)
            ATT_SLASH(16)
            ATT_CRUSH(5)
            ATT_MAGIC(16)
            ATT_RANGED(16)

            DEF_STAB(124)
            DEF_SLASH(124)
            DEF_CRUSH(124)
            DEF_MAGIC(124)
            DEF_RANGE(124)

            PRAYER(5)
        }

        CORRUPTED_BODY_BASIC.bodyGauntlet {
            ATT_STAB(8)
            ATT_SLASH(8)
            ATT_CRUSH(3)
            ATT_MAGIC(8)
            ATT_RANGED(8)

            DEF_STAB(86)
            DEF_SLASH(86)
            DEF_CRUSH(86)
            DEF_MAGIC(86)
            DEF_RANGE(86)

            PRAYER(3)
        }
        CORRUPTED_BODY_ATTUNED.bodyGauntlet {
            ATT_STAB(12)
            ATT_SLASH(12)
            ATT_CRUSH(4)
            ATT_MAGIC(12)
            ATT_RANGED(12)

            DEF_STAB(102)
            DEF_SLASH(102)
            DEF_CRUSH(102)
            DEF_MAGIC(102)
            DEF_RANGE(102)

            PRAYER(4)
        }
        CORRUPTED_BODY_PERFECTED.bodyGauntlet {
            ATT_STAB(15)
            ATT_SLASH(16)
            ATT_CRUSH(5)
            ATT_MAGIC(16)
            ATT_RANGED(16)

            DEF_STAB(124)
            DEF_SLASH(124)
            DEF_CRUSH(124)
            DEF_MAGIC(124)
            DEF_RANGE(124)

            PRAYER(5)
        }

        CRYSTAL_AXE.axe()
        CRYSTAL_AXE_INACTIVE.axe()
        CRYSTAL_AXE_23862.axe(gauntlet = true)
        CORRUPTED_AXE.axe(gauntlet = true)

        CRYSTAL_HARPOON.harpoon()
        CRYSTAL_HARPOON_INACTIVE.harpoon()
        CRYSTAL_HARPOON_23864.harpoon(gauntlet = true)
        CORRUPTED_HARPOON.harpoon(gauntlet = true)

        CRYSTAL_PICKAXE.pickAxe()
        CRYSTAL_PICKAXE_INACTIVE.pickAxe()
        CRYSTAL_PICKAXE_23863.pickAxe(gauntlet = true)
        CORRUPTED_PICKAXE.pickAxe(gauntlet = true)

        CRYSTAL_SCEPTRE.weapon(weight = 0.003F) {
            weapon {
                attackSpeed = 5
                blockAnimation = 424
                accurateAnimation = 401
                aggressiveAnimation = 395
                controlledAnimation = 395
                defensiveAnimation = 395
                interfaceVarbit = 2
            }
            bonuses {
                ATT_STAB(8)
                ATT_SLASH(10)
                ATT_CRUSH(16)

                STRENGTH(20)
            }
        }

        CORRUPTED_SCEPTRE.weapon(weight = 0.003F) {
            weapon {
                attackSpeed = 5
                blockAnimation = 424
                accurateAnimation = 401
                aggressiveAnimation = 395
                controlledAnimation = 395
                defensiveAnimation = 395
                interfaceVarbit = 2
            }
            bonuses {
                ATT_STAB(8)
                ATT_SLASH(10)
                ATT_CRUSH(16)

                STRENGTH(20)
            }
        }

        CRYSTAL_BOW.bow()
        CRYSTAL_BOW_INACTIVE.bow()

        CRYSTAL_BOW_BASIC.bowGauntlet {
            ATT_RANGED(72)
            RANGE_STRENGTH(42)
            PRAYER(1)
        }

        CRYSTAL_BOW_ATTUNED.bowGauntlet {
            ATT_RANGED(118)
            RANGE_STRENGTH(88)
            PRAYER(2)
        }

        CRYSTAL_BOW_PERFECTED.bowGauntlet {
            ATT_RANGED(172)
            RANGE_STRENGTH(138)
            PRAYER(3)
        }

        CORRUPTED_BOW_BASIC.bowGauntlet {
            ATT_RANGED(72)
            RANGE_STRENGTH(42)
            PRAYER(1)
        }

        CORRUPTED_BOW_ATTUNED.bowGauntlet {
            ATT_RANGED(118)
            RANGE_STRENGTH(88)
            PRAYER(2)
        }

        CORRUPTED_BOW_PERFECTED.bowGauntlet {
            ATT_RANGED(172)
            RANGE_STRENGTH(138)
            PRAYER(3)
        }

        CRYSTAL_HALBERD.halberd()
        CRYSTAL_HALBERD_INACTIVE.halberd()

        CRYSTAL_HALBERD_BASIC.halberdGauntlet {
            ATT_STAB(68)
            ATT_SLASH(68)
            ATT_CRUSH(4)
            STRENGTH(42)
            PRAYER(1)
        }

        CRYSTAL_HALBERD_ATTUNED.halberdGauntlet {
            ATT_STAB(114)
            ATT_SLASH(114)
            ATT_CRUSH(12)
            STRENGTH(88)
            PRAYER(2)
        }

        CRYSTAL_HALBERD_PERFECTED.halberdGauntlet {
            ATT_STAB(166)
            ATT_SLASH(166)
            ATT_CRUSH(28)
            STRENGTH(138)
            PRAYER(3)
        }

        CORRUPTED_HALBERD_BASIC.halberdGauntlet {
            ATT_STAB(68)
            ATT_SLASH(68)
            ATT_CRUSH(4)
            STRENGTH(42)
            PRAYER(1)
        }

        CORRUPTED_HALBERD_ATTUNED.halberdGauntlet {
            ATT_STAB(114)
            ATT_SLASH(114)
            ATT_CRUSH(12)
            STRENGTH(88)
            PRAYER(2)
        }

        CORRUPTED_HALBERD_PERFECTED.halberdGauntlet {
            ATT_STAB(166)
            ATT_SLASH(166)
            ATT_CRUSH(28)
            STRENGTH(138)
            PRAYER(3)
        }

        CRYSTAL_STAFF_BASIC.staff {
            ATT_MAGIC(84)
            PRAYER(1)
        }

        CRYSTAL_STAFF_ATTUNED.staff {
            ATT_MAGIC(128)
            PRAYER(2)
        }

        CRYSTAL_STAFF_PERFECTED.staff {
            ATT_MAGIC(184)
            PRAYER(3)
        }

        CORRUPTED_STAFF_BASIC.staff {
            ATT_MAGIC(84)
            PRAYER(1)
        }

        CORRUPTED_STAFF_ATTUNED.staff {
            ATT_MAGIC(128)
            PRAYER(2)
        }

        CORRUPTED_STAFF_PERFECTED.staff {
            ATT_MAGIC(184)
            PRAYER(3)
        }

        CRYSTAL_SHIELD.shield()
        CRYSTAL_SHIELD_INACTIVE.shield()

        BLADE_OF_SAELDOR.bladeOfSaeldor()
        BLADE_OF_SAELDOR_INACTIVE.bladeOfSaeldor()
        BLADE_OF_SAELDOR_C.bladeOfSaeldor(tradable = false)
        BLADE_OF_SAELDOR_C_25870.bladeOfSaeldor(tradable = false)
        BLADE_OF_SAELDOR_C_25872.bladeOfSaeldor(tradable = false)
        BLADE_OF_SAELDOR_C_25874.bladeOfSaeldor(tradable = false)
        BLADE_OF_SAELDOR_C_25876.bladeOfSaeldor(tradable = false)
        BLADE_OF_SAELDOR_C_25878.bladeOfSaeldor(tradable = false)
        BLADE_OF_SAELDOR_C_25880.bladeOfSaeldor(tradable = false)
        BLADE_OF_SAELDOR_C_25882.bladeOfSaeldor(tradable = false)

        BOW_OF_FAERDHINEN_INACTIVE.bowOfFardhinen(tradable = false, stats = false)
        BOW_OF_FAERDHINEN.bowOfFardhinen()
        BOW_OF_FAERDHINEN_C.bowOfFardhinen(tradable = false)
        BOW_OF_FAERDHINEN_C_25884.bowOfFardhinen(tradable = false)
        BOW_OF_FAERDHINEN_C_25886.bowOfFardhinen(tradable = false)
        BOW_OF_FAERDHINEN_C_25888.bowOfFardhinen(tradable = false)
        BOW_OF_FAERDHINEN_C_25890.bowOfFardhinen(tradable = false)
        BOW_OF_FAERDHINEN_C_25892.bowOfFardhinen(tradable = false)
        BOW_OF_FAERDHINEN_C_25894.bowOfFardhinen(tradable = false)
        BOW_OF_FAERDHINEN_C_25896.bowOfFardhinen(tradable = false)

        ENHANCED_CRYSTAL_KEY {
            weight = 0.01F
        }

        ETERNAL_TELEPORT_CRYSTAL {
            weight = 0.001F
        }
    }

    fun Int.helm(bonuses: WearableDefinitionBonusesBuilder.() -> Unit = {}) =
        armour(0.5F, EquipmentSlot.HELMET, FULL_MASK, bonuses = bonuses)

    fun Int.legs(bonuses: WearableDefinitionBonusesBuilder.() -> Unit = {}) =
        armour(1F, EquipmentSlot.LEGS, bonuses = bonuses)

    fun Int.body(bonuses: WearableDefinitionBonusesBuilder.() -> Unit = {}) =
        armour(2F, EquipmentSlot.PLATE, FULL_BODY, bonuses = bonuses)

    fun Int.shield() =
        armour(2.721F, EquipmentSlot.SHIELD) {
            ATT_MAGIC(-10)
            ATT_RANGED(-10)

            DEF_STAB(51)
            DEF_SLASH(54)
            DEF_CRUSH(53)
            DEF_RANGE(80)
        }

    fun Int.axe(gauntlet: Boolean = false) = weapon(0.907F) {
        weapon {
            attackSpeed = 5
            blockAnimation = 424
            accurateAnimation = 401
            aggressiveAnimation = 395
            controlledAnimation = 395
            defensiveAnimation = 395
            interfaceVarbit = 1
        }
        bonuses {
            if (gauntlet) {
                ATT_STAB(4)
                ATT_SLASH(4)
                ATT_CRUSH(4)

                STRENGTH(5)
            } else {
                ATT_STAB(-2)
                ATT_SLASH(38)
                ATT_CRUSH(32)

                DEF_SLASH(1)

                STRENGTH(42)
            }
        }
        if (!gauntlet) {
            requirements {
                AGILITY(50)
                ATTACK(70)
            }
        }
    }

    fun Int.harpoon(gauntlet: Boolean = false) = weapon(0.907F) {
        weapon {
            attackSpeed = 5
            blockAnimation = 424
            standAnimation = 808
            walkAnimation = 819
            runAnimation = 824
            standTurnAnimation = 823
            rotate90Animation = 821
            rotate180Animation = 820
            rotate270Animation = 822
            accurateAnimation = 381
            aggressiveAnimation = 381
            controlledAnimation = 390
            defensiveAnimation = 381
            interfaceVarbit = 17
        }
        bonuses {
            if (gauntlet) {
                ATT_STAB(4)
                ATT_SLASH(4)
                ATT_CRUSH(4)

                STRENGTH(5)
            } else {
                ATT_STAB(38)
                ATT_SLASH(32)

                DEF_SLASH(1)

                STRENGTH(42)
            }
        }
        if (!gauntlet) {
            requirements {
                AGILITY(50)
                ATTACK(70)
            }
        }
    }

    fun Int.pickAxe(gauntlet: Boolean = false) = weapon(1.36F) {
        weapon {
            attackSpeed = 5
            accurateAnimation = 401
            aggressiveAnimation = 401
            controlledAnimation = 401
            defensiveAnimation = 400
            interfaceVarbit = 11
        }
        bonuses {
            if (gauntlet) {
                ATT_STAB(4)
                ATT_SLASH(4)
                ATT_CRUSH(4)

                STRENGTH(5)
            } else {
                ATT_STAB(38)
                ATT_SLASH(1)

                STRENGTH(42)
            }
        }
        if (!gauntlet) {
            requirements {
                AGILITY(50)
                ATTACK(70)
            }
        }
    }

    fun Int.bow() = weapon(1.814F) {
        weapon {
            isTwoHanded = true
            attackSpeed = 4
            accurateAnimation = 426
            aggressiveAnimation = 426
            controlledAnimation = 426
            defensiveAnimation = 426
            interfaceVarbit = 3
            normalAttackDistance = 9
            longAttackDistance = 9
        }
        bonuses {
            ATT_RANGED(100)

            RANGE_STRENGTH(78)
        }
        requirements {
            AGILITY(50)
            RANGED(70)
        }
    }

    fun Int.bowGauntlet(inputBonuses: WearableDefinitionBonusesBuilder.() -> Unit = {}) = weapon(1.814F) {
        weapon {
            isTwoHanded = true
            attackSpeed = 5
            accurateAnimation = 426
            aggressiveAnimation = 426
            controlledAnimation = 426
            defensiveAnimation = 426
            interfaceVarbit = 3
            normalAttackDistance = 10
            longAttackDistance = 10
        }
        bonuses(inputBonuses)
    }

    fun Int.halberd() = weapon(1.814F) {
        weapon {
            isTwoHanded = true
            attackSpeed = 2
            accurateAnimation = 428
            aggressiveAnimation = 440
            controlledAnimation = 440
            defensiveAnimation = 440
            interfaceVarbit = 12
        }
        bonuses {
            ATT_STAB(85)
            ATT_SLASH(110)
            ATT_CRUSH(5)
            ATT_MAGIC(-4)

            DEF_STAB(-1)
            DEF_SLASH(4)
            DEF_CRUSH(5)

            STRENGTH(118)
        }
        requirements {
            AGILITY(50)
            ATTACK(70)
            STRENGTH(35)
        }
    }

    fun Int.halberdGauntlet(inputBonuses: WearableDefinitionBonusesBuilder.() -> Unit = {}) = weapon(1.814F) {
        weapon {
            isTwoHanded = true
            attackSpeed = 6
            accurateAnimation = 428
            aggressiveAnimation = 440
            controlledAnimation = 440
            defensiveAnimation = 440
            interfaceVarbit = 12
        }
        bonuses(inputBonuses)
    }

    fun Int.staff(inputBonuses: WearableDefinitionBonusesBuilder.() -> Unit = {}) = weapon(1.36F) {
        weapon {
            attackSpeed = 6
            interfaceVarbit = 23
        }
        bonuses(inputBonuses)
    }

    fun Int.bladeOfSaeldor(tradable: Boolean = true) = weapon(1.814F, tradable) {
        weapon {
            attackSpeed = 6
            accurateAnimation = 390
            aggressiveAnimation = 390
            controlledAnimation = 412
            defensiveAnimation = 390
            interfaceVarbit = 9
        }
        bonuses {
            ATT_STAB(55)
            ATT_SLASH(94)

            STRENGTH(89)
        }
        requirements {
            ATTACK(80)
        }
    }

    fun Int.bowOfFardhinen(tradable: Boolean = true, stats: Boolean = true) = weapon(1.5F, tradable) {
        weapon {
            isTwoHanded = true
            attackSpeed = 5
            accurateAnimation = 426
            aggressiveAnimation = 426
            controlledAnimation = 426
            defensiveAnimation = 426
            interfaceVarbit = 3
            normalAttackDistance = 9
            longAttackDistance = 9
        }
        if (stats) {
            bonuses {
                ATT_RANGED(128)
                RANGE_STRENGTH(106)
            }
        }
        requirements {
            AGILITY(70)
            RANGED(80)
        }
    }

    fun Int.weapon(weight: Float, tradable: Boolean = true, equipment: WearableDefinition.() -> Unit) = this {
        this.weight = weight
        this.slot = EquipmentSlot.WEAPON.slot
        this.tradable = tradable
        equipment {
            equipment(this)
        }
    }

    fun Int.armour(
        weight: Float,
        slot: EquipmentSlot,
        equipmentType: EquipmentType = DEFAULT,
        bonuses: WearableDefinitionBonusesBuilder.() -> Unit = {},
    ) = this {
        this.weight = weight
        this.slot = slot.slot
        equipment(equipmentType) {
            bonuses {
                bonuses(this)
            }
            requirements {
                DEFENCE(70)
            }
        }
    }

    fun Int.helmGauntlet(bonuses: WearableDefinitionBonusesBuilder.() -> Unit = {}) =
        armourGauntlet(0.5F, EquipmentSlot.HELMET, FULL_MASK, bonuses = bonuses)

    fun Int.legsGauntlet(bonuses: WearableDefinitionBonusesBuilder.() -> Unit = {}) =
        armourGauntlet(1F, EquipmentSlot.LEGS, FULL_LEGS, bonuses = bonuses)

    fun Int.bodyGauntlet(bonuses: WearableDefinitionBonusesBuilder.() -> Unit = {}) =
        armourGauntlet(2F, EquipmentSlot.PLATE, FULL_BODY, bonuses = bonuses)

    fun Int.armourGauntlet(
        weight: Float,
        slot: EquipmentSlot,
        equipmentType: EquipmentType = DEFAULT,
        bonuses: WearableDefinitionBonusesBuilder.() -> Unit = {},
    ) = this {
        this.weight = weight
        this.slot = slot.slot
        equipment(equipmentType) {
            bonuses {
                bonuses(this)
            }
        }
    }

}
