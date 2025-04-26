package com.zenyte.plugins

import com.google.common.eventbus.Subscribe
import com.near_reality.game.world.entity.player.PlayerActionPlugin
import com.zenyte.cores.ScheduledExternalizable
import com.zenyte.cores.ScheduledExternalizableManager
import com.zenyte.game.content.skills.agility.AgilityCourse
import com.zenyte.game.content.skills.agility.AgilityCourseManager
import com.zenyte.game.content.skills.magic.Magic
import com.zenyte.game.content.skills.magic.spells.MagicSpell
import com.zenyte.game.model.item.*
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.model.ui.ButtonAction
import com.zenyte.game.model.ui.Interface
import com.zenyte.game.model.ui.NewInterfaceHandler
import com.zenyte.game.model.ui.UserInterface
import com.zenyte.game.world.entity.npc.AbstractNPCManager
import com.zenyte.game.world.entity.npc.Spawnable
import com.zenyte.game.world.entity.npc.actions.NPCHandler
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessorLoader
import com.zenyte.game.world.`object`.ObjectAction
import com.zenyte.game.world.`object`.ObjectHandler
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.game.world.region.RegionArea
import com.zenyte.plugins.PluginScanTypes.*
import com.zenyte.plugins.equipment.EquipmentPlugin
import com.zenyte.plugins.equipment.EquipmentPluginLoader
import com.zenyte.plugins.equipment.equip.EquipPlugin
import com.zenyte.plugins.equipment.equip.EquipPluginLoader
import com.zenyte.plugins.flooritem.FloorItemPlugin
import com.zenyte.plugins.flooritem.FloorItemPluginLoader
import com.zenyte.plugins.handlers.InterfaceSwitchHandler
import com.zenyte.plugins.handlers.InterfaceSwitchPlugin
import com.zenyte.utils.StaticInitializer
import io.github.classgraph.ScanResult
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

/**
 * @author Jire
 */
enum class PluginType(
    val id: Int,
    val scanType: PluginScanType,
    val scanClass: KClass<*>,
    val pluginTypeLoader: PluginTypeLoader?,
    val initializeClass: Boolean = false
) {

    STATIC_INITIALIZER(1, ANNOTATED, StaticInitializer::class, null, true),
    INIT(2, IMPLEMENTING, InitPlugin::class, InitPluginLoader),
    SUBSCRIBER(3, METHOD_ANNOTATED, Subscribe::class, {
        val methods = it.declaredMethods
        for (method in methods) {
            if (method.isAnnotationPresent(Subscribe::class.java) && Modifier.isStatic(method.modifiers)) {
                PluginManager.register(it, method)
            }
        }
    }),
    LISTENER(4, METHOD_ANNOTATED, Listener::class, {
        val methods = it.declaredMethods
        for (method in methods) {
            if (method.isAnnotationPresent(Listener::class.java)) {
                MethodicPluginHandler.register(it, method)
            }
        }
    }),

    AGILITY_COURSE(5, IMPLEMENTING, AgilityCourse::class, AgilityCourseManager::initUnsafe),

    OBJECT(6, IMPLEMENTING, ObjectAction::class, ObjectHandler::add),
    ITEM_ON_OBJECT(7, IMPLEMENTING, ItemOnObjectAction::class, ItemOnObjectHandler::add),
    ITEM_ON_NPC(8, IMPLEMENTING, ItemOnNPCAction::class, ItemOnNPCHandler::add),
    ITEM_ON_ITEM(9, IMPLEMENTING, ItemOnItemAction::class, ItemOnItemHandler::add),
    ITEM_ON_PLAYER(10, IMPLEMENTING, ItemOnPlayerPlugin::class, ItemOnPlayerHandler::addUnsafe),
    ITEM_ON_FLOOR_ITEM(11, IMPLEMENTING, ItemOnFloorItemAction::class, ItemOnFloorItemHandler::add),
    USER_INTERFACE(12, IMPLEMENTING, UserInterface::class, ButtonAction::add),
    INTERFACE_SWITCH(13, IMPLEMENTING, InterfaceSwitchPlugin::class, InterfaceSwitchHandler::addUnsafe),
    FLOOR_ITEM(14, IMPLEMENTING, FloorItemPlugin::class, FloorItemPluginLoader::add),
    EQUIP(15, IMPLEMENTING, EquipPlugin::class, EquipPluginLoader::add),
    SCHEDULED_EXTERNALIZABLE(
        16,
        IMPLEMENTING,
        ScheduledExternalizable::class,
        ScheduledExternalizableManager::addUnsafe
    ),
    SPAWNABLE(17, IMPLEMENTING, Spawnable::class, AbstractNPCManager::addUnsafe),
    MAGIC_SPELL(18, IMPLEMENTING, MagicSpell::class, Magic::addUnsafe),

    DROP_PROCESSOR(19, SUPERCLASS, DropProcessor::class, DropProcessorLoader::add),
    NPC(20, SUPERCLASS, NPCPlugin::class, NPCHandler::add),
    ITEM(21, SUPERCLASS, ItemPlugin::class, ItemActionHandler::add),
    EQUIPMENT(22, SUPERCLASS, EquipmentPlugin::class, EquipmentPluginLoader::add),
    AREA(23, AreaPluginScanType, RegionArea::class, GlobalAreaManager::addUnsafe),
    INTERFACE(24, SUPERCLASS, Interface::class, NewInterfaceHandler::addUnsafe),
    PLAYER_ACTION(25, SUPERCLASS, PlayerActionPlugin::class, PlayerActionPlugin::add)
    ;

    fun scan(result: ScanResult) = scanType.scan(result, scanClass.java)

    fun loadClasses(classes: Collection<Class<*>>) = pluginTypeLoader?.loadClasses(classes)

    companion object {

        @JvmStatic
        val values = values()

        private val idToValue: Int2ObjectMap<PluginType> = Int2ObjectOpenHashMap(values.size)

        init {
            for (value in values) {
                val id = value.id
                val inUseBy = idToValue.put(id, value)
                if (inUseBy != null) {
                    throw IllegalArgumentException("$value cannot override ID $id, already in use by $inUseBy")
                }
            }
        }

        @JvmStatic
        fun forID(id: Int): PluginType? = idToValue.get(id)

    }

}
