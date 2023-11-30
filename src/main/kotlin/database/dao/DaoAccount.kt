package database.dao

import game.account.Account
import game.role.Role


/**
 * The `DaoAccount` interface defines a set of data access methods for managing user accounts.
 */
interface DaoAccount {
    /**
     * Create a new user `account` in the data source.
     *
     * @param account The account object representing the user to be created.
     * @return The ID of the newly created user account, or `-1` if an error occurs.
     */
    fun createAccount(account: Account): Int

    /**
     * Retrieve a user `account` by its unique identifier (ID).
     *
     * @param id The unique identifier of the user account to retrieve.
     * @return An instance of [Account] representing the retrieved user account, or null if not found.
     */
    fun readAccount(id: Int): Account?

    /**
     * Update an existing user `account` in the data source.
     *
     * @param account The updated account object.
     * @return The number of affected rows or `-1` if an error occurs.
     */
    fun updateAccount(account: Account): Int

    /**
     * Delete a user account by its unique identifier (ID).
     *
     * @param id The unique identifier of the user account to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    fun deleteAccount(id: Int): Boolean

    /**
     * Retrieve a list of all user accounts stored in the data source.
     *
     * @return A list of [Account] objects representing all user accounts.
     */
    fun getAllAccounts(): List<Account>

    /**
     * Retrieve a user account by its username.
     *
     * @param username The username of the user `account` to retrieve.
     * @return An instance of [Account] representing the retrieved user account, or null if not found.
     */
    fun getAccountByUsername(username: String): Account?

    /**
     * Retrieve a list of user accounts with a specific role.
     *
     * @param role The role to filter user accounts by.
     * @return A list of [Account] objects representing user accounts with the specified role.
     */
    fun getAccountsByRole(role: Role): List<Account>
}