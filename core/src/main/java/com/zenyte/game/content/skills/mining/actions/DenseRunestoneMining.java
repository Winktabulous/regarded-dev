package com.zenyte.game.content.skills.mining.actions;

import com.near_reality.game.content.skills.mining.PickAxeDefinition;
import com.zenyte.game.content.boons.impl.MinerFortyNiner;
import com.zenyte.game.content.skills.mining.MiningDefinitions;
import com.zenyte.game.content.skills.mining.MiningDefinitions.PickaxeDefinitions;
import com.zenyte.game.item.Item;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.player.Action;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.ItemChat;
import com.zenyte.plugins.dialogue.PlainChat;

import java.util.Optional;

/**
 * @author Kris | 26/04/2019 17:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DenseRunestoneMining extends Action {
    private final WorldObject rock;
    private PickAxeDefinition tool;
    private int ticks;
    private int cycle;

    public DenseRunestoneMining(final WorldObject rock) {
        this.rock = rock;
    }

    @Override
    public boolean start() {
        if (player.getVarManager().getBitValue(rock.getDefinitions().getVarbitId()) == 1) {
            player.sendMessage("The runestone has depleted.");
            return false;
        }
        if (!checkTool()) return false;
        if (!check()) {
            return false;
        }
        player.sendMessage("You swing your pick at the runestone.");
        delay(7);
        return true;
    }

    @Override
    public boolean process() {
        if (!check()) {
            return false;
        }
        if (ticks++ % 4 == 0) {
            if ((cycle++ & 1) == 0) {
                player.setAnimation(tool.getAnim());
            } else {
                player.setAnimation(new Animation(7201));
            }
        }
        return checkObject();
    }

    @Override
    public int processWithDelay() {
        player.getSkills().addXp(SkillConstants.MINING, 12);
        player.getSkills().addXp(SkillConstants.CRAFTING, 8);
        player.getInventory().addOrDrop(new Item(13445));
        boolean deplete = !player.getBoonManager().hasBoon(MinerFortyNiner.class);
        if (deplete && Utils.random(10 + (player.getSkills().getLevel(SkillConstants.MINING) / 30)) == 0) {
            player.getVarManager().sendBit(rock.getDefinitions().getVarbitId(), 1);
            WorldTasksManager.schedule(() -> player.getVarManager().sendBit(rock.getDefinitions().getVarbitId(), 0), 30);
            return -1;
        }
        return 7;
    }

    @Override
    public void stop() {
        player.setAnimation(Animation.STOP);
    }

    private boolean check() {
        return (checkLevel() && player.getInventory().checkSpace());
    }

    private boolean checkTool() {
        final Optional<MiningDefinitions.PickaxeDefinitions.PickaxeResult> axe = PickaxeDefinitions.get(player, true);
        if (!axe.isPresent()) {
            player.getDialogueManager().start(new PlainChat(player, "You need a pickaxe to mine this rock. You do not have a pickaxe which you have the Mining level to use."));
            return false;
        }
        if (!player.getInventory().containsItem(1755, 1)) {
            player.getDialogueManager().start(new ItemChat(player, new Item(1755), "You need a chisel to craft the runestone into blocks."));
            return false;
        }
        final MiningDefinitions.PickaxeDefinitions.PickaxeResult definitions = axe.get();
        this.tool = definitions.getDefinition();
        return true;
    }

    private boolean checkLevel() {
        final Skills skills = player.getSkills();
        if (skills.getLevel(SkillConstants.MINING) < 38 || skills.getLevel(SkillConstants.CRAFTING) < 38) {
            player.getDialogueManager().start(new PlainChat(player, "You need a Mining and a Crafting level of 38 to mine this rock."));
            return false;
        }
        return true;
    }

    private boolean checkObject() {
        return player.getVarManager().getBitValue(rock.getDefinitions().getVarbitId()) == 0;
    }
}
