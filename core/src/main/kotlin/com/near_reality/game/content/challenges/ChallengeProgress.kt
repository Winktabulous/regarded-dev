package com.near_reality.game.content.challenges

class ChallengeProgress {
    var count = 0
        set(count) {
            field = count
            lastUpdate = System.currentTimeMillis()
        }
    var lastUpdate: Long = 0
        private set
}
