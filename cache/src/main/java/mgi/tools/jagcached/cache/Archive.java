package mgi.tools.jagcached.cache;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.Helper;
import mgi.utilities.ByteBuffer;
import mgi.utilities.Whirlpool;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class Archive {
    /**
     * Underlying cache.
     */
    private final Cache cache;
    /**
     * Contains ID of the archive.
     */
    private final int id;
    /**
     * Whether to use automatic version incrementer.
     */
    private boolean useAutomaticVersionIncrementer = true;
    /**
     * Contains all groups.
     */
    private Group[] groups;
    /**
     * Whether whirlpool hashes are used.
     */
    private boolean useWhirlpool;
    /**
     * Whether names are used.
     */
    private boolean useNames;
    /**
     * Version of this archive.
     */
    private int version;
    /**
     * CRC32 of this archive.
     */
    private int crc;
    private int highestGroupId;
    /**
     * 64byte whirlpool digest
     * of this archive.
     */
    private byte[] digest;
    /**
     * Contains packed archive file
     * which was provided with load() call.
     */
    private ByteBuffer packed;
    /**
     * Whether any changes were made to
     * archive properties such as version or properties.
     */
    private boolean changed;

    private Int2ObjectMap<Group> idToGroup;
    private Int2ObjectMap<Group> nameToGroup;

    private final Object $lock = new Object();

    public Archive(int id, Cache cache) {
        this.id = id;
        this.cache = cache;
    }

    /**
     * Tries to load this archive from cache.
     */
    public void load() {
        if (isLoaded()) throw new RuntimeException("Already loaded!");
        ByteBuffer buffer = cache.getMasterIndex().get(id);
        if (buffer == null) {
            return;
        }
        //throw new RuntimeException("Missing archive file.");
        ByteBuffer unpacked = Helper.decodeFITContainer(buffer);
        unpacked.setPosition(0);
        int protocol = unpacked.readUnsignedByte();
        if (protocol >= 6) version = unpacked.readInt();
        int properties = unpacked.readUnsignedByte();
        useNames = (properties & 1) != 0;
        useWhirlpool = (properties & 2) != 0;
        groups = new Group[protocol >= 7 ? unpacked.readSmartInt() : unpacked.readUnsignedShort()];
        int[] groupIDS = new int[groups.length];
        int[] groupNames = new int[groups.length];
        int[] groupCRCS = new int[groups.length];
        byte[][] groupDigests = new byte[groups.length][64];
        int[] groupVersions = new int[groups.length];
        int[][] groupFilesIDS = new int[groups.length][];
        int[][] groupFilesNames = new int[groups.length][];
        boolean readOnly = cache.readOnly;
        if (readOnly) {
            idToGroup = new Int2ObjectOpenHashMap<>(groups.length);
            nameToGroup = new Int2ObjectOpenHashMap<>(groups.length);
        }
        int highest = 0;
        for (int offset = 0, i = 0; i < groups.length; i++) {
            groupIDS[i] = offset += (protocol >= 7 ? unpacked.readSmartInt() : unpacked.readUnsignedShort());
            if (groupIDS[i] <= highest) {
                continue;
            }
            highest = groupIDS[i];
        }
        this.highestGroupId = highest == 0 ? 0 : (highest + 1);
        for (int i = 0; i < groups.length; i++) groupNames[i] = useNames ? unpacked.readInt() : -1;
        for (int i = 0; i < groups.length; i++) groupCRCS[i] = unpacked.readInt();
        if (useWhirlpool) for (int i = 0; i < groups.length; i++) unpacked.readBytes(groupDigests[i], 0, 64);
        for (int i = 0; i < groups.length; i++) groupVersions[i] = unpacked.readInt();
        for (int i = 0; i < groups.length; i++) {
            int filesCount = protocol >= 7 ? unpacked.readSmartInt() : unpacked.readUnsignedShort();
            groupFilesIDS[i] = new int[filesCount];
            groupFilesNames[i] = new int[filesCount];
        }
        for (int i = 0; i < groups.length; i++)
            for (int offset = 0, a = 0; a < groupFilesIDS[i].length; a++) {
                groupFilesIDS[i][a] = offset += (protocol >= 7 ? unpacked.readSmartInt() :
                        unpacked.readUnsignedShort());
                //System.out.println("group " + i + " file " + a);
            }
        for (int i = 0; i < groups.length; i++)
            for (int a = 0; a < groupFilesIDS[i].length; a++)
                groupFilesNames[i][a] = useNames ? unpacked.readInt() : -1;
        for (int i = 0; i < groups.length; i++) {
            int groupID = groupIDS[i];
            int groupName = groupNames[i];
            Group group = new Group(groupID, groupName, groupVersions[i], groupCRCS[i], groupDigests[i],
                    groupFilesIDS[i], groupFilesNames[i], readOnly);
            groups[i] = group;
            if (readOnly) {
                idToGroup.put(groupID, group);
                if (groupName >= 0)
                    nameToGroup.put(groupName, group);
            }
        }
        updateHashes(buffer);
        packed = buffer;
    }

    /**
     * Deletes all groups on this archive.
     * Resets version to 0.
     */
    public void reset() {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        changed = true;
        groups = new Group[0];
        version = 0;
        if (idToGroup != null)
            idToGroup.clear();
        if (nameToGroup != null)
            nameToGroup.clear();
    }

    /**
     * Finishes any changes to this archive.
     */
    public void finish() {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        if (!needsRepack()) return;
        for (Group group : groups) {
            if (group.isIndexChanged()) {
                if (useAutomaticVersionIncrementer) group.setVersion(group.getVersion() + 1);
                ByteBuffer buffer = group.finish();
                if (!cache.getIndex(id).put(group.getID(), buffer, buffer.getBuffer().length))
                    throw new RuntimeException("Couldn't update group:" + group);
            }
        }
        if (useAutomaticVersionIncrementer) version++;
        // sort the group by their id
        Arrays.sort(groups, Comparator.comparingInt(Group::getID));
        for (final Group group : groups) {
            final List<File> files = Arrays.asList(group.getFiles());
            files.removeIf(Objects::isNull);
            files.sort(Comparator.comparingInt(File::getID));
            group.setFiles(files.toArray(new File[0]));
        }
        packed = pack();
        updateHashes(packed);
        if (!cache.getMasterIndex().put(id, packed, packed.getBuffer().length))
            throw new RuntimeException("Couldn't update packed archive.");
        changed = false;
        for (Group group : groups) group.markIndexAsNotChanged();
    }

    /**
     * Packs this archive.
     */
    private ByteBuffer pack() {
        int protocol = decideProtocol();
        ByteBuffer pack = new ByteBuffer(calculatePackedAllocSize(protocol));
        pack.writeByte(protocol);
        if (protocol >= 6) pack.writeInt(version);
        pack.writeByte((useNames ? 1 : 0) | (useWhirlpool ? 2 : 0));
        if (protocol >= 7) pack.writeSmart32(groups.length);
        else pack.writeShort(groups.length);
        for (int delta = 0, i = 0; i < groups.length; i++) {
            if (protocol >= 7) pack.writeSmart32(groups[i].getID() - delta);
            else pack.writeShort(groups[i].getID() - delta);
            delta = groups[i].getID();
        }
        if (useNames) for (Group group : groups) pack.writeInt(group.getName());
        for (Group group : groups) pack.writeInt(group.getCrc());
        if (useWhirlpool) for (Group group : groups) pack.writeBytes(group.getDigest(), 0, 64);
        for (Group group : groups) pack.writeInt(group.getVersion());
        for (Group group : groups)
            if (protocol >= 7) pack.writeSmart32(group.fileCount());
            else pack.writeShort(group.fileCount());
        for (Group group : groups) {
            for (int delta = 0, a = 0; a < group.fileCount(); a++) {
                if (protocol >= 7) pack.writeSmart32(group.getFiles()[a].getID() - delta);
                else pack.writeShort(group.getFiles()[a].getID() - delta);
                delta = group.getFiles()[a].getID();
            }
        }
        if (useNames) for (Group group : groups)
            for (int a = 0; a < group.fileCount(); a++) pack.writeInt(group.getFiles()[a].getName());
        return Helper.encodeFITContainer(pack, version);
    }

    /**
     * Calculates new packed archive size.
     */
    private int calculatePackedAllocSize(int protocol) {
        int size = 2;
        if (protocol >= 6) size += 4;
        size += protocol >= 7 ? (groups.length > 32767 ? 4 : 2) : 2;
        for (int delta = 0, i = 0; i < groups.length; i++) {
            if (protocol >= 7 && (groups[i].getID() - delta) > 32767) size += 4;
            else size += 2;
            delta = groups[i].getID();
        }
        if (useNames) size += groups.length * 4; // names
        size += groups.length * 4; // crcs
        if (useWhirlpool) size += groups.length * 64; // whirlpool
        size += groups.length * 4; // versions
        for (Group group : groups) size += protocol >= 7 ? (group.fileCount() > 32767 ? 4 : 2) : 2;
        for (Group group : groups) {
            for (int delta = 0, a = 0; a < group.fileCount(); a++) {
                if (protocol >= 7 && (group.getFiles()[a].getID() - delta) > 32767) size += 4;
                else size += 2;
                delta = group.getFiles()[a].getID();
            }
        }
        for (Group group : groups) for (int a = 0; a < group.fileCount(); a++) size += 4;
        return size;
    }

    /**
     * Decides protocol ID to be used when packing
     * this archive.
     */
    private int decideProtocol() {
        if (groups.length > 65535) return 7;
        for (Group group : groups) {
            if (group.getID() > 65535) return 7;
            if (group.getHighestFileId() > 65535) return 7;
        }
        for (int delta = 0, i = 0; i < groups.length; i++) {
            if ((groups[i].getID() - delta) > 65535) return 7;
            delta = groups[i].getID();
        }
        for (Group group : groups) {
            if (group.fileCount() > 65535) return 7;
            for (int delta = 0, a = 0; a < group.fileCount(); a++) {
                if ((group.getFiles()[a].getID() - delta) > 65535) return 7;
                delta = group.getFiles()[a].getID();
            }
        }
        return version != 0 ? 6 : 5;
    }

    /**
     * Finds group by id.
     * Returns null if none found.
     */
    public Group findGroupByID(int id) {
        return findGroupByID(id, null, true);
    }

    /**
     * Finds group by id.
     * Returns null if none found.
     */
    public Group findGroupByID(final GroupType group) {
        return findGroupByID(group.getId());
    }

    /**
     * Finds group by id.
     * Returns null if none found.
     */
    public Group findGroupByID(int id, int[] xtea, boolean unpack) {
        return findGroupByID(id, xtea, unpack, false);
    }

    /**
     * Finds group by id.
     * Returns null if none found.
     */
    public Group findGroupByID(int id, int[] xtea, boolean unpack, final boolean throwException) {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");

        if (idToGroup != null) {
            final Group group = idToGroup.get(id);
            if (group != null) {
                if (!group.isLoaded()) {
                    final ByteBuffer data = cache.getIndex(this.id).get(id);
                    if (data == null) {
                        if (throwException) throw new RuntimeException("Missing group:" + id);
                        return null;
                    }
                    group.load(data, xtea, unpack);
                }
                return group;
            }
        }

        for (Group group : groups) {
            if (group.getID() == id) {
                if (group.isLoaded()) return group;
                ByteBuffer data = cache.getIndex(this.id).get(id);
                if (data == null) {
                    if (throwException) {
                        throw new RuntimeException("Missing group:" + id);
                    }
                    return null;
                }
                group.load(data, xtea, unpack);
                return group;
            }
        }
        return null;
    }

    /**
     * Finds group by name.
     * Returns null if none found.
     */
    public Group findGroupByName(String name) {
        return findGroupByName(name, null);
    }

    /**
     * Finds group by name.
     * Returns null if none found.
     */
    public Group findGroupByName(int name) {
        return findGroupByName(name, null);
    }

    public Group findGroupByName(String name, int[] xtea) {
        return findGroupByName(name, xtea, true);
    }

    /**
     * Finds group by name.
     * Returns null if none found.
     */
    public Group findGroupByName(String name, int[] xtea, boolean unpack) {
        return findGroupByName(Helper.strToI(name), xtea, unpack);
    }

    public Group findGroupByName(int name, int[] xtea) {
        return findGroupByName(name, xtea, true);
    }

    /**
     * Finds group by name.
     * Returns null if none found.
     */
    public Group findGroupByName(int name, int[] xtea, boolean unpack) {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        if (name == -1) return null;

        if (nameToGroup != null) {
            final Group group = nameToGroup.get(name);
            if (group != null) {
                if (!group.isLoaded()) {
                    synchronized ($lock) {
                        final ByteBuffer data = cache.getIndex(this.id).get(group.getID());
                        if (data == null) throw new RuntimeException("Missing group:" + group.getID());
                        group.load(data, xtea, unpack);
                    }
                }
                return group;
            }
        }

        for (Group group : groups) {
            if (group.getName() == name) {
                synchronized ($lock) {
                    if (group.isLoaded()) return group;
                    ByteBuffer data = cache.getIndex(this.id).get(group.getID());
                    if (data == null) throw new RuntimeException("Missing group:" + group.getID());
                    group.load(data, xtea, unpack);
                    return group;
                }
            }
        }
        return null;
    }

    /**
     * Adds new group to this archive.
     * If there's already a group with same id then
     * it gets overwritten.
     */
    public void addGroup(Group group) {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        if (!group.isLoaded()) throw new RuntimeException("group is not loaded.");
        if (findGroupIndex(group) != -1) return;
        if (group.getID() == -1) group.setID(getFreeGroupID());
        int index = -1;
        for (int i = 0; i < groups.length; i++)
            if (groups[i].getID() == group.getID()) {
                index = i;
                break;
            }
        if (index == -1) {
            Group[] newGroups = new Group[groups.length + 1];
            System.arraycopy(groups, 0, newGroups, 0, groups.length);
            index = groups.length;
            groups = newGroups;
        }
        groups[index] = group;
        group.markIndexAsChanged(); // cause it needs to be packed to store.
        changed = true;
    }

    /**
     * Deletes given group.
     */
    public void deleteGroup(Group group) {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        int index = findGroupIndex(group);
        if (index == -1) return;
        Group[] newGroups = new Group[groups.length - 1];
        for (int write = 0, i = 0; i < groups.length; i++) if (groups[i] != group) newGroups[write++] = groups[i];
        groups = newGroups;
        changed = true;
    }

    /**
     * Deletes all group in this fs.
     */
    public void deleteAllGroups() {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        groups = new Group[0];
        changed = true;
        if (idToGroup != null)
            idToGroup.clear();
        if (nameToGroup != null)
            nameToGroup.clear();
    }

    /**
     * Loads group if it's not yet loaded.
     */
    public void load(Group group) {
        load(group, null);
    }

    /**
     * Loads group if it's not yet loaded.
     */
    public void load(Group group, int[] xtea) {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        int index = findGroupIndex(group);
        if (index == -1) return;
        synchronized ($lock) {
            if (group.isLoaded()) return;
            ByteBuffer data = cache.getIndex(id).get(group.getID());
            if (data == null) throw new RuntimeException("Missing group:" + group.getID());
            group.load(data, xtea, true);
        }
    }

    /**
     * Finishes any pending caches on this archive
     * and then unloads some buffered files.
     */
    public void unloadCachedFiles() {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        finish();
        for (Group group : groups) if (group.isLoaded()) group.unload();
    }

    /**
     * Gets free group ID.
     */
    public int getFreeGroupID() {
        if (groups.length == 0) return 0;
        int highest = -1;
        for (Group group : groups) if (group.getID() > highest) highest = group.getID();
        return highest + 1;
    }

    /**
     * Finds group index.
     */
    private int findGroupIndex(Group group) {
        for (int i = 0; i < groups.length; i++) if (groups[i] == group) return i;
        return -1;
    }

    public int findGroupIdByName(final String name) {
        return Helper.strToI(name);
    }

    /**
     * Updates crc32 and whirlpool hash to ones from
     * given buffer.
     */
    private void updateHashes(ByteBuffer packed) {
        crc = Helper.crc32(packed, 0, packed.getBuffer().length);
        digest = Whirlpool.whirlpool(packed.getBuffer(), 0, packed.getBuffer().length);
    }

    /**
     * Whether this archive is loaded.
     */
    public boolean isLoaded() {
        return packed != null;
    }

    /**
     * Whether archive file needs repack.
     */
    private boolean needsRepack() {
        if (changed) return true;
        for (Group group : groups) if (group.isIndexChanged()) return true;
        return false;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * Gets ID of this archive.
     */
    public int getID() {
        return id;
    }

    /**
     * Whether this archive uses names.
     */
    public boolean usesNames() {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        return useNames;
    }

    /**
     * Whether this archive uses whirlpool.
     */
    public boolean usesWhirlpool() {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        return useWhirlpool;
    }

    /**
     * Sets Whether this archive uses names.
     */
    public void setUsesNames(boolean uses) {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        if (this.useNames != uses) {
            changed = true;
            this.useNames = uses;
        }
    }

    /**
     * Sets Whether this archive uses whirlpool.
     */
    public void setUsesWhirlpool(boolean uses) {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        if (this.useWhirlpool != uses) {
            changed = true;
            this.useWhirlpool = uses;
        }
    }

    /**
     * Gets version of this archive.
     */
    public int getVersion() {
        //if (!isLoaded())
        //	throw new RuntimeException("Using nonloaded archive.");
        return version;
    }

    public ByteBuffer getPacked() {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        return packed;
    }

    /**
     * Sets version of this archive.
     */
    public void setVersion(int version) {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        if (this.version != version) {
            changed = true;
            this.version = version;
        }
    }

    /**
     * Gets crc32 of packed version of this archive.
     */
    public int getCRC32() {
        //if (!isLoaded())
        //	throw new RuntimeException("Using nonloaded archive.");
        return crc;
    }

    /**
     * Gets whirlpool digest of packed version of this archive.
     */
    public byte[] getDigest() {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        return digest;
    }

    /**
     * Whether versions are automatically incremented
     * each time finish() is called.
     */
    public boolean usingAutomaticVersionsIncrementer() {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        return useAutomaticVersionIncrementer;
    }

    /**
     * Sets Whether to use automatic versions incrementer.
     */
    public void setUseAutomaticVersionsIncrementer(boolean use) {
        if (!isLoaded()) throw new RuntimeException("Using nonloaded archive.");
        this.useAutomaticVersionIncrementer = use;
    }

    /**
     * Gets all group.
     * Returned group array can't be modified
     * in any way.
     */
    public Group[] getGroups() {
        return groups;
    }

    /**
     * Gets count of groups this archive has.
     */
    public int groupCount() {
        return groups.length;
    }

    public int getHighestGroupId() {
        return highestGroupId;
    }

    /*
    RS
     */
    public byte[] getFileFlat(int var1) {
        if (this.groups.length == 1) {
            return this.findGroupByID(0).findFileByID(var1).getData().getBuffer();
        } else if (this.groups[var1].fileCount() == 1) {
            return this.findGroupByID(var1).findFileByID(0).getData().getBuffer();
        } else {
            throw new RuntimeException();
        }
    }

    public byte[] takeFileFlat(int var1) {
        if (this.groups.length == 1) {
            return takeFile(0, var1);
        } else if (this.groups[var1].fileCount() == 1) {
            return takeFile(var1, 0);
        } else {
            throw new RuntimeException();
        }
    }

    public byte[] takeFile(int var1, int var2) {
        return findGroupByID(var1).findFileByName(var2).getData().getBuffer();
    }
    public File[] getGroupFiles(int var1) {
        return var1 >= 0 && var1 < this.groups.length ? this.groups[var1].getFiles() : null;
    }
    public int getGroupFileCount(int var1) {
        return var1 >= 0 && var1 < this.groups.length ? this.groups[var1].fileCount() : 0;
    }
}
