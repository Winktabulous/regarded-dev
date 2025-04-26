package com.near_reality.game.world.entity.npc.spawns

import com.github.javaparser.ast.Modifier.Keyword
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.visitor.ModifierVisitor
import com.github.javaparser.ast.visitor.Visitable
import com.github.javaparser.utils.CodeGenerationUtils.mavenModuleRoot
import com.github.javaparser.utils.SourceRoot
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.spawns.NPCSpawn
import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.io.File
import java.io.File.separatorChar

/**
 * Generates KTS files based off the old [NPCSpawn]s (which were from `spawns.json`)
 *
 * @author Jire
 */
class OldSpawnKTSGenerator @JvmOverloads constructor(
    val spawns: Collection<NPCSpawn> = NPCSpawnLoader.DEFINITIONS,

    val targetClass: Class<*> = DEFAULT_TARGET_CLASS,

    val generatedPackageName: String = DEFAULT_GENERATED_PACKAGE_NAME,
    val generatedPackageDirectoryPath: String = generatedPackageName.replace('.', separatorChar) + separatorChar
) {

    private val idToVars: Int2ObjectMap<String> by lazy { idToVars(targetClass) }

    fun generateKTS(): Int2ObjectMap<String> {
        val regionToSB: Int2ObjectMap<StringBuilder> = Int2ObjectOpenHashMap()

        for (spawn in spawns) {
            val regionID = Location.getRegionId(spawn.x, spawn.y)

            var sb = regionToSB.get(regionID)
            if (sb == null) {
                sb = StringBuilder("package $generatedPackageName\n")
                regionToSB.put(regionID, sb)
            }

            sb.appendSpawn(spawn)
        }

        val regionToKTS: Int2ObjectMap<String> = Int2ObjectOpenHashMap(regionToSB.size)

        for ((regionID, sb) in regionToSB.int2ObjectEntrySet()) {
            regionToKTS.put(regionID, sb.toString())
        }

        return regionToKTS
    }

    @JvmOverloads
    fun generateAndWriteKTS(
        rootDirectoryPath: String = DEFAULT_ROOT_DIRECTORY_PATH,
        filePrefix: String = DEFAULT_FILE_PREFIX,
        fileSuffix: String = DEFAULT_FILE_SUFFIX,
    ) {
        val rootDirectory = File(rootDirectoryPath)
        check(rootDirectory.exists()) { "Root directory path must exist!" }
        check(rootDirectory.isDirectory) { "Root directory path must be a directory!" }

        val generatedPackageDirectory = File(rootDirectory, generatedPackageDirectoryPath)

        val regionToKTS = generateKTS()

        for ((regionID, kts) in regionToKTS.int2ObjectEntrySet()) {
            val child = "$filePrefix$regionID$fileSuffix"
            val file = File(generatedPackageDirectory, child)
            file.writeText(kts)
        }
    }

    private fun StringBuilder.appendSpawn(spawn: NPCSpawn) = spawn.run {
        append('\n')

        val idVar = idToVars.get(id)
        if (idVar == null) append(id)
        else append(idVar)

        append('(')

        append(x)

        operator fun Any.invoke() = append(',').append(' ').append(this)

        y()
        z()
        direction.name()
        radius()

        append(')')
    }

    companion object {
        val DEFAULT_TARGET_CLASS = NpcId::class.java
        const val DEFAULT_GENERATED_PACKAGE_NAME = "com.near_reality.plugins.spawns"

        const val DEFAULT_ROOT_DIRECTORY_PATH = "plugins/spawns/src/main/kotlin/"

        const val DEFAULT_FILE_PREFIX = "region"
        const val DEFAULT_FILE_SUFFIX = ".spawns.kts"

        @JvmStatic
        fun idToVars(targetClass: Class<*>): Int2ObjectMap<String> {
            val packageName = targetClass.packageName
            val className = targetClass.simpleName
            val classFileName = "${className}.java"

            val map: Int2ObjectMap<String> = Int2ObjectOpenHashMap()

            val sr = SourceRoot(mavenModuleRoot(targetClass).resolve("../src/main/java/").normalize())
            val cu = sr.parse(packageName, classFileName)

            val requiredKeywords = listOf(Keyword.PUBLIC, Keyword.STATIC, Keyword.FINAL)

            cu.accept(object : ModifierVisitor<Void>() {
                override fun visit(n: FieldDeclaration, arg: Void?): Visitable {
                    val mods = n.modifiers
                    val keywords = mods.map { it.keyword }
                    for (keyword in requiredKeywords)
                        if (!keywords.contains(keyword))
                            return super.visit(n, arg)

                    val vars = n.variables
                    if (vars.size == 1) {
                        val v = vars[0]
                        v.initializer.ifPresent {
                            if (it.isIntegerLiteralExpr) {
                                val intExpr = it.asIntegerLiteralExpr()
                                val int = intExpr.asNumber().toInt()
                                map.put(int, v.nameAsString)
                            }
                        }
                    }
                    return super.visit(n, arg)
                }
            }, null)

            return map
        }
    }

}