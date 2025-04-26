package com.zenyte.game.content.minigame.wintertodt;

import com.zenyte.game.content.boons.impl.SwissArmyMan;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.AnimationUtil;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.pathfinding.events.player.TileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import com.zenyte.plugins.item.LightSourceItem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Corey
 * @since 21:58 - 24/07/2019
 */
public class Brazier {
    private static final int TINDERBOX = 590;
    private static final int HAMMER = 2347;
    private final int x;
    private final int y;
    private int ticks;
    private State state = State.UNLIT;
    private boolean fireProjectiles = false;
    private boolean beingAttacked = false;

    Brazier(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void transitionState(final State state, final boolean refreshOverlay) {
        if (this.state == state) {
            return;
        }
        if (Wintertodt.betweenRounds()) {
            return;
        }
        this.state = state;
        this.ticks = 0;
        World.spawnObject(new WorldObject(this.state.getObjectId(), 10, 0, x, y, 0));
        if (refreshOverlay) {
            Wintertodt.refreshOverlayForAllPlayers();
        }
        if (isLit()) {
            WorldTasksManager.schedule(() -> fireProjectiles = true, 4);
        } else {
            fireProjectiles = false;
        }
    }

    void reset() {
        state = State.UNLIT;
        ticks = 0;
        fireProjectiles = false;
        beingAttacked = false;
        World.spawnObject(new WorldObject(state.getObjectId(), 10, 0, x, y, 0));
    }

    private void transitionState(final State state) {
        transitionState(state, true);
    }

    private boolean isUnlit() {
        return state == State.UNLIT;
    }

    public boolean isBroken() {
        return state == State.BROKEN;
    }

    boolean isLit() {
        return state == State.LIT;
    }

    void startSmallAttack() {
        beingAttacked = true;
        World.spawnTemporaryObject(new WorldObject(Wintertodt.SNOWFALL_OBJECT, 10, 0, getX() + 1, getY() + 1, 0), 5);
        WorldTasksManager.schedule(() -> {
            if (Wintertodt.betweenRounds()) {
                beingAttacked = false;
                return;
            }
            World.sendGraphics(new Graphics(502), new Location(x, y));
            WorldTasksManager.schedule(() -> {
                for (Player player : CharacterLoop.find(new Location(getX() + 1, getY() + 1), 2, Player.class, p -> true)) {
                    player.sendSound(new SoundEffect(2300));
                }
                transitionState(State.UNLIT);
                WorldTasksManager.schedule(() -> beingAttacked = false, 4);
            }, 1);
        }, 4);
    }

    void startLargeAttack() {
        beingAttacked = true;
        final Location centreTile = new Location(getX() + 1, getY() + 1);
        World.spawnTemporaryObject(new WorldObject(Wintertodt.SNOWFALL_OBJECT, 10, 0, centreTile.getX(), centreTile.getY(), 0), 5);
        for (final Direction direction : Direction.cardinalDirections) {
            final Location newTile = centreTile.transform(direction, 1);
            World.spawnTemporaryObject(new WorldObject(Wintertodt.SNOWFALL_OBJECT, 10, 0, newTile.getX(), newTile.getY(), 0), 5);
        }
        WorldTasksManager.schedule(() -> {
            if (Wintertodt.betweenRounds()) {
                beingAttacked = false;
                return;
            }
            final Pyromancer pyromancer = Wintertodt.Corner.getFromBrazier(this).getPyromancer();
            World.sendGraphics(new Graphics(502), new Location(x, y));
            transitionState(State.BROKEN);
            Wintertodt.checkAllBraziersBroken();
            if (pyromancer.isHealthy()) {
                WorldTasksManager.schedule(() -> pyromancer.applyHit(new Hit(Utils.random(5, 15), HitType.DEFAULT)));
            }
            final List<Player> players = CharacterLoop.find(new Location(getX() + 1, getY() + 1), 2, Player.class, p -> true);
            for (Player player : players) {
                final int damageAmount = 2 * Wintertodt.getBaseAOEDamageAmount(player);
                player.applyHit(new Hit(damageAmount, HitType.DEFAULT));
                player.sendMessage("The brazier is broken and shrapnel damages you.");
                player.sendSound(new SoundEffect(2299));
                player.sendSound(new SoundEffect(513));
            }
            WorldTasksManager.schedule(() -> beingAttacked = false, 4);
        }, 5);
    }

    public int getX() {
        return x;
    }


    public static final class BrazierAction implements ObjectAction, ItemOnObjectAction {
        private static final Int2ObjectMap<Location> brazierAccessTiles = new Int2ObjectOpenHashMap<>();
        private static final Set<Location> northEasternBrazierAccessPoints = new ObjectOpenHashSet<>();
        private static final Location northEasternBrazier = new Location(1638, 4015, 0);

        static {
            brazierAccessTiles.put(new Location(1620, 3997, 0).hashCode(), new Location(1622, 3999, 0));
            brazierAccessTiles.put(new Location(1638, 3997, 0).hashCode(), new Location(1638, 3999, 0));
            brazierAccessTiles.put(new Location(1620, 4015, 0).hashCode(), new Location(1622, 4015, 0));
            for (int y = 4015; y < 4018; y++) {
                northEasternBrazierAccessPoints.add(new Location(1641, y, 0));
            }
            for (int x = 1638; x < 1641; x++) {
                northEasternBrazierAccessPoints.add(new Location(x, 4018, 0));
            }
        }

        @Override
        public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
            final Brazier brazier = Wintertodt.Corner.getFromBrazier(object.getX(), object.getY()).getBrazier();
            if (option.equalsIgnoreCase("light") && brazier.isUnlit()) {
                if (!player.hasBoon(SwissArmyMan.class) && !player.getInventory().containsItem(TINDERBOX, 1) && !player.carryingItem(LightSourceItem.LightSource.BRUMA_TORCH.getLitId())) {
                    player.sendMessage("You need a tinderbox or bruma torch to light that brazier.");
                    return;
                }
                lightBrazier(player, brazier);
            } else if (option.equalsIgnoreCase("fix") && brazier.isBroken()) {
                if (!player.getInventory().containsItem(HAMMER, 1)) {
                    player.sendMessage("You need a hammer to fix this brazier.");
                    return;
                }
                fixBrazier(player, brazier);
            } else if (option.equalsIgnoreCase("feed") && brazier.isLit()) {
                player.getActionManager().setAction(new FeedBrazierAction(brazier));
            }
        }

        @Override
        public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
            if (item.getId() == TINDERBOX || item.getId() == LightSourceItem.LightSource.BRUMA_TORCH.getLitId()) {
                final Brazier brazier = Wintertodt.Corner.getFromBrazier(object.getX(), object.getY()).getBrazier();
                lightBrazier(player, brazier);
            }
            if (item.getId() == BrumaRoot.ROOT || item.getId() == BrumaRoot.KINDLING) {
                final Brazier brazier = Wintertodt.Corner.getFromBrazier(object.getX(), object.getY()).getBrazier();
                player.getActionManager().setAction(new FeedBrazierAction(brazier));
            }
        }

