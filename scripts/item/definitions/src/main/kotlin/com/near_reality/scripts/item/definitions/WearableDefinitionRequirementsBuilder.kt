package com.near_reality.scripts.item.definitions

import mgi.types.config.items.WearableDefinition

/**
 * @author Jire
 */
class WearableDefinitionRequirementsBuilder(private val def: WearableDefinition) {

    operator fun Int.invoke(requiredLevel: Int) {
        var requirements = def.requirements
        if (requirements == null) {
            requirements = HashMap()
            def.requirements = requirements
        }

        requirements.put(this, requiredLevel)
    }

}