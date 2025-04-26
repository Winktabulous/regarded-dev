package mgi.tools.dumpers;

import mgi.tools.jagcached.cache.Group;
import mgi.utilities.ByteBuffer;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.GZIPInputStream;

public class GzipExtractor {

    public static void main(String[] args) {
        for (final File file : Objects.requireNonNull(Paths.get("cache/assets/cs2/ng_store/").toFile().listFiles())) {
            try {
                if(file.isDirectory())
                    continue;
                final String name = file.getName().replace(".cs2.pack", "");
                final int groupId = Integer.parseInt(name);
                byte[] decompress = decompressGzipToBytes(file.toPath());
                Files.write(Paths.get("cache/assets/cs2/ng_store/", groupId + ".cs2"), decompress);
            } catch (Exception e) {
                System.err.println("Could not pack sprite '" + file + "'");
                e.printStackTrace();
            }
        }
    }

    public static byte[] decompressGzipToBytes(Path source) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            FileInputStream fs = new FileInputStream(source.toFile());
            byte[] fsb = fs.readAllBytes();
            output.write(fsb, 5, fsb.length - 5 - 2);
        }catch (Exception ignored) {

        }

        return output.toByteArray();
    }
}
