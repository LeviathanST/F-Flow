package configs

import java.sql.Connection
import java.sql.DriverManager
import kotlin.runCatching

class Database {
    public fun connect(): Result<Connection> {
        Class.forName("com.mysql.cj.jdbc.Driver")
        return runCatching {
            DriverManager.getConnection("jdbc:mysql://db:3306/mm", "root", "root")
        }
    }
}
