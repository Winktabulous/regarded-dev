package com.zenyte.game.content.godwars.npcs;

import com.zenyte.game.content.achievementdiary.diaries.FremennikDiary;
import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary;
import com.zenyte.game.content.godwars.GodType;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author Kris | 11. veebr 2018 : 2:18.42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SpiritualMage extends SpawnableKillcountNPC implements CombatScript {

    private static final Graphics earthCast = new Graphics(164, 20, 60);
    private static final Graphics earthImpact = new Graphics(166, 0, 92);
    private static final Projectile earthProjectile = new Projectile(165, 20, 25, 57, 15, 18, 64, 5);

    private static final Graphics saradominImpactGraphics = new Graphics(76, 0, 96);
    private static final Graphics zamorakImpactGraphics = new Graphics(78);

    private static final Projectile thrownAxeProjectile = new Projectile(1193, 85, 30, 30, 15, 8, 0, 5);

    private static final SoundEffect spiritualMageSound = new SoundEffect(1655, 10, 0);
    private static final SoundEffect priestMageSound = new SoundEffect(1659, 10, 0);

    public SpiritualMage(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public GodType type() {
        switch (id) {
            case NpcId.SPIRITUAL_MAGE_2244 -> {
                return GodType.BANDOS;
            }
            case NpcId.SPIRITUAL_MAGE, NpcId.SARADOMIN_PRIEST -> {
                return GodType.SARADOMIN;
            }
            case NpcId.SPIRITUAL_MAGE_3161 -> {
                return GodType.ZAMORAK;
            }
            case NpcId.SPIRITUAL_MAGE_3168 -> {
                return GodType.ARMADYL;
            }
            case NpcId.SPIRITUAL_MAGE_11292 -> {
                return GodType.ANCIENT;
            }
        }
        return null;
    }

    @Override
    public int attack(final Entity target) {
        setAnimation(getCombatDefinitions().getAttackAnim());
        switch (id) {
            // Bandos
            case NpcId.SPIRITUAL_MAGE_2244 -> {
                setGraphics(earthCast);
                sendHit(earthProjectile, target, hit -> target.setGraphics(earthImpact));
            }
            // Saradomin
            case NpcId.SPIRITUAL_MAGE, NpcId.SARADOMIN_PRIEST -> {
                World.sendSoundEffect(getMiddleLocation(), id == 2209 ? priestMageSound : spiritualMageSound);
                sendHit(1, target, hit -> target.setGraphics(saradominImpactGraphics));
            }
            // Zamorak
            case NpcId.SPIRITUAL_MAGE_3161 -> {
                World.sendSoundEffect(getMiddleLocation(), spiritualMageSound);
                sendHit(1, target, hit -> target.setGraphics(zamorakImpactGraphics));
            }
            // Armadyl
            case NpcId.SPIRITUAL_MAGE_3168 ->
                    sendHit(thrownAxeProjectile, target, null);
            // Zaros
            case NpcId.SPIRITUAL_MAGE_11292 -> {
                switch (Utils.random(3)) {
                    case 0 -> useSpell(CombatSpell.SMOKE_RUSH, target);
                    case 1 -> useSpell(CombatSpell.SHADOW_RUSH, target);
                    case 2 -> useSpell(CombatSpell.BLOOD_RUSH, target);
                    case 3 -> useSpell(CombatSpell.ICE_RUSH, target);
                }
            }
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    private void sendHit(Projectile projectile, Entity target, Consumer<Hit> onLand) {
        sendHit(World.sendProjectile(this, target, projectile), target, onLand);
    }

    private void sendHit(int delay, Entity target, Consumer<Hit> onLand) {
        final Hit hit = new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC);
        if (onLand != null)
            hit.onLand(onLand);
        delayHit(this, delay, target, hit);
    }

    @Override
    public boolean canBeMulticannoned(@NotNull Player player) {
        return false;
    }

    @Override
    public void onDeath(Entity source) {
        super.onDeath(source);
        if (source instanceof final Player player) {
            if (id != NpcId.SARADOMIN_PRIEST) {
                player.getAchievementDiaries().update(FremennikDiary.SLAY_A_SPIRITUAL_MAGE);
                player.getAchievementDiaries().update(WildernessDiary.SLAY_A_SPIRITRUAL_MAGE);
            }
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.SPIRITUAL_MAGE_2244
                || id == NpcId.SPIRITUAL_MAGE
                || id == NpcId.SARADOMIN_PRIEST
                || id == NpcId.SPIRITUAL_MAGE_3161
                || id == NpcId.SPIRITUAL_MAGE_3168
                || id == NpcId.SPIRITUAL_MAGE_11292;
    }
}
