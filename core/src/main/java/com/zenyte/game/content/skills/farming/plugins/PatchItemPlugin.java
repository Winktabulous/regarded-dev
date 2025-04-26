package com.zenyte.game.content.skills.farming.plugins;

import com.zenyte.game.content.skills.farming.*;
import com.zenyte.game.content.skills.farming.actions.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnObjectAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 11. nov 2017 : 0:54.32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
@SuppressWarnings("unused")
public final class PatchItemPlugin implements ItemOnObjectAction {
    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object) {
        final FarmingSpot spot = player.getFarming().create(object);
        final int id = item.getId();
        if (id == 6951) {
            spot.forceGrowForDebug();
            return;
        }
        if (spot.getPatch().getType() == PatchType.COMPOST_BIN) {
            if (item.getId() >= 6470 && item.getId() <= 6476) {
                player.getActionManager().setAction(new BinSaturating(spot, item));
            } else if (CompostBin.isCompostableItem(item.getId()) || item.getId() == ItemId.VOLCANIC_ASH) {
                player.getActionManager().setAction(new BinFilling(spot, item));
            } else {
                player.sendMessage("You can't make compost out of that.");
            }
            return;
        }
        if (spot.getPatch().getType() == PatchType.GRAPEVINE_PATCH) {
            if (id == FarmingConstants.SALTPETRE.getId() || id == FarmingConstants.GARDENING_TROWEL.getId() || id == FarmingConstants.SPADE.getId()) {
                player.getActionManager().setAction(new GrapevineTreating(spot));
                return;
            }
        }
        if (id == 6036) {
            player.getActionManager().setAction(new Curing(object, spot));
            return;
        }
        if (id == 6059) {
            player.getActionManager().setAction(new ScarecrowPlacement(spot));
            return;
        }
        if (id == 5350) {
            player.getActionManager().setAction(new PlantPotFilling(spot, item, slot));
            return;
        }
        if (id == 13353 || (id >= 5333 && id <= 5340)) {
            player.getActionManager().setAction(new Watering(item, spot));
            return;
        }
        if (id == 6032 || id == 6034 || id == 21483 || id == 22994 || id == 22997) {
            player.getActionManager().setAction(new Saturating(spot, item));
            return;
        }
        if (id == 952) {
            player.getActionManager().setAction(new Clearing(spot));
            return;
        }
        final FarmingProduct product = FarmingProduct.products.get(item.getId());
        if (product == null) {
            player.sendMessage("Nothing interesting happens.");
            return;
        }
        player.getActionManager().setAction(new Planting(spot, product));
    }

    @Override
    public int getObjectStrategyDistance(WorldObject obj) {
        if (obj.getId() == PatchPlugin.VARROCK_OBJECT_OBJECT) {
            return 1;
        }

        return ItemOnObjectAction.super.getObjectStrategyDistance(obj);
    }

    @Override
    public Object[] getItems() {
        final List<Object> list = new ArrayList<>();
        list.add(5350);
        for (int i = 5333; i <= 5340; i++) {
            list.add(i);
        }
        list.add(FarmingConstants.SALTPETRE.getId());
        list.add(FarmingConstants.GARDENING_TROWEL.getId());
        list.add(6032);
        list.add(6034);
        list.add(21483);
        list.add(1982);
        list.add(21622);
        list.add(1925);
        list.add(6951);//5733
        list.add(6059);
        list.add(6036);
        list.add(952);
        list.add(22994);
        list.add(22997);
        list.add(6470);
        list.add(6472);
        list.add(6474);
        list.add(6476);
        list.add(13353);
        FarmingProduct.products.forEach((k, v) -> list.add(k));
        for (final FarmingProduct product : FarmingProduct.values()) {
            final Item produce = product.getProduct();
            if (produce != null) {
                list.add(produce.getId());
            }
        }
        return list.toArray();
    }

    @Override
    public Object[] getObjects() {
        final List<Object> list = new ArrayList<Object>();
        for (final FarmingPatch patch : FarmingPatch.values) {
            for (int i : patch.getIds()) {
                list.add(i);
            }
        }
        return list.toArray();
    }
}
