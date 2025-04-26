package com.near_reality.osrsbox_db

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Jire
 */
object MonsterDefinitionDatabase : AbstractDefinitionDatabase<MonsterDefinition>(
    MonsterDefinition::class.java,
    "data/osrsbox-db/monsters-complete.json"
) {

    override val logger: Logger = LoggerFactory.getLogger(MonsterDefinitionDatabase::class.java)

    override fun buildConfigs() {
        for ((id, def) in definitions) {
            def.apply(id)
        }
    }

}