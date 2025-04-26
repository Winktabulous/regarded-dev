package com.zenyte.game.world.entity.player;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingVariables;
import com.zenyte.game.model.ui.testinterfaces.advancedsettings.SettingsInterface;
import com.zenyte.game.util.AccessMask;
import mgi.types.config.items.ItemDefinitions;
import mgi.utilities.StringFormatUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author Tommeh | 19 feb. 2018 : 20:18:54
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 * profile</a>}
 */
public class NotificationSettings {
    public static final int INTERFACE = 492;
    public static final ImmutableList<String> BOSS_NPC_NAMES = ImmutableList.of("araxxor", "tormented demon", "duke sucellus", "vardorvis", "phantom muspah", "deranged archaeologist", "abyssal sire", "vanstrom klause", "sarachnis", "general graardor", "kree'arra", "commander zilyana", "k'ril tsutsaroth", "callisto", "venenatis", "scorpia", "vet'ion", "chaos elemental", "chaos fanatic", "crazy archaeologist", "cerberus", "vorkath", "zulrah", "giant mole", "thermonuclear smoke devil", "king black dragon", "dagannoth rex", "dagannoth supreme", "dagannoth prime", "skotizo", "barrows", "kraken", "grotesque guardians", "hespori", "obor", "bryophyta", "corporeal beast", "tztok-jad", "alchemical hydra", "tzkal-zuk", "mimic", "nex", "artio", "calvar'ion", "spindel");
    public static final ImmutableList<String> SLAYER_NPC_NAMES = ImmutableList.of("crawling hand", "cave bug", "cave crawler", "banshee", "cave slime", "rockslug", "desert lizard", "cockatrice", "pyrefiend", "mogre", "harpie bug swarm", "wall beast", "killerwatt", "molanisk", "basilisk", "sea snake", "terror dog", "fever spider", "infernal mage", "brine rat", "bloodveld", "jellie", "turoth", "mutated zygomite", "cave horror", "aberrant spectre", "spiritual ranger", "dust devil", "spiritual warrior", "kurask", "skeletal wyvern", "gargoyle", "nechryael", "spiritual mage", "abyssal demon", "cave kraken", "dark beast", "smoke devil", "tortured gorilla", "demonic gorilla", "adamant dragon", "rune dragon", "superior creature", "brutal black dragon", "fossil island wyvern", "revenant", "hydra", "wyrm", "drake");
    public static final ImmutableList<String> EXTRA_TRACKED_NPC_NAMES = ImmutableList.of("ganodermic beast", "lizardman", "lizardman shaman", "lizardman brute");
    private final transient Player player;
    private static final ImmutableMap<String, String> slayerRedirections = ImmutableMap.<String, String>builder().put("crushing hand", "crawling hand").put("chasm crawler", "cave crawler").put("screaming banshee", "banshee").put("screaming twisted banshee", "banshee").put("giant rockslug", "rock slug").put("cockathrice", "cockatrice").put("flaming pyrelord", "pyrefiend").put("infernal pyrelord", "pyrelord").put("monstrous basilisk", "basilisk").put("malevolent mage", "infernal mage").put("insatiable bloodveld", "bloodveld").put("insatiable mutated bloodveld", "bloodveld").put("vitreous jelly", "jelly").put("vitreous warped jelly", "warped jelly").put("cave abomination", "cave horror").put("abhorrent spectre", "aberrant spectre").put("repugnant spectre", "aberrant spectre").put("basilisk sentinel", "basilisk knight").put("choke devil", "dust devil").put("king kurask", "kurask").put("marble gargoyle", "gargoyle").put("nechryarch", "nechryael").put("greater nechryael", "nechryael").put("greater abyssal demon", "abyssal demon").put("night beast", "dark beast").put("nuclear smoke devil", "smoke devil").put("deviant spectre", "aberrant spectre").put("mutated bloodveld", "bloodveld").put("twisted banshee", "banshee").build();
    /**
     * A list of pairings which will show up in the slayer killcount when you slaughtered the given boss.
     * So for example, if someone killed 100 kraken bosses, the slayer log should show cave krakens as quantity 100, plus whatever cave krakens they killed..
     */
    private static final Map<String, String> bossToSlayerInclusions = Map.of("cave kraken", "kraken", "smoke devil", "thermonuclear smoke devil", "gargoyle", "grotesque guardians", "hydra", "alchemical hydra");

    public NotificationSettings(final Player player) {
        this.player = player;
    }

    public static boolean isKillcountTracked(@NotNull final String source) {
        final String lowercase = source.toLowerCase();
        final String name = slayerRedirections.getOrDefault(lowercase, lowercase);
        return BOSS_NPC_NAMES.contains(name) || SLAYER_NPC_NAMES.contains(name) || EXTRA_TRACKED_NPC_NAMES.contains(name);
    }

