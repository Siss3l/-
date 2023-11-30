package database.sql


/**
 * Represents the database tables `subarea` and `maps` for storing maps information.
 * It is possible to calculate a `hash` of mapdata to re-decrypt only on a change and
 * some elements such as `SuperArea` may be undeclared.
 */
object SqlMap : org.jetbrains.exposed.sql.Table(name = "maps") {
    val id = integer("id").autoIncrement()
    val date = varchar("date", 50).nullable()
    val h = integer("h")
    val w = integer("w")
    val x = integer("x")
    val y = integer("y")
    val subareaId = integer("subarea_id")
    val indoor = bool("indoor").nullable()
    val key = text("key").nullable()
    val mapdata = text("mapdata")

    override val primaryKey = PrimaryKey(id, name = "PK_Maps_ID")
}

object SqlSubarea : org.jetbrains.exposed.sql.Table(name = "subareas") {
    val id = integer("id")
    val areaId = integer("area_id")
    val name = varchar("name", 200)
    val conquerable = bool("conquerable").nullable()
    val alignment = bool("alignment").nullable()

    override val primaryKey = PrimaryKey(id, name = "PK_Subareas_ID")
}