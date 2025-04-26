package mgi.types.clientscript;

import com.zenyte.CacheManager;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;

import java.util.Arrays;

/**
 * @author Tommeh | 11/02/2020 | 11:46
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class ClientScriptDefinitions implements Definitions {
    
    private static ClientScriptDefinitions[] definitions;

    @Override
    public void load() {
        final Cache cache = CacheManager.getCache();
        final Archive scripts = cache.getArchive(ArchiveType.CLIENTSCRIPTS);
        definitions = new ClientScriptDefinitions[scripts.getHighestGroupId()];
        for (int id = 0; id < scripts.getHighestGroupId(); id++) {
            final Group scriptGroup = scripts.findGroupByID(id);
            if (scriptGroup == null) {
                continue;
            }
            final File file = scriptGroup.findFileByID(0);
            if (file == null) {
                continue;
            }
            final ByteBuffer buffer = file.getData();
            if (buffer == null) {
                continue;
            }
            buffer.setPosition(0);
            definitions[id] = new ClientScriptDefinitions(id, buffer);
        }
    }

    public int id;
    private int intArgumentCount;
    private int stringArgumentCount;
    public String[] stringArgs;
    public int[] instructions;
    private int intArgsCount;
    private int stringArgsCount;
    public int[] intArgs;

    ClientScriptDefinitions(final int id, final ByteBuffer buffer) {
        this.id = id;
        try {
            decode(buffer);
        } catch (Exception e){
            System.err.println("Failed to decode client script "+id);
            e.printStackTrace();
        }
    }

    public static ClientScriptDefinitions get(final int id) {
        return definitions[id];
    }

    public ClientScriptDefinitions() {
    }

    @Override
    public void decode(final ByteBuffer buffer) {
        buffer.setPosition(buffer.getBuffer().length - 2);
        int switchBlocksSize = buffer.readUnsignedShort();
        int codeBlockEnd = buffer.getBuffer().length - 2 - switchBlocksSize - 12;
        buffer.setPosition(codeBlockEnd);
        int codeSize = buffer.readInt();
        intArgumentCount = buffer.readUnsignedShort();
        stringArgumentCount = buffer.readUnsignedShort();
        intArgsCount = buffer.readUnsignedShort();
        stringArgsCount = buffer.readUnsignedShort();
        int someCount = buffer.readUnsignedByte();
        if (someCount > 0) {
            for (int i = 0; i < someCount; i++) {
                int i_18_ = buffer.readUnsignedShort();
                while (i_18_-- > 0) {
                    buffer.readInt();
                    buffer.readInt();
                }
            }
        }
        buffer.setPosition(0);
        String scriptName = buffer.readString();
        instructions = new int[codeSize];
        intArgs = new int[codeSize];
        stringArgs = new String[codeSize];
        int i_33_ = 0;
        while (buffer.getPosition() < codeBlockEnd) {
            int opcode = buffer.readUnsignedShort();
            if (opcode == 3) stringArgs[i_33_] = buffer.readString();
            else if (opcode < 100 && opcode != 21 && opcode != 38 && opcode != 39) intArgs[i_33_] = buffer.readInt();
            else intArgs[i_33_] = buffer.readUnsignedByte();
            instructions[i_33_++] = opcode;
        }
    }

    public int getId() {
        return id;
    }

    public int getIntArgumentCount() {
        return intArgumentCount;
    }

    public int getStringArgumentCount() {
        return stringArgumentCount;
    }

    public String[] getStringArgs() {
        return stringArgs;
    }

    public int[] getInstructions() {
        return instructions;
    }

    public int getIntArgsCount() {
        return intArgsCount;
    }

    public int getStringArgsCount() {
        return stringArgsCount;
    }

    public int[] getIntArgs() {
        return intArgs;
    }

    @Override
    public String toString() {
        return "ClientScriptDefinitions(id=" + this.getId() + ", intArgumentCount=" + this.getIntArgumentCount() + "," +
                " stringArgumentCount=" + this.getStringArgumentCount() + ", stringArgs=" + Arrays.deepToString(this.getStringArgs()) + ", instructions=" + Arrays.toString(this.getInstructions()) + ", anInt1364=" + this.getIntArgsCount() + ", anInt1365=" + this.getStringArgsCount() + ", intArgs=" + Arrays.toString(this.getIntArgs()) + ")";
    }

}