    public void increaseKill(final String requestedName) {
        final String lowercaseName = requestedName.toLowerCase();
        final String name = slayerRedirections.getOrDefault(lowercaseName, lowercaseName);
        int kills = (player.getSettings().getKillsLog().getOrDefault(name, 0) & 65535) + 1;
        int streak = ((player.getSettings().getKillsLog().getOrDefault(name, 0) >> 16) & 65535) + 1;
        if (kills > 65535) {
            kills = 65535;
        }
        if (streak > 65535) {
            streak = 65535;
        }
        final int packed = kills & 65535 | (streak & 65535) << 16;
        player.getSettings().getKillsLog().put(name, packed);
    }

    public boolean shouldNotifyRareDrop(@NotNull final Item item) {
        if (!item.isTradable()) {
            return false;
        }
        final int value = item.getSellPrice();
        final int threshold = player.getVarManager().getBitValue(SettingsInterface.MINIMUM_DROP_ITEM_VALUE_VARBIT_ID);
        final boolean enabled = player.getVarManager().getBitValue(SettingVariables.LOOT_DROP_NOTIFICATIONS_VARBIT_ID) == 1;
        return enabled && threshold != 0 && value >= threshold;
    }

    public void sendDropNotification(final Item item) {
        final int value = item.getSellPrice();
        final int threshold = player.getVarManager().getBitValue(SettingsInterface.MINIMUM_DROP_ITEM_VALUE_VARBIT_ID);
        final ItemDefinitions defs = item.getDefinitions();
        if (defs == null) return;
        if (!item.isTradable()) {
            if (player.getVarManager().getBitValue(SettingVariables.LOOT_DROP_NOTIFICATIONS_VARBIT_ID) == 1 &&
                    player.getVarManager().getBitValue(SettingVariables.UNTRADEABLE_LOOT_NOTIFICATIONS_VARBIT_ID) == 1) {
                player.sendMessage("<col=ef1020>Untradeable drop: " + defs.getName() + "</col>");
                return;
            }
        }
        if (player.getVarManager().getBitValue(SettingVariables.LOOT_DROP_NOTIFICATIONS_VARBIT_ID) != 1 || threshold == 0 || value < threshold) {
            return;
        }
        final int amount = item.getAmount();
        if (amount == 1) {
            player.sendMessage("<col=ef1020>Valuable drop: " + defs.getName() + " (" + StringFormatUtil.format(value) + " coins)</col>");
        } else {
            player.sendMessage("<col=ef1020>Valuable drop: " + amount + " x " + defs.getName() + " (" + StringFormatUtil.format(value * amount) + " coins)</col>");
        }
    }

    public void sendBossKillCountNotification(final String name) {
        final int kills = player.getSettings().getKillsLog().getOrDefault(name.toLowerCase(), 0) & 65535;
        player.sendMessage("Your " + StringFormatUtil.formatString(name) + " kill count is: <col=FF0000>" + kills + "</col>.");
    }

    public int getKillcount(final String name) {
        final String lowercase = name.toLowerCase();
        final String lowercaseName = slayerRedirections.getOrDefault(lowercase, lowercase);
        final int defaultKills = player.getSettings().getKillsLog().getOrDefault(lowercaseName, 0) & 65535;
        int extraKills = 0;
        final String bossName = bossToSlayerInclusions.get(lowercaseName);
        if (bossName != null) {
            extraKills = player.getSettings().getKillsLog().getOrDefault(bossName, 0) & 65535;
        }
        return Math.min(65535, defaultKills + extraKills);
    }

    public int getKillstreak(final String name) {
        final String lowercaseName = name.toLowerCase();
        final int defaultKills = (player.getSettings().getKillsLog().getOrDefault(lowercaseName, 0) >> 16) & 65535;
        int extraKills = 0;
        final String bossName = bossToSlayerInclusions.get(lowercaseName);
        if (bossName != null) {
            extraKills = player.getSettings().getKillsLog().getOrDefault(bossName, 0) & 65535;
        }
        return Math.min(65535, defaultKills + extraKills);
    }

    public void sendKillLog(final ImmutableList<String> names, final boolean sendInterface) {
        if (sendInterface) {
            player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 549);
        }
        final StringBuilder[] builders = new StringBuilder[] {new StringBuilder(), new StringBuilder(), new StringBuilder()};
        final String type = names.size() >= 42 ? "Slayer" : "Boss";
        for (final String name : names) {
            final int kills = getKillcount(name);
            final int streaks = getKillstreak(name);
            final String n = name.equals("tztok-jad") ? "TzTok-Jad" : name.equals("tzkal-zuk") ? "TzKal-Zuk" : StringFormatUtil.formatString(name);
            builders[0].append(n).append(type.equals("Slayer") ? "s|" : "|");
            builders[1].append(kills).append("|");
            builders[2].append(streaks).append("|");
        }
        player.getPacketDispatcher().sendComponentSettings(549, 16, 0, names.size(), AccessMask.CLICK_OP1);
        player.getPacketDispatcher().sendClientScript(1584, builders[0].toString(), builders[1].toString(), builders[2].toString(), names.size(), type + " Kill Log");
        player.getTemporaryAttributes().put("KillLogType", type);
    }
}
