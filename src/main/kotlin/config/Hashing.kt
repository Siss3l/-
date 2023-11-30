package config


/**
 * Utility object for hashing, checking, encoding and decoding passwords.
 */
object Hashing {
    /**
     * Hashes a password using secure `Argon2id` algorithm who summarizes
     * the state of the art in the design of memory-hard methods.
     *
     * @param password The password to hash.
     * @return The hashed password as a string.
     */
    internal fun hash(password: String): String {
        return de.mkammerer.argon2.Argon2Factory.create(de.mkammerer.argon2.Argon2Factory.Argon2Types.ARGON2id)
            .hash(4, 65536, 8, password.toByteArray())
    }

    /**
     * Checks if a given `hash` matches a password.
     *
     * @param hash The hash to verify.
     * @param password The password to check against the hash.
     * @return `true` if the password matches the hash, `false` otherwise.
     */
    internal fun check(hash: String, password: String): Boolean {
        return de.mkammerer.argon2.Argon2Factory.create(de.mkammerer.argon2.Argon2Factory.Argon2Types.ARGON2id)
            .verify(hash, password.toByteArray())
    }

    /**
     * Decodes a password using a key.
     * The `#1` prefix is for default encoding and
     * the `#2` prefix for `MD5` hashing algorithm usage.
     *
     * @param passwd The encoded password to decode.
     * @param key The key used for decoding.
     * @return The decoded password as a string.
     */
    fun decodePassword(passwd: String, key: String): String {
        require(passwd.isNotEmpty() && key.isNotEmpty()) { return "" }
        require(key.length * 2 >= passwd.length - 2) { return "" }
        var pwd = ""
        val (charset, password) = (('a'..'z') + ('A'..'Z') + ('0'..'9') + '-' + '_'
                to passwd.removePrefix("#1"))
        for (i in password.indices step 2) {
            pwd += ((charset.indexOf(password[i]) + charset.size - key[i / 2].code + 64) % 64 * 16 +
                    (charset.indexOf(password[i + 1]) + charset.size - key[i / 2].code + 64) % 64).toChar()
        }
        return pwd
    }

    /**
     * Encodes a password using a key.
     *
     * @param password The password to encode.
     * @param key The key used for encoding.
     * @return The encoded password as a string.
     */
    fun encodePassword(password: String, key: String): String {
        require(password.isNotEmpty() && key.isNotEmpty()) { return "" }
        val charset: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') + '-' + '_'
        return password.mapIndexed { k, v ->
            "${charset[(v.code / 16 + key[k % key.length].code) % charset.size]}" +
                    "${charset[(v.code % 16 + key[k % key.length].code) % charset.size]}"
        }.joinToString("")
    }
}