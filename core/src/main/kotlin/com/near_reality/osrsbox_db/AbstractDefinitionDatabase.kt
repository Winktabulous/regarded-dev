package com.near_reality.osrsbox_db

import it.unimi.dsi.fastutil.Hash

/**
 * @author Jire
 */
abstract class AbstractDefinitionDatabase<T>(
	override val definitionClass: Class<T>,
	private val fileName: String,
	expectedSize: Int = Hash.DEFAULT_INITIAL_SIZE
) : DefinitionDatabase<T> {

	override lateinit var definitions: Map<Int, T>
	
	override fun get(id: Int): T? = definitions[id]
	
	override fun fileName(): String = fileName
	
}