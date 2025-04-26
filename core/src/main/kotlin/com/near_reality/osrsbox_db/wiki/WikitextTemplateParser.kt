package com.near_reality.osrsbox_db.wiki

/**
 * @author Jire
 */
class WikitextTemplateParser(
    val wikitext: String,
    val template: String? = null,
    val isVersioned: Boolean = false,
    val versionIdentifiers: Map<String, Int> =
        mapOf("id" to 0, "version" to 0, "name" to 0, "itemid" to 0)
)