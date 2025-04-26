package com.zenyte.game.content.area.prifddinas.zalcano.combat;

import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.game.content.skills.mining.MiningDefinitions;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.action.combat.AttackStyle;
import com.zenyte.game.world.entity.player.action.combat.CombatType;
import com.zenyte.game.world.entity.player.action.combat.MeleeCombat;

public class DownedCombatStrategy extends MeleeCombat {

    public DownedCombatStrategy(Entity target) {
        super(target);
    }

    @Override
    public int processWithDelay() {

        if (!isWithinAttackDistance()) {
            return 0;
        }
        if (!canAttack()) {
            return -1;
        }


        return super.processWithDelay();
    }

    @Override
    protected boolean canInitiate() {
        if (MiningDefinitions.PickaxeDefinitions.get(player, true).isEmpty()) {
            player.sendMessage("You need a pickaxe to mine The Zalcano.");
            return false;
        }

        return super.canInitiate();
    }

    @Override
    protected int getSpeed() {
        return 2;
    }

    @Override
    protected void grantExperience(int skill, double amount) {
        player.getSkills().addXp(SkillConstants.MINING, 35.2);
        player.sendMessage("You chip away at Zalcano and deal some damage to her.");
    }

    @Override
    public AttackStyle getAttackStyle() {
        return new AttackStyle(AttackType.MELEE, AttackStyle.AttackExperienceType.NOT_AVAILABLE);
    }

    public MiningDefinitions.PickaxeDefinitions.PickaxeResult getCurrentPickaxe() {
        var current = MiningDefinitions.PickaxeDefinitions.get(player, true);
        return current.get();
    }

    @Override
    protected void animate() {
        final Animation animationId = getCurrentPickaxe().getDefinition().getAnim();
        player.setAnimation(animationId);
    }

    @Override
    public int getMaxHit(Player player, double specialModifier, double activeModifier, boolean ignorePrayers) {
        MiningDefinitions.PickaxeDefinitions.PickaxeResult getCurrentPick = getCurrentPickaxe();
        float max = Math.max(((float)(getCurrentPick.getDefinition().getLevel()) - 40) / 15 ,0);
        float miningLevel = ((float) player.getSkills().getLevel(SkillConstants.MINING)) / 2.8F;

        if (player.hasPrivilege(PlayerPrivilege.DEVELOPER)) {
            miningLevel *= 4;
        }

        return (int) Math.floor(max + miningLevel);
    }



}
