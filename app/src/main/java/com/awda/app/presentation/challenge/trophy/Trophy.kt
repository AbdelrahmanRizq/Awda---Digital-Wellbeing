package com.awda.app.presentation.challenge.trophy

import java.util.concurrent.TimeUnit

/**
 * Created by Abdelrahman Rizq
 */

const val TROPHY_INTERVAL = 2

fun trophy(millis: Long, passed: Int, lost: Int): String {
    val index = (TimeUnit.MILLISECONDS.toHours(millis) / TROPHY_INTERVAL).toInt()
        .coerceIn(0, trophies.size - 1)

    val passedWeight = 0.01
    val lostWeight = 0.001

    val stateAdjustedIndex = (index +
            (passed * passedWeight).toInt() -
            (lost * lostWeight).toInt()).coerceIn(0, trophies.size - 1)

    return trophies[stateAdjustedIndex]
}

val trophies = listOf(
    "Beginner",
    "Novice Victor",
    "Rookie Conqueror",
    "Emerging Champion",
    "Promising Warrior",
    "Developing Gladiator",
    "Apprentice Hero",
    "Skillful Vanquisher",
    "Competent Overcomer",
    "Proficient Dominator",
    "Intermediate Master",
    "Capable Triumphator",
    "Advanced Victor",
    "Seasoned Conqueror",
    "Experienced Champion",
    "Adept Warrior",
    "Skilled Gladiator",
    "Expert Hero",
    "Accomplished Vanquisher",
    "Prodigy Overcomer",
    "Elite Dominator",
    "Superior Master",
    "Distinguished Triumphator",
    "Supreme Victor",
    "Grand Conqueror",
    "Majestic Champion",
    "Pinnacle Warrior",
    "Supreme Gladiator",
    "Eminent Hero",
    "Unrivaled Vanquisher",
    "Invincible Overcomer",
    "Supreme Dominator",
    "Grandmaster",
    "Maestro Conqueror",
    "Legendary Champion",
    "Mythical Warrior",
    "Immortal Gladiator",
    "Celestial Hero",
    "Eternal Vanquisher",
    "Divine Overcomer",
    "Virtuoso Master",
    "Magisterial Triumphator",
    "Grand Victor",
    "Exalted Conqueror",
    "Illustrious Champion",
    "Noble Warrior",
    "Royal Gladiator",
    "Sovereign Hero",
    "Paramount Vanquisher",
    "Peerless Overcomer",
    "Grandiose Master",
    "Transcendent Triumphator",
    "Regal Victor",
    "Imposing Conqueror",
    "Prestigious Champion",
    "Renowned Warrior",
    "Aristocratic Gladiator",
    "Splendid Hero",
    "Magnificent Vanquisher",
    "Exceptional Overcomer",
    "Incomparable Master",
    "Stupendous Triumphator",
    "Sovereign Victor",
    "Grand Champion",
    "Exalted Hero",
    "Perfect"
)

