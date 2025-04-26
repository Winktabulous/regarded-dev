package com.zenyte.tools;

import com.zenyte.game.GameConstants;
import mgi.types.config.StructDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public final class StructExtractor implements Extractor {

    private static final Logger log = LoggerFactory.getLogger(StructExtractor.class);

    @Override
    public void extract() {
        try {
            //final int len = CacheUtil.getStore().getFilesSystem(2).findFolderByID(8).filesCount();
            final BufferedWriter writer = new BufferedWriter(new FileWriter(new File("info/#" + GameConstants.REVISION + " struct-list.txt")));
            for (final StructDefinitions t : StructDefinitions.definitions) {
                if (t == null) {
                    continue;
                }
                try {
                    writer.write("[" + t.getId() + "]\r\n");
                    writer.write("val=" + t.printParams() + "\r\n");
                    writer.write("-----");
                    writer.write("\r\n");
                } catch(Exception id) {
                    continue;
                }

            }
            writer.flush();
            writer.close();
        } catch (final Exception e) {
            log.error("", e);
        }
    }

}
