package com.zenyte.game.content.skills.magic.spells.teleports;

import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.region.RegionArea;
import com.zenyte.utils.efficientarea.Area;

import static com.zenyte.game.world.region.area.wilderness.WildernessArea.isWithinWilderness;


/**
 * @author Kris | 9. juuli 2018 : 02:29:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface Teleport {

	int WILDERNESS_LEVEL = 20;

	int DISTANCE = 2;

	boolean UNRESTRICTED = false;
	boolean RESTRICTED = true;

	TeleportType getType();

	Location getDestination();

	int getLevel();

	double getExperience();

	int getRandomizationDistance();

	Item[] getRunes();

	int getWildernessLevel();

	boolean isCombatRestricted();

	default void onUsage(final Player player) {

	}

	default void onArrival(final Player player) {

	}

	default void teleport(final Player player) {
		teleport(player, false);
	}

	default void teleport(final Player player, final boolean alwaysWarnForDeepWilderness) {
		try {
			if(isWithinWilderness(this.getDestination()) && (alwaysWarnForDeepWilderness || !player.getBooleanAttribute("NO_WILDERNESS_TELEPORT_WARNING"))) {
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						if (alwaysWarnForDeepWilderness) {
							options("Teleport into the DEEP wilderness?",
									new DialogueOption("Yes", () -> getType().getStructure().teleport(player, Teleport.this)),
									new DialogueOption("No"));
						} else {
							options("Wilderness Teleport Confirmation",
									new DialogueOption(Colour.RS_RED.wrap("Yes (enter the wilderness)"), () -> getType().getStructure().teleport(player, Teleport.this)),
									new DialogueOption(Colour.RS_RED.wrap("Yes (never show again)"), () -> {
										player.toggleBooleanAttribute("NO_WILDERNESS_TELEPORT_WARNING");
										getType().getStructure().teleport(player, Teleport.this);
									}),
									new DialogueOption(Colour.RS_GREEN.wrap("Cancel (return to safety)")));
						}
					}
				});
			} else {
				getType().getStructure().teleport(player, this);
			}
		} catch (final Exception e) {
            Magic.logger.error("", e);
		}
	}

}
