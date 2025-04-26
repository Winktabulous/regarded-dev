package com.zenyte.game.content.skills.construction;

import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Kris | 6. march 2018 : 16:20.06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class CombatDummyNPC extends NPC {

    private final WorldObject object;

    public CombatDummyNPC(final int id, final WorldObject object) {
        super(id, object, Direction.cardinalDirections[object.getRotation()], 0);
        this.combat = new DummyCombatHandler(this);
        this.object = object;
    }
	
	/*@Override
	public int getRandomMeleeHit(Player player, final int maxhit, final double modifier, final AttackType oppositeIndex) {
		return maxhit;
	}
	
	@Override
	public int getRandomRangedHit(Player player, int maxhit, double modifier, AttackType oppositeIndex) {
		return maxhit;
	}
	
	@Override
	public int getRandomMagicHit(Player player, CombatSpell spell, int maxhit, double modifier, AttackType oppositeIndex) {
		return maxhit;
	}*/
	
	@Override
    public void processHit(final Hit hit) {
		if (isDead())
			return;
		getUpdateFlags().flag(UpdateFlag.HIT);
        getNextHits().add(hit);
        if (!getHitBars().isEmpty())
            getHitBars().clear();
    }

    @Override
    public float getXpModifier(Hit hit) {
        return 0;
    }

    public WorldObject getObject() {
        return object;
    }

    private static final class DummyCombatHandler extends NPCCombat {

        public DummyCombatHandler(NPC npc) {
            super(npc);
        }

        @Override
        public int combatAttack() {
            return 0;
        }

        @Override
        public boolean checkAll() {
            return false;
        }

    }

}
