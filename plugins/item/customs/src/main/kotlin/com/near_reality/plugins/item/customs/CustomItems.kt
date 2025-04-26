package com.near_reality.plugins.item.customs

import com.near_reality.scripts.item.definitions.ItemDefinitionsScript
import com.zenyte.game.item.ItemId.ABYSSAL_TENTACLE
import com.zenyte.game.item.ItemId.ABYSSAL_WHIP
import com.zenyte.game.item.ItemId.ANCIENT_BOOK_32004
import com.zenyte.game.item.ItemId.ANCIENT_EYE
import com.zenyte.game.item.ItemId.ANCIENT_MEDALLION_32024
import com.zenyte.game.item.ItemId.ANGUISH_ORNAMENT_KIT
import com.zenyte.game.item.ItemId.ANKOUS_LEGGINGS
import com.zenyte.game.item.ItemId.ANKOU_GLOVES
import com.zenyte.game.item.ItemId.ANKOU_MASK
import com.zenyte.game.item.ItemId.ANKOU_SOCKS
import com.zenyte.game.item.ItemId.ANKOU_TOP
import com.zenyte.game.item.ItemId.ARMADYL_BOW
import com.zenyte.game.item.ItemId.ARMADYL_SOUL_CRYSTAL
import com.zenyte.game.item.ItemId.AVERNIC_DEFENDER
import com.zenyte.game.item.ItemId.BANDOS_BOW
import com.zenyte.game.item.ItemId.BANDOS_CHESTPLATE
import com.zenyte.game.item.ItemId.BANDOS_CHESTPLATE_OR
import com.zenyte.game.item.ItemId.BANDOS_ORNAMENT_KIT
import com.zenyte.game.item.ItemId.BANDOS_SOUL_CRYSTAL
import com.zenyte.game.item.ItemId.BANDOS_TASSETS
import com.zenyte.game.item.ItemId.BANDOS_TASSETS_OR
import com.zenyte.game.item.ItemId.BARROWS_GLOVES
import com.zenyte.game.item.ItemId.BLACK_ANKOUS_LEGGINGS
import com.zenyte.game.item.ItemId.BLACK_ANKOU_GLOVES
import com.zenyte.game.item.ItemId.BLACK_ANKOU_MASK
import com.zenyte.game.item.ItemId.BLACK_ANKOU_SOCKS
import com.zenyte.game.item.ItemId.BLACK_ANKOU_TOP
import com.zenyte.game.item.ItemId.BLUE_ANKOUS_LEGGINGS
import com.zenyte.game.item.ItemId.BLUE_ANKOU_GLOVES
import com.zenyte.game.item.ItemId.BLUE_ANKOU_MASK
import com.zenyte.game.item.ItemId.BLUE_ANKOU_SOCKS
import com.zenyte.game.item.ItemId.BLUE_ANKOU_TOP
import com.zenyte.game.item.ItemId.BLUE_TWISTED_BOW
import com.zenyte.game.item.ItemId.BROKEN_CRAB_SHELL
import com.zenyte.game.item.ItemId.BROKEN_EGG_SHELLS
import com.zenyte.game.item.ItemId.BRONZE_KEY
import com.zenyte.game.item.ItemId.BRONZE_SPEAR
import com.zenyte.game.item.ItemId.CARROT_CROWN
import com.zenyte.game.item.ItemId.CARROT_SPEAR
import com.zenyte.game.item.ItemId.CHRISTMAS_SCYTHE
import com.zenyte.game.item.ItemId.CORRUPTED_GAUNTLET_SLAYER_HELM
import com.zenyte.game.item.ItemId.DEATH_CAPE
import com.zenyte.game.item.ItemId.DIAMOND_KEY
import com.zenyte.game.item.ItemId.DIVINE_KIT
import com.zenyte.game.item.ItemId.DONATOR_PIN_10
import com.zenyte.game.item.ItemId.DONATOR_PIN_100
import com.zenyte.game.item.ItemId.DONATOR_PIN_25
import com.zenyte.game.item.ItemId.DONATOR_PIN_50
import com.zenyte.game.item.ItemId.DRAGONFIRE_SHIELD
import com.zenyte.game.item.ItemId.DRAGON_BOOTS
import com.zenyte.game.item.ItemId.DRAGON_KITE
import com.zenyte.game.item.ItemId.EASTER_CARDS
import com.zenyte.game.item.ItemId.EASTER_MYSTERY_BOX
import com.zenyte.game.item.ItemId.ELYSIAN_SPIRIT_SHIELD
import com.zenyte.game.item.ItemId.ELYSIAN_SPIRIT_SHIELD_OR
import com.zenyte.game.item.ItemId.EYE_OF_NEWT
import com.zenyte.game.item.ItemId.FIRE_CAPE
import com.zenyte.game.item.ItemId.GAUNTLET_SLAYER_HELM
import com.zenyte.game.item.ItemId.GOLD_ANKOUS_LEGGINGS
import com.zenyte.game.item.ItemId.GOLD_ANKOU_GLOVES
import com.zenyte.game.item.ItemId.GOLD_ANKOU_MASK
import com.zenyte.game.item.ItemId.GOLD_ANKOU_SOCKS
import com.zenyte.game.item.ItemId.GOLD_ANKOU_TOP
import com.zenyte.game.item.ItemId.GOLD_KEY
import com.zenyte.game.item.ItemId.GREEN_ANKOUS_LEGGINGS
import com.zenyte.game.item.ItemId.GREEN_ANKOU_GLOVES
import com.zenyte.game.item.ItemId.GREEN_ANKOU_MASK
import com.zenyte.game.item.ItemId.GREEN_ANKOU_SOCKS
import com.zenyte.game.item.ItemId.GREEN_ANKOU_TOP
import com.zenyte.game.item.ItemId.HELM_OF_NEITIZNOT
import com.zenyte.game.item.ItemId.IMBUED_ANCIENT_CAPE
import com.zenyte.game.item.ItemId.IMBUED_ARMADYL_CAPE
import com.zenyte.game.item.ItemId.IMBUED_BANDOS_CAPE
import com.zenyte.game.item.ItemId.IMBUED_GUTHIX_CAPE
import com.zenyte.game.item.ItemId.IMBUED_SEREN_CAPE
import com.zenyte.game.item.ItemId.KEY
import com.zenyte.game.item.ItemId.LAVA_WHIP
import com.zenyte.game.item.ItemId.LIME_WHIP
import com.zenyte.game.item.ItemId.LIME_WHIP_SPECIAL
import com.zenyte.game.item.ItemId.MAGES_BOOK
import com.zenyte.game.item.ItemId.MUSHROOM_SPORE
import com.zenyte.game.item.ItemId.MYSTERY_BOX
import com.zenyte.game.item.ItemId.MYSTIC_CARDS
import com.zenyte.game.item.ItemId.NEAR_REALITY_PARTY_HAT
import com.zenyte.game.item.ItemId.NR_TABLET
import com.zenyte.game.item.ItemId.OCCULT_NECKLACE
import com.zenyte.game.item.ItemId.OLD_SCHOOL_BOND_UNTRADEABLE
import com.zenyte.game.item.ItemId.ORANGE_PARTYHAT
import com.zenyte.game.item.ItemId.OSNR_MYSTERY_BOX
import com.zenyte.game.item.ItemId.PINK_PARTYHAT
import com.zenyte.game.item.ItemId.PLATINUM_KEY
import com.zenyte.game.item.ItemId.POLYPORE_SPORES
import com.zenyte.game.item.ItemId.POLYPORE_STAFF
import com.zenyte.game.item.ItemId.POLYPORE_STAFF_DEG
import com.zenyte.game.item.ItemId.PURPLE_ANKOUS_LEGGINGS
import com.zenyte.game.item.ItemId.PURPLE_ANKOU_GLOVES
import com.zenyte.game.item.ItemId.PURPLE_ANKOU_MASK
import com.zenyte.game.item.ItemId.PURPLE_ANKOU_SOCKS
import com.zenyte.game.item.ItemId.PURPLE_ANKOU_TOP
import com.zenyte.game.item.ItemId.PURPLE_TWISTED_BOW
import com.zenyte.game.item.ItemId.RED_HALLOWEEN_MASK
import com.zenyte.game.item.ItemId.RED_PARTYHAT
import com.zenyte.game.item.ItemId.RED_TWISTED_BOW
import com.zenyte.game.item.ItemId.ROYAL_CROWN
import com.zenyte.game.item.ItemId.SANTA_HAT
import com.zenyte.game.item.ItemId.SARADOMIN_BOW
import com.zenyte.game.item.ItemId.SARADOMIN_SOUL_CRYSTAL
import com.zenyte.game.item.ItemId.SILVER_KEY
import com.zenyte.game.item.ItemId.SLAYER_HELMET_I
import com.zenyte.game.item.ItemId.TOXIC_STAFF_OF_THE_DEAD
import com.zenyte.game.item.ItemId.TWISTED_BOW
import com.zenyte.game.item.ItemId.VARROCK_TELEPORT
import com.zenyte.game.item.ItemId.WHITE_ANKOUS_LEGGINGS
import com.zenyte.game.item.ItemId.WHITE_ANKOU_GLOVES
import com.zenyte.game.item.ItemId.WHITE_ANKOU_MASK
import com.zenyte.game.item.ItemId.WHITE_ANKOU_SOCKS
import com.zenyte.game.item.ItemId.WHITE_ANKOU_TOP
import com.zenyte.game.item.ItemId.WHITE_PARTYHAT
import com.zenyte.game.item.ItemId.WHITE_TWISTED_BOW
import com.zenyte.game.item.ItemId.WOODEN_SHIELD
import com.zenyte.game.item.ItemId.ZAMORAK_BOW
import com.zenyte.game.item.ItemId.ZAMORAK_SOUL_CRYSTAL
import com.zenyte.game.world.entity.player.Bonuses
import com.zenyte.game.world.entity.player.Bonuses.Bonus.MAGIC_DAMAGE
import com.zenyte.game.world.entity.player.SkillConstants.AGILITY
import com.zenyte.game.world.entity.player.SkillConstants.ATTACK
import com.zenyte.game.world.entity.player.SkillConstants.RANGED
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType.FULL_BODY
import mgi.types.config.items.JSONItemDefinitions

