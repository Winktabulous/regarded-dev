package com.near_reality.idtransform

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.symbolsolver.JavaSymbolSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver
import com.github.javaparser.utils.CodeGenerationUtils
import com.github.javaparser.utils.SourceRoot
import com.near_reality.game.world.entity.npc.spawns.OldSpawnKTSGenerator
import com.near_reality.idtransform.NPCIDTransform.new
import com.near_reality.idtransform.NPCIDTransform.oldNPCs
import com.near_reality.idtransform.ObjectIDTransform.new
import com.near_reality.idtransform.ObjectIDTransform.oldObjects
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.`object`.ObjectId
import com.zenyte.plugins.PluginScanner
import com.zenyte.plugins.renewednpc.ShopNPCHandler
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import net.runelite.cache.fs.Store
import java.io.File

/**
 * @author Jire
 */
object IDTransform {

    val old by lazy {
        Store(File("cache/data/cache-179")).apply { load() }
    }

    val new by lazy {
        Store(File("cache/data/cache")).apply { load() }
    }

    fun sourceRoot(): SourceRoot {
        val srPath = CodeGenerationUtils
            .mavenModuleRoot(PluginScanner::class.java)
            .resolve("../src/main/java/").normalize()

        val tr = CombinedTypeSolver().apply {
            add(ReflectionTypeSolver(false))
            add(JavaParserTypeSolver(srPath))
        }
        val jsr = JavaSymbolSolver(tr)
        StaticJavaParser.getConfiguration().setSymbolResolver(jsr)

        return SourceRoot(srPath).apply {
            parserConfiguration.setSymbolResolver(jsr)
        }
    }

    fun transform(sr: SourceRoot = sourceRoot()) {
        val npcIDToVars = OldSpawnKTSGenerator.idToVars(NpcId::class.java)
        val objIDToVars = OldSpawnKTSGenerator.idToVars(ObjectId::class.java)

        transformShops(sr, npcIDToVars)

        val results = sr.tryToParseParallelized("")
        //println("results: ${results.size}")

        val saveNPC: ObjectSet<CompilationUnit> = ObjectOpenHashSet(results.size)
        val saveObj: ObjectSet<CompilationUnit> = ObjectOpenHashSet(results.size)

        for (result in results) result.ifSuccessful { cu ->
            val visitor = IDTransformModifierVisitor(cu, npcIDToVars, saveNPC, objIDToVars, saveObj)
            cu.accept(visitor, null)
        }

        //sr.saveAll()
        for (cu in saveNPC) cu.storage.ifPresent {
            cu.addImport(NpcId::class.java)
            it.save()
        }
        for (cu in saveObj) cu.storage.ifPresent {
            cu.addImport(ObjectId::class.java)
            it.save()
        }
    }

    fun transformShops(
        sr: SourceRoot, npcIDToVars: Int2ObjectMap<String>,
        startPackage: String = ShopNPCHandler::class.java.packageName,
        fileName: String = "${ShopNPCHandler::class.java.simpleName}.java"
    ) = sr.tryToParse(startPackage, fileName).ifSuccessful { cu ->
        val visitor = ShopIDTransformModifierVisitor(npcIDToVars)
        cu.accept(visitor, null)
        cu.storage.ifPresent {
            cu.addImport(NpcId::class.java)
            it.save()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        var matches = 0

        for (oldNPC in oldNPCs.npcs) {
            @Suppress("UNUSED_VARIABLE") val newNPC = oldNPC.new() ?: continue
            //println("Matched ${oldNPC.id} (\"${oldNPC.name}\") with ${newNPC.id} (\"${newNPC.name}\")")
            matches++
        }

        println("NPCS matched $matches / ${oldNPCs.npcs.size}")
        matches = 0

        for (oldObject in oldObjects.objects) {
            @Suppress("UNUSED_VARIABLE") val newObject = oldObject.new() ?: continue
            //println("Matched ${oldObject.id} (\"${oldObject.name}\") with ${newObject.id} (\"${newObject.name}\")")
            matches++
        }

        println("Objects matched $matches / ${oldObjects.objects.size}")
    }

}