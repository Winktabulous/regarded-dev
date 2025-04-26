package com.zenyte.game.world.entity.npc.impl.slayer.superior.impl;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.impl.slayer.DeathSpawn;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 28/05/2019 02:18
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Nechryarch extends SuperiorNPC implements CombatScript {
    private static final Graphics TELEPORT_GRAPHICS = new Graphics(333);
    private static final Animation SPECIAL_ATTACK_ANIMATION = new Animation(1529);
    private static final byte[][] BASIC_OFFSETS = new byte[][] {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
    private final List<NPC> spawns = new ArrayList<>();
    private int spawnCount;

    public Nechryarch(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7411, tile);
        spawnCount = 0;
    }

    public void onDeath(final Entity source) {
        super.onDeath(source);
        for (final NPC spawn : spawns) {
            spawn.sendDeath();
        }
        spawnCount = 0;
    }

    @Override
    public int attack(Entity target) {
        setAnimation(getCombatDefinitions().getAttackAnim());
        delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        if (Utils.random(5) == 0) {
            for (int i = 0; i < 3; i++) {
                Location tile;
                int tryCount = 0;
                while (true) {
                    if (tryCount++ == 20) {
                        return getCombatDefinitions().getAttackSpeed();
                    }
                    final byte[] offsets = BASIC_OFFSETS[Utils.random(3)];
                    if (World.isTileFree(tile = new Location(getX() + offsets[0], getY() + offsets[1], getPlane()), 1)) {
                        break;
                    }
                }
                if (spawnCount <= 3) {
                    final DeathSpawn spawn = new DeathSpawn(i == 0 ? 6716 : i == 1 ? 6723 : 7649, tile, Direction.SOUTH, 5);
                    spawn.spawn();
                    spawns.add(spawn);
                    spawnCount += 1;
                    setAnimation(SPECIAL_ATTACK_ANIMATION);
                    World.sendGraphics(TELEPORT_GRAPHICS, tile);
                    spawn.getCombat().setTarget(target);
                }
            }
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    public int getSpawnCount() {
        return spawnCount;
    }

    public void setSpawnCount(int spawnCount) {
        this.spawnCount = spawnCount;
    }
}
