package com.example.codechallenge.utils

import com.example.codechallenge.BuildConfig
import java.time.ZoneId
import java.util.Date

fun Date.minusMonths(
    months: Long,
    zoneId: ZoneId = ZoneId.of(BuildConfig.TIMEZONE)
    ): Date {
    val zdt = this.toInstant().atZone(zoneId).minusMonths(months)
    return Date.from(zdt.toInstant())
}