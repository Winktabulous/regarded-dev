package com.zenyte.game.content.skills.thieving;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.content.achievementdiary.diaries.FaladorDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.events.ServerLaunchEvent;
import com.zenyte.utils.TimeUnit;
import it.unimi.dsi.fastutil.ints.Int2LongMap;
import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;

/**
 * @author Kris | 24/03/2019 20:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WallSafe extends Action {
    private static final Animation start = new Animation(2247);
    private static final SoundEffect attemptSound = new SoundEffect(1243);
    private static final SoundEffect successSound = new SoundEffect(1238);
    private static final Animation endAnim = new Animation(2248);
    private static final Animation failAnimation = new Animation(1113);
    private static final Animation locAnim = new Animation(1111);
    private static final SoundEffect failSound = new SoundEffect(1383);
    private static final int floorTrap = 7227;
    private static final Int2LongOpenHashMap safesMap = new Int2LongOpenHashMap();
    private final WorldObject object;

    @Subscribe
    public static final void onServerLaunch(final ServerLaunchEvent event) {
        WorldTasksManager.schedule(() -> {
            for (final Int2LongMap.Entry map : safesMap.int2LongEntrySet()) {
                final int hash = map.getIntKey();
                final long time = map.getLongValue();
                if (time < System.currentTimeMillis()) {
                    final WorldObject object = World.getObjectWithType(hash, 10);
                    if (object != null) {
                        World.removeObject(object);
                    }
                }
            }
        }, 100, 100);
    }

    @Override
    public boolean start() {
        if (player.getSkills().getLevelForXp(SkillConstants.THIEVING) < 50) {
            player.sendMessage("You need a Thieving level of at least 50 to crack the wall safes.");
            return false;
        }
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage("You need some free space to crack the safe.");
            return false;
        }
        player.sendMessage("You start cracking the safe.");
        player.setAnimation(start);
        player.sendSound(attemptSound);
        delay(2);
        return true;
    }

    @Override
    public boolean process() {
        return World.exists(object);
    }

    public WallSafe(WorldObject object) {
        this.object = object;
    }

    @Override
    public int processWithDelay() {
        if (Utils.random(1) == 0) {
            player.setAnimation(start);
            player.sendSound(attemptSound);
            return 1;
        }
        player.lock(1);
        final boolean hasDisarmed = player.getTemporaryAttributes().remove("Wall safe disarmed") != null;
        if (Thieving.success(player, hasDisarmed ? 30 : 50)) {
            player.setAnimation(endAnim);
            player.sendSound(attemptSound);
            WorldTasksManager.schedule(() -> {
                player.getAchievementDiaries().update(FaladorDiary.CRACK_WALL_SAFE);
                player.sendSound(successSound);
                player.setAnimation(Animation.STOP);
                player.getSkills().addXp(SkillConstants.THIEVING, 70);
                player.sendMessage("You get some loot.");
                final WorldObject cracked = new WorldObject(object);
                cracked.setId(7238);
                World.spawnObject(cracked);
                WorldTasksManager.schedule(() -> World.spawnObject(object), 1);
                final int roll = Utils.random(SafeLoot.totalWeight);
                int current = 0;
                for (final WallSafe.SafeLoot loot : SafeLoot.values) {
                    if ((current += loot.weight) >= roll) {
                        player.getInventory().addItem(new Item(loot.id, loot.amount));
                        break;
                    }
                }
            });
        } else {
            player.setAnimation(endAnim);
            player.sendSound(attemptSound);
            WorldTasksManager.schedule(() -> {
                player.sendSound(failSound);
                player.setAnimation(failAnimation);
                player.setAnimation(Animation.STOP);
                if (!safesMap.containsKey(player.getLocation().getPositionHash())) {
                    World.spawnObject(new WorldObject(floorTrap, 10, 0, player.getLocation()));
                }
                final WorldObject object = World.getObjectWithType(player.getLocation(), 10);
                if (object != null) {
                    World.sendObjectAnimation(object, locAnim);
                }
                safesMap.put(player.getLocation().getPositionHash(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1));
                player.sendMessage("You slip and trigger a trap!");
                player.applyHit(new Hit(Utils.random(2, 6), HitType.REGULAR));
                WorldTasksManager.schedule(() -> player.setAnimation(Animation.STOP));
            });
        }
        return -1;
    }


    private enum SafeLoot {
        LOW_COINS(995, 200, 250),
        MEDIUM_COINS(995, 400, 250),
        HIGH_COINS(995, 800, 250),
        SAPPHIRE(1623, 1, 50),
        EMERALD(1621, 1, 30),
        RUBY(1619, 1, 15),
        DIAMOND(1617, 1, 5);

        private final int id;
        private final int amount;
        private final int weight;
        private static int totalWeight;
        private static final SafeLoot[] values = values();

        static {
            for (final WallSafe.SafeLoot value : values) {
                totalWeight += value.weight;
            }
        }

        SafeLoot(int id, int amount, int weight) {
            this.id = id;
            this.amount = amount;
            this.weight = weight;
        }
    }
}
