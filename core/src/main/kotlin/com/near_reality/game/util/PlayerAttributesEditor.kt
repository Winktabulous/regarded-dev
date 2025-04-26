package com.near_reality.game.util

import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import com.zenyte.plugins.dialogue.OptionsMenuD

/**
 * Utility class for easily modifying player utilities in-game.
 *
 * @author Stan van der Bend
 */
class PlayerAttributesEditor(player: Player) : Dialogue(player) {

    override fun buildDialogue() {
        player.options {
            "Search persistent attribute" {
                search(persistent = true)
            }
            "Search temporary attribute" {
                search(persistent = false)
            }
            "List all persistent attributes" {
                openMenu(getAttributeKeys(true),true)
            }
            "List all temporary attributes" {
                openMenu(getAttributeKeys(false),false)
            }
        }
    }

    private fun search(persistent: Boolean) {
        player.dialogueManager.finish()
        player.sendInputString("Enter part of the attribute key") { searchTerm ->
            val keys = getAttributeKeys(persistent)
            val matches = keys.filter { it.contains(searchTerm, persistent) }
            if (matches.isEmpty())
                player.dialogue { plain("Did not find any matches for input string $searchTerm") }
            else
                openMenu(matches, persistent)
        }
    }

    private fun openMenu(matches: List<String>, persistent: Boolean) =
        player.dialogueManager.start(Options(player, matches.sortedDescending(), persistent))

    private fun getAttributeKeys(persistent: Boolean) = if (persistent)
        player.attributes.keys.toList()
    else
        player.temporaryAttributes.keys.map { if (it is String) it else it::class.simpleName }

    private class Options(player: Player, val attributeKeys: List<String>, val persistent: Boolean) :
        OptionsMenuD(player, "Click to modify attribute", *attributeKeys.toTypedArray()) {
        override fun handleClick(slotId: Int) {
            val key = attributeKeys[slotId]
            player.sendInputString("Set value for `$key`") { inputValue ->
                val previousValue = if (persistent)
                    player.attributes[key]
                else
                    player.temporaryAttributes[key]
                if (previousValue != null) {
                    val newValue = when (previousValue) {
                        is Int -> inputValue.toInt()
                        is Double -> inputValue.toDouble()
                        is Float -> inputValue.toFloat()
                        is Long -> inputValue.toLong()
                        is Byte -> inputValue.toByte()
                        is String -> inputValue
                        else -> {
                            player.sendDeveloperMessage("Unknown value type ${previousValue::class} (value=$previousValue)")
                            return@sendInputString
                        }
                    }
                    setValue(key, newValue)
                } else {
                    player.options("Could not detect type of attribute, please choose one of the following:") {
                        "String" { setValue(key, inputValue) }
                        "Int" { setValue(key, inputValue.toInt()) }
                        "Double" { setValue(key, inputValue.toDouble()) }
                        "Float" { setValue(key, inputValue.toFloat()) }
                    }
                }
            }
        }

        private fun setValue(key: String, newValue: Comparable<*>) {
            if (persistent)
                player.attributes[key] = newValue
            else
                player.temporaryAttributes[key] = newValue
        }

        override fun cancelOption(): Boolean = true
    }
}
