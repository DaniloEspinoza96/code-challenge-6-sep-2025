package com.example.codechallenge.utils

import com.google.crypto.tink.Aead
import javax.inject.Inject

class TinkManager @Inject constructor(
    private val aead: Aead
) {
    fun encrypt(plainText: String, associatedData: String = ""): ByteArray {
        return aead.encrypt(plainText.toByteArray(Charsets.UTF_8), associatedData.toByteArray())
    }

    fun decrypt(cipherText: ByteArray, associatedData: String = ""): String {
        val decryptedBytes = aead.decrypt(cipherText, associatedData.toByteArray())
        return String(decryptedBytes, Charsets.UTF_8)
    }
}