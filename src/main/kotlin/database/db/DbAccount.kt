package database.db

import database.dao.DaoAccount
import database.sql.SqlAccount
import database.sql.SqlAccount.answer
import database.sql.SqlAccount.id
import database.sql.SqlAccount.password
import database.sql.SqlAccount.pseudo
import database.sql.SqlAccount.question
import database.sql.SqlAccount.roles
import database.sql.SqlAccount.username
import game.account.Account
import game.role.Role
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


/**
 * The `DbAccount` object implements the `DaoAccount` interface, providing database-related
 * operations for user accounts.
 */
object DbAccount : DaoAccount {
    /**
     * Creates a new user `account` in the database.
     * No need to use `resultedValues` or `insertAndGetId` here.
     * The `auto-increment` counter is incremented as soon as a new row is attempted to be inserted,
     * even if the insertion fails due to an error.
     *
     * @param account The account to be created.
     * @return The ID of the newly created account, or `-1` if an error occurs.
     */
    override fun createAccount(account: Account): Int = transaction {
        try {
            addLogger(StdOutSqlLogger)
            SqlAccount.insertIgnore {
                it[username] = account.username()
                it[pseudo] = account.pseudo()
                it[password] = account.password()
                it[question] = account.question()
                it[answer] = account.answer()
                it[roles] = account.roles()
            } get (SqlAccount.id)
        } catch (e: Exception) {
            println(e)
            rollback()
            -1
        }
    }

    /**
     * Retrieves a user `account` from the database by its ID.
     *
     * @param id The ID of the account to retrieve.
     * @return The retrieved account or null if not found.
     */
    override fun readAccount(id: Int): Account? = transaction {
        SqlAccount.select { SqlAccount.id eq id }.mapNotNull { toAccount(it) }.singleOrNull()
    }

    /**
     * Updates an existing user `account` in the database.
     *
     * @param account The updated account information.
     * @return The number of rows updated or `-1` if an error occurs.
     */
    override fun updateAccount(account: Account): Int = transaction {
        try {
            SqlAccount.update({ SqlAccount.id eq account.id() }) {
                it[username] = account.username()
                it[pseudo] = account.pseudo()
                it[password] = account.password()
                it[question] = account.question()
                it[answer] = account.answer()
                it[roles] = account.roles()
            }
        } catch (e: Exception) {
            println(e)
            rollback()
            -1
        }
    }

    /**
     * Deletes a user `account` from the database by its ID.
     *
     * @param id The ID of the account to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    override fun deleteAccount(id: Int): Boolean = transaction {
        SqlAccount.deleteIgnoreWhere { SqlAccount.id eq id } > 0
    }

    /**
     * Retrieves a user `account` from the database by its username.
     *
     * @param username The username of the account to retrieve.
     * @return The retrieved account or null if not found.
     */
    override fun getAccountByUsername(username: String): Account? = transaction {
        SqlAccount.select { SqlAccount.username eq username }.mapNotNull { toAccount(it) }.singleOrNull()
    }

    /**
     * Retrieves all user accounts from the database.
     *
     * @return A list of all user accounts.
     */
    override fun getAllAccounts(): List<Account> = transaction {
        SqlAccount.selectAll().mapNotNull { toAccount(it) }
    }

    /**
     * Retrieves user accounts from the database by their `role` value.
     *
     * @param role The role of the accounts to retrieve.
     * @return A list of user accounts with the specified role.
     */
    override fun getAccountsByRole(role: Role): List<Account> = transaction {
        SqlAccount.select { roles eq role }.map { toAccount(it) }
    }

    /**
     * Converts a database row result into an `Account` object.
     *
     * @param row The database result row.
     * @return An `Account` object created from the row data.
     */
    private fun toAccount(row: ResultRow): Account =
        Account(
            id = row[id],
            username = row[username],
            pseudo = row[pseudo],
            password = row[password],
            question = row[question],
            answer = row[answer],
            roles = row[roles]
        )
}