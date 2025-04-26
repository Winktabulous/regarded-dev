package com.zenyte.game.world.entity.npc.impl.slayer.superior.impl;

import com.zenyte.game.content.skills.slayer.SlayerEquipment;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import com.zenyte.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CaveAbomination extends SuperiorNPC implements CombatScript {
    public CaveAbomination(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7401, tile);
        this.deathDelay = 1;
    }

    private static final Animation ATTACK_1_ANIMATION = new Animation(4234);
    private static final Animation ATTACK_2_ANIMATION = new Animation(4235);
    private static final Animation ATTACK_3_ANIMATION = new Animation(4237);
    private static final Animation PLAYER_ANIMATION = new Animation(4434);

    @Override
    public int attack(Entity target) {
        if (!(target instanceof Player)) {
            return 0;
        }
        final Player player = (Player) target;
        int random = Utils.random(4) == 0 ? 4 : Utils.random(1);
        setAnimation(random == 0 ? ATTACK_1_ANIMATION : random == 1 ? ATTACK_2_ANIMATION : ATTACK_3_ANIMATION);
        final boolean hasWitchwoodIcon = SlayerEquipment.WITCHWOOD_ICON.isWielding(player);
        delayHit(this, 0, target, new Hit(this, hasWitchwoodIcon ? getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MAGIC, target) : 24, HitType.MELEE).setExecuteIfLocked());
        if (random == 4) {
            player.setAnimation(PLAYER_ANIMATION);
            player.lock(3);
        }
        return getCombatDefinitions().getAttackSpeed();
    }
}
