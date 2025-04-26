package com.zenyte.game.content.stars;

import com.zenyte.game.content.skills.mining.actions.Mining;
import com.zenyte.game.util.Direction;
import com.zenyte.game.world.World;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import org.jetbrains.annotations.NotNull;

import static com.zenyte.game.world.broadcasts.BroadcastType.LOTTERY;

/**
 * @author Andys1814
 */
public final class ShootingStar extends WorldObject  {

    // Your Celestial ring has 673 charges.

    private ShootingStarLocation location;

    private ShootingStarLevel level;

    private int stardust;

    private final NPC progressBarNpc;

    private final ShootingStarProgressBar progressBar = new ShootingStarProgressBar(0);

    private boolean undiscovered;

    public ShootingStar(ShootingStarLevel level, ShootingStarLocation location, boolean undiscovered) {
        super(level.getObjectId(), 10, WorldObject.DEFAULT_ROTATION, location.getX(), location.getY(), location.getZ());
        this.location = location;
        this.level = level;
        this.stardust = level.getStardust();
        this.progressBarNpc = new NPC(10629, getLocation(), Direction.SOUTH, 0);
        this.progressBarNpc.spawn();
        this.undiscovered = undiscovered;
    }

    public ShootingStar(ShootingStarLevel level, ShootingStarLocation location) {
        super(level.getObjectId(), 10, WorldObject.DEFAULT_ROTATION, location.getX(), location.getY(), location.getZ());
        this.location = location;
        this.level = level;
        this.stardust = level.getStardust();
        this.progressBarNpc = new NPC(10629, getLocation(), Direction.SOUTH, 0);
        this.progressBarNpc.spawn();
        this.undiscovered = false;
    }

    public int getStardust() {
        return stardust;
    }

    public void onHarvest() {
        stardust--;

        progressBar.setPercentage(percentRemaining());
        progressBarNpc.getUpdateFlags().flag(UpdateFlag.HIT);
        progressBarNpc.getHitBars().add(progressBar);

        if (stardust <= 0) {
            progressBarNpc.remove();
            World.removeObject(this);

            World.sendSoundEffect(getLocation(), new SoundEffect(4923, 10));

            if (level == ShootingStarLevel.ONE) {
                CharacterLoop.forEach(getLocation(), 5, Player.class, (player) -> {
                    if (player.getActionManager().getAction() instanceof Mining) {
                        player.sendMessage("The star disintegrates into dust.");
                    }
                });
                WorldBroadcasts.sendMessage("<img=51><col=2980B9><shad=000000>The shooting star has been fully mined - keep an eye out for the next one!", LOTTERY, false);
                ShootingStars.setCurrent(null);
                return;
            }

            ShootingStarLevel next = level.getNextLevel();

            ShootingStar star = new ShootingStar(next, location);
            World.spawnObject(star);
            ShootingStars.setCurrent(star);
        }
    }

    public void remove() {
        progressBarNpc.remove();
        World.removeObject(this);
        CharacterLoop.forEach(getLocation(), 5, Player.class, (player) -> {
            if (player.getActionManager().getAction() instanceof Mining) {
                player.sendMessage("The star disintegrates into dust.");
            }
        });
    }

    public int percentRemaining() {
        return (int) (((double) stardust / level.getStardust()) * 100.0);
    }

    public int percentMined() {
        return 100 - percentRemaining();
    }

    public ShootingStarLevel getLevel() {
        return level;
    }

    public ShootingStarLocation getStarLocation() {
        return location;
    }

    public boolean isUndiscovered() {
        return undiscovered;
    }

    public void setUndiscovered(boolean undiscovered) {
        this.undiscovered = undiscovered;
    }
}
