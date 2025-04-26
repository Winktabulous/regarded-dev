package com.zenyte.tools;

import com.zenyte.Main;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawn;
import com.zenyte.game.world.entity.npc.spawns.NPCSpawnLoader;
import com.zenyte.utils.MapPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Kris | 01/05/2019 16:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NPCSpawnPrinter implements MapPrinter {

    private static final Logger log = LoggerFactory.getLogger(NPCSpawnPrinter.class);

    public static final void main(final String[] args) throws IOException, InterruptedException {
        Main.main(new String[] {});
        final ArrayList<Callable<Void>> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            final int plane = i;
            list.add(() -> {
                new NPCSpawnPrinter().load(plane);
                return null;
            });
        }
        ForkJoinPool.commonPool().invokeAll(list);
        System.exit(-1);
    }

    @Override
    public String path(final int plane) {
        return "data/map/produced npc global areas image " + plane + ".png";
    }

    @Override
    public void draw(final Graphics2D graphics, final int plane) throws IOException {
        log.info("Drawing map image");

        for (final NPCSpawn spawn : NPCSpawnLoader.DEFINITIONS) {
            if (spawn.getZ() != plane) {
                continue;
            }
            graphics.setColor(Color.BLACK);
            graphics.drawOval(getX(spawn.getX()), getY(spawn.getY()), 6, 6);
            graphics.setColor(Color.CYAN);
            graphics.fillOval(getX(spawn.getX()), getY(spawn.getY()), 5, 5);
        }
    }
}
