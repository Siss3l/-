package database.sql


/**
 * Represents the database table `accounts` for storing user account information.
 * This is not the same usage as `IntIdTable` open class.
 */
object SqlAccount : org.jetbrains.exposed.sql.Table(name = "accounts") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 36).uniqueIndex()
    val pseudo = varchar("pseudo", 36).nullable()
    val password = varchar("password", 100).nullable()
    val question = varchar("question", 64).nullable()
    val answer = varchar("answer", 256).nullable()
    val roles = enumerationByName("roles", 3, game.role.Role::class).nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_Accounts_ID")
}