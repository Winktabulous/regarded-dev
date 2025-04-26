package com.zenyte.game.world.entity.player.controller;

import com.zenyte.game.content.consumables.edibles.Food;
import com.zenyte.game.content.skills.magic.Magic.TeleportType;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 22. okt 2017 : 17:39.54 An abstract (to avoid instantiation)
 * class for Controllers.
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 * profile</a>}
 */
public class Controller {


    protected Player player;

    public void start() {
    }

    public void process() {
    }

    public void forceStop() {
    }

    public final Object[] getArguments() {
        return player.getControllerManager().getArguments();
    }

    public final void setArguments(final Object[] objects) {
        player.getControllerManager().setArguments(objects);
    }

    public boolean processTeleport(final Location location, final TeleportType type) {
        return true;
    }

    public boolean processObjectClick1(final WorldObject object) {
        return true;
    }

    public boolean processObjectClick2(final WorldObject object) {
        return true;
    }

    public boolean processObjectClick3(final WorldObject object) {
        return true;
    }

    public boolean processObjectClick4(final WorldObject object) {
        return true;
    }

    public boolean processObjectClick5(final WorldObject object) {
        return true;
    }

    public boolean processNPCClick1(final NPC npc) {
        return true;
    }

    public boolean processNPCClick2(final NPC npc) {
        return true;
    }

    public boolean processNPCClick3(final NPC npc) {
        return true;
    }

    public boolean processNPCClick4(final NPC npc) {
        return true;
    }

    public boolean processNPCClick5(final NPC npc) {
        return true;
    }

    public boolean sendDeath(final Entity source) {
        return true;
    }

    public boolean removeOnLogin() {
        return true;
    }

    public boolean removeOnLogout() {
        return true;
    }

    public boolean canDropItem(final Item item) {
        return true;
    }

	/*public boolean canDrinkPotion(final Potion potion) {
		return true;
	}*/

    public boolean canEatFood(final Food food) {
        return true;
    }

    public boolean canUsePrayer(final Prayer prayer) {
        return true;
    }

    public boolean canMove(final int direction, final int x, final int y) {
        return true;
    }

    public boolean canButtonClick(final int interfaceId, final int componentId, final int slotId) {
        return true;
    }

    public boolean canAttack(final Entity entity) {
        return true;
    }

    public boolean canHit(final Entity entity) {
        return true;
    }

    public boolean canEquipItem(final Item item, final int slot) {
        return true;
    }

    public void processOutgoingHit(final Entity target, final Hit hit) {

    }

    public boolean processPlayerCombat(final Entity entity, final String style) {
        return true;
    }

    public void move(final boolean primaryTile, final int x, final int y) {
    }

    public void teleport(final Location location) {
    }

    public void finishTeleport() {
    }

    public boolean canLeaveClanChannel() {
        return true;
    }

    public void forceLeaveClanChannel() {
    }

    public boolean processItemOption(final Player player, final int itemId, final int slotId, final String option) {
        return true;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
