package com.zenyte.game.world.entity.player.privilege;

import com.zenyte.game.world.entity.player.Player;

public enum ExpConfigurations {

    REGULAR(GameMode.REGULAR, new ExpConfiguration[]{
            new ExpConfiguration(150, 80, 0),
            new ExpConfiguration(80, 50, 0),
            new ExpConfiguration(10, 10, 5),
            new ExpConfiguration(5, 5, 8),
    }),
    IRONMAN(GameMode.STANDARD_IRON_MAN, new ExpConfiguration[]{
            new ExpConfiguration(80, 80, 0),
            new ExpConfiguration(20, 20, 2),
            new ExpConfiguration(10, 10, 5),
            new ExpConfiguration(5, 5, 10),
    }),
    HARD_CORE(GameMode.HARDCORE_IRON_MAN, new ExpConfiguration[]{
            new ExpConfiguration(80, 80, 5),
            new ExpConfiguration(20, 20, 7),
            new ExpConfiguration(10, 10, 10),
            new ExpConfiguration(5, 5, 17),
    }),
    ULTIMATE(GameMode.ULTIMATE_IRON_MAN, new ExpConfiguration[]{
            new ExpConfiguration(80, 80, 0),
            new ExpConfiguration(20, 20, 2),
            new ExpConfiguration(10, 10, 5),
            new ExpConfiguration(5, 5, 10),
    }),
    GROUP(GameMode.GROUP_IRON_MAN, new ExpConfiguration[]{
        new ExpConfiguration(20, 20, 0)
    }),
    ;

    public static final ExpConfigurations[] VALUES = values();
    private final GameMode gameMode;
    private final ExpConfiguration[] configurations;

    ExpConfigurations(GameMode gameMode, ExpConfiguration[] configurations) {
        this.gameMode = gameMode;
        this.configurations = configurations;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public ExpConfiguration[] getConfigurations() {
        return configurations;
    }

    public ExpConfiguration[] getEasier(int end) {
        if(end == 0)
            return null;
        ExpConfiguration[] config = new ExpConfiguration[end];
            System.arraycopy(configurations, 0, config, 0, end);
        return config;
    }

    public static ExpConfigurations of(GameMode gameMode) {
        for (ExpConfigurations value : VALUES) {
            if(value.gameMode == gameMode)
                return value;
        }
        return REGULAR;
    }

    public int getExpConfigurationIndex(int combat, int skilling) {
        for (int i = 0; i < configurations.length; i++) {
            if(configurations[i].matches(combat, skilling))
                return i;
        }
        return 0;
    }

    public static ExpConfiguration getForPlayer(Player player) {
        ExpConfigurations config = ExpConfigurations.of(player.getGameMode());
        int currentIndex = config.getExpConfigurationIndex(player.getCombatXPRate(), player.getSkillingXPRate());
        ExpConfiguration[] configurations = ExpConfigurations.of(player.getGameMode()).getConfigurations();
        return configurations[currentIndex];
    }
}
