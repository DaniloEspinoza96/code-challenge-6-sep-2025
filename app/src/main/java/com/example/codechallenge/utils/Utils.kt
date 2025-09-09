package com.example.codechallenge.utils

import com.example.codechallenge.BuildConfig
import com.example.codechallenge.features.user.presentation.UiError
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

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

fun localDateFormatted(
    localDate: LocalDate = LocalDate.now(),
    pattern: String = "dd/MM/yyyy"
): String {
    val format = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    return localDate.format(format)
}