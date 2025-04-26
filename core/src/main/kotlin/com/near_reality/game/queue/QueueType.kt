package com.near_reality.game.queue

sealed class QueueType {

    object Weak : QueueType()

    object Normal : QueueType()

    object Strong : QueueType()

}