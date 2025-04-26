package com.zenyte.game.content.skills.firemaking;

import com.zenyte.game.content.achievementdiary.diaries.*;
import com.zenyte.game.content.advent.AdventCalendarManager;
import com.zenyte.game.content.treasuretrails.clues.SherlockTask;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.bank.BankPolygons;
import com.zenyte.game.world.entity.player.perk.PerkWrapper;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.LayableObjectPlugin;

import java.util.Optional;

public class FiremakingAction extends Action {
    public static final Item TINDERBOX = new Item(590);
    private static final Item ASHES = new Item(592);
    private final Firemaking data;
    private final int slot;
    private final boolean floor;
    private final FiremakingTool tool;
    private final Optional<FloorItem> floorItem;
    private boolean pyromancer;

    private boolean check() {
        if (tool == FiremakingTool.TINDERBOX && !player.getInventory().containsItem(TINDERBOX)) {
            player.sendMessage("You need a tinderbox to light a fire.");
            return false;
        }
        if (player.getSkills().getLevel(SkillConstants.FIREMAKING) < data.getLevel()) {
            player.sendMessage("You need a Firemaking level of at least " + data.getLevel() + " to burn " + data.getLogs().getDefinitions().getName().toLowerCase() + ".");
            return false;
        }
        Location location = new Location(player.getLocation());
        if (!World.isTileFree(location, 0) || World.getObjectWithType(location, 10) != null || World.getObjectWithType(location, 11) != null) {
            player.sendMessage("You can't light a fire here.");
            return false;
        }
        if (floorItem.isPresent()) {
            location = floorItem.get().getLocation();
            if (!World.isTileFree(location, 0) || World.getObjectWithType(location, 10) != null || World.getObjectWithType(location, 11) != null) {
                player.sendMessage("You can't light the logs from here.");
                return false;
            }
        }
        final RegionArea area = player.getArea();
        if (area instanceof LayableObjectPlugin) {
            return ((LayableObjectPlugin) area).canLay(player, LayableObjectPlugin.LayableObjectType.FIRE);
        }
        return true;
    }

    @Override
    public boolean start() {
        if (BankPolygons.contains(player.getLocation())) {
            player.sendMessage("You can't light fires inside banks.");
            return false;
        }
        if (!check()) {
            return false;
        }
        final Object burnDelay = player.getTemporaryAttributes().remove("BurnDelay");
        final Long time = !(burnDelay instanceof Long) ? null : (Long) burnDelay;
        final boolean quickFire = time != null && time > Utils.currentTimeMillis();
        final int levelDifference = player.getSkills().getLevel(SkillConstants.FIREMAKING) - data.getLevel();
        final int successPercentage = levelDifference * 7;
        final int random = Utils.random(1, 3);
        delay(quickFire ? 1 : (successPercentage > Utils.random(100) ? 2 : (random * 4)));
        if (!quickFire) {
            player.setAnimation(tool.getAnimation());
            player.sendFilteredMessage("You attempt to light the logs.");
        }
        if (!floor) {
            player.getInventory().deleteItem(slot, data.getLogs());
            if (player.getPerkManager().isValid(PerkWrapper.PYROMANCER) && player.getInventory().containsItem(data.getLogs())) {
                player.getInventory().deleteItem(data.getLogs());
                pyromancer = true;
            }
            World.spawnFloorItem(data.getLogs(), player);
        }
        return true;
    }

    @Override
    public boolean process() {
        return check();
    }

    @Override
    public int processWithDelay() {
        final Location location = new Location(player.getX(), player.getY(), player.getPlane());
        final FloorItem item = World.getRegion(location.getRegionId()).getFloorItem(data.getLogs().getId(), location, player);
        if (item == null || !item.isVisibleTo(player)) {
            return -1;
        }
        if (!player.addWalkSteps(player.getX() - 1, player.getY(), 1) && !player.addWalkSteps(player.getX() + 1, player.getY(), 1) && !player.addWalkSteps(player.getX(), player.getY() + 1, 1)) {
            player.addWalkSteps(player.getX(), player.getY() - 1, 1);
        }
        light();
        return -1;
    }

