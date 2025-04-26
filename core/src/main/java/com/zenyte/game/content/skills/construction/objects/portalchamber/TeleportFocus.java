package com.zenyte.game.content.skills.construction.objects.portalchamber;

import com.zenyte.game.content.skills.construction.Construction;
import com.zenyte.game.content.skills.construction.FurnitureData;
import com.zenyte.game.content.skills.construction.ObjectInteraction;
import com.zenyte.game.content.skills.construction.RoomReference;
import com.zenyte.game.content.skills.construction.constants.Furniture;
import com.zenyte.game.content.skills.construction.dialogue.PortalRedirectD;
import com.zenyte.game.content.skills.construction.dialogue.PortalScryingD;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import mgi.types.config.ObjectDefinitions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kris | 22. nov 2017 : 3:38.58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class TeleportFocus implements ObjectInteraction {

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        final List<String> portals = new ArrayList<String>();
        final List<FurnitureData> furnitureList = new ArrayList<FurnitureData>();
        populateLists(reference, portals, furnitureList);
        if (option.equals("direct-portal")) {
            player.getDialogueManager().start(new PortalRedirectD(player, portals, furnitureList, reference));
        } else if (option.equals("scry")) {
            player.getDialogueManager().start(new PortalScryingD(player, portals, furnitureList));
        }
    }

    private final void populateLists(final RoomReference reference, final List<String> portals, final List<FurnitureData> furnitureList) {
        for (FurnitureData furniture : reference.getFurnitureData()) {
            if (furniture.getFurniture().ordinal() >= Furniture.TEAK_VARROCK_PORTAL.ordinal() && furniture.getFurniture().ordinal() <= Furniture.MARBLE_KOUREND_PORTAL.ordinal()) {
                if (furniture.getLocation().getX() == 3 && furniture.getLocation().getY() == 0) {
                    portals.add(ObjectDefinitions.get(furniture.getFurniture().getObjectId()).getName().replaceAll(" portal", "(S)"));
                    furnitureList.add(furniture);
                }
                if (furniture.getLocation().getX() == 0 && furniture.getLocation().getY() == 3) {
                    portals.add(ObjectDefinitions.get(furniture.getFurniture().getObjectId()).getName().replaceAll(" portal", "(W)"));
                    furnitureList.add(furniture);
                }
                if (furniture.getLocation().getX() == 3 && furniture.getLocation().getY() == 7) {
                    portals.add(ObjectDefinitions.get(furniture.getFurniture().getObjectId()).getName().replaceAll(" portal", "(N)"));
                    furnitureList.add(furniture);
                }
                if (furniture.getLocation().getX() == 7 && furniture.getLocation().getY() == 3) {
                    portals.add(ObjectDefinitions.get(furniture.getFurniture().getObjectId()).getName().replaceAll(" portal", "(E)"));
                    furnitureList.add(furniture);
                }
                continue;
            }
            if (!(furniture.getFurniture().getObjectId() > 13635 && furniture.getFurniture().getObjectId() < 13639))
                continue;
            if (furniture.getLocation().getX() == 3 && furniture.getLocation().getY() == 0) {
                portals.add("Nowhere(S)");
                furnitureList.add(furniture);
            }
            if (furniture.getLocation().getX() == 0 && furniture.getLocation().getY() == 3) {
                portals.add("Nowhere(W)");
                furnitureList.add(furniture);
            }
            if (furniture.getLocation().getX() == 3 && furniture.getLocation().getY() == 7) {
                portals.add("Nowhere(N)");
                furnitureList.add(furniture);
            }
            if (furniture.getLocation().getX() == 7 && furniture.getLocation().getY() == 3) {
                portals.add("Nowhere(E)");
                furnitureList.add(furniture);
            }
        }
        while (portals.size() < 3) {
            portals.add("<str>No portal frame</str>");
        }
        portals.add("Nowhere");
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SCRYING_POOL, ObjectId.TELEPORTATION_FOCUS, ObjectId.GREATER_TELEPORT_FOCUS };
    }
}
