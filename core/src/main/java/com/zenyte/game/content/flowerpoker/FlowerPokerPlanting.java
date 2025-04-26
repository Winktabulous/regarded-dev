package com.zenyte.game.content.flowerpoker;

import com.near_reality.tools.logging.GameLogMessage;
import com.near_reality.tools.logging.GameLogger;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.TickTask;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.misc.MithrilSeedPlanting;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.utils.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.util.ArrayList;
import java.util.Objects;

public final class FlowerPokerPlanting extends TickTask {

    private static final Item MITHRIL_SEEDS = new Item(299);
    private static final Logger log = LoggerFactory.getLogger(FlowerPokerPlanting.class);

    private final FlowerPokerSession session;

    private final Player player;
    private final Player other;
    private int tick;
    private int delay;

    public FlowerPokerPlanting(Player player, Player other, FlowerPokerSession session) {
        this.session = session;
        this.player = player;
        this.other = other;
    }

    public boolean start() {
        if (session.stage == FlowerPokerStage.RESTARTING) {
            session.removeFlowers();
            session.opponentFlowers.clear();
            session.challengerFlowers.clear();
            boolean hasSeeds = true;
            if (!player.getInventory().containsItem(299, 5)) {
                player.sendMessage("You don't have enough mithril seeds to start fp.");
                other.sendMessage("The other player doesn't have enough mithril seeds to start fp.");
                hasSeeds = false;
            }

            if (!other.getInventory().containsItem(299, 5)) {
                other.sendMessage("You don't have enough mithril seeds to start fp.");
                player.sendMessage("The other player doesn't have enough mithril seeds to start fp.");
                hasSeeds = false;
            }
            if (!hasSeeds) {
                cancelAndRefund();
                stop();
                return false;
            }
        }
        player.getTemporaryAttributes().put("mithril seed delay", System.currentTimeMillis() + 10_000);
        other.getTemporaryAttributes().put("mithril seed delay", System.currentTimeMillis() + 10_000);
        session.stage = FlowerPokerStage.PLANTING;
        return true;
    }

    public int processWithDelay() {

        if (World.isUpdating() && World.getUpdateTimer() < TimeUnit.MINUTES.toTicks(1)) {
            cancelAndRefund();
            return -1;
        }

        if (session.stage == FlowerPokerStage.STARTING) {
            start();
            return 1;
        }

        if (session.stage == FlowerPokerStage.RESTARTING) {
            reset();
            return 1;
        }


        if (session.stage == FlowerPokerStage.FINISHED) {
            var flowerPokerRankC = FlowerPokerRank.calculateRank(session.challengerFlowers);
            var flowerPokerRankO = FlowerPokerRank.calculateRank(session.opponentFlowers);
            var bestRank = FlowerPokerRank.bestOf(flowerPokerRankO, flowerPokerRankC);
            var draw = bestRank == null;
            var win = bestRank == flowerPokerRankC ? player : other;
            var lost = bestRank == flowerPokerRankC ? other : player;
            var winner = bestRank == flowerPokerRankC ? World.getPlayerByUsername(player.getUsername()) : World.getPlayerByUsername(other.getUsername());
            var loser = bestRank == flowerPokerRankC ? World.getPlayerByUsername(other.getUsername()) : World.getPlayerByUsername(player.getUsername());

            if (Objects.nonNull(winner))
                winner.setForceTalk((draw ? "[DRAW]" : "[WINNER]") + " ROLLED " + flowerPokerRankC.getName() + "... " + (draw ? "RESTARTING!" : ""));
            if (Objects.nonNull(loser))
                loser.setForceTalk((draw ? "[DRAW]" : "[LOSER]") + " ROLLED " + flowerPokerRankO.getName() + "... " + (draw ? "RESTARTING!" : ""));

            if (draw) {
                session.stage = FlowerPokerStage.RESTARTING;
                return 2;
            }
            else {
                session.stage = FlowerPokerStage.AWARDING;
                player.unlock();
                other.unlock();
                FlowerPokerManager.get(player).reset(false);
                FlowerPokerManager.get(other).reset(false);

                session.stakedItemsChallenger.getItems().forEach((slot, item) -> awardWinner(winner, item));
                session.stakedItemsOpponent.getItems().forEach((slot, item) -> awardWinner(winner, item));

                final GameLogMessage.FlowerPokerSession log = session.toLogMessage(win.getUsername());
                GameLogger.log(Level.INFO, () -> log);
                if (Objects.nonNull(FlowerPokerManager.get(player).staked_items))
                    FlowerPokerManager.get(player).staked_items.clear();
                if (Objects.nonNull(FlowerPokerManager.get(other).staked_items))
                    FlowerPokerManager.get(other).staked_items.clear();
                win.sendMessage("You won the flower poker game!");
                lost.sendMessage("You lost the flower poker game!");
                return -1;
            }
        }

        if (session.stage == FlowerPokerStage.PLANTING) {
            if (forcePlant(player))
                plantAndWalk(player, session.arena.getDirection());

            if (forcePlant(other))
                plantAndWalk(other, session.arena.getDirection());
        }
        return 1;
    }

