package com.near_reality.tools.discord
 import dev.kord.common.entity.Snowflake

 sealed class DiscordEmoji(id: Long) {

     protected open val name: String = this::class.simpleName!!

     val id = Snowflake(id)

     object Owner : DiscordEmoji(998380209566912542)
     object Administrator : DiscordEmoji(998380210607116288)
     object ServerSupport : DiscordEmoji(998380211395625051)
     object SeniorMod : DiscordEmoji(998380212704256030)
     object Mod : DiscordEmoji(998380213996101642)
     object Manager : DiscordEmoji(998380215401201694)
     object Dev : DiscordEmoji(998380216663674930)
     object Uber : DiscordEmoji(998380217703878758)
     object Respected : DiscordEmoji(998380219142508667)
     object Premium : DiscordEmoji(998380220283359304)
     object Mythical : DiscordEmoji(998380221411643462)
     object Legendary : DiscordEmoji(998380222539907102)
     object Extreme : DiscordEmoji(998380223764627496)
     object Expansion : DiscordEmoji(998380224867737710)
     object YT : DiscordEmoji(998380225987616828)

     override fun toString() = "<:$name:${id.value}>"
 }
