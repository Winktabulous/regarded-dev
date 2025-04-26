package mgi.tools.jagcached.cache;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.tools.jagcached.Helper;
import mgi.types.component.ComponentDefinitions;
import mgi.utilities.ByteBuffer;
import mgi.utilities.Whirlpool;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Group {
	/**
	 * Contains ID of the group.
	 */
	private int id;
	/**
	 * Contains name hash of this group.
	 */
	private int name;
	/**
	 * Contains version of packed group file.
	 */
	private int version;
	/**
	 * Contains crc of packed group file.
	 */
	private int crc;
	private int highestFileId;
	/**
	 * Contains 64-byte whirlpool digest.
	 */
	private byte[] digest;
	/**
	 * Contains all files in this group.
	 */
	private File[] files;
	/**
	 * Contains packed files buffer 
	 * which was provided with load() call.
	 */
	private ByteBuffer packedFiles;
	/**
	 * Contains xtea keys. (int[4])
	 * Can be null.
	 */
	private int[] xtea;
	/**
	 * Whether any changes were made.
	 */
	private boolean needsRepack;
	/**
	 * Whether any of those things changed (id,name,version,crc,digest,files and their data).
	 */
	private boolean indexChanged;

	private final Int2ObjectMap<File> idToFile;
	private final Int2ObjectMap<File> nameToFile;
	private boolean unknownFileCount = false;

	/**
	 * Constructor for copy() methods.
	 */
	private Group() {
		idToFile = null;
		nameToFile = null;
	}

	/**
	 * Creates a new empty group with an id of {@code id}.
	 */
	public Group(int id) {
		this(id, -1, 0, new File[0]);
	}

	/**
	 * Creates new group with autoassigned id from given data.
	 * Each file must have it's buffer attached.
	 */
	public Group(File... files) {
		this(-1, -1, 0, files);
	}

	/**
	 * Creates new group with autoassigned id from given data.
	 * Each file must have it's buffer attached.
	 */
	public Group(int id, File... files) {
		this(id, -1, 0, files);
	}

	/**
	 * Creates new group with autoassigned id from given data.
	 * Each file must have it's buffer attached.
	 */
	public Group(String name, int version, File... files) {
		this(-1, Helper.strToI(name), version, files);
	}

	/**
	 * Creates new group with autoassigned id from given data.
	 * Each file must have it's buffer attached.
	 */
	public Group(int name, int version, File... files) {
		this(-1, name, version, files);
	}

	/**
	 * Creates new group from given data.
	 * Each file must have it's buffer attached.
	 */
	public Group(int groupID, String name, int version, File... files) {
		this(groupID, Helper.strToI(name), version, files);
	}

	/**
	 * Creates new group from given data.
	 * Each file must have it's buffer attached.
	 */
	public Group(int groupID, int name, int version, File[] files) {
		for (File file : files) if (!file.isLoaded()) throw new RuntimeException("Nonloaded file.");
		this.id = groupID;
		this.name = name;
		this.version = version;
		this.files = files;
		idToFile = null;
		nameToFile = null;
		for (File file : files) if (file.getID() == -1) file.setID(getFreeFileID());
		updateHashes(this.packedFiles = pack());
	}

	/**
	 * Creates new unloaded group from given FIT data.
	 */
	public Group(int groupID, int name, int version, int crc32, byte[] digest, int[] filesIDS, int[] filesNames, boolean readOnly) {
		if (digest.length != 64 || filesIDS.length != filesNames.length) throw new RuntimeException("Invalid data provided.");
		this.id = groupID;
		this.name = name;
		this.version = version;
		this.crc = crc32;
		this.digest = digest;
		this.files = new File[filesIDS.length];
		idToFile = readOnly ? new Int2ObjectOpenHashMap<>(files.length) : null;
		nameToFile = readOnly ? new Int2ObjectOpenHashMap<>(files.length) : null;
		for (int i = 0; i < filesIDS.length; i++) {
			int fileID = filesIDS[i];
			int fileName = filesNames[i];
			File file = new File(fileID, fileName);
			files[i] = file;
			if (readOnly) {
				idToFile.put(fileID, file);
				if (fileName >= 0) nameToFile.put(fileName, file);
			}
		}
	}

	/**
	 * Copies this group and every file in it.
	 */
	public Group copy() {
		Group copy = new Group();
		copy.id = id;
		copy.name = name;
		copy.version = version;
		copy.crc = crc;
		if (digest != null) {
			copy.digest = new byte[64];
			System.arraycopy(digest, 0, copy.digest, 0, 64);
		}
		if (files != null) {
			copy.files = new File[files.length];
			for (int i = 0; i < files.length; i++) copy.files[i] = files[i].copy();
		}
		if (packedFiles != null) {
			copy.packedFiles = new ByteBuffer(packedFiles.toArray(0, packedFiles.getBuffer().length), packedFiles.getPosition());
		}
		if (xtea != null) {
			copy.xtea = new int[4];
			System.arraycopy(xtea, 0, copy.xtea, 0, 4);
		}
		copy.needsRepack = needsRepack;
		copy.indexChanged = indexChanged;
		return copy;
	}

	/**
	 * Loads packed group file.
	 */
	public void load(ByteBuffer packedFiles) {
		load(packedFiles, null, true);
	}

	public void loadCustom(ByteBuffer packedFiles, boolean unknownFileCount) {
		this.unknownFileCount = unknownFileCount;
		this.files = new File[1];
		this.files[0] = new File(0, -1);
		this.packedFiles = packedFiles;
		unpackCustom(packedFiles);
		int highest = 0;
		for (final File file : files) {
			if (file == null || file.getID() <= highest) {
				continue;
			}
			highest = file.getID();
		}
		this.highestFileId = highest + 1;

	}

	private void unpackCustom(ByteBuffer packed) {
		if (xtea != null && xtea[0] != 0 && xtea[1] != 0 && xtea[2] != 0 && xtea[3] != 0) {
			Helper.decryptContainer(packed, xtea);
		}
		packed = Helper.decodeFilesContainer(packed);
		StringBuilder sb = new StringBuilder();
		int pos = 0;
		int count = 0;
		int lastPos = 0;
		for(byte b: packed.getBuffer()) {
			int diff = (pos - lastPos);
			if(b == -1 && (diff == 0 || diff > 49)) {
				count++;
				sb.append(pos);
				if(pos < packed.getBuffer().length)
					sb.append(",");
				lastPos = pos;
			}

			pos++;
		}
		LinkedHashMap<Integer, ComponentDefinitions> list = new LinkedHashMap<>();
		int componentId = 0;
		while(packed.getPosition() < packed.getBuffer().length) {
			ComponentDefinitions def = new ComponentDefinitions();
			if(packed.getBuffer()[packed.getPosition()] == -1)
				def.decodeIf3(packed);
			else break;
			list.put(componentId, def);
			componentId++;
		}

		java.io.File directory = new java.io.File("dumps/interfaces/");

		for(Map.Entry<Integer, ComponentDefinitions> entry: list.entrySet()) {
			try {
				Files.write(Path.of(directory + "\\" + entry.getKey()), entry.getValue().encode().getBuffer());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}

	/**
	 * Unloads this group, can be called
	 * only by {@code Index#unloadCachedData} method.
	 */
	public void unload() {
		if (!isLoaded()) throw new RuntimeException("Using nonloaded group.");
		packedFiles = null;
		for (File file : files) file.unload();
	}

	/**
	 * Loads packed group file.
	 */
	public void load(ByteBuffer packedFiles, int[] xtea, boolean unpack) {
		//if (isLoaded())
		//	throw new RuntimeException("Already loaded.");
		this.packedFiles = packedFiles;
		this.xtea = xtea;
		if (unpack) {
			unpack(packedFiles);
			int highest = 0;
			for (final File file : files) {
				if (file == null || file.getID() <= highest) {
					continue;
				}
				highest = file.getID();
			}
			this.highestFileId = highest + 1;
		}
	}

	/**
	 * Finishes any changes by generating new packedFiles buffer and updating hashes
	 * if something was changed.
	 */
	public ByteBuffer finish() {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded group.");
		if (needsRepack()) {
			sortFiles();
			packedFiles = pack();
			updateHashes(packedFiles);
			markAsNotNeedRepack();
			return packedFiles;
		}
		return packedFiles;
	}

	/**
	 * Unpacks given packed files buffer.
	 */
	@SuppressWarnings("Duplicates")
	private void unpack(ByteBuffer packed) {
		if (xtea != null && xtea[0] != 0 && xtea[1] != 0 && xtea[2] != 0 && xtea[3] != 0) {
			Helper.decryptContainer(packed, xtea);
		}
		packed = Helper.decodeFilesContainer(packed);
		if (fileCount() <= 1) {
			if (fileCount() <= 0) return;
			ByteBuffer copy = new ByteBuffer(packed.getBuffer().length);
			copy.writeBytes(packed.getBuffer(), 0, packed.getBuffer().length);
			files[0].load(copy);
			return;
		}
		int sectorsCount = packed.getBuffer()[packed.getBuffer().length - 1] & 255;
		int[] lengths = new int[fileCount()];
		packed.setPosition(packed.getBuffer().length - ((sectorsCount * fileCount() * 4) + 1));
		for (int sectorID = 0; sectorID < sectorsCount; sectorID++)  {
			for (int i = 0, length = 0; i < fileCount(); i++) {
				lengths[i] += (length += packed.readInt());
			}
		}
		for (int i = 0; i < lengths.length; i++) {
			files[i].load(new ByteBuffer(lengths[i]));
			lengths[i] = 0;
		}
		packed.setPosition(packed.getBuffer().length - ((sectorsCount * fileCount() * 4) + 1));
		for (int fRead = 0, sectorID = 0; sectorID < sectorsCount; sectorID++) for (int i = 0, length = 0; i < fileCount(); i++) {
			length += packed.readInt();
			System.arraycopy(packed.getBuffer(), fRead, files[i].getData().getBuffer(), lengths[i], length);
			lengths[i] += length;
			fRead += length;
		}
	}

	/**
	 * Packs unpacked files.
	 */
	private ByteBuffer pack() {
		if (fileCount() <= 1) {
			if (fileCount() <= 0) {
				ByteBuffer container = Helper.encodeFilesContainer(new ByteBuffer(new byte[0]), version);
				if (xtea != null && xtea[0] != 0 && xtea[1] != 0 && xtea[2] != 0 && xtea[3] != 0) {
					Helper.encryptContainer(container, xtea);
				}
				return container;
			}
			ByteBuffer packed = new ByteBuffer(files[0].getData().getBuffer().length);
			packed.writeBytes(files[0].getData().getBuffer(), 0, files[0].getData().getBuffer().length);
			ByteBuffer container = Helper.encodeFilesContainer(packed, version);
			if (xtea != null && xtea[0] != 0 && xtea[1] != 0 && xtea[2] != 0 && xtea[3] != 0) {
				Helper.encryptContainer(container, xtea);
			}
			return container;
		}
		int allocLength = 1 + (4 * fileCount()); // sector header
		for (File value : files) allocLength += value.getData().getBuffer().length;
		ByteBuffer packed = new ByteBuffer(allocLength);
		for (File file : files) packed.writeBytes(file.getData().getBuffer(), 0, file.getData().getBuffer().length);
		for (int i = 0, v = 0; i < files.length; i++, v += (files[i - 1].getData().getBuffer().length - v)) packed.writeInt(files[i].getData().getBuffer().length - v);
		packed.writeByte(1); // 1 sector
		ByteBuffer container = Helper.encodeFilesContainer(packed, version);
		if (xtea != null && xtea[0] != 0 && xtea[1] != 0 && xtea[2] != 0 && xtea[3] != 0) {
			Helper.encryptContainer(container, xtea);
		}
		return container;
	}

	/**
	 * Updates crc32 and whirlpool hash to one's from
	 * given buffer.
	 */
	private void updateHashes(ByteBuffer packedFiles) {
		crc = Helper.crc32(packedFiles, 0, packedFiles.getBuffer().length - 2);
		digest = Whirlpool.whirlpool(packedFiles.getBuffer(), 0, packedFiles.getBuffer().length - 2);
	}

	/**
	 * Adds new file to this group.
	 * If there's already a file with same id then 
	 * it get's overwriten.
	 */
	public Group addFile(File file) {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded group.");
		if (findFileIndex(file) != -1) return this;
		if (file.getID() == -1) file.setID(getFreeFileID());
		File toOverwrite = findFileByID(file.getID());
		int index = toOverwrite != null ? findFileIndex(toOverwrite) : -1;
		if (index == -1) {
			File[] newFiles = new File[files.length + 1];
			System.arraycopy(files, 0, newFiles, 0, files.length);
			index = files.length;
			files = newFiles;
		}
		if (file.getID() > highestFileId)
			highestFileId = file.getID() + 1;
		files[index] = file;
		needsRepack = indexChanged = true;
		return this;
	}

	/**
	 * Removes file from this group.
	 */
	public void removeFile(File file) {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded group.");
		int index = findFileIndex(file);
		if (index == -1) return;
		File[] newFiles = new File[files.length - 1];
		for (int write = 0, i = 0; i < files.length; i++) if (i != index) newFiles[write++] = files[i];
		files = newFiles;
		needsRepack = indexChanged = true;
	}

	/**
	 * Deletes all files on this group.
	 */
	public void deleteAllFiles() {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded group.");
		files = new File[0];
		needsRepack = indexChanged = true;
	}

	/**
	 * Recalculates groups hashes.
	 */
	public void recalculate() {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded group.");
		updateHashes(packedFiles);
		indexChanged = true;
	}

	/**
	 * Sorts files.
	 */
	private void sortFiles() {
		if (files.length > 1) {
			boolean needsSort = false;
			for (int i = 0; i < files.length; i++) {
				if (i > 0 && files[i - 1].getID() >= files[i].getID()) {
					needsSort = true;
					break;
				}
			}
			if (needsSort) {
				// we must order files from lowest id to highest id
				File[] rebuff = new File[files.length];
				boolean[] processed = new boolean[files.length];
				for (int count = 0; count < files.length; count++) {
					int lowest = Integer.MAX_VALUE;
					int index = -1;
					for (int x = 0; x < files.length; x++) {
						if (processed[x]) continue;
						if (files[x].getID() < lowest) {
							lowest = files[x].getID();
							index = x;
						}
					}
					if (index == -1) throw new RuntimeException("N/A");
					processed[index] = true;
					rebuff[count] = files[index];
				}
				files = rebuff;
				needsRepack = indexChanged = true;
			}
		}
	}

	/**
	 * Gets free file ID.
	 */
	public int getFreeFileID() {
		if (files.length == 0) return 0;
		int highest = -1;
		for (File file : files) if (file.getID() > highest) highest = file.getID();
		return highest + 1;
	}

	/**
	 * Finds file by id's id.
	 * Returns null if not found.
	 */
	public File findFileByID(int id) {
		if (!isLoaded()) throw new RuntimeException("Group is not loaded.");
		if (idToFile != null) {
			return idToFile.get(id);
		}
		for (File file : files) if (file.getID() == id) return file;
		return null;
	}

	/**
	 * Finds file by name.
	 * Returns null if not found.
	 */
	public File findFileByName(String name) {
		return findFileByName(Helper.strToI(name));
	}

	/**
	 * Finds file by name.
	 * Returns null if not found.
	 */
	public File findFileByName(int name) {
		if (!isLoaded()) throw new RuntimeException("Group is not loaded.");
		if (name == -1) return null;
		if (nameToFile != null) {
			return nameToFile.get(name);
		}
		for (File file : files) if (file.getName() == name) return file;
		return null;
	}

	/**
	 * Finds file index.
	 */
	private int findFileIndex(File file) {
		for (int i = 0; i < files.length; i++) if (files[i] == file) return i;
		return -1;
	}

	/**
	 * Whether this group was loaded.
	 */
	public boolean isLoaded() {
		return packedFiles != null;
	}

	/**
	 * Whether this group needs repacking.
	 */
	private boolean needsRepack() {
		if (needsRepack) return true;
		for (File file : files) if (file.isDataChanged()) return true;
		return false;
	}

	/**
	 * Marks this group as already repacked.
	 */
	private void markAsNotNeedRepack() {
		needsRepack = false;
		for (File file : files) file.markDataAsNotChanged();
	}

	/**
	 * Whether this group was changed in any way.
	 */
	public boolean isIndexChanged() {
		if (indexChanged) return true;
		for (File file : files) if (file.isIndexInfoChanged()) return true;
		return false;
	}

	/**
	 * Marks this group as not changed, this
	 * happens when fit is repacked.
	 */
	public void markIndexAsNotChanged() {
		indexChanged = false;
		for (File file : files) file.markIndexAsNotChanged();
	}

	/**
	 * Marks this group index info as changed,
	 * gets called by {@link Archive} only.
	 */
	public void markIndexAsChanged() {
		indexChanged = true;
	}

	/**
	 * Gets count of files this group has.
	 */
	public int getHighestFileId() {
		return highestFileId;
	}

	/**
	 * Gets count of files this group has.
	 */
	public int fileCount() {
		return files.length;
	}

	/**
	 * Gets all files that this group has.
	 * Modifying the array in any way is not allowed.
	 */
	public File[] getFiles() {
		return files;
	}

	/**
	 * Gets ID of this group.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Sets ID of this group.
	 */
	public void setID(int id) {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded group.");
		if (this.id != id) this.indexChanged = true;
		this.id = id;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	/**
	 * Gets name of this group.
	 */
	public int getName() {
		return name;
	}

	/**
	 * Sets name of this group.
	 */
	public void setName(String name) {
		setName(Helper.strToI(name));
	}

	/**
	 * Sets name of this group.
	 */
	public void setName(int name) {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded group.");
		if (this.name != name) this.indexChanged = true;
		this.name = name;
	}

	/**
	 * Gets version of this group.
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * Sets version of this group.
	 */
	public void setVersion(int version) {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded group.");
		if (this.version != version) this.indexChanged = this.needsRepack = true;
		this.version = version;
	}

	/**
	 * Sets XTEA keys of this group.
	 */
	public void setXTEA(int[] xtea) {
		if (!isLoaded()) throw new RuntimeException("Altering nonloaded group.");
		if (this.xtea != xtea) this.indexChanged = this.needsRepack = true;
		this.xtea = xtea;
	}

	/**
	 * Gets crc32 of packed version of this group.
	 */
	public int getCrc() {
		return crc;
	}

	/**
	 * Gets whirlpool digest of packed version of this group.
	 */
	public byte[] getDigest() {
		return digest;
	}

	/**
	 * Gets packed files buffer.
	 * Call to finish() is a must if you want
	 * to get up-to-date version.
	 */
	public ByteBuffer getPackedFiles() {
		return packedFiles;
	}
}
