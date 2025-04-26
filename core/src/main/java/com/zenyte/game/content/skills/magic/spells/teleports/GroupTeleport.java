package com.zenyte.game.content.skills.magic.spells.teleports;

import com.google.common.base.CaseFormat;
import com.zenyte.game.content.skills.magic.SpellDefinitions;
import com.zenyte.game.content.skills.magic.SpellState;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.actions.Teleother;
import com.zenyte.game.content.skills.magic.spells.TeleportSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 16. juuli 2018 : 01:39:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum GroupTeleport implements TeleportSpell, Teleport {
    TELE_GROUP_MOONCLAN(67, TeleportCollection.TELE_GROUP_MOONCLAN), TELE_GROUP_WATERBIRTH(72, TeleportCollection.TELE_GROUP_WATERBIRTH), TELE_GROUP_BARBARIAN(77, TeleportCollection.TELE_GROUP_BARBARIAN), TELE_GROUP_KHAZARD(79, TeleportCollection.TELE_GROUP_KHAZARD), TELE_GROUP_FISHING_GUILD(86, TeleportCollection.TELE_GROUP_FISHING_GUILD), TELE_GROUP_CATHERBY(93, TeleportCollection.TELE_GROUP_CATHERBY), TELE_GROUP_ICE_PLATEAU(99, TeleportCollection.TELE_GROUP_ICE_PLATEAU);
    private final double experience;
    private final Teleport teleport;

    GroupTeleport(final double experience, final Teleport teleport) {
        this.experience = experience;
        this.teleport = teleport;
    }

    @Override
    public int getDelay() {
        return 3000;
    }

    @Override
    public int getRandomizationDistance() {
        return 3;
    }

    @Override
    public int getLevel() {
        return TeleportSpell.super.getLevel();
    }

    @Override
    public Item[] getRunes() {
        return TeleportSpell.super.getRunes();
    }

    @Override
    public String getSpellName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name().toLowerCase()).replaceAll("_", " ");
    }

    @Override
    public void execute(final Player player, final int optionId, final String option) {
        if (!canCast(player)) {
            player.sendMessage("You cannot cast that spell on this spellbook.");
            return;
        }
        if (!canUse(player)) {
            return;
        }
        switch (option) {
        case "Cast": 
            if (equals(TELE_GROUP_ICE_PLATEAU)) {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        plain("You are about to teleport to " + Colour.RED + "deep wilderness" + Colour.END + ".<br>Are you sure you wish to continue?");
                        options("Teleport to deep wilderness?", "Yes, teleport me.", "No, abort.").onOptionOne(() -> perform(player));
                    }
                });
                return;
            }
            perform(player);
            return;
        }
    }

    /**
     * Sends the teleother request to all nearby potential players, and teleports this given player.
     *
     * @param player the player who's casting the spell.
     */
    private final void perform(final Player player) {
        if (!canCast(player)) {
            player.sendMessage("You cannot cast that spell on this spellbook.");
            return;
        }
        if (!canUse(player)) {
            return;
        }
        final SpellDefinitions definitions = SpellDefinitions.SPELLS.get(getSpellName());
        if (definitions == null) {
            return;
        }
        final SpellState state = new SpellState(player, definitions.getLevel(), definitions.getRunes());
        if (!state.check()) {
            return;
        }
        player.findCharacters(3, Player.class, p -> !p.isLocked() && !p.isDead() && p.getVarManager().getBitValue(SettingVariables.ACCEPT_AID_VARBIT_ID) == 1 && !p.getInterfaceHandler().containsInterface(InterfacePosition.CENTRAL) && !p.getInterfaceHandler().containsInterface(InterfacePosition.DIALOGUE) && p != player).forEach(p -> Teleother.request(player, p, teleport));
        teleport(player);
    }

    @Override
    public int getWildernessLevel() {
        return WILDERNESS_LEVEL;
    }

    @Override
    public boolean isCombatRestricted() {
        return false;
    }

    @Override
    public Spellbook getSpellbook() {
        return Spellbook.LUNAR;
    }

    @Override
    public TeleportType getType() {
        return TeleportType.GROUP_TELEPORT;
    }

    @Override
    public Location getDestination() {
        return teleport.getDestination();
    }

    public double getExperience() {
        return experience;
    }
}