        private void lightBrazier(final Player player, final Brazier brazier) {
            if (Wintertodt.betweenRounds()) {
                player.sendFilteredMessage("There's no need to do that at this time.");
                return;
            }
            if (brazier.isBroken()) {
                player.sendFilteredMessage("Fix the brazier before lighting it.");
                return;
            }
            if (brazier.isLit()) {
                player.sendFilteredMessage("The brazier is already lit.");
                return;
            }
            final Pyromancer pyromancer = Wintertodt.Corner.getFromBrazier(brazier).getPyromancer();
            if (!pyromancer.isHealthy()) {
                player.sendMessage("Heal the Pyromancer before lighting the brazier.");
                return;
            }
            if (player.getTemporaryAttributes().containsKey("lighting_wintertodt_brazier")) {
                player.sendFilteredMessage("You are already lighting the brazier.");
                return;
            }
            player.setAnimation(new Animation(733));
            player.addTemporaryAttribute("lighting_wintertodt_brazier", true);
            WorldTasksManager.schedule(() -> {
                player.setAnimation(Animation.STOP);
                if (Wintertodt.betweenRounds()) {
                    player.getTemporaryAttributes().remove("lighting_wintertodt_brazier");
                    return;
                }
                brazier.transitionState(State.LIT);
                player.sendSound(new SoundEffect(2596));
                player.sendMessage("You light the brazier.");
                Wintertodt.addPoints(player, 25);
                player.getSkills().addXp(SkillConstants.FIREMAKING, 6 * player.getSkills().getLevelForXp(SkillConstants.FIREMAKING));
                player.getTemporaryAttributes().remove("lighting_wintertodt_brazier");
            }, 2);
        }

        private void fixBrazier(final Player player, final Brazier brazier) {
            if (!brazier.isBroken()) {
                return;
            }
            if (player.getTemporaryAttributes().containsKey("fixing_wintertodt_brazier")) {
                player.sendFilteredMessage("You are already fixing the brazier.");
                return;
            }
            player.addTemporaryAttribute("fixing_wintertodt_brazier", true);
            player.getCombatAchievements().complete(CAType.HANDYMAN);
            player.setAnimation(new Animation(3676));
            player.sendSound(new SoundEffect(930));
            WorldTasksManager.schedule(() -> {
                player.setAnimation(Animation.STOP);
                if (Wintertodt.betweenRounds()) {
                    player.getTemporaryAttributes().remove("fixing_wintertodt_brazier");
                    return;
                }
                brazier.transitionState(State.UNLIT);
                player.sendMessage("You fix the brazier.");
                Wintertodt.addPoints(player, 25);
                // TODO only award construction exp if user has a house already
//                if (player.getConstruction().getHouse() != null) {
//                    player.getSkills().addXp(Skills.CONSTRUCTION, 4 * player.getSkills().getLevelForXp(Skills.CONSTRUCTION));
//                }
                player.getTemporaryAttributes().remove("fixing_wintertodt_brazier");
            }, 2);
        }

