package com.zenyte.game.content.minigame.inferno.model;

import com.near_reality.game.world.entity.TargetSwitchCause;
import com.zenyte.game.content.follower.PetWrapper;
import com.zenyte.game.content.minigame.inferno.instance.Inferno;
import com.zenyte.game.content.minigame.inferno.npc.impl.JalNib;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.CharacterLoop;
import mgi.types.config.AnimationDefinitions;

/**
 * @author Tommeh | 26/11/2019 | 19:06
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class RockySupport extends NPC {
    private static final Animation collapseAnim = new Animation(7561);
    private static final SoundEffect collapseSound = new SoundEffect(1021, 7, 0);
    private final WorldObject object;
    private final Inferno inferno;
    private final RockySupportLocation type;

    @Override
    public boolean isCycleHealable() {
        return false;
    }

    public RockySupport(final RockySupportLocation type, final Inferno inferno) {
        super(7709, inferno.getLocation(type.getLocation()), Direction.SOUTH, 5);
        this.type = type;
        this.inferno = inferno;
        object = new WorldObject(type.getId(), 10, 0, inferno.getLocation(type.getLocation()));
        setTargetType(EntityType.NPC);
        this.combat = new NPCCombat(this) {
            @Override
            public void setTarget(final Entity target, TargetSwitchCause cause) {
            }
            @Override
            public void forceTarget(final Entity target) {
            }
        };
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    protected boolean isMovableEntity() {
        return false;
    }

    @Override
    public void setAnimation(final Animation animation) {
        this.animation = animation;
        if (animation == null) {
            updateFlags.set(UpdateFlag.ANIMATION, false);
            lastAnimation = 0;
        } else {
            updateFlags.flag(UpdateFlag.ANIMATION);
            final AnimationDefinitions defs = AnimationDefinitions.get(animation.getId());
            if (defs != null) {
                lastAnimation = Utils.currentTimeMillis() + defs.getDuration();
            } else {
                lastAnimation = Utils.currentTimeMillis();
            }
        }
    }

    @Override
    public NPC spawn() {
        if (!isFinished()) {
            throw new RuntimeException("The NPC has already been spawned: " + getId() + ", " + getDefinitions().getName() + ", " + getNpcSpawn() + ", " + getLocation());
        }
        World.addNPC(this);
        location.setLocation(getRespawnTile());
        setFinished(false);
        updateLocation();
        if (!combatDefinitionsMap.isEmpty()) {
            combatDefinitionsMap.clear();
        }
        updateCombatDefinitions();
        World.spawnObject(object);
        if (inferno != null) {
            inferno.getPlayer().getVarManager().sendBit(object.getDefinitions().getVarbitId(), 250 - hitpoints);
        }
        return this;
    }

    @Override
    public boolean setHitpoints(final int amount) {
        final boolean dead = isDead();
        this.hitpoints = amount;
        if (!dead && hitpoints <= 0) {
            sendDeath();
            return true;
        }
        if (inferno != null) {
            inferno.getPlayer().getVarManager().sendBit(object.getDefinitions().getVarbitId(), 250 - hitpoints);
        }
        return false;
    }

    @Override
    public void sendDeath() {
        for (final JalNib nibbler : inferno.getNPCs(JalNib.class)) {
            nibbler.applyHit(new Hit(this, 20, HitType.REGULAR));
        }
        CharacterLoop.forEach(getMiddleLocation(), 2, Entity.class, e -> {
            if (e instanceof NPC && PetWrapper.getByPet(((NPC) e).getId()) != null || e.equals(RockySupport.this)) {
                return;
            }
            e.applyHit(new Hit(Math.min(e.getMaxHitpoints(), e.getHitpoints() / 2), HitType.REGULAR));
        });
        World.removeObject(object);
        World.sendSoundEffect(this, collapseSound);
        final NPC fallenPillar = new NPC(7710, getLocation(), Direction.SOUTH, 0);
        fallenPillar.lock();
        fallenPillar.setInvalidAnimation(collapseAnim);
        fallenPillar.spawn();
        WorldTasksManager.schedule(fallenPillar::finish, 2);
        finish();
    }

    public WorldObject getObject() {
        return object;
    }

    public RockySupportLocation getType() {
        return type;
    }
}
