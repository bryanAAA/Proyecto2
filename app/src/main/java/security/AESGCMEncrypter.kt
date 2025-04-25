package security

import model.Usuario
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec

class authentication() {
    fun AESGCMEncrypter(value: String): ByteArray {
        val plaintext: ByteArray = value.toByteArray()
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        val password = "mySecretPassword"
        val salt = ByteArray(16)
        val key = getKeyFromPassword(password, salt)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        val iv: ByteArray = cipher.iv
        return iv + ciphertext
    }

    fun AESGCMDecrypter(ciphertextwithiv: ByteArray, key: SecretKey): String {
        try {
            val iv = ciphertextwithiv.take(12).toByteArray()
            val ciphertext = ciphertextwithiv.copyOfRange(12, ciphertextwithiv.size)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val gcmSpec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec)
            val decryptedData = cipher.doFinal(ciphertext)
            return String(decryptedData)
        } catch (e: Exception) {
            e.printStackTrace()
            return "Decryption failed: ${e.message}"
        }
    }

    fun getKeyFromPassword(password: String, salt: ByteArray): SecretKey {
        val spec = PBEKeySpec(password.toCharArray(), salt, 10000, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        return factory.generateSecret(spec)
    }
}