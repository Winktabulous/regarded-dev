package com.zenyte.game.content.skills.magic.spells.teleports;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.zenyte.game.content.achievementdiary.DiaryReward;
import com.zenyte.game.content.achievementdiary.DiaryUtil;
import com.zenyte.game.content.skills.magic.Magic;
import com.zenyte.game.content.skills.magic.Spellbook;
import com.zenyte.game.content.skills.magic.spells.TeleportSpell;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingStructs;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Setting;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.MemberRank;
import com.zenyte.plugins.dialogue.PlainChat;

import static com.zenyte.game.content.skills.magic.Spellbook.*;
import static com.zenyte.game.content.skills.magic.spells.teleports.TeleportType.*;

/**
 * @author Kris | 11. dets 2017 : 4:01.56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public enum SpellbookTeleport implements TeleportSpell, Teleport {

    HOME_TELEPORT(NORMAL, TeleportType.HOME_TELEPORT, 0, new Location(3087, 3490, 0)),

	EDGE_TELEPORT(NORMAL, ARCEUUS_TELEPORT, 0, new Location(3095, 3501, 0)),

	HOME_TELEPORT_INSTANT(NORMAL, TeleportType.HOME_TELEPORT, 0, new Location(3087, 3490, 0)) {
		@Override
		public TeleportType getType() {
			return TeleportType.NEAR_REALITY_PORTAL_TELEPORT;
		}
	},

    LUMBRIDGE_HOME_TELEPORT(NORMAL, TeleportType.HOME_TELEPORT, 0, new Location(3222, 3218, 0)),
	VARROCK_TELEPORT(NORMAL, REGULAR_TELEPORT, 35, new Location(3212, 3423, 0)),
	GRAND_EXCHANGE(NORMAL, REGULAR_TELEPORT, 35, new Location(3165, 3477, 0)) {
		@Override
		public Item[] getRunes() {
			return VARROCK_TELEPORT.getRunes();
		}

		@Override
		public int getLevel() {
			return VARROCK_TELEPORT.getLevel();
		}
	},
	LUMBRIDGE_TELEPORT(NORMAL, REGULAR_TELEPORT, 41, new Location(3222, 3218, 0)),
	FALADOR_TELEPORT(NORMAL, REGULAR_TELEPORT, 48, new Location(2965, 3379, 0)),
	TELEPORT_TO_HOUSE(NORMAL, HOUSE_TELEPORT, 30, null),
	CAMELOT_TELEPORT(NORMAL, REGULAR_TELEPORT, 55.5, new Location(2757, 3478, 0)),
	SEERS_TELEPORT(NORMAL, REGULAR_TELEPORT, 55.5, new Location(2725, 3485, 0)) {
		@Override
		public Item[] getRunes() {
			return CAMELOT_TELEPORT.getRunes();
		}

		@Override
		public int getLevel() {
			return CAMELOT_TELEPORT.getLevel();
		}

		@Override
        public void teleport(final Player player) {
            try {
                if (!DiaryUtil.eligibleFor(DiaryReward.KANDARIN_HEADGEAR3, player)) {
                    player.getDialogueManager().start(new PlainChat(player, "You need to have completed hard kandarin diaries to use this teleport."));
                    return;
                }
                getType().getStructure().teleport(player, this);
            } catch (final Exception e) {
                Magic.logger.error("", e);
            }
        }
	},
	ARDOUGNE_TELEPORT(NORMAL, REGULAR_TELEPORT, 61, new Location(2662, 3306, 0)),
	WATCHTOWER_TELEPORT(NORMAL, REGULAR_TELEPORT, 68, new Location(2549, 3114, 2)),
	YANILLE_TELEPORT(NORMAL, REGULAR_TELEPORT, 68, new Location(2582, 3097, 0)) {
		@Override
		public Item[] getRunes() {
			return WATCHTOWER_TELEPORT.getRunes();
		}

		@Override
		public int getLevel() {
			return WATCHTOWER_TELEPORT.getLevel();
		}
	},
	TROLLHEIM_TELEPORT(NORMAL, REGULAR_TELEPORT, 68, new Location(2891, 3669, 0)),
	APE_ATOLL_TELEPORT_REG(NORMAL, REGULAR_TELEPORT, 74, new Location(2796, 2799, 1)) {
		@Override
		public Item[] getRunes() {
			return new Item[]{
					new Item(Magic.LAW_RUNE, 2), new Item(Magic.WATER_RUNE, 2),
					new Item(Magic.FIRE_RUNE, 2), new Item(1963, 1)
			};
		}

		@Override
		public int getLevel() {
            return 64;
        }

        @Override
        public String toString() {
	        return "Ape Atoll Teleport";
        }

        @Override
        public String failureMessage() {
	        return null;
        }
    },
	KOUREND_CASTLE_TELEPORT(NORMAL, REGULAR_TELEPORT, 69, new Location(1644, 3673, 0)),

    ANCIENT_ZENYTE_HOME_TELEPORT(ANCIENT, TeleportType.HOME_TELEPORT, 0, new Location(3087, 3490, 0)) {
        @Override
        public String toString() {
            return "home teleport";
        }
    },
	PADDEWWA_TELEPORT(ANCIENT, ANCIENT_TELEPORT, 64, new Location(3097, 9869, 0)),
	SENNTISTEN_TELEPORT(ANCIENT, ANCIENT_TELEPORT, 70, new Location(3348, 3346, 0)),
	KHARYRLL_TELEPORT(ANCIENT, ANCIENT_TELEPORT, 76, new Location(3498, 3485, 0)),
	LASSAR_TELEPORT(ANCIENT, ANCIENT_TELEPORT, 82, new Location(3010, 3499, 0)),
	DAREEYAK_TELEPORT(ANCIENT, ANCIENT_TELEPORT, 88, new Location(2972, 3702, 0)),
	CARRALLANGAR_TELEPORT(ANCIENT, ANCIENT_TELEPORT, 94, new Location(3142, 3673, 0)),
	ANNAKARL_TELEPORT(ANCIENT, ANCIENT_TELEPORT, 100, new Location(3288, 3886, 0)),
	GHORROCK_TELEPORT(ANCIENT, ANCIENT_TELEPORT, 106, new Location(2977, 3873, 0)),

    LUNAR_ZENYTE_HOME_TELEPORT(LUNAR, TeleportType.HOME_TELEPORT, 0, new Location(3087, 3490, 0)) {
        @Override
        public String toString() {
            return "home teleport";
        }
    },
	LUNAR_HOME_TELEPORT(LUNAR, TeleportType.HOME_TELEPORT, 0, new Location(2086, 3913, 0)),
	MOONCLAN_TELEPORT(LUNAR, LUNAR_TELEPORT, 66, new Location(2113, 3915, 0)),
	OURANIA_TELEPORT(LUNAR, LUNAR_TELEPORT, 69, new Location(2454, 3233, 0)),
	WATERBIRTH_TELEPORT(LUNAR, LUNAR_TELEPORT, 71, new Location(2548, 3757, 0)),
	BARBARIAN_TELEPORT(LUNAR, LUNAR_TELEPORT, 53, new Location(2518, 3571, 0)),
	KHAZARD_TELEPORT(LUNAR, LUNAR_TELEPORT, 54, new Location(2654, 3158, 0)),
	FISHING_GUILD_TELEPORT(LUNAR, LUNAR_TELEPORT, 65, new Location(2611, 3390, 0)),
	CATHERBY_TELEPORT(LUNAR, LUNAR_TELEPORT, 78, new Location(2803, 3434, 0)),
	ICE_PLATEAU_TELEPORT(LUNAR, LUNAR_TELEPORT, 96, new Location(2976, 3924, 0)),

    ARCEUUS_ZENYTE_HOME_TELEPORT(ARCEUUS, TeleportType.HOME_TELEPORT, 0, new Location(3087, 3490, 0)) {
        @Override
        public String toString() {
            return "home teleport";
		}
	},
	ARCEUUS_HOME_TELEPORT(ARCEUUS, TeleportType.HOME_TELEPORT, 0, new Location(1699, 3879, 0)),
	LUMBRIDGE_GRAVEYARD_TELEPORT(ARCEUUS, ARCEUUS_TELEPORT, 10, new Location(3244, 3183, 0)),
	DRAYNOR_MANOR_TELEPORT(ARCEUUS, ARCEUUS_TELEPORT, 16, new Location(3110, 3328, 0)),
	BATTLEFRONT_TELEPORT(ARCEUUS, ARCEUUS_TELEPORT, 19, new Location(1384, 3718, 0)),
	MIND_ALTAR_TELEPORT(ARCEUUS, ARCEUUS_TELEPORT, 22, new Location(2978, 3518, 0)),
	RESPAWN_TELEPORT(ARCEUUS, RESPAWN_POINT_TELEPORT, 27, null),
	SALVE_GRAVEYARD_TELEPORT(ARCEUUS, ARCEUUS_TELEPORT, 30, new Location(3438, 3485, 0)),
	WEST_ARDOUGNE_TELEPORT(ARCEUUS, ARCEUUS_TELEPORT, 68, new Location(2502, 3289, 0)),
	HARMONY_ISLAND_TELEPORT(ARCEUUS, ARCEUUS_TELEPORT, 74, new Location(3800, 2829, 0)),
	CEMETERY_TELEPORT(ARCEUUS, ARCEUUS_TELEPORT, 82, new Location(2978, 3753, 0)),
	BARROWS_TELEPORT(ARCEUUS, ARCEUUS_TELEPORT, 90, new Location(3565, 3306, 0)),
	APE_ATOLL_TELEPORT(ARCEUUS, ARCEUUS_TELEPORT, 100, new Location(2764, 9103, 0)),
	FENKENSTRAINS_CASTLE_TELEPORT(ARCEUUS, ARCEUUS_TELEPORT, 50, new Location(3548, 3529, 0)) {
		@Override
		public String toString() {
			return "FENKENSTRAIN'S_CASTLE_TELEPORT";
		}
	},
	TELEPORT_TO_BOUNTY_TARGET(null, BOUNTY_TARGET_TELEPORT, 45, null, true) {
		@Override
		public boolean canCast(final Player player) {
			return player.getCombatDefinitions().getSpellbook() != Spellbook.ARCEUUS;
		}
	};

	private final double experience;
	private final Location destination;
	private final TeleportType type;
	private final int wildernessLevel;
	private final boolean combatRestricted;
	private final Spellbook spellbook;

	SpellbookTeleport(final Spellbook spellbook, final TeleportType type, final double experience, final Location destination) {
		this(spellbook, type, experience, destination, 20, false);
	}

    SpellbookTeleport(final Spellbook spellbook, final TeleportType type, final double experience, final Location destination,
                      final boolean combatRestricted) {
		this(spellbook, type, experience, destination, 20, combatRestricted);
	}

	SpellbookTeleport(final Spellbook spellbook, final TeleportType type, final double experience, final Location destination,
                      final int wildernessLevel, final boolean combatRestricted) {
		this.spellbook = spellbook;
		this.type = type;
		this.experience = experience;
		this.destination = destination;
		this.wildernessLevel = wildernessLevel;
		this.combatRestricted = combatRestricted;
	}

	@Override
	public int getDelay() {
		return 1500;
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
		return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, toString().toLowerCase()).replaceAll("_", " ");
	}

	protected String failureMessage() {
        return "You cannot cast that spell on this spellbook.";
    }
	/**
	 * TODO: Varbit 5006, NPC contact. Value = last spoken-to op; (10 + vb 5037, 1 = Steve)
	 */

	@Override
	public void execute(final Player player, final int optionId, final String option) {

		if (!canCast(player)) {
		    final String message = failureMessage();
		    if (message != null) {
                player.sendMessage(message);
            }
			return;
		}
		if (!canUse(player)) {
			return;
		}
		if (this == HOME_TELEPORT || this == ANCIENT_ZENYTE_HOME_TELEPORT || this == LUNAR_ZENYTE_HOME_TELEPORT || this == ARCEUUS_ZENYTE_HOME_TELEPORT) {
			if (option.equalsIgnoreCase("Lumbridge")) {
                Preconditions.checkArgument(player.getCombatDefinitions().getSpellbook() == NORMAL);
		        LUMBRIDGE_HOME_TELEPORT.teleport(player);
		        return;
            }
		    if (option.equalsIgnoreCase("Lunar Isle")) {
                Preconditions.checkArgument(player.getCombatDefinitions().getSpellbook() == LUNAR);
		        LUNAR_HOME_TELEPORT.teleport(player);
		        return;
            } else if (option.equalsIgnoreCase("Arceuus")) {
                Preconditions.checkArgument(player.getCombatDefinitions().getSpellbook() == ARCEUUS);
		        ARCEUUS_HOME_TELEPORT.teleport(player);
		        return;
            } else {
				if (player.getMemberRank().equalToOrGreaterThan(MemberRank.LEGENDARY)) {
					HOME_TELEPORT_INSTANT.teleport(player);
					return;
				}
			}
			if (optionId == 10) {
				if (!player.getBooleanSetting(Setting.UNLOCKED_RUNEFEST_HOME_TELEPORT_ANIMATION)) {
					return;
				}
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						options("Pick a home teleport animation.", new DialogueOption("Default.", () -> {
                            setKey(10);
                            player.getSettings().setSetting(Setting.USING_RUNEFEST_TELEPORT_ANIMATION, 0);
                        }),
                                new DialogueOption("RuneFest 2014 animation.", () -> {
                                    setKey(20);
                                    player.getSettings().setSetting(Setting.USING_RUNEFEST_TELEPORT_ANIMATION, 1);
                                }));
						plain(10, "You have chosen the default home teleport animation.");
						plain(20,
								"You have activated the RuneFest 2014 home teleport animation. In areas where this animation cannot play, the default animation will be used instead.");
					}
				});
				return;
			}
		}
		if (option.equals("Configure")) {
			switch (this) {
			case VARROCK_TELEPORT:
				player.getSettings().toggleSetting(Setting.VARROCK_TELEPORT_CONFIGURATION);
				player.sendMessage("Varrock teleport order has been reversed; left-click teleport now teleports you to: "
						+ (player.getBooleanSetting(Setting.VARROCK_TELEPORT_CONFIGURATION) ? "Grand Exchange" : "Varrock") + ".");
				break;
			case CAMELOT_TELEPORT:
				player.getSettings().toggleSetting(Setting.CAMELOT_TELEPORT_CONFIGURATION);
				player.sendMessage("Camelot teleport order has been reversed; left-click teleport now teleports you to: "
						+ (player.getBooleanSetting(Setting.CAMELOT_TELEPORT_CONFIGURATION) ? "Seers' village" : "Camelot") + ".");
				break;
			case WATCHTOWER_TELEPORT:
				player.getSettings().toggleSetting(Setting.WATCHTOWER_TELEPORT_CONFIGURATION);
				player.sendMessage("Watchtower teleport order has been reversed; left-click teleport now teleports you to: "
						+ (player.getBooleanSetting(Setting.WATCHTOWER_TELEPORT_CONFIGURATION) ? "Yanille" : "Watchtower") + ".");
				break;
			default:
				break;
			}
			return;
		}

		int structId = -1;
		switch (this) {
			case TELEPORT_TO_BOUNTY_TARGET:
				structId = SettingStructs.SHOW_WARNING_WHEN_CASTING_TELEPORT_TO_TARGET_STRUCT_ID;
				break;
			case DAREEYAK_TELEPORT:
				structId = SettingStructs.SHOW_WARNING_WHEN_CASTING_DAREEYAK_TELEPORT_STRUCT_ID;
				break;
			case CARRALLANGAR_TELEPORT:
				structId = SettingStructs.SHOW_WARNING_WHEN_CASTING_CARRALLANGAR_TELEPORT_STRUCT_ID;
				break;
			case ANNAKARL_TELEPORT:
				structId = SettingStructs.SHOW_WARNING_WHEN_CASTING_ANNAKARL_TELEPORT_STRUCT_ID;
				break;
			case GHORROCK_TELEPORT:
				structId = SettingStructs.SHOW_WARNING_WHEN_CASTING_GHORROCK_TELEPORT_STRUCT_ID;
				break;
		}
		if (structId != -1) {
			final com.zenyte.game.model.ui.testinterfaces.advancedsettings.Setting setting = com.zenyte.game.model.ui.testinterfaces.advancedsettings.Settings.findSettingByStructId(structId);
			if (optionId == 10) {
				SettingVariables.setVariableValue(setting, player, SettingVariables.getVariableValue(setting, player) == 1 ? 0 : 1);
				return;
			}
			if (optionId == 1 && SettingVariables.getVariableValue(setting, player) == 1) {
				warn(player);
				return;
			}
		}
		if (optionId == 1) {
			if (this == CEMETERY_TELEPORT) {
				warn(player);
				return;
			} else if (this == ICE_PLATEAU_TELEPORT) {
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain("You are about to teleport to " + Colour.RED + "deep wilderness" + Colour.END
								+ ".<br>Are you sure you wish to continue?");
						options("Teleport to deep wilderness?", "Yes, teleport me.", "No, abort.").onOptionOne(() -> {
							if (!canCast(player)) {
								player.sendMessage("You cannot cast that spell on this spellbook.");
								return;
							}
							if (!canUse(player)) {
								return;
							}
							ICE_PLATEAU_TELEPORT.teleport(player);
						});
					}
				});
				return;
			} else if (this == VARROCK_TELEPORT) {
				if (player.getBooleanSetting(Setting.VARROCK_TELEPORT_CONFIGURATION)) {
					GRAND_EXCHANGE.teleport(player);
					return;
				}
			} else if (this == CAMELOT_TELEPORT) {
				if (player.getBooleanSetting(Setting.CAMELOT_TELEPORT_CONFIGURATION)) {
					SEERS_TELEPORT.teleport(player);
					return;
				}
			} else if (this == WATCHTOWER_TELEPORT) {
				if (player.getBooleanSetting(Setting.WATCHTOWER_TELEPORT_CONFIGURATION)) {
					YANILLE_TELEPORT.teleport(player);
					return;
				}
			}
			teleport(player);
			return;
		} else if (optionId == 3) {
			if (this == VARROCK_TELEPORT) {
				if (player.getBooleanSetting(Setting.VARROCK_TELEPORT_CONFIGURATION)) {
					VARROCK_TELEPORT.teleport(player);
					return;
				}
			} else if (this == CAMELOT_TELEPORT) {
				if (player.getBooleanSetting(Setting.CAMELOT_TELEPORT_CONFIGURATION)) {
					CAMELOT_TELEPORT.teleport(player);
					return;
				}
			} else if (this == WATCHTOWER_TELEPORT) {
				if (player.getBooleanSetting(Setting.WATCHTOWER_TELEPORT_CONFIGURATION)) {
					WATCHTOWER_TELEPORT.teleport(player);
					return;
				}
			}
		}
		switch (option) {
		case "Grand Exchange":
			GRAND_EXCHANGE.teleport(player);
			return;
		case "Seers'":
			SEERS_TELEPORT.teleport(player);
			return;
		case "Yanille":
			YANILLE_TELEPORT.teleport(player);
			return;
		}
	}

	private void warn(final Player player) {
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				plain("You are about to teleport to " + Colour.RED + "wilderness" + Colour.END + ".<br>Are you sure you wish to continue?");
				options(TITLE, "Yes, teleport me.", "No, abort.").onOptionOne(() -> {
					if (!canCast(player)) {
						player.sendMessage("You cannot cast that spell on this spellbook.");
						return;
					}
					if (!canUse(player)) {
						return;
					}
					teleport(player);
				});
			}
		});
	}

	public double getExperience() {
	    return experience;
	}

	public Location getDestination() {
	    return destination;
	}

	public TeleportType getType() {
	    return type;
	}

	public int getWildernessLevel() {
	    return wildernessLevel;
	}

	public boolean isCombatRestricted() {
	    return combatRestricted;
	}

	public Spellbook getSpellbook() {
	    return spellbook;
	}
}
