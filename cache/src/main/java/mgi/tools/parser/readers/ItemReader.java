package mgi.tools.parser.readers;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.tools.parser.TypeParser;
import mgi.tools.parser.TypeProperty;
import mgi.tools.parser.TypeReader;
import mgi.types.Definitions;
import mgi.types.config.items.ItemDefinitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Tommeh | 22/01/2020 | 18:56
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class ItemReader implements TypeReader {
    @Override
    public ArrayList<Definitions> read(final Map<String, Object> properties) throws NoSuchFieldException, IllegalAccessException, InstantiationException {

        final ArrayList<Definitions> defs = new ArrayList<>();

        if (properties.containsKey("inherit")) {
            final Object inherit = properties.get("inherit");
            if (inherit instanceof ArrayList) {
                final ArrayList<Integer> ids = (ArrayList<Integer>) inherit;
                for (final Integer id : ids) {
                    final ItemDefinitions def = ItemDefinitions.get(id);
                    final ItemDefinitions copy = TypeParser.KRYO.copy(def);
                    defs.add(copy);
                }
            } else {
                final ItemDefinitions def = ItemDefinitions.get((int) inherit);
                final ItemDefinitions copy = TypeParser.KRYO.copy(def);
                defs.add(copy);
            }
        } else {
            defs.add(new ItemDefinitions());
        }
        for (final Definitions definition : defs) {
            final ItemDefinitions item = (ItemDefinitions) definition;
            try {
                TypeReader.setFields(item, properties);
            } catch (Exception e) {
                System.err.println("Error parsing item: " + definition +", properties: " + properties);
                throw e;
            }
            for (final TypeProperty property : TypeProperty.values) {
                final String identifier = property.getIdentifier();
                if (!properties.containsKey(identifier)) {
                    continue;
                }
                if (property.toString().startsWith("OP_")) {
                    final int index = Integer.parseInt(identifier.substring(2)) - 1;
                    item.setOption(index, Objects.toString(properties.get(identifier)));
                } else if (property.equals(TypeProperty.PARAMETERS)) {
                    final Map<String, Object> map = new HashMap<>((Map<String, Object>) properties.get(identifier));
                    boolean clear = false;
                    if (map.containsKey("clear")) {
                        clear = (Boolean) map.get("clear");
                    }
                    map.remove("clear");

                    Set<Integer> removeKeys = new HashSet<>();
                    if (map.containsKey("remove")) {
                        for (int key : (ArrayList<Integer>) map.get("remove")) {
                            removeKeys.add(key);
                        }
                    }
                    map.remove("remove");

                    final Int2ObjectOpenHashMap<Object> parameters = new Int2ObjectOpenHashMap<>(map.entrySet().stream()
                            .collect(Collectors.toMap(e -> Integer.parseInt(e.getKey()), Map.Entry::getValue)));
                    if (clear || item.getParameters() == null) {
                        item.setParameters(parameters);
                    } else {
                        for (final Int2ObjectMap.Entry<Object> entry : parameters.int2ObjectEntrySet()) {
                            item.getParameters().put(entry.getIntKey(), entry.getValue());
                        }
                    }
                    for (int key : removeKeys)
                        item.getParameters().remove(key);
                }
            }
        }
        return defs;
    }

    @Override
    public String getType() {
        return "item";
    }
}
