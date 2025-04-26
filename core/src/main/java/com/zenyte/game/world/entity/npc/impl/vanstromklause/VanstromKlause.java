package com.zenyte.game.world.entity.npc.impl.vanstromklause;

import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.EntityHitBar;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.pathfinding.events.npc.NPCTileEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.game.world.region.RSPolygon;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class VanstromKlause extends NPC implements CombatScript {

    private static final int NPC_ID = NpcId.VANSTROM_KLAUSE_9569;
    private static final int MIST = NpcId.MIST_9572;
    private static final int BLOOD_ORB = 38015;//anim 8652
    private static final int ACID_POOL = 37991;
    private static final Graphics BLOOD_ORB_EXPLODE = new Graphics(1791);
    private static final Graphics BLOODVELD_GFX = new Graphics(1792);
    private static final Graphics LIGHTNING_FIRST = new Graphics(1796);
    private static final Graphics LIGHTNING = new Graphics(1794);

    private static final Animation AUTO_ANIM = new Animation(8704);
    private static final Animation SPECIAL_ANIM = new Animation(8722);

    RSPolygon POLYGONS = new RSPolygon(new int[][] {
            { 3565, 3350 },
            { 3565, 3367 },
            { 3571, 3367 },
            { 3575, 3363 },
            { 3575, 3354 },
            { 3570, 3350 }});

    private WorldObject acidPool;
    private WorldObject bloodOrb;
    private int acidTicks;
    private int lightningTicks;
    private final ObjectArrayList<Location> FIGHT_AREA = new ObjectArrayList<>();
    ObjectArrayList<Location> lightningLocations = new ObjectArrayList<>();
    private int bloodOrbTicks;

    private final Player player;

    private int autoAttack;
    private int specialAttack;

    public boolean secondStage = false;
    private final VanstromInstance instance;
    private NPC bloodVeld;

    public VanstromKlause(VanstromInstance instance, Player player) {
        super(NPC_ID, instance.getLocation(3568, 3358), Direction.SOUTH, -1);
        this.player = player;
        this.instance = instance;
        for (Location allposition : POLYGONS.getAllpositions(0)) {
            FIGHT_AREA.add(instance.getLocation(allposition));
        }
        forceAggressive = true;
        attackDistance = 3;
        player.getHpHud().open(NPC_ID, 750);
        player.setFaceLocation(getLocation().copy());
        hitBar = new EntityHitBar(this) {
            @Override
            public int getType() {
                return 18;
            }
        };
        spawned = true;
    }

    @Override
    public int attack(Entity target) {
        if(!secondStage) {
            if (shouldSpecial()) {
                specialAttack++;
                if (specialAttack > 2)
                    specialAttack = 0;


                setAnimation(SPECIAL_ANIM);
                switch (specialAttack) {
                    case 0 -> bloodVeldAttack();
                    case 1 -> darkness();
                    case 2 -> bloodOrb();
                }
                autoAttack = 0;
                return 6;
            }
        }

        autoAttack();
        return 6;
    }

    public void autoAttack() {
        setAnimation(AUTO_ANIM);
        boolean blood = Utils.random(1) == 0;
        CombatSpell spell = blood ? CombatSpell.BLOOD_BURST : CombatSpell.SHADOW_BURST;
        player.sendSound(spell.getCastSound());
        delay(1, () -> {
            player.sendSound(spell.getHitSound());
            if(blood) {
                player.setGraphics(new Graphics(376));
            } else {
                player.setGraphics(new Graphics(382));
            }
            if(secondStage) {
                player.applyHit(new Hit(Utils.random(1, 10), HitType.MAGIC));
            } else {
                int hit = getRandomMaxHit(this,
                        bloodOrb == null ? combatDefinitions.getMaxHit() : combatDefinitions.getMaxHit() + 15, MAGIC, player);
                delayHit(this, 0, player, new Hit(this, hit, HitType.MAGIC));

                if(hit > 4 && Utils.random(1) == 0)
                    heal((int) (hit * 0.75));
            }
        });
        autoAttack++;
    }

    @Override
    public int getDeathDelay() {
        return 1;
    }

    @Override
    protected void postHitProcess(Hit hit) {
        player.getHpHud().updateValue(getHitpoints());
    }

    @Override
    public double getMagicPrayerMultiplier() {
        return 0.5;
    }

    public boolean shouldSpecial() {
        return autoAttack > 8;
    }

    public Location randomLocation(){
        return instance.getLocation(new Location(3570, 3362)).random(1);
    }
    public void bloodVeldAttack() {
        player.sendMessage("<col=ff1100>Vanstrom summons a bloodveld!");
        setForceTalk("My pets will feast on your corpse!");
        WorldTasksManager.schedule(() -> {
            bloodVeld = new AcidicBloodveld(randomLocation(), player, this).spawn();
            World.sendGraphics(BLOODVELD_GFX, bloodVeld.getMiddleLocation());
        }, 2);
    }

    public void darkness() {
        player.sendMessage("<col=ff1100>Darkness emanates from Vanstrom!");
        setForceTalk("Stare into darkness!");
        delay(1, () -> {
            player.getInterfaceHandler().sendInterface(InterfacePosition.OVERLAY, 174);
            player.getPacketDispatcher().sendClientScript(951);
            delay(1, () -> {
                Direction vanstromDirection = Direction.getDirection(player.getLocation(), getLocation());
                Direction playerDirection = Direction.getDirection(player.getFaceLocation(), player.getLocation());
                if(vanstromDirection == playerDirection || player.getFaceEntity() == getClientIndex()) {
                    player.sendMessage("<col=ff1100>You're consumed by the darkness");
                    player.applyHit(new Hit(Utils.random(40, 55), HitType.REGULAR));
                } else {
                    player.sendMessage("<col=03ad06>You manage to avoid the darkness");//green
                }
                player.getPacketDispatcher().sendClientScript(948, 0, 0, 0, 255, 50);
            });
        });
    }

    public void bloodOrb() {
        setForceTalk("Blood will be my strength!");
        player.sendMessage("<col=ff1100>Vanstrom summons a magical blood orb!");
        delay(1, () -> {
            player.sendMessage("The blood orb increases the damage of Vanstrom's magic attacks");
            removeBloodOrb();
            Location location = instance.getLocation(new Location(3569, 3353)).random(1);
            World.spawnObject(bloodOrb = new WorldObject(BLOOD_ORB, 10, 0, location));
        });
    }


    public void spawnAcid(Location location) {
        removeAcid();
        World.spawnObject(acidPool = new WorldObject(ACID_POOL, 10, 0, location));
    }

    public void removeAcid() {
        if(acidPool != null) {
            World.removeObject(acidPool);
            acidPool = null;
        }
        acidTicks = 0;
    }
    public void removeBloodOrb() {
        if(bloodOrb != null) {
            World.removeObject(bloodOrb);
            bloodOrb = null;
        }
        bloodOrbTicks = 0;
    }

    @Override
    public void handleIngoingHit(Hit hit) {
        if(transforming) {
            hit.setDamage(0);
        }
        if(hit.getSource() instanceof Player) {
            Player player = (Player) hit.getSource();
            if(hit.getHitType() == HitType.MAGIC) {
                hit.setDamage(0);
                player.sendMessage("Magic does not harm Vanstrom.");
            }else if(!player.getEquipment().containsItem(ItemId.IVANDIS_FLAIL) && !player.getEquipment().containsItem(ItemId.BLISTERWOOD_FLAIL)) {
                hit.setDamage(0);
                player.sendMessage("You must use a Blisterwood Flail, or Ivandis flail to harm Vampyres.");
            }
        }
        super.handleIngoingHit(hit);
    }

    @Override
    public boolean canAttack(Player source) {
        return !transforming;
    }

    @Override
    public boolean checkAggressivity() {
        if(transforming)
            return false;
        return super.checkAggressivity();
    }

    private boolean transforming;
    @Override
    public void sendDeath() {
        if(!secondStage) {
            if(transforming)
                return;
            transforming = true;
            setForceTalk("Ha! Did you think you could beat me that easily?");
            setAnimation(new Animation(8712));
            cancelCombat();
            player.cancelCombat();
            lock();
            temporaryAttributes.put("ignoreWalkingRestrictions", true);

            WorldTask runnable = () -> {
                unlock();
                setTransformation(9570);
                setLocation(instance.getLocation(new Location(3568, 3358)));
                setFaceEntity(player);
                setAnimation(new Animation(8716));
                delay(2, () -> {
                    setForceTalk("See if you can survive this!");
                    setTransformation(NPC_ID);
                    setGraphics(new Graphics(1793));
                    secondStage = true;
                    delay(1, () -> {
                        heal(200);
                        getUpdateFlags().flag(UpdateFlag.HIT);
                        getNextHits().add(new Hit(200, HitType.HEALED));
                        addHitbar();
                        player.getHpHud().updateValue(200);
                        transforming = false;
                    });
                });
            };

            delay(2, () -> {
                setTransformation(MIST);
                NPCTileEvent tileEvent = new NPCTileEvent(this, new TileStrategy(instance.getLocation(new Location(3567, 3357))), () -> delay(1, runnable));
                tileEvent.setOnFailure(runnable);
                setRouteEvent(tileEvent);
            });
            return;
        }

        deathDelay = 1;
        player.getBossTimer().finishTracking("Vanstrom Klause");
        player.getHpHud().close();
        super.sendDeath();
    }

    @Override
    public void finish() {
        removeBloodOrb();
        removeAcid();
        if(bloodVeld != null && !bloodVeld.isFinished())
            bloodVeld.finish();
        super.finish();
    }

    @Override
    public boolean isDead() {
        if(transforming)
            return false;
        return super.isDead();
    }

    @Override
    public boolean isDying() {
        if(transforming)
            return false;
        return super.isDying();
    }

    @Override
    public void processNPC() {
        super.processNPC();

        if(isDying() || isDead())
            return;

        if(acidPool != null) {
            acidTicks++;
            if(player.getLocation().equals(acidPool.getLocation())) {
                player.applyHit(new Hit(5, HitType.REGULAR));
            }
            if(acidTicks >= 50) {
                removeAcid();
            }
        }

        if(bloodOrb != null) {
            bloodOrbTicks++;
            boolean exploded = false;
            if(player.getLocation().withinDistance(bloodOrb.getLocation(), 1)) {
                player.applyHit(new Hit(Utils.random(30, 39), HitType.REGULAR));
                World.sendGraphics(BLOOD_ORB_EXPLODE, bloodOrb.getLocation());
                removeBloodOrb();
                player.sendMessage("<col=ff1100>You're badly hurt by the exploding blood orb!");
                exploded = true;
            }

            if(!exploded && this.getLocation().withinDistance(bloodOrb.getLocation(), 1)) {
                applyHit(new Hit(Utils.random(30, 39), HitType.REGULAR));
                World.sendGraphics(BLOOD_ORB_EXPLODE, bloodOrb.getLocation());
                removeBloodOrb();
            }
            if(bloodOrbTicks >= 50) {
                removeBloodOrb();
            }
        }

        if(secondStage && !transforming) {
            if(lightningTicks == 0) {
                for (int i = 0; i < 15; i++) {
                    Location random = Utils.random(FIGHT_AREA);
                    lightningLocations.add(random);
                    World.sendGraphics(LIGHTNING_FIRST, random);
                }
                lightningTicks++;
                return;
            }
            if(lightningTicks == 2) {
                for (Location lightningLocation : lightningLocations) {
                    World.sendGraphics(LIGHTNING, lightningLocation);
                    if(player.getLocation().equals(lightningLocation))
                        player.applyHit(new Hit(Utils.random(20, 30), HitType.REGULAR));
                }
                lightningLocations.clear();
                lightningTicks = 0;
                return;
            }
            lightningTicks++;
        }
    }

    @Override
    public boolean isTolerable() {
        return false;
    }

}