class CustomItems : ItemDefinitionsScript() {

	init {
		ANCIENT_EYE(EYE_OF_NEWT) {}
		ANCIENT_BOOK_32004(MAGES_BOOK) {
			equipment {
				bonuses {
					MAGIC_DAMAGE(2)
				}
			}
		}

		ARMADYL_SOUL_CRYSTAL {}
		ARMADYL_BOW.godBow()

		BANDOS_SOUL_CRYSTAL {}
		BANDOS_BOW.godBow()

		SARADOMIN_SOUL_CRYSTAL {}
		SARADOMIN_BOW.godBow()

		ZAMORAK_SOUL_CRYSTAL {}
		ZAMORAK_BOW.godBow()

		DRAGON_KITE(DRAGONFIRE_SHIELD) {
			equipment {
				bonuses {
					Bonuses.Bonus.ATT_STAB(13)
					Bonuses.Bonus.ATT_SLASH(12)
					Bonuses.Bonus.ATT_CRUSH(8)
					Bonuses.Bonus.ATT_RANGED(8.unaryMinus())
					Bonuses.Bonus.ATT_MAGIC(9.unaryMinus())

					Bonuses.Bonus.DEF_STAB(85)
					Bonuses.Bonus.DEF_SLASH(90)
					Bonuses.Bonus.DEF_CRUSH(87)
					Bonuses.Bonus.DEF_RANGE(87)
					Bonuses.Bonus.DEF_MAGIC(15)

					Bonuses.Bonus.STRENGTH(10)
				}
			}
		}

		ANCIENT_MEDALLION_32024(OCCULT_NECKLACE) {
			equipment {
				bonuses {
					MAGIC_DAMAGE(12)
				}
			}
		}

		IMBUED_ANCIENT_CAPE.mageCape()
		IMBUED_ARMADYL_CAPE.mageCape()
		IMBUED_BANDOS_CAPE.mageCape()
		//ZAMORAK_MAGE_CAPE.mageCape()
		IMBUED_SEREN_CAPE.mageCape()

		DEATH_CAPE(FIRE_CAPE) {
			equipment {
				bonuses {
					Bonuses.Bonus.STRENGTH(6)
				}
			}
		}

		GAUNTLET_SLAYER_HELM(SLAYER_HELMET_I) {}
		CORRUPTED_GAUNTLET_SLAYER_HELM(SLAYER_HELMET_I) {}

		POLYPORE_SPORES(MUSHROOM_SPORE) {}
		POLYPORE_STAFF_DEG.polyporeStaff()
		POLYPORE_STAFF.polyporeStaff()

		BRONZE_KEY.key()
		SILVER_KEY.key()
		GOLD_KEY.key()
		PLATINUM_KEY.key()
		DIAMOND_KEY.key()
		NR_TABLET.invoke(VARROCK_TELEPORT) {}

		LIME_WHIP.invoke(ABYSSAL_WHIP) {
			equipment {
				weapon {
					attackSpeed = 6
				}
			}
		}

		LIME_WHIP_SPECIAL.invoke(ABYSSAL_TENTACLE) {
			equipment {
				weapon {
					attackSpeed = 6
				}
			}
		}
		LAVA_WHIP.invoke(ABYSSAL_WHIP) {}

		PINK_PARTYHAT.partyHat()
		ORANGE_PARTYHAT.partyHat()
		NEAR_REALITY_PARTY_HAT.partyHat()

		DONATOR_PIN_10.donatorPin()
		DONATOR_PIN_25.donatorPin()
		DONATOR_PIN_50.donatorPin()
		DONATOR_PIN_100.donatorPin()



		OSNR_MYSTERY_BOX.invoke(MYSTERY_BOX) {}

		BLUE_ANKOU_SOCKS.invoke(ANKOU_SOCKS) {}
		BLUE_ANKOU_GLOVES.invoke(ANKOU_GLOVES) {}
		BLUE_ANKOUS_LEGGINGS.invoke(ANKOUS_LEGGINGS) {}
		BLUE_ANKOU_MASK.invoke(ANKOU_MASK) {}
		BLUE_ANKOU_TOP.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }
		GREEN_ANKOU_SOCKS.invoke(ANKOU_SOCKS) {}
		GREEN_ANKOU_GLOVES.invoke(ANKOU_GLOVES) {}
		GREEN_ANKOUS_LEGGINGS.invoke(ANKOUS_LEGGINGS) {}
		GREEN_ANKOU_MASK.invoke(ANKOU_MASK) {}
		GREEN_ANKOU_TOP.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }
		GOLD_ANKOU_SOCKS.invoke(ANKOU_SOCKS) {}
		GOLD_ANKOU_GLOVES.invoke(ANKOU_GLOVES) {}
		GOLD_ANKOUS_LEGGINGS.invoke(ANKOUS_LEGGINGS) {}
		GOLD_ANKOU_MASK.invoke(ANKOU_MASK) {}
		GOLD_ANKOU_TOP.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }
		WHITE_ANKOU_SOCKS.invoke(ANKOU_SOCKS) {}
		WHITE_ANKOU_GLOVES.invoke(ANKOU_GLOVES) {}
		WHITE_ANKOUS_LEGGINGS.invoke(ANKOUS_LEGGINGS) {}
		WHITE_ANKOU_MASK.invoke(ANKOU_MASK) {}
		WHITE_ANKOU_TOP.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }
		BLACK_ANKOU_SOCKS.invoke(ANKOU_SOCKS) {}
		BLACK_ANKOU_GLOVES.invoke(ANKOU_GLOVES) {}
		BLACK_ANKOUS_LEGGINGS.invoke(ANKOUS_LEGGINGS) {}
		BLACK_ANKOU_MASK.invoke(ANKOU_MASK) {}
		BLACK_ANKOU_TOP.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }
		PURPLE_ANKOU_SOCKS.invoke(ANKOU_SOCKS) {}
		PURPLE_ANKOU_GLOVES.invoke(ANKOU_GLOVES) {}
		PURPLE_ANKOUS_LEGGINGS.invoke(ANKOUS_LEGGINGS) {}
		PURPLE_ANKOU_MASK.invoke(ANKOU_MASK) {}
		PURPLE_ANKOU_TOP.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }

		BLUE_TWISTED_BOW.invoke(TWISTED_BOW) {}
		PURPLE_TWISTED_BOW.invoke(TWISTED_BOW) {}
		RED_TWISTED_BOW.invoke(TWISTED_BOW) {}
		WHITE_TWISTED_BOW.invoke(TWISTED_BOW) {}
		DIVINE_KIT.invoke(ANGUISH_ORNAMENT_KIT) {}
		ELYSIAN_SPIRIT_SHIELD_OR.invoke(ELYSIAN_SPIRIT_SHIELD) {}
		32216.invoke(ANKOU_SOCKS) {}
		32217.invoke(ANKOU_TOP) { equipment(FULL_BODY) {} }
		32218.invoke(ANKOU_GLOVES) {}
		32219.invoke(CHRISTMAS_SCYTHE) {}
		32220.invoke(ANKOUS_LEGGINGS) {}
		32221.invoke(ANKOU_MASK) {}
		32222.invoke(WOODEN_SHIELD) {}
		32223.invoke(WOODEN_SHIELD) {}

		32224.invoke(RED_PARTYHAT) {}
		32225.invoke(SANTA_HAT) {}
		32226.invoke(RED_HALLOWEEN_MASK) {}
		32415.invoke(RED_HALLOWEEN_MASK) {}
		32417.invoke(RED_HALLOWEEN_MASK) {}
		32419.invoke(RED_HALLOWEEN_MASK) {}

		32221.invoke(HELM_OF_NEITIZNOT) {}
		32217.invoke(BANDOS_CHESTPLATE) {}
		32220.invoke(BANDOS_TASSETS) {}
		32223.invoke(DRAGONFIRE_SHIELD) {}
		32222.invoke(AVERNIC_DEFENDER) {}
		32218.invoke(BARROWS_GLOVES) {}
		32216.invoke(DRAGON_BOOTS) {}
		32219 {
			tradable = true
			slot = EquipmentSlot.WEAPON.slot
			equipment {
				weapon {
					attackSpeed = 6
					accurateAnimation = 390
					aggressiveAnimation = 390
					controlledAnimation = 412
					defensiveAnimation = 390
					interfaceVarbit = 9
				}
				bonuses {
					Bonuses.Bonus.ATT_STAB(55)
					Bonuses.Bonus.ATT_SLASH(94)

					Bonuses.Bonus.STRENGTH(89)
				}
				requirements {
					ATTACK(80)
				}
			}
		}
		CARROT_SPEAR.invoke(BRONZE_SPEAR) {}
		CARROT_CROWN.invoke(ROYAL_CROWN) {}
		BROKEN_EGG_SHELLS.invoke(BROKEN_CRAB_SHELL) {}
		EASTER_MYSTERY_BOX.invoke(MYSTERY_BOX) {}
		EASTER_CARDS.invoke(MYSTIC_CARDS) {}

		BANDOS_CHESTPLATE_OR.invoke(BANDOS_CHESTPLATE) {}
		BANDOS_TASSETS_OR.invoke(BANDOS_TASSETS) {}
		BANDOS_ORNAMENT_KIT.invoke(ANGUISH_ORNAMENT_KIT) {}
	}

	fun Int.godBow(build: JSONItemDefinitions.() -> Unit = {}) = invoke {
		this.weight = 1.5F
		this.slot = EquipmentSlot.WEAPON.slot
		equipment {
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
			bonuses {
				Bonuses.Bonus.ATT_RANGED(128)

				Bonuses.Bonus.RANGE_STRENGTH(106)
			}
			requirements {
				AGILITY(70)
				RANGED(80)
			}
		}
		build()
	}


	fun Int.mageCape(build: JSONItemDefinitions.() -> Unit = {}) =
		invoke(IMBUED_GUTHIX_CAPE) {
			build()
			tradable = false
		}

	fun Int.polyporeStaff(build: JSONItemDefinitions.() -> Unit = {}) =
		invoke(TOXIC_STAFF_OF_THE_DEAD, build)
	fun Int.key(build: JSONItemDefinitions.() -> Unit = {}) =
		invoke(KEY, build)
	fun Int.whip() =
		invoke(ABYSSAL_WHIP) {}
	fun Int.partyHat() =
		invoke(WHITE_PARTYHAT) {}
	fun Int.donatorPin() = invoke(OLD_SCHOOL_BOND_UNTRADEABLE){
		tradable = false
	}

}
