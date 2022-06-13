package app.database

import org.jetbrains.exposed.sql.Database

object DatabaseConnection {
    val db: Database =
        Database.connect("jdbc:mariadb://localhost:3306/javalin_db", "org.mariadb.jdbc.Driver", "root", "root");

    fun connect(): Database {
        return db;
    }
}
