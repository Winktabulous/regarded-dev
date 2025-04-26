package com.zenyte.game.world.entity.player.action.combat.magic;

import com.zenyte.game.content.chambersofxeric.greatolm.FireWallNPC;
import com.zenyte.game.content.chambersofxeric.greatolm.GreatOlm;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.EntitySpell;
import com.zenyte.game.content.skills.magic.spells.arceuus.*;
import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Graphics;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.combatdefs.NPCCombatDefinitions;
import com.zenyte.game.world.entity.npc.combatdefs.StatType;
import com.zenyte.game.world.entity.npc.race.Demon;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.PlayerCombat;
import com.zenyte.game.world.entity.player.action.combat.magic.spelleffect.*;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.region.area.wilderness.WildernessArea;

import static com.zenyte.game.content.skills.magic.Spellbook.ANCIENT;
import static com.zenyte.game.content.skills.magic.Spellbook.NORMAL;
import static com.zenyte.game.world.entity.player.action.combat.CombatUtilities.ANCIENT_MULTI_CAST;

/**
 * @author Kris | 16. okt 2017 : 16:15.42
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public enum CombatSpell implements EntitySpell {

	WIND_STRIKE(NORMAL, 2, 5.5, new Animation(1162),
            new Graphics(90, 0, 92), new Graphics(92, -1, 124),
            new SoundEffect(220, 0, 0), new SoundEffect(221, 10, -1),
            new Projectile(91, 43, 31, 51, 23, -5, 64, 10)),

	WATER_STRIKE(NORMAL, 4, 7.5, new Animation(1162),
            new Graphics(93, 0, 92), new Graphics(95, -1, 124),
            new SoundEffect(211, 0, 0), new SoundEffect(212, 10, -1),
            new Projectile(94, 43, 31, 51, 23, -5, 64, 10)),

	EARTH_STRIKE(NORMAL, 6, 9.5, new Animation(1162),
            new Graphics(96, 0, 92), new Graphics(98, -1, 124),
            new SoundEffect(132, 0, 0), new SoundEffect(133, 10, -1),
            new Projectile(97, 43, 31, 51, 23, -5, 64, 10)),

	FIRE_STRIKE(NORMAL, 8, 11.5, new Animation(1162),
            new Graphics(99, 0, 92), new Graphics(101, -1, 124),
            new SoundEffect(160, 0, 0), new SoundEffect(161, 10, -1),
            new Projectile(100, 43, 31, 51, 23, -5, 64, 10)),

	WIND_BOLT(NORMAL, 9, 13.5, new Animation(1162),
            new Graphics(117, 0, 92), new Graphics(119, -1, 124),
            new SoundEffect(218, 0, 0), new SoundEffect(219, 10, -1),
            new Projectile(118, 43, 31, 51, 23, -5, 64, 10)),

	WATER_BOLT(NORMAL, 10, 16.5, new Animation(1162),
            new Graphics(120, 0, 92), new Graphics(122, -1, 124),
            new SoundEffect(209, 0, 0), new SoundEffect(210, 10, -1),
            new Projectile(121, 43, 31, 51, 23, -5, 64, 10)),

	EARTH_BOLT(NORMAL, 11, 19.5, new Animation(1162),
            new Graphics(123, 0, 92), new Graphics(125, -1, 124),
            new SoundEffect(130, 0, 0), new SoundEffect(131, 10, -1),
            new Projectile(124, 43, 31, 51, 23, -5, 64, 10)),

	FIRE_BOLT(NORMAL, 12, 22.5, new Animation(1162),
            new Graphics(126, 0, 92), new Graphics(128, -1, 124),
            new SoundEffect(157, 0, 0), new SoundEffect(158, 10, -1),
            new Projectile(127, 43, 31, 51, 23, -5, 64, 10)),


    WIND_BLAST(NORMAL, 13, 25.5, new Animation(1162),
            new Graphics(132, 0, 92), new Graphics(134, -1, 124),
            new SoundEffect(216, 0, 0), new SoundEffect(217, 10, -1),
            new Projectile(133, 43, 31, 51, 23, -5, 64, 10)),

	WATER_BLAST(NORMAL, 14, 28.5, new Animation(1162),
            new Graphics(135, 0, 92), new Graphics(137, -1, 124),
            new SoundEffect(207, 0, 0), new SoundEffect(208, 10, -1),
            new Projectile(136, 43, 31, 51, 23, -5, 64, 10)),

	EARTH_BLAST(NORMAL, 15, 31.5, new Animation(1162),
            new Graphics(138, 0, 92), new Graphics(140, -1, 124),
            new SoundEffect(132, 0, 0), new SoundEffect(133, 10, -1),
            new Projectile(139, 43, 31, 51, 23, -5, 64, 10)),

	FIRE_BLAST(NORMAL, 16, 34.5, new Animation(1162),
            new Graphics(129, 0, 92), new Graphics(131, -1, 124),
            new SoundEffect(155, 0, 0), new SoundEffect(156, 10, -1),
            new Projectile(130, 43, 31, 51, 23, -5, 64, 10)),


    WIND_WAVE(NORMAL, 17, 36, new Animation(1167),
            new Graphics(158, 0, 92), new Graphics(160, -1, 124),
            new SoundEffect(222, 0, 0), new SoundEffect(223, 10, -1),
            new Projectile(159, 43, 31, 51, 23, -5, 64, 10)),

	WATER_WAVE(NORMAL, 18, 37.5, new Animation(1167),
            new Graphics(161, 0, 92), new Graphics(163, 86, 124),
            new SoundEffect(213, 0, 0), new SoundEffect(214, 10, 86),
            new Projectile(162, 43, 31, 51, 23, -5, 64, 10)),

	EARTH_WAVE(NORMAL, 19, 40, new Animation(1167),
            new Graphics(164, 0, 92), new Graphics(166, -1, 124),
            new SoundEffect(134, 0, 0), new SoundEffect(135, 10, -1),
            new Projectile(165, 43, 31, 51, 23, -5, 64, 10)),

	FIRE_WAVE(NORMAL, 20, 42.5, new Animation(727),
            new Graphics(155, 0, 92), new Graphics(157, -1, 124),
            new SoundEffect(162, 0, 0), new SoundEffect(163, 10, -1),
            new Projectile(156, 43, 31, 51, 23, -5, 64, 10)),


    WIND_SURGE(NORMAL, 21, 44, new Animation(7855),
            new Graphics(1455, 0, 92), new Graphics(1457, -1, 124),
            new SoundEffect(4028, 0, 0), new SoundEffect(4027, 10, -1),
            new Projectile(1456, 43, 31, 51, 23, -5, 64, 10)),

	WATER_SURGE(NORMAL, 22, 46, new Animation(7855),
            new Graphics(1458, 0, 92), new Graphics(1460, -1, 124),
            new SoundEffect(4030, 0, 0), new SoundEffect(4029, 10, -1),
            new Projectile(1459, 43, 31, 51, 23, -5, 64, 10)),

	EARTH_SURGE(NORMAL, 23, 48, new Animation(7855),
            new Graphics(1461, 0, 92), new Graphics(1463, -1, 124),
            new SoundEffect(4025, 0, 0), new SoundEffect(4026, 10, -1),
            new Projectile(1462, 43, 31, 51, 23, -5, 64, 10)),

	FIRE_SURGE(NORMAL, 24, 61, new Animation(7855),
            new Graphics(1464, 0, 92), new Graphics(1466, -1, 124),
            new SoundEffect(4032, 0, 0), new SoundEffect(4031, 10, -1),
            new Projectile(1465, 43, 31, 51, 23, -5, 64, 10)),









    CONFUSE(NORMAL, 0, 13, new Animation(1163),
            new Graphics(102, 0, 92), new Graphics(104, -1, 200),
            new SoundEffect(119, 0, 0), new SoundEffect(121, 10, -1),
            new Projectile(103, 36, 31, 61, 23, -15, 64, 10),
            new DebuffEffect(SkillConstants.ATTACK, 5, 1)) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            final float percentage = 0.05F;
            if (target instanceof Player) {
                final Player p = (Player) target;
                final int skill = SkillConstants.ATTACK;
                final int base = p.getSkills().getLevelForXp(skill);
                final int current = p.getSkills().getLevel(skill);
                if (current <= (base - Math.max(1, ((int) Math.floor(base * percentage))))) {
                    player.sendMessage("Your foe's attack has already been lowered.");
                    return false;
                }
                return true;
            } else {
                final NPC npc = (NPC) target;
                final StatType skill = StatType.ATTACK;
                final NPCCombatDefinitions baseDefinitions = npc.getBaseCombatDefinitions();
                if (!npc.isAttackable() || baseDefinitions == null) {
                    player.sendMessage("You can't attack this npc.");
                    return false;
                }
                final int base = baseDefinitions.getStatDefinitions().get(skill);
                final int current = npc.getCombatDefinitions().getStatDefinitions().get(skill);
                if (current <= (base - Math.max(1, ((int) Math.floor(base * percentage))))) {
                    player.sendMessage("Your foe's attack has already been lowered.");
                    return false;
                }
            }
            return true;
        }
    },

    WEAKEN(NORMAL, 0, 21, new Animation(1164),
            new Graphics(105, -1, 92), new Graphics(107, -1, 200),
            new SoundEffect(3011, 0, 0), new SoundEffect(3010, 10, -1),
            new Projectile(106, 36, 31, 44, 23, 2, 64, 10),
            new DebuffEffect(SkillConstants.STRENGTH, 5, 1)) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            final float percentage = 0.05F;
            if (target instanceof Player) {
                final Player p = (Player) target;
                final int skill = SkillConstants.STRENGTH;
                final int base = p.getSkills().getLevelForXp(skill);
                final int current = p.getSkills().getLevel(skill);
                if (current <= (base - Math.max(1, ((int) Math.floor(base * percentage))))) {
                    player.sendMessage("Your foe's strength has already been lowered.");
                    return false;
                }
                return true;
            } else {
                final NPC npc = (NPC) target;
                final StatType skill = StatType.STRENGTH;
                final NPCCombatDefinitions baseDefinitions = npc.getBaseCombatDefinitions();
                if (!npc.isAttackable() || baseDefinitions == null) {
                    player.sendMessage("You can't attack this npc.");
                    return false;
                }
                final int base = baseDefinitions.getStatDefinitions().get(skill);
                final int current = npc.getCombatDefinitions().getStatDefinitions().get(skill);
                if (current <= Math.max(1, (base - ((int) Math.floor(base * percentage))))) {
                    player.sendMessage("Your foe's strength has already been lowered.");
                    return false;
                }
            }
            return true;
        }
    },

	CURSE(NORMAL, 0, 29, new Animation(1165),
            new Graphics(108, -1, 92), new Graphics(110, -1, 124),
            new SoundEffect(127, 0, 0), new SoundEffect(125, 10, 76),
            new Projectile(109, 43, 31, 51, 23, -5, 64, 10),
            new DebuffEffect(SkillConstants.DEFENCE, 5, 1)) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            final float percentage = 0.05F;
            if (target instanceof Player) {
                final Player p = (Player) target;
                final int skill = SkillConstants.DEFENCE;
                final int base = p.getSkills().getLevelForXp(skill);
                final int current = p.getSkills().getLevel(skill);
                if (current <= (base - Math.max(1, ((int) Math.floor(base * percentage))))) {
                    player.sendMessage("Your foe's defence has already been lowered.");
                    return false;
                }
                return true;
            } else {
                final NPC npc = (NPC) target;
                final StatType skill = StatType.DEFENCE;
                final NPCCombatDefinitions baseDefinitions = npc.getBaseCombatDefinitions();
                if (!npc.isAttackable() || baseDefinitions == null) {
                    player.sendMessage("You can't attack this npc.");
                    return false;
                }
                final int base = baseDefinitions.getStatDefinitions().get(skill);
                final int current = npc.getCombatDefinitions().getStatDefinitions().get(skill);
                if (current <= (base - Math.max(1, ((int) Math.floor(base * percentage))))) {
                    player.sendMessage("Your foe's defence has already been lowered.");
                    return false;
                }
            }
            return true;
        }
    },


    BIND(NORMAL, 0, 30, new Animation(1161),
            new Graphics(177, 0, 120), new Graphics(181, -1, 124),
            new SoundEffect(101, 0, 0), new SoundEffect(99, 10, 2),
            new Projectile(178, 45, 0, 75, 23, -29, 64, 10),
            new BindEffect(8, true)) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            if (target.isFrozen()) {
                player.sendMessage("Your target is already held by a magical force.");
                return false;
            }
            return true;
        }
    },


    SNARE(NORMAL, 2, 60, new Animation(1161),
            new Graphics(177, 0, 120), new Graphics(180, -1, 124),
            new SoundEffect(3003, 0, 0), new SoundEffect(3002, 10, -1),
            new Projectile(178, 45, 0, 75, 23, -29, 64, 10),
            new BindEffect(16, true)) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            if (target.isFrozen()) {
                player.sendMessage("Your target is already held by a magical force.");
                return false;
            }
            return true;
        }
    },


    ENTANGLE(NORMAL, 4, 89, new Animation(710),
            new Graphics(177, 0, 120), new Graphics(179, -1, 100),
            new SoundEffect(151, 0, 0), new SoundEffect(153, 10, -1),
            new Projectile(178, 45, 0, 75, 23, -29, 64, 10),
            new BindEffect(24, true)) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            if (target.isFrozen()) {
                player.sendMessage("Your target is already held by a magical force.");
                return false;
            }
            return true;
        }
    },

    VULNERABILITY(NORMAL, 0, 76, new Animation(718),
            new Graphics(167, 0, 92), new Graphics(169, -1, 124),
            new SoundEffect(3009, 0, 0), new SoundEffect(3008, 10, -1),
            new Projectile(168, 36, 31, 34, 23, 12, 64, 10),
            new DebuffEffect(SkillConstants.DEFENCE, 10, 1)) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            final float percentage = 0.1F;
            if (target instanceof Player) {
                final Player p = (Player) target;
                final int skill = SkillConstants.DEFENCE;
                final int base = p.getSkills().getLevelForXp(skill);
                final int current = p.getSkills().getLevel(skill);
                if (current <= (base - Math.max(1, ((int) Math.floor(base * percentage))))) {
                    player.sendMessage("Your foe's defence has already been lowered.");
                    return false;
                }
                return true;
            } else {
                final NPC npc = (NPC) target;
                final StatType skill = StatType.DEFENCE;
                final NPCCombatDefinitions baseDefinitions = npc.getBaseCombatDefinitions();
                if (!npc.isAttackable() || baseDefinitions == null) {
                    player.sendMessage("You can't attack this npc.");
                    return false;
                }
                final int base = baseDefinitions.getStatDefinitions().get(skill);
                final int current = npc.getCombatDefinitions().getStatDefinitions().get(skill);
                if (current <= (base - Math.max(1, ((int) Math.floor(base * percentage))))) {
                    player.sendMessage("Your foe's defence has already been lowered.");
                    return false;
                }
            }
            return true;
        }
    },

    ENFEEBLE(NORMAL, 0, 83, new Animation(728),
            new Graphics(170, 0, 92), new Graphics(172, -1, 124),
            new SoundEffect(148, 0, 0), new SoundEffect(150, 10, -1),
            new Projectile(171, 36, 31, 48, 23, -2, 64, 10),
            new DebuffEffect(SkillConstants.STRENGTH, 10, 1)) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            final float percentage = 0.1F;
            if (target instanceof Player) {
                final Player p = (Player) target;
                final int skill = SkillConstants.STRENGTH;
                final int base = p.getSkills().getLevelForXp(skill);
                final int current = p.getSkills().getLevel(skill);
                if (current <= (base - Math.max(1, (int) Math.floor(base * percentage)))) {
                    player.sendMessage("Your foe's strength has already been lowered.");
                    return false;
                }
                return true;
            } else {
                final NPC npc = (NPC) target;
                final StatType skill = StatType.STRENGTH;
                final NPCCombatDefinitions baseDefinitions = npc.getBaseCombatDefinitions();
                if (!npc.isAttackable() || baseDefinitions == null) {
                    player.sendMessage("You can't attack this npc.");
                    return false;
                }
                final int base = baseDefinitions.getStatDefinitions().get(skill);
                final int current = npc.getCombatDefinitions().getStatDefinitions().get(skill);
                if (current <= (base - Math.max(1, (int) Math.floor(base * percentage)))) {
                    player.sendMessage("Your foe's strength has already been lowered.");
                    return false;
                }
            }
            return true;
        }
    },

    STUN(NORMAL, 0, 90, new Animation(729),
            new Graphics(173, 0, 92), new Graphics(80, -1, 124),
            new SoundEffect(3004, 0, 0), new SoundEffect(3005, 10, -1),
            new Projectile(174, 36, 31, 52, 23, -6, 64, 10),
            new DebuffEffect(SkillConstants.ATTACK, 10, 1)) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            final float percentage = 0.1F;
            if (target instanceof Player) {
                final Player p = (Player) target;
                final int skill = SkillConstants.ATTACK;
                final int base = p.getSkills().getLevelForXp(skill);
                final int current = p.getSkills().getLevel(skill);
                if (current <= (base - Math.max(1, (int) Math.floor(base * percentage)))) {
                    player.sendMessage("Your foe's attack has already been lowered.");
                    return false;
                }
                return true;
            } else {
                final NPC npc = (NPC) target;
                final StatType skill = StatType.ATTACK;
                final NPCCombatDefinitions baseDefinitions = npc.getBaseCombatDefinitions();
                if (!npc.isAttackable() || baseDefinitions == null) {
                    player.sendMessage("You can't attack this npc.");
                    return false;
                }
                final int base = baseDefinitions.getStatDefinitions().get(skill);
                final int current = npc.getCombatDefinitions().getStatDefinitions().get(skill);
                if (current <= (base - Math.max(1, (int) Math.floor(base * percentage)))) {
                    player.sendMessage("Your foe's attack has already been lowered.");
                    return false;
                }
            }
            return true;
        }
    },

    IBAN_BLAST(NORMAL, 25, 30, new Animation(708),
            new Graphics(87, 0, 92), new Graphics(89, -1, 124),
            new SoundEffect(162, 0, 0), new SoundEffect(163, 10, -1),
            new Projectile(88, 36, 31, 60, 23, -14, 64, 10)),


    TELE_BLOCK(NORMAL, 0, 84, new Animation(1819),
            null, new Graphics(345),
            new SoundEffect(202, 0, 0), new SoundEffect(203, 10, -1),
            new Projectile(1299, 43, 25, 50, 15, 28, 64, 5),
            new TeleblockEffect()) {
		@Override
		public boolean canCast(final Player player, final Entity target) {
			if (target instanceof NPC) {
				player.sendMessage("You can only cast that on other players.");
				return false;
			}
			final Player p = (Player) target;
			if (p.getVariables().getTime(TickVariable.TELEBLOCK) > 0) {
			    player.sendMessage("Your target is already teleblocked.");
			    return false;
            }
			if (p.getVariables().getTime(TickVariable.TELEBLOCK_IMMUNITY) > 0) {
			    player.sendMessage("Your target is currently immune to teleblock.");
			    return false;
            }
			return true;
		}
	},
	CRUMBLE_UNDEAD(NORMAL, 15, 24.5, new Animation(724),
            new Graphics(145, 0, 92), new Graphics(147, -1, 124),
            new SoundEffect(122, 0, 0), new SoundEffect(124, 10, -1),
            new Projectile(146, 31, 31, 46, 23, 0, 64, 5)) {
		@Override
		public boolean canCast(final Player player, final Entity target) {
			if (!(target instanceof NPC) || !CombatUtilities.canCastCrumbleUndead((NPC) target)) {
				player.sendMessage("This spell only affects skeletons, zombies, ghosts and shades.");
				return false;
			}
			return true;
		}
	},

    MAGIC_DART(NORMAL, 0, 30, new Animation(1576),
            null, new Graphics(329, -1, 124),
            new SoundEffect(1718, 0, 0), new SoundEffect(174, 10, 0),
            new Projectile(328, 43, 31, 51, 23, 20, 64, 5)),

	SARADOMIN_STRIKE(NORMAL, 20, 35, new Animation(811),
            null, new Graphics(76, -1, 128),
            null, new SoundEffect(1659, 10, -1),
            new Projectile(-1, 43, 31, 51, 23, 20, 64, 5),
            new SaradominStrikeEffect()),

	CLAWS_OF_GUTHIX(NORMAL, 20, 35, new Animation(811),
            null, new Graphics(77, 0, 96),
            null, new SoundEffect(1653, 10, 0),
            new Projectile(-1, 43, 31, 51, 23, 20, 64, 5),
            new ClawsOfGuthixEffect()),

	FLAMES_OF_ZAMORAK(NORMAL, 20, 35, new Animation(811),
            null, new Graphics(78, 0, 0),
            null, new SoundEffect(1654, 10, 0),
            new Projectile(-1, 43, 31, 51, 23, 20, 64, 5),
            new FlamesOfZamorakEffect()),





    SMOKE_RUSH(ANCIENT, 13, 30, new Animation(1978),
            new Graphics(385, -1, 124),
            new SoundEffect(183, 0, 0), new SoundEffect(185, 10, -1),
            new Projectile(384, 43, 31, 51, 23, -5, 64, 10),
            new SmokeEffect(2)),

	SHADOW_RUSH(ANCIENT, 14, 31, new Animation(1978),
            new Graphics(379, -1, 0),
            new SoundEffect(178, 0, 0), new SoundEffect(179, 10, -1),
            new Projectile(378, 43, 0, 51, 23, -5, 64, 10),
            new ShadowEffect(10)),

	BLOOD_RUSH(ANCIENT, 15, 33, new Animation(1978),
            new Graphics(373, -1, 0),
            new SoundEffect(106, 0, 0), new SoundEffect(110, 10, -1),
            new Projectile(-1, 43, 0, 51, 23, -5, 64, 10),
            new BloodEffect()) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            if (target instanceof NPC npc && npc.getName().equalsIgnoreCase("<col=00ffff>Totem</col>")) {
                player.sendMessage("You can't charge the totem with blood spells.");
                return false;
            }
            return true;
        }
    },

	ICE_RUSH(ANCIENT, 16, 34, new Animation(1978),
            new Graphics(361, -1, 0),
            new SoundEffect(171, 0, 0), new SoundEffect(173, 10, -1),
            new Projectile(360, 43, 0, 51, 23, -5, 64, 10),
            new BindEffect(8)),


    SMOKE_BURST(ANCIENT, 17, 36, new Animation(1979),
            new Graphics(389, -1, 124),
            new SoundEffect(183, 0, 0), new SoundEffect(182, 10, -1),
            new Projectile(388, 43, 31, 51, 23, -5, 64, 10),
            new SmokeEffect(2)),

	SHADOW_BURST(ANCIENT, 18, 37, new Animation(1979),
            new Graphics(382, -1, 0),
            new SoundEffect(178, 0, 0), new SoundEffect(177, 10, -1),
            new Projectile(-1, 43, 31, 51, 23, -5, 64, 10),
            new ShadowEffect(10)),

	BLOOD_BURST(ANCIENT, 21, 39, new Animation(1979),
            new Graphics(376, -1, 0),
            new SoundEffect(106, 0, 0), new SoundEffect(105, 10, -1),
            new Projectile(-1, 43, 31, 51, 23, -5, 64, 10),
            new BloodEffect()) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            if (target instanceof NPC npc && npc.getName().equalsIgnoreCase("<col=00ffff>Totem</col>")) {
                player.sendMessage("You can't charge the totem with blood spells.");
                return false;
            }
            return true;
        }
    },

	ICE_BURST(ANCIENT, 22, 40, ANCIENT_MULTI_CAST,
            new Graphics(363, -1, 0),
            new SoundEffect(171, 0, 0), new SoundEffect(170, 10, -1),
            new Projectile(366, 43, 0, 51, 23, -5, 64, 10),
            new BindEffect(16)),


    SMOKE_BLITZ(ANCIENT, 23, 42, new Animation(1978),
            new Graphics(387, -1, 124),
            new SoundEffect(183, 0, 0), new SoundEffect(181, 10, -1),
            new Projectile(386, 43, 31, 51, 23, -5, 64, 10),
            new SmokeEffect(4)),

	SHADOW_BLITZ(ANCIENT, 24, 43, new Animation(1978),
            new Graphics(381, -1, 0),
            new SoundEffect(178, 0, 0), new SoundEffect(176, 10, -1),
            new Projectile(380, 43, 0, 51, 23, -5, 64, 10),
            new ShadowEffect(15)),

	BLOOD_BLITZ(ANCIENT, 25, 45, new Animation(1978),
            new Graphics(375, -1, 0),
            new SoundEffect(106, 0, 0), new SoundEffect(104, 10, -1),
            new Projectile(374, 43, 0, 51, 23, -5, 64, 10),
            new BloodEffect()) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            if (target instanceof NPC npc && npc.getName().equalsIgnoreCase("<col=00ffff>Totem</col>")) {
                player.sendMessage("You can't charge the totem with blood spells.");
                return false;
            }
            return true;
        }
    },

	ICE_BLITZ(ANCIENT, 26, 46, new Animation(1978),
            new Graphics(366, 0, 124), new Graphics(367, -1, 0),
            new SoundEffect(171, 0, 0), new SoundEffect(169, 10, -1),
            new Projectile(-1, 43, 0, 51, 23, -5, 64, 10),
            new BindEffect(24)),


    SMOKE_BARRAGE(ANCIENT, 27, 48, new Animation(1979),
            new Graphics(391, -1, 124),
            new SoundEffect(183, 0, 0), new SoundEffect(180, 10, -1),
            new Projectile(390, 43, 31, 51, 23, -5, 64, 10),
            new SmokeEffect(4)),

	SHADOW_BARRAGE(ANCIENT, 28, 49, new Animation(1979),
            new Graphics(383, -1, 0),
            new SoundEffect(178, 0, 0), new SoundEffect(175, 10, -1),
            new Projectile(-1, 43, 31, 51, 23, -5, 64, 10),
            new ShadowEffect(15)),

	BLOOD_BARRAGE(ANCIENT, 29, 51, new Animation(1979),
            new Graphics(377, -1, 0),
            new SoundEffect(106, 0, 0), new SoundEffect(102, 10, -1),
            new Projectile(-1, 43, 31, 51, 23, -5, 64, 10),
            new BloodEffect()) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            if (target instanceof NPC npc && npc.getName().equalsIgnoreCase("<col=00ffff>Totem</col>")) {
                player.sendMessage("You can't charge the totem with blood spells.");
                return false;
            }
            return true;
        }
    },

	ICE_BARRAGE(ANCIENT, 30, 52, new Animation(1979),
            new Graphics(369, -1, 0),
            new SoundEffect(171, 0, 0), new SoundEffect(168, 10, -1),
            new Projectile(368, 43, 0, 51, 23, -5, 64, 10),
            new BindEffect(32)),

    TUMEKENS_SHADOW(null, 23, 0, new Animation(9493),
            new Graphics(2125), new Graphics(2127, -1, 60),
            null, null,
            new Projectile(2126, 65, 15, 60, 23, 25, 64, 5)) {
    },

    TRIDENT_OF_THE_SEAS(null, 20, 0, new Animation(1167),
            new Graphics(1251, 0, 92), new Graphics(1253, -1, 60),
            new SoundEffect(178, 0, 0), new SoundEffect(1460, 10, -1),
            new Projectile(1252, 23, 15, 51, 23, 10, 64, 5)) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            if (target instanceof Player && WildernessArea.isWithinWilderness(target.getX(), target.getY())) {
                player.sendMessage("You cannot attack other players in Wilderness with the trident.");
                return false;
            }
            return true;
        }
    },

    POLYPORE_STAFF(null, 25, 0, new Animation(1167),
            new Graphics(1251, 0, 92), new Graphics(1253, -1, 60),
            new SoundEffect(178, 0, 0), new SoundEffect(1460, 10, -1),
            new Projectile(1252, 23, 15, 51, 23, 10, 64, 5)) {
    },
	TRIDENT_OF_THE_SWAMP(null, 23, 0, new Animation(1167),
            new Graphics(665, 0, 92), new Graphics(1042, -1, 60),
            new SoundEffect(178, 0, 0), new SoundEffect(1460, 10, -1),
            new Projectile(1040, 23, 15, 51, 23, 25, 64, 5)) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            if (target instanceof Player && WildernessArea.isWithinWilderness(target.getX(), target.getY())) {
                player.sendMessage("You cannot attack other players in Wilderness with a trident.");
                return false;
            }
            return true;
        }
    },
    SANGUINESTI_STAFF(null, 24, 0, new Animation(1167),
            new Graphics(1540, 0, 92), new Graphics(1541, -1, 60),
            new SoundEffect(178, 0, 0), new SoundEffect(1460, 10, -1),
            new Projectile(1539, 23, 15, 51, 23, 25, 64, 5), (player, target, damage) -> {
                if (Utils.randomBoolean(6)) {
                    player.heal(damage);
                    target.setGraphics(new Graphics(1542, 0, 96));
                }
            }) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            if (target instanceof Player && WildernessArea.isWithinWilderness(target.getX(), target.getY())) {
                player.sendMessage("You cannot attack other players in Wilderness with the staff of sanguinesti.");
                return false;
            }
            return true;
        }
    },
    DAWNBRINGER(null, 24, 0, new Animation(1167),
            new Graphics(1543, 0, 92), new Graphics(1545, -1, 60),
            new SoundEffect(178, 0, 0), new SoundEffect(1460, 10, -1),
            new Projectile(1544, 23, 15, 51, 23, 25, 64, 5)) {
        @Override
        public boolean canCast(final Player player, final Entity target) {
            if (!player.inArea("The Final Challenge")) {
                player.sendMessage("You cannot use the Dawnbringer outside of the Theatre of Blood.");
                return false;
            }
            return true;
        }
    },
    STARTER_STAFF(null,8, 0, new Animation(1162),
            new Graphics(1521, 0, 92), new Graphics(1523, -1, 124),
            new SoundEffect(160, 0, 0), new SoundEffect(161, 10, -1),
            new Projectile(1522, 43, 31, 51, 23, -5, 64, 10)),
    CRYSTAL_STAFF(null, 20, 0, new Animation(1167),
            new Graphics(1719, 0, 92), new Graphics(1721, -1, 60),
            new SoundEffect(178, 0, 0), new SoundEffect(1460, 10, -1),
            new Projectile(1720, 23, 15, 51, 23, 10, 64, 5)),
    CORRUPTED_STAFF(null, 20, 0, new Animation(1167),
            new Graphics(1722, 0, 92), new Graphics(1724, -1, 60),
            new SoundEffect(178, 0, 0), new SoundEffect(1460, 10, -1),
            new Projectile(1723, 23, 15, 51, 23, 10, 64, 5)),

    MARK_OF_DARKNESS(Spellbook.ARCEUUS, 0, 68, new Animation(8970),
            new Graphics(1852), new Graphics(1853, -1, 0),
            new SoundEffect(5046, 10, 0), new SoundEffect(5041, 10, -1),
            new Projectile(-1, 43, 31, 51, 23, -5, 64, 10),
            new MarkOfDarknessEffect()),

    GHOSTLY_GRASP(Spellbook.ARCEUUS, 12, 22.5, new Animation(8972),
            new Graphics(1856), new Graphics(1858, 30, 0),
            new SoundEffect(5042, 10, 0), new SoundEffect(5029, 10, 30),
            new Projectile(-1, 43, 31, 0, 23, 30, 64, 0),
            new GhostlyGraspEffect()),
    SKELETAL_GRASP(Spellbook.ARCEUUS, 17, 33.0, new Animation(8972),
            new Graphics(1859), new Graphics(1861, 30, 0),
            new SoundEffect(5026, 10, 0), new SoundEffect(5050, 10, 30),
            new Projectile(-1, 43, 31, 0, 23, 30, 64, 0),
            new SkeletalGraspEffect()),
    UNDEAD_GRASP(Spellbook.ARCEUUS, 24, 46.5, new Animation(8972),
            new Graphics(1862), new Graphics(1864, 30, 0),
            new SoundEffect(5030, 10, 0), new SoundEffect(5055, 10, 30),
            new Projectile(-1, 43, 31, 0, 23, 30, 64, 0),
            new UndeadGraspEffect()),
    DARK_LURE(Spellbook.ARCEUUS, 0, 60.0, new Animation(8974),
            new Graphics(1882), new Graphics(1884, -1, 124),
            new SoundEffect(5059), new SoundEffect(5031, 10, -1),
            new Projectile(1883, 31, 31, 46, 8, 0, 64, 10),
            new DarkLureEffect()) {
        @Override
        public boolean canCast(Player player, Entity target) {
            if (target instanceof Player) {
                player.sendMessage("This spell can only be cast on monsters.");
                return false;
            }
            if (DarkLureKt.getDarkLureCooldown(player)) {
                player.sendMessage("You can only cast Dark Lure every 10 seconds.");
                return false;
            }
            if (DarkLureKt.getUnderDarkLure(target)) {
                player.sendMessage("That creature is already under the effect of a Dark Lure.");
                return false;
            }
            return true;
        }
    },
    INFERIOR_DEMONBANE(Spellbook.ARCEUUS, 16, 27.0, new Animation(8977),
            new Graphics(1865), new Graphics(1866, 60, 0),
            new SoundEffect(5038, 0, 0), new SoundEffect(5036, 10, 60),
            new Projectile(-1, 43, 31, 0, 23, 60, 64, 0)) {
        @Override
        public boolean canCast(Player player, Entity target) {
            if (!(target instanceof NPC) || !Demon.isDemon(((NPC) target), true)) {
                player.sendMessage("This spell only affects demons.");
                return false;
            }
            return true;
        }
    },
    SUPERIOR_DEMONBANE(Spellbook.ARCEUUS, 23, 36.0, new Animation(8977),
            new Graphics(1867), new Graphics(1868, 60, 0),
            new SoundEffect(5027, 0, 0), new SoundEffect(5060, 10, 60),
            new Projectile(-1, 43, 31, 0, 23, 60, 64, 0)) {
        @Override
        public boolean canCast(Player player, Entity target) {
            if (!(target instanceof NPC) || !Demon.isDemon(((NPC) target), true)) {
                player.sendMessage("This spell only affects demons.");
                return false;
            }
            return true;
        }
    },
    DARK_DEMONBANE(Spellbook.ARCEUUS, 30, 43.5, new Animation(8977),
            new Graphics(1869), new Graphics(1870, 60, 0),
            new SoundEffect(5053, 0, 0), new SoundEffect(5035, 10, 60),
            new Projectile(-1, 43, 31, 0, 23, 60, 64, 0)) {
        @Override
        public boolean canCast(Player player, Entity target) {
            if (!(target instanceof NPC) || !Demon.isDemon(((NPC) target), true)) {
                player.sendMessage("This spell only affects demons.");
                return false;
            }
            return true;
        }
    },

    THAMMARONS_SCEPTRE(null, 25, 0, new Animation(1167),
            null, new Graphics(292, -1, 0),
            new SoundEffect(5046, 10, 0), new SoundEffect(5041, 10, -1),
            new Projectile(2340, 31, 31, 51, 23, -5, 64, 10)
    ),

    ACCURSED_SCEPTRE(null, 27, 0, new Animation(1167),
            null, new Graphics(292, -1, 0),
            new SoundEffect(5046, 10, 0), new SoundEffect(5041, 10, -1),
            new Projectile(2337, 31, 31, 51, 23, -5, 64, 10)
    ),

            //::multigfx 290, its 292

    ;
	
	private final int maxHit;
	private final Spellbook spellbook;
	private final double experience;
	private final Animation animation;
	private final Graphics castGfx, hitGfx;
	private final Projectile projectile;
	private final SpellEffect effect;
	private final Item[] staves;
	private final SoundEffect castSound;
	private final SoundEffect hitSound;

    static {
		for (final CombatSpell spell : values()) {
			Magic.SPELLS_BY_NAME.put(spell.getSpellName(), spell);
		}
	}
	
	private static Projectile spellProjectile(final int id) {
	    return new Projectile(id, 43, 31, 51, 23, -5, 64, 10);
    }

    CombatSpell(final Spellbook spellbook, final int maxHit, final double experience, final Animation animation, final Graphics castGfx, final Graphics hitGfx, SoundEffect castSound, SoundEffect hitSound, final Projectile projectile, final SpellEffect effect) {
		this(spellbook, maxHit, experience, animation, castGfx, hitGfx, castSound, hitSound, projectile, effect, null);
	}
	
	CombatSpell(final Spellbook spellbook, final int maxHit, final double experience, final Animation animation, final Graphics hitGfx, SoundEffect castSound, SoundEffect hitSound, final Projectile projectile, final SpellEffect effect) {
		this(spellbook, maxHit, experience, animation, null, hitGfx, castSound, hitSound, projectile, effect, null);
	}
	
	CombatSpell(final Spellbook spellbook, final int maxHit, final double experience, final Animation animation, final Graphics castGfx, final Graphics hitGfx, SoundEffect castSound, SoundEffect hitSound, final Projectile projectile) {
		this(spellbook, maxHit, experience, animation, castGfx, hitGfx, castSound, hitSound, projectile, null, null);
	}
	
	CombatSpell(final Spellbook spellbook, final int maxHit, final double experience, final Animation animation, final Graphics castGfx, final Graphics hitGfx, SoundEffect castSound, SoundEffect hitSound, final Projectile projectile, final SpellEffect effect, final Item[] staves) {
		this.spellbook = spellbook;
		this.maxHit = maxHit;
		this.experience = experience;
		this.animation = animation;
		this.castGfx = castGfx;
		this.hitGfx = hitGfx;
		this.projectile = projectile;
		this.effect = effect;
		this.staves = staves;
		this.castSound = castSound;
		this.hitSound = hitSound;
	}

	@Override
	public int getDelay() {
		return 1000;
	}
	
	@Override
	public void execute(final Player player, final Entity target) {
		if (!canCast(player)) {
			player.sendMessage("You cannot cast that spell on this spellbook.");
			return;
		}
        if (!canUse(player)) {
            return;
        }
		if (target instanceof FireWallNPC) {
            GreatOlm.douse(player, (FireWallNPC) target, this);
            return;
        }
		PlayerCombat.attackEntity(player, target, this);
	}
	
	@Override
	@Deprecated
	public boolean spellEffect(final Player player, final Player target) {
		return false;
	}
	
	public boolean canCast(final Player player, final Entity target) {
		return true;
	}
	
	@Override
	public String getSpellName() {
		return toString().toLowerCase().replaceAll("_", " ");
	}
	
	public int getMaxHit() {
	    return maxHit;
	}
	
	public Spellbook getSpellbook() {
	    return spellbook;
	}
	
	public double getExperience() {
	    return experience;
	}
	
	public Animation getAnimation() {
	    return animation;
	}
	
	public Graphics getCastGfx() {
	    return castGfx;
	}
	
	public Graphics getHitGfx() {
	    return hitGfx;
	}
	
	public Projectile getProjectile() {
	    return projectile;
	}
	
	public SpellEffect getEffect() {
	    return effect;
	}

    public boolean hasBindEffect() {
        return effect instanceof BindEffect;
    }
	
	public Item[] getStaves() {
	    return staves;
	}
	
	public SoundEffect getCastSound() {
	    return castSound;
	}
	
	public SoundEffect getHitSound() {
	    return hitSound;
	}

}
