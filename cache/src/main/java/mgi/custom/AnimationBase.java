package mgi.custom;

import com.zenyte.CacheManager;
import mgi.tools.jagcached.ArchiveType;
import mgi.tools.jagcached.cache.Archive;
import mgi.tools.jagcached.cache.File;
import mgi.tools.jagcached.cache.Group;
import mgi.utilities.ByteBuffer;
import org.apache.commons.compress.utils.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Kris | 08/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public enum AnimationBase {
    PLAYER(5000, "assets/animations/bases/player base.dat"),
    DICE_GRAPHICS(5001, "assets/animations/bases/dice bag graphics base.dat"),
    TRICK_HALLOWEEN_EMOTE(5002, "assets/animations/bases/trick halloween emote base.dat"),
    THANKSGIVING_TURKEY(5003, "assets/animations/bases/thanksgiving 2019 turkey base.dat"),
    THANKSGIVING_POOF(5004, "assets/animations/bases/thanksgiving 2019 poof base.dat"),
    PLAYER_ALT(5005, "assets/animations/bases/player base.dat"),
    CHRISTMAS_BELLS_SWAY(5006, "assets/christmas/animations/bases/5006 - bells sway base.dat"),
    FROZEN_NPCS_BASE(5007, "assets/christmas/animations/bases/5007 - frozen npcs base.dat"),
    LANTERN_SWAY_BASE(5008, "assets/christmas/animations/bases/5008 - lantern sway base.dat"),
    STAGS_BASE(5009, "assets/christmas/animations/bases/5009 - stags base.dat"),
    FOUNTAIN_BASE(5010, "assets/christmas/animations/bases/5010 - fountain base.dat"),
    GRANDFATHERS_CLOCK_BASE(5011, "assets/christmas/animations/bases/5011 - grandfathers clock base.dat"),
    SCOURGES_BED_BASE(5012, "assets/christmas/animations/bases/5012 - scourges bed base.dat"),
    CANDLE_TABLE_BASE(5013, "assets/christmas/animations/bases/5013 - candle table base.dat"),
    SANTA_BASE(5014, "assets/christmas/animations/bases/5014 - santa base.dat"),
    EBE_BASE(5015, "assets/christmas/animations/bases/Base 5015.dat"),
    IMP_BASE(5016, "assets/christmas/animations/bases/Base 5016.dat"),
    //DO NOT RENAME
    HUMANOID_BASE1(5017, "assets/christmas/animations/bases/Base 5017.dat"),
    HUMANOID_BASE2(5018, "assets/christmas/animations/bases/Base 5018.dat"),
    HUMANOID_BASE3(5019, "assets/christmas/animations/bases/Base 5019.dat"),
    HUMANOID_BASE4(5020, "assets/christmas/animations/bases/Base 5020.dat"),
    HUMANOID_BASE5(5021, "assets/christmas/animations/bases/Base 5021.dat"),
    HUMANOID_BASE6(5022, "assets/christmas/animations/bases/Base 5022.dat"),
    HUMANOID_BASE7(5023, "assets/christmas/animations/bases/Base 5023.dat"),
    HUMANOID_BASE8(5024, "assets/christmas/animations/bases/Base 5024.dat"),
    HUMANOID_BASE9(5025, "assets/christmas/animations/bases/Base 5025.dat"),
    HUMANOID_BASE10(5026, "assets/christmas/animations/bases/Base 5026.dat"),
    HUMANOID_BASE11(5027, "assets/christmas/animations/bases/Base 5027.dat"),
    HUMANOID_BASE12(5028, "assets/christmas/animations/bases/Base 5028.dat"),
    HUMANOID_BASE13(5029, "assets/christmas/animations/bases/Base 5029.dat"),
    HUMANOID_BASE14(5030, "assets/christmas/animations/bases/Base 5030.dat"),
    HUMANOID_BASE15(5031, "assets/christmas/animations/bases/Base 5031.dat"),
    HUMANOID_BASE16(5032, "assets/christmas/animations/bases/Base 5032.dat"),
    HUMANOID_BASE17(5033, "assets/christmas/animations/bases/Base 5033.dat"),
    HUMANOID_BASE18(5034, "assets/christmas/animations/bases/Base 5034.dat"),
    HUMANOID_BASE19(5035, "assets/christmas/animations/bases/Base 5035.dat"),
    HUMANOID_BASE20(5036, "assets/christmas/animations/bases/Base 5036.dat"),
    //DO NOT RENAME
    TINY_THOM_BASE(5037, "assets/christmas/animations/bases/Base 5037.dat"),
    CAMEL_BASE(5038, "assets/christmas/animations/bases/Base 5038.dat"),
    SANTA_NPC_BASE(5039, "assets/christmas/animations/bases/Base 5039.dat"),
    GOBLIN_BASE(5040, "assets/christmas/animations/bases/Base 5040.dat"),
    REGULAR_CHAT_BASE(5041, "assets/christmas/animations/bases/Base 5041.dat"),
    HUMANOID_BASE_21(5042, "assets/christmas/animations/bases/Base 5042.dat"),
    //DO NOT RENAME
    HUMANOID_BASE_22(5043, "assets/christmas/animations/bases/Base 5043.dat"),
    HUMANOID_BASE_23(5044, "assets/christmas/animations/bases/Base 5044.dat"),
    HUMANOID_BASE_24(5045, "assets/christmas/animations/bases/Base 5045.dat"),
    HUMANOID_BASE_25(5046, "assets/christmas/animations/bases/Base 5046.dat"),
    HUMANOID_BASE_26(5047, "assets/christmas/animations/bases/Base 5047.dat"),
    HUMANOID_BASE_27(5048, "assets/christmas/animations/bases/Base 5048.dat"),
    HUMANOID_BASE_28(5049, "assets/christmas/animations/bases/Base 5049.dat"),
    //DO NOT RENAME
    //DO NOT RENAME
    HUMANOID_BASE_29(5050, "assets/christmas/animations/bases/Base 5050.dat"),
    HUMANOID_BASE_30(5051, "assets/christmas/animations/bases/Base 5051.dat"),
    HUMANOID_BASE_31(5052, "assets/christmas/animations/bases/Base 5052.dat"),
    HUMANOID_BASE_32(5053, "assets/christmas/animations/bases/Base 5053.dat"),
    HUMANOID_BASE_33(5054, "assets/christmas/animations/bases/Base 5054.dat"),
    HUMANOID_BASE_34(5055, "assets/christmas/animations/bases/Base 5055.dat"),
    HUMANOID_BASE_35(5056, "assets/christmas/animations/bases/Base 5056.dat"),
    HUMANOID_BASE_36(5057, "assets/christmas/animations/bases/Base 5057.dat"),
    //DO NOT RENAME
    FROZEN_NPC_DEFREEZE_BASE(5058, "assets/christmas/animations/bases/Base 5058.dat"),
    WATER_LANDING_BASE(5059, "assets/christmas/animations/bases/Base 5059.dat"),
    DRAMATIC_POINT_BASE(5060, "assets/christmas/animations/bases/Base 5060.dat"),
    //DO NOT RENAME
    HUMANOID_BASE_37(5061, "assets/christmas/animations/bases/Base 5061.dat"),
    HUMANOID_BASE_38(5062, "assets/christmas/animations/bases/Base 5062.dat"),
    HUMANOID_BASE_39(5063, "assets/christmas/animations/bases/Base 5063.dat"),
    HUMANOID_BASE_40(5064, "assets/christmas/animations/bases/Base 5064.dat"),
    //DO NOT RENAME
    FREEZE_GRAPHICS_BASE(5065, "assets/christmas/animations/bases/Base 5065.dat"),
    SNOWBALL_FLYING_BASE(5066, "assets/christmas/animations/bases/Base 5066.dat"),
    SNOWBALL_IMPACT_BASE(5067, "assets/christmas/animations/bases/Base 5067.dat"),
    //4628, 12660
    IMP_EXPLOSION_BASE(5068, "assets/christmas/animations/bases/Base 5068.dat"),
    SCOURGES_GRAVE_ANIMATION(5069, "assets/christmas/animations/bases/Base 5069.dat"),
    HUMANOID_BASE_41(5070, "assets/christmas/animations/bases/Base 5070.dat"),
    HUMANOID_BASE_42(5071, "assets/christmas/animations/bases/Base 5071.dat"),
    HUMANOID_BASE_43(5072, "assets/christmas/animations/bases/Base 5072.dat"),
    HUMANOID_BASE_44(5073, "assets/christmas/animations/bases/Base 5073.dat"),
    POOF_EXPLOSION(5074, "assets/christmas/animations/bases/Base 5074.dat"),
    FROZEN_OBJECT_BASE(5075, "assets/christmas/animations/bases/Base 5075.dat"),
    FROZEN_NPC_BASE_1(5076, "assets/christmas/animations/bases/Base 5076.dat"),
    FROZEN_NPC_BASE_2(5077, "assets/christmas/animations/bases/Base 5077.dat"),
    FROZEN_NPC_BASE_3(5078, "assets/christmas/animations/bases/Base 5078.dat"),
    FROZEN_NPC_BASE_4(5079, "assets/christmas/animations/bases/Base 5079.dat"),
    FROZEN_NPC_BASE_5(5080, "assets/christmas/animations/bases/Base 5080.dat"),
    FROZEN_NPC_BASE_6(5081, "assets/christmas/animations/bases/Base 5081.dat"),
    FROZEN_NPC_BASE_7(5082, "assets/christmas/animations/bases/Base 5082.dat"),
    FROZEN_NPC_BASE_8(5083, "assets/christmas/animations/bases/Base 5083.dat"),
    FROZEN_NPC_BASE_9(5084, "assets/christmas/animations/bases/Base 5084.dat"),
    FROZEN_NPC_BASE_10(5085, "assets/christmas/animations/bases/Base 5085.dat"),
    PET_BASE_5086(5086, "assets/pets/animation bases/Base 5086.dat"),
    PET_BASE_5087(5087, "assets/pets/animation bases/Base 5087.dat"),
    PET_BASE_5088(5088, "assets/pets/animation bases/Base 5088.dat"),
    PET_BASE_5089(5089, "assets/pets/animation bases/Base 5089.dat"),
    PET_BASE_5090(5090, "assets/pets/animation bases/Base 5090.dat"),
    PET_BASE_5091(5091, "assets/pets/animation bases/Base 5091.dat"),
    PET_BASE_5092(5092, "assets/pets/animation bases/Base 5092.dat"),
    PET_BASE_5093(5093, "assets/pets/animation bases/Base 5093.dat"),
    PET_BASE_5094(5094, "assets/pets/animation bases/Base 5094.dat"),
    PET_BASE_5095(5095, "assets/pets/animation bases/Base 5095.dat"),
    PET_BASE_5096(5096, "assets/pets/animation bases/Base 5096.dat"),
    PET_BASE_5097(5097, "assets/pets/animation bases/Base 5097.dat"),
    PET_BASE_5098(5098, "assets/pets/animation bases/Base 5098.dat"),
    PET_BASE_5099(5099, "assets/pets/animation bases/Base 5099.dat"),
    PET_BASE_5100(5100, "assets/pets/animation bases/Base 5100.dat"),
    PET_BASE_5101(5101, "assets/pets/animation bases/Base 5101.dat"),
    PET_BASE_5102(5102, "assets/pets/animation bases/Base 5102.dat"),
    EASTER_OBJECT_BASE_5103(5103, "assets/easter/animation bases/Base 5103.dat"),
    EASTER_OBJECT_BASE_5104(5104, "assets/easter/animation bases/Base 5104.dat"),
    EASTER_OBJECT_BASE_5105(5105, "assets/easter/animation bases/Base 5105.dat"),
    EASTER_OBJECT_BASE_5106(5106, "assets/easter/animation bases/Base 5106.dat"),
    EASTER_OBJECT_BASE_5107(5107, "assets/easter/animation bases/Base 5107.dat"),
    EASTER_OBJECT_BASE_5108(5108, "assets/easter/animation bases/Base 5108.dat"),
    EASTER_OBJECT_BASE_5109(5109, "assets/easter/animation bases/Base 5109.dat"),
    EASTER_OBJECT_BASE_5110(5110, "assets/easter/animation bases/Base 5110.dat"),
    EASTER_OBJECT_BASE_5111(5111, "assets/easter/animation bases/Base 5111.dat"),
    EASTER_OBJECT_BASE_5112(5112, "assets/easter/animation bases/Base 5112.dat"),
    EASTER_OBJECT_BASE_5113(5113, "assets/easter/animation bases/Base 5113.dat"),
    EASTER_OBJECT_BASE_5114(5114, "assets/easter/animation bases/Base 5114.dat"),
    EASTER_OBJECT_BASE_5115(5115, "assets/easter/animation bases/Base 5115.dat"),
    EASTER_OBJECT_BASE_5116(5116, "assets/easter/animation bases/Base 5116.dat"),
    EASTER_OBJECT_BASE_5117(5117, "assets/easter/animation bases/Base 5117.dat"),
    EASTER_OBJECT_BASE_5118(5118, "assets/easter/animation bases/Base 5118.dat"),
    EASTER_OBJECT_BASE_5119(5119, "assets/easter/animation bases/Base 5119.dat"),
    EASTER_OBJECT_BASE_5120(5120, "assets/easter/animation bases/Base 5120.dat"),
    EASTER_OBJECT_BASE_5121(5121, "assets/easter/animation bases/Base 5121.dat"),
    EASTER_OBJECT_BASE_5122(5122, "assets/easter/animation bases/Base 5122.dat"),
    EASTER_OBJECT_BASE_5123(5123, "assets/easter/animation bases/Base 5123.dat"),
    EASTER_OBJECT_BASE_5124(5124, "assets/easter/animation bases/Base 5124.dat"),
    EASTER_OBJECT_BASE_5125(5125, "assets/easter/animation bases/Base 5125.dat"),
    EASTER_OBJECT_BASE_5126(5126, "assets/easter/animation bases/Base 5126.dat"),
    EASTER_OBJECT_BASE_5127(5127, "assets/easter/animation bases/Base 5127.dat"),
    EASTER_OBJECT_BASE_5128(5128, "assets/easter/animation bases/Base 5128.dat"),
    EASTER_OBJECT_BASE_5129(5129, "assets/easter/animation bases/Base 5129.dat"),
    EASTER_OBJECT_BASE_5130(5130, "assets/easter/animation bases/Base 5130.dat"),
    EASTER_OBJECT_BASE_5131(5131, "assets/easter/animation bases/Base 5131.dat"),
    EASTER_OBJECT_BASE_5132(5132, "assets/easter/animation bases/Base 5132.dat"),
    EASTER_OBJECT_BASE_5133(5133, "assets/easter/animation bases/Base 5133.dat"),
    EASTER_OBJECT_BASE_5134(5134, "assets/easter/animation bases/Base 5134.dat"),
    EASTER_OBJECT_BASE_5135(5135, "assets/easter/animation bases/Base 5135.dat"),
    EASTER_OBJECT_BASE_5136(5136, "assets/easter/animation bases/Base 5136.dat"),
    EASTER_OBJECT_BASE_5137(5137, "assets/easter/animation bases/Base 5137.dat"),
    EASTER_OBJECT_BASE_5138(5138, "assets/easter/animation bases/Base 5138.dat"),
    EASTER_OBJECT_BASE_5139(5139, "assets/easter/animation bases/Base 5139.dat"),
    EASTER_OBJECT_BASE_5140(5140, "assets/easter/animation bases/Base 5140.dat"),
    EASTER_OBJECT_BASE_5141(5141, "assets/easter/animation bases/Base 5141.dat"),
    EASTER_OBJECT_BASE_5142(5142, "assets/easter/animation bases/Base 5142.dat"),
    EASTER_OBJECT_BASE_5143(5143, "assets/easter/animation bases/Base 5143.dat"),
    EASTER_OBJECT_BASE_5144(5144, "assets/easter/animation bases/Base 5144.dat"),
    EASTER_OBJECT_BASE_5145(5145, "assets/easter/animation bases/Base 5145.dat"),
    EASTER_OBJECT_BASE_5146(5146, "assets/easter/animation bases/Base 5146.dat"),
    EASTER_OBJECT_BASE_5147(5147, "assets/easter/animation bases/Base 5147.dat"),
    EASTER_OBJECT_BASE_5148(5148, "assets/easter/animation bases/Base 5148.dat"),
    EASTER_OBJECT_BASE_5149(5149, "assets/easter/animation bases/Base 5149.dat"),
    EASTER_OBJECT_BASE_5150(5150, "assets/easter/animation bases/Base 5150.dat"),
    EASTER_OBJECT_BASE_5151(5151, "assets/easter/animation bases/Base 5151.dat"),
    EASTER_OBJECT_BASE_5152(5152, "assets/easter/animation bases/Base 5152.dat"),
    EASTER_OBJECT_BASE_5153(5153, "assets/easter/animation bases/Base 5153.dat"),
    EASTER_OBJECT_BASE_5154(5154, "assets/easter/animation bases/Base 5154.dat"),
    EASTER_OBJECT_BASE_5155(5155, "assets/easter/animation bases/Base 5155.dat"),
    EASTER_OBJECT_BASE_5156(5156, "assets/easter/animation bases/Base 5156.dat"),
    EASTER_OBJECT_BASE_5157(5157, "assets/easter/animation bases/Base 5157.dat"),
    EASTER_OBJECT_BASE_5158(5158, "assets/easter/animation bases/Base 5158.dat"),
    EASTER_OBJECT_BASE_5159(5159, "assets/easter/animation bases/Base 5159.dat"),
    EASTER_OBJECT_BASE_5160(5160, "assets/easter/animation bases/Base 5160.dat"),
    EASTER_OBJECT_BASE_5161(5161, "assets/easter/animation bases/Base 5161.dat"),
    EASTER_OBJECT_BASE_5162(5162, "assets/easter/animation bases/Base 5162.dat"),
    EASTER_OBJECT_BASE_5163(5163, "assets/easter/animation bases/Base 5163.dat"),
    EASTER_OBJECT_BASE_5164(5164, "assets/easter/animation bases/Base 5164.dat"),
    EASTER_OBJECT_BASE_5165(5165, "assets/easter/animation bases/Base 5165.dat"),
    EASTER_OBJECT_BASE_5166(5166, "assets/easter/animation bases/Base 5166.dat"),
    EASTER_OBJECT_BASE_5167(5167, "assets/easter/animation bases/Base 5167.dat"),
    EASTER_OBJECT_BASE_5168(5168, "assets/easter/animation bases/Base 5168.dat"),
    EASTER_OBJECT_BASE_5169(5169, "assets/easter/animation bases/Base 5169.dat"),
    EASTER_OBJECT_BASE_5170(5170, "assets/easter/animation bases/Base 5170.dat"),
    EASTER_OBJECT_BASE_5171(5171, "assets/easter/animation bases/Base 5171.dat"),
    EASTER_OBJECT_BASE_5172(5172, "assets/easter/animation bases/Base 5172.dat"),
    EASTER_OBJECT_BASE_5173(5173, "assets/easter/animation bases/Base 5173.dat"),
    EASTER_OBJECT_BASE_5174(5174, "assets/easter/animation bases/Base 5174.dat"),
    EASTER_OBJECT_BASE_5175(5175, "assets/easter/animation bases/Base 5175.dat"),
    EASTER_OBJECT_BASE_5176(5176, "assets/easter/animation bases/Base 5176.dat"),
    EASTER_OBJECT_BASE_5177(5177, "assets/easter/animation bases/Base 5177.dat"),
    EASTER_OBJECT_BASE_5178(5178, "assets/easter/animation bases/Base 5178.dat"),
    EASTER_OBJECT_BASE_5179(5179, "assets/easter/animation bases/Base 5179.dat"),
    EASTER_OBJECT_BASE_5180(5180, "assets/easter/animation bases/Base 5180.dat"),
    EASTER_OBJECT_BASE_5181(5181, "assets/easter/animation bases/Base 5181.dat"),
    EASTER_OBJECT_BASE_5182(5182, "assets/easter/animation bases/Base 5182.dat"),
    EASTER_OBJECT_BASE_5183(5183, "assets/easter/animation bases/Base 5183.dat"),
    EASTER_OBJECT_BASE_5184(5184, "assets/easter/animation bases/Base 5184.dat"),
    EASTER_OBJECT_BASE_5185(5185, "assets/easter/animation bases/Base 5185.dat"),
    EASTER_OBJECT_BASE_5186(5186, "assets/easter/animation bases/Base 5186.dat"),
    TELEPORT_BASE_5187(5187, "assets/teleportation/animations/bases/Base 5187.dat"),
    TELEPORT_BASE_5188(5188, "assets/teleportation/animations/bases/Base 5188.dat"),
    TELEPORT_BASE_5189(5189, "assets/teleportation/animations/bases/Base 5189.dat"),
    TELEPORT_BASE_5190(5190, "assets/teleportation/animations/bases/Base 5190.dat"),
    TRICK_BASE_5191(5191, "assets/halloween/animations/bases/Base 5191.dat"),
    TRICK_BASE_5192(5192, "assets/halloween/animations/bases/Base 5192.dat"),
    DICE_BASE_5193(5193, "assets/dice bag/animations/bases/Base 5193.dat"),
    DICE_BASE_5194(5194, "assets/dice bag/animations/bases/Base 5194.dat"),
    DICE_BASE_5195(5195, "assets/dice bag/animations/bases/Base 5195.dat"),
    DICE_BASE_5196(5196, "assets/dice bag/animations/bases/Base 5196.dat"),
    DICE_BASE_5197(5197, "assets/dice bag/animations/bases/Base 5197.dat"),

    KORASI_GRAPHICS(32766, "assets/korasi/animations/bases/korasi_graphics.dat"),
    NEW_HUMAN(32767, "assets/korasi/animations/bases/new_human.dat"),
    ;
    private final int baseId;
    private final String path;

    /**
     * Packs all defined animation bases into the cache. Does not actually write on its own though.
     *
     * @throws IOException the exception thrown if a necessary data file is missing.
     */
    public static final void pack() throws IOException {
        for (final AnimationBase value : values()) {
            pack(value.baseId, IOUtils.toByteArray(new FileInputStream(value.path)));
        }
    }

    public static void pack(int baseID, byte[] bytes) {
        Archive archive = CacheManager.getCache().getArchive(ArchiveType.BASES);
        archive.addGroup(new Group(baseID,
                new File(new ByteBuffer(bytes))));
    }

    AnimationBase(int baseId, String path) {
        this.baseId = baseId;
        this.path = path;
    }

    public int getBaseId() {
        return baseId;
    }

    public String getPath() {
        return path;
    }
}
