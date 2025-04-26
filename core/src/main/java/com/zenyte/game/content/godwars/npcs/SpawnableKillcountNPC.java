package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.Spawnable;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

/**
 * @author Kris | 20/11/2018 22:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public abstract class SpawnableKillcountNPC extends AbstractKillcountNPC implements Spawnable {

    static final IntSet specialNPCs = new IntOpenHashSet(new int[]{2218, 3174, 3183, 3182, 487, 3165, 3175, 2211, 2242, 3173, 3132, 2209, 486, 485, 3168, 2244, 4813, 3131, 3160, 2216, 2208, 3164, 3140, 2212, 2215, 2217, 3162, 3129, 3179, 3178, 3138, 3163, 3161, 3130, 3171, 2206, 2205, 484, 2207, 3172, 3170, 3169, 2249, 3181, 3176, 3177, 3180, 2210, 2243, 3159, 3166});

    public SpawnableKillcountNPC(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }
}
