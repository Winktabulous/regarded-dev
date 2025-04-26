package com.zenyte.game.content.area.strongholdofsecurity;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.Emote;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.ContainerResult;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.dialogue.PlainChat;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import mgi.utilities.CollectionUtils;

import java.util.function.Predicate;

/**
 * @author Kris | 4. sept 2018 : 21:36:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SOSChest implements ObjectAction {

	private enum Chest {
		FIRST_FLOOR(20656, player -> player.getBooleanAttribute("sos first claimed"), player -> {
			final int coins = player.getInventory().getAmountOf(995);
			final int space = player.getInventory().getFreeSlots();
			return space == 0 && coins > 0 && coins <= (Integer.MAX_VALUE - 2000) || space > 0 && coins <= (Integer.MAX_VALUE - 2000);
		}) {
			@Override
			public Dialogue getDialogue(final Player player) {
				return new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain("The box hinges creak and appear to be forming audible words....").executeAction(() -> {
							player.getInventory().addItem(new Item(995, 2000)).onFailure(item -> World.spawnFloorItem(item, player));
							player.getEmotesHandler().unlock(Emote.FLAP);
							player.addAttribute("sos first claimed", 1);
						});
						plain("...congratulations adventurer, you have been deemed worthy of this reward. You have also unlocked the Flap emote!");
					}
				};
			}
		},
		SECOND_FLOOR(19000, player -> player.getBooleanAttribute("sos second claimed"), player -> {
			final int coins = player.getInventory().getAmountOf(995);
			final int space = player.getInventory().getFreeSlots();
			return space == 0 && coins > 0 && coins <= (Integer.MAX_VALUE - 3000) || space > 0 && coins <= (Integer.MAX_VALUE - 3000);
		}) {
			@Override
			public Dialogue getDialogue(final Player player) {
				return new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain("The grain shifts in the sack, sighing audible words....").executeAction(() -> {
							player.getInventory().addItem(new Item(995, 3000)).onFailure(item -> World.spawnFloorItem(item, player));
							player.getEmotesHandler().unlock(Emote.SLAP_HEAD);
							player.addAttribute("sos second claimed", 1);
						});
						plain("...congratulations adventurer, you have been deemed worthy of this reward. You have also unlocked the Slap head emote!");
					}
				};
			}
		},
		THIRD_FLOOR(23709, player -> player.getBooleanAttribute("sos third claimed"), player -> {
			final int coins = player.getInventory().getAmountOf(995);
			final int space = player.getInventory().getFreeSlots();
			return space == 0 && coins > 0 && coins <= (Integer.MAX_VALUE - 5000) || space > 0 && coins <= (Integer.MAX_VALUE - 5000);
		}) {
			@Override
			public Dialogue getDialogue(final Player player) {
				return new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain("The box hinges creak and appear to be forming audible words....").executeAction(() -> {
							player.getInventory().addItem(new Item(995, 5000)).onFailure(item -> World.spawnFloorItem(item, player));
							player.getEmotesHandler().unlock(Emote.IDEA);
							player.sendMessage("You feel refreshed and renewed.");
							player.addAttribute("sos third claimed", 1);
							player.heal(player.getMaxHitpoints());
							player.getPrayerManager().restorePrayerPoints(player.getSkills().getLevelForXp(SkillConstants.PRAYER));
						});
						plain("...congratulations adventurer, you have been deemed worthy of this reward. You have also unlocked the Idea emote!");
					}
				};
			}
		},
		FOURTH_FLOOR(23731, player -> false, player -> player.getInventory().hasFreeSlots()) {
			@Override
			public Dialogue getDialogue(final Player player) {
				return new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain("As your hand touches the cradle, you hear a voice in your head of a million dead adventurers...");
						if (player.getBooleanAttribute("sos fourth claimed")) {
							final boolean colourful = player.containsItem(new Item(9005));
							final boolean fighting = player.containsItem(new Item(9006));
							if (colourful || fighting) {
								options("Would you like to swap your boots to the other style?", new DialogueOption("Yes, I'd like the other pair instead please!", key(10)), new DialogueOption("No thanks, I'll keep these.", key(20)));
								player(10, "Yes, I'd like the other pair instead please!").executeAction(() -> {
									final int currentBoots = colourful ? 9005 : 9006;
									final Container[] containers = new Container[] { player.getInventory().getContainer(),
											player.getBank().getContainer(), player.getEquipment().getContainer() };
									final Container container = CollectionUtils.findMatching(containers, c -> c.contains(currentBoots, 1));
									if (container == null) {
										throw new RuntimeException("Unable to locate the container containing the players' boots.");
									}
                                    container.getItems().int2ObjectEntrySet().fastForEach(entry -> {
                                        Item item = entry.getValue();
                                        if (item.getId() == currentBoots) {
                                            item.setId(currentBoots == 9005 ? 9006 : 9005);
                                            container.refresh(entry.getIntKey());
                                            if (container.getType() == ContainerType.EQUIPMENT) {
                                                player.getEquipment().refresh();
                                            }
                                        }
                                    });
									container.refresh(player);
								});
								player(20, "No thanks, I'll keep these.");
								return;
							}
						}
						plain("....welcome adventurer... you have a choice....");
						this.doubleItem(new Item(9005), new Item(9006), "You can choose between these two pairs of boots.");
						plain("They will both protect your feet exactly the same, however they look very different. You can always come back and get another pair if you lose them, or even swap them for the other style!");
						options("Choose your style of boots", new DialogueOption("I'll take the colourful ones!", key(10)), new DialogueOption("I'll take the fighting ones!", key(20)));
						player(10, "I'll take the colourful ones!").executeAction(() -> {
							player.getEmotesHandler().unlock(Emote.STAMP);
							player.addAttribute("sos fourth claimed", 1);
							final ContainerResult result = player.getInventory().addItem(new Item(9005));
							if (result.getResult() != RequestResult.SUCCESS) {
								player.sendMessage("You need some more inventory space to do this.");
							}
						});
						if (!player.getBooleanAttribute("sos fourth claimed")) {
							plain("Congratulations! You have successfully navigated the Stronghold of Security and learned to secure your account. You have unlocked the 'Stamp Foot' emote. Remember to keep your account secure in the future!");
						}
						player(20, "I'll take the fighting ones!").executeAction(() -> {
							player.getEmotesHandler().unlock(Emote.STAMP);
							player.addAttribute("sos fourth claimed", 1);
							final ContainerResult result = player.getInventory().addItem(new Item(9006));
							if (result.getResult() != RequestResult.SUCCESS) {
								player.sendMessage("You need some more inventory space to do this.");
							}
						});
						if (!player.getBooleanAttribute("sos fourth claimed")) {
							plain("Congratulations! You have successfully navigated the Stronghold of Security and learned to secure your account. You have unlocked the 'Stamp Foot' emote. Remember to keep your account secure in the future!");
						}
					}
				};
			}
		};
		private final int id;
		private final Predicate<Player> predicate;
		private final Predicate<Player> spacePredicate;

		public abstract Dialogue getDialogue(final Player player);

		private static final Chest[] VALUES = values();
		
		Chest(int id, Predicate<Player> predicate, Predicate<Player> spacePredicate) {
		    this.id = id;
		    this.predicate = predicate;
		    this.spacePredicate = spacePredicate;
		}
	}

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId,
			final String option) {
		final Chest chest = CollectionUtils.findMatching(Chest.VALUES, c -> c.id == object.getId());
		if (chest == null) {
			throw new RuntimeException("Unable to locate the SOS chest for object " + object);
		}
		if (chest.predicate.test(player)) {
			player.getDialogueManager().start(new PlainChat(player, "You have already claimed your reward from this level."));
			return;
		}
		if (!chest.spacePredicate.test(player)) {
			player.sendMessage("You need some more inventory space to do this.");
			return;
		}
		player.getDialogueManager().start(chest.getDialogue(player));
	}

	@Override
	public Object[] getObjects() {
		final IntArrayList list = new IntArrayList(Chest.VALUES.length);
		for (final Chest value : Chest.VALUES) {
			list.add(value.id);
		}
		return list.toArray();
	}

}
