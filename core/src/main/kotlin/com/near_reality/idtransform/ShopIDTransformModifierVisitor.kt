package com.near_reality.idtransform

import com.github.javaparser.ast.expr.IntegerLiteralExpr
import com.github.javaparser.ast.visitor.ModifierVisitor
import com.github.javaparser.ast.visitor.Visitable
import com.near_reality.idtransform.NPCIDTransform.npc
import it.unimi.dsi.fastutil.ints.Int2ObjectMap

/**
 * @author Jire
 */
class ShopIDTransformModifierVisitor(
    val npcIDToVars: Int2ObjectMap<String>
) : ModifierVisitor<Void>() {

    override fun visit(n: IntegerLiteralExpr, arg: Void?): Visitable {
        val oldID = n.asNumber().toInt()
        val new = npc(oldID)?.id
        if (new == null) {
            println("NOPE for $oldID")
            return super.visit(n, arg)
        }
        //if (oldID == new) return@forEach
        val varName = npcIDToVars.get(new)
        n.value = if (varName == null) new.toString() else "NpcId.$varName"
        println("ENUM CHANGE NPC $oldID to $new")
        return super.visit(n, arg)
    }

}