package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.content.boss.BossRespawnTimer;
import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.content.godwars.instance.GodwarsInstance;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.*;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import com.zenyte.game.world.entity.player.calog.CAType;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;

/**
 * @author Tommeh | 26 mrt. 2018 : 16:55:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KrilTsutsaroth extends GodwarsBossNPC implements Spawnable, CombatScript {
    private static final Animation meleeAnimation = new Animation(6948);
    private static final Animation magicAnimation = new Animation(6950);
    private static final Projectile projectile = new Projectile(1155, 41, 16, 30, 5, 10, 0, 5);
    private static final ForceTalk forceTalk = new ForceTalk("YARRRRRRR!");
    private static final String message = "K'ril Tsutsaroth slams through your protection prayer, leaving you feeling" +
            " drained.";
    private static final SoundEffect magicSound = new SoundEffect(3866, 10, 0);
    private boolean usedSpecial;
    private boolean takenMeleeHit;
    private boolean onlyDemonBaneSpells = true;

    public KrilTsutsaroth(final int id, final Location tile, final Direction direction, final int radius) {
        super(id, tile, direction, radius);
        if (isAbstractNPC() || tile.getX() >= 6400) return;
        setMinions(new GodwarsBossMinion[] {new GodwarsBossMinion(NpcId.TSTANON_KARLAK, new Location(2929, 5327, 2), Direction.SOUTH, 5), new ZaklNGritch(NpcId.ZAKLN_GRITCH, new Location(2921, 5327, 2), Direction.SOUTH, 5), new BalfrugKreeyath(NpcId.BALFRUG_KREEYATH, new Location(2923, 5324, 2), Direction.SOUTH, 5)});
    }

    @Override
    protected BossRespawnTimer timer() {
        return BossRespawnTimer.KRIL_TSUTSAROTH;
    }

    public KrilTsutsaroth(final GodwarsBossMinion[] minions, final int id, final Location tile, final Direction direction, final int radius) {
        this(id, tile, direction, radius);
        setMinions(minions);
    }

    @Override
    protected int diaryFlag() {
        return 8;
    }

    @Override
    protected ForceTalk[] getQuotes() {
        return null;
    }

    @Override
    public GodType type() {
        return GodType.ZAMORAK;
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == NpcId.KRIL_TSUTSAROTH;
    }

    @Override public void handleIngoingHit(Hit hit) {
        super.handleIngoingHit(hit);
        if (!CombatSpell.INFERIOR_DEMONBANE.equals(hit.getWeapon()) && !CombatSpell.SUPERIOR_DEMONBANE.equals(hit.getWeapon())
                && !CombatSpell.DARK_DEMONBANE.equals(hit.getWeapon())) {
            onlyDemonBaneSpells = false;
        }
    }

    @Override
    protected void onFinish(Entity source) {
        super.onFinish(source);

        if (source instanceof final Player player) {
            player.getCombatAchievements().checkKcTask("k'ril tsutsaroth", 50, CAType.KRIL_TSUTSAROTH_ADEPT);
            player.getCombatAchievements().checkKcTask("k'ril tsutsaroth", 100, CAType.KRIL_TSUTSAROTH_VETERAN);
            if (allMinionsDead()) {
                player.getCombatAchievements().complete(CAType.DEMONIC_SHOWDOWN);
            }
            if (usedSpecial) {
                player.getCombatAchievements().complete(CAType.YARR_NO_MORE);
            }
            if (PlayerCombat.isDemonbaneWeapon(player.getEquipment().getId(EquipmentSlot.WEAPON))) {
                player.getCombatAchievements().complete(CAType.DEMONBANE_WEAPONRY_II);
            }
            if ((int) player.getAttributes().getOrDefault(GodwarsInstance.CA_TASK_INSTANCE_KC_ATT, 0) >= 20) {
                player.getCombatAchievements().complete(CAType.ASH_COLLECTOR);
            }
            if (player.getAttributes().containsKey(GodwarsInstance.CA_TASK_INSTANCE_ENTERED_ATT)) {
                if (!minionsHitSomeone) {
                    player.getCombatAchievements().complete(CAType.DEMON_WHISPERER);
                }
                if (!takenMeleeHit) {
                    player.getCombatAchievements().complete(CAType.DEMONIC_DEFENCE);
                }
                if (onlyDemonBaneSpells) {
                    player.getCombatAchievements().complete(CAType.THE_BANE_OF_DEMONS);
                }
            }
        }
    }

    @Override
    public int attack(final Entity target) {
        final int style = Utils.random(15);
        if (style == 15 && target instanceof Player && ((Player) target).getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE)) {
            setAnimation(magicAnimation);
            setForceTalk(forceTalk);
            final Player player = (Player) target;
            player.sendMessage(message);
            player.getPrayerManager().drainPrayerPoints(player.getPrayerManager().getPrayerPoints() / 2);
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, 49, MELEE, target), HitType.REGULAR));
            usedSpecial = true;
        } else if (style < 10) {
            setAnimation(meleeAnimation);
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, 47, AttackType.SLASH, target), HitType.MELEE));
            takenMeleeHit = true;
            target.getToxins().applyToxin(Toxins.ToxinType.POISON, 16, this);
        } else {
            World.sendSoundEffect(getMiddleLocation(), magicSound);
            setAnimation(magicAnimation);
            for (final Entity t : getPossibleTargets(EntityType.PLAYER)) {
                World.sendProjectile(this, t, projectile);
                int damage = getRandomMaxHit(this, 30, MAGIC, t);
                if (damage > 0) {
                    damage = Utils.random(10, 30);
                }
                //k'ril deals a minimum of 10 damage upon successful hit; for even distribution, we re-calc it.
                delayHit(this, projectile.getTime(this, t), t, new Hit(this, damage, HitType.MAGIC));
            }
        }
        return getCombatDefinitions().getAttackSpeed();
    }
}
