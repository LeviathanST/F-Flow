package common

import com.mysql.cj.jdbc.MysqlDataSource
import common.EnvLoader
import configs.DatabaseConfig
import javax.sql.DataSource

class Database {
    private val connection: DataSource by lazy {
        val dbConfig = EnvLoader.load(DatabaseConfig::class).getOrElse { throw it }
        Class.forName("com.mysql.cj.jdbc.Driver")
        val url = "jdbc:mysql://${dbConfig.url}/${dbConfig.database}"
        MysqlDataSource().apply {
            setUrl(url)
            user = dbConfig.username
            password = dbConfig.password
        }
    }

    fun connect() = connection
}
