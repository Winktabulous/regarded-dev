package mgi.tools.dumpers;

import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.Cache;
import mgi.tools.jagcached.cache.Group;

import java.io.*;

public class InterfaceDumper {
    public static void main(String[] args) throws IOException {
        File directory = new File("dumps/interfaces/");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        final Cache cache = Cache.openCache("cache/data/cache/");
        final int id = 629;
        final Archive archive = cache.getArchive(ArchiveType.INTERFACES);
        final Group group = archive.findGroupByID(id);
        if (group == null) {
            System.err.println("Script doesn't exist!");
            return;
        }
        for(mgi.tools.jagcached.cache.File file: group.getFiles()) {
            final DataOutputStream dos = new DataOutputStream(new FileOutputStream(new File(directory, file.getID() + "")));
            dos.write(file.getData().getBuffer());
        }
    }
}
