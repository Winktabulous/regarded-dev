package com.near_reality.tools.discord.community

import com.near_reality.game.world.entity.player.killingBlowHit
import com.near_reality.tools.discord.DiscordServer
import com.near_reality.tools.discord.congratulate
import com.near_reality.tools.discord.itemInventoryImageUrl
import com.near_reality.tools.discord.pity
import com.zenyte.CacheManager
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Utils
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.SpecialAttack
import com.zenyte.game.world.entity.player.privilege.GameMode
import com.zenyte.game.world.region.GlobalAreaManager
import com.zenyte.game.world.region.area.wilderness.WildernessArea
import dev.kord.common.Color
import dev.kord.core.behavior.channel.createEmbed
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import kotlinx.coroutines.launch
import mgi.tools.jagcached.cache.Cache
import mgi.types.config.enums.EnumDefinitions

fun DiscordCommunityBot.onKill(attacker: Player, victim: Player) {

    val attackerName = "**${attacker.name}**"
    val victimName = "**${victim.name}**"
    val lastHit = victim.killingBlowHit
    val killedWeapon = lastHit?.weapon as? Item
    val area = GlobalAreaManager.getArea(victim)
    scope.launch {

        broadcastChannel.run {
            createEmbed {
                title = "$attackerName killed $victimName"
                color = Color(136, 8, 8)
                author {
                    name = "Death"
                    icon = "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/e9e9064a-af38-4ab4-f81f-d7691fe24b00/public"
                }
                if (killedWeapon != null)
                    thumbnail { url = itemInventoryImageUrl(killedWeapon.id) }
                description = buildString {
                    if (lastHit != null) {
                        append(attackerName)
                        append(" hit a ${lastHit.damage} with ")
                        if (killedWeapon != null) {
                            val name = killedWeapon.name
                            append("${Utils.getAOrAn(name)} $name")
                            if (lastHit.isSpecial) {
                                val specialAttack = SpecialAttack.SPECIAL_ATTACKS[killedWeapon.id]
                                if (specialAttack != null) {
                                    append(" using its special attack **${specialAttack.specialAttackName}**")
                                }
                            }
                        }
                        append("!")
                    } else
                        append("Did you just die without getting hit $victimName?")
                    append(congratulate(attacker))
                    append(pity(victim))
                }
                val areaName = area?.name()
                if (areaName != null) {
                    footer {
                        if (area is WildernessArea)
                            icon = "https://imagedelivery.net/nzzTKI-8_OClTAbISD7cGQ/e8be8c8f-aee3-4d5f-e3d0-72586652d300/public"
                        text = areaName
                    }
                }
            }
        }
    }
}

fun main() {
    CacheManager.loadCache(Cache.openCache("base/cache/data/cache", true))
    CacheManager.loadDefinitions()
    EnumDefinitions().load()

    DiscordCommunityBot.init(
        DiscordServer.Main,
        token = "MTAxOTY0MTYxNTE4ODUwODgxNg.GoD34n.27s6pG87WITl8rN9r1S25a1z76BNyWo28trlIg"
    )
    val stan = mockPlayer("Stan", 319978682016071681L)
    val jacmob = mockPlayer("Jacmob", 451982134320562196L)

    val hit = spyk(Hit(stan, 70, HitType.MELEE, 0))
    hit.setSpecialAttack()
    hit.weapon = Item(ItemId.ARMADYL_GODSWORD)

    jacmob.killingBlowHit = hit

    mockkStatic(GlobalAreaManager::class)
    every { GlobalAreaManager.getArea(any()) } returns WildernessArea()

    DiscordCommunityBot.onKill(stan, jacmob)
}

private fun mockPlayer(username: String, discordUserId: Long): Player {
    val player = mockk<Player>()
    every { player.name } returns username
    every { player.position } returns Location(0, 0, 0)
    every { player.getNumericAttribute(any()) } returns 1
    every { player.notificationSettings.getKillcount(any()) } returns 0
    every { player.gameMode } returns GameMode.REGULAR
    every { player.combatXPRate } returns 250
    every { player.skillingXPRate } returns 80
    every { player.appearance.gender } returns "Man"
    every { player.skills.totalLevel } returns 420
    every { player.combatLevel } returns 69

    val attributes = mutableMapOf<String, Any?>()
    attributes["discordUserId"] = discordUserId
    every { player.attributes } returns attributes

    val tempAttributes = mutableMapOf<Any, Any?>()
    every { player.temporaryAttributes } returns tempAttributes
    return player
}
