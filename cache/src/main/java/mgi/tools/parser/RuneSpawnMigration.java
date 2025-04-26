package mgi.tools.parser;

import mgi.tools.DataMigration;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.GroupType;
import mgi.tools.jagcached.cache.*;

import java.util.Arrays;

import static mgi.tools.parser.TypeParser.POST_PACK_UNIV_SHOP;

public class RuneSpawnMigration {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RuneSpawnMigration.class);

    Cache newCache;
    Cache runespawn;
    public RuneSpawnMigration(Cache cache, Cache runespawn) {
        this.newCache = cache;
        this.runespawn = runespawn;
    }

    public void run() {
        portDBTables();
        if(!POST_PACK_UNIV_SHOP)
            portDBRows();
        portDBColumns();
        portInterfaces(5003);
        //portEnums(20002);

    }

    private void portEnums(int... i) {
        Archive arc = runespawn.getArchive(ArchiveType.CONFIGS);
        Group grp = arc.findGroupByID(GroupType.ENUM);
        Archive newCacheArchive = newCache.getArchive(ArchiveType.CONFIGS);
        Group newGroup = newCacheArchive.findGroupByID(GroupType.ENUM);
        for(int enumid : i) {
            File file = grp.findFileByID(enumid);
            newGroup.addFile(file);
        }
    }

    private void portDBColumns() {
        Archive oldConfigs = newCache.getArchive(ArchiveType.CONFIGS);
        Group oldRows = oldConfigs.findGroupByID(GroupType.DBTABLE);

        Archive newConfigs = runespawn.getArchive(ArchiveType.CONFIGS);
        Group newRows = newConfigs.findGroupByID(GroupType.DBTABLE);

        int live_count = newRows.getHighestFileId();
        int old_count = oldRows.getHighestFileId();

        logger.info("Porting db_tables config index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            File file = newRows.findFileByID(port_index);
            if(file == null) {
                port_index++;
                continue;
            }
            oldRows.addFile(file);
            successful_ports++;
            port_index++;
        }

        logger.info("Successfully ported " + successful_ports + " db tables configs from latest OSRS cache");
    }

    private void portDBRows() {
        Archive oldConfigs = newCache.getArchive(ArchiveType.CONFIGS);
        Group oldRows = oldConfigs.findGroupByID(GroupType.DBROW);

        Archive newConfigs = runespawn.getArchive(ArchiveType.CONFIGS);
        Group newRows = newConfigs.findGroupByID(GroupType.DBROW);

        int live_count = newRows.getHighestFileId();
        int old_count = oldRows.getHighestFileId();

        logger.info("Porting db_rows config index with new group ct " + live_count + " into old group ct " + old_count);

        int port_index = old_count;
        int successful_ports = 0;

        while(port_index < live_count) {
            File file = newRows.findFileByID(port_index);
            if(file == null) {
                port_index++;
                continue;
            }
            oldRows.addFile(file);
            successful_ports++;
            port_index++;
        }

        logger.info("Successfully ported " + successful_ports + " db rows from latest OSRS cache");
    }

    private void portDBTables() {
        Archive liveIdx = this.runespawn.getArchive(ArchiveType.DBTABLEINDEX);
        Archive oldIdx  = this.newCache.getArchive(ArchiveType.DBTABLEINDEX);

        int live_count = liveIdx.getHighestGroupId();
        int old_count = oldIdx.getHighestGroupId();

        logger.info("Porting DB_TABLE index with new group ct " + live_count + " into old group ct " + old_count);

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

        logger.info("Successfully ported " + successful_ports + " DB Tables from latest OSRS cache");
    }

    private void portInterfaces(int... i) {
        Archive arc = runespawn.getArchive(ArchiveType.INTERFACES);
        Archive newCacheArchive = newCache.getArchive(ArchiveType.INTERFACES);
        for(int inter : i) {
            Group group = arc.findGroupByID(inter);
            newCacheArchive.addGroup(group);
        }
    }
}
