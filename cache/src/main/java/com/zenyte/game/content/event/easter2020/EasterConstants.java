package com.zenyte.game.content.event.easter2020;

import com.zenyte.game.world.entity.Location;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

/**
 * @author Corey
 * @since 25/03/2020
 */
public class EasterConstants {
    
    public static final int WARREN_ENTRANCE = 46251;
    public static final int WARREN_EXIT = 46254;
    public static final int EASTER_FACTORY_ENTRANCE = 46255;
    public static final int EASTER_FACTORY_EXIT = 46256;
    public static final int NOTICEBOARD = 46257;
    public static final int WATER_TANK = 46261;
    public static final int WATER_BOWL = 46405;
    
    public static final int HUMAN_TO_BUNNY_ANIM = 15189;
    public static final int BUNNY_TO_HUMAN_ANIM = 15190;
    
    public static final int HUMAN_TO_BUNNY_GFX = 2510;
    
    public static final int PLAYER_BUNNY_NPC = 15171;
    
    public static final Location rabbitHoleStart = new Location(2227, 4298);
    
    public static final int SAD_EASTER_BUNNY = 15197;
    public static final int HAPPY_EASTER_BUNNY = 15213;
    public static final int EASTER_BUNNY_OUTSIDE_CUTSCENE = 15179;
    public static final int EASTER_BUNNY_JR_CUTSCENE = 15180;
    public static final int POST_QUEST_EASTER_BUNNY = 15198;
    public static final int EASTER_BUNNY_CUTSCENE = 15182;
    public static final int EASTER_BUNNY_JR = 15181;
    public static final int EASTER_BUNNY_JR_CHAIR = 15199;
    public static final int EASTER_BIRD_NPC = 15187;
    public static final int EASTER_BUNNY_JR_NEXT_TO_BIRD = 15200;
    public static final int EASTER_BUNNY_JR_NEXT_TO_BIRD_VIS = 15180;
    public static final int EASTER_BUNNY_JR_NEXT_TO_EGGPAINTING_MACHINE = 15203;
    public static final int EASTER_BUNNY_JR_NEXT_TO_COAL = 15201;
    public static final int EASTER_BUNNY_JR_NEXT_TO_NUT_MACHINE = 15202;
    public static final int EASTER_BUNNY_JR_ON_RED_CARPET = 15204;
    public static final int BIG_BEN = 15186;
    public static final int BENY = 15184;
    public static final int FLUFFY = 15183;
    public static final int BUBBLES = 15185;
    
    public static final int IMPLING_WORKER_EAST_NEAR_EGGPAINTING_MACHINE = 15193;
    public static final int IMPLING_WORKER_MIDDLE_NEAR_EGGPAINTING_MACHINE = 15211;
    public static final int IMPLING_WORKER_WEST_NEAR_EGGPAINTING_MACHINE = 15212;
    
    public static final int IMPLING_MANAGER = 15195;
    
    public static final int GENERAL_EVENT_VARBIT = 15050;
    public static final int EASTER_BIRD_SEED_BOWL_VARBIT = 15062;
    public static final int EASTER_BIRD_WATER_BOWL_VARBIT = 15063;
    public static final int INCUBATOR_VARBIT = 15052;
    public static final int COAL_SUPPLY_VARBIT = 15054;
    public static final int WATER_TANK_VARBIT = 15053;
    public static final int INCUBATOR_CONTROLS_VARBIT = 15055;
    
    public static final int RAT_ID = 15172;
    public static final int SNAIL_ID = 15176;
    
    public static final int IMPLING_INCUBATOR_WORKER = 15196;
    public static final int INCUBATOR = 15229;
    public static final int COAL_SUPPLY = 46410;
    public static final int INCUBATOR_WATER_TANK = 46409;
    public static final int INCUBATOR_CONTROLS = 46408;

    public static final int EXPRESSION_SAD = 15184;
    public static final int EXPRESSION_HAPPY = 15194;
    public static final int EXPRESSION_VERY_HAPPY = 15193;
    public static final int EXPRESSION_NORMAL = 15242;
    public static final int EXPRESSION_SAY_NO = 15226;

    public static final int BIRD_EXPRESSION_HAPPY = 15160;
    public static final int BIRD_EXPRESSION_CHATTY = 15161;
    public static final int BIRD_EXPRESSION_DRUNK = 15162;
    public static final int BIRD_EXPRESSION_SAD = 15181;
    public static final int BIRD_EXPRESSION_SAY_NO = 15195;
    public static final int BIRD_EXPRESSION_SNORING = 15230;

    public static final int EGG_PAINTING_MACHINE_BASE_EAST = 15238;
    public static final int EGG_PAINTING_MACHINE_BASE_WEST = 15234;
    public static final int EGG_PAINTING_MACHINE_EAST = 15240;
    public static final int EGG_PAINTING_MACHINE_WEST = 15236;

    public static final int NUT_MACHINE_START = 15251;
    public static final int NUT_MACHINE_BASE = 15254;
    public static final int NUT_MACHINE = 15257;
    public static final int NUT_MACHINE_WORKING = 15259;

    public enum EasterItem {
        SUNFLOWER_SEEDS(30117),
        CRACKER_BITS(30118),
        WORM_BITS(30119),
        POPPY_SEEDS(30120),
        CHIMNEY(30121),
        COG(30122),
        PISTONS(30123),
        WET_PIPE(30124),
        CLEAN_PIPE(30125),
        SOOTY_PIPE(30126),
        INCUBATOR_BLUEPRINTS(30127),
        IMPLING_JAR(30129),
        IMPLING_NET(30128),
        EASTER_IMPLING_JAR(30130),
        CHOCATRICE_CAPE(30116),
        EASTER_CARROT(30131);

        private static final EasterItem[] values = values();
        public static final IntList items = new IntArrayList(values.length);

        static {
            for (EasterItem item : values) {
                if (item == CHOCATRICE_CAPE || item == EASTER_CARROT) {
                    continue;
                }
                items.add(item.getItemId());
            }
        }

        private final int itemId;

        EasterItem(int itemId) {
            this.itemId = itemId;
        }

        public int getItemId() {
            return itemId;
        }

    }
    
}
