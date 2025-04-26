package com.zenyte.game.world.entity.npc.actions;

import com.google.gson.GsonBuilder;
import com.zenyte.CacheManager;
import com.zenyte.ContentConstants;
import com.zenyte.Main;
import com.zenyte.cores.CoresManager;
import com.zenyte.game.GameConstants;
import com.zenyte.game.content.skills.thieving.PocketData;
import com.zenyte.game.content.skills.thieving.actions.Pickpocket;
import com.zenyte.game.world.entity.ImmutableLocation;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawn;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader;
import com.zenyte.game.world.entity.pathfinding.events.player.EntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.plugins.DynamicPluginLoader;
import com.zenyte.plugins.Plugin;
import com.zenyte.plugins.PluginType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import kotlin.Unit;
import mgi.tools.parser.TypeParser;
import mgi.tools.parser.readers.NPCReader;
import mgi.types.Definitions;
import mgi.types.config.npcs.NPCDefinitions;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author Kris | 24/11/2018 21:45
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("WeakerAccess")
public abstract class NPCPlugin implements Plugin {
    private static final Logger log = LoggerFactory.getLogger(NPCPlugin.class);
    private static final Map<String, NPCPluginHandler> handlerMap = new HashMap<>();
    private static final Map<String, NPCPluginHandler> defaultHandlerMap = new HashMap<>();

