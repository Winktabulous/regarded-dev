package com.near_reality.tools.discord
 import dev.kord.common.entity.Snowflake

/**
 * Represents a discord server.
 *
 * @author Stan van der Bend
 */
 sealed class DiscordServer(id: Long) {

    /**
     * The id of this server, or guild in discord terminology.
     */
     val guildId = Snowflake(id)

    /**
     * The [id][Snowflake] of the general chat channel.
     */
     abstract val generalChannelId: Snowflake

    /**
     * The [id][Snowflake] of the broadcast channel.
     */
     abstract val broadcastChannelId: Snowflake

     /**
      * A [DiscordServer] in which logs are send and broadcasts for the beta world.
      */
     data object Staff : DiscordServer(id = 998350045957066834) {

         override val generalChannelId = Snowflake(value = 998350046485561437)
         override val broadcastChannelId = Snowflake(value = 1019645205726625813)

         val economySearchChannelId = Snowflake(1020775683259121724)
         val automatedDetectionChannelId = Snowflake(1219397383171866775)
         val modelChannelId = Snowflake(1020828638054199396)
         val developerRoleId = Snowflake(1020770045170827325)
         val managerRoleId = Snowflake(1020769802505162862)
     }

     /**
      * The main community [DiscordServer].
      */
     object Main : DiscordServer(id = 313252692438941696) {

         override val generalChannelId = Snowflake(value = 556203034116489237)
         override val broadcastChannelId = Snowflake(value = 1023626993180356628)
     }
 }
