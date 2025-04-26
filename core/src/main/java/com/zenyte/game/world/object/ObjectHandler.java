package com.zenyte.game.world.object;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mgi.types.config.ObjectDefinitions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ObjectHandler {
    ;

    private static final Logger log = LoggerFactory.getLogger(ObjectHandler.class);
    private static final Int2ObjectMap<ObjectAction> idToAction = new Int2ObjectOpenHashMap<>();
    private static final Object2ObjectMap<String, ObjectAction> nameToAction = new Object2ObjectOpenHashMap<>();

    /**
     * A temporary method for until we rewrite region object management to allow up to 5 objects per location. This
     * fixes the generic gourd tree issue for now.
     */
    public static WorldObject verifyObject(@NotNull final Location tile, final int id) {
        final WorldObject object = World.getObjectWithId(tile, id);
        if (object == null && id == 29772) {
            final WorldObject overlappingObject = World.getObjectWithType(tile, 9);
            if (overlappingObject != null && overlappingObject.getId() == 29781) {
                return new WorldObject(id, 10, 1, tile);
            }
        }
        return object;
    }

    public static void handle(final Player player, final int id, final Location tile, final boolean forcerun,
                              final int option) {
        final WorldObject object = verifyObject(tile, id);
        if (object == null || player.isLocked() || object.isLocked()) {
            return;
        }
        final ObjectDefinitions defs = object.getDefinitions();
        final int transformedId = player.getTransmogrifiedId(defs, object.getId());
        final ObjectDefinitions transformedDefinitions = Utils.getOrDefault(ObjectDefinitions.get(transformedId), defs);
        final String op = transformedDefinitions.getOption(option);
        final String name = transformedDefinitions.getName();
        ObjectAction action = idToAction.get(id);
        if (action == null) {
            action = nameToAction.get(name.toLowerCase());
        }

        if (action == null) {
            if (object instanceof ObjectAction) {
                action = (ObjectAction) object;
            }
        }

        final boolean isDebugEnabled = log.isDebugEnabled();
        if (isDebugEnabled && action == null) {
            log.debug("[" + name + "], id=" + transformedId + (transformedId != id ? ("(real id: " + id + ")") : "") + ", type=" + object.getType() + ", rotation=" + object.getRotation() + ", option=" + (op == null ? "null" : op) + "(" + option + "), tile=" + object.getX() + ", " + object.getY() + ", " + object.getPlane() + ", varbit=" + defs.getVarbit() + ", varp=" + defs.getVarp());
        }
        player.setFacedInteractableEntity(object);
        player.stopAll();
        player.getPacketDispatcher().sendMapFlag(player.getXInScene(object), player.getYInScene(object));
        if (forcerun && player.getPrivilege().eligibleTo(PlayerPrivilege.ADMINISTRATOR)) {
            // player.setNextWorldTile(new WorldTile(object));
            if (isDebugEnabled)
                log.debug("[" + name + "], id=" + transformedId + (transformedId != id ? ("(real id: " + id + ")") :
                        "") + ", type=" + object.getType() + ", rotation=" + object.getRotation() + ", option=" + (op == null ? "null" : op) + "(" + option + "), tile=" + object.getX() + ", " + object.getY() + ", " + object.getPlane());
            player.sendMessage("Object: <col=C22731>" + object.getName() + "</col> - <col=C22731>" + object.getId() + "</col>, coords: " + object.getX() + ", " + object.getY() + ", " + object.getPlane()+", face: "+object.getRotation());
            return;
        } else if (forcerun) {
            player.setRun(true);
        }
        if (isDebugEnabled && action != null) {
            log.debug("[" + name + ", " + action.getClass().getSimpleName() + "], id=" + transformedId + (transformedId != id ? "(real id: " + id + ")" : "") + ", type=" + object.getType() + ", rotation=" + object.getRotation() + ", option=" + (op == null ? "null" : op) + "(" + option + "), tile=" + object.getX() + ", " + object.getY() + ", " + object.getPlane() + ", varbit=" + defs.getVarbit() + ", varp=" + defs.getVarp());
        }
        if (action == null) {
            player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), () -> {
                if (World.getObjectWithId(object, object.getId()) == null || player.getPlane() != object.getPlane()) {
                    return;
                }
                player.stopAll();
                player.faceObject(object);
                if (!handleOptionClick(player, option, object)) {
                    return;
                }
                player.sendMessage("Nothing interesting happens.");
            }));
            return;
        }
        action.handle(player, object, name, option, op == null ? "null" : op);
    }

    public static boolean handleOptionClick(final Player player, final int option, final WorldObject object) {
        if (option == 1) {
            return player.getControllerManager().processObjectClick1(object);
        } else if (option == 2) {
            return player.getControllerManager().processObjectClick2(object);
        } else if (option == 3) {
            return player.getControllerManager().processObjectClick3(object);
        } else if (option == 4) {
            return player.getControllerManager().processObjectClick4(object);
        } else if (option == 5) {
            return player.getControllerManager().processObjectClick5(object);
        }
        return true;
    }

    public static void add(final Class<?> c) {
        final boolean isErrorEnabled = log.isErrorEnabled();
        try {
            final ObjectAction action = (ObjectAction) c.getDeclaredConstructor().newInstance();
            action.init();
            for (final Object object : action.getObjects()) {
                final ObjectAction previous = object instanceof String
                        ? nameToAction.put(((String) object).toLowerCase(), action)
                        : idToAction.put((int) object, action);
                if (previous != null && isErrorEnabled) {
                    log.error("OVERLAPPING object handler: {}, {}", previous.getClass().getSimpleName(),
                            action.getClass().getSimpleName());
                }
            }
        } catch (final Exception e) {
            if (isErrorEnabled)
                log.error("", e);
        }
    }

    public static ObjectAction getPlugin(final int id) {
        return idToAction.get(id);
    }

    public static ObjectAction getPlugin(String name) {
        return nameToAction.get(name.toLowerCase());
    }

}
