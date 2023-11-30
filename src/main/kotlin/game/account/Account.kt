package game.account

import game.role.Role


/**
 * The `Account` class represents user account information.
 *
 * @property id The unique identifier for the account.
 * @property username The username associated with the account.
 * @property pseudo A pseudo name associated with the account.
 * @property password A password associated with the account.
 * @property question A security question associated with the account.
 * @property answer A security question answer associated with the account.
 * @property roles A user role associated with the account.
 *
 * @constructor Creates an `Account` instance with the specified properties.
 */
data class Account(
    private val id: Int,
    private var username: String,
    private var pseudo: String? = null,
    private var password: String? = null,
    private var question: String? = null,
    private var answer: String? = null,
    private var roles: Role? = null
) {

    /**
     * Retrieves the unique identifier of the account.
     *
     * @return The identifier (id) of the account.
     */
    fun id(): Int {
        return this.id
    }

    fun username(): String {
        return this.username
    }

    fun setUsername(username: String) {
        this.username = username
    }

    fun pseudo(): String? {
        return this.pseudo
    }

    fun setPseudo(pseudo: String) {
        this.pseudo = pseudo
    }

    fun password(): String? {
        return this.password
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun question(): String? {
        return this.question
    }

    fun setQuestion(question: String) {
        this.question = question
    }

    fun answer(): String? {
        return this.answer
    }

    fun setAnswer(answer: String) {
        this.answer = answer
    }

    fun roles(): Role? {
        return this.roles
    }

    fun setRoles(roles: Role) {
        this.roles = roles
    }
}