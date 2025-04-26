package com.zenyte.game.content.chambersofxeric.skills;

import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Optional;
import java.util.Set;

/**
 * @author Kris | 2. mai 2018 : 20:25:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class RaidFarming {

    /**
     * A map of <patch tile, patch object> containing all the patches in this raid.
     */
    private final Int2ObjectMap<HerbPatch> patches = new Int2ObjectOpenHashMap<>();

    /**
     * Executed every tick, starting at the point when the raid is initiated by
     * the owner, and finishing when the map is destroyed. Grows all the
     * existing patches.
     */
    public void process() {
        if (patches.isEmpty()) {
            return;
        }
        patches.forEach((k, v) -> {
            if (v.stage < 4) {
                if (++v.ticks >= 12) {
                    v.stage++;
                    v.ticks = 0;
                    World.spawnObject(v.currentPatch = new WorldObject(v.type.objectId + (v.stage * 3), v.patch.getType(), v.patch.getRotation(), v.patch));
                }
            }
        });
    }

    /**
     * Handles clicking on the patch objects.
     *
     * @param player the player interacting with the patch.
     * @param object the patch object
     * @param option the option the player clicked.
     */
    public void handle(final Player player, final WorldObject object, final String option) {
        final RaidFarming.HerbPatch patch = patches.get(object.getPositionHash());
        switch(option) {
            case "Inspect":
                player.sendMessage("You inspect the herb patch...");
                player.lock(1);
                WorldTasksManager.schedule(() -> {
                    if (patch == null) {
                        player.sendMessage("This patch is currently empty. The soil looks to be imbued by a magical power, not many seeds would be capable of growing here.");
                        return;
                    }
                    switch(patch.stage) {
                        case 0:
                            player.sendMessage("There's a freshly planted " + patch.type.toString().toLowerCase() + " plant growing here.");
                            return;
                        case 1:
                            player.sendMessage("There's a " + patch.type.toString().toLowerCase() + " plant growing here.");
                            return;
                        case 2:
                            player.sendMessage("There's a " + patch.type.toString().toLowerCase() + " plant growing here. It looks to be about half-grown.");
                            return;
                        case 3:
                            player.sendMessage("There's a " + patch.type.toString().toLowerCase() + " plant growing here. " + "It's almost ready for harvesting.");
                            return;
                        case 4:
                            player.sendMessage("There's a " + patch.type.toString().toLowerCase() + " plant growing here. " + "It's ready to be harvested.");
                    }
                }, 1);
                break;
            case "Clear":
                {
                    if (patch == null) {
                        return;
                    }
                    player.getActionManager().setAction(new HerbClearAction(this, patch));
                    return;
                }
            case "Pick":
                {
                    if (patch == null) {
                        return;
                    }
                    player.getActionManager().setAction(new HerbHarvestAction(this, patch));
                }
        }
    }

    /**
     * Handles planting a seed into the patch.
     *
     * @param player the player attempting to plant the seed.
     * @param object the patch object.
     * @param item   the seed.
     */
    public void plant(final Player player, final WorldObject object, final Item item) {
        if (object.getId() != ObjectId.HERB_PATCH_29765) {
            player.sendMessage("There are already herbs growing there!");
            return;
        }
        if (object.isLocked()) {
            player.sendMessage("Someone else is already planting something here.");
            return;
        }
        final PatchType type = PatchType.map.get(item.getId());
        if (type == null) {
            player.sendMessage("You can't plant that!");
            return;
        }
        player.getActionManager().setAction(new HerbPlantAction(this, object, type));
    }

    /**
     * An enum containing information about all three special raid farming patches.
     */
    private enum PatchType {

        GOLPAR(29998, 20906, 20904, 27, 4, 10), BUCHU(29999, 20909, 20907, 39, 6, 15), NOXIFER(29997, 20903, 20901, 55, 12, 30);

        private static final PatchType[] values = values();

        private static final Int2ObjectMap<PatchType> map = new Int2ObjectOpenHashMap<>(values.length);

        static {
            for (final PatchType val : values) {
                map.put(val.seedId, val);
            }
        }

        private final int objectId;

        private final int seedId;

        private final int produceId;

        private final int level;

        private final int plantingXp;

        private final int harvestingXp;

        PatchType(int objectId, int seedId, int produceId, int level, int plantingXp, int harvestingXp) {
            this.objectId = objectId;
            this.seedId = seedId;
            this.produceId = produceId;
            this.level = level;
            this.plantingXp = plantingXp;
            this.harvestingXp = harvestingXp;
        }
    }

    /**
     * The action executed when the player begins picking herbs from the patches.
     */
    private static final class HerbHarvestAction extends Action {

        /**
         * The animation played when the player picks a herb.
         */
        private static final Animation herbPickingAnimation = new Animation(2282);

        /**
         * The sound effect played when the player picks a herb.
         */
        private static final SoundEffect sound = new SoundEffect(2581);

        /**
         * The farming controller object which manages all farming related things in this raid.
         */
        private final RaidFarming farming;

        /**
         * The herb patch we're currently picking herbs from.
         */
        private final HerbPatch patch;

        HerbHarvestAction(final RaidFarming farming, final HerbPatch patch) {
            this.farming = farming;
            this.patch = patch;
        }

        @Override
        public boolean start() {
            if (player.getSkills().getLevel(SkillConstants.FARMING) < patch.type.level) {
                player.sendMessage("You need a Farming level of at least " + patch.type.level + " to harvest these herbs.");
                return false;
            }
            player.setAnimation(herbPickingAnimation);
            player.sendMessage("You begin to harvest the herb patch.");
            delay(2);
            return true;
        }

        @Override
        public boolean process() {
            for (final String clearingPlayer : patch.clearingPlayers) {
                final Optional<Player> player = World.getPlayer(clearingPlayer);
                if (!player.isPresent()) {
                    continue;
                }
                final Player p = player.get();
                if (p.isNulled()) {
                    continue;
                }
                final Action action = p.getActionManager().getAction();
                if (!(action instanceof HerbClearAction)) {
                    continue;
                }
                if (((HerbClearAction) action).patch == patch) {
                    this.player.sendMessage("This farming patch is about to be cleared.");
                    return false;
                }
            }
            return true;
        }

        @Override
        public int processWithDelay() {
            if (!World.containsSpawnedObject(patch.currentPatch)) {
                return -1;
            }
            if (patch.lives <= 0) {
                return -1;
            }
            player.sendSound(sound);
            player.getSkills().addXp(SkillConstants.FARMING, patch.type.harvestingXp);
            player.getInventory().addOrDrop(new Item(patch.type.produceId, 1));
            final int req = patch.type.level;
            final double chance = Math.min(0.95, 0.65 + ((player.getSkills().getLevel(SkillConstants.FARMING) - req) / 250.0F));
            if (chance < Utils.getRandomDouble(1)) {
                patch.lives--;
                if (patch.lives <= 0) {
                    farming.patches.remove(patch.patch.getPositionHash());
                    World.spawnObject(patch.patch);
                    player.sendMessage("The herb patch is now empty.");
                    return -1;
                }
            }
            player.setAnimation(herbPickingAnimation);
            return 2;
        }
    }

    /**
     * The patch clear action when the player doesn't want to harvest the herbs, but rather wants to clear the patch out.
     */
    private static final class HerbClearAction extends Action {

        /**
         * The regular spade using animation.
         */
        private static final Animation spadeAnimation = new Animation(830);

        /**
         * The spade item.
         */
        private static final Item spade = new Item(952);

        /**
         * The sound effect played when digging up the patch.
         */
        private static final SoundEffect sound = new SoundEffect(1470);

        /**
         * The farming controller object which manages all farming related things in this raid.
         */
        private final RaidFarming farming;

        /**
         * The herb patch we're currently picking herbs from.
         */
        private final HerbPatch patch;

        HerbClearAction(final RaidFarming farming, final HerbPatch patch) {
            this.farming = farming;
            this.patch = patch;
        }

        @Override
        public boolean start() {
            final RaidFarming.PatchType type = patch.type;
            if (player.getSkills().getLevel(SkillConstants.FARMING) < type.level) {
                player.sendMessage("You need a Farming level of at least " + type.level + " to clear " + type.toString().toLowerCase() + " patch.");
                return false;
            }
            if (!player.getInventory().containsItem(spade)) {
                player.sendMessage("You need a spade to clear this patch.");
                return false;
            }
            player.setAnimation(spadeAnimation);
            player.sendSound(sound);
            delay(1);
            patch.clearingPlayers.add(player.getUsername());
            return true;
        }

        @Override
        public boolean process() {
            return true;
        }

        @Override
        public int processWithDelay() {
            player.sendMessage("You clear the patch...");
            player.getRaid().ifPresent(raid -> raid.sendGlobalMessage(player.getName() + " has cleared the farming patch."));
            patch.lives = 0;
            farming.patches.remove(patch.patch.getPositionHash());
            World.spawnObject(patch.patch);
            return -1;
        }

        @Override
        public void stop() {
            patch.clearingPlayers.remove(player.getUsername());
        }
    }

    /**
     * The action executed when the player plants a herb seed into the raid herb patch.
     */
    private static final class HerbPlantAction extends Action {

        /**
         * The animation played when the player dips a seed into the ground in the farming patch.
         */
        private static final Animation seedDippingAnimation = new Animation(2291);

        /**
         * The seed dibber item.
         */
        private static final Item seedDibber = new Item(5343);

        /**
         * The sound effect played when the player dips a seed into the ground in the herb patch.
         */
        private static final SoundEffect sound = new SoundEffect(2432);

        /**
         * The farming controller object which manages all farming related things in this raid.
         */
        private final RaidFarming farming;

        /**
         * The patch object which we are planting the herb seed into.
         */
        private final WorldObject object;

        /**
         * The type of the patch that it will grow into based on the seed.
         */
        private final PatchType type;

        HerbPlantAction(final RaidFarming farming, final WorldObject object, final PatchType type) {
            this.farming = farming;
            this.object = object;
            this.type = type;
        }

        @Override
        public boolean start() {
            if (player.getSkills().getLevel(SkillConstants.FARMING) < type.level) {
                player.sendMessage("You need a Farming level of at least " + type.level + " to plant " + type.toString().toLowerCase() + " seeds.");
                return false;
            }
            if (!player.getInventory().containsItem(seedDibber)) {
                player.sendMessage("You need a seed dibber to plant seeds.");
                return false;
            }
            if (!player.getInventory().containsItem(type.seedId, 1)) {
                player.sendMessage("You need some " + type.toString().toLowerCase() + " seeds to do this.");
                return false;
            }
            object.setLocked(true);
            player.setAnimation(seedDippingAnimation);
            player.sendSound(sound);
            delay(1);
            return true;
        }

        @Override
        public boolean process() {
            return true;
        }

        @Override
        public int processWithDelay() {
            if (!player.getInventory().containsItem(type.seedId, 1)) {
                return -1;
            }
            player.sendMessage("You plant a " + type.toString().toLowerCase() + " in the herb patch.");
            final RaidFarming.HerbPatch herbPatch = new HerbPatch(object, type);
            farming.patches.put(object.getPositionHash(), herbPatch);
            World.spawnObject(herbPatch.currentPatch = new WorldObject(type.objectId, object.getType(), object.getRotation(), object));
            object.setLocked(false);
            player.getSkills().addXp(SkillConstants.FARMING, type.plantingXp);
            player.getInventory().deleteItem(type.seedId, 1);
            return -1;
        }

        @Override
        public void stop() {
            object.setLocked(false);
        }
    }

    /**
     * The herb patch object, containing all the necessary information about the herb patch.
     */
    private static final class HerbPatch {

        /**
         * The empty patch game object which is transformed by spawning another object on-top of it whenever it grows, unlike normal farming, raid farming is not ran on varbits.
         */
        private final WorldObject patch;

        /**
         * The type of the herb that was planed into this patch.
         */
        private final PatchType type;

        /**
         * The current patch game object, whatever the default patch has been transformed into.
         */
        private WorldObject currentPatch;

        /**
         * The stage of growth that the patch is currently at.
         */
        private int stage;

        /**
         * The number of lives the patch has remaining. Every time a player picks a herb from the patch, there's a chance a life is consumed. When all lives are consumed, the patch
         * is cleared out.
         */
        private int lives = 3;

        /**
         * The number of ticks the patch has been growing for some the last growth cycle. Resets every new cycle, taking 12 ticks to grow to another cycle.
         */
        private int ticks;

        /**
         * A set of players' usernames who are currently attempting to clear the patch.
         */
        private final Set<String> clearingPlayers = new ObjectOpenHashSet<>();

        HerbPatch(final WorldObject patch, final PatchType type) {
            this.patch = patch;
            this.type = type;
        }
    }
}
