//package com.near_reality.game.content.wilderness
//
//import com.google.common.eventbus.Subscribe
//import com.zenyte.game.world.World
//import com.zenyte.game.world.entity.Location
//import com.zenyte.game.world.`object`.ObjectId
//import com.zenyte.game.world.`object`.WorldObject
//import com.zenyte.plugins.events.ServerLaunchEvent
//
//object WildernessObjectsUpdate {
//
//    private val objs = mutableListOf(
//        WorldObject(ObjectId.GEM_ROCK, 10, 0, Location(3009, 3594, 0)),
//        WorldObject(ObjectId.GEM_ROCK, 10, 0, Location(3009, 3595, 0)),
//        WorldObject(ObjectId.GEM_ROCK, 10, 0, Location(3011, 3598, 0)),
//        WorldObject(ObjectId.GEM_ROCK, 10, 0, Location(3012, 3599, 0)),
//        WorldObject(ObjectId.GEM_ROCK, 10, 0, Location(3013, 3598, 0)),
//        WorldObject(ObjectId.GEM_ROCK, 10, 0, Location(3016, 3601, 0)),
//        WorldObject(ObjectId.GEM_ROCK, 10, 0, Location(3017, 3601, 0)),
//        WorldObject(ObjectId.GEM_ROCK, 10, 0, Location(3025, 3594, 0)),
//        WorldObject(ObjectId.GEM_ROCK, 10, 0, Location(3020, 3585, 0)),
//        WorldObject(ObjectId.GEM_ROCK, 10, 0, Location(3021, 3584, 0)),
//        WorldObject(ObjectId.GEM_ROCK, 10, 0, Location(3017, 3591, 0)),
//        WorldObject(ObjectId.GEM_ROCK, 10, 0, Location(3018, 3591, 0)),
//    )
//
//
//    @JvmStatic
//    @Subscribe
//    fun onServerLaunch(@Suppress("UNUSED_PARAMETER") event: ServerLaunchEvent) {
//        for(obj in objs) {
//            World.spawnObject(obj)
//        }
//    }
//}