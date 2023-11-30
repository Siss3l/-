import com.zaxxer.hikari.HikariDataSource
import config.Conf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.checkerframework.framework.qual.AnnotatedFor
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import java.io.File
import java.sql.SQLException
import java.util.concurrent.ConcurrentHashMap


/**
 * Allow testing and creation of tables.
 */
internal class DbTest {
    private val pools = ConcurrentHashMap<String, HikariDataSource>()

    @Test
    fun testRun() {
        val db = Database.connect(
            url = "jdbc:${Conf.jdbcType}://${Conf.hostname}:${Conf.dbPort}?sslMode=0",
            driver = Conf.driverClassName, user = Conf.username, password = Conf.password
        )
        File("./src/main/resources/db/migration/static/").walkTopDown().forEach {
            if (it.isFile) {
                it.readText().split(";").map { k -> k.trim() }.forEach {
                    transaction {
                        TransactionManager.current().exec(it)
                    }
                }
            }
        }
        println("Finished $pools")
        TransactionManager.closeAndUnregister(db)
    }

    /**
     * Load all table names from `existent` database.
     *
     * @throws SQLException if there is an issue loading table names.
     */
    @Test
    @Throws(SQLException::class)
    fun load() {
        try {
            Database.connect(
                url = "jdbc:${Conf.jdbcType}://${Conf.hostname}:${Conf.dbPort}/${Conf.dbName}?sslMode=0",
                driver = Conf.driverClassName, user = Conf.username, password = Conf.password
            )
            transaction {
                println(TransactionManager.current().db.dialect.allTablesNames())
            }
        } catch (e: Exception) {
            println(e)
        }
    }

    /**
     * Execute a database query within a transaction, on a background thread using `coroutines`.
     *
     * @param block The query block to execute within the transaction.
     * @return The result of the query as specified by the block.
     */
    @Suppress("unused")
    @AnnotatedFor
    internal suspend fun <T> dbQueries(block: () -> T): T = withContext(Dispatchers.IO) { transaction { block() } }
}