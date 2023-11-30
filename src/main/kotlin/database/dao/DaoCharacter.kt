package database.dao

import game.character.Character


/**
 * Interface defining operations for interacting with character data in the database.
 */
interface DaoCharacter {
    /**
     * Creates a new character in the database.
     *
     * @param character The character to be created.
     * @return The ID of the created [Character].
     */
    fun createCharacter(character: Character): Int

    /**
     * Retrieves a character from the database based on the provided ID.
     *
     * @param id The ID of the character to retrieve.
     * @return The retrieved character or null if not found.
     */
    fun readCharacter(id: Int): Character?

    /**
     * Updates an existing character in the database.
     *
     * @param character The updated character information.
     * @return The ID of the updated character.
     */
    fun updateCharacter(character: Character): Int

    /**
     * Deletes a character from the database based on the provided ID.
     *
     * @param id The ID of the character to delete.
     * @return `true` if the deletion was successful, `false` otherwise.
     */
    fun deleteCharacter(id: Int): Boolean

    /**
     * Retrieves all characters associated with a specific account from the database.
     *
     * @param account The account for which to retrieve characters.
     * @return A list of characters associated with the given account.
     */
    fun getAllCharactersFromAccount(account: game.account.Account): List<Character>

    /**
     * Retrieves all characters from the database.
     *
     * @return A list of all characters in the database.
     */
    fun getAllCharacters(): List<Character>
}