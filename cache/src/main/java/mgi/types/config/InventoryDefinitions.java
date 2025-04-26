package mgi.types.config;

import com.zenyte.CacheManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.types.Definitions;
import mgi.utilities.ByteBuffer;
import org.jetbrains.annotations.Nullable;

/**
 * @author Kris | 13. march 2018 : 1:57.09
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class InventoryDefinitions implements Definitions {
	public static Int2ObjectOpenHashMap<InventoryDefinitions> definitions = new Int2ObjectOpenHashMap<>();

	@Override
	public void load() {
		final Cache cache = CacheManager.getCache();
		final Archive configs = cache.getArchive(ArchiveType.CONFIGS);
		final Group invs = configs.findGroupByID(GroupType.INV);
		for (int id = 0; id < invs.getHighestFileId(); id++) {
			final File file = invs.findFileByID(id);
			if (file == null) {
				continue;
			}
			final ByteBuffer buffer = file.getData();
			if (buffer == null) {
				continue;
			}
			definitions.put(id, new InventoryDefinitions(id, buffer));
		}
		logger.info("Loaded " + definitions.size() + " inventory definitions from IDX2");
	}

	private final int id;
	private int size;

	public InventoryDefinitions(final int id, final ByteBuffer buffer) {
		this.id = id;
		decode(buffer);
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
		case 2:
			size = buffer.readUnsignedShort();
		}
	}

	public InventoryDefinitions() {
		this.id = 0;
	}

	@Nullable
	public static InventoryDefinitions get(final int id) {
		return definitions.getOrDefault(id, null);
	}

	@Override
	public ByteBuffer encode() {
		ByteBuffer buffer = new ByteBuffer();
		buffer.writeUnsignedShort(this.size);
		buffer.writeUnsignedByte(0);
		return buffer;
	}

	public static void pack(int id, ByteBuffer buffer) {
		CacheManager.getCache()
				.getArchive(ArchiveType.CONFIGS)
				.findGroupByID(GroupType.INV)
				.addFile(new File(id, buffer));
	}

	@Override
	public void pack() {
		definitions.put(id, this);
		try {
			pack(id, encode());
		} catch (Exception e) {
			logger.error("Failed to pack InventoryDefintiions with id: {}", id);
		}
	}

	public int getId() {
		return id;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "InventoryDefinitions{" +
				"id=" + id +
				", size=" + size +
				'}';
	}
}
