package com.abhishek101.core.models

import com.abhishek101.core.db.Authentication
import kotlinx.datetime.Clock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime

@Serializable
data class AuthenticationRemoteEntity(
    @SerialName("accessToken") val accessToken: String,
    @SerialName("expiresIn") val expiresIn: Long
)

@ExperimentalTime
fun AuthenticationRemoteEntity.toAuthentication(clock: Clock = Clock.System): Authentication =
    Authentication(
        accessToken = this.accessToken,
        expiresBy = clock.now().epochSeconds + this.expiresIn
    )
