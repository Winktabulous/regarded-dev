package com.zenyte.game.packet.in.event;

import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.packet.in.ClientProtEvent;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.game.world.entity.player.LogLevel;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.renewednpc.ZenyteGuide;
import mgi.types.config.identitykit.IdentityKitDefinitions;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Tommeh | 25-1-2019 | 22:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SetAppearanceEvent implements ClientProtEvent {
    private final int gender;
    private final short[] appearance;
    private final byte[] colours;

    @Override
    public void log(@NotNull final Player player) {
        log(player, "Gender: " + gender + ", appearance: " + Arrays.toString(appearance) + ", colours: " + Arrays.toString(colours));
    }

    @Override
    public LogLevel level() {
        return LogLevel.HIGH_PACKET;
    }

    @Override
    public void handle(Player player) {
        if (!player.getInterfaceHandler().isVisible(269)) {
            return;
        }
        for (int i = 0; i < 7; i++) {
            appearance[i] = (short) validate(player, appearance[i], i);
        }
        player.getAppearance().setAppearance(appearance);
        player.getAppearance().setColours(colours);
        player.getAppearance().setMale(gender == 0);
        player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        player.getInterfaceHandler().closeInterface(InterfacePosition.CENTRAL);
        if (!player.getBooleanAttribute("registered")) {
            ZenyteGuide.finishAppearance(player);
        }
    }

    private int validate(final Player player, final int value, final int index) {
        if (gender == 1 && value == 1000) {
            //1000 doesn't exist in identitykit?
            return value;
        }
        final IdentityKitDefinitions defs = IdentityKitDefinitions.get(value);
        if (defs == null || defs.getModelIds() == null || defs.getBodyPartId() != (index + (gender == 1 ? 7 : 0)) || !defs.isSelectable()) {
            return player.getAppearance().getAppearance()[index];
        }
        return value;
    }

    public SetAppearanceEvent(int gender, short[] appearance, byte[] colours) {
        this.gender = gender;
        this.appearance = appearance;
        this.colours = colours;
    }
}
