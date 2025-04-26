package mgi.types.config;

import com.google.common.base.Preconditions;
import com.zenyte.CacheManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

/**
 * @author Kris | 14/01/2019 00:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class StructDefinitions implements Definitions, Cloneable {
    private static final Logger log = LoggerFactory.getLogger(StructDefinitions.class);
    public static StructDefinitions[] definitions;

    public static StructDefinitions get(final int id) {
        if (definitions[id] == null) {
            log.error("Unable to find struct {}", id);
            throw new IllegalArgumentException();
        }
        return definitions[id];
    }

    private StructDefinitions(final int id, final ByteBuffer buffer) {
        this.id = id;
        decode(buffer);
    }

    private final int id;
    private Int2ObjectMap<Object> parameters;

    @Override
    public void load() {
        final Cache cache = CacheManager.getCache();
        final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
        final Group structs = configs.findGroupByID(GroupType.STRUCT);
        definitions = new StructDefinitions[structs.getHighestFileId()];
        for (int id = 0; id < structs.getHighestFileId(); id++) {
            final File file = structs.findFileByID(id);
            if (file == null) {
                continue;
            }
            final ByteBuffer buffer = file.getData();
            if (buffer == null) {
                continue;
            }
            if (buffer.remaining() < 1) {
                continue;
            }
            definitions[id] = new StructDefinitions(id, buffer);
        }
    }

    public StructDefinitions clone() throws CloneNotSupportedException {
        return (StructDefinitions) super.clone();
    }

    @Override
    public void decode(final ByteBuffer buffer) {
        while (true) {
            final int opcode = buffer.readUnsignedByte();
            if (opcode == 0) {
                return;
            }
            decode(buffer, opcode);
        }
    }

    public final Optional<?> getValue(final int id) {
        return Optional.ofNullable(parameters.get(id));
    }

    public final int getParamAsInt(final int id) {
        Optional<?> value = getValue(id);
        if (value.isEmpty()) {
            ParamDefinitions param = ParamDefinitions.get(id);
            Preconditions.checkNotNull(param);
            return param.getDefaultInt();
        }
        Object type = value.get();
        if (!(type instanceof Integer)) {
            throw new IllegalArgumentException("Param " + id + " is not of type 'int'.");
        }
        return (Integer) type;
    }

    public final boolean getParamAsBoolean(final int id) {
        Optional<?> value = getValue(id);
        if (value.isEmpty()) {
            ParamDefinitions param = ParamDefinitions.get(id);
            Preconditions.checkNotNull(param);
            return param.getDefaultInt() == 1;
        }
        Object type = value.get();
        if (!(type instanceof Integer)) {
            throw new IllegalArgumentException("Param " + id + " is not of type 'int'.");
        }
        return (Integer) type == 1;
    }

    public final String getParamAsString(final int id) {
        Optional<?> value = getValue(id);
        if (value.isEmpty()) {
            ParamDefinitions param = ParamDefinitions.get(id);
            Preconditions.checkNotNull(param);
            return param.getDefaultString();
        }
        Object type = value.get();
        if (!(type instanceof String)) {
            throw new IllegalArgumentException("Param " + id + " is not of type 'string'.");
        }
        return (String) type;
    }

    @Override
    public void decode(final ByteBuffer buffer, int opcode) {
        if (opcode == 249) {
            parameters = buffer.readParameters();
            return;
        }
        throw new IllegalStateException("Opcode: " + opcode);
    }

    @Override
    public ByteBuffer encode() {
        final ByteBuffer buffer = new ByteBuffer(1132);
        if (parameters != null && !parameters.isEmpty()) {
            buffer.writeByte(249);
            buffer.writeParameters(parameters);
        }
        buffer.writeByte(0);
        return buffer;
    }

    @Override
    public void pack() {
        CacheManager.getCache().getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.STRUCT).addFile(new File(id, encode()));
    }

    public StructDefinitions(int id) {
        this.id = id;
    }

    public StructDefinitions() {
        this(0);
    }

    public int getId() {
        return id;
    }

    public Int2ObjectMap<Object> getParameters() {
        return parameters;
    }

    public void setParameters(Int2ObjectMap<Object> parameters) {
        this.parameters = parameters;
    }

    public String printParams() {
        StringBuilder s = new StringBuilder();
        for(Int2ObjectMap.Entry<?> entry: parameters.int2ObjectEntrySet()) {
            s.append(entry.getIntKey());
            s.append(" : ");
            s.append(entry.getValue());
            s.append(";  ");
        }
        return s.toString();
    }

    public StructDefinitions copy(int newId) {
        StructDefinitions newDefs = new StructDefinitions(newId);
        newDefs.setParameters(this.getParameters());
        return newDefs;
    }
}
