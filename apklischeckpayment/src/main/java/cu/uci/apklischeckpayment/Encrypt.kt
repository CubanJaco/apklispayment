package cu.uci.apklischeckpayment

import android.util.Base64
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.spec.X509EncodedKeySpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

private val publicKey: String =
    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtDkUQSp0ujvsr8kSFOIy\n" +
            "HU7H3Z3bDQwngeZDflDoMCkLkFjcInuKdFiZGBe6UJMQEvCmnv2oJfB5cetM0OQc\n" +
            "1nLZsXF5WyhPC1iOdFaT8B4Y5kRQOTVOmnyGulIzFZ6MgltsBSn6Nat0XJjCLNO8\n" +
            "oK9TGV2+FzTtM/ktWPKRIm+ozs+q53D6Wn8AG2ObVVrY8t9/RZEUJmFyNr86AXqz\n" +
            "YrG5bHDAOJMYBfoMs5vZuQXLjQrhE5mpGesbr31zJvg5E8DwYUMzj1Vy9XxSXQ1c\n" +
            "lPfIW8GFKCMaabDWOWnx0xzlsdt39Pfn6li0OFy4xZi/aSQ37E0d8JuT1HnfOB/9\n" +
            "cwIDAQAB"

@Throws(
    NoSuchAlgorithmException::class,
    NoSuchPaddingException::class,
    InvalidKeyException::class,
    IllegalBlockSizeException::class,
    BadPaddingException::class
)
fun decryptRSA(encrypted: String): String {
    return decryptRSA(toByteArray(encrypted))
}

@Throws(
    NoSuchAlgorithmException::class,
    NoSuchPaddingException::class,
    InvalidKeyException::class,
    IllegalBlockSizeException::class,
    BadPaddingException::class
)
private fun decryptRSA(encryptedBytes: ByteArray): String {

    //generar la clave publica dado solamente el string de la misma
    val keyFac = KeyFactory.getInstance("RSA")
    val keySpec = X509EncodedKeySpec(
        Base64.decode(
            publicKey.trim { it <= ' ' }.toByteArray(),
            Base64.DEFAULT
        )
    )
    val key = keyFac.generatePublic(keySpec)

    //desencriptar
    val cipher1 = Cipher.getInstance("RSA")
    cipher1.init(Cipher.DECRYPT_MODE, key)
    val decryptedBytes = cipher1.doFinal(encryptedBytes)
    return String(decryptedBytes)
}

private fun toByteArray(value: String): ByteArray {
    return Base64.decode(value, Base64.DEFAULT)
}