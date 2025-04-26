package com.zenyte.game.content.preset;

import com.google.common.eventbus.Subscribe;
import com.zenyte.game.model.ui.testinterfaces.PresetManagerInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.events.InitializationEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author Tommeh | 20/04/2020 | 00:32
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class PresetManager {
    private final transient WeakReference<Player> player;
    private final List<Preset> presets;
    private int defaultPreset;
    private int unlockedSlots;

    public PresetManager(@NotNull final Player player) {
        this.player = new WeakReference<>(player);
        this.presets = new ObjectArrayList<>();
    }

    @Subscribe
    public static final void onInitialization(final InitializationEvent event) {
        final Player player = event.getPlayer();
        final Player savedPlayer = event.getSavedPlayer();
        final PresetManager manager = savedPlayer.getPresetManager();
        if (manager == null) {
            return;
        }
        final PresetManager thisManager = player.getPresetManager();
        thisManager.defaultPreset = manager.defaultPreset;
        thisManager.unlockedSlots = manager.unlockedSlots;
        if (manager.presets != null) {
            for (final Preset preset : manager.presets) {
                thisManager.presets.add(new Preset(preset));
            }
        }
    }

    public void revalidatePresets() {
        final int max = getMaximumPresets();
        final int current = getTotalPresets();
        for (int i = 0; i < current; i++) {
            final Preset preset = presets.get(i);
            preset.setAvailable(i < max);
        }
        if (defaultPreset >= max) {
            defaultPreset = 0;
        }
    }

    public void addPreset(final int index, final String name) {
        final Player player = this.player.get();
        if (player == null) {
            return;
        }
        presets.add(index, new Preset(name, player));
        revalidatePresets();
    }

    public void setPreset(final int index, @NotNull final Preset preset) {
        presets.set(index, preset);
    }

    @Nullable
    public Preset getPreset(final int index) {
        if (index < 0 || index >= getTotalPresets()) {
            return null;
        }
        return presets.get(index);
    }

    public int getTotalPresets() {
        return presets.size();
    }

    public int getMaximumPresets() {
        int availablePresets = 4;
        final Player player = this.player.get();
        if (player == null) {
            throw new IllegalStateException();
        }
        //Donator rank slots
        availablePresets += getSlotsAmount(player);
        return availablePresets;
    }

    public static int getSlotsAmount(Player player) {
        switch (player.getMemberRank()) {
            case PREMIUM:
                return 5;
            case EXPANSION:
                return 10;
            case EXTREME:
                return 15;
            case RESPECTED:
                return 20;
            case LEGENDARY:
                return 30;
            case MYTHICAL:
                return 40;
            case UBER:
            case AMASCUT:
                return 50;
        }
        return 0;
    }

    public void setDefaultPreset(final int slot) {
        if (slot < 0 || slot >= getTotalPresets()) {
            return;
        }
        this.defaultPreset = slot;
    }

    public List<Preset> getPresets() {
        return presets;
    }

    public int getDefaultPreset() {
        return defaultPreset;
    }

    public int getUnlockedSlots() {
        return unlockedSlots;
    }

    public void loadLastPreset() {
        final Player player = this.player.get();
        if (player == null) {
            return;
        }

        int index = player.getNumericAttributeOrDefault("last preset loaded", -1).intValue();
        if (index <= -1) {
            player.sendMessage("You haven't loaded a preset yet.");
            return;
        }

        PresetManagerInterface.load(player, index);
    }

}
