import database.db.DbAccount.getAccountByUsername
import database.db.DbCharacter.createCharacter
import database.db.DbCharacter.getAllCharacters
import database.db.DbCharacter.getAllCharactersFromAccount
import database.db.DbCharacter.readCharacter
import database.db.DbCharacter.updateCharacter
import database.db.DbFactory.closePool
import database.db.DbFactory.init
import database.db.DbFactory.shut
import game.account.Account
import game.character.Character
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


/**
 * This class contains unit tests for the `DbCharacter` utility.
 */
internal class DbCharacterTest {
    /**
     * Test characters
     */
    private val id = 1
    private val packet = "AATest|1|0|16247229|16237423|16775644|15129804|16772016|1|8"
    private val username = "test"

    init {
        init()
    }

    @Test
    fun testCreate() {
        val user: Account? = getAccountByUsername(username)
        val character = Character.invoke(packet, user?.id())
        assertNotNull(character, "Character is null")
        if (character != null) {
            val chr = createCharacter(character)
            assertTrue(chr >= 0, "Character creation error")
        }
        shut()
        closePool()
    }

    @Test
    fun testRead() {
        val user: Account? = getAccountByUsername(username)
        val character = user?.let { getAllCharactersFromAccount(it) }
        val c = character?.joinToString(separator = "|")
        println("ALK${character?.size ?: "E"}|$c")
        assertAll(
            "Character",
            { assertEquals(username, user?.username(), "User does not exists") },
            { assertNotNull(character, "Characters do not exists") },
        )
        shut()
        closePool()
    }

    @Test
    fun testUpdate() {
        val character: Character? = readCharacter(id)
        assertNotNull(character, "Character is null")
        if (character != null) {
            character.setName("null")
            character.setColors("e6dccc;e6dccc;e6dccc;e6dccc;e6dccc")
            character.setHat(9)
            assertTrue(updateCharacter(character) >= 0, "Character updating error")
        }
        shut()
        closePool()
    }

    @Test
    fun testDelete() {
        val character: Character? = readCharacter(id)
        assertNotNull(character, "Character is null")

    }

    @Test
    fun testReadAll() {
        val charList = getAllCharacters()
        println(charList)
        assertFalse(charList.isEmpty(), "There is no character.")
    }
}