package database.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import config.Conf
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database.Companion.connect


/**
 * The `DbFactory` object is responsible for managing database connections
 * using `HikariCP` and handling database migration with `Flyway` tool.
 */
object DbFactory {
    private lateinit var pool: HikariDataSource

    init {
        try {
            pool = hikari()
            if (pool.isRunning) {
                println("Starting Database at ${pool.jdbcUrl}")
                Flyway.configure().dataSource(
                    "jdbc:${Conf.jdbcType}://${Conf.hostname}:${Conf.dbPort}/${Conf.dbName}?sslMode=0",
                    Conf.username, Conf.password
                ).load().migrate()
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    /**
     * The `shut` function closes the database connection pool stream
     * and releases any system resources associated with it.
     * If the stream is already closed then invoking this method has no effect.
     */
    fun shut() {
        if (!pool.connection.isClosed) {
            pool.connection.close()
            println("PoolConnection closed.")
        }
    }

    /**
     * Closes the MariaDB database `pool` stream.
     */
    fun closePool() {
        if (!pool.isClosed) {
            pool.close()
        }
    }

    /**
     * Initialize and configure the `HikariCP` database connection pool.
     *
     * No need to open the stream online, could authenticate using `ssh`.
     * @return A configured `HikariCP` database connection pool.
     */
    private fun hikari(): HikariDataSource = HikariDataSource(HikariConfig().apply {
        connectionTimeout = Conf.connectionTimeout
        dataSourceProperties.setProperty("alwaysSendSetIsolation", "true")
        dataSourceProperties.setProperty("cacheCallableStmts", "true")
        dataSourceProperties.setProperty("cachePrepStmts", "true")
        dataSourceProperties.setProperty("cacheResultSetMetadata", "true")
        dataSourceProperties.setProperty("cacheServerConfiguration", "true")
        dataSourceProperties.setProperty("elideSetAutoCommits", "true")
        dataSourceProperties.setProperty("maxLifetime", "3600000")
        dataSourceProperties.setProperty("prepStmtCacheSize", "250")
        dataSourceProperties.setProperty("prepStmtCacheSqlLimit", "2048")
        dataSourceProperties.setProperty("setMaxWaitMillis", "2000")
        dataSourceProperties.setProperty("useLocalSessionState", "true")
        dataSourceProperties.setProperty("useServerPrepStmts", "true")
        driverClassName = Conf.driverClassName
        jdbcUrl = "jdbc:${Conf.jdbcType}://${Conf.hostname}:${Conf.dbPort}/${Conf.dbName}?sslMode=0"
        maximumPoolSize = Conf.maximumPoolSize
        password = Conf.password
        username = Conf.username
    })

    /**
     * Initialize the default database `connection` pool.
     * May have too many connections.
     */
    fun init() {
        try {
            connect(pool)
        } catch (e: Exception) {
            println(e)
        }
    }
}