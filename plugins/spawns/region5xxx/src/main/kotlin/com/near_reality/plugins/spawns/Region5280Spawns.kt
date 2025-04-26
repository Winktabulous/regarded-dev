package com.near_reality.plugins.spawns

import com.near_reality.scripts.npc.spawns.NPCSpawnsScript
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.util.Direction.*

class Region5280Spawns : NPCSpawnsScript() {
    init {
        HYDRA(1302, 10240, 0, SOUTH, 8)
        HYDRA(1304, 10260, 0, SOUTH, 8)
        HYDRA(1304, 10267, 0, SOUTH, 8)
        HYDRA(1305, 10247, 0, SOUTH, 8)
        HYDRA(1309, 10266, 0, SOUTH, 8)
        HYDRA(1310, 10261, 0, SOUTH, 8)
        HYDRA(1311, 10246, 0, SOUTH, 8)
        HYDRA(1311, 10271, 0, SOUTH, 8)
        TARFOL_QUO_MATEN(1314, 10259, 0, SOUTH, 5)
        HYDRA(1316, 10240, 0, SOUTH, 8)
        HYDRA(1317, 10245, 0, SOUTH, 8)
        HYDRA(1324, 10267, 0, SOUTH, 8)
        HYDRA(1326, 10261, 0, SOUTH, 8)
        HYDRA(1329, 10267, 0, SOUTH, 8)
        DRAKE_8612(1301, 10240, 1, SOUTH, 6)
        DRAKE_8612(1308, 10253, 1, SOUTH, 6)
        DRAKE_8612(1309, 10246, 1, SOUTH, 6)
        DRAKE_8612(1319, 10241, 1, SOUTH, 6)
        KORMAR_QUO_MATEN(1328, 10240, 1, SOUTH, 0)
        DRAKE_8612(1341, 10243, 1, SOUTH, 6)
    }
}