        @Override
        public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
            pathfind(player, object, getRunnable(player, object, name, optionId, option));
        }

        @Override
        public void handle(final Player player, final Item item, int slot, final WorldObject object) {
            pathfind(player, object, () -> {
                player.stopAll();
                player.faceObject(object);
                handleItemOnObjectAction(player, item, slot, object);
            });
        }

        private void pathfind(final Player player, final WorldObject object, final Runnable runnable) {
            if (object.matches(northEasternBrazier)) {
                double distance = Double.MAX_VALUE;
                Location tile = null;
                for (final Location accessPoint : northEasternBrazierAccessPoints) {
                    final double distanceToAccessPoint = player.getLocation().getDistance(accessPoint);
                    if (distanceToAccessPoint < distance) {
                        distance = distanceToAccessPoint;
                        tile = accessPoint;
                    }
                }
                final Location destinationTile = Objects.requireNonNull(tile);
                player.setRouteEvent(new TileEvent(player, new TileStrategy(destinationTile), runnable));
                return;
            }
            final Location destinationTile = Objects.requireNonNull(brazierAccessTiles.get(object.getPositionHash()));
            player.setRouteEvent(new TileEvent(player, new TileStrategy(destinationTile, 3), runnable));
        }

        @Override
        public Object[] getItems() {
            return new Object[] {TINDERBOX, BrumaRoot.ROOT, BrumaRoot.KINDLING, LightSourceItem.LightSource.BRUMA_TORCH.getLitId()};
        }

        @Override
        public Object[] getObjects() {
            return new Object[] {State.UNLIT.getObjectId(), State.BROKEN.getObjectId(), State.LIT.getObjectId()};
        }
    }

    public int getY() {
        return y;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public State getState() {
        return state;
    }

    public boolean isFireProjectiles() {
        return fireProjectiles;
    }

    public boolean isBeingAttacked() {
        return beingAttacked;
    }

    public void setBeingAttacked(boolean beingAttacked) {
        this.beingAttacked = beingAttacked;
    }


    public enum State {
        UNLIT(29312, 1, "Light this brazier!"), BROKEN(29313, 0, "Fix this brazier!"), LIT(29314, 2, "Yemalo shi cardito!");
        private final int objectId;
        private final int cs2Value;
        private final String pryomancerPhrase;

        State(int objectId, int cs2Value, String pryomancerPhrase) {
            this.objectId = objectId;
            this.cs2Value = cs2Value;
            this.pryomancerPhrase = pryomancerPhrase;
        }

        public int getObjectId() {
            return objectId;
        }

        public int getCs2Value() {
            return cs2Value;
        }

        public String getPryomancerPhrase() {
            return pryomancerPhrase;
        }
    }


    private static final class FeedBrazierAction extends Action {
        static final Animation FEED_ANIM = new Animation(832);
        private final Brazier brazier;

        private boolean hasFuel() {
            if (!player.getInventory().containsAnyOf(BrumaRoot.ROOT, BrumaRoot.KINDLING)) {
                player.sendMessage("You have run out of bruma roots.");
                return false;
            }
            return true;
        }

        @Override
        public boolean start() {
            if (!hasFuel() || !brazier.isLit()) {
                return false;
            }
            player.sendSound(new SoundEffect(2596));
            player.setAnimation(FEED_ANIM);
            delay((AnimationUtil.getCeiledDuration(FEED_ANIM) / 600) + 1);
            return true;
        }

        @Override
        public boolean process() {
            if (!brazier.isLit()) {
                player.sendMessage("The brazier has gone out.");
                return false;
            }
            return hasFuel();
        }

        public FeedBrazierAction(Brazier brazier) {
            this.brazier = brazier;
        }

        @Override
        public void stop() {
        }

        @Override
        public int processWithDelay() {
            final boolean hasKindling = player.getInventory().containsItem(BrumaRoot.KINDLING, 1);
            player.getInventory().deleteItem(hasKindling ? BrumaRoot.KINDLING : BrumaRoot.ROOT, 1);
            if (hasKindling) {
                player.getSkills().addXp(SkillConstants.FIREMAKING, player.getSkills().getLevelForXp(SkillConstants.FIREMAKING) * 3.8);
                Wintertodt.addPoints(player, 25);
            } else {
                player.getSkills().addXp(SkillConstants.FIREMAKING, player.getSkills().getLevelForXp(SkillConstants.FIREMAKING) * 3);
                Wintertodt.addPoints(player, 10);
            }
            player.sendSound(new SoundEffect(2596, 0, 15));
            player.setAnimation(FEED_ANIM);
            return (AnimationUtil.getCeiledDuration(FEED_ANIM) / 600);
        }
    }
}