    private void awardWinner(Player winner, Item item) {
        if (Objects.isNull(item))
            return;
        if (Objects.isNull(winner)) {
            log.error("Winner is null");
            return;
        }
        if (Objects.equals(13420, winner.getPosition().getRegionId()))
            winner.getInventory().addOrDrop(item);
    }

    @Override
    public void stop() {
        player.putBooleanTemporaryAttribute("gambling", false);
        other.putBooleanTemporaryAttribute("gambling", false);
        session.removeFlowers();
        FlowerPokerManager.FLOWER_POKER_AREAS.replace(session.arena, null);
        super.stop();
    }

    public void cancelAndRefund() {
        player.sendMessage("Flower Poker cancelled.");
        other.sendMessage("Flower Poker cancelled.");
        player.unlock();
        other.unlock();
        FlowerPokerManager.get(player).reset(true);
        FlowerPokerManager.get(other).reset(true);
        session.stakedItemsChallenger.getItems().forEach((slot, item) -> {
            if (item != null) {
                player.getInventory().addOrDrop(item);
            }
        });
        session.stakedItemsOpponent.getItems().forEach((slot, item) -> {
            if (item != null) {
                other.getInventory().addOrDrop(item);
            }
        });
        FlowerPokerManager.get(player).staked_items.clear();
        FlowerPokerManager.get(other).staked_items.clear();
    }

    public void plantAndWalk(Player player, Direction direction) {
        if (session.stage != FlowerPokerStage.PLANTING)
            return;
        if (player.getNumericTemporaryAttribute("manual_plant").longValue() > System.currentTimeMillis()) {
            return;
        }

        ArrayList<MithrilSeedPlanting.Flowers> flower;
        FlowerPokerRank last;
        if (player == this.player) {
            last = session.lastRankP;
            flower = session.challengerFlowers;
        }
        else {
            last = session.lastRankO;
            flower = session.opponentFlowers;
        }
        if (flower.size() >= 5)
            return;

        final MithrilSeedPlanting.Flowers flowers = MithrilSeedPlanting.Flowers.random();
        final WorldObject object = new WorldObject(flowers.getObject(), 10, 0, player.getPosition());
        player.getInventory().deleteItem(MITHRIL_SEEDS);
        World.spawnObject(object);
        session.flowerObjects.add(object);
        player.getTemporaryAttributes().put("mithril seed delay", System.currentTimeMillis() + 10_000);
        flower.add(flowers);

        if (flowers.equals(MithrilSeedPlanting.Flowers.BLACK) || flowers.equals(MithrilSeedPlanting.Flowers.WHITE)) {
            session.stage = FlowerPokerStage.RESTARTING;
            boolean black = flowers.equals(MithrilSeedPlanting.Flowers.BLACK);
            player.setForceTalk("DROPPED " + (black ? "BLACK" : "WHITE") + " FLOWERS... RESTARTING!");
            player.addWalkSteps(direction, 1, 1, false);
            return;
        }
        player.addWalkSteps(direction, 1, 1, false);
        player.getTemporaryAttributes().put("manual_plant", System.currentTimeMillis() + 1_000);
        FlowerPokerRank flowerPokerRank = FlowerPokerRank.calculateRank(flower);
        if (last != flowerPokerRank && flowerPokerRank != FlowerPokerRank.NONE)
            player.setForceTalk(flowerPokerRank.getName());

        if (session.plantingFinish()) {
            session.stage = FlowerPokerStage.FINISHED;
            delay = 2;
        }
    }

    public void reset() {
        FlowerPokerAreas fpArena = session.arena;
        other.getTemporaryAttributes().put("manual_plant", System.currentTimeMillis() + 3_000);
        player.getTemporaryAttributes().put("manual_plant", System.currentTimeMillis() + 3_000);
        Location l1 = fpArena.getOne().copy().transform(fpArena.getDirection(), 1);
        Location l2 = fpArena.getTwo().copy().transform(fpArena.getDirection(), 1);

        if (!player.getLocation().equals(l1)) {
            player.setLocation(l1);
        }
        if (!other.getLocation().equals(l2)) {
            other.setLocation(l2);
        }
        start();
    }

    public boolean forcePlant(Player player) {
        return player.getNumericTemporaryAttribute("mithril seed delay").longValue() < System.currentTimeMillis();
    }

    @Override
    public void run() {
        if (tick == delay) {
            delay = processWithDelay();
            tick = 0;
        }
        if (delay == -1) {
            stop();
        }
        tick++;
    }
}
