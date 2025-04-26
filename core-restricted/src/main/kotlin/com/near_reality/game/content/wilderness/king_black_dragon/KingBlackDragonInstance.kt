package com.near_reality.game.content.wilderness.king_black_dragon

import com.zenyte.game.content.achievementdiary.diaries.WildernessDiary
import com.zenyte.game.item.Item
import com.zenyte.game.util.Colour
import com.zenyte.game.util.Direction
import com.zenyte.game.world.Position
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.region.DynamicArea
import com.zenyte.game.world.region.area.plugins.LootBroadcastPlugin
import com.zenyte.game.world.region.dynamicregion.AllocatedArea
import java.lang.ref.WeakReference

/**
 * @author Kris | 28/11/2018 17:42
 * @see [Rune-Server profile](https://www.rune-server.ee/members/kris/)
 */
class KingBlackDragonInstance(player: Player, allocatedArea: AllocatedArea?, copiedChunkX: Int, copiedChunkY: Int) :
    DynamicArea(allocatedArea, copiedChunkX, copiedChunkY),
    LootBroadcastPlugin
{
    var kc: Int = 0
        private set

    private val player = WeakReference(player)

    override fun constructed() {
        val kbd = KingBlackDragon(239, getLocation(2270, 4695, 0), Direction.SOUTH, 5)
        kbd.setInstance(this)
        kbd.spawn()
        val p = player.get()
        p?.setLocation(getLocation(insideTile))
    }

    override fun enter(player: Player) {
        player.viewDistance = Player.LARGE_VIEWPORT_RADIUS
        player.achievementDiaries.update(WildernessDiary.ENTER_KING_BLACK_DRAGON_LAIR)
        player.sendMessage(Colour.RED.wrap("Should you die in the instance, your items will be permanently lost!"))
        if (player.getNumericAttribute("king black dragon instance warning").toInt() == 0) {
            player.addAttribute("king black dragon instance warning", 1)
            player.dialogueManager.start(object : Dialogue(player) {
                override fun buildDialogue() {
                    plain(Colour.RED.wrap("WARNING: ") + "If you die in this instance, your items will be permanently lost!")
                }
            })
        }
    }

    override fun leave(player: Player, logout: Boolean) {
        player.resetViewDistance()
    }

    override fun onLoginLocation(): Location {
        return outsideTile
    }

    override fun name(): String {
        return player.get().toString() + "\'s King Black Dragon instance"
    }

    override fun isMultiwayArea(position: Position): Boolean {
        return true
    }

    fun increaseKc() {
        kc++
    }

    companion object {
        @JvmField
        val price: Item = Item(995, 50000)
        val outsideTile: Location = Location(3067, 10253, 0)
        @JvmField
        val insideTile: Location = Location(2271, 4680, 0)
    }
}
