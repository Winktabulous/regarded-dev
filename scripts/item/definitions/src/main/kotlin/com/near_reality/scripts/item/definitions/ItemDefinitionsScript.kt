package com.near_reality.scripts.item.definitions

import com.near_reality.scripts.item.ItemScript
import com.zenyte.game.parser.impl.JSONItemDefinitionsLoader
import com.zenyte.game.world.entity.player.Bonuses
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentType
import com.zenyte.plugins.InitPlugin
import com.zenyte.plugins.PluginPriority
import mgi.types.config.items.ItemDefinitions
import mgi.types.config.items.JSONItemDefinitions
import mgi.types.config.items.WearableDefinition
import mgi.types.config.items.WieldableDefinition
import kotlin.script.experimental.annotations.KotlinScript

/**
 * Represents an [ItemScript] that can be used to define Item Definitions.
 *
 * If a definition for the id is already defined in another system,
 * this plugin will override the value.
 *
 * maybe restart will fix tho havent tried that lol
 *
 * @author Jire
 * @author Stan van der Bend
 */
@KotlinScript(
    "Item Definitions Script",
    fileExtension = "items.kts",
    compilationConfiguration = ItemDefinitionsCompilation::class
)
@PluginPriority(1_000)
abstract class ItemDefinitionsScript : ItemScript, InitPlugin {

    /**
     * Create a new [JSONItemDefinitions] for the invoked upon id
     * and [merge it in the pre-existing item definition][ItemDefinitions.apply].
     *
     * @param baseId the to be build item definition will inherit default values
     *               from the definition associated with this id.
     */
    operator fun Int.invoke(baseId: Int? = null, build: JSONItemDefinitions.() -> Unit) {
        val baseDefinition = if (baseId != null)
            JSONItemDefinitionsLoader.lookup(baseId).copy()
        else
            JSONItemDefinitions()

        baseDefinition.id = this
        baseDefinition.build()

        ItemDefinitions.apply(baseDefinition)
    }

    fun JSONItemDefinitions.equipment(
        equipmentType: EquipmentType = this.equipmentType ?: EquipmentType.DEFAULT,
        build: WearableDefinition.() -> Unit,
    ) {
        this.equipmentType = equipmentType


        val def = (equipmentDefinition ?: WearableDefinition()).apply(build)
        equipmentDefinition = def
    }

    fun WearableDefinition.bonuses(vararg bonuses: Int) {
        this.bonuses = bonuses.joinToString(",")
    }

    fun WearableDefinition.requirements(build: WearableDefinitionRequirementsBuilder.() -> Unit) {
        WearableDefinitionRequirementsBuilder(this).build()
    }

    fun WearableDefinition.bonuses(build: WearableDefinitionBonusesBuilder.() -> Unit) {
        WearableDefinitionBonusesBuilder(this).apply {
            // copy bonuses of inherited item if any
            this@bonuses.bonuses?.split(",")?.forEachIndexed { index, value ->
                Bonuses.Bonus.VALUES[index](value.trim().toInt())
            }
            build()
            apply()
        }
    }

    fun WearableDefinition.weapon(build: WieldableDefinition.() -> Unit) {
        weaponDefinition = (weaponDefinition ?: WieldableDefinition()).apply(build)
    }

}
