package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.content.boss.BossRespawnTimer;
import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.content.godwars.instance.GodwarsInstance;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CAType;

/**
 * @author Kris | 25. okt 2017 : 11:17.15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public class GeneralGraardor extends GodwarsBossNPC implements Spawnable, CombatScript {
    private static final ForceTalk[] quotes = new ForceTalk[] {new ForceTalk("Death to our enemies!"), new ForceTalk("Brargh!"), new ForceTalk("Break their bones!"), new ForceTalk("For the glory of Bandos!"), new ForceTalk("Split their skulls!"), new ForceTalk("We feast on the bones of our enemies tonight!"), new ForceTalk("CHAAARGE"), new ForceTalk("Crush them underfoot!"), new ForceTalk("All glory to Bandos!"), new ForceTalk("GRRRAAAAAR!"), new ForceTalk("FOR THE GLORY OF THE BIG HIGH WAR GOD!")};

    private boolean hitSomeone;

    @Override
    protected void onFinish(Entity source) {
        super.onFinish(source);

        if (source instanceof final Player player) {
            player.getCombatAchievements().checkKcTask("general graardor", 50, CAType.GENERAL_GRAARDOR_ADEPT);
            player.getCombatAchievements().checkKcTask("general graardor", 100, CAType.GENERAL_GRAARDOR_VETERAN);
            if (allMinionsDead()) {
                player.getCombatAchievements().complete(CAType.GENERAL_SHOWDOWN);
            }
            if (isFrozen()) {
                player.getCombatAchievements().complete(CAType.OURG_FREEZER);
            }
            if ((int) player.getAttributes().getOrDefault(GodwarsInstance.CA_TASK_INSTANCE_KC_ATT, 0) >= 15) {
                player.getCombatAchievements().complete(CAType.OURG_KILLER);
            }
            if (!hitSomeone) {
                player.getCombatAchievements().complete(CAType.OURG_FREEZER_II);
                if (!minionsHitSomeone && player.getAttributes().containsKey(GodwarsInstance.CA_TASK_INSTANCE_ENTERED_ATT)) {
                    player.getCombatAchievements().complete(CAType.KEEP_AWAY);
                }
            }
        }
    }

    public GeneralGraardor(final int id, final Location tile, final Direction direction, final int radius) {
        super(id, tile, direction, radius);
        if (isAbstractNPC() || tile.getX() >= 6400) return;
        setMinions(new GodwarsBossMinion[] {new GodwarsBossMinion(NpcId.SERGEANT_STRONGSTACK, new Location(2868, 5362, 2), Direction.SOUTH, 5), new SergeantSteelwill(NpcId.SERGEANT_STEELWILL, new Location(2872, 5354, 2), Direction.SOUTH, 5), new SergeantGrimspike(NpcId.SERGEANT_GRIMSPIKE, new Location(2871, 5359, 2), Direction.SOUTH, 5)});
    }

    @Override
    protected BossRespawnTimer timer() {
        return BossRespawnTimer.GENERAL_GRAARDOR;
    }

    public GeneralGraardor(final GodwarsBossMinion[] minions, final int id, final Location tile, final Direction direction, final int radius) {
        this(id, tile, direction, radius);
        setMinions(minions);
    }

    @Override
    protected ForceTalk[] getQuotes() {
        return quotes;
    }

    @Override
    protected int diaryFlag() {
        return 1;
    }

    @Override
    public GodType type() {
        return GodType.BANDOS;
    }

    @Override public void handleOutgoingHit(Entity target, Hit hit) {
        super.handleOutgoingHit(target, hit);
        if (hit.getDamage() > 0) {
            hitSomeone = true;
        }
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == NpcId.GENERAL_GRAARDOR;
    }

    private static final Projectile projectile = new Projectile(1202, 41, 16, 30, 5, 10, 0, 5);
    private static final Animation meleeAnimation = new Animation(7018);
    private static final Animation rangedAnimation = new Animation(7021);

    @Override
    public int attack(final Entity target) {
        if (Utils.randomBoolean(2)) {
            setAnimation(rangedAnimation);
            for (final Entity t : getPossibleTargets(EntityType.PLAYER)) {
                int damage = getRandomMaxHit(this, 35, RANGED, t);
                //graardor deals a minimum of 15 damage upon successful hit; for even distribution, we re-calc it.
                if (damage > 0) {
                    damage = Utils.random(15, 35);
                }
                delayHit(this, World.sendProjectile(this, t, projectile), t, new Hit(this, damage, HitType.RANGED));
            }
        } else {
            setAnimation(meleeAnimation);
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        }
        return getCombatDefinitions().getAttackSpeed();
    }
}
