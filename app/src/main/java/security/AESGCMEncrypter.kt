package security

import model.Usuario
import javax.crypto.KeyGenerator

fun  AESGCMEncrypter(usuario: Usuario) {
    val namepassword = usuario.nombre + ':' + usuario.password
    val plaintext: ByteArray = namepassword.toByteArray()
    val keygen = KeyGenerator.getInstance("AES_256")
    keygen.init(256)
    val key: SecretKey = keygen.generateKey()
    val cipher = Cipher.getInstance("AES_256/GCM/NoPadding")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    val ciphertext: ByteArray = cipher.doFinal(plaintext)
    val iv: ByteArray = cipher.iv

}