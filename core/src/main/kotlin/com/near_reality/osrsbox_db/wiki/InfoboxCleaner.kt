package com.near_reality.osrsbox_db.wiki

import org.mariuszgromada.math.mxparser.Expression
import java.util.regex.Pattern
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * @author Jire
 */
object InfoboxCleaner {

    private fun cleanWikitext(value: String) = @Suppress("DEPRECATION") value.trim()
        .replace(Regex("[\\[\\]]+"), "") /* Removes all "[" and "]" */
        .replace(Regex(" \\([^()]*\\)"), "") /* Removes " (anything)" */
        .replace(Regex("<!--(.*?)-->"), "") /* Removed "<!--anything-->" */
        .replace(Regex("<br(.*)"), "") /* Removes "<br" */

    fun cleanBoolean(value: String): Boolean {
        val v = cleanWikitext(value)
        if ("True" == v || "true" == v || "Yes" == v || "yes" == v)
            return true
        /*else if ("False" == v || "false" == v || "No" == v || "no" == v)
            return false*/
        return false
    }

    fun cleanFloat(value: String) = cleanWikitext(value).toFloatOrNull() ?: 0.0F

    fun cleanInteger(value: String) = cleanWikitext(value).toIntOrNull() ?: 0

    val cleanDropQuantityPattern: Pattern = Pattern.compile("^\\d*([-,]\\d*)?")

    fun cleanDropQuantity(value: String): String? {
        var v = cleanWikitext(value)
        if (v.isBlank()) return null

        /* Replace spaces, then remove "(noted)" */
        v = v.replace(" ", "").replace(Regex(" *\\(noted\\) *"), "")

        /* Change semi-colon seperated list of numbers to commas */
        v = v.replace(';', ',')

        if (!cleanDropQuantityPattern.matcher(v).matches()) {
            System.err.println("Drop quantity regex failed: $v")
            return null
        }

        return v
    }

    val cleanDropRarityPattern = Pattern.compile("^\\d*(\\.\\d*)?/(\\d*)(\\.\\d*)?")

    fun cleanDropRarity(value: String, baseValue: String?): Double {
        /* Temp fix for 0 division: 484 Bloodveld */
        if (value == "0/0") return 1.0 / 128.0

        var v = value
        if (!v.contains("#expr")) v = cleanWikitext(v)
        if (v.isEmpty()) return -1.0

        /* Clean the original value */
        /* Remove: brackets, curly braces, spaces, tidle, plus */
        /* Remove (if expr): curly braces, spaces */
        v = if (!v.contains("#expr")) v.replace(Regex("[(){}, ~+]"), "")
        else v.replace(Regex("[{} ]"), "")

        /* Quick fix, remove <small></small> */
        v = v.replace(Regex("<.*?>"), "")

        /* Remove "Rarity|" from value */
        v = v.replace("Rarity|", "")

        v = v.replace("DropsTableBottom", "")

        /* Convert raw value into fraction */
        val vl = v.lowercase()

        var numerator = -1.0
        var denominator = -1.0

        fun set(num: Double, denom: Double) {
            numerator = num
            denominator = denom
        }

        fun outOf(outOf: Double) = set(1.0, outOf)

        fun none() = set(-1.0, -1.0)

        fun convertBase(
            replaceRound: String,
            replaceString: String
        ) =
            set(
                v.split("/")[0].toDouble(),
                Expression(
                    value.split("#expr:")[1]
                        .replace(Regex(replaceRound), "")
                        .replace(replaceString, baseValue!!)
                        .ridOfEnding()
                )
                    .calculate()
                    .roundToOneDecimalPlace()
            )

        when {
            vl == "unknown" || vl == "varies" || vl == "unsure" || vl == "random" -> none()
            vl == "always" -> outOf(1.0)
            vl == "common" -> outOf(8.0)
            vl == "uncommon" -> outOf(32.0)
            vl == "rare" -> outOf(128.0)
            vl == "veryrare" -> outOf(512.0)
            vl.contains("brimstone") -> {
                if (true) return -1.0 // TODO support brimstone rarities (bugged due to escaping out of |)
                // Rarity={{Brimstone rarity|96|bonus=yes}}
                val split = v.split("|")
                val level = split[1].toInt()
                val bonus = if (split.lastIndex >= 2) split[2] else "no"

                var outOfValue: Double = if (level < 100)
                    (0.2 * (level - 100)).pow(2) + 100
                else -0.2 * level + 120
                if (bonus.contains("yes")) outOfValue *= 0.8

                outOf(outOfValue)
            }
            vl.contains("var:herbbase") -> convertBase("round[1 ]", "#var:herbbase")
            vl.contains("var:seedbase") -> convertBase("round[1 ]", "#var:seedbase")
            vl.contains("var:uht") -> convertBase("round[2 ]", "#var:uht")
            vl.contains("var:bolttipbase") -> convertBase("round[1 ]", "#var:bolttipbase")
            vl.contains("#expr:") -> {
                set(
                    v.split("/")[0].toDouble(),
                    Expression(
                        value.split("#expr:")[1]
                            .replace(Regex("round[1 ]"), "")
                            .ridOfEnding()
                    )
                        .calculate()
                        .roundToOneDecimalPlace()
                )
            }
            vl.contains(":") -> {
                val split = v.split(":")[0].split("/")
                set(split[0].toDouble(), split[1].toDouble())
            }
            else -> return Expression(v).calculate()
        }

        return numerator / denominator
    }

    private fun String.ridOfEnding() = if (endsWith(" 1")) substring(0, length - 2) else this

    private fun Double.roundToOneDecimalPlace() = (this * 10).roundToInt() / 10.0

    fun cleanDropRequirements(value: String): String {
        val v = cleanWikitext(value)
        return when {
            v.isEmpty() -> ""
            v.contains("[[Wilderness") -> "wilderness-only"
            v.contains("[[Konar quo Maten]]") -> "konar-task-only"
            v.contains("[[Catacombs of Kourend]]")
                    || v.contains("name=catacomb")
                    || v.contains("name=\"catacomb\"") -> "catacombs-only"
            v.contains("[[Krystilia]]") -> "wilderness-slayer"
            v.contains("[[Treasure Trails") -> "treasure-trails-only"
            v.contains("[[Iorwerth Dungeon]]") -> "iorwerth-dungeon-only"
            v.contains("Forthos Dungeon") -> "forthos-dungeon-only"
            v.contains("[[Revenant Caves]]")
                    || v.contains("name=\"revcaves\"") -> "revenants-only"
            else -> ""
        }
    }

}