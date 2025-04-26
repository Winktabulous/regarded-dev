package com.zenyte.game.world.entity.player.action.combat.special;

import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.race.Demon;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.action.combat.SpecialAttackScript;

import static com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell.*;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-17
 */
public class ScatteredAshesSpecial implements SpecialAttackScript {
    @Override
    public void attack(Player player, PlayerCombat combat, Entity target) {
        var spellbook = player.getCombatDefinitions().getSpellbook();
        if (spellbook != Spellbook.ARCEUUS) {
            player.sendMessage("You cannot use this Special from the %s spellbook".formatted(spellbook));
            return;
        }
        if (Demon.isDemon(((NPC) target), true)) {
            var magicLevel = player.getSkills().getLevel(Skills.MAGIC);
            if (magicLevel < 44) {
                player.sendMessage("You need a magic level of at least 44 to use this Special Attack.");
                return;
            }
            var size = target.getSize();
            var gfxId = switch(size) {
                case 3 -> 2813;
                case 2 -> 2812;
                default -> 2811;
            };
            var spell = INFERIOR_DEMONBANE;
            if (magicLevel >= 82)
                spell = DARK_DEMONBANE;
            else if (magicLevel >= 62)
                spell = SUPERIOR_DEMONBANE;
            // TODO: World.sendSoundEffect(player, ID)
            PlayerCombat.magicAttack(player, target, spell, false);
            target.setGraphics(new Graphics(gfxId, 1, 4));
        }
        else
            player.sendMessage("This special can only be cast on Demonic Creatures.");
    }
}
