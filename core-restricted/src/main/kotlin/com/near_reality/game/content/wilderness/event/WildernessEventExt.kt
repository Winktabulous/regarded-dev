package com.near_reality.game.content.wilderness.event

fun WildernessEvent.isActive() = WildernessEventManager.stateOf(this) is WildernessEvent.State.Active
