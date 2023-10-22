package com.awda.app.domain.common.models

import com.awda.app.data.challenge.models.AppChallenge

/**
 * Created by Abdelrahman Rizq
 */

data class EnableChallenge(
    val challenge: AppChallenge,
    val enabled: Boolean
)
