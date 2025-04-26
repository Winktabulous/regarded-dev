package com.zenyte.game.content.area.prifddinas.zalcano;

import com.zenyte.game.content.area.prifddinas.zalcano.combat.symbols.DemonicSymbol;
import com.zenyte.game.content.area.prifddinas.zalcano.formation.DefaultZalcanoRockFormationHandler;
import com.zenyte.game.content.area.prifddinas.zalcano.formation.ZalcanoRockFormationHandler;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.flooritem.FloorItem;
import com.zenyte.game.world.region.GlobalAreaManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ZalcanoInstance {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZalcanoInstance.class);

    /**
     * Singleton for 1 instance only, since it's a world boss and anyone can fight him
     */
    public static final ZalcanoInstance INSTANCE = new ZalcanoInstance();

    private ZalcanoLair lair;
    private ZalcanoBoss zalcano;

    private final ZalcanoRockFormationHandler rockFormationHandler = new DefaultZalcanoRockFormationHandler(this);

    private final List<DemonicSymbol> symbols = new ArrayList<>();
    private final List<ZalcanoGolemSpawn> golems = new ArrayList<>();

    public void clearInstance() {
        clearSymbols();
        rockFormationHandler.depleteAllFormations();
        golems.forEach(ZalcanoInstance::finishGolem);
        golems.clear();
    }

    public void spawnZalcano() {
        try {
            zalcano = new ZalcanoBoss(this);
            zalcano.spawn();
            initiate();
        } catch (Exception e) {
            LOGGER.error("Failed to spawn Zalcano {}", zalcano, e);
        }
    }

    public void spawnGolem(ZalcanoGolemSpawn golem) {
        try {
            golem.setSpawned(true);
            golem.spawn();
            golems.add(golem);
        } catch (Exception e) {
            LOGGER.error("Failed to spawn golem {}", golem, e);
        }
    }

    public void removeAndFinishGolem(ZalcanoGolemSpawn toDelete) {
        golems.remove(toDelete);
        finishGolem(toDelete);
    }

    private static void finishGolem(ZalcanoGolemSpawn toDelete) {
        toDelete.setHitpoints(0);
        toDelete.finish();
    }

    public void clearSymbols() {
        symbols.forEach(World::removeObject);
        symbols.clear();
    }

    public void registerSymbols(Collection<DemonicSymbol> symbols) {
        this.symbols.addAll(symbols);
        this.symbols.forEach(e -> World.spawnTemporaryObject(e, 35));

        WorldTasksManager.schedule(() -> {
            clearSymbols();
            this.getPlayers().forEach(player -> {
                try {
                    player.getTemporaryAttributes().remove("BLUE_SYMBOL");
                    player.getTemporaryAttributes().remove("ORANGE_SYMBOL");
                } catch (NullPointerException ex) {
                    LOGGER.error("Failed to remove symbol attributes from player {}", player.getUsername(), ex);
                }
            });
        }, 35);

        WorldTasksManager.schedule(new ZalcanoTick(this) {

            @Override
            public void run() {

                if (instance.getSymbols().size() == 0) {
                    this.stop();
                    return;
                }

                if (ticks >= 35) {
                    this.stop();
                    return;
                }

                instance.getSymbols().forEach(DemonicSymbol::processOrange);
                for (Player player : ZalcanoInstance.this.getPlayers()) {
                    boolean onBlue = false;

                    for (DemonicSymbol symbol : symbols) {
                        if (symbol.isStandingOnBlue(player)) {
                            onBlue = true;
                        }
                    }

                    if (onBlue) {
                        player.getTemporaryAttributes().put("BLUE_SYMBOL", true);
                    } else {
                        player.getTemporaryAttributes().remove("BLUE_SYMBOL");
                    }
                }

                ticks++;

            }
        }, 0, 0);
    }

    public void initiate() {
        rockFormationHandler.deactivateAllFormations();
        rockFormationHandler.switchActivateFormation();
        lair = GlobalAreaManager.getArea(ZalcanoLair.class);
    }

    public static void deleteTephra(Player player) {
        player.getInventory().deleteItem(new Item(ZalcanoConstants.IMBUED_TEPHRA_ITEM_ID, Integer.MAX_VALUE));
        player.getInventory().deleteItem(new Item(ZalcanoConstants.TEPHRA_ITEM_ID, Integer.MAX_VALUE));
        player.getInventory().deleteItem(new Item(ZalcanoConstants.REFINED_TEPHRA_ITEM_ID, Integer.MAX_VALUE));
        player.getEquipment().deleteItem(new Item(ZalcanoConstants.IMBUED_TEPHRA_ITEM_ID, Integer.MAX_VALUE));
    }

    public static void deleteFloorTephra() {
        var tephra = new ArrayList<FloorItem>();
        for (FloorItem item : World.getAllFloorItems()) {
            if (item.getId() == ZalcanoConstants.IMBUED_TEPHRA_ITEM_ID) {
                tephra.add(item);
            }//no longer owe u 4 fags
        }
        tephra.forEach(World::destroyFloorItem);
    }

    public void activateFormation() {
        rockFormationHandler.switchActivateFormation();
    }

    public List<ZalcanoGolemSpawn> getGolems() {
        return golems;
    }

    public ZalcanoLair getLair() {
        return lair;
    }

    public Set<Player> getPlayers() {
        return GlobalAreaManager.get(ZalcanoLair.NAME).getPlayers();
    }

    public List<DemonicSymbol> getSymbols() {
        return symbols;
    }

    public ZalcanoBoss getZalcano() {
        return zalcano;
    }

    public void setZalcano(ZalcanoBoss zalcano) {
        this.zalcano = zalcano;
    }

    public ZalcanoRockFormationHandler getRockFormationHandler() {
        return rockFormationHandler;
    }

}
