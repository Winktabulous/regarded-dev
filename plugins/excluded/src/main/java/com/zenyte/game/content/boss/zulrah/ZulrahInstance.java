package com.zenyte.game.content.boss.zulrah;

import com.zenyte.game.content.ItemRetrievalService;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.Position;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.cutscene.FadeScreen;
import com.zenyte.game.world.region.DynamicArea;
import com.zenyte.game.world.region.area.plugins.CannonRestrictionPlugin;
import com.zenyte.game.world.region.area.plugins.DeathPlugin;
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin;
import com.zenyte.game.world.region.area.plugins.PartialMovementPlugin;
import com.zenyte.game.world.region.dynamicregion.AllocatedArea;
import com.zenyte.game.world.region.dynamicregion.MapBuilder;
import com.zenyte.game.world.region.dynamicregion.OutOfSpaceException;
import com.zenyte.plugins.dialogue.PlainChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zenyte.game.world.entity.player.Player.DEATH_ANIMATION;

/**
 * @author Kris | 28. jaan 2018 : 18:06.36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public final class ZulrahInstance extends DynamicArea implements DeathPlugin, PartialMovementPlugin, CannonRestrictionPlugin, LootBroadcastPlugin {
    private static final Logger log = LoggerFactory.getLogger(ZulrahInstance.class);
    private static final int WIDTH = 7;
    private static final int HEIGHT = 7;
    private final Player player;
    private final FadeScreen fadeScreen;
    private ZulrahNPC zulrah;
    private boolean initiated;
    private long ms;

    public ZulrahInstance(final Player player, final FadeScreen fadeScreen, final AllocatedArea area) {
        super(area, 280, 381);
        this.player = player;
        this.fadeScreen = fadeScreen;
    }

    /**
     * Initiates the zulrah launch sequence which teleports player to the starting location and initiates the dialogue.
     *
     * @param player player to teleport.
     */
    public static final void launch(final Player player) {
        try {
            final AllocatedArea area = MapBuilder.findEmptyChunk(WIDTH, HEIGHT);
            final FadeScreen fadeScreen = new FadeScreen(player);
            final ZulrahInstance instance = new ZulrahInstance(player, fadeScreen, area);
            instance.ms = System.currentTimeMillis();
            instance.constructRegion();
            fadeScreen.fade();
        } catch (OutOfSpaceException e) {
            log.error("", e);
        }
    }

    @Override
    public Location onLoginLocation() {
        return new Location(2213, 3056, 0);
    }

    @Override
    public void constructed() {
        final Location position = getLocation(2268, 3068, 0);
        final Location camPos = getLocation(2256, 3064, 0);
        final Location camLook = getLocation(2275, 3080, 0);
        WorldTasksManager.schedule(new TickTask() {
            @Override
            public void run() {
                switch (ticks++) {
                case 0:
                    player.setLocation(position);
                    return;
                case 1:
                    final Location tile = player.getLastLoadedMapRegionTile();
                    player.getPacketDispatcher().sendCameraPosition(camPos.getLocalX(tile), camPos.getLocalY(tile), 1000, -128, 0);
                    player.getPacketDispatcher().sendCameraLook(camLook.getLocalX(tile), camLook.getLocalY(tile), 1000, -128, 0);
                    return;
                case 2:
                    player.getDialogueManager().start(new PlainChat(player, "The priestess rows you to Zulrah\'s shrine,<br>then hurriedly paddles away."));
                    fadeScreen.unfade();
                    stop();
                }
            }
        }, Math.min(3 - (int) ((System.currentTimeMillis() - ms) / 600), 5), 0);
    }

    @Override
    public void enter(final Player player) {
        player.setViewDistance(25);
        if (!initiated && player == this.player) {
            initiated = true;
            WorldTasksManager.schedule(new WorldTask() {
                @Override
                public void run() {
                    if (!player.getInterfaceHandler().isVisible(229)) {
                        zulrah = new ZulrahNPC(getLocation(new Location(2266, 3073, 0)), ZulrahInstance.this);
                        zulrah.spawn();
                        stop();
                    }
                }
            }, 0, 0);
        }
    }

    @Override
    public void leave(final Player player, boolean logout) {
        player.resetViewDistance();
    }

    @Override
    public String name() {
        return "Zulrah\'s shrine";
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
        zulrah.setStopped(true);
        final int resurrections = player.getVariables().getZulrahResurrections();
        if (resurrections == 0 && DiaryUtil.eligibleFor(DiaryReward.WESTERN_BANNER4, player)) {
            player.getVariables().setZulrahResurrections(1);
            player.blockIncomingHits();
            player.setHitpoints(player.getMaxHitpoints());
            player.getToxins().reset();
            player.getSkills().setLevel(SkillConstants.PRAYER, player.getSkills().getLevelForXp(SkillConstants.PRAYER));
            player.sendMessage(Colour.RS_GREEN.wrap("The blessing bestowed upon you for completing the Western elite diary empowers you to continue. Carry on the fight!"));
            return true;
        }
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
                    player.getDeathMechanics().service(ItemRetrievalService.RetrievalServiceType.ZUL_GWENWYNIG, source, true);
                    player.sendMessage("Oh dear, you have died.");
                    player.reset();
                    player.blockIncomingHits();
                    player.setAnimation(Animation.STOP);
                    player.sendMessage("Priestess Zul-Gwenwynig has retrieved some of your items. You can collect them from her at the pier in Zul-Andra.");
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

    @Override
    public boolean processMovement(Player player, int x, int y) {
        if (zulrah != null) {
            zulrah.setStopped(false);
        }
        return false;
    }

    public Player getPlayer() {
        return player;
    }

    public ZulrahNPC getZulrah() {
        return zulrah;
    }

    public void setZulrah(ZulrahNPC zulrah) {
        this.zulrah = zulrah;
    }

    @Override
    public boolean isMultiwayArea(Position position) {
        return true;
    }

}
