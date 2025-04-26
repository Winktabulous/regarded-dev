package com.zenyte.game.world.entity.player.action.combat;

import com.near_reality.game.content.combat.CombatUtility;
import com.near_reality.game.content.custom.SlayerHelmetEffects;
import com.near_reality.game.world.entity.player.action.combat.ISpecialAttack;
import com.near_reality.game.world.entity.player.action.combat.effect.DeathCapeEffect;
import com.zenyte.game.content.boons.impl.*;
import com.zenyte.game.content.boss.grotesqueguardians.boss.Dawn;
import com.zenyte.game.content.skills.prayer.Prayer;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.degradableitems.DegradeType;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.CollisionUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Entity.EntityType;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.Toxins.ToxinType;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.pathfinding.events.player.CombatEntityEvent;
import com.zenyte.game.world.entity.pathfinding.strategy.PredictedEntityStrategy;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.game.world.entity.player.variables.TickVariable;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.game.world.region.area.plugins.EntityAttackPlugin;
import com.zenyte.game.world.region.area.plugins.PlayerCombatPlugin;
import mgi.types.config.items.ItemDefinitions;

import static com.zenyte.game.world.entity.player.action.combat.AttackStyle.AttackExperienceType.*;

/**
 * @author Kris | 5. jaan 2018 : 2:03.26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>
 */
public class MeleeCombat extends PlayerCombat {
    private int extraSpace;

    public MeleeCombat(final Entity target) {
        super(target);
    }

    @Override
    int getAttackDistance() {
        return 0;
    }

    @Override
    int fireProjectile() {
        return 0;
    }

    @Override
    public Hit getHit(final Player player, final Entity target, final double accuracyModifier, final double passiveModifier, double activeModifier, final boolean ignorePrayers) {
        return new Hit(player, getRandomHit(player, target, getMaxHit(player, passiveModifier, activeModifier, ignorePrayers), accuracyModifier), HitType.MELEE);
    }

    @Override
    public int getMaxHit(final Player player, final double passiveModifier, double activeModifier, final boolean ignorePrayers) {
        final AttackStyle.AttackExperienceType attackExperienceType = player.getCombatDefinitions().getAttackExperienceType();
        final AttackType attackType = player.getCombatDefinitions().isUsingSpecial() ? getSpecialType() : player.getCombatDefinitions().getAttackType();
        float boost = CombatUtilities.hasFullMeleeVoid(player, true) ? 1.12F : CombatUtilities.hasFullMeleeVoid(player, false) ? 1.1F : 1;
        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
        if (CombatUtilities.lanceEquipped(weaponId) && CombatUtilities.isDraconic(target))
            boost += 0.3F;
        if (CombatUtilities.applyForinthrySurge(player, target))
            boost += 0.15F;
        if (CombatUtilities.applyPvmArenaBoost(player, target))
            boost += 0.05F;
        boost += determineBountyHunterDmgBoost(player, target);
        if (attackType == AttackType.CRUSH)
            boost += CombatUtilities.getInquisitorSetBoost(player);
        final double a = (Math.floor(player.getSkills().getLevel(SkillConstants.STRENGTH) * player.getPrayerManager().getSkillBoost(SkillConstants.STRENGTH)) + (attackExperienceType == STRENGTH_XP ? 3 : attackExperienceType == SHARED_XP ? 1 : 0) + 8) * (boost);
        final float b = (float) player.getBonuses().getBonus(10);
        double result = Math.floor(0.5F + a * (b + 64.0F) / 640.0F);
        final int amuletId = player.getEquipment().getId(EquipmentSlot.AMULET);
        final String amuletName = ItemDefinitions.nameOf(amuletId).toLowerCase();

        if (amuletId == ItemId.AMULET_OF_AVARICE && CombatUtilities.isRevenant(target)) {
            result *= 1.2F;
        } else if ((amuletName.startsWith("salve amulet")) && (CombatUtilities.SALVE_AFFECTED_NPCS.contains(name) || CombatUtilities.isUndeadCombatDummy(target))) {
            result *= (amuletId == 4081 || amuletId == 12017) ? (7.0F / 6.0F) : 1.2F;
        }

        boolean hasTask = (player.getSlayer().isCurrentAssignment(target) || player.hasBoon(SlayersSovereignty.class)) || CombatUtilities.isCombatDummy(target);
        result *= determineSlayerHelmetDamageBoost(hasTask, HitType.MELEE, player, target);
        result = Math.floor(result);

        result = Math.floor(result * passiveModifier);

        final boolean wieldingObsidianWeapon = obsidianWeaponry.contains(weaponId);
        if (wieldingObsidianWeapon && CombatUtilities.hasFullObisidian(player)) {
            result *= 1.1F;
        }
        result = Math.floor(result);
        if (!ignorePrayers) {
            if (target instanceof Player) {
                if (((Player) target).getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE)) {
                    result *= target.getMeleePrayerMultiplier();
                    result = Math.floor(result);
                }
            }
        }
        //Darklight or arclight
        if (isDemonbaneWeapon(weaponId) && CombatUtilities.isDemon(target)) {
            if(target instanceof NPC npc && npc.getId() == NpcId.DUKE_SUCELLUS_12191) {
                result *= 1.5F;
            } else result *= weaponId == ItemId.ARCLIGHT ? 1.7F : 1.6F;
        }
        //Berserker necklace
        if ((amuletId == ItemId.BERSERKER_NECKLACE || amuletId == ItemId.BERSERKER_NECKLACE_OR) && wieldingObsidianWeapon) {
            result *= 1.2F;
        } else
        //Dharok's set effect
        if (CombatUtilities.hasFullBarrowsSet(player, "Dharok's")) {
            result *= CombatUtilities.getDharokModifier(player);
        } else
        //Gadderhammer
        if (weaponId == 7668 && CombatUtilities.isShade(target)) {
            result *= Utils.random(99) < 5 ? 2.0F : 1.25F;
        }
        result *= activeModifier;
        result = Math.floor(result);

