package com.near_reality.plugins.item.customs

import com.near_reality.scripts.item.definitions.ItemDefinitionsScript
import com.zenyte.game.world.entity.player.Bonuses
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot
import com.zenyte.game.item.ItemId.*

class RingEditsItems : ItemDefinitionsScript() {
    init {
        TYRANNICAL_RING {
            equipment {
                slot = EquipmentSlot.RING.slot
                bonuses {
                    Bonuses.Bonus.ATT_CRUSH(4)
                    Bonuses.Bonus.DEF_CRUSH(4)
                    Bonuses.Bonus.STRENGTH(4)
                }
            }
        }
        TYRANNICAL_RING_I {
            equipment {
                slot = EquipmentSlot.RING.slot
                bonuses {
                    Bonuses.Bonus.ATT_CRUSH(8)
                    Bonuses.Bonus.DEF_CRUSH(8)
                    Bonuses.Bonus.STRENGTH(8)
                }
            }
        }
        TREASONOUS_RING {
            equipment {
                slot = EquipmentSlot.RING.slot
                bonuses {
                    Bonuses.Bonus.ATT_STAB(4)
                    Bonuses.Bonus.DEF_STAB(4)
                    Bonuses.Bonus.STRENGTH(4)
                }
            }
        }
        TREASONOUS_RING_I {
            equipment {
                slot = EquipmentSlot.RING.slot
                bonuses {
                    Bonuses.Bonus.ATT_STAB(8)
                    Bonuses.Bonus.DEF_STAB(8)
                    Bonuses.Bonus.STRENGTH(8)
                }
            }
        }
        RING_OF_THE_GODS {
            equipment {
                slot = EquipmentSlot.RING.slot
                bonuses {
                    Bonuses.Bonus.DEF_STAB(3)
                    Bonuses.Bonus.DEF_SLASH(3)
                    Bonuses.Bonus.DEF_MAGIC(3)
                    Bonuses.Bonus.DEF_CRUSH(3)
                    Bonuses.Bonus.DEF_RANGE(3)
                    Bonuses.Bonus.PRAYER(4)
                }
            }
        }
        RING_OF_THE_GODS_I {
            equipment {
                slot = EquipmentSlot.RING.slot
                bonuses {
                    Bonuses.Bonus.DEF_STAB(6)
                    Bonuses.Bonus.DEF_SLASH(6)
                    Bonuses.Bonus.DEF_MAGIC(6)
                    Bonuses.Bonus.DEF_CRUSH(6)
                    Bonuses.Bonus.DEF_RANGE(6)
                    Bonuses.Bonus.PRAYER(8)
                }
            }
        }

    }
}

