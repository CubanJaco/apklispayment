package cu.uci.example.check.paid.provider

import kotlin.random.Random

object Util {

    val USERNAME_VALID_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"

    fun randomString(length: Int, acceptedChars: String): String {

        var output = ""

        for (i in 0..length) {
            val position = Random.nextInt(0, acceptedChars.length)
            output += acceptedChars[position]
        }

        return output

    }

}