    private void light() {
        //WorldTasksManager.schedule(() -> {
        final Location location = new Location(player.getX(), player.getY(), player.getPlane());
        final FloorItem item = World.getRegion(location.getRegionId()).getFloorItem(data.getLogs().getId(), location, player);
        //System.err.println(item);
        if (item == null || !item.isVisibleTo(player)) {
            return;
        }
        final WorldObject object = new FireObject(player, data.getObjectId(), 10, 1, location);
        World.spawnTemporaryObject(object, ASHES, 200);
        if (data.equals(Firemaking.WILLOW)) {
            player.getAchievementDiaries().update(FaladorDiary.CHOP_BURN_WILLOW_LOGS, 2);
        } else if (data.equals(Firemaking.YEW) && location.getX() >= 3438 && location.getX() <= 3441 && location.getY() >= 2909 && location.getY() <= 2923 && location.getPlane() == 1) {
            player.getAchievementDiaries().update(DesertDiary.BURN_YEW_LOGS);
        } else if (data.equals(Firemaking.YEW) && location.getX() >= 3254 && location.getX() <= 3259 && location.getY() >= 3484 && location.getY() <= 3488 && location.getPlane() == 3) {
            player.getAchievementDiaries().update(VarrockDiary.CHOP_AND_BURN_YEW_LOGS, 2);
        } else if (data.equals(Firemaking.OAK)) {
            player.getAchievementDiaries().update(LumbridgeDiary.CHOP_AND_BURN_LOGS, 2);
            player.getAchievementDiaries().update(FremennikDiary.CHOP_AND_BURN_OAK_LOGS, 2);
        } else if (data.equals(Firemaking.MAGIC)) {
            player.getAchievementDiaries().update(WildernessDiary.CUT_AND_BURN_MAGIC_LOGS, 2);
        } else if (data.equals(Firemaking.MAPLE) && !tool.equals(FiremakingTool.TINDERBOX)) {
            player.getAchievementDiaries().update(KandarinDiary.BURN_MAPLE_LOGS);
        } else if (data.equals(Firemaking.TEAK)) {
            player.getAchievementDiaries().update(WesternProvincesDiary.CHOP_AND_BURN_TEAK_LOGS, 2);
        } else if (data.equals(Firemaking.MAHOGANY)) {
            player.getAchievementDiaries().update(WesternProvincesDiary.CHOP_AND_BURN_MAHOGANY_LOGS, 2);
            player.getAchievementDiaries().update(MorytaniaDiary.CHOP_AND_BURN_MAHOGANY_LOGS, 2);
        }
        if (data.equals(Firemaking.YEW)) {
            SherlockTask.BURN_A_YEW_LOG.progress(player);
        } else if (data.equals(Firemaking.MAGIC)) {
            SherlockTask.BURN_MAGIC_LOG.progress(player);
        } else if (data.equals(Firemaking.REDWOOD)) {
            SherlockTask.BURN_REDWOOD_LOG.progress(player);
        }

        AdventCalendarManager.increaseChallengeProgress(player, 2022, 24, 1);
        player.sendFilteredMessage("The fire catches and the " + (data.equals(Firemaking.KINDLING) ? "kindlings begin to burn." : "logs begin to burn."));
        player.getSkills().addXp(SkillConstants.FIREMAKING, data.getXp() * (pyromancer ? 2 : 1));
        World.destroyFloorItem(player, item, object);
        player.getTemporaryAttributes().put("BurnDelay", Utils.currentTimeMillis() + 1800);
        WorldTasksManager.schedule(() -> player.setFaceLocation(location));
        //});
    }

    @Override
    public void stop() {
        delay(3);
    }

    public FiremakingAction(Firemaking data, int slot, boolean floor, FiremakingTool tool, Optional<FloorItem> floorItem) {
        this.data = data;
        this.slot = slot;
        this.floor = floor;
        this.tool = tool;
        this.floorItem = floorItem;
    }
}
