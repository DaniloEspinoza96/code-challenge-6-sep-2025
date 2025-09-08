package com.example.codechallenge.features.main.data.remote.adapter

import com.example.codechallenge.features.main.data.remote.model.CurrencyEntry
import com.example.codechallenge.features.main.data.remote.model.CurrencyResponse
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader

class CurrencyResponseAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): CurrencyResponse {
        var currency: String? = null
        val entries = mutableListOf<CurrencyEntry>()

        reader.beginObject()
        while (reader.hasNext()) {
            currency = reader.nextName()

            reader.beginArray()
            while (reader.hasNext()) {
                var value: Double? = null
                var date: String? = null

                reader.beginObject()
                while (reader.hasNext()) {
                    when (reader.nextName()) {
                        "Valor" -> {
                            val raw = reader.nextString()
                            value = raw.replace(',', '.').toDouble()
                        }
                        "Fecha" -> date = reader.nextString()
                        else -> reader.skipValue()
                    }
                }
                reader.endObject()

                if (value != null && date != null) {
                    entries.add(CurrencyEntry(value, date))
                }
            }
            reader.endArray()

        }
        reader.endObject()

        return CurrencyResponse(
            currency = currency ?: throw Exception("Error al recibir los datos"),
            entries = entries.toList()
        )
    }
}