package com.zenyte.game.content.minigame.motherlode;

import com.zenyte.game.content.skills.mining.MiningDefinitions;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCObjectEvent;
import com.zenyte.game.world.entity.pathfinding.events.player.ObjectEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.ObjectStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.game.world.region.GlobalAreaManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Optional;

import static com.zenyte.game.content.skills.mining.actions.Mining.ROCKFALL_EXPLOSION;
import static com.zenyte.game.content.skills.mining.actions.Mining.ROCKFALL_PROJECTILE;

/**
 * @author Kris | 01/07/2019 15:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MotherlodeMiner extends NPC implements Spawnable {
    private static final Animation mining = new Animation(4021);
    private static final ForceTalk paydirt = new ForceTalk("Pay-dirt!");
    private static final int[] ids = new int[] {6567, 6565, 6645, 5813, 5606, 6571, 6570, 6572, 6569, 6568, 5814};
    private OreVein currentVein;
    private int delay;

    public MotherlodeMiner(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return ArrayUtils.contains(ids, id);
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public void processNPC() {
        final boolean moving = hasWalkSteps();
        if (!moving && (currentVein == null || !World.exists(currentVein))) {
            findVein().ifPresent(vein -> {
                setRouteEvent(new NPCObjectEvent(this, new ObjectStrategy(currentVein = vein)));
                delay = 2;
            });
            return;
        }
        if (!moving && !(currentVein == null || !World.exists(currentVein))) {
            if (getLocation().withinDistance(currentVein, 1)) {
                if (delay > 0) {
                    delay--;
                    return;
                }
                setFaceLocation(currentVein);
                setAnimation(mining);
                if (Utils.random(5) == 0) {
                    setForceTalk(paydirt);
                }
            } else {
                final Optional<WorldObject> rockfall = findRockfall();
                if (rockfall.isPresent()) {
                    final WorldObject rock = rockfall.get();
                    if (World.exists(rock)) {
                        setAnimation(mining);
                        setFaceLocation(rock);
                        WorldTasksManager.schedule(() -> {
                            if (World.exists(rock)) {
                                World.removeObject(rock);
                                WorldTasksManager.schedule(() -> {
                                    final int[] elements = new int[] {-1, 1};
                                    World.sendProjectile(rock.transform(elements[Utils.random(elements.length - 1)], elements[Utils.random(elements.length - 1)], 0), rock, ROCKFALL_PROJECTILE);
                                    World.sendProjectile(rock.transform(elements[Utils.random(elements.length - 1)], elements[Utils.random(elements.length - 1)], 0), rock, ROCKFALL_PROJECTILE);
                                    WorldTasksManager.schedule(() -> {
                                        CharacterLoop.forEach(rock, 1, Entity.class, entity -> {
                                            if (CollisionUtil.collides(rock.getX(), rock.getY(), 1, entity.getX(), entity.getY(), entity.getSize())) {
                                                if (entity instanceof Player) {
                                                    entity.applyHit(new Hit(Utils.random(1, 4), HitType.DEFAULT));
                                                }
                                                if (entity instanceof Player) {
                                                    entity.setRouteEvent(new ObjectEvent(((Player) entity), new ObjectStrategy(rock), null));
                                                } else {
                                                    entity.setRouteEvent(new NPCObjectEvent(((NPC) entity), new ObjectStrategy(rock)));
                                                }
                                            }
                                        });
                                        World.sendGraphics(ROCKFALL_EXPLOSION, rock);
                                        World.spawnObject(rock);
                                    });
                                }, MiningDefinitions.OreDefinitions.ROCKFALL.getTime());
                            }
                            findVein().ifPresent(vein -> {
                                setRouteEvent(new NPCObjectEvent(this, new ObjectStrategy(currentVein = vein)));
                                delay = 2;
                            });
                        });
                    }
                } else {
                    currentVein = null;
                }
            }
        } else {
            super.processNPC();
        }
    }

    private final Optional<WorldObject> findRockfall() {
        for (final Int2ObjectMap.Entry<WorldObject> rockfallEntry : GlobalAreaManager.getArea(MotherlodeArea.class).getRockfallMap().int2ObjectEntrySet()) {
            final WorldObject obj = rockfallEntry.getValue();
            if (!World.exists(obj) || !obj.withinDistance(getLocation(), 1)) {
                continue;
            }
            return Optional.of(obj);
        }
        return Optional.empty();
    }

    private final Optional<OreVein> findVein() {
        OreVein vein = null;
        final MotherlodeArea area = GlobalAreaManager.getArea(MotherlodeArea.class);
        int distance = Integer.MAX_VALUE;
        final int npcX = getX();
        final int npcY = getY();
        final Int2ObjectMap<OreVein> map = UpperMotherlodeArea.polygon.contains(getLocation()) ? area.getHigherOreMap() : area.getLowerOreMap();
        for (final Int2ObjectMap.Entry<OreVein> entry : map.int2ObjectEntrySet()) {
            final int hash = entry.getIntKey();
            final int x = Location.getX(hash);
            final int y = Location.getY(hash);
            final int currentDistance = Utils.getDistance(npcX, npcY, x, y);
            if (currentDistance < distance) {
                final OreVein currentVein = entry.getValue();
                if (World.exists(currentVein)) {
                    vein = currentVein;
                    distance = currentDistance;
                }
            }
        }
        return Optional.ofNullable(vein);
    }
}
