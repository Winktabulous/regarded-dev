package com.zenyte.game.content.wildernessVault.queenreaver;

import com.zenyte.game.content.wildernessVault.WildernessVaultConstants;
import com.zenyte.game.content.wildernessVault.queenreaver.QueenReaver;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

public class QueenReaverMinion extends NPC implements CombatScript {

    private QueenReaver reaver;
    private boolean dying = false;

    public QueenReaverMinion(Location location, QueenReaver queenReaver) {
        super(WildernessVaultConstants.MINION2_ID, new Location(location), Direction.NORTH, 0);
        this.reaver = queenReaver;
        this.spawned = true;
        this.supplyCache = false;
        this.setCrawling(true);
    }


    @Override
    public void processNPC() {
        setFaceEntity(reaver);
        if (dying || isFinished() || isDead() || reaver.isFinished() || reaver.isDead()) {
            finish();
            return;
        }
        if (!hasWalkSteps() && !isFrozen() && Utils.getDistance(getX(), getY(), reaver.getMiddleLocation().getX(),  reaver.getMiddleLocation().getY()) < 1) {
            if (!dying)
                absorb();
            return;
        }
        if (!isFrozen() && !hasWalkSteps()) {
            calcFollow(reaver.getMiddleLocation(), -1, true, true, false);
        }
    }

    @Override
    protected boolean canMove(int fromX, int fromY, int direction) {
        return true;
    }

    @Override
    public boolean isEntityClipped() {
        return false;
    }

    @Override
    public void sendDeath() {
        dying = true;
        WorldTasksManager.schedule(() -> onFinish(null), 1);
    }

    private void absorb() {
        reaver.absorb(this);
        setHitpoints(0);
    }


    @Override
    public int attack(Entity target) {
        return 0;
    };

    @Override

    protected void onDeath(Entity source) {
        super.onDeath(source);
    }

    @Override
    public void autoRetaliate(Entity source) {}

    @Override
    public boolean isDying() {
        return dying;
    }
}