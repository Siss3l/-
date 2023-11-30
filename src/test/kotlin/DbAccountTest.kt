import config.Hashing
import database.db.DbAccount.createAccount
import database.db.DbAccount.deleteAccount
import database.db.DbAccount.getAccountByUsername
import database.db.DbAccount.getAccountsByRole
import database.db.DbAccount.getAllAccounts
import database.db.DbAccount.readAccount
import database.db.DbAccount.updateAccount
import database.db.DbFactory.closePool
import database.db.DbFactory.init
import database.db.DbFactory.shut
import game.account.Account
import game.role.Role
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID.randomUUID


/**
 * This class contains unit tests for the `DbAccount` utility.
 * The `account` should be attached to the `SessionContext`.
 * Certain values must be unique.
 */
internal class DbAccountTest {
    private val passwordHash = Hashing.hash("test")
    private val pseudo = "${randomUUID()}"
    private val username = "test"

    init {
        init()
    }

    /**
     * Unit test for creating a new `account` in the database.
     */
    @Test
    fun testCreate() {
        val account = Account(
            id = -1,
            username = username,
            pseudo = pseudo.dropLast(4),
            password = passwordHash,
            question = "Yes?",
            answer = "No",
            roles = Role.Player
        )
        assertTrue(createAccount(account) >= 0, "Account creation error")
        shut()
        closePool()
    }

    /**
     * Unit test for reading an `account` from the database.
     */
    @Test
    fun testRead() {
        val user: Account? = getAccountByUsername(username)
        val read: Account? = user?.let { readAccount(it.id()) }
        assertAll("User",
            { assertEquals(username, user?.username(), "User does not exists") },
            { assertEquals(username, read?.username(), "User is not found") }
        )
        shut()
        closePool()
    }

    /**
     * Unit test for reading all user accounts from the database.
     */
    @Test
    fun testReadAll() {
        val accList = getAllAccounts()
        println(accList)
        assertFalse(accList.isEmpty(), "There is no accounts")
        shut()
        closePool()
    }

    /**
     * Unit test for updating an `account` in the database.
     */
    @Test
    fun testUpdate() {
        val account: Account? = getAccountByUsername(username)
        assertEquals(username, account?.username(), "User does not exists")
        if (account != null) {
            account.setPseudo("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
            account.setUsername("null")
            account.setPassword("null")
            assertTrue(updateAccount(account) >= 0, "Account updating error")
        }
        shut()
        closePool()
    }

    /**
     * Unit test for deleting an `account` from the database.
     */
    @Test
    fun testDelete() {
        val account: Account? = getAccountByUsername(username)
        assertEquals(username, account?.username(), "User does not exists")
        if (account != null) {
            assertTrue(deleteAccount(account.id()), "Account deletion error")
        }
        shut()
        closePool()
    }

    /**
     * Unit test for retrieving `accounts` by `role` value.
     */
    @Test
    fun testRole() {
        val role = getAccountsByRole(Role.Player)
        assertEquals(false, role.isEmpty(), "Role(s) not found")
        shut()
        closePool()
    }
}