        if(target instanceof NPC npc && MinionsMight.shouldBoostCombat(player, npc))
            result *= 1.1F;

        //Castle wars bracelet effect
        if (player.getTemporaryAttributes().containsKey("castle wars bracelet effect") && player.inArea("Castle Wars")) {
            if (target instanceof Player) {
                final int targetWeapon = ((Player) target).getEquipment().getId(EquipmentSlot.WEAPON);
                if (targetWeapon == 4037 || targetWeapon == 4039) {
                    result = Math.floor(result * 1.2F);
                }
            }
        }
        if (target instanceof final Player tp) {
            if (tp.getVariables().getTime(TickVariable.POWER_OF_DEATH) > 0) {
                result *= 0.5F;
            }
        }
        if(target instanceof NPC) {
            if(player.hasBoon(VigourOfInquisition.class) && VigourOfInquisition.applies(player) && player.getWeapon() != null && attackType == AttackType.CRUSH)
                result *= 1.2;
        }
        result = SlayerHelmetEffects.INSTANCE.rollBonusDamage(player, target, result);
        return (int) Math.floor(result);
    }

    @Override
    public final int getRandomHit(final Player player, final Entity target, final int maxhit, final double modifier) {
        return getRandomHit(player, target, maxhit, modifier, player.getCombatDefinitions().getAttackType());
    }

    private AttackType getSpecialType() {
        final ISpecialAttack special = SpecialAttack.SPECIAL_ATTACKS.get(player.getEquipment().getId(EquipmentSlot.WEAPON.getSlot()));
        if (special == null) {
            return player.getCombatDefinitions().getAttackType();
        }
        return special.getAttackType();
    }

    @Override
    public int getRandomHit(final Player player, final Entity target, final int maxhit, final double modifier, final AttackType attackType) {

        final Integer deathCapeDamageOverride = DeathCapeEffect.apply(player, target);
        if (deathCapeDamageOverride != null)
            return deathCapeDamageOverride;

        if (CombatUtilities.isAlwaysTakeMaxHit(target, HitType.MELEE) || CombatUtilities.isWardenCore(target)) {
            return maxhit;
        }
        final int accuracy = getAccuracy(player, target, modifier);
        final int targetRoll = getTargetDefenceRoll(player, target, attackType);
        sendDebug(accuracy, targetRoll, maxhit);
        final int accRoll = accuracy > 0 ? Utils.random(accuracy) : 0;
        final int defRoll = targetRoll > 0 ? Utils.random(targetRoll) : 0;
        if (accRoll <= defRoll) {
            return 0;
        }
        int playerMinimum = calculateMinimumHit(player, maxhit);
        return Utils.random(playerMinimum, maxhit);
    }


    @Override
    public int getAccuracy(final Player player, final Entity target, final double resultModifier) {
        final AttackStyle.AttackExperienceType type = player.getCombatDefinitions().getAttackExperienceType();
        final AttackType attackType = player.getCombatDefinitions().isUsingSpecial() ? getSpecialType() : player.getCombatDefinitions().getAttackType();
        float boost = CombatUtilities.hasFullMeleeVoid(player, true) ? 1.125F : CombatUtilities.hasFullMeleeVoid(player, false) ? 1.1F : 1.0F;
        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
        if (CombatUtilities.lanceEquipped(weaponId) && CombatUtilities.isDraconic(target))
            boost += 0.3F;
        if (CombatUtilities.dragonSlayerGlovesEquipped(player) && CombatUtilities.isDraconic(target))
            boost += 0.15F;
        if (attackType == AttackType.CRUSH)
            boost += CombatUtilities.getInquisitorSetBoost(player);
        if (CombatUtilities.applyForinthrySurge(player, target))
            boost += 0.15F;
        if (CombatUtilities.applyPvmArenaBoost(player, target))
            boost += 0.05F;
        boost += determineBountyHunterAccBoost(player, target);
        final double a = Math.floor(Math.floor(player.getSkills().getLevel(SkillConstants.ATTACK) * player.getPrayerManager().getSkillBoost(SkillConstants.ATTACK)) + (type == ATTACK_XP ? 3 : type == SHARED_XP ? 1 : 0) + 8.0F) * (boost);
        final int b = player.getBonuses().getBonus(attackType.ordinal());
        double result = a * (b + 64.0F);
        final int amuletId = player.getEquipment().getId(EquipmentSlot.AMULET);
        if (amuletId == ItemId.AMULET_OF_AVARICE && CombatUtilities.isRevenant(target)) {
            result *= 1.2F;
        } else if ((amuletId == 4081 || amuletId == 12017 || amuletId == 10588 || amuletId == 12018) && CombatUtilities.SALVE_AFFECTED_NPCS.contains(name)) {
            result *= (amuletId == 4081 || amuletId == 12017) ? (7.0F / 6.0F) : 1.2F;
        }

        boolean hasTask = (player.getSlayer().isCurrentAssignment(target) || player.hasBoon(SlayersSovereignty.class)) || CombatUtilities.isUndeadCombatDummy(target);
        result *= determineSlayerHelmetAccuracyBoost(hasTask, HitType.MELEE, player, target);
        result = Math.floor(result);

        if(target instanceof NPC npc && MinionsMight.shouldBoostCombat(player, npc))
            result *= 1.1F;

        result *= resultModifier;
        if (isDemonbaneWeapon(weaponId) && CombatUtilities.isDemon(target)) {
            if(target instanceof NPC npc && npc.getId() == NpcId.DUKE_SUCELLUS_12191) {
                result *= 1.5F;
            } else result *= weaponId == ItemId.ARCLIGHT ? 1.7F : 1.6F;
        }
        if (obsidianWeaponry.contains(weaponId) && CombatUtilities.hasFullObisidian(player)) {
            result *= 1.1F;
        }
        if(target instanceof NPC) {
            if(player.hasBoon(JabbasRightHand.class) && CombatUtility.hasBountyHunterWeapon(player))
                result *= 1.15;
            if (player.hasBoon(BrawnOfJustice.class) && BrawnOfJustice.applies(player) && player.getBooleanTemporaryAttribute("TOB_inside"))
                result *= 1.2;
            if(player.hasBoon(VigourOfInquisition.class) && VigourOfInquisition.applies(player) && player.getWeapon() != null && attackType == AttackType.CRUSH)
                result *= 1.2;
        }
        return (int) result;
    }



    @Override
    public boolean process() {
        return initiateCombat(player);
    }

    @Override
    public int processWithDelay() {
        return 0;
    }

    @Override
    public int processAfterMovement() {
        if (!isWithinAttackDistance()) {
            return 0;
        }
        if (!canAttack()) {
            return -1;
        }
        final RegionArea area = player.getArea();
        if (area instanceof PlayerCombatPlugin) {
            ((PlayerCombatPlugin) area).onAttack(player, target, "Melee", null, false);
        }
        addAttackedByDelay(player, target);
        final int delay = special();
        if (delay != -2) {
            return delay == SpecialAttackScript.WEAPON_SPEED ? getSpeed() : delay;
        }
        sendSoundEffect();
        final Hit hit = getHit(player, target, 1, 1, 1, false);
        extra(hit);
        addPoisonTask(hit.getDamage(), 0);
        delayHit(0, hit);
        if (hit.getDamage() > 0 && player.getEquipment().getId(EquipmentSlot.WEAPON) == ItemId.ARCLIGHT) {
            player.getChargesManager().removeCharges(player.getWeapon(), 1, player.getEquipment().getContainer(), EquipmentSlot.WEAPON.getSlot());
        }
        animate();
        player.getChargesManager().removeCharges(DegradeType.OUTGOING_HIT);
        resetFlag();
        checkIfShouldTerminate(HitType.MELEE);
        return getSpeed();
    }

    protected void extra(final Hit hit) {
        bloodFury(HitType.MELEE, hit);
    }

    protected int special() {
        if (!player.getCombatDefinitions().isUsingSpecial()) return -2;
        return useSpecial(player, SpecialType.MELEE);
    }

    @Override
    public boolean start() {
        extraSpace = isExtendedMeleeDistance(player);
        player.setCombatEvent(new CombatEntityEvent(player, new PredictedEntityStrategy(target)));
        player.setLastTarget(target);
        if (target.getEntityType() == EntityType.NPC) {
            final NPC npc = (NPC) target;
            final int id = npc.getId();
            if (id >= 3162 && id <= 3183 || id == 7037 || id == 6587 || npc instanceof Dawn) {
                player.sendMessage("You cannot use melee against this creature.");
                return false;
            }
        }
        if (player.isFrozen()) {
            player.sendMessage("A magical force stops you from moving.");
        }
        player.setFaceEntity(target);
        if (initiateCombat(player)) {
            return true;
        }
        player.setFaceEntity(null);
        return false;
    }

    protected void addPoisonTask(final int damage, final int delay) {
        if (damage <= 0) return;

        final Item weapon = player.getEquipment().getItem(EquipmentSlot.WEAPON);
        if (weapon == null) return;

        switch (weapon.getId()) {
            case ItemId.ABYSSAL_TENTACLE, 26484, ItemId.LIME_WHIP -> {
                if (Utils.random(3) == 0)
                    WorldTasksManager.scheduleOrExecute(() -> target.getToxins().applyToxin(ToxinType.POISON, 4, player), delay);
                return;
            }
        }

        final String name = weapon.getName();
        if (!name.contains("(p"))
            return;

        WorldTasksManager.scheduleOrExecute(() -> target.getToxins().applyToxin(ToxinType.POISON, name.contains("p++") ? 6 : name.contains("p+") ? 5 : 4, player), delay);
    }

    protected void animate() {
        final int id = player.getEquipment().getAttackAnimation(player.getCombatDefinitions().getStyle());
        final Animation animation = new Animation(getAttackAnimation(target instanceof Player, id == 393 ? (player.getShield() == null ? 414 : id) : id));
        player.setAnimation(animation);
    }

    protected boolean canAttack() {
        if (!attackable()) return false;
        if (!target.canAttack(player)) {
            return false;
        }
        final RegionArea area = player.getArea();
        if ((area instanceof EntityAttackPlugin && !((EntityAttackPlugin) area).attack(player, target, this))) {
            return false;
        }
        return (!(area instanceof PlayerCombatPlugin) || ((PlayerCombatPlugin) area).processCombat(player, target, "Melee")) && player.getControllerManager().processPlayerCombat(target, "Melee");
    }

    protected int getSpeed() {
        int speed = 3;
        final Item weapon = player.getWeapon();
        if (weapon != null) {
            speed = 9 - weapon.getDefinitions().getAttackSpeed();
        }

        return adjustAttackSpeed(player, speed);
    }

    protected boolean initiateCombat(final Player player) {
        if (player.isDead() || player.isFinished() || player.isLocked() || player.isStunned() || player.isFullMovementLocked()) {
            return false;
        }
        if (target.isFinished() || target.isCantInteract() || target.isDead()) {
            return false;
        }
        final int distanceX = player.getX() - target.getX();
        final int distanceY = player.getY() - target.getY();
        final int size = target.getSize();
        final int viewDistance = player.getViewDistance();
        if (player.getPlane() != target.getPlane() || distanceX > size + viewDistance || distanceX < -1 - viewDistance || distanceY > size + viewDistance || distanceY < -1 - viewDistance) {
            return false;
        }
        if (target.getEntityType() == EntityType.PLAYER) {
            if (!player.isCanPvp() || !((Player) target).isCanPvp()) {
                player.sendMessage("You can't attack someone in a safe zone.");
                return false;
            }
        }
        if (player.isFrozen() || player.isMovementLocked(false)) {
            return true;
        }
        if (!canInitiate()) {
            return false;
        }
        if (!target.hasWalkSteps() && CollisionUtil.collides(player.getX(), player.getY(), player.getSize(), target.getX(), target.getY(), target.getSize())) {
            player.getCombatEvent().process();
            return true;
        }
        if (handleDragonfireShields(player, false)) {
            if (!canAttack()) {
                return false;
            }
            handleDragonfireShields(player, true);
            player.getActionManager().addActionDelay(4);
            return true;
        }
        player.resetWalkSteps();
        final Location nextLocation = target.getLocation();
        if (player.isProjectileClipped(target, extraSpace <= 0 && !(player.getDuel() != null && player.getDuel().inDuel())) || !(withinRange(target, extraSpace, target.getSize())) || target.hasWalkSteps() && (target instanceof Player || !CollisionUtil.collides(player.getX(), player.getY(), player.getSize(), nextLocation.getX(), nextLocation.getY(), target.getSize()))) {
            appendWalksteps();
        }
        if (!player.hasWalkSteps() && !isWithinAttackDistance()) {
            player.sendMessage("I can't reach that!");
            return false;
        }
        return true;
    }

    protected boolean canInitiate() {
        return true;
    }

    protected boolean isWithinAttackDistance() {
        if(target instanceof NPC npc && hasManualDistanceDefined(npc))
            return checkManualDistance(npc);
        if (target.checkProjectileClip(player, true) && isProjectileClipped(true, extraSpace <= 0 && !(player.getDuel() != null && player.getDuel().inDuel()))) {
            return false;
        }
        final Location nextTile = target.getNextLocation();
        final Location tile = nextTile != null ? nextTile : target.getLocation();
        final int distanceX = player.getX() - tile.getX();
        final int distanceY = player.getY() - tile.getY();
        final int size = target.getSize();
        int maxDistance = extraSpace;
        final Location nextLocation = target.getLocation();
        if ((player.isFrozen() || player.isStunned()) && (CollisionUtil.collides(player.getX(), player.getY(), player.getSize(), nextLocation.getX(), nextLocation.getY(), target.getSize()) || !withinRange(target, maxDistance, target.getSize()))) {
            return false;
        }
        return distanceX <= size + maxDistance && distanceX >= -1 - maxDistance && distanceY <= size + maxDistance && distanceY >= -1 - maxDistance;
    }

    private boolean checkManualDistance(NPC target) {
        final Location nextTile = target.getNextLocation();
        final Location tile = nextTile != null ? nextTile : target.getLocation();
        final int distanceX = player.getX() - tile.getX();
        final int distanceY = player.getY() - tile.getY();
        final int size = target.getSize();
        int maxDistance = 3;
        if ((player.isFrozen() || player.isStunned()) && (!withinRange(target, maxDistance, target.getSize()))) {
            return false;
        }
        return distanceX <= size + maxDistance && distanceX >= -1 - maxDistance && distanceY <= size + maxDistance && distanceY >= -1 - maxDistance;
    }

    private boolean hasManualDistanceDefined(NPC target) {
        if(target.getId() == 12191 || target.getId() == 12195) //Duke
            return true;
        return false;
    }

    protected void resetFlag() {
        if (!minimapFlag) {
            return;
        }
        player.getPacketDispatcher().resetMapFlag();
        minimapFlag = false;
    }

    protected void sendSoundEffect() {
        final int weaponId = player.getEquipment().getId(EquipmentSlot.WEAPON);
        final CombatSoundEffect sound = CombatSoundEffect.getSound(weaponId);
        if (sound == null) {
            final SoundEffect fallbackSound = CombatSoundEffect.getDefaultSoundEffect(weaponId);
            if (fallbackSound == null) {
                return;
            }
            World.sendSoundEffect(new Location(player.getLocation()), fallbackSound);
            return;
        }
        World.sendSoundEffect(new Location(player.getLocation()), sound.getSound());
    }
}
