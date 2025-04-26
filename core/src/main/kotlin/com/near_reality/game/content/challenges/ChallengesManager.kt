package com.near_reality.game.content.challenges

import kotlin.reflect.KClass

object ChallengesManager {

    val registries = mutableMapOf<KClass<out ChallengeType>, ChallengeRegistry<out ChallengeType>>()

    fun<T : ChallengeType> add(kClass: KClass<T>, registry: ChallengeRegistry<T>) {
        // TODO: maybe check for overlapping structs?
        registries[kClass] = registry
    }

    @Suppress("UNCHECKED_CAST")
    inline fun<reified T : ChallengeType> cashInChallenge(type: T) =
        (registries[type::class] as? ChallengeRegistry<T>)?.cashIn(type)

    fun<T : ChallengeType> get(kClass: KClass<T>, structId: Int): String  {
        for (registry in registries.entries.filter { it.key == kClass }) {
            return registry.value.buildTransmit(structId) ?: continue
        }
        return ""
    }

    fun<T : ChallengeType> checkCompleted(kClass: KClass<T>, structId: Int): Int {
        for (registry in registries.entries.filter { it.key == kClass }) {
            return registry.value.challengeCompleted(structId)
        }
        return 0
    }

}
