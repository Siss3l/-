package database.sql


/**
 * Represents the database table `characters` for storing character information.
 * To use `datetime` date, we could use `exposed-java-time` extension.
 */
object SqlCharacter : org.jetbrains.exposed.sql.Table(name = "characters") {
    val id = integer("id").autoIncrement()
    val accountId = reference("account_id", SqlAccount.id).nullable()
    val level = integer("level").nullable()
    val name = varchar("name", 36)
    val gender = integer("gender").nullable()
    val colors = text("colors").nullable()
    val gfxId = integer("gfx_id").nullable()
    val hat = integer("hat").nullable()
    val date = varchar("date", 50).nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_Characters_ID")
}