package com.zenyte.game.world.broadcasts;

import com.near_reality.tools.discord.community.DiscordBroadcastKt;
import com.near_reality.tools.discord.community.DiscordCommunityBot;
import com.near_reality.tools.logging.GameLogMessage;
import com.near_reality.tools.logging.GameLogger;
import com.zenyte.GameToggles;
import com.zenyte.game.content.achievementdiary.AdventurersLogIcon;
import com.zenyte.game.GameConstants;
import com.zenyte.game.content.follower.Pet;
import com.zenyte.game.content.follower.impl.BossPet;
import com.zenyte.game.content.minigame.inferno.model.InfernoCompletions;
import com.zenyte.game.content.serverevent.WorldBoost;
import com.zenyte.game.content.treasuretrails.rewards.BroadcastedTreasure;
import com.zenyte.game.content.vote.VoteHandler;
import com.zenyte.game.content.well.WellPerk;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.enums.RareDrop;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.MessageType;
import com.zenyte.game.world.entity.player.NotificationSettings;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.Skills;
import com.zenyte.game.world.entity.player.privilege.GameMode;
import mgi.types.config.items.ItemDefinitions;
import mgi.types.config.npcs.NPCDefinitions;
import org.slf4j.event.Level;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Tommeh | 4-2-2019 | 22:11
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class WorldBroadcasts {
    public static final List<String> DISABLED_SKILLS = Arrays.asList("attack", "strength", "defence", "hitpoints");
    public static final String[] HELPFUL_TIPS = {
            "Did you know: You can filter this message in the game settings.",
            "Did you know: Loyalty points are given to those actively playing " + GameConstants.SERVER_NAME + ". These points can be spent at the achievement " +
            "hall for cosmetic items.",
            "Did you know: You can imbue items such as slayer helmets and dagannoth rings using Imbue Tokens purchasable from the loyalty store.",
            "Did you know: You can use regular teleports, fairy rings, spirit trees, gnome gliders and the" +
            " " + GameConstants.SERVER_NAME + " portal to travel around the world.", "Did you know: You can complete " +
            "daily challenges to obtain extra experience.", "Did you know: " + GameConstants.SERVER_NAME + " has full" +
            " RuneLite integration? You can click the wrench icon on the client to customize the RuneLite settings to" +
            " your liking.", "Did you know: You can enable two factor authentication (2FA) to prevent unauthorized " +
            "logins to your accounts. Enable it in the Game noticeboard tab!", "Did you know: You can enable or " +
            "disable level-up dialogues and broadcasts in the Game Settings menu in the Game Noticeboard Tab.", "Did " +
            "you know: You can view the drop rate of any monster or item using the Drop Viewer in your Game " +
            "noticeboard tab.", "Did you know: You can easily access our Website, Forums, Discord and Store from your" +
            " Game noticeboard tab.", "Did you know: Your home, Varrock, Camelot, and Watchtower teleport can be " +
            "right clicked so you can teleport to additional locations.", "Did you know: Runecrafting yields double " +
            "the normal amount of runes per essence in addition to multiple runes at certain Runecrafting levels.",
            "Did you know: You can change your displayed experience drops by right clicking the “XP” orb and " +
                    "choosing" + " “XP multiplier.”",
            "Did you know: You can fully customize your F-keys in the Options tab.",
            "Did you know: You can rewatch the " + GameConstants.SERVER_NAME + " tutorial by talking to the " + GameConstants.SERVER_NAME + " guide near the Grand Exchange at home.", "Did you know: You can change your spellbook using the occult altar north of the Grand Exchange.", "Did you know: The Magic shop sells " + GameConstants.SERVER_NAME + " home teleport tablets that allow you to instantly teleport to the home area.", "Did you know: You can vote for " + GameConstants.SERVER_NAME + " to receive a cash reward and vote points.", "Did you know: There's a chance Krystilia will upgrade your emblem when completing a Wilderness slayer assignment.", "Did you know: You can buy RFD gloves from the chest in the Lumbridge Castle Basement.", "Did you know: You can view current buy and sell offers in the Grand Exchange with the \"Offers Viewer\" on the G.E Interface.", "Did you know: All farming timers are half that of Oldschool Runescape.", "Did you know: We provide a Wiki command and quick link under the world map that directs you to the Oldschool Runescape Wiki which is rather accurate.", "Did you know: We have a help chat called \"" + GameConstants.SERVER_CHANNEL_NAME + "\" for any new and seasoned players alike. Join via the clan chat interface.",
//            "Did you know: You can play " + GameConstants.SERVER_NAME + " on your favorite Android mobile device by " +
//                    "downloading the APK via our website.",
            "Did you know: You can find all of" +
            " our community made guides with the ::guides command.",
            "Did you know: You can start Slayer South-West of the Grand Exchange with Turael. Higher level slayer masters can also be found here.",
            "Did you know: You can purchase fully charged custom starter weapons from the " + GameConstants.SERVER_NAME + " guide for 200K each.",
            "Did you know: You can link your Discord account with your in-game account by typing /link in tyhe OSNR Discord",
            "Did you know: " + GameConstants.SERVER_NAME + " accepts OSGP donations - " +
                    "contact a member of staff for more information.", "Did you know: Zahur at Home can add " +
            "herbs to vials of water and crush secondary ingredients for you.", "Did you know: You can apply to be " +
            "part of the quality assurance team on our forums and be the first to test upcoming content.",
            "Did you know: the first to discover a Shooting star receives a nice reward"
    };



    public static void onLogin(Player player) {
        for (WorldBoost worldBoost : World.getWorldBoosts()) {
            sendMessage(player, "World Boost active - "+worldBoost.getBoostType().getMssg(), worldBoost.getBoostType().getBroadcastType());
        }
        if(VoteHandler.isBonusVote()) {
            player.sendMessage(VoteHandler.getBonusVoteMssg()+"|"+GameConstants.SERVER_VOTE_URL, MessageType.GLOBAL_BROADCAST);
        }
        switch(player.getPrivilege()) {
            case TRUE_DEVELOPER -> broadcast(player, BroadcastType.DEV_LOGIN);
            case DEVELOPER, ADMINISTRATOR -> broadcast(player, BroadcastType.ADMIN_LOGIN);
            case MODERATOR, SENIOR_MODERATOR -> broadcast(player, BroadcastType.MOD_LOGIN);
            case SUPPORT -> broadcast(player, BroadcastType.SUPPORT_LOGIN);
            default -> {
            }
        }
    }


    public static void sendMessage(Player player, String message, final BroadcastType type) {
        message = "<img=%d><col=%s><shad=000000>%s".formatted(type.getIcon(), type.getColor(), message);
        player.sendMessage(message, MessageType.UNFILTERABLE);

    }
    public static void sendMessage(String message, final BroadcastType type, final boolean aboveChatbox) {
        final MessageType messageType = aboveChatbox ? MessageType.GLOBAL_BROADCAST : MessageType.UNFILTERABLE;
        for (final Player player : World.getPlayers()) {
            if (type.getSetting().isPresent() && player.getNumericAttribute(type.getSetting().get().toString()).intValue() == 0) {
                continue;
            }
            player.sendMessage(message, messageType);
        }
    }


    public static void broadcast(final Player player, final BroadcastType type, final Object... args) {
        final StringBuilder builder = new StringBuilder();
        final StringBuilder secondaryBuilder = new StringBuilder();
        final int icon = type.getIcon();
        final String color = type.getColor();
        boolean broadcast = true;
        builder.append("<img=").append(icon).append("><col=").append(color).append(">").append("<shad=000000>");
        switch (type) {
            case BOUNTY_HUNTER: {
                builder.append("News: ");
                secondaryBuilder.append(args[0]);
                secondaryBuilder.append(" is now the Bounty Hunter Hotspot!");
                builder.append(secondaryBuilder);
                break;
            }
            case SUPER_RARE_DROP: {
                builder.append("News: ");
                secondaryBuilder.append(player.getGameModeCrown().getCrownTag());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(args[0]);
                builder.append(secondaryBuilder);
                break;
            }
            case MYSTERY_BOX_RARE_ITEM: {
                if (!(args[0] instanceof Integer) || !(args[1] instanceof String boxName)) {
                    return;
                }
                final Item it = new Item((Integer) args[0]);
                builder.append("News: ");
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has found ");
                secondaryBuilder.append(Utils.getAOrAn(it.getName()));
                secondaryBuilder.append(" ");
                secondaryBuilder.append(it.getName());
                secondaryBuilder.append(" in ");
                secondaryBuilder.append(Utils.getAOrAn(boxName));
                secondaryBuilder.append(" ");
                secondaryBuilder.append(boxName);
                secondaryBuilder.append("!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(it.getId() + ".png", secondaryBuilder.toString(), false);
            }
            break;
            case TREASURE_TRAILS: {
                if (!(args[0] instanceof Integer) || !(args[1] instanceof String)) {
                    return;
                }
                final int id = Integer.parseInt(args[0].toString());
                if (!BroadcastedTreasure.isBroadcasted(id)) {
                    return;
                }
                final String name = ItemDefinitions.getOrThrow(id).getName();
                builder.append("News: ");
                secondaryBuilder.append(player.getGameModeCrown().getCrownTag());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has received ");
                if (name.contains("gloves") || name.contains("vambraces") || name.contains("gauntlets") || name.contains("boots") || name.contains("manacles") || name.contains("sandals") || name.contains("legs")) {
                    secondaryBuilder.append("a pair of ").append(name);
                }
                else {
                    secondaryBuilder.append(Utils.getAOrAn(name));
                    secondaryBuilder.append(" ");
                    secondaryBuilder.append(name);
                }
                final String tier = args[1].toString();
                secondaryBuilder.append(" from ").append(Utils.getAOrAn(tier)).append(" ").append(tier).append(" clue" +
                    " scroll on casket ").append(player.getNumericAttribute("completed " + tier + " treasure " +
                    "trails"));
                secondaryBuilder.append("!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(id + ".png", secondaryBuilder.toString(), false);
            }
            case RARE_DROP: {
                if (!(args[0] instanceof final Item item) || !(args[1] instanceof String rewardFrom)) {
                    return;
                }
                if (!RareDrop.contains(item)) {
                    return;
                }
                if (World.hasBoost(WellPerk.DOUBLE_UNIQUES) && BossPet.getByItem(item.getId()) == null) {
                    item.setAmount(item.getAmount() * 2);
                }

                GameLogger.log(Level.INFO, () ->
                    new GameLogMessage.RareDrop(GameLogMessage.TimeProvider.getCurrentInstant(), player.getUsername(), item, rewardFrom));
                builder.append("News: ");
                secondaryBuilder.append(player.getGameModeCrown().getCrownTag());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has received ");
                secondaryBuilder.append(Utils.getAOrAn(item.getName()));
                secondaryBuilder.append(" ");
                secondaryBuilder.append(item.getName()).append(" drop ");
                if (rewardFrom.endsWith("Chambers of Xeric")) {
                    secondaryBuilder.append("from ")
                        .append(rewardFrom)
                        .append(" on chest ")
                        .append(player.getNumericAttribute(rewardFrom.startsWith("Challenge Mode") ? "challengechambersofxeric" : "chambersofxeric").intValue());
                }
                else if (rewardFrom.endsWith("Tombs of Amascut")) {
                    secondaryBuilder.append("from ")
                        .append(rewardFrom)
                        .append(" on chest ")
                        .append(player.getNumericAttribute(rewardFrom.startsWith("Expert Mode") ? "tombs of amascut: expert mode" :
                            rewardFrom.startsWith("Entry Mode") ? "tombs of amascut: entry mode" : "tombs of amascut: normal mode").intValue());
                }
                else if (rewardFrom.startsWith(".")) {
                    secondaryBuilder.append("from ").append(rewardFrom.substring(1));
                }
                else {
                    secondaryBuilder.append("from ").append(Utils.getAOrAn(rewardFrom)).append(" ").append(rewardFrom);
                    if (NotificationSettings.isKillcountTracked(rewardFrom)) {
                        secondaryBuilder.append(" on killcount ").append(player.getNotificationSettings().getKillcount(rewardFrom) + 1);
                    }
                }
                secondaryBuilder.append("!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(item.getId() + ".png", secondaryBuilder.toString(), false);
                break;
            }
            case LVL_99:
                if (!(args[0] instanceof Integer skill)) {
                    return;
                }
                String skillName = Skills.getSkillName(skill);
                if (player.getCombatXPRate() == 50 && DISABLED_SKILLS.contains(skillName.toLowerCase()))
                    broadcast = false;
                builder.append("News: ");
                secondaryBuilder.append(player.getGameModeCrown().getCrownTag());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has achieved level 99 ");
                secondaryBuilder.append(skillName);
                secondaryBuilder.append(" on x").append(player.getCombatXPRate());
                if (player.getCombatXPRate() != player.getSkillingXPRate()) {
                    secondaryBuilder.append('/').append(player.getSkillingXPRate());
                }
                secondaryBuilder.append("!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(skillName.toLowerCase() + ".png", secondaryBuilder.toString(), false);
                break;
            case MAXED:
                builder.append("News: ");
                secondaryBuilder.append(player.getGameModeCrown().getCrownTag());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has achieved level 99 in all skills");
                secondaryBuilder.append(" on x").append(player.getCombatXPRate());
                if (player.getCombatXPRate() != player.getSkillingXPRate()) {
                    secondaryBuilder.append('/').append(player.getSkillingXPRate());
                }
                secondaryBuilder.append("!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(AdventurersLogIcon.OVERALL_SKILLING, secondaryBuilder.toString());
                break;
            case XP_200M:
                if (!(args[0] instanceof Integer skill)) {
                    return;
                }
                String name = Skills.getSkillName(skill);
                builder.append("News: ");
                secondaryBuilder.append(player.getGameModeCrown().getCrownTag());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has achieved 200 million XP in ");
                secondaryBuilder.append(name);
                secondaryBuilder.append(" on x").append(player.getCombatXPRate());
                if (player.getCombatXPRate() != player.getSkillingXPRate()) {
                    secondaryBuilder.append('/').append(player.getSkillingXPRate());
                }
                secondaryBuilder.append("!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(name.toLowerCase() + ".png", secondaryBuilder.toString(), false);
                break;
            case PET:
                if (!(args[0] instanceof Pet pet)) {
                    return;
                }
                builder.append("News: ");
                secondaryBuilder.append(player.getGameModeCrown().getCrownTag());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has received the ");
                secondaryBuilder.append(Objects.requireNonNull(NPCDefinitions.get(pet.petId())).getName());
                secondaryBuilder.append(" pet!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(pet.itemId() + ".png", secondaryBuilder.toString(), false);
                break;
            case GAMBLE_FIRECAPE:
                builder.append("News: ");
                secondaryBuilder.append(player.getGameModeCrown().getCrownTag());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has received the TzRek-Jad pet by gambling their fire cape!");
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry("13225.png", secondaryBuilder.toString(), false);
                break;
            case HCIM_DEATH:
                if (player.getSkills().getTotalLevel() < 500) {
                    return;
                }
                builder.append("News: ");
                secondaryBuilder.append(player.getGameModeCrown().getCrownTag());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has died as a Hardcore ");
                secondaryBuilder.append(player.getAppearance().getGender());
                secondaryBuilder.append(" with a skill total of ");
                secondaryBuilder.append(player.getSkills().getTotalLevel());
                appendCauseOfDeath(player, secondaryBuilder, args);
                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(AdventurersLogIcon.HCIM_DEATH, secondaryBuilder.toString());
                break;
            case GROUP_HCIM_DEATH:
                final int remainingLives = (int) args[1];
                final String groupName = (String) args[2];
                builder.append("News: ");
                secondaryBuilder.append(player.getGameModeCrown().getCrownTag());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has died in hardcore group '").append(groupName).append("'");
                appendCauseOfDeath(player, secondaryBuilder, args);
                secondaryBuilder.append(" ");
                if (remainingLives > 0) {
                    secondaryBuilder.append("The group has ").append(remainingLives).append(" ");
                    if (remainingLives > 1) {
                        secondaryBuilder.append("lives");
                    } else {
                        secondaryBuilder.append("life");
                    }
                    secondaryBuilder.append(" remaining!");
                } else
                    secondaryBuilder.append("The group is out of lives and has been reverted back to a regular Iron group.");

                builder.append(secondaryBuilder);
                player.sendAdventurersEntry(AdventurersLogIcon.GROUP_HCIM_DEATH, secondaryBuilder.toString());
                break;
            case HELPFUL_TIP:
                if (!(args[0] instanceof String tip)) {
                    return;
                }
                builder.append(tip);
                break;
            case INFERNO_COMPLETION:
                final GameMode mode = player.getGameMode();
                final int completions = InfernoCompletions.getCompletions(mode);
                final String completion = completions == 1
                        ? "first" : completions == 2
                        ? "second" : completions == 3
                        ? "third" : "";
                builder.append("News: ");
                secondaryBuilder.append(player.getGameModeCrown().getCrownTag());
                secondaryBuilder.append(player.getName());
                if (!InfernoCompletions.isBroadcasted(player) && !completion.isEmpty()) {
                    secondaryBuilder.append(" is the ").append(completion).append(" ").append(mode.crown().getCrownTag()).append("</col>").append(GameMode.getTitle(player)).append("<col=").append(type.getColor()).append("> to complete the Inferno").append(", with a combat level of ").append(player.getCombatLevel()).append("!");
                    InfernoCompletions.setBroadcasted(player);
                } else {
                    secondaryBuilder.append(" has completed the Inferno").append(", with a combat level of ").append(player.getCombatLevel()).append("!");
                }
                builder.append(secondaryBuilder);
                break;
            case WILDERNESS_EVENT:
                builder.append("Event: ");
                secondaryBuilder.append(args[0]);
                builder.append(secondaryBuilder);
                break;
            case XAMPHUR:
                builder.append("Event: ");
                secondaryBuilder.append("Xamphur has been spawned, defeat him at ::event!");
                builder.append(secondaryBuilder);
                break;
            case COLOSSAL_CHICKEN:
                builder.append("Event: ");
                secondaryBuilder.append("The Colossal Chicken has been spawned, defeat her at ::easter!");
                builder.append(secondaryBuilder);
                break;
            case WILDERNESS_VAULT:
                builder.append("Event: ");
                builder.append(secondaryBuilder);
                break;
            case NEW_PLAYER: {
                if(GameToggles.NEW_PLAYER_BROADCAST_DISABLED)
                    return;
                builder.append("News: ");
                secondaryBuilder.append(player.getGameModeCrown().getCrownTag());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has just joined the server!");
                builder.append(secondaryBuilder);
                break;
            }
            case DEV_LOGIN, ADMIN_LOGIN, MOD_LOGIN, SUPPORT_LOGIN: {
                broadcast = false;
                builder.append("Staff: ");
                secondaryBuilder.append(player.getPrivilege().crown().getCrownTag());
                secondaryBuilder.append(player.getName());
                secondaryBuilder.append(" has just logged in!");
                builder.append(secondaryBuilder);
                sendMessage(builder.toString(), type, false);
                break;
            }
        }
        if (broadcast) {
            DiscordBroadcastKt.broadcast(DiscordCommunityBot.INSTANCE, player, type, args);
            sendMessage(builder.toString(), type, false);
        }
    }

    private static void appendCauseOfDeath(Player player, StringBuilder secondaryBuilder, Object[] args) {
        if (args[0] instanceof Entity source) {
            if (source instanceof Player) {
                if (source.equals(player)) {
                    secondaryBuilder.append(", by self-inflicted damage!");
                } else {
                    secondaryBuilder.append(", losing against ");
                    secondaryBuilder.append(((Player) source).getName());
                    secondaryBuilder.append("!");
                }
            } else if (source instanceof NPC) {
                secondaryBuilder.append(", fighting against: ");
                secondaryBuilder.append(((NPC) source).getDefinitions().getName());
            }
        } else {
            secondaryBuilder.append(", by unknown damage!");
        }
    }
}
