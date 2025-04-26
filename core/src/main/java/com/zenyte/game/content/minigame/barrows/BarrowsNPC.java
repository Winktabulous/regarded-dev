package com.zenyte.game.content.minigame.barrows;

import com.zenyte.GameToggles;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author Kris | 30/11/2018 16:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BarrowsNPC extends NPC implements Spawnable {
    public BarrowsNPC(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    WeakReference<Player> owner;
    protected boolean pvmArenaVersion = false;
    private final boolean wight = this instanceof BarrowsWightNPC;

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (pvmArenaVersion)
            return;
        if(!GameToggles.BARROWS_OLD_DISABLED || (source instanceof Player p && p.getPlane() > 0)) {
            if (source instanceof Player) {
                ((Player) source).getBarrows().onDeath(this);
            }
        }
    }

    @SuppressWarnings("unchecked")
    static final List<BarrowsNPC> getWightsList(@NotNull final Player player) {
        Object listAttr = player.getTemporaryAttributes().get("barrows monsters list");
        if (!(listAttr instanceof ObjectArrayList<?>)) {
            listAttr = new ObjectArrayList<NPC>();
            player.getTemporaryAttributes().put("barrows monsters list", listAttr);
        }
        final ObjectArrayList<BarrowsNPC> list = (ObjectArrayList<BarrowsNPC>) listAttr;
        list.removeIf(n -> n.isDead() || n.isFinished());
        return list;
    }

    @Override
    public void onFinish(final Entity source) {
        super.onFinish(source);
        if (pvmArenaVersion)
            return;
        if(!GameToggles.BARROWS_OLD_DISABLED || (source instanceof Player p && p.getPlane() > 0)) {
            if (source instanceof Player) {
                ((Player) source).getBarrows().onFinish(this);
            }
            if (!wight) {
                if (owner != null) {
                    final Player owner = this.owner.get();
                    if (owner != null) {
                        getWightsList(owner).remove(this);
                    }
                }
            }
        }
    }

    private int ticks = -1;

    @Override
    public NPC spawn() {
        final NPC npc = super.spawn();

        if (pvmArenaVersion)
            return npc;

        if(!GameToggles.BARROWS_OLD_DISABLED || (npc.getPlane() > 0)) {
            if (spawned) {
                ticks = 500;
            }
            if (!wight) {
                if (owner != null) {
                    final Player owner = this.owner.get();
                    if (owner != null) {
                        getWightsList(owner).add(this);
                    }
                }
            }
        }
        return npc;
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (pvmArenaVersion)
            return;
        if(!GameToggles.BARROWS_OLD_DISABLED || (this.getPlane() > 0)) {
            if (ticks != -1) {
                if (--ticks <= 0) {
                    finish();
                }
            }
            if (isDead() || owner == null) return;
            final Player owner = this.owner.get();
            if (owner == null) {
                return;
            }
            if (owner.getLocation().getDistance(this.getLocation()) >= 15) {
                if (wight) {
                    owner.getBarrows().removeTarget();
                }
                finish();
            }
        }
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

    @Override
    public boolean validate(int id, String name) {
        return id >= 1678 && id <= 1688;
    }

    @Override
    public boolean isEntityClipped() {
        return !wight && id != 1679 && id != 1683;
    }

    @Override
    public int getRespawnDelay() {
        return 15;
    }

    public void setPvmArenaVersion(boolean pvmArenaVersion) {
        this.pvmArenaVersion = pvmArenaVersion;
    }

    public boolean isPvmArenaVersion() {
        return pvmArenaVersion;
    }
}
