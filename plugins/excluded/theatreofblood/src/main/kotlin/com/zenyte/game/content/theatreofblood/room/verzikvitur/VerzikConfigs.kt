package com.zenyte.game.content.theatreofblood.room.verzikvitur

import com.zenyte.game.content.theatreofblood.room.verzikvitur.first.SupportingPillar
import com.zenyte.game.content.theatreofblood.room.verzikvitur.spiders.NylocasAthanatos
import com.zenyte.game.content.theatreofblood.room.verzikvitur.spiders.NylocasMatomenos
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.PurpleTornado
import com.zenyte.game.content.theatreofblood.room.verzikvitur.third.Web
import com.zenyte.game.item.ItemId
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.combatdefs.*
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType
import mgi.types.config.items.ItemDefinitions
import java.util.*

/**
 * TODO improve this and separate it out into a real DSL in a nice way.
 *
 * @author Jire
 */
internal object VerzikConfigs {

    fun configs() {
        npc(NpcId.NYLOCAS_VASILIAS) {
            hitpoints = 100
        }
        npc(SupportingPillar.NPC_ID) {
            hitpoints = 350
        }
        npc(PurpleTornado.ID) {
            hitpoints = 100
            attackDistance = 1
            attackSpeed = 1
        }
        verzik(8369, 100) {}//Verzik transformation so it doesn't bug out.
        verzik(VerzikViturPhase.FIRST.npcID, 1500) {//shield change from 2000 to 1500 cus of combat system
            statDefinitions.apply {
                combatStats = intArrayOf(400, 400, 20, 400, 400)
                aggressiveStats = intArrayOf(0, 0, 0, 80, 80)
                defensiveStats = intArrayOf(20, 20, 20, 20, 20)
                otherBonuses = intArrayOf(0, 80, 80)
            }
            blockDefinitions.apply {
                animation = Animation(8110)
            }
        }
        verzik(8371, 100) {}//Verzik transformation so it doesn't bug out.
        verzik(VerzikViturPhase.SECOND.npcID, 3250) {
            statDefinitions.apply {
                combatStats = intArrayOf(400, 400, 100, 400, 400)//defence change from 200 to 100 cus of combat system
                aggressiveStats = intArrayOf(0, 0, 0, 80, 80)
                defensiveStats = intArrayOf(100, 60, 100, 70, 250)
                otherBonuses = intArrayOf(0, 80, 80)
            }
        }
        verzik(8373, 100) {}//Verzik transformation so it doesn't bug out.
        verzik(VerzikViturPhase.THIRD.npcID, 3250) {
            statDefinitions.apply {
                combatStats = intArrayOf(300, 400, 150, 300, 300)
                aggressiveStats = intArrayOf(80, 30, 80, 80, 80)
                defensiveStats = intArrayOf(70, 30, 70, 100, 230)
                otherBonuses = intArrayOf(0, 5, 5)
            }
            spawnDefinitions = SpawnDefinitions().apply {
                deathAnimation = Animation(8128)
            }
        }
        npc(NylocasMatomenos.ID) {
            hitpoints = 200
            statDefinitions.apply {
                combatStats = intArrayOf(100, 100, 100, 100, 100)
            }
            immunityTypes = EnumSet.of(ImmunityType.POISON, ImmunityType.VENOM)
            attackSpeed = 6

            spawnDefinitions = SpawnDefinitions().apply {
                deathAnimation = Animation(8097)
                spawnAnimation = Animation(8098)
            }
        }
        npc(NylocasAthanatos.ID) {
            hitpoints = 100
            statDefinitions.apply {
                combatStats = intArrayOf(100, 100, 100, 100, 100)
            }
            immunityTypes = EnumSet.of(ImmunityType.POISON, ImmunityType.VENOM)
            attackSpeed = 6

            spawnDefinitions = SpawnDefinitions().apply {
                deathAnimation = Animation(8078)
                spawnAnimation = Animation(8079)
            }
        }
        npc(Web.ID) {
            hitpoints = 10
        }
        item(ItemId.DAWNBRINGER) {
            examine = "A weapon of light to hold back the darkness."
            equipmentType = EquipmentType.DEFAULT
            slot = EquipmentSlot.WEAPON.slot
            //tradable = true
            isGrandExchange = true
            weight = 1.5F
            bonuses = intArrayOf(0, 0, 0, 25, 0, 2, 3, 1, 15, 0, 0, 0, 0, 0)
            isTwoHanded = false
            blockAnimation = 424
            standAnimation = 813
            walkAnimation = 1205
            runAnimation = 1210
            standTurnAnimation = 823
            rotate90Animation = 821
            rotate180Animation = 820
            rotate270Animation = 821
            accurateAnimation = 422
            aggressiveAnimation = 423
            controlledAnimation = 422
            defensiveAnimation = 422
            attackSpeed = 4
            interfaceVarbit = 23
            normalAttackDistance = 0//7
            longAttackDistance = 0//9
        }
    }


    private inline fun verzik(npcID: Int, hp: Int, crossinline apply: NPCCombatDefinitions.() -> Unit) =
        NPCCDLoader.insert(npcID, NPCCombatDefinitions().apply {
            id = npcID
            hitpoints = hp
            statDefinitions = StatDefinitions()
            blockDefinitions = BlockDefinitions()
            aggressionType = AggressionType.ALWAYS_AGGRESSIVE
            aggressionDistance = 64
            immunityTypes = EnumSet.of(ImmunityType.POISON, ImmunityType.VENOM)

            apply()
        })

    private inline fun npc(npcID: Int, crossinline apply: NPCCombatDefinitions.() -> Unit) =
        NPCCDLoader.insert(npcID, NPCCombatDefinitions().apply {
            id = npcID
            statDefinitions = StatDefinitions()
            blockDefinitions = BlockDefinitions()

            apply()
        })

    private inline fun item(itemID: Int, crossinline apply: ItemDefinitions.() -> Unit) =
        ItemDefinitions.get(itemID).apply(apply)

}