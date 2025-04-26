package mgi.tools;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.region.XTEALoaderPorted;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.tools.parser.MapChanges;
import mgi.tools.parser.TypeParser;
import mgi.types.Definitions;
import mgi.types.component.SpriteDefaults;
import mgi.types.config.AnimationDefinitions;
import mgi.types.config.HitbarDefinitions;
import mgi.types.config.ObjectDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import mgi.utilities.ByteBuffer;
import net.runelite.api.NpcID;
import org.runestar.cs2.type.Value;

import java.util.ArrayList;
import java.util.List;

import static com.zenyte.game.world.entity._Location.getRegionIDByRegion;

public class DataMigration {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DataMigration.class);
    private Cache target;
    private Cache source;
    private boolean portSequencesDirect;

    public DataMigration(Cache old_cache, Cache new_cache, boolean directSequences) {
        this.target = old_cache;
        this.source = new_cache;
        this.portSequencesDirect = directSequences;
    }

    public void run() {
        portHitbars();
        portModels();
        portUnderlays();
        portOverlays();
        portGraphics();
        portSkins();
        portSkeletons();
        portSequences();
        portTextures();
        generatePrePortSpriteMapping();
        portSprites();
        portNPCs();
        portItems();
        portMaps();
        if(!portSequencesDirect)
            portObjects();
    }

    private static IntArrayList manualModelList = new IntArrayList();
    static {
        manualModelList.addAll(List.of(32362, 32586, 44968, 32585, 32358, 32073, 29622, 44936));
    }

    private void portHitbars() {
        Archive oldConfigs = target.getArchive(ArchiveType.CONFIGS);
        Group oldHitbars = oldConfigs.findGroupByID(GroupType.HITBAR);

        Archive newConfigs = source.getArchive(ArchiveType.CONFIGS);
        Group newHitbars = newConfigs.findGroupByID(GroupType.HITBAR);

        int live_count = newHitbars.fileCount();
        int old_count = oldHitbars.fileCount();

        logger.info("Porting hitbars index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            File file = newHitbars.findFileByID(port_index);
            if(file == null) {
                port_index++;
                continue;
            }
            HitbarDefinitions definitions = new HitbarDefinitions(file.getID(), file.getData());
            MANUAL_SPRITE_GROUPS.put(definitions.getPrimarySprite(), definitions.getPrimarySprite());
            MANUAL_SPRITE_GROUPS.put(definitions.getSecondarySprite(), definitions.getSecondarySprite());
            oldHitbars.addFile(file);
            successful_ports++;
            port_index++;
        }

        logger.info("Successfully ported " + successful_ports + " hitbars from latest OSRS cache");
    }


    private void portObjects() {
        Archive oldConfigs = target.getArchive(ArchiveType.CONFIGS);
        Group oldObjects = oldConfigs.findGroupByID(GroupType.OBJECT);

        Archive newConfigs = source.getArchive(ArchiveType.CONFIGS);
        Group newObjects = newConfigs.findGroupByID(GroupType.OBJECT);

        int live_count = newObjects.fileCount();
        int old_count = oldObjects.fileCount();

        logger.info("Porting objects index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            File file = newObjects.findFileByID(port_index);
            if(file == null) {
                port_index++;
                continue;
            }
            oldObjects.addFile(file);
            successful_ports++;
            port_index++;
        }

        logger.info("Successfully ported " + successful_ports + " objects from latest OSRS cache");
    }

    private void portUnderlays() {
        Archive oldConfigs = target.getArchive(ArchiveType.CONFIGS);
        Group oldUnderlays = oldConfigs.findGroupByID(GroupType.UNDERLAY);

        Archive newConfigs = source.getArchive(ArchiveType.CONFIGS);
        Group newUnderlays = newConfigs.findGroupByID(GroupType.UNDERLAY);

        int live_count = newUnderlays.fileCount();
        int old_count = oldUnderlays.fileCount();

        logger.info("Porting underlays index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            File file = newUnderlays.findFileByID(port_index);
            if(file == null) {
                port_index++;
                continue;
            }
            oldUnderlays.addFile(file);
            successful_ports++;
            port_index++;
        }

        logger.info("Successfully ported " + successful_ports + " underlays from latest OSRS cache");
    }

    private void portOverlays() {
        Archive oldConfigs = target.getArchive(ArchiveType.CONFIGS);
        Group oldOverlays = oldConfigs.findGroupByID(GroupType.OVERLAY);

        Archive newConfigs = source.getArchive(ArchiveType.CONFIGS);
        Group newOverlays = newConfigs.findGroupByID(GroupType.OVERLAY);

        int live_count = newOverlays.fileCount();
        int old_count = oldOverlays.fileCount();

        logger.info("Porting overlays index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            File file = newOverlays.findFileByID(port_index);
            if(file == null) {
                port_index++;
                continue;
            }
            oldOverlays.addFile(file);
            successful_ports++;
            port_index++;
        }

        logger.info("Successfully ported " + successful_ports + " overlays from latest OSRS cache");
    }
    public static List<Integer> forcedGFXOverwrites = List.of(2510);
    private void portGraphics() {
        Archive oldConfigs = target.getArchive(ArchiveType.CONFIGS);
        Group oldGraphics = oldConfigs.findGroupByID(GroupType.SPOTANIM);

        Archive newConfigs = source.getArchive(ArchiveType.CONFIGS);
        Group newGraphics = newConfigs.findGroupByID(GroupType.SPOTANIM);

        int live_count = newGraphics.fileCount();
        int old_count = oldGraphics.fileCount();

        logger.info("Porting graphics index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            File file = newGraphics.findFileByID(port_index);
            if(file == null) {
                port_index++;
                continue;
            }
            oldGraphics.addFile(file);
            successful_ports++;
            port_index++;
        }

        for(Integer id: forcedGFXOverwrites) {
            File file = newGraphics.findFileByID(id);
            if(file == null) {
                continue;
            }
            oldGraphics.addFile(file);
        }

        logger.info("Successfully ported " + successful_ports + " graphics from latest OSRS cache");
    }

    private void portMaps() {
        Archive newMaps = this.source.getArchive(ArchiveType.MAPS);
        Archive oldMaps  = this.target.getArchive(ArchiveType.MAPS);

        int newCount = newMaps.getHighestGroupId();
        int old_count = oldMaps.getHighestGroupId();

        logger.info("Porting map index with new group ct " + newCount + " into old group ct " + old_count);

        int regionX = 0, regionY = 0;
        int successful_ports = 0;
        int failed_ports = 0;

        while(regionX <= 99) {
            regionY = 0;
            while(regionY < 256) {
                if(skipRegion(getRegionIDByRegion(regionX, regionY))) {
                    regionY++;
                    continue;
                }
                try {
                    int[] keys = XTEALoaderPorted.getXTEAKeys(getRegionIDByRegion(regionX, regionY));
                    Group landGroup = newMaps.findGroupByName("l" + regionX + "_" + regionY, keys);
                    Group mapGroup = newMaps.findGroupByName("m" + regionX + "_" + regionY);

                    final ByteBuffer mapBuffer = mapGroup == null ? null : mapGroup.findFileByID(0).getData();
                    final ByteBuffer landBuffer = landGroup == null ? null : landGroup.findFileByID(0).getData();

                    if(mapBuffer == null || landBuffer == null) {
                        regionY++;
                        continue;
                    }
                    byte[] l_data = MapChanges.modifyRegionData(getRegionIDByRegion(regionX, regionY), landBuffer.getBuffer());
                    landGroup.findFileByID(0).setData(new ByteBuffer(l_data));
                    landGroup.setXTEA(null);
                    oldMaps.addGroup(landGroup);
                    oldMaps.addGroup(mapGroup);
                    //TODO Address later
                    //TypeParser.regionChanged((regionX << 8) | regionY);

                    successful_ports++;
                    regionY++;
                }   catch (RuntimeException ex) {
                    regionY++;
                    failed_ports++;
                }
            }
            regionX++;
        }

        logger.info("Successfully ported " + successful_ports + " maps from latest OSRS cache, "+ failed_ports + " skipped");
    }

    private boolean skipRegion(int regionIDByRegion) {
        IntArrayList skips = IntArrayList.of(12889,
                13136, 13137, 13138, 13139, 13140, 13141, 13142, 13143, 13144, 13145,
                13392, 13393, 13394, 13395, 13396, 13397, 13398, 13399, 13400, 13401,

                12610, 12611, 12612, 12613,
                12866, 12867, 12868, 12869,
                13122, 13123, 13124, 13125
                );
        return skips.contains(regionIDByRegion);
    }

    public Int2IntOpenHashMap MANUAL_SPRITE_GROUPS = new Int2IntOpenHashMap();

    private void generatePrePortSpriteMapping() {
        Archive liveIdx = this.source.getArchive(ArchiveType.DEFAULTS);
        byte[] defaults = liveIdx.findGroupByID(3).findFileByID(0).getData().getBuffer();
        SpriteDefaults defaultData = SpriteDefaults.decode(new ByteBuffer(defaults));
        MANUAL_SPRITE_GROUPS.put(defaultData.mapScenes, 317);
    }


    private void portSprites() {
        Archive liveIdx = this.source.getArchive(ArchiveType.SPRITES);
        Archive oldIdx  = this.target.getArchive(ArchiveType.SPRITES);

        int live_count = liveIdx.getHighestGroupId();
        int old_count = oldIdx.getHighestGroupId();

        logger.info("Porting sprite index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            Group group = liveIdx.findGroupByID(port_index);
            if(group == null) {
                port_index++;
                continue;
            }
            oldIdx.addGroup(group);
            successful_ports++;
            port_index++;
        }

        for(Int2IntMap.Entry i: MANUAL_SPRITE_GROUPS.int2IntEntrySet()) {
            Group group = liveIdx.findGroupByID(i.getIntKey());
            group.setID(i.getIntValue());
            oldIdx.addGroup(group);
            logger.warn("Migrated default sprite group {} to {}", i.getIntKey(), i.getIntValue());
        }

        logger.info("Successfully ported " + successful_ports + " sprites from latest OSRS cache");
    }

    private void portTextures() {
        Archive oldConfigs = target.getArchive(ArchiveType.TEXTURES);
        Group oldTextures = oldConfigs.findGroupByID(0);

        Archive newConfigs = source.getArchive(ArchiveType.TEXTURES);
        Group newTextures = newConfigs.findGroupByID(0);

        int live_count = newTextures.getHighestFileId();
        int old_count = oldTextures.getHighestFileId();

        logger.info("Porting textures index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            File file = newTextures.findFileByID(port_index);
            if(file == null) {
                port_index++;
                continue;
            }
            oldTextures.addFile(file);
            successful_ports++;
            port_index++;
        }

        logger.info("Successfully ported " + successful_ports + " textures from latest OSRS cache");
    }

    private void portSequences() {
        Archive oldConfigs = target.getArchive(ArchiveType.CONFIGS);
        Group oldSequences = oldConfigs.findGroupByID(GroupType.SEQUENCE);

        Archive newConfigs = source.getArchive(ArchiveType.CONFIGS);
        Group newSequences = newConfigs.findGroupByID(GroupType.SEQUENCE);

        int live_count = newSequences.fileCount();
        int old_count = oldSequences.fileCount();

        logger.info("Porting sequences index with new group ct {} into old group ct {}", live_count, old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            File file = newSequences.findFileByID(port_index);
            if(file == null) {
                port_index++;
                continue;
            }
            if(portSequencesDirect) {
                oldSequences.addFile(file);
            } else {
                AnimationDefinitions def = AnimationDefinitions.decodeNew(file.getID(), file.getData());
                File newFile = new File(file.getID(), def.encode());
                oldSequences.addFile(newFile);
            }
            successful_ports++;
            port_index++;
        }

        logger.info("Successfully ported " + successful_ports + " sequences from latest OSRS cache");
    }

    private void portNPCs() {
        Archive oldConfigs = target.getArchive(ArchiveType.CONFIGS);
        Group oldNpcs = oldConfigs.findGroupByID(GroupType.NPC);

        Archive newConfigs = source.getArchive(ArchiveType.CONFIGS);
        Group newNpcs = newConfigs.findGroupByID(GroupType.NPC);

        int live_count = newNpcs.getHighestFileId();
        int old_count = oldNpcs.getHighestFileId();

        logger.info("Porting npcs index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            File file = newNpcs.findFileByID(port_index);
            if(file == null) {
                port_index++;
                continue;
            }
            NPCDefinitions definitions = new NPCDefinitions(file.getID(), file.getData());
            File newFile = new File(file.getID(), definitions.encode());

            oldNpcs.addFile(newFile);
            successful_ports++;
            port_index++;
        }

        logger.info("Successfully ported " + successful_ports + " npcs from latest OSRS cache");
    }

    private void portModels() {
        Archive liveIdx = this.source.getArchive(ArchiveType.MODELS);
        Archive oldIdx  = this.target.getArchive(ArchiveType.MODELS);

        int live_count = liveIdx.getHighestGroupId();
        int old_count = oldIdx.getHighestGroupId();

        logger.info("Porting model index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            Group group = liveIdx.findGroupByID(port_index);
            if(group == null) {
                port_index++;
                continue;
            }
            oldIdx.addGroup(group);
            successful_ports++;
            port_index++;
        }


        for(int i: manualModelList) {
            Group group = liveIdx.findGroupByID(i);
            oldIdx.addGroup(group);
        }

        logger.info("Successfully ported " + successful_ports + " models from latest OSRS cache");
    }

    private void portSkeletons() {
        Archive liveIdx = this.source.getArchive(ArchiveType.FRAMES);
        Archive oldIdx  = this.target.getArchive(ArchiveType.FRAMES);

        int live_count = liveIdx.getHighestGroupId();
        int old_count = oldIdx.getHighestGroupId();

        logger.info("Porting skeleton index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            Group group = liveIdx.findGroupByID(port_index);
            if(group == null) {
                port_index++;
                continue;
            }
            oldIdx.addGroup(group);
            successful_ports++;
            port_index++;
        }

        logger.info("Successfully ported " + successful_ports + " skeletons from latest OSRS cache");
    }

    private void portSkins() {
        Archive liveIdx = this.source.getArchive(ArchiveType.BASES);
        Archive oldIdx  = this.target.getArchive(ArchiveType.BASES);

        int live_count = liveIdx.getFreeGroupID();
        int old_count = oldIdx.getHighestGroupId();

        logger.info("Porting skins (bases) index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            Group group = liveIdx.findGroupByID(port_index);
            if(group == null) {
                port_index++;
                continue;
            }
            oldIdx.addGroup(group);
            successful_ports++;
            port_index++;
        }

        logger.info("Successfully ported " + successful_ports + " skins from latest OSRS cache");
    }

    private void portItems() {
        Archive oldConfigs = target.getArchive(ArchiveType.CONFIGS);
        Group oldItems = oldConfigs.findGroupByID(GroupType.ITEM);

        Archive newConfigs = source.getArchive(ArchiveType.CONFIGS);
        Group newItems = newConfigs.findGroupByID(GroupType.ITEM);

        int live_count = newItems.getHighestFileId();
        int old_count = oldItems.getHighestFileId();

        logger.info("Porting items index with new group ct " + live_count + " into old group ct " + old_count);

        IntArrayList manualOverwrites = IntArrayList.of(ItemId.VIRTUS_MASK, ItemId.VIRTUS_ROBE_TOP, ItemId.VIRTUS_ROBE_LEGS, 26242, 26244, 26246);
        // We overwrite item definitions because some older items have new model ids now
        int port_index = old_count;
        int successful_ports = 0;
        for(int id: manualOverwrites) {
            File file = newItems.findFileByID(id);
            if(file == null) {
                continue;
            }
            oldItems.addFile(file);
        }

        while(port_index < live_count) {
            File file = newItems.findFileByID(port_index);
            if(file == null) {
                port_index++;
                continue;
            }
            oldItems.addFile(file);
            successful_ports++;
            port_index++;
        }

        logger.info("Successfully ported " + successful_ports + " items from latest OSRS cache");
    }

    public void preload() {

    }
}
