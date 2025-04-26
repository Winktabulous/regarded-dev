package mgi.types.config;

import com.zenyte.CacheManager;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;
import net.runelite.cache.util.ScriptVarType;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static mgi.utilities.ByteBuffer.unicodeTable;

/**
 * @author Kris | 6. apr 2018 : 19:59.19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class ParamDefinitions implements Definitions {
	public static ParamDefinitions[] definitions;

	@Override
	public void load() {
		final Cache cache = CacheManager.getCache();
		final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
		final Group params = configs.findGroupByID(GroupType.PARAMS);
		definitions = new ParamDefinitions[params.getHighestFileId()];
		for (int id = 0; id < params.getHighestFileId(); id++) {
			final File file = params.findFileByID(id);
			if (file == null) {
				continue;
			}
			final ByteBuffer buffer = file.getData();
			if (buffer == null) {
				continue;
			}
			if(id == 10307 || id == 10315 || id == 10316 || id == 10318) {
				continue;
			}
			definitions[id] = new ParamDefinitions(id, buffer);
		}
	}

	private final int id;
	private char stackType;
	private ScriptVarType varType;
	private int defaultInt;
	private String defaultString;
	private boolean autoDisable = true;

	private ParamDefinitions(final int id, final ByteBuffer buffer) {
		this.id = id;
		decode(buffer);
	}

	@Override
	public void pack() {
		final Cache cache = CacheManager.getCache();
		final Archive archive = cache.getArchive(ArchiveType.CONFIGS);
		final Group params = archive.findGroupByID(GroupType.PARAMS);
		if (params.findFileByID(id) == null) {
			CacheManager.getCache().getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.PARAMS).addFile(new File(id, encode()));
		} else {
			logger.error("Param {} was just overwritten by a cache packing operation.", id);
			CacheManager.getCache().getArchive(ArchiveType.CONFIGS).findGroupByID(GroupType.PARAMS).addFile(new File(id, encode()));
		}
	}

	@Override
	public ByteBuffer encode() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.writeUnsignedByte(1);
		buffer.writeUnsignedByte(byteToChar(stackType));

		if(defaultInt != -1) {
			buffer.writeUnsignedByte(2);
			buffer.writeInt(defaultInt);
		}

		if(!autoDisable) {
			buffer.writeUnsignedByte(4);
		}

		if(defaultString != null) {
			buffer.writeUnsignedByte(5);
			buffer.writeString(defaultString);
		}

		buffer.writeUnsignedByte(0);
		return buffer;
	}

	public int byteToChar(char c) {
		if (c >= 127 && c < 160) {
			char curChar = unicodeTable[c - 128];
			if (curChar == 0) {
				curChar = 63;
			}
			c = curChar;
		}
		return c;
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

	@Override
	public void decode(final ByteBuffer buffer, final int opcode) {
		switch (opcode) {
		case 1: 
			stackType = buffer.readJagexChar();
			varType = ScriptVarType.forCharKey(stackType);
			return;
		case 2: 
			defaultInt = buffer.readInt();
			return;
		case 4: 
			autoDisable = false;
			return;
		case 5: 
			defaultString = buffer.readString();
			return;
		}
	}

	public static final ParamDefinitions get(final int id) {
		if (id < 0 || id >= definitions.length) {
			return null;
		}
		return definitions[id];
	}

	public ParamDefinitions() {
		this.id = 0;
	}

	public ParamDefinitions(int id, ScriptVarType varType) {
		this.id = id;
		this.varType = varType;
		this.stackType = varType.getKeyChar();
		if(this.varType == ScriptVarType.STRING)
			defaultString = "";
	}

	public int getId() {
		return id;
	}

	public char getStackType() {
		return stackType;
	}

	public int getDefaultInt() {
		return defaultInt;
	}

	public String getDefaultString() {
		return defaultString;
	}

	public boolean isAutoDisable() {
		return autoDisable;
	}

	public void setDefaultInt(int i) {
		this.defaultInt = i;
	}
}
