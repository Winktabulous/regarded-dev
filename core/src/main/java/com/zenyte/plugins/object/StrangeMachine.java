package com.zenyte.plugins.object;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class StrangeMachine implements ObjectAction {
    public static final IntList requiredItems = IntArrayList.wrap(new int[] {ItemId.HAMMER, ItemId.WYVERN_VISAGE, ItemId.ELEMENTAL_SHIELD});
    public static final int STRANGE_MACHINE_ID = 30944;
    private static final Animation machineAnim = new Animation(7733);
    private static final Animation defaultMachineAnim = new Animation(7735);
    private static final Animation hammeringAnim = new Animation(898);
    private static final Animation magicAnim = new Animation(811);
    private static final Graphics magicGfx = new Graphics(111, 0, 70);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Investigate")) {
            start(player, object);
        }
    }

    public static void start(final Player player, final WorldObject object) {
        if (check(player)) {
            player.lock();
            player.setAnimation(hammeringAnim);
            WorldTasksManager.schedule(() -> {
                player.setAnimation(magicAnim);
                player.setGraphics(magicGfx);
                World.sendObjectAnimation(object, machineAnim);
                WorldTasksManager.schedule(() -> reward(player, object));
            }, 4);
        }
    }

    private static boolean check(final Player player) {
        if (!player.getInventory().containsAll(requiredItems)) {
            player.sendMessage("You need a hammer, wyvern visage, and elemental shield to do this.");
            return false;
        }
        if (player.getSkills().getLevel(SkillConstants.MAGIC) < 66 || player.getSkills().getLevel(SkillConstants.SMITHING) < 66) {
            player.sendMessage("You require level 66 in Magic and Smithing to do this.");
            return false;
        }
        return true;
    }

    private static void reward(final Player player, final WorldObject object) {
        player.getInventory().deleteItemsIfContains(new Item[] {new Item(ItemId.ELEMENTAL_SHIELD), new Item(ItemId.WYVERN_VISAGE)}, () -> {
            World.sendObjectAnimation(object, defaultMachineAnim);
            final Skills skills = player.getSkills();
            final Inventory inventory = player.getInventory();
            final Item reward = new Item(ItemId.ANCIENT_WYVERN_SHIELD_21634);
            skills.drainSkill(SkillConstants.MAGIC, player.getSkills().getLevel(SkillConstants.MAGIC));
            skills.addXp(SkillConstants.MAGIC, 2000);
            skills.addXp(SkillConstants.SMITHING, 2000);
            inventory.addItem(reward);
            player.getDialogueManager().start(new ItemChat(player, reward, "At great cost to your personal magical energies, you have crafted the wyvern visage and elemental shield into an ancient wyvern shield."));
        });
        player.unlock();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {STRANGE_MACHINE_ID};
    }
}
