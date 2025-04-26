package org.runestar.cs2.type

import org.runestar.cs2.util.CP1252
import org.runestar.cs2.util.toByte

enum class Type(id: Int = -1, desc: Char = 0.toChar(), fullName: String = "") {

    INT(0, 'i', "integer"),
    BOOLEAN(1, '1', "boolean"),
    SEQ(6, 'A', "seq"),
    COLOUR(7, 'C', "colour"),
    COMPONENT(9, 'I', "component"),
    IDKIT(10, 'K', "idkit"),
    MIDI(11, 'M', "midi"),
    NAMEDOBJ(13, 'O', "namedobj"),
    SYNTH(14, 'P', "synth"),
    COORD(22, 'c', "coordgrid"),
    GRAPHIC(23, 'd', "graphic"),
    FONTMETRICS(25, 'f', "fontmetrics"),
    ENUM(26, 'g', "enum"),
    JINGLE(28, 'j', "jingle"),
    LOC(30, 'l', "loc"),
    MODEL(31, 'm', "model"),
    NPC(32, desc = 'n', fullName = "npc"),
    OBJ(33, desc = 'o', fullName = "obj"),
    STRING(36, desc = 's', fullName = "string"),
    SPOTANIM(37, desc = 't', fullName = "spotanim"),
    INV(39, desc = 'v', fullName = "inv"),
    TEXTURE(40, desc = 'x', fullName = "texture"),
    CHAR(42, desc = 'z', fullName = "char"),
    MAPSCENEICON(55, desc = '£', fullName = "mapsceneicon"),
    MAPELEMENT(59, desc = 'µ', fullName = "mapelement"),
    HITMARK(62, desc = '×', fullName = "hitmark"),
    STRUCT(73, desc = 'J', fullName = "struct"),
    DBROW(74, desc = 'K', fullName = "dbrow"),
    // Deprecated types below?
    AREA(desc = 'R'),
    CATEGORY(desc = 'y'),
    INTERFACE(desc = 'a'),
    MAPAREA(desc = '`'),
    PARAM,
    STAT(desc = 'S'),
    NEWVAR(desc = '-'),
    NPC_UID(desc = 'u'),
    PLAYER_UID(desc = 'p'),
    TYPE,
    ;

    val desc = desc.toByte(CP1252)

    val stackType get() = if (this == STRING) StackType.STRING else StackType.INT

    val literal = name.lowercase()

    override fun toString() = literal

    companion object {

        private val VALUES = values().filter { it.desc != 0.toByte() }.associateBy { it.desc }

        fun of(desc: Byte): Type = VALUES.getValue(desc)

        fun ofAuto(desc: Byte): Type = if (desc == 0.toByte()) INT else of(desc)

        fun union(types: Set<Type>): Type? {
            when (types.size) {
                0 -> return null
                1 -> return types.iterator().next()
                2 -> {
                    if (OBJ in types && NAMEDOBJ in types) return OBJ
                    if (FONTMETRICS in types && GRAPHIC in types) return FONTMETRICS
                }
            }
            error(types)
        }

        fun intersection(types: Set<Type>): Type? {
            when (types.size) {
                0 -> return null
                1 -> return types.iterator().next()
                2 -> {
                    if (OBJ in types && NAMEDOBJ in types) return NAMEDOBJ
                    if (FONTMETRICS in types && GRAPHIC in types) return GRAPHIC
                }
            }
            error(types)
        }
    }
}
