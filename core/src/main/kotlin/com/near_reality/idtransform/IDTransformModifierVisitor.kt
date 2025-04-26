package com.near_reality.idtransform

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.ArrayCreationExpr
import com.github.javaparser.ast.expr.BinaryExpr
import com.github.javaparser.ast.stmt.IfStmt
import com.github.javaparser.ast.visitor.ModifierVisitor
import com.github.javaparser.ast.visitor.Visitable
import com.near_reality.idtransform.NPCIDTransform.npc
import com.near_reality.idtransform.ObjectIDTransform.obj
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.`object`.WorldObject
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.objects.ObjectSet

/**
 * @author Jire
 */
class IDTransformModifierVisitor(
    val cu: CompilationUnit,

    val npcIDToVars: Int2ObjectMap<String>,
    val saveNPC: ObjectSet<CompilationUnit>,

    val objIDToVars: Int2ObjectMap<String>,
    val saveObj: ObjectSet<CompilationUnit>
) : ModifierVisitor<Void>() {

    override fun visit(n: MethodDeclaration, arg: Void?): Visitable {
        if (n.type.isArrayType && n.nameAsString == "getNPCs") {
            val at = n.type.asArrayType()
            if (at.elementType.isPrimitiveType) {
                n.body.ifPresent { block ->
                    block.findAll(ArrayCreationExpr::class.java) {
                        it.elementType.isPrimitiveType
                    }.forEach { ace ->
                        //println(ace)
                        ace.initializer.ifPresent { aie ->
                            aie.values.filter { it.isIntegerLiteralExpr }.forEach expr@{ expr ->
                                val intLiteral = expr.asIntegerLiteralExpr()
                                val oldID = intLiteral.asNumber().toInt()
                                val new = npc(oldID)?.id ?: return@expr
                                //if (oldID == new) return@expr
                                val varName = npcIDToVars.get(new)
                                intLiteral.value = if (varName == null) new.toString() else "NpcId.$varName"
                                println("changed NPC $oldID to $new")
                                saveNPC.add(cu)
                            }
                        }
                    }
                }
            }
        } else if (n.type.isArrayType && n.nameAsString == "getObjects") {
            val at = n.type.asArrayType()
            if (at.elementType.isClassOrInterfaceType) {
                n.body.ifPresent { block ->
                    block.findAll(ArrayCreationExpr::class.java) {
                        it.elementType.isClassOrInterfaceType
                    }.forEach { ace ->
                        ace.initializer.ifPresent { aie ->
                            aie.values.filter { it.isIntegerLiteralExpr }.forEach expr@{ expr ->
                                val intLiteral = expr.asIntegerLiteralExpr()
                                val oldID = intLiteral.asNumber().toInt()
                                val new = obj(oldID)?.id ?: return@expr
                                //if (oldID == new) return@expr
                                val varName = objIDToVars.get(new)
                                intLiteral.value = if (varName == null) new.toString() else "ObjectId.$varName"
                                println("changed OBJ $oldID to $new")
                                saveObj.add(cu)
                            }
                        }
                    }
                }
            }
        }
        return super.visit(n, arg)
    }

    override fun visit(n: IfStmt, arg: Void?): Visitable {
        if (n.condition.isBinaryExpr) {
            val be = n.condition.asBinaryExpr()
            if (be.operator == BinaryExpr.Operator.EQUALS || be.operator == BinaryExpr.Operator.NOT_EQUALS) {
                if (be.left.isMethodCallExpr && be.right.isIntegerLiteralExpr) {
                    val le = be.left.asMethodCallExpr()
                    if ("getId" == le.nameAsString && le.arguments.isEmpty()) {
                        val declaringType = le.resolve().declaringType()
                        if (declaringType.isClass) {
                            val dtC = declaringType.asClass()
                            if (dtC.qualifiedName == NPC::class.java.name) {
                                val intLiteral = be.right.asIntegerLiteralExpr()
                                val oldID = intLiteral.asNumber().toInt()
                                val new = npc(oldID)?.id ?: return super.visit(n, arg)
                                //if (oldID == new) return super.visit(n, arg)
                                val varName = npcIDToVars.get(new)
                                intLiteral.value = if (varName == null) new.toString() else "NpcId.$varName"
                                println("IF-STMT changed NPC $oldID to $new")
                                saveNPC.add(cu)
                            } else if (dtC.qualifiedName == WorldObject::class.java.name) {
                                val intLiteral = be.right.asIntegerLiteralExpr()
                                val oldID = intLiteral.asNumber().toInt()
                                val new = obj(oldID)?.id ?: return super.visit(n, arg)
                                //if (oldID == new) return super.visit(n, arg)
                                val varName = objIDToVars.get(new)
                                intLiteral.value = if (varName == null) new.toString() else "ObjectId.$varName"
                                println("IF-STMT changed obj $oldID to $new")
                                saveObj.add(cu)
                            }
                        }
                    }
                }
                //println("left=${be.left}(${be.left.javaClass}), right=${be.right}(${be.right.javaClass})")
            }
        }
        return super.visit(n, arg)
    }

}