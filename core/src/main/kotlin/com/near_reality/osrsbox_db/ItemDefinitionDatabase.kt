package com.near_reality.osrsbox_db

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Jire
 */
object ItemDefinitionDatabase : AbstractDefinitionDatabase<ItemDefinition>(
    ItemDefinition::class.java,
    "data/osrsbox-db/items-complete.json"
) {

    override val logger: Logger = LoggerFactory.getLogger(ItemDefinitionDatabase::class.java)

    override fun buildConfigs() {
        for ((id, def) in definitions) {
            def.apply(id)
        }
    }

}