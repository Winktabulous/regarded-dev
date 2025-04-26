package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region13436Spawns : NPCSpawnsScript() {
    init {
        RUNE_DRAGON_8031(3365, 8015, 2, SOUTH, 2)
        RUNE_DRAGON_8031(3368, 8011, 2, SOUTH, 2)
        RUNE_DRAGON_8031(3362, 8010, 2, SOUTH, 2)
        RUNE_DRAGON_8031(3366, 8008, 2, SOUTH, 2)
        RUNE_DRAGON_8031(3363, 8006, 2, SOUTH, 2)
        
        ADAMANT_DRAGON(3367, 8001, 2, SOUTH, 2)
        ADAMANT_DRAGON(3363, 8001, 2, SOUTH, 2)
        ADAMANT_DRAGON(3365, 7997, 2, SOUTH, 2)
        ADAMANT_DRAGON(3367, 7994, 2, SOUTH, 2)
        ADAMANT_DRAGON(3362, 7995, 2, SOUTH, 2)
        
        CARNIVOROUS_CHINCHOMPA(3390, 8031, 0, SOUTH, 3)
        CHINCHOMPA(3390, 8028, 0, SOUTH, 3)
        CARNIVOROUS_CHINCHOMPA(3390, 8027, 0, SOUTH, 3)
        CHINCHOMPA(3387, 8029, 0, SOUTH, 3)
        CARNIVOROUS_CHINCHOMPA(3387, 8033, 0, SOUTH, 3)
        CHINCHOMPA(3390, 8031, 0, SOUTH, 3)
        CARNIVOROUS_CHINCHOMPA(3396, 8028, 0, SOUTH, 3)
        CHINCHOMPA(3395, 8025, 0, SOUTH, 3)
        CARNIVOROUS_CHINCHOMPA(3393, 8023, 0, SOUTH, 3)
        CHINCHOMPA(3390, 8024, 0, SOUTH, 3)
        
        BANKER_10734(3409, 8006, 0, WEST, 0)
        BANKER_10734(3409, 8005, 0, WEST, 0)
        BANKER_10734(3409, 8004, 0, WEST, 0)
        BANKER_10734(3409, 8003, 0, WEST, 0)
        BANKER_10734(3409, 8002, 0, WEST, 0)
        BANKER_10734(3409, 8001, 0, WEST, 0)
        
        FISHING_SPOT_6488(3377, 7990, 0, WEST, 0)
        FISHING_SPOT_6488(3377, 7986, 0, WEST, 0)
        
        16032(3404, 8007, 0, WEST, 1) // RDI Shop
        16051(3403, 8000, 0, WEST, 1) // RDI Mage of Zamorak
        
        16033(3395, 7761, 0, WEST, 0) // UDI Shop
    }
}