    public static void main(String[] args) {
        Main.configureWorldProfile();
        CoresManager.init();

        CacheManager.loadDetached();

        Definitions.loadDefinitions(Definitions.lowPriorityDefinitions);

        TypeParser.initializeKryo();
        TypeParser.parse(new File("cache/assets/types"), false, new NPCReader());
        TypeParser.pack(NPCDefinitions.class);

        new NPCDefinitions().load();
        NPCPlugin.verifyOptionExists = false;

        DynamicPluginLoader.load(PluginType.NPC);

        Int2ObjectMap<List<String>> options = loadUsedNpcOptions();

        final File file =
                Paths.get(args.length == 0 ? "cache/" + NpcActions.DEFAULT_OPTIONS_JSON_PATH : args[0]).toFile();
        try {
            final FileWriter writer = new FileWriter(file);
            new GsonBuilder().setPrettyPrinting().create().toJson(options, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(0);
    }


    private static Int2ObjectMap<List<String>> loadUsedNpcOptions() {
        Int2ObjectMap<List<String>> optionsMap = new Int2ObjectOpenHashMap<>(10 * 1024);
        for (NPCDefinitions npc : NPCDefinitions.getDefinitions()) {
            if (npc == null) continue;
            final List<String> list = optionsMap.computeIfAbsent(npc.getId(), n -> new ArrayList<>());
            final String[] options = npc.getOptions();
            for (final String option : options) {
                if (option == null) {
                    list.add(null);
                    continue;
                }
                final NPCPlugin.NPCPluginHandler plugin = NPCPlugin.getHandler(npc.getId(), option);
                list.add(plugin == null ? null : option);
            }
        }
        return optionsMap;
    }

    static {
        final NPCPlugin.OptionHandler handler = new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                if (npc.isAttackable(player)) {
                    PlayerCombat.attackEntity(player, npc, null);
                }
            }

            @Override
            public void click(Player player, NPC npc, final NPCOption option) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        };
        setDefault("Charge", handler);
        setDefault("Attack", handler);
        setDefault("Destroy", handler);
        setDefault("Disturb", handler);
        setDefault("Pickpocket", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.getActionManager().setAction(new Pickpocket(PocketData.getData(player, npc), npc));
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                handle(player, npc);
            }
        });
        setDefault("Remove", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                npc.finish();
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                handle(player, npc);
            }
        });
        setDefault("Teleport to me", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                npc.setLocation(new Location(player.getLocation()));
                npc.setRespawnTile(new ImmutableLocation(npc.getX(), npc.getY(), npc.getPlane()));
                final NPCSpawn spawn = npc.getNpcSpawn();
                spawn.setX(player.getX());
                spawn.setY(player.getY());
                NPCSpawnLoader.save();
                //npc.finish();
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                handle(player, npc);
            }
        });
        setDefault("Set radius", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                final NPCSpawn spawn = npc.getNpcSpawn();
                player.sendInputInt("Enter radius(Current: " + spawn.getRadius() + ")", value -> {
                    spawn.setRadius(value);
                    NPCSpawnLoader.save();
                });
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                handle(player, npc);
            }
        });
        setDefault("Remove spawn", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                player.stopAll();
                player.faceEntity(npc);
                final NPCSpawn spawn = npc.getNpcSpawn();
                NPCSpawnLoader.DEFINITIONS.remove(spawn);
                npc.finish();
                NPCSpawnLoader.save();
                //npc.finish();
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                handle(player, npc);
            }
        });
    }

    public static NPCPluginHandler getHandler(final int npcId, final String option) {
        final String op = option.toLowerCase();
        final NPCPlugin.NPCPluginHandler handler = handlerMap.get(npcId + "|" + op);
        if (handler != null) {
            return handler;
        }
        return defaultHandlerMap.get(op);
    }

    private static void setDefault(final String option, final OptionHandler handler) {
        defaultHandlerMap.put(option.toLowerCase(), new NPCPluginHandler(null, handler));
    }

    public static void filter() {
        if (/*Constants.WORLD_PROFILE.isDevelopment() && */!ContentConstants.SPAWN_MODE) return;
        log.info("Filtering definitions.");
        for (final NPCDefinitions definition : NPCDefinitions.getDefinitions()) {
            if (definition == null) continue;
            for (int i = 0; i < 5; i++) {
                final String option = definition.getFilteredOptions()[i];
                if (ContentConstants.SPAWN_MODE) {
                    if (i == 0) {
                        definition.setFilteredOption(i, "Teleport to me");
                        continue;
                    }
                    if (i == 1) {
                        definition.setFilteredOption(i, "Set radius");
                        continue;
                    }
                    if (i == 4) {
                        definition.setFilteredOption(i, "Remove spawn");
                        continue;
                    }
                }
                if (option == null) continue;
                if (getHandler(definition.getId(), option) == null) {
                    definition.setFilteredOption(i, null);
                }
            }
        }
    }

    public abstract void handle();

    private int[] cachedNPCs;

    private int[] getCachedNPCs() {
        return cachedNPCs == null ? cachedNPCs = getNPCs() : cachedNPCs;
    }

    public static boolean verifyOptionExists = false;

    public void bind(final String option, final OptionHandler handler, boolean verifyOptionExists) {
        if (verifyOptionExists) verifyIfOptionExists(option);
        registerHandler(option, handler);
    }

    public void bind(final String option, final OptionHandler handler) {
        bind(option, handler, verifyOptionExists);
    }

    public void bindOptions(final Predicate<String> optionPredicate, final ExtendedOptionHandler handler) {
        final HashSet<String> uniqueOptions = new HashSet<>();
        for (final int i : getCachedNPCs()) {
            final NPCDefinitions definitions = NPCDefinitions.get(i);
            if(definitions == null)
                continue;
            for (final String op : definitions.getOptions()) {
                if (op == null || !optionPredicate.test(op)) continue;
                uniqueOptions.add(op);
            }
        }
        uniqueOptions.forEach(option -> registerHandler(option, handler));
    }

    private void registerHandler(String option, OptionHandler handler) {
        option = option.toLowerCase();
        for (int i : getCachedNPCs()) {
            if (handlerMap.containsKey(i + "|" + option)) {
                log.warn("Overlapping handler found for option: " + option + ", " + getClass().getSimpleName() + ", " + handlerMap.get(i + "|" + option)
                        .plugin.getClass().getSimpleName());
            }
            handlerMap.put(i + "|" + option, new NPCPluginHandler(this, handler));
        }

    }

    private void verifyIfOptionExists(final String option) {
        final int[] npcs = getCachedNPCs();
        if (npcs.length == 0) {
            return;
        }
        for (final int id : npcs) {
            final NPCDefinitions definitions = NPCDefinitions.get(id);
            if (definitions == null) {
                continue;
            }
            if (definitions.containsOption(option)) return;
        }
        throw new RuntimeException("None of the npcs enlisted in " + getClass().getSimpleName() + " contains option " + option + ".");
    }

    public abstract int[] getNPCs();


    @FunctionalInterface
    public interface OptionHandler {
        void handle(final Player player, final NPC npc);

        default void click(final Player player, final NPC npc, final NPCOption option) {
            player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc), () -> execute(player, npc), true));
        }

        default void execute(final Player player, final NPC npc) {
            player.stopAll();
            player.setFaceEntity(npc);
            handle(player, npc);
            if (npc.getRadius() > 0)
                npc.setInteractingWith(player);
        }
    }


    @FunctionalInterface
    public interface ExtendedOptionHandler extends OptionHandler {
        @Override
        default void click(final Player player, final NPC npc, final NPCOption option) {
            player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc), () -> execute(player, npc, option),
                    true));
        }

        default void execute(final Player player, final NPC npc, final NPCOption option) {
            player.stopAll();
            player.setFaceEntity(npc);
            handle(player, npc, option);
            if (npc.getRadius() > 0)
                npc.setInteractingWith(player);
        }

        @Deprecated
        default void handle(final Player player, final NPC npc) {
        }

        void handle(final Player player, final NPC npc, final NPCOption option);

        @Deprecated
        default void execute(final Player player, final NPC npc) {
        }
    }


    public static final class NPCOption {
        private final int id;
        private final String option;

        public NPCOption(int id, String option) {
            this.id = id;
            this.option = option;
        }

        public int getId() {
            return id;
        }

        public String getOption() {
            return option;
        }
    }


    public static class NPCPluginHandler {
        private final NPCPlugin plugin;
        private final OptionHandler option;

        public NPCPluginHandler(NPCPlugin plugin, OptionHandler option) {
            this.plugin = plugin;
            this.option = option;
        }

        public NPCPlugin getPlugin() {
            return plugin;
        }

        public OptionHandler getOption() {
            return option;
        }
    }

}
