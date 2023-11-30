import config.Hashing
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


/**
 * This class contains unit tests for the `Hashing` utility.
 * Other comparative measures of assertion can also be considered.
 */
internal class HashingTest {
    private val key = (1..32).map { ('a'..'z').toList().random() }.shuffled().joinToString("")
    private val password = "test"

    /**
     * Unit test for the `Hashing` object.
     */
    @Test
    fun testHash() {
        val k = Hashing.encodePassword(password, key)
        val v = Hashing.decodePassword(k, key)
        val hash = Hashing.hash(v)
        val good = Hashing.check(hash, v)
        Assertions.assertAll(
            "Password",
            { Assertions.assertEquals(32, key.length, "The length of the key is not 32") },
            { Assertions.assertEquals(false, k.isEmpty(), "The encoded password is empty") },
            { Assertions.assertEquals(password, v, "The encoded password is not $password") },
            { Assertions.assertEquals(false, hash.isEmpty(), "The hashed password is empty") },
            { Assertions.assertEquals(true, good, "The hash is not corresponding") },
        )
    }
}