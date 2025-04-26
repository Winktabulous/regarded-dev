package com.zenyte.game.model.ui.testinterfaces;

import com.google.common.eventbus.Subscribe;
import com.near_reality.api.service.user.UserPlayerAttributesKt;
import com.near_reality.api.service.vote.VotePlayerAttributesKt;
import com.zenyte.game.GameClock;
import com.zenyte.game.GameConstants;
import com.zenyte.game.GameInterface;
import com.zenyte.game.content.achievementdiary.Diary;
import com.zenyte.game.content.boons.impl.AnimalTamer;
import com.zenyte.game.content.donation.DonationToggle;
import com.zenyte.game.model.BonusXpManager;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.PaneType;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.util.Colour;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.player.*;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.game.world.entity.player.privilege.ExpConfiguration;
import com.zenyte.game.world.entity.player.privilege.ExpConfigurations;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.plugins.events.LoginEvent;
import com.zenyte.plugins.events.LogoutEvent;
import com.zenyte.utils.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Tommeh | 2-12-2018 | 16:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class GameNoticeboardInterface extends Interface {
    private static int lastSentStaff = 0;
    private static final Logger logger = LoggerFactory.getLogger(GameNoticeboardInterface.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private static final PlayerPrivilege[] STAFF = {
            PlayerPrivilege.TRUE_DEVELOPER, PlayerPrivilege.DEVELOPER, PlayerPrivilege.ADMINISTRATOR,
            PlayerPrivilege.SENIOR_MODERATOR, PlayerPrivilege.MODERATOR, PlayerPrivilege.SUPPORT,
    };

    @Subscribe
    public static void onLogin(final LoginEvent event) {
        final Player p = event.getPlayer();

        p.getPacketDispatcher().sendClientScript(10586,
                162 << 16 | 2, 1701 << 16 | 11, 1701 << 16 | 16,
                1701 << 16 | 31, 1701 << 16 | 32, 1701 << 16 | 33, 1701 << 16 | 47);
        p.getVariables().synchNoticeboardVars();
        GameInterface.GAME_NOTICEBOARD.getPlugin().ifPresent(plugin ->
                p.getPacketDispatcher().sendComponentText(
                        plugin.getInterface(),
                        plugin.getComponent("Time"),
                        "Time: " + Colour.WHITE.wrap(GameClock.gameTime())));

        refreshCounters();
    }

    public static void refreshXericsWisdom(@NotNull final Player player) {
        player.getVarManager().sendVar(3806, (int) (player.getVariables().getRaidsBoost() * 0.6F));
    }

    public static void refreshBonusXP() {
        final int bonusXPSecondsLeft = Math.max(0, (int) TimeUnit.MILLISECONDS.toSeconds(
                BonusXpManager.expirationDate - System.currentTimeMillis())
        );
        for (final Player player : World.getPlayers()) {
            player.getVarManager().sendVar(3805, bonusXPSecondsLeft);
        }
    }

    public static final AtomicInteger wildernessCount = new AtomicInteger();

    public static void refreshCounters() {
        final int displayedPlayerCount = World.getDisplayedPlayerCount();
        final int staffCount = World.getStaffCountOnline();
        final int wildernessCount = GameNoticeboardInterface.wildernessCount.get();

        if(lastSentStaff != staffCount)
            updateStaffCounters(staffCount);

        for (final Player player : World.getPlayers()) {
            player.getVarManager().sendVar(3803, displayedPlayerCount);
            //player.getVarManager().sendVar(3508, mobile.intValue());
            player.getVarManager().sendVar(3807, wildernessCount);
        }
    }

    private static void updateStaffCounters(int staffCount) {
        for (final Player player : World.getPlayers()) {
            player.getVarManager().sendVar(3804, staffCount);
        }
        lastSentStaff = staffCount;
    }

    public static void refreshWildernessCounters(final int wildernessCount) {
        for (final Player player : World.getPlayers()) {
            player.getVarManager().sendVar(3807, wildernessCount);
        }
    }

    @Subscribe
    public static void onLogout(final LogoutEvent event) {
        final Player p = event.getPlayer();
        refreshCounters();
    }

    private static List<Player> getStaff(final Player requester, final PlayerPrivilege privilege) {
        final boolean requesterIsStaff = requester.getPrivilege().inherits(PlayerPrivilege.FORUM_MODERATOR);
        return World.getPlayers()
                .stream()
                .filter(p -> privilege.equals(p.getPrivilege())
                        && (requesterIsStaff || !isHidden(p)))
                .collect(Collectors.toList());
    }

    public static void showStaffOnline(final Player player) {
        final ArrayList<String> lines = new ArrayList<>();
        int count = 0;
        for (final PlayerPrivilege privilege : STAFF) {
            final List<Player> members = getStaff(player, privilege);
            final int size = members.size();
            if (size < 1) continue;
            count += size;
            lines.add(privilege.crown().getCrownTag() + ' '
                    + Colour.DARK_BLUE.wrap(privilege.getPrettyName() + 's'));
            members.forEach(p -> lines.add(p.getName().replace('_', ' ')
                    + (isHidden(p) ? " (" + Colour.MAROON.wrap("Hidden") + ")" : "")));
            lines.add("\n");
        }
        Diary.sendJournal(player, "Staff online: " + count, lines);
    }

    public static boolean isHidden(final Player player) {
        return !SocialManager.PrivateStatus.ALL.equals(player.getSocialManager().getStatus());
    }

    @Override
    protected void attach() {
        put(8, "Players online");
        put(9, "Staff online");
        put(10, "Wilderness players");
        put(11, "Up-time");
        put(12, "Time");
        put(14, "2FA");
        put(15, "XP rate");
        put(16, "Time played");
        put(17, "Register date");
        put(18, "Privilege");
        put(19, "Game Mode");
        put(20, "Member Rank");
        put(21, "Loyalty points");
        put(22, "Total donated");
        put(23, "Vote credits");
        put(25, "Game Settings");
        put(27, "Drop Viewer");
        put(29, "Daily Challenges");
        put(31, "Bonus XP");
        put(32, "CoX Boost");
        put(35, "Website");
        put(37, "Forums");
        put(39, "Discord");
        put(42, "Store");
        put(43, "Toggles");
        put(45, "Boosters");
        put(46, "Drop rate");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface().getId(), 33, PaneType.JOURNAL_TAB_HEADER, true);
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Time"),
                "Time: <col=ffffff>" + GameClock.gameTime());
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("2FA"),
                (player.getAuthenticator().isEnabled() ? Colour.GREEN : Colour.RED).wrap("Two-Factor Authentication"));
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("XP rate"),
                "XP: <col=ffffff>" + ((player.getSkillingXPRate() == 1) ? "-" : (player.getCombatXPRate() + "x Combat" +
                        " & " + player.getSkillingXPRate() + "x Skilling</col>")));
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Privilege"), "Privilege: " +
                "<col=ffffff>" + player.getPrivilege().crown().getCrownTag() + player.getPrivilege().getPrettyName() +
                "</col>");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Game Mode"), "Mode: <col=ffffff" +
                ">" + player.getGameModeCrown().getCrownTag() + player.getGameMode().toString() + "</col>");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Member Rank"), "Member: " +
                "<col=ffffff>" + player.getMemberCrown().getCrownTag() + player.getMemberName().replace(" Member", "") + "</col>");
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Loyalty points"), "Loyalty " +
                "points: <col=ffffff>" + player.getLoyaltyManager().getLoyaltyPoints() + "</col>");
        final String totalDonated = "Total donated: <col=ffffff>$" + UserPlayerAttributesKt.getStoreTotalSpent(player) + "</col>";
        player.log(LogLevel.INFO, totalDonated);
        player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Total donated"), totalDonated);
        player.getPacketDispatcher()
                .sendComponentText(getInterface(), getComponent("Vote credits"), "Vote credits: " +
                        "<col=ffffff>" + VotePlayerAttributesKt.getTotalVoteCredits(player) + "</col>");
        player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("2FA"), -1, 0,
                AccessMask.CLICK_OP1);
        updateDropRate(player);

        try {
            final long time = player.getNumericAttribute("forum registration date").longValue();
            final ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.UTC);
            final String formatted = FORMATTER.format(zdt);
            player.getPacketDispatcher().sendComponentText(getInterface(), getComponent("Register date"), "Registered" +
                    " on: <col=ffffff>" + formatted + "</col>");
        } catch (Exception e) {
            logger.error("Failed to send register date", e);
        }
    }

    public static void updateDropRate(Player player) {
        ExpConfigurations config = ExpConfigurations.of(player.getGameMode());
        int currentIndex = config.getExpConfigurationIndex(player.getCombatXPRate(), player.getSkillingXPRate());
        ExpConfiguration[] configurations = ExpConfigurations.of(player.getGameMode()).getConfigurations();
        ExpConfiguration configuration = configurations[currentIndex];
        double gameMode = configuration.getDropRateIncrease() / 100.0D;
        double donor = player.getMemberRank().getDR();
        double pin = player.getBooleanAttribute("drop_rate_pin_claimed") ? 0.05D : 0.0D;
        double pet = player.getBoonManager().hasBoon(AnimalTamer.class) && player.getPetId() != -1 ? 0.02D : 0.0D;
        double compCape = player.getCompletionistCapeDRBoost();
        int percent = (int) ((gameMode + donor + pin + pet + compCape) * 100.0D);
        player.getPacketDispatcher().sendComponentText(GameInterface.GAME_NOTICEBOARD, 46, "Drop Rate Boost: " + Colour.WHITE.wrap(percent + "%"));
    }

    @Override
    protected void build() {
        bind("Staff online", GameNoticeboardInterface::showStaffOnline);
        bind("Game Settings", player -> {
            GameInterface.GAME_SETTINGS.open(player);
            Analytics.flagInteraction(player, Analytics.InteractionType.GAME_SETTINGS);
        });
        bind("Drop Viewer", player -> {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    options("Which viewer would you like to open?", "NPC/Item Drops", "Table Drops")
                            .onOptionOne(() -> GameInterface.DROP_VIEWER.open(player))
                            .onOptionTwo(() -> DropViewerInterface.openToAlternateTables(player));
                }
            });
        });
        bind("Daily Challenges", GameInterface.DAILY_CHALLENGES_OVERVIEW::open);
        bind("2FA", player -> player.getPacketDispatcher().sendURL(GameConstants.SERVER_ACCOUNT_URL));
        bind("Website", player -> player.getPacketDispatcher().sendURL(GameConstants.SERVER_WEBSITE_URL));
        bind("Forums", player -> player.getPacketDispatcher().sendURL(GameConstants.SERVER_FORUMS_URL));
        bind("Discord", player -> player.getPacketDispatcher().sendURL(GameConstants.DISCORD_INVITE));
        bind("Store", GameInterface.CREDIT_STORE::open);
        bind("Toggles", player -> {
            if(player.isMember()) {
                DonationToggle.openInterface(player);
            } else {
                player.sendMessage("You need to be a donator to open this.");
            }
        });
        bind("Boosters", GameCommands::openBoosters);
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.GAME_NOTICEBOARD;
    }
}
