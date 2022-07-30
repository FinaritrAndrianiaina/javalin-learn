package app.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.StdOutSqlLogger

object DatabaseConnection {
    private val databaseConfig = DatabaseConfig.invoke {
        sqlLogger = StdOutSqlLogger
    }

    val db: Database =
        Database.connect(
            "jdbc:mariadb://localhost:3306/javalin_db",
            "org.mariadb.jdbc.Driver",
            "root",
            "root",
            databaseConfig = databaseConfig
        );

    fun connect(): Database {
        return db;
    }
}
