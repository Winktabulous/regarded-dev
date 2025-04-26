package com.zenyte.game.packet.in.event;

import com.near_reality.api.model.Sanction;
import com.near_reality.api.service.sanction.SanctionExtKt;
import com.near_reality.api.service.sanction.SanctionPlayerExtKt;
import com.near_reality.game.packet.out.chat_channel.ChatChannelType;
import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.near_reality.tools.logging.GameLogMessage;
import com.near_reality.tools.logging.GameLogger;
import com.zenyte.game.content.clans.ClanChannel;
import com.zenyte.game.content.clans.ClanManager;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.player.GameCommands;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege;
import com.zenyte.utils.TextUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlinx.datetime.Instant;
import org.jetbrains.annotations.NotNull;
import org.slf4j.event.Level;

/**
 * @author Tommeh | 25-1-2019 | 21:23
 * @author Jire
 */
public class MessagePublicEvent implements ClientProtEvent {

    private final int type;
    private final int colour;
    private final int effect;
    private final String message;
    private final int clanType;

    @Override
    public void handle(Player player) {
        final int effects = ((colour & 255) << 8) | (effect & 255);
        if (player.getPrivilege().eligibleTo(PlayerPrivilege.SUPPORT) && message.startsWith(";;")) {
            GameCommands.process(player, message.substring(2));
            return;
        }
        final Sanction mute = SanctionPlayerExtKt.getMuteSanction(player);
        if (mute != null) {
            player.sendMessage("You cannot talk while the punishment is active:<br>" + SanctionExtKt.format(mute) + ".");
            return;
        }
        switch (type) {
            case 2: {
                final ClanChannel channel = player.getSettings().getChannel();
                if (channel != null) {
                    String clanMessage = message.indexOf('/') == 0
                            ? message.substring(1)
                            : message;
                    clanMessage = TextUtils.censor(clanMessage);
                    ClanManager.message(player, clanMessage);
                    String finalClanMessage = clanMessage;
                    GameLogger.log(Level.INFO, () -> new GameLogMessage.Message.Clan(
                            Instant.Companion.now(),
                            player.getUsername(),
                            finalClanMessage,
                            channel.getPrefix(),
                            channel.getOwner()
                    ));
                    return;
                }
            }
            case 3: {
                if (clanType == ChatChannelType.GIM.getPacketIdentifier()) {
                    Function2<String, String, Unit> function = PlayerAttributesKt.getIronGroupMessageHandler(player);
                    if (function != null) {
                        function.invoke(message, player.getName());
                        return;
                    }
                }
            }
        }
        if (player.getUpdateFlags().get(UpdateFlag.CHAT)) {
            return;
        }
        player.getUpdateFlags().flag(UpdateFlag.CHAT);
        player.getChatMessage().set(message, effects, type == 1);
        GameLogger.log(Level.INFO, () -> new GameLogMessage.Message.Public(
                Instant.Companion.now(),
                player.getUsername(),
                message
        ));
    }

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Type: " + type + ", colour: " + colour + ", effect: " + effect + ", message: " + message);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public MessagePublicEvent(int type, int colour, int effect, String message, int clanType) {
        this.type = type;
        this.colour = colour;
        this.effect = effect;
        this.message = message;
        this.clanType = clanType;
    }
}
