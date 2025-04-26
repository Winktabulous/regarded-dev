package com.zenyte.game.world.entity.npc.impl.misc;

import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;

/**
 * @author Kris | 26/06/2019 13:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class DarkWizard extends NPC implements Spawnable, CombatScript {
    private static final Location draynor = new Location(3080, 3242, 0);

    public DarkWizard(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
        if (tile == null) {
            return;
        }
        if (draynor.withinDistance(tile, 10)) {
            this.aggressionDistance = 3;
            this.attackDistance = 7;
            return;
        }
        this.radius = 5;
        this.aggressionDistance = 10;
        this.attackDistance = 10;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("Dark wizard");
    }

    @Override
    public int attack(final Entity target) {
        final int combat = getCombatLevel();
        final int spellType = Utils.random(5);
        final CombatSpell spell = spellType <= 4 ? (combat == 7 ? CombatSpell.WATER_STRIKE : CombatSpell.EARTH_STRIKE) : (combat == 7 ? CombatSpell.CONFUSE : CombatSpell.WEAKEN);
        useSpell(spell, target);
        return 4;
    }
}
