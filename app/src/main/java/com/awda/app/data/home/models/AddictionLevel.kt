package com.awda.app.data.home.models

/**
 * Created by Abdelrahman Rizq
 */

enum class AddictionLevel(val value: Int) {
    UNAVAILABLE(-1),
    ADDICTED(0),
    OBSESSED(1),
    DEPENDENT(2),
    HABITUAL(3),
    ACHIEVER(4),
    CHAMPION(5)
}