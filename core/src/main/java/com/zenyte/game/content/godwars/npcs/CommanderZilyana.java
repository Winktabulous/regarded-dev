package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.content.boss.BossRespawnTimer;
import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.content.godwars.instance.GodwarsInstance;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;

/**
 * @author Tommeh | 26 mrt. 2018 : 16:55:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CommanderZilyana extends GodwarsBossNPC implements Spawnable, CombatScript {
    private static final ForceTalk[] quotes = new ForceTalk[] {new ForceTalk("Death to the enemies of the light!"), new ForceTalk("Slay the evil ones!"), new ForceTalk("Saradomin lend me strength!"), new ForceTalk("By the power of Saradomin!"), new ForceTalk("May Saradomin be my sword!"), new ForceTalk("Good will always triumph!"), new ForceTalk("Forward! Our allies are with us!"), new ForceTalk("Saradomin is with us!"), new ForceTalk("In the name of Saradomin!"), new ForceTalk("All praise Saradomin!"), new ForceTalk("Attack! Find the Godsword!")};

    private boolean hitSomeone;
    private boolean receivedMeleeHitsOnly = true;

    public CommanderZilyana(final int id, final Location tile, final Direction direction, final int radius) {
        super(id, tile, direction, radius);
        if (isAbstractNPC() || tile.getX() >= 6400) return;
        setMinions(new GodwarsBossMinion[] {new GodwarsBossMinion(NpcId.STARLIGHT, new Location(2901, 5264, 0), Direction.SOUTH, 5), new Growler(NpcId.GROWLER, new Location(2897, 5263, 0), Direction.SOUTH, 5), new Bree(NpcId.BREE, new Location(2895, 5265, 0), Direction.SOUTH, 5)});
    }

    @Override
    protected void onFinish(Entity source) {
        super.onFinish(source);

        if (source instanceof final Player player) {
            player.getCombatAchievements().checkKcTask("commander zilyana", 50, CAType.COMMANDER_ZILYANA_ADEPT);
            player.getCombatAchievements().checkKcTask("commander zilyana", 100, CAType.COMMANDER_ZILYANA_VETERAN);
            if (allMinionsDead()) {
                player.getCombatAchievements().complete(CAType.COMMANDER_SHOWDOWN);
            }
            if ((int) player.getAttributes().getOrDefault(GodwarsInstance.CA_TASK_INSTANCE_KC_ATT, 0) >= 50) {
                player.getCombatAchievements().complete(CAType.PEACH_CONJURER);
            }
            if (player.getAttributes().containsKey(GodwarsInstance.CA_TASK_INSTANCE_ENTERED_ATT)) {
                if (receivedMeleeHitsOnly) {
                    player.getCombatAchievements().complete(CAType.REMINISCE);
                }
                if (!hitSomeone && !minionsHitSomeone) {
                    player.getCombatAchievements().complete(CAType.ANIMAL_WHISPERER);
                }
            }
        }
    }

    @Override
    protected BossRespawnTimer timer() {
        return BossRespawnTimer.COMMANDER_ZILYANA;
    }

    public CommanderZilyana(final GodwarsBossMinion[] minions, final int id, final Location tile, final Direction direction, final int radius) {
        this(id, tile, direction, radius);
        setMinions(minions);
    }

    @Override public void handleOutgoingHit(Entity target, Hit hit) {
        super.handleOutgoingHit(target, hit);
        if (hit.getDamage() > 0) {
            hitSomeone = true;
        }
    }

    @Override public void handleIngoingHit(Hit hit) {
        super.handleIngoingHit(hit);
        if (HitType.MAGIC.equals(hit.getHitType()) || HitType.RANGED.equals(hit.getHitType())) {
            receivedMeleeHitsOnly = false;
        }
    }

    @Override
    protected ForceTalk[] getQuotes() {
        return quotes;
    }

    @Override
    protected int diaryFlag() {
        return 2;
    }

    @Override
    public GodType type() {
        return GodType.SARADOMIN;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == NpcId.COMMANDER_ZILYANA;
    }

    public static final Animation meleeAnimation = new Animation(6967);
    public static final Animation magicAnimation = new Animation(6970);
    public static final Graphics magicGraphics = new Graphics(1221);
    public static final SoundEffect meleeAttackSound = new SoundEffect(3876, 10, 0);
    public static final SoundEffect specialHittingSound = new SoundEffect(3887, 10, 30);

    @Override
    public int attack(final Entity target) {
        final CommanderZilyana npc = this;
        final int style = Utils.random(2);
        if (style < 2) {
            npc.setAnimation(meleeAnimation);
            World.sendSoundEffect(getMiddleLocation(), meleeAttackSound);
            delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, 31, MELEE, target), HitType.MELEE));
        } else {
            npc.freeze(2);
            npc.setAnimation(magicAnimation);
            for (final Entity t : npc.getPossibleTargets(EntityType.PLAYER)) {
                int damage = getRandomMaxHit(npc, 20, MAGIC, t);
                //zilyana deals a minimum of 10 damage upon successful hit; for even distribution, we re-calc it.
                if (damage > 0) {
                    damage = Utils.random(10, 20);
                }
                delayHit(npc, 0, t, new Hit(npc, damage, HitType.MAGIC).onLand(hit -> t.setGraphics(magicGraphics)));
                World.sendSoundEffect(new Location(target.getLocation()), specialHittingSound);
            }
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
