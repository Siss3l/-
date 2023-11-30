package config

import kotlinx.serialization.Serializable


/**
 * Serializable data class representing JSON configuration information.
 *
 * @property client Information about the client application.
 * @property db Information about the database connection.
 * @property tcp Information about TCP communication settings.
 */
@Serializable
data class JsonInfo(val client: ClientInfo = ClientInfo(), val db: DbInfo = DbInfo(), val tcp: TcpInfo = TcpInfo()) {
    /**
     * Serializable data class representing client-specific information.
     *
     * @property version The version of the client application.
     */
    @Serializable
    data class ClientInfo(
        internal val version: String = ""
    )

    /**
     * Serializable data class representing database connection information.
     *
     * @property connectionTimeout The connection timeout in milliseconds.
     * @property dbName The name of the database.
     * @property dbPort The database port number.
     * @property driverClassName The driver class name for database connectivity.
     * @property hostname The hostname of the database server.
     * @property jdbcType The JDBC type for database connectivity.
     * @property maximumPoolSize The maximum pool size for database connections.
     * @property password The database password.
     * @property username The database username.
     */
    @Serializable
    data class DbInfo(
        internal val connectionTimeout: Long = 250,
        internal val dbName: String = "",
        internal val dbPort: Int = 0,
        internal val driverClassName: String = "",
        internal val hostname: String = "",
        internal val jdbcType: String = "",
        internal val maximumPoolSize: Int = 100,
        internal val password: String = "",
        internal val username: String = ""
    )

    /**
     * Serializable data class representing TCP communication settings.
     *
     * @property authPort The authentication port number.
     * @property gamePort The game port number.
     * @property host The host information for TCP communication.
     */
    @Serializable
    data class TcpInfo(
        internal val authPort: Int = 0,
        internal val gamePort: Int = 1,
        internal val host: String = ""
    )
}

/**
 * Configuration object that reads and provides access to JSON configuration settings.
 */
object Conf {
    private val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
    private val conf =
        json.decodeFromString<List<JsonInfo>>(java.io.File("./src/main/resources/config.json").readText())
    val authPort = conf[2].tcp.authPort
    val connectionTimeout = conf[1].db.connectionTimeout
    val dbName = conf[1].db.dbName
    val dbPort = conf[1].db.dbPort
    val driverClassName = conf[1].db.driverClassName
    val gamePort = conf[2].tcp.gamePort
    val host = conf[2].tcp.host
    val hostname = conf[1].db.hostname
    val jdbcType = conf[1].db.jdbcType
    val maximumPoolSize = conf[1].db.maximumPoolSize
    val password = conf[1].db.password
    val username = conf[1].db.username
    val version = conf[0].client.version
}