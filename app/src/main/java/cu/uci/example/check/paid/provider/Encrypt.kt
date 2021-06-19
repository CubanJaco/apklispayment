package cu.uci.example.check.paid.provider

import android.util.Base64
import java.security.InvalidKeyException
import java.security.KeyFactory
import java.security.NoSuchAlgorithmException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

/**
 * Created by osvel on 2/3/19.
 *
 * La clave privada se genera con:
 * openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048
 *
 * La clve publica se exporta de la privada con:
 * openssl rsa -pubout -in private_key.pem -out public_key.pem
 *
 */
private val publicKey: String =
    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtDkUQSp0ujvsr8kSFOIy\n" +
            "HU7H3Z3bDQwngeZDflDoMCkLkFjcInuKdFiZGBe6UJMQEvCmnv2oJfB5cetM0OQc\n" +
            "1nLZsXF5WyhPC1iOdFaT8B4Y5kRQOTVOmnyGulIzFZ6MgltsBSn6Nat0XJjCLNO8\n" +
            "oK9TGV2+FzTtM/ktWPKRIm+ozs+q53D6Wn8AG2ObVVrY8t9/RZEUJmFyNr86AXqz\n" +
            "YrG5bHDAOJMYBfoMs5vZuQXLjQrhE5mpGesbr31zJvg5E8DwYUMzj1Vy9XxSXQ1c\n" +
            "lPfIW8GFKCMaabDWOWnx0xzlsdt39Pfn6li0OFy4xZi/aSQ37E0d8JuT1HnfOB/9\n" +
            "cwIDAQAB"

//TODO: Este valor estara solamente en la app del servidor
//asi si alguien decompila la app de Datos Cuba no podra obtener la clave de encriptado
private val privateKey: String =
    "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC0ORRBKnS6O+yv\n" +
            "yRIU4jIdTsfdndsNDCeB5kN+UOgwKQuQWNwie4p0WJkYF7pQkxAS8Kae/agl8Hlx\n" +
            "60zQ5BzWctmxcXlbKE8LWI50VpPwHhjmRFA5NU6afIa6UjMVnoyCW2wFKfo1q3Rc\n" +
            "mMIs07ygr1MZXb4XNO0z+S1Y8pEib6jOz6rncPpafwAbY5tVWtjy339FkRQmYXI2\n" +
            "vzoBerNisblscMA4kxgF+gyzm9m5BcuNCuETmakZ6xuvfXMm+DkTwPBhQzOPVXL1\n" +
            "fFJdDVyU98hbwYUoIxppsNY5afHTHOWx23f09+fqWLQ4XLjFmL9pJDfsTR3wm5PU\n" +
            "ed84H/1zAgMBAAECggEBAI6u+hg1ssCXHOZ3PHeINcD6VEZi6cBEtSqk0R66k3k9\n" +
            "pcgccfxi7tT6KArpDjsok3tLR79sd6nB34SPk/t1/lbSCwPvIPNI0HvdGruEqA9c\n" +
            "5L25rAH+8EJjAI+W8JbXNcsm8jiMaWoAWlnNHz46TIazCAl3LD5XSszxJDPdp4Yo\n" +
            "qM3vAFxSXWd+c8DPsKqzROk4jsaB6Y3/VoiSqtukKMfMQ6EYHSO+QUHkhLMHem6Z\n" +
            "+siftnl+UEPCj2FQDcNI4T7jpvcG+94cqH76C6urfq1GI9sgzPyQiBPDtePfc4y6\n" +
            "o5+oKQUtIO+RnChMu9N+tGby+0N4AJ9OOevGOFVysrECgYEA2NxiPsIWITUCTFtW\n" +
            "JOQXAW+zNj8QjtvXwGjPcDPNjhBU9yRLSbLcjUZL/ToOqbr6HkBdb1xXBsiLxV1S\n" +
            "jxyPCkWylBgMRVy7siSPD6wRRhJdbvRQqTAI7oMpnfXEdGR41Ja28w8eNcq++gix\n" +
            "SwdYITwZVjcGHac7SQrLJazhkU0CgYEA1L/qSbh10/8DaTjyEn/jrqM55x8wFoka\n" +
            "6AI11Sjjn664PHK8qnoXf3gZoFOHsZr4k3Fkol4zGnUywj7ecxjkw65s54v15bdA\n" +
            "K6B5Ka2Lg7aSkV4uf3EpoZKI3pFv/Vt3gxPXcEH5rT7MvDgr2PIYW5mFql4KNIo7\n" +
            "oJQPbbYsab8CgYBU02euirajSJvciEGa7ok9K5cbGfH9H2s2PQkfJ3Q5JJRHk6/L\n" +
            "ZSY2MJAavjHsHFInsR+bJrB328woaEeZ6R7ecsLWgM2T+CEHHpvDEpgzI43ej7xZ\n" +
            "YYus2CtYOUEeEgrdhRKM4pX7jpHDNdE7uuSzI1H50k4XHUnoWUt7MjONZQKBgBLb\n" +
            "g/fO2trUosfD3WkYXuUO4MYEtK16r6Da+UyQmESCJyBshx6RPd31lkfov4FVnCzt\n" +
            "gx8FPIBe3QXsrqkDDtvtbQfFFyGqcYysZ7DzCge9uhYahAKV1DvXEjywJX4OinRa\n" +
            "4OC5aFkb1OfmDrTnGJOd8wQb28m2avybwxZVbHBfAoGBAMEHGeeiuaaNwBOmesXW\n" +
            "n9IsxI8kcoRCOfB1lbhC7QRzt3+rUUjJkn7CEATZ1pPoBCCIgV4BBxEmGgty8H8J\n" +
            "M+z6xjuWXezXAcO1N0WtrKgu7kqHcgPxU21U3W6TgJtAigGxvACovv1VDQ+Zds2g\n" +
            "+dQdQ8hRuHvVrp3ezobzDJ8D"

@Throws(
    NoSuchAlgorithmException::class,
    NoSuchPaddingException::class,
    InvalidKeyException::class,
    IllegalBlockSizeException::class,
    BadPaddingException::class
)
fun encryptRSA(clearText: String): String {

    //generar la clave privada dado solamente el string de la misma
    val keyFac = KeyFactory.getInstance("RSA")
    val keySpec = PKCS8EncodedKeySpec(
        Base64.decode(
            privateKey.trim { it <= ' ' }.toByteArray(),
            Base64.DEFAULT
        )
    )
    val key = keyFac.generatePrivate(keySpec)

    //encriptar
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    return toString(cipher.doFinal(clearText.toByteArray())).replace("\n", "")
}

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

private fun toString(value: ByteArray): String {
    return String(Base64.encode(value, Base64.DEFAULT))
}

private fun toByteArray(value: String): ByteArray {
    return Base64.decode(value, Base64.DEFAULT)
}