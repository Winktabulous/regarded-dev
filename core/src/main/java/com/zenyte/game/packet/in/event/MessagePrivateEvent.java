package com.zenyte.game.packet.in.event;

import com.near_reality.api.model.Sanction;
import com.near_reality.api.service.sanction.SanctionExtKt;
import com.near_reality.api.service.sanction.SanctionPlayerExtKt;
import com.near_reality.tools.logging.GameLogMessage;
import com.near_reality.tools.logging.GameLogger;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.masks.ChatMessage;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import kotlinx.datetime.Instant;
import org.jetbrains.annotations.NotNull;
import org.slf4j.event.Level;

/**
 * @author Tommeh | 25-1-2019 | 21:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MessagePrivateEvent implements ClientProtEvent {

    private static final int MAX_RECIPIENT_LENGTH = 12;

    private final String recipient;
    private final ChatMessage message;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Recipient: " + recipient + ", message: " + message.getChatText());
        GameLogger.log(Level.INFO, () -> new GameLogMessage.Message.Private(
                Instant.Companion.now(),
                player.getUsername(),
                recipient,
                message.getChatText()
        ));
    }

    @Override
    public void handle(Player player) {
        final Sanction mute = SanctionPlayerExtKt.getMuteSanction(player);
        if (mute != null)
            player.sendMessage("You cannot talk while the punishment is active:<br>" + SanctionExtKt.format(mute) + ".");
        else
            player.getSocialManager().sendMessage(recipient, message);
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    public MessagePrivateEvent(String recipient, ChatMessage message) {
        this.recipient = recipient.length() > MAX_RECIPIENT_LENGTH
                ? recipient.substring(0, MAX_RECIPIENT_LENGTH)
                : recipient;
        this.message = message;
    }

    @Override
    public void close() throws Exception {
        message.close();
    }

}
