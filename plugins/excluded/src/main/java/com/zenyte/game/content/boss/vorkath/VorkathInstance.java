package com.zenyte.game.content.boss.vorkath;

import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.ForceMovement;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.MagicCombat;
import com.zenyte.game.world.entity.player.action.combat.RangedCombat;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.*;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.zenyte.game.world.entity.player.Player.DEATH_ANIMATION;

/**
 * @author Kris | 1. juuli 2018 : 17:49:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class VorkathInstance extends DynamicArea implements CycleProcessPlugin, FullMovementPlugin, DeathPlugin, CannonRestrictionPlugin, DropPlugin, LootBroadcastPlugin {
    private static final Animation CLIMB_OVER_WALL_ANIM = new Animation(839);
    private static final Location INSIDE_TILE = new Location(2272, 4054, 0);
    private static final Location OUTSIDE_TILE = new Location(2272, 4052, 0);
    //TODO: Turn icy chunks into 31824
    private final transient Player player;
    private transient VorkathNPC vorkath;
    private transient int instanceKc;
    private transient boolean dodgedSpecials = true;
    private transient int startingPrayerPoints;
    private transient boolean faithlessEncounter = true;

    public VorkathInstance(final Player player, final AllocatedArea area) {
        super(area, 280, 504);
        this.player = player;
        this.startingPrayerPoints = player.getPrayerManager().getPrayerPoints();
    }

    @Override
    public boolean acceptDropBroadcast(@NotNull final Item item, final boolean guaranteedDrop) {
        if (guaranteedDrop && item.getId() != ItemId.BLUE_DRAGONHIDE) {
            return false;
        }
        return (long) item.getSellPrice() * item.getAmount() >= broadcastValueThreshold();
    }

    @Override
    public void constructed() {
        vorkath = (VorkathNPC) new VorkathNPC(getLocation(2269, 4062, 0), this).spawn();
        player.setLocation(getLocation(OUTSIDE_TILE));
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                switch (ticks++) {
                case 0:
                    player.setAnimation(CLIMB_OVER_WALL_ANIM);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, getLocation(INSIDE_TILE), 45, ForceMovement.NORTH));
                    return;
                case 2:
                    player.setLocation(getLocation(INSIDE_TILE));
                    player.unlock();
                    stop();
                    return;
                }
            }
        }, 1, 0);
    }

    @Override
    public void enter(final Player player) {
    }

    @Override
    public void leave(final Player player, boolean logout) {
        if (logout) {
            player.forceLocation(OUTSIDE_TILE);
        }
    }

    @Override
    public Location onLoginLocation() {
        return new Location(2272, 4052, 0);
    }

    @Override
    public String name() {
        return player.getName() + "\'s Vorkath instance";
    }

    @Override
    public void process() {
        if (vorkath == null) {
            return;
        }
        if (player == null || player.isFinished()) {
            System.err.println("VorkathInstance.process -> Player is null or finished.");
            return;
        }
        if (player.getPrayerManager().getPrayerPoints() < startingPrayerPoints) {
            faithlessEncounter = false;
        }
        if (player.getActionManager().getAction() instanceof RangedCombat || player.getActionManager().getAction() instanceof MagicCombat) {
            vorkath.meleeOnly = false;
        }

        if (player.hasWalkSteps() || !containsPool(player.getX(), player.getY())) {
            return;
        }
        final int damage = Utils.random(1, 10);
        player.applyHit(new Hit(vorkath, damage, HitType.REGULAR));
        vorkath.applyHit(new Hit(damage, HitType.HEALED));
        dodgedSpecials = false;
    }

    @Override
    public boolean processMovement(final Player player, final int x, final int y) {
        if (!containsPool(x, y)) {
            return true;
        }
        final int damage = Utils.random(1, 10);
        player.applyHit(new Hit(vorkath, damage, HitType.REGULAR));
        vorkath.applyHit(new Hit(damage, HitType.HEALED));
        dodgedSpecials = false;
        return true;
    }

    private final boolean containsPool(final int x, final int y) {
        final List<AcidPool> pools = vorkath.getAcidPools();
        if (pools.isEmpty()) {
            return false;
        }
        for (int i = pools.size() - 1; i >= 0; i--) {
            final AcidPool pool = pools.get(i);
            if (pool == null) {
                continue;
            }
            if (pool.getX() == x && pool.getY() == y) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void cleared() {
        if (players.isEmpty()) {
            destroyRegion();
        }
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public String getDeathInformation() {
        return null;
    }

    @Override
    public Location getRespawnLocation() {
        return null;
    }

    @Override
    public boolean sendDeath(Player player, Entity source) {
        player.setAnimation(Animation.STOP);
        player.lock();
        player.stopAll();
        if (player.getPrayerManager().isActive(Prayer.RETRIBUTION)) {
            player.getPrayerManager().applyRetributionEffect(source);
        }
        WorldTasksManager.schedule(new WorldTask() {
            int ticks;
            @Override
            public void run() {
                if (player.isFinished() || player.isNulled()) {
                    stop();
                    return;
                }
                if (ticks == 0) {
                    player.setAnimation(DEATH_ANIMATION);
                } else if (ticks == 2) {
                    player.getDeathMechanics().service(ItemRetrievalService.RetrievalServiceType.TORFINN, source, true);
                    player.sendMessage("Oh dear, you have died.");
                    player.reset();
                    player.setAnimation(Animation.STOP);
                    player.sendMessage("Torfinn has retrieved some of your items. You can collect them from him in Rellekka.");
                    ItemRetrievalService.updateVarps(player);
                    if (player.getVariables().isSkulled()) {
                        player.getVariables().setSkull(false);
                    }
                    player.blockIncomingHits();
                    player.setLocation(player.getRespawnPoint().getLocation());
                } else if (ticks == 3) {
                    player.unlock();
                    player.getAppearance().resetRenderAnimation();
                    player.setAnimation(Animation.STOP);
                    stop();
                }
                ticks++;
            }
        }, 0, 1);
        return true;
    }

    public Player getPlayer() {
        return player;
    }

    public VorkathNPC getVorkath() {
        return vorkath;
    }

    public void setVorkath(VorkathNPC vorkath) {
        this.vorkath = vorkath;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

    public void increaseInstanceKc() {
        this.instanceKc++;
    }

    public int getInstanceKc() {
        return instanceKc;
    }

    public void setDodgedSpecials(boolean dodgedSpecials) {
        this.dodgedSpecials = dodgedSpecials;
    }

    public boolean isDodgedSpecials() {
        return dodgedSpecials;
    }

    public boolean isFaithlessEncounter() {
        return faithlessEncounter;
    }

}
