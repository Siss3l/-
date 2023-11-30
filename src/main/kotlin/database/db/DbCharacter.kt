package database.db

import database.dao.DaoCharacter
import database.sql.SqlAccount
import database.sql.SqlCharacter
import database.sql.SqlCharacter.accountId
import database.sql.SqlCharacter.colors
import database.sql.SqlCharacter.date
import database.sql.SqlCharacter.gender
import database.sql.SqlCharacter.gfxId
import database.sql.SqlCharacter.hat
import database.sql.SqlCharacter.id
import database.sql.SqlCharacter.level
import database.sql.SqlCharacter.name
import game.account.Account
import game.character.Character
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction


/**
 * Database `Character` Data Access Object.
 *
 * This object provides `CRUD` operations for managing character data and implements
 * the `DaoCharacter` interface to provide methods that interacts with the database.
 */
object DbCharacter : DaoCharacter {
    /**
     * Create a new character in the database.
     *
     * @param character The character object to be created.
     * @return The ID of the newly created character or `-1` if an error occurs.
     */
    override fun createCharacter(character: Character): Int = transaction {
        try {
            addLogger(StdOutSqlLogger)
            SqlCharacter.insertIgnore {
                it[accountId] = character.accountId()
                it[level] = character.level()
                it[name] = character.name()
                it[gender] = character.gender()
                it[colors] = character.colors()
                it[gfxId] = character.gfxId()
                it[hat] = character.hat()
                it[date] = character.date()
            } get (SqlCharacter.id)
        } catch (e: Exception) {
            println(e)
            rollback()
            -1
        }
    }

    /**
     * Read a character from the database based on its ID.
     *
     * @param id The ID of the character to be read.
     * @return The Character object if found, or null if not found.
     */
    override fun readCharacter(id: Int): Character? = transaction {
        SqlCharacter.select { SqlCharacter.id eq id }.mapNotNull {
            toCharacter(it)
        }.singleOrNull()
    }

    /**
     * Update an existing character in the database.
     *
     * @param character The updated character object.
     * @return The ID of the updated character or `-1` if an error occurs.
     */
    override fun updateCharacter(character: Character): Int = transaction {
        try {
            SqlCharacter.update({ SqlCharacter.id eq character.id() }) {
                it[level] = character.level()
                it[accountId] = character.accountId()
                it[name] = character.name()
                it[gender] = character.gender()
                it[colors] = character.colors()
                it[gfxId] = character.gfxId()
                it[hat] = character.hat()
                it[date] = character.date()
            }
        } catch (e: Exception) {
            println(e)
            rollback()
            -1
        }
    }

    /**
     * Delete a character from the database based on its ID.
     *
     * @param id The ID of the character to be deleted.
     * @return True if the character is successfully deleted, false otherwise.
     */
    override fun deleteCharacter(id: Int): Boolean = transaction {
        addLogger(StdOutSqlLogger)
        SqlCharacter.deleteIgnoreWhere { SqlCharacter.id eq id } > 0
    }

    /**
     * Get a list of all characters in the database.
     *
     * @return A list of Character objects representing all characters in the database.
     */
    override fun getAllCharacters(): List<Character> = transaction {
        SqlCharacter.selectAll().mapNotNull {
            toCharacter(it)
        }
    }

    /**
     * Get a list of all characters associated with a specific account.
     *
     * @param account The account for which characters are retrieved.
     * @return A list of Character objects associated with the given account.
     */
    override fun getAllCharactersFromAccount(account: Account): List<Character> = transaction {
        addLogger(StdOutSqlLogger)
        SqlCharacter.innerJoin(SqlAccount)
            .slice(SqlCharacter.columns)
            .selectAll()
            .groupBy(date)
            .andWhere { SqlAccount.id eq account.id() }
            .mapNotNull { toCharacter(it) }
    }

    /**
     * Convert a database row result to a Character object.
     *
     * @param row The database result row.
     * @return A Character object created from the database row.
     */
    private fun toCharacter(row: ResultRow): Character =
        Character(
            id = row[id],
            accountId = row[accountId],
            level = row[level],
            name = row[name],
            gender = row[gender],
            colors = row[colors],
            gfxId = row[gfxId],
            hat = row[hat],
            date = row[date]
        )
}