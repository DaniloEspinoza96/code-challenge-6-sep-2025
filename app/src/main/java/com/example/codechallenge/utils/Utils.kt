package com.example.codechallenge.utils

import com.example.codechallenge.BuildConfig
import com.example.codechallenge.features.user.presentation.UiError
import java.time.ZoneId
import java.util.Date

fun Date.minusMonths(
    months: Long,
    zoneId: ZoneId = ZoneId.of(BuildConfig.TIMEZONE)
    ): Date {
    val zdt = this.toInstant().atZone(zoneId).minusMonths(months)
    return Date.from(zdt.toInstant())
}

fun validateEmail(email: String): UiError? {
    if (email.isBlank()) return UiError.REQUIRED
    val r = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    return if (!r.matches(email)) UiError.INVALID